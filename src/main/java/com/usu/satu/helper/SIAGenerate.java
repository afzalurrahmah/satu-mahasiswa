package com.usu.satu.helper;

import com.usu.satu.dto.Grade;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.service.StudyCardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class SIAGenerate {

//    @Value("${server.sia}")
//    private String siaServer;

    @Autowired
    StudyCardService studyCardService;

    @Autowired
    FormatData formatData;

//    FormatData formatData = new FormatData();

    String siaServer = "https://akademik.usu.ac.id/api/";

    public HashMap<String, Object> getDetailPeriod(String periodId, String majorCode) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/semester?major=" + majorCode);

        if (result.isNull("data")) {
            throw new AcceptedException("period detail is not exist in major code "+majorCode);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        HashMap<String, Object> hashMapDetail = new HashMap<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            if (jsonObject.get("id").equals(periodId)) {
                jsonObject.keys().forEachRemaining(key->{
                    if (jsonObject.isNull(key)){
                        hashMapDetail.put(key, "");
                    } else{
                        hashMapDetail.put(key, jsonObject.get(key));
                    }
                });
            }
        }
        return hashMapDetail;
    }

    public List<HashMap<String, Object>> listPeriod(String majorCode) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/semester?major=" + majorCode);

        if (result.isNull("data")) {
            throw new AcceptedException("period list is not exist in major code "+majorCode);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, Object>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String, Object> periodHash = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    periodHash.put(key, "");
                } else{
                    periodHash.put(key, jsonObject.get(key));
                }
            });

            hashList.add(periodHash);
        }
        return hashList;
    }

    public HashMap<String, Object> periodActive(String majorCode) {
//        JSONObject result   = formatData.restTemplate(siaServer + "satu/major_code/" + majorCode + "/semester");
        JSONObject result   = formatData.restTemplate(siaServer+"satu/semester");

        if (result.isNull("data")) {
            throw new AcceptedException("period active is not exist in major code "+majorCode);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        HashMap<String, Object> periodHash = new HashMap<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            if (jsonObject.get("status").toString().equalsIgnoreCase("active")) {
                jsonObject.keys().forEachRemaining(key->{
                    if (jsonObject.isNull(key)){
                        periodHash.put(key, "");
                    } else{
                        periodHash.put(key, jsonObject.get(key));
                    }
                });
            }
        }
        return periodHash;

//        JSONObject jsonObject  = result.getJSONObject("data");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        jsonObject.keys().forEachRemaining(key->{
//            if (jsonObject.isNull(key)){
//                hashMap.put(key, "");
//            } else{
//                hashMap.put(key, jsonObject.get(key));
//            }
//        });
//        return hashMap;
    }

    public HashMap<String, Object> coursesOfferedList(String major){
        JSONObject result = formatData.restTemplate(siaServer + "satu/major_code/" + major + "/schedule");

        if (result.isNull("data")) {
            throw new AcceptedException("course offered list is not exist in major code "+major);
        }

        JSONObject jsonData = result.getJSONObject("data");
        JSONArray jsonClass = jsonData.getJSONArray("class_schedules");
        JSONObject jsonPeriodDetail = jsonData.getJSONObject("semester");

        HashMap<String, Object> hashMapClass = new HashMap<>();
        List<HashMap<String, Object>> classList = new ArrayList<>();

        hashMapClass.put("id", jsonData.getString("id"));
        hashMapClass.put("major_code", jsonData.getString("major"));
        hashMapClass.put("period_id", jsonPeriodDetail.getString("semester_id"));
        hashMapClass.put("period_name", jsonPeriodDetail.getString("name"));

        for (int i=0;i<jsonClass.length();i++) {
            HashMap<String, Object> classDetail = new HashMap<>();

            JSONObject jsonObject       = jsonClass.getJSONObject(i);
            JSONObject jsonClassDetail  = jsonObject.getJSONObject("class_info");
            JSONObject jsonRoomDetail   = jsonObject.getJSONObject("room_info");

            classDetail.put("offered_class_id", jsonObject.getString("id"));

            if (!jsonClassDetail.isNull("course_code")) {
                classDetail.put("course_code", jsonClassDetail.getString("course_code"));
            } else {
                classDetail.put("course_code", "");
            }

            if (!jsonClassDetail.isNull("course_name")) {
                classDetail.put("course_name", jsonClassDetail.getString("course_name"));
            } else {
                classDetail.put("course_name", "");
            }

            if (!jsonClassDetail.isNull("class_code")) {
                classDetail.put("class_code", jsonClassDetail.getString("class_code"));
            } else {
                classDetail.put("class_code", "null");
            }

            if (!jsonClassDetail.isNull("class_name")) {
                classDetail.put("class_name", jsonClassDetail.getString("class_name"));
            } else {
                classDetail.put("class_name", "");
            }

            if (!jsonClassDetail.isNull("capacity")) {
                classDetail.put("capacity", jsonClassDetail.get("capacity"));
            } else {
                classDetail.put("capacity", "");
            }

            Object classWeight = jsonClassDetail.getJSONObject("class_weight").get("credit");
            if (!classWeight.equals(null)) {
                classDetail.put("credit", classWeight);
            } else {
                classDetail.put("credit", "");
            }

            if (!jsonRoomDetail.isNull("room_code")) {
                classDetail.put("room_code", jsonRoomDetail.getString("room_code"));
            } else {
                classDetail.put("room_code", "");
            }

            if (!jsonRoomDetail.isNull("room_name")) {
                classDetail.put("room_name", jsonRoomDetail.getString("room_name"));
            } else {
                classDetail.put("room_name", "");
            }

            if (!jsonClassDetail.isNull("class_types")) {
                classDetail.put("class_types", jsonClassDetail.getJSONArray("class_types"));
            } else {
                classDetail.put("class_types", "");
            }

            int intTakenCourse;
            String strTakenCourse = studyCardService.getCapacityLimit(jsonPeriodDetail.getString("semester_id"), jsonObject.getString("id"));
            if (strTakenCourse == null) {
                intTakenCourse = 0;
            } else {
                intTakenCourse = Integer.parseInt(studyCardService.getCapacityLimit(jsonPeriodDetail.getString("semester_id"), jsonObject.getString("id")));
            }

            if (intTakenCourse < Integer.parseInt(jsonClassDetail.get("capacity").toString())) {
                classDetail.put("status", "available");
            } else {
                classDetail.put("status", "full");
            }

            classList.add(classDetail);
            hashMapClass.put("class_schedules", classList);
        }

        return hashMapClass;
    }

    public String getCurriculum(String major){
        JSONObject result = formatData.restTemplate(siaServer + "satu/curriculum/" + major + "/latest");

        if (result.isNull("data")) {
            throw new AcceptedException("active curriculum is not exist in major code "+major);
        }

        JSONObject jsonData = result.getJSONObject("data");
        return jsonData.getString("id");
    }

    public HashMap<String,Object> mbkmMapping(String scheduleId, List<HashMap<String,Object>> allClasses) {
        List<HashMap<String,Object>> hashMapListMbkm = new ArrayList<>();
        List<HashMap<String,Object>> hashMapListNotMbkm = new ArrayList<>();

        List<HashMap<String,Object>> allOfStudyCards = new ArrayList<>();
        List<HashMap<String,Object>> allOfGrades = new ArrayList<>();

        List<String> classIdMbkm = new ArrayList<>();
        List<String> classIdNotMbkm = new ArrayList<>();
        HashMap<String,Object> result = new HashMap<>();

        for (HashMap<String,Object> data : allClasses) {
            if (data.get("mbkm").equals(true)) {
                hashMapListMbkm.add(data);
                classIdMbkm.add(data.get("id").toString());
            } else if (data.get("mbkm").equals(false)) {
                hashMapListNotMbkm.add(data);
                classIdNotMbkm.add(data.get("id").toString());
            }
        }

        HashMap<String, Object> resultMbkm = getDetailMbkmList(classIdMbkm, hashMapListMbkm);
        HashMap<String, Object> resultNotMbkm = getDetailNotMbkmList(scheduleId, classIdNotMbkm, hashMapListNotMbkm);

        allOfGrades.addAll((Collection<? extends HashMap<String, Object>>) resultMbkm.get("gradeCredit"));
        allOfGrades.addAll((Collection<? extends HashMap<String, Object>>) resultNotMbkm.get("gradeCredit"));

        allOfStudyCards.addAll((Collection<? extends HashMap<String, Object>>) resultMbkm.get("study_card"));
        allOfStudyCards.addAll((Collection<? extends HashMap<String, Object>>) resultNotMbkm.get("study_card"));

        result.put("study_card", allOfStudyCards);
        result.put("ips", formatData.countIP(allOfGrades, scheduleId));

        return result;
    }

    public HashMap<String, Object> getDetailMbkmList(List<String> classes, List<HashMap<String,Object>> classGrades) {
        String classIdList  = classes.toString().replace("[", "").replace("]", "");
        JSONObject result   = formatData.restTemplate(siaServer + "/satu/mbkm_class/classes?mbkmClassIds=" + classIdList);
        JSONArray jsonData  = result.getJSONArray("data");

        List<HashMap<String, Object>> hashMapListIp = new ArrayList<>();
        List<HashMap<String, Object>> hashMapList = new ArrayList<>();

        HashMap<String, Object> ipData = new HashMap<>();

        for (int i=0;i<jsonData.length();i++) {
            HashMap<String, Object> hashMapDetail = new HashMap<>();
            HashMap<String, Object> hashMapIp = new HashMap<>();

            JSONObject jsonObject = jsonData.getJSONObject(i);
            JSONObject jsonMbkm = jsonObject.getJSONObject("mbkm_info");

            HashMap<String,Object> hashMapClass = new HashMap<>();
            HashMap<String,Object> hashMapSks = new HashMap<>();

            jsonObject.keys().forEachRemaining(key->{
                if(key.equalsIgnoreCase("mbkm_info")){
                    System.out.println();
                } else if(key.equalsIgnoreCase("class_name")) {
                    hashMapClass.put("class_name", jsonObject.get("class_name"));
                    hashMapClass.put("class_code", jsonObject.get("class_code"));
                } else if(key.equalsIgnoreCase("sks")) {
                    hashMapSks.put("credit", jsonObject.get("sks"));
                    hashMapClass.put("class_weight", hashMapSks);
                } else if(key.equalsIgnoreCase("created_at")) {
                    hashMapDetail.put("added_at", jsonObject.get("created_at"));
                }
//                    else{
                hashMapDetail.put("id", jsonObject.get("id"));
                hashMapDetail.put("day", "");
                hashMapDetail.put("class_start", "");
                hashMapDetail.put("class_end", "");

                classGrades.forEach(e->{
                    if (hashMapDetail.get("id") != null){
                        if (e.get("class").toString().equalsIgnoreCase(hashMapDetail.get("id").toString())) {
                            hashMapDetail.put("grade", e.get("grade"));

                            hashMapIp.put("id", jsonObject.get("id"));
                            hashMapIp.put("grade", e.get("grade"));
                            hashMapIp.put("credit", jsonObject.get("sks"));

                        }
                    }
                });
//                    }
                hashMapDetail.put("class_info", hashMapClass);
            });
            hashMapListIp.add(hashMapIp);
            hashMapList.add(hashMapDetail);
        }
//        ipData.put("study_card", hashMapList);

        ipData.put("gradeCredit", hashMapListIp);
        ipData.put("study_card", hashMapList);

        return ipData;
//        return hashMapList;
    }

    public HashMap<String, Object> getDetailNotMbkmList(String scheduleId, List<String> classes, List<HashMap<String,Object>> classGrades) {
        long start = System.currentTimeMillis();

        String classIdList  = classes.toString().replace("[", "").replace("]", "");
        JSONObject result   = formatData.restTemplate(siaServer + "satu/schedule/" +scheduleId+ "/classes?classScheduleIds=" + classIdList);
        JSONArray jsonData  = result.getJSONArray("data");

        HashMap<String, Object> ipData = new HashMap<>();

        List<HashMap<String, Object>> hashMapListIp = new ArrayList<>();
        List<HashMap<String, Object>> hashMapList = new ArrayList<>();

        for (int i=0;i<jsonData.length();i++) {
            HashMap<String, Object> hashMapIp = new HashMap<>();
            HashMap<String, Object> hashMapDetail = new HashMap<>();
            JSONObject jsonObject = jsonData.getJSONObject(i);
            JSONArray lectureInfo = jsonObject.getJSONArray("lecture_info_list");
            JSONArray classLecInfo = jsonObject.getJSONArray("class_lectures");
            JSONObject classInfo = jsonObject.getJSONObject("class_info");
            JSONObject classWeight = classInfo.getJSONObject("class_weight");
            JSONObject roomInfo = jsonObject.getJSONObject("room_info");

            jsonObject.keys().forEachRemaining(key->{

                if (jsonObject.isNull(key) || jsonObject.get(key).toString().equalsIgnoreCase("")){
                    hashMapDetail.put(key, "");
                } else{
                    if (key.equalsIgnoreCase("lecture_info_list")){
                        List<HashMap<String,Object>> hashMapLecInfoList = new ArrayList<>();
                        for (int j=0;j<lectureInfo.length();j++) {
                            JSONObject object = lectureInfo.getJSONObject(j);
                            HashMap<String,Object> hashMapLecInfo = new HashMap<>();
                            object.keys().forEachRemaining(e->hashMapLecInfo.put(e, object.get(e)));
                            hashMapLecInfoList.add(hashMapLecInfo);
                        }
                        hashMapDetail.put("lecture_info_list", hashMapLecInfoList);
                    } else if (key.equalsIgnoreCase("class_lectures")){
                        List<HashMap<String,Object>> hashMapClassLecList = new ArrayList<>();
                        for (int j=0;j<classLecInfo.length();j++) {
                            JSONObject object = classLecInfo.getJSONObject(j);
                            HashMap<String,Object> hashMapClassLec = new HashMap<>();
                            object.keys().forEachRemaining(e->hashMapClassLec.put(e, object.get(e)));
                            hashMapClassLecList.add(hashMapClassLec);
                        }
                        hashMapDetail.put("class_lectures", hashMapClassLecList);
                    } else if(key.equalsIgnoreCase("class_info")){
                        HashMap<String, Object> classInfoHash = new HashMap<>();
                        classInfo.keys().forEachRemaining(classInfoKey-> {
                            if (classInfoKey.equalsIgnoreCase("class_weight")){
                                HashMap<String, Object> classWeightHash = new HashMap<>();
                                classWeight.keys().forEachRemaining(classWeightKey-> classWeightHash.put(classWeightKey, classWeight.get(classWeightKey)));
                                classInfoHash.put("class_weight", classWeightHash);
                            } else{
                                classInfoHash.put(classInfoKey, classInfo.get(classInfoKey));
                            }
                        });
                        hashMapDetail.put("class_info", classInfoHash);
                    } else if(key.equalsIgnoreCase("room_info")){
                        HashMap<String, Object> roomInfoHash = new HashMap<>();
                        roomInfo.keys().forEachRemaining(roomKey-> roomInfoHash.put(roomKey, roomInfo.get(roomKey)));

                        hashMapDetail.put("room_info", roomInfoHash);
                    } else{
                        hashMapDetail.put(key, jsonObject.get(key));
                        classGrades.forEach(e->{
                            if (hashMapDetail.get("id") != null){
                                if (e.get("class").toString().equalsIgnoreCase(hashMapDetail.get("id").toString())) {
                                    hashMapDetail.put("grade", e.get("grade"));

                                    hashMapIp.put("id", jsonObject.get("id"));
                                    Grade grade = (Grade) e.get("grade");
//                                    grade.setResult(formatData.countGrade(grade == null ? new Grade() : grade));
                                    hashMapIp.put("grade", grade);
                                    hashMapIp.put("credit", classWeight.get("credit"));

                                }
                            }
                        });
                    }
                }
            });
            hashMapListIp.add(hashMapIp);
            hashMapList.add(hashMapDetail);
        }

//        ipData.put("ips", formatData.getIp(hashMapListIp));
        ipData.put("gradeCredit", hashMapListIp);
        ipData.put("study_card", hashMapList);

        System.out.println(ipData);

        long end = System.currentTimeMillis();
        System.out.println("Time :"+(end-start));

        return ipData;
//        return hashMapList;
    }

    public List<HashMap<String, Object>> getDetailCourses(String scheduleId, String classes) {
        String classIdList  = classes.replace("[", "").replace("]", "");
        JSONObject result   = formatData.restTemplate(siaServer + "satu/schedule/" +scheduleId+ "/classes?classScheduleIds=" + classIdList);

        if (result.isNull("data")) {
            throw new AcceptedException("course detail is not exist in schedule id "+scheduleId+" and classes "+classes);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, Object>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String, Object> hashMap = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    hashMap.put(key, "");
                } else{
                    hashMap.put(key, jsonObject.get(key));
                }
            });

            hashList.add(hashMap);
        }
        return hashList;
    }

    public int getCredits(String scheduleId, String classId) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/schedule/" +scheduleId+ "/classes?classScheduleIds=" + classId);

        if (result.isNull("data")) {
            throw new AcceptedException("credit is not exist in schedule id "+scheduleId+" and class "+classId);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        int credit = 0;
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            JSONObject classInfo = jsonObject.getJSONObject("class_info");
            JSONObject classWeight = classInfo.getJSONObject("class_weight");
            credit = Integer.parseInt(String.valueOf(classWeight.get("credit")));
        }
        return credit;
    }

    public int getCreditTotalClasses(String scheduleId, String classes) {
        String classIdList  = classes.replace("[", "").replace("]", "");
        JSONObject result   = formatData.restTemplate(siaServer + "satu/schedule/" +scheduleId+ "/classes?classScheduleIds=" + classIdList);

        if (result.isNull("data")) {
            throw new AcceptedException("credit is not exist in schedule id "+scheduleId+" and classes "+classes);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        int credits = 0;
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            JSONObject classInfo = jsonObject.getJSONObject("class_info");
            JSONObject classWeight = classInfo.getJSONObject("class_weight");

            int credit = Integer.parseInt(classWeight.get("credit").toString());
            credits+=credit;
        }
        return credits;
    }

    public List<HashMap<String, Object>> getMBKMList() {
        JSONObject result   = formatData.restTemplate(siaServer + "mbkm_program_list/");

        if (result.isNull("data")) {
            throw new AcceptedException("MBKM list is not exist");
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, Object>> hashMapList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            JSONObject jsonObject = jsonData.getJSONObject(i);
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    hashMap.put(key, "");
                } else{
                    hashMap.put(key, jsonObject.get(key));
                }
            });
            hashMapList.add(hashMap);
        }
        return hashMapList;
    }

    public List<HashMap<String, Object>> getMBKMDetail(String url) {
        JSONObject result   = formatData.restTemplate(siaServer + url);

        if (result.isNull("data")) {
            throw new AcceptedException("MBKM detail is not exist in "+url);
        }

        JSONArray jsonArray = result.getJSONArray("data");
        List<HashMap<String, Object>> hashMapList = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            JSONObject jsonObject   = jsonArray.getJSONObject(i);
            JSONObject jsonInfo     = jsonObject.getJSONObject("mbkm_info");

            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key) || jsonObject.get(key).toString().equalsIgnoreCase("")){
                    hashMap.put(key, "");
                } else{
                    if (key.equalsIgnoreCase("mbkm_info")) {
                        HashMap<String, Object> hashMapInfo = new HashMap<>();
                        jsonInfo.keys().forEachRemaining(infoKey -> hashMapInfo.put(infoKey, jsonInfo.get(infoKey)));

                        hashMap.put("mbkm_info", hashMapInfo);
                    } else {
                        hashMap.put(key, jsonObject.get(key));
                    }
                }
            });
            hashMapList.add(hashMap);
        }
        return hashMapList;
    }

    public List<HashMap<String, Object>> getFaculties() {
        JSONObject result   = formatData.restTemplate(siaServer + "data/faculties");

        if (result.isNull("data")) {
            throw new AcceptedException("faculties is not exist");
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, Object>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String, Object> periodHash = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    periodHash.put(key, "");
                } else{
                    periodHash.put(key, jsonObject.get(key));
                }
            });
            hashList.add(periodHash);
        }
        return hashList;
    }

    public List<HashMap<String, Object>> getMajors(String id) {
        JSONObject result   = formatData.restTemplate(siaServer + "data/major_list?faculty=" + id);

        if (result.isNull("data")) {
            throw new AcceptedException("majors is not exist in faculty id "+id);
        }

        JSONArray jsonData  = result.getJSONArray("data");
        List<HashMap<String, Object>> hashList = new ArrayList<>();
        for (int i=0;i<jsonData.length();i++) {
            JSONObject jsonObject = jsonData.getJSONObject(i);
            HashMap<String, Object> periodHash = new HashMap<>();
            jsonObject.keys().forEachRemaining(key->{
                if (jsonObject.isNull(key)){
                    periodHash.put(key, "");
                } else{
                    periodHash.put(key, jsonObject.get(key));
                }
            });

            hashList.add(periodHash);
        }
        return hashList;
    }

    public HashMap<String, Object> majorDetail(String majorCode) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/majors?code=" + majorCode);

        if (result.isNull("data")) {
            throw new AcceptedException("major detail is not exist in major code "+majorCode);
        }

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

    public HashMap<String, Object> facultyDetail(String facultyId) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/faculty/" + facultyId);

        if (result.isNull("data")) {
            throw new AcceptedException("faculty detail is not exist in faculty id "+facultyId);
        }

        JSONObject jsonData  = result.getJSONObject("data");
        HashMap<String, Object> periodHash = new HashMap<>();
        jsonData.keys().forEachRemaining(key->{
            if (jsonData.isNull(key)){
                periodHash.put(key, "");
            } else{
                periodHash.put(key, jsonData.get(key));
            }
        });
        return periodHash;
    }

    public String facultyUnitId (String majorCode) {
        HashMap<String,Object> hashMap = majorDetail(majorCode);
        return hashMap.get("faculty_unit_id").toString();
    }

    public String facultyName (String facultyUnitId) {
        HashMap<String,Object> hashMap = facultyDetail(facultyUnitId);
        return hashMap.get("name").toString();
    }

    public int getMaxPkaMajor(String major) {
        JSONObject result   = formatData.restTemplate(siaServer + "satu/setting/" + major + "/pka");
        JSONObject jsonData  = result.getJSONObject("data");

        return Integer.parseInt(jsonData.get("max_pka").toString());
    }

    public int getMaxCredit(double gpa, String nim) {
        JSONObject result   = formatData.restTemplate(siaServer + "/satu/setting/credit_limit?gpa=" + gpa + "&nim=" + nim);
        JSONObject jsonData  = result.getJSONObject("data");

        return Integer.parseInt(jsonData.get("max_credits").toString());
    }

    public HashMap<String,String> getCourseWeight(String scheduleId, String classId) {
        JSONObject result   = formatData.restTemplate(siaServer + "/satu/class_weight/"+ scheduleId +"/" + classId);
        JSONObject jsonData  = result.getJSONObject("data");

        HashMap<String, String> hashMap = new HashMap<>();
        jsonData.keys().forEachRemaining(key->{
            if (jsonData.isNull(key)){
                hashMap.put(key, "");
            } else{
                hashMap.put(key, jsonData.get(key).toString());
            }
        });

        return hashMap;
    }
}
