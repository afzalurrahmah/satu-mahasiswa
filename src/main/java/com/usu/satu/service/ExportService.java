package com.usu.satu.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.usu.satu.dto.StudentCourse;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.repository.StudyCardRepository;
import com.usu.satu.template.StudyCardPlanTemplate;
import com.usu.satu.template.StudyCardResultTemplate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ExportService {

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StudyCardService studyCardService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StudentService studentService;

    @Autowired
    FormatData formatData;

    @Autowired
    PeriodService periodService;

    public ByteArrayInputStream printKRS(String nim, String periodId) {
        Optional<StudyCard> optional = studyCardRepository.findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(nim, periodId);

        if (optional.isEmpty()) {
            throw new AcceptedException("study card "+nim+" is not exist");
        } else {
            StudyCard studyCard = optional.get();
            Student student = studentRepository.getMatchStudyCard(studyCard.getNim(), studyCard.getPeriodTaken(), studyCard.getScheduleId());

            List<StudyCard> studyCardList = new ArrayList<>();
            studyCardList.add(studyCard);
            HashMap<String,String> itemContentList = generateContentKRS(student);
            String template = StudyCardPlanTemplate.TEMPLATE;
            String html = templating(student, template, itemContentList, "");
            return this.generatePdf(html);
        }
    }

    public ByteArrayInputStream printKHS(String nim, String periodId) {
        Optional<StudyCard> optional = studyCardRepository.findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(nim, periodId);

        if (optional.isEmpty()) {
            throw new AcceptedException("study card "+nim+" is not exist");
        } else {
            StudyCard studyCard = optional.get();
            Student student = studentRepository.getMatchStudyCard(studyCard.getNim(), studyCard.getPeriodTaken(), studyCard.getScheduleId());

            List<StudyCard> studyCardList = new ArrayList<>();
            studyCardList.add(studyCard);
            HashMap<String,String> itemContentList = generateContentKHS(student);
            String template = StudyCardResultTemplate.TEMPLATE;
            String html = templating(student, template, itemContentList, studyCard.getScheduleId());
            return this.generatePdf(html);
        }
    }

    public String templating(Student student, String template, HashMap<String,String> itemContentList, String scheduleId) {
        // add logo usu
        URL url = getClass().getResource("/static/logo-usu.png");
        String logoUsu = "";
        if (url != null) {
            logoUsu = url.toString();
        }

//        HashMap<String,String> itemContentList = generateContentKRS(student);
        String content = itemContentList.get("content");
        String credits = itemContentList.get("credits");
        String gradeTotal = itemContentList.get("grade_value_total");

        HashMap<String, Object> majorDetail = siaGenerate.majorDetail(student.getMajorCode());
        String majorName = majorDetail.get("major_name").toString();
        String degree    = majorDetail.get("education_level").toString();
        String facultyId = majorDetail.get("faculty_unit_id").toString();

        HashMap<String, Object> facultyDetail = siaGenerate.facultyDetail(facultyId);
        String facultyName = facultyDetail.get("name").toString();

        String lectureId = student.getAcademicLectures().get(student.getAcademicLectures().size()-1).getLectureId();
        HashMap<String, Object> lectureDetail = formatData.getUserDetail(lectureId);

        String frontDegree;
        if (lectureDetail.get("front_degree").toString().isEmpty() || lectureDetail.get("front_degree").toString().equals("")) {
            frontDegree = "";
        } else {
            frontDegree = lectureDetail.get("front_degree").toString()+" ";
        }

        String behindDegree;
        if (lectureDetail.get("behind_degree").toString().isEmpty() || lectureDetail.get("behind_degree").toString().equals("")) {
            behindDegree = "";
        } else {
            behindDegree = ", "+lectureDetail.get("behind_degree").toString();
        }

        // current date indonesia //
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy",new Locale("id", "ID"));
        Date date = new Date();

        HashMap<String,Object> periodDetail = siaGenerate.getDetailPeriod(student.getStudyCard().getPeriodTaken(), student.getMajorCode());

        HashMap<String,String> tempIPK = studyCardService.getAllGrade(student.getNim(), scheduleId);
        String ipk = tempIPK.get("ipk");
        double ipBefore = formatData.getIPBefore(student.getNim(), student.getStudyCard().getPeriodTaken());

        HashMap<String, Object> tempIP = studyCardService.getActStudy(student.getNim());
        String ips = String.valueOf(tempIP.get("ip"));

        // count max credit this period based ip //
        int maxCredit = siaGenerate.getMaxCredit(ipBefore, student.getNim());
        int maxNextCredit = siaGenerate.getMaxCredit(Double.parseDouble(ips), student.getNim());

        template = template.replace("{logo_usu}", logoUsu);
        template = template.replace("{student_nim}", student.getNim());
        template = template.replace("{student_name}", student.getName());
        template = template.replace("{faculty_name}", facultyName);
        template = template.replace("{study_program_name}", majorName);
        template = template.replace("{period_name}", periodDetail.get("name").toString());
        template = template.replace("{degree}", degree);
        template = template.replace("{lecture_PA_name}", frontDegree+lectureDetail.get("full_name").toString()+behindDegree);
        template = template.replace("{table_content_list}", content);
        template = template.replace("{credit_total}", credits);
        template = template.replace("{credit_grade}", gradeTotal);
        template = template.replace("{credit_maks}", String.valueOf(maxCredit));
        template = template.replace("{credit_next_maks}", String.valueOf(maxNextCredit));
        template = template.replace("{ip_period_before}", String.valueOf(ipBefore));
        template = template.replace("{ip_now}", ips);
        template = template.replace("{ip_kumulatif}", ipk);
        template = template.replace("{study_card_date}", formatter.format(date));

        return template;
    }

    public ByteArrayInputStream generatePdf(String template){
        try {
            ConverterProperties properties = new ConverterProperties();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
            pdfDocument.setDefaultPageSize(PageSize.A4);
            HtmlConverter.convertToPdf(template, pdfDocument, properties);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public HashMap<String,String> generateContentKRS(Student student) {
        String[] itemRow = {""};

        List<StudentCourse> studentCourses = student.getStudyCard().getStudentCourses();

        List<String> classes = new ArrayList<>();
        studentCourses.forEach(e -> classes.add(e.getClassId()));

        List<HashMap<String, Object>> hashMapList = siaGenerate.getDetailCourses(student.getStudyCard().getScheduleId(), classes.toString());

        final int[] no = {1};
        final Integer[] creditTotal = {0};
        hashMapList.forEach(item -> {
            JSONObject jsonClass = new JSONObject(item.get("class_info").toString());
            JSONObject jsonRoom = new JSONObject(item.get("room_info").toString());
            JSONObject jsonWeight = jsonClass.getJSONObject("class_weight");

            String classCode = jsonClass.getString("class_code");
            String className = jsonClass.getString("class_name");
            String roomName = jsonRoom.getString("room_name");
            String credit = String.valueOf(jsonWeight.get("credit"));
            creditTotal[0] = creditTotal[0] + Integer.parseInt(credit);


            itemRow[0] += "<tr>\n" +
                    "                         <td style=\"text-align: center;\" class=\"table-course\">"+ no[0] +"</td>\n" +
                    "                         <td class=\"table-course\">"+roomName+"</td>\n" +
                    "                         <td class=\"table-course\">"+classCode+"</td>\n" +
                    "                         <td class=\"table-course\">"+className+"</td>\n" +
                    "                         <td style=\"text-align: center;\" class=\"table-course\">"+credit+"</td>\n" +
                    "                     </tr>";
            no[0]++;
        });
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("credits", String.valueOf(creditTotal[0]));
        hashMap.put("content", itemRow[0]);
        return hashMap;
    }

    public HashMap<String,String> generateContentKHS(Student student) {
        String[] itemRow = {""};

        List<StudentCourse> studentCourses = student.getStudyCard().getStudentCourses();

        List<String> classes = new ArrayList<>();

        studentCourses.forEach(e->{
            classes.add(e.getClassId());
        });

        List<HashMap<String, Object>> hashMapList = siaGenerate.getDetailCourses(student.getStudyCard().getScheduleId(), classes.toString());

        final int[] no = {1};
        final Integer[] creditTotal = {0};
        final double[] valTotal = {0};
        hashMapList.forEach(item -> {
            JSONObject jsonClass = new JSONObject(item.get("class_info").toString());
            JSONObject jsonRoom = new JSONObject(item.get("room_info").toString());
            JSONObject jsonWeight = jsonClass.getJSONObject("class_weight");

            String classCode = jsonClass.getString("class_code");
            String className = jsonClass.getString("class_name");
            String roomName = jsonRoom.getString("room_name");
            String credit = String.valueOf(jsonWeight.get("credit"));

            HashMap<String,Object> hashMap = formatData.contentKHS(student.getNim(), student.getStudyCard().getScheduleId(), item.get("id").toString());

            creditTotal[0] = creditTotal[0] + Integer.parseInt(credit);
            double weight = Double.parseDouble(String.valueOf(hashMap.get("weight")));
            double score = weight*(Double.parseDouble(credit));


            itemRow[0] += "<tr>\n" +
                    "                        <td style=\"text-align: right;\" class=\"table-course\">"+no[0]+"</td>\n" +
                    "                        <td class=\"table-course\" style=\"text-align: left;\">"+classCode+"</td>\n" +
                    "                        <td class=\"table-course\" style=\"text-align: left;\">"+className+"</td>\n" +
                    "                        <td class=\"table-course text-right\">"+credit+"</td>\n" +
                    "                        <td class=\"table-course text-right\"></td>\n" +
                    "                        <td style=\"text-align: center;\" class=\"table-course\">"+hashMap.get("grade")+"</td>\n" +
                    "                        <td class=\"table-course text-right\">"+weight+"</td>\n" +
                    "                        <td class=\"table-course text-right\">"+score+"</td>\n" +
                    "                    </tr>";
            no[0]++;
            valTotal[0] +=score;
        });
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("credits", String.valueOf(creditTotal[0]));
        hashMap.put("content", itemRow[0]);
        hashMap.put("grade_value_total", String.valueOf(valTotal[0]));
        return hashMap;
    }
}
