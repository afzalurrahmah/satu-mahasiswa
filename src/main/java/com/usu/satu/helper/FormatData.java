package com.usu.satu.helper;

import com.usu.satu.dto.*;
import com.usu.satu.model.Billing;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudyCardRepository;
import com.usu.satu.service.StudentService;
import com.usu.satu.service.StudyCardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class FormatData {

    @Value("${server.asa}")
    String asaServer;

    @Value("${server.billing}")
    String billingServer;

    @Autowired
    StudyCardService studyCardService;

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    SiaStatus siaComponent;

    //    String billingServer    = "https://tagihan.usu.ac.id";
//    String asaServer        = "https://api-asa.usu.ac.id";
    String userServer       = "https://api.usu.ac.id/0.1/users/";

    public JSONObject restTemplate(String url) {
        RestTemplate restTemplate   = new RestTemplate();
        HttpHeaders headers         = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity   = new HttpEntity<>(headers);
        String request              = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        return new JSONObject(request);
    }

    public String getCurrentTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public LocalDateTime getNowLocal() {
        return LocalDateTime.now();
    }

    public LocalDateTime stringToLocalDateTime (String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);

        return dateTime;
    }

    public JSONArray getDataAsa(String nim, String ssoToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ssoToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String request = restTemplate.exchange(asaServer+"/requested-letters?requester="+nim+"&sort_field=_id&sort_order=-1", HttpMethod.GET, entity, String.class).getBody();

        JSONObject result = new JSONObject(request);

        return result.getJSONArray("payload");
    }

    public JSONArray getDataBilling(String nim) {
        RestTemplate restTemplate   = new RestTemplate();
        HttpHeaders headers         = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity   = new HttpEntity<>(headers);
        String request              = restTemplate.exchange(billingServer+"/api/billing?items.item_name="+nim, HttpMethod.GET, entity, String.class).getBody();
        JSONObject result           = new JSONObject(request);
        JSONObject jsonObject       = result.getJSONObject("data");

        return jsonObject.getJSONArray("billings");
    }

    public static class sortClassStart implements Comparator<DetailCourseSchedule> {
        public int compare(DetailCourseSchedule a1, DetailCourseSchedule a2)
        {
            return a1.getClassStart().compareTo(a2.getClassStart());
        }
    }

    public static class sortCreatedAtStatus implements Comparator<StatusList> {

        public int compare(StatusList a, StatusList b)
        {
            return a.getUpdatedAt().compareTo(b.getUpdatedAt());
        }
    }

    public static class sortCreatedAtBilling implements Comparator<Billing> {

        public int compare(Billing a, Billing b)
        {
            if (b.getDatetimeCreated() != null && a.getDatetimeCreated() != null) {
                return b.getDatetimeCreated().compareTo(a.getDatetimeCreated());
            } else {
                return -1;
            }
        }
    }

    public static class sortCreatedAtPA implements Comparator<AcademicLecture> {

        public int compare(AcademicLecture a, AcademicLecture b)
        {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

    public static class sortCreatedAtStudyCard implements Comparator<StudyCard> {

        public int compare(StudyCard a, StudyCard b)
        {

            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

    public HashMap<String, Object> jsonObjToHashmap(JSONObject json) {
        HashMap<String, Object> map = new HashMap<>();
        json.keySet().forEach(key -> {
            if (json.isNull(key)) {
                map.put(key, null);
            } else if(json.optJSONObject(key) != null) {
                HashMap<String, Object> keyMap = new HashMap<>();
                map.put(key, keyMap);
            } else if(json.optJSONArray(key) != null) {
                JSONArray array = json.getJSONArray(key);
                List<HashMap<String, Object>> arrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject childJson = array.getJSONObject(i);
                    HashMap<String, Object> childMap = new HashMap<>();
                    arrayList.add(childMap);
                }
                map.put(key, arrayList);
            } else {
                map.put(key, json.get(key));
            }
        });
        return map;
    }

    public int getTotalPkaStudent(List<PeriodList> periodLists) {
//        AtomicInteger intPka;
//        if (periodLists == null) {
//            intPka = new AtomicInteger(0);
//        } else {
//            intPka = new AtomicInteger();
//            periodLists.forEach(e->{
//                e.getStatusLists().forEach(status->{
//                    if (status.getStatus().toUpperCase(Locale.ROOT).equalsIgnoreCase("pka")) {
//                        intPka.getAndIncrement();
//                    }
//                });
//            });
//        }
//
//        return intPka.intValue();

        return 0;
    }

    public double getIPBefore(String nim, String periodId){

        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByNimAndIsDeletedIsFalse(nim);
        studyCardList.sort(new FormatData.sortCreatedAtStudyCard());

        int index = 0;
        for (int i=0;i<studyCardList.size();i++) {
            if (studyCardList.get(i).getPeriodTaken().equalsIgnoreCase(periodId)){
                index = i;
            }
        }

        if (index > 0) {
            StudyCard krsBefore = studyCardList.get(index-1);
            System.out.println("krsBefore : " + krsBefore);

            HashMap<String, Object> hashMapGetIP = studyCardService.getStudyPeriod(krsBefore);

            return Double.parseDouble(hashMapGetIP.get("ip").toString());
        } else {
            return 0;
        }
    }

    public HashMap<String,Object> getUserDetail(String id) {
        JSONObject result   = restTemplate(userServer + "/" + id);
        JSONObject jsonData  = result.getJSONObject("data");
        HashMap<String, Object> hashMap = new HashMap<>();
        jsonData.keys().forEachRemaining(key->{
            if (jsonData.isNull(key)){
                hashMap.put(key, "");
            } else{
                hashMap.put(key, jsonData.get(key));
            }
        });

        return hashMap;
    }

    public double gradeToWeight(String grade) {
        double value;
        if (grade.equalsIgnoreCase("A")) {
            value = 4.00;
        } else if (grade.equalsIgnoreCase("B+")) {
            value = 3.50;
        } else if (grade.equalsIgnoreCase("B")) {
            value = 3.00;
        } else if (grade.equalsIgnoreCase("C+")) {
            value = 2.50;
        } else if (grade.equalsIgnoreCase("C")) {
            value = 2.00;
        } else if (grade.equalsIgnoreCase("D")) {
            value = 1.00;
        } else if (grade.equalsIgnoreCase("E") || grade.equalsIgnoreCase("F") || grade.equalsIgnoreCase("T")) {
            value = 0.00;
        } else {
            value = 0.00;
        }

        return value;
    }

    public String weightToGrade(double grade) {
        String value = null;
        if (grade == 4) {
            value = "A";
        } else if (grade == 3.5) {
            value = "B+";
        } else if (grade == 3) {
            value = "B";
        } else if (grade == 2.5) {
            value = "C+";
        } else if (grade == 2) {
            value = "C";
        } else if (grade == 1) {
            value = "D";
        } else if (grade == 0) {
            value = "E";
        }
        return value;
    }

    public String gradeScore(float score) {
        String valGrade = null;
        if (score < 41) {
            valGrade = "E";
        } else if (score >= 41 && score < 51) {
            valGrade = "D";
        } else if (score >= 51 && score < 59) {
            valGrade = "C";
        } else if (score >= 59 && score < 66) {
            valGrade = "C+";
        } else if (score >= 66 && score < 74) {
            valGrade = "B";
        } else if (score >= 74 && score < 81) {
            valGrade = "B+";
        } else if (score >= 81 && score <= 100) {
            valGrade = "A";
        }

        return valGrade;
    }

    public float getValueGrade(Grade grade, String scheduleId, String classId) {
        HashMap<String,String> weightLecture = siaGenerate.getCourseWeight(scheduleId, classId);
        float result =  (Float.parseFloat(weightLecture.get("final_test"))  * grade.getFinalTest() / 100) +
                (Float.parseFloat(weightLecture.get("mid_test"))    * grade.getMidTest() / 100) +
                (Float.parseFloat(weightLecture.get("quiz"))        * grade.getQuiz() / 100) +
                (Float.parseFloat(weightLecture.get("case_study"))  * grade.getCaseStudy() / 100) +
                (Float.parseFloat(weightLecture.get("face_to_face"))  * grade.getFaceToFace() / 100) +
                (Float.parseFloat(weightLecture.get("field_practical"))  * grade.getFieldPractical() / 100) +
                (Float.parseFloat(weightLecture.get("practical"))  * grade.getPractical() / 100) +
                (Float.parseFloat(weightLecture.get("simulation"))  * grade.getSimulation() / 100) +
                (Float.parseFloat(weightLecture.get("discussion"))  * grade.getDiscussion() / 100);

//        String valGrade = gradeScore(result);
//        grade.setResult(valGrade);

        return result;
    }

    public String countGrade(Grade grade, String scheduleId, String classId) {
        float valGrade  = getValueGrade(grade, scheduleId, classId);
        String score    = gradeScore(valGrade);
        grade.setResult(score);

        return score;
    }

    public double countIP(List<HashMap<String,Object>> data, String scheduleId) {
        System.out.println("data : "+data);
        double score = 0;
        double creditTotal = 0;

        for (HashMap<String,Object> item:data){
            Grade valGrade = (Grade) item.get("grade");
            String grade = countGrade(valGrade == null ? new Grade() : valGrade, scheduleId, item.get("id").toString());

            double credit = Double.parseDouble(item.get("credit").toString());
            double value = credit*(gradeToWeight(grade));

            creditTotal+=credit;
            score+=value;
        }
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("grade", String.valueOf(score));
        hashMap.put("credit", String.valueOf(creditTotal));

        return countGradeAndCreditTotal(hashMap);
    }

    public double countGradeAndCreditTotal(HashMap<String,String> gradeAndCredit) {
        String strGrade = gradeAndCredit.get("grade"); // angka //
        double grade = Double.parseDouble(strGrade);

        String strCredit = gradeAndCredit.get("credit");
        double credit = Double.parseDouble(strCredit);

        return Math.round( (grade/credit) * 100.0) / 100.0;
    }

    public HashMap<String,Object> contentKHS(String nim, String scheduleId, String classId) {
        Optional<StudyCard> optionalStudyCard = studyCardRepository.findStudyCardByNimAndScheduleIdAndIsDeletedIsFalse(nim, scheduleId);
        StudyCard studyCard = optionalStudyCard.get();

        final Grade[] grade = new Grade[1];
        studyCard.getStudentCourses().forEach(e->{
            if (e.getClassId().equalsIgnoreCase(classId)) {
                grade[0] = e.getGrade();
            }
        });

        // get ke berapa

        // get nilai huruf
        String valGrade = countGrade(grade[0], scheduleId, classId);

        // get weight
        double weight = gradeToWeight(valGrade);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("grade", valGrade);
        hashMap.put("weight", weight);

        return hashMap;
    }
}