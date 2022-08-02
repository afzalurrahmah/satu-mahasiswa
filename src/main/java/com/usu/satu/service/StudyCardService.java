package com.usu.satu.service;

import com.usu.satu.dto.StudentCourse;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.mapper.StudyCardMapper;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.repository.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Locale;

@Service
public class StudyCardService {

    @Autowired
    PeriodService periodService;

    @Autowired
    StudentService studentService;

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StudyCardMapper studyCardMapper;

    @Autowired
    FormatData formatData;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    StatusService statusService;

    public HashMap<String, Object> getStudyPeriod(StudyCard data){
        HashMap<String, Object> hashMapCourse = new HashMap<>();

        HashMap<String, Object> period = siaGenerate.getDetailPeriod(data.getPeriodTaken(), studentService.getMajorCode(data.getNim()));

        hashMapCourse.put("id", data.getId());
        hashMapCourse.put("nim", data.getNim());
        hashMapCourse.put("period_taken", data.getPeriodTaken());
        hashMapCourse.put("period_name", period.get("name") );
        hashMapCourse.put("schedule_id", data.getScheduleId());
        hashMapCourse.put("period_start", period.get("semester_start") );
        hashMapCourse.put("is_processed_by_pa", data.isProcessedByPA());
        hashMapCourse.put("created_at", data.getCreatedAt());

        List<String> classes = new ArrayList<>();
        List<HashMap<String,Object>> classGradesList = new ArrayList<>();
        data.getStudentCourses().forEach(studentCourse->{
            HashMap<String,Object> classGrade = new HashMap<>();
            classes.add(studentCourse.getClassId());
            classGrade.put("class", studentCourse.getClassId());
            classGrade.put("grade", studentCourse.getGrade());
            classGrade.put("mbkm", studentCourse.isMbkm());
            classGrade.put("id", studentCourse.getClassId());
            classGradesList.add(classGrade);
        });

        HashMap<String,Object> result = siaGenerate.mbkmMapping(data.getScheduleId(), classGradesList);

        hashMapCourse.put("ip", result.get("ips"));
        hashMapCourse.put("student_courses", result.get("study_card"));

        return hashMapCourse;
    }

    public StudyCard saveStudy(StudyCard studyCard){
        StudyCard data = new StudyCard();

        String periodId = periodService.periodActive(studyCard.getNim()).get("id").toString();
        double ip = formatData.getIPBefore(data.getNim(), periodId);

        // count max credit this period based ip //
        int maxCredit = siaGenerate.getMaxCredit(ip, data.getNim());

        List<String> classes = new ArrayList<>();
        studyCard.getStudentCourses().forEach(e->{
            classes.add(e.getClassId());
        });

        // count credit study card taken //
        int countCredit = siaGenerate.getCreditTotalClasses(studyCard.getScheduleId(), classes.toString());

        if (countCredit > maxCredit) {
            throw new AcceptedException("credit is not enough, max total credit is " + maxCredit);
        } else {
            // add status "krs" //
            if (!studyCard.getStudentCourses().isEmpty()){
                Student student = studentService.setLastStatus(studyCard.getNim(), "nim", periodId, "get");
                //create status krs
                String schemaId = siaGenerate.periodActive(student.getMajorCode()).get("sk_number").toString();

                StudentStatus studentStatus = statusService.updateStatus(student.getNim(), periodId, "krs", true, schemaId);
                statusRepository.save(studentStatus);
            }

            studyCardMapper.updateStudyCard(studyCard, data, periodId);
            studyCardRepository.save(data);
            return data;
        }
    }

    public HashMap<String, Object> editStudy(String id, StudyCard studyCard){
        Optional<StudyCard> std = studyCardRepository.findByIdAndIsDeletedIsFalse(id);
        if (std.isPresent()){
            StudyCard data = std.get();
            String periodId = periodService.periodActive(data.getNim()).get("id").toString();

            double ip = formatData.getIPBefore(data.getNim(), periodId);
            System.out.println("ip : "+ip);

            int maxCredit = siaGenerate.getMaxCredit(ip, data.getNim());
            System.out.println("maxCredit : "+maxCredit);

            List<String> classes = new ArrayList<>();
            studyCard.getStudentCourses().forEach(e->{
                classes.add(e.getClassId());
            });

            // count credit study card taken //
            int countCredit = siaGenerate.getCreditTotalClasses(studyCard.getScheduleId(), classes.toString());
            System.out.println("countCredit : " + countCredit);

            if (countCredit > maxCredit) {
                throw new AcceptedException("credit is not enough, max total credit is " + maxCredit);
            } else {
                studyCardMapper.updateStudyCard(studyCard, data, periodId);
            studyCardRepository.save(data);
                return getStudyPeriod(data);
            }
        }else {
            throw new AcceptedException("id "+ id + " is not exist");
        }
    }

    public StudyCard removeStudy(String id){
        Optional<StudyCard> studyCard = studyCardRepository.findByIdAndIsDeletedIsFalse(id);
        if (studyCard.isPresent()){
            StudyCard data = studyCard.get();
            data.setDeleted(true);
            studyCardRepository.save(data);
            return data;
        } else {
            return null;
        }
    }

    public List<StudyCard> getAll(){
        List<StudyCard> data = studyCardRepository.findAllByIsDeletedIsFalse();
        if(data.isEmpty()){
            return null;
        }else {
            return data;
        }
    }

    public List<HashMap<String, Object>> getStudyNim(String nim){
        List<StudyCard> data = studyCardRepository.findStudyCardsByNimAndIsDeletedIsFalse(nim);

        List<HashMap<String, Object>> listCourses = new ArrayList<>();
        if(data.isEmpty()){
            return null;
        }else {
            data.forEach(datum->{
                HashMap<String, Object> hashMapCourse = getStudyPeriod(datum);
                listCourses.add(hashMapCourse);
            });

            return listCourses;
        }
    }

    public List<HashMap<String,Object>> getSchedule(String nim) {

        String periodId = periodService.periodActive(nim).get("id").toString();

        Optional<StudyCard> studyCard = studyCardRepository.findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(nim, periodId);

        if (studyCard.isPresent()) {
            StudyCard data = studyCard.get();
            List<String> arDay = new ArrayList<>();
            arDay.add("monday");
            arDay.add("tuesday");
            arDay.add("wednesday");
            arDay.add("thursday");
            arDay.add("friday");

            List<HashMap<String,Object>> groupDay = new ArrayList<>();

            HashMap<String, Object> hashMapCourse = getStudyPeriod(data);

            List<HashMap<String, Object>> hashMapList = (List<HashMap<String, Object>>) hashMapCourse.get("student_courses");

            System.out.println("hashMapList : "+hashMapList);

            for (int i=0;i<arDay.size();i++) {
                HashMap hashMap = new HashMap();
                hashMap.put("day", arDay.get(i));

                List<HashMap> contentList = new ArrayList<>();

                for (int j=0;j<hashMapList.size();j++) {
                    HashMap detailCourse = hashMapList.get(j);

                    if (arDay.get(i).toLowerCase(Locale.ROOT).equalsIgnoreCase(hashMapList.get(j).get("day").toString().toLowerCase(Locale.ROOT))) {
//                    String[] arTimeStart = detailCourse.get("class_start").toString().split(":");
//                    LocalTime classStart = LocalTime.parse(arTimeStart[0]+":"+arTimeStart[1]+":"+arTimeStart[2]);
//
//                    String[] arTimeEnd = detailCourse.get("class_end").toString().split(":");
//                    LocalTime classEnd = LocalTime.parse(arTimeEnd[0]+":"+arTimeEnd[1]+":"+arTimeEnd[2]);

//                    DetailCourseSchedule detailCourseSchedule1 = new DetailCourseSchedule();
//                    detailCourseSchedule1.setLectureNip(detailCourse.get("lecture_nip").toString());
//                    detailCourseSchedule1.setLectureName(detailCourse.get("lecture_name").toString());
//                    detailCourseSchedule1.setLectureFrontDegree(detailCourse.get("lecture_front_degree").toString());
//                    detailCourseSchedule1.setLectureBehindDegree(detailCourse.get("lecture_behind_degree").toString());
//                    detailCourseSchedule1.setCourseName(detailCourse.get("course_name").toString());
//                    detailCourseSchedule1.setCourseCode(detailCourse.get("course_code").toString());
//                    detailCourseSchedule1.setClassName(detailCourse.get("class_name").toString());
//                    detailCourseSchedule1.setClassCode(detailCourse.get("class_code").toString());
//                    detailCourseSchedule1.setRoomName(detailCourse.get("room_name").toString());
//                    detailCourseSchedule1.setRoomCode(detailCourse.get("room_code").toString());
//                    detailCourseSchedule1.setClassStart(classStart);
//                    detailCourseSchedule1.setClassEnd(classEnd);
//                    detailCourse1.setClassTypes((String[]) detailCourse.get("class_types"));
//                    detailCourseSchedule1.setCredit(Integer.parseInt(detailCourse.get("credit").toString()));

                        contentList.add(detailCourse);

                    }
//                contentList.sort(new FormatData.sortClassStart());
                    hashMap.put("student_courses", contentList);
                }
                groupDay.add(hashMap);
            }

            System.out.println(groupDay);

            return groupDay;
        } else {
            throw new AcceptedException("Schedule "+ nim + "is not exist");
        }
    }

    public Optional<StudyCard> getStudyId(String id){
        Optional<StudyCard> data = studyCardRepository.findByIdAndIsDeletedIsFalse(id);
        if(data.isEmpty()){
            return null;
        }else {
            System.out.println(getAllGrade("141402023", "61c071715b2e9b27229b040f"));
            return data;
        }
    }

    public HashMap<String, Object> getStudyPeriodAndNim(String period, String nim) {
        Optional<StudyCard> optional = studyCardRepository.findStudyCardByPeriodTakenInAndNimAndIsDeletedIsFalse(period, nim);
        if (optional.isEmpty()){
            throw new AcceptedException("nim "+nim+" and period id "+period+" not existed");
        }else {
            StudyCard studyCard = optional.get();
            return getStudyPeriod(studyCard);
        }
    }

    public HashMap getCoursesOffered(String nim){
//        HashMap data = formatData.coursesOfferedList(studentService.getMajorCode(nim));
        HashMap data = siaGenerate.coursesOfferedList(studentService.getMajorCode(nim));
//        Object a = studyCardRepository.countCapacity("tes", "test");
//        System.out.println(a);
        if (data.isEmpty()) {
            return null;
        } else {
            return data;
        }
    }

    public String getCapacityLimit(String periodId, String classId) {
        return studyCardRepository.countCapacity(periodId, classId);
    }

    public HashMap getStudyCardBasedPeriod(String nim, String periodId){
        Optional<StudyCard> data = studyCardRepository.findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(nim, periodId);
        if (data.isPresent()) {
            return getStudyPeriod(data.get());
        } else {
            return null;
        }
    }

    public HashMap<String,Object> getActStudy(String nim){
        String periodId = periodService.periodActive(nim).get("id").toString();
        Optional<StudyCard> data = studyCardRepository.findStudyCardByNimAndPeriodTakenAndIsDeletedIsFalse(nim, periodId);
        if (data.isPresent()) {
            return getStudyPeriod(data.get());
        } else {
            return null;
        }
    }

    public HashMap<String,String> getAllGrade(String nim, String scheduleId) {
        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByNimAndIsDeletedIsFalse(nim);
        studyCardList.sort(new FormatData.sortCreatedAtStudyCard());

        int index = 0;
        for (int i=0;i<studyCardList.size();i++) {
            if (studyCardList.get(i).getScheduleId().equalsIgnoreCase(scheduleId)){
                index = i;
            }
        }

//        List<HashMap<String,Object>> tempGradeList = new ArrayList<>();
        final int[] creditTotal = {0};
        float scoreTotal=0;
        for (int j=0;j<=index;j++) {
            for (int k = 0; k<studyCardList.get(j).getStudentCourses().size(); k++) {
//                HashMap<String,Object> tempValue = new HashMap<>();
                StudentCourse studentCourse = studyCardList.get(j).getStudentCourses().get(k);
                String scheduleJ = studyCardList.get(j).getScheduleId();
                float gradeWeight = formatData.getValueGrade(studentCourse.getGrade(), scheduleJ, studentCourse.getClassId());
                String gradeScore = formatData.gradeScore(gradeWeight); // huruf
                double gradeToWeight = formatData.gradeToWeight(gradeScore); // bobot huruf
                int credit = siaGenerate.getCredits(scheduleJ, studentCourse.getClassId());

//                tempValue.put("id", studentCourse.getClassId());
//                tempValue.put("grade", studentCourse.getGrade());
//                tempValue.put("gradeFloat", gradeToWeight);
//                tempValue.put("credit", credit);
//                tempValue.put("scoreTotal", gradeToWeight * credit);
//                tempGradeList.add(tempValue);

                creditTotal[0] += credit;
                scoreTotal += (gradeToWeight * credit);
            }
        }
        HashMap<String,String> gradeTotal = new HashMap<>();
        gradeTotal.put("grade", String.valueOf(scoreTotal));
        gradeTotal.put("credit", String.valueOf(creditTotal[0]));
        gradeTotal.put("ipk", String.valueOf(formatData.countGradeAndCreditTotal(gradeTotal)));
        return gradeTotal;
//        return tempGradeList;
    }
}
