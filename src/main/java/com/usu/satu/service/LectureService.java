package com.usu.satu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usu.satu.dto.AcademicLecture;
import com.usu.satu.dto.StudentCourse;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.FormatData;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.repository.StudyCardRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;

@Service
public class LectureService {

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StudyCardService studyCardService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FormatData formatData;

    @Autowired
    MongoOperations mongoOperations;

    public HashMap<String,Object> getStudyCardGrade(String userId, String periodId) throws JsonProcessingException {

        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByPeriodTakenAndIsDeletedIsFalse(periodId);

        HashMap<String,Object> hashmapResult = new HashMap<>();
        hashmapResult.put("user_id", userId);
        hashmapResult.put("period_id", periodId);

        List<HashMap<String,Object>> listCourses = new ArrayList<>();
        List<HashMap<String,Object>> listStd     = new ArrayList<>();

        for (StudyCard data: studyCardList) {
            HashMap<String,Object> hashMap = studyCardService.getStudyPeriod(data);

            HashMap<String,Object> hashMapStd = new HashMap<>();
            hashMapStd.put("nim", data.getNim());
            hashMapStd.put("period_name", data.getPeriodName());

            JSONObject json = new JSONObject(hashMap);

            JSONArray objCourse = json.getJSONArray("student_courses");
            for (int i=0;i<objCourse.length();i++) {
                JSONObject jsonObject = objCourse.getJSONObject(i);
                JSONArray jsonArray = jsonObject.getJSONArray("lecture_info_list");

                for (int j=0;j<jsonArray.length();j++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    if (jsonObject1.get("lead").equals(true)) {
                        HashMap<String,Object> result = new ObjectMapper().readValue(String.valueOf(jsonObject), HashMap.class);
                        listCourses.add(result);
                    }
                }
            }
            hashMapStd.put("courses", listCourses);
            listStd.add(hashMapStd);
        }

        hashmapResult.put("student", listStd);

        return hashmapResult;
    }

    public List<HashMap<String, Object>> editGrade (String periodId, String classId, String grade) {

        System.out.println(studyCardRepository.findStudyCardsByPeriodTakenAndIsDeletedIsFalse(periodId));
        System.out.println(grade);

//        List<StudyCard> studyCardList = studyCardRepository.findStudyCardByPeriodTakenAndIsDeletedIsFalse(periodId);
//
//        GsonBuilder gsonBuilder     = new GsonBuilder();
//        Gson gson                   = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
//        Grade valGrade              = gson.fromJson(student.getAddress(), Grade.class);

        return new ArrayList<>();
    }

    public HashMap<String,String> updateAcademicLecture (HashMap<String,String> academic) {
//        List<HashMap<String,Object>> stdIdList = (List<HashMap<String, Object>>) academic.get("students");
        String valLectureId = academic.get("lecturer_id");
        String valPeriodId  = academic.get("semester_id");
        String valNim       = academic.get("student_id");

//        for (HashMap<String,Object> data: stdIdList) {
            Student student = studentRepository.findByNimAndIsDeletedIsFalse(valNim).get();

            List<AcademicLecture> academicLectureList = student.getAcademicLectures();

            AcademicLecture academicLecture = new AcademicLecture();

            if (academicLectureList.isEmpty()) {
                academicLecture.setLectureId(valLectureId);
                academicLecture.setPeriodId(valPeriodId);
                academicLecture.setCreatedAt(new FormatData().getCurrentTime());

                academicLectureList.add(academicLecture);
                student.setAcademicLectures(academicLectureList);

                studentRepository.save(student);
            } else {
                academicLectureList.sort(new FormatData.sortCreatedAtPA());
                AcademicLecture lectureLastIndex = academicLectureList.get(academicLectureList.size()-1);
                if (!lectureLastIndex.getLectureId().equalsIgnoreCase(valLectureId)) {
                    academicLecture.setLectureId(valLectureId);
                    academicLecture.setPeriodId(valPeriodId);
                    academicLecture.setCreatedAt(new FormatData().getCurrentTime());

                    academicLectureList.add(academicLecture);
                    student.setAcademicLectures(academicLectureList);

                    studentRepository.save(student);
                } else {
                    throw new AcceptedException("PA lecture is exist");
                }
            }
//        }
        return academic;
    }

    public List<Student> getAcademicLecture (String lectureId, String periodId) {

        System.out.println(lectureId + " : " + periodId + " : " + studentRepository.findAllByIsDeletedIsFalse());

        List<Student> studentAllList = studentRepository.findAllByIsDeletedIsFalse();
        List<Student> studentList = new ArrayList<>();

        if (studentAllList.isEmpty()) {
            return studentList;
        } else {
            for (Student data : studentAllList) {
                List<AcademicLecture> academicLectureList = data.getAcademicLectures();

                if (!academicLectureList.isEmpty()) {
                    academicLectureList.sort(new FormatData.sortCreatedAtPA());
                    AcademicLecture academicLecture = academicLectureList.get(academicLectureList.size()-1);

                    if (academicLecture.getLectureId().equalsIgnoreCase(lectureId) && academicLecture.getPeriodId().equalsIgnoreCase(periodId)) {
                        studentList.add(data);
                    }
                }
            }
            return studentList;
        }
    }

    public StudyCard removeCourseByPA(String studyCardId, String classId) {
        System.out.println(studyCardId + " : " + classId);

        StudyCard studyCard = studyCardRepository.findByIdAndIsDeletedIsFalse(studyCardId).get();

        List<StudentCourse> studentCourseList = studyCard.getStudentCourses();

//        entityList.removeIf(oldClass->{
//            if (studentCoursesList.stream().anyMatch(newClass->newClass.getClassId().equalsIgnoreCase(oldClass.getClassId()))){
//                return false;
//            }
//            return true;
//        });



        StudentCourse studentCourse = studentCourseList
                .stream()
                .filter(p -> p.getClassId().equalsIgnoreCase(classId))
                .findFirst()
                .get();
        studentCourseList.remove(studentCourse);

        studyCardRepository.save(studyCard);

        return studyCard;
    }

    public StudyCard accStudyCardByPA (String studyCardId) {
        Optional<StudyCard> data = studyCardRepository.findByIdAndIsDeletedIsFalse(studyCardId);
        if (data.isEmpty()) {
            throw new AcceptedException("Study card ID is not exist");
        } else {
            StudyCard studyCard = data.get();

            if (studyCard.isProcessedByPA()) {
                throw new AcceptedException("Study card is approved");
            } else {
                studyCard.setProcessedByPA(true);
                studyCard.setProcessedAt(formatData.getCurrentTime());

                studyCardRepository.save(studyCard);

                return studyCard;
            }
        }
    }

    public List<Student> studentNeedPA (Map<String,String> request) {

        Query qry = new Query();
        List<Criteria> criteria = new ArrayList<>();

        request.forEach((param, value) -> {
            if(param.equalsIgnoreCase("major_code")){
                criteria.add(Criteria.where("major_code").is(value));
            }
            else if(param.equalsIgnoreCase("period")){
                criteria.add(Criteria.where("entry_year").regex(value, "i"));
            }
            else {
                criteria.add(Criteria.where(param).regex(value, "i"));
            }
        });
        criteria.add(new Criteria().andOperator(
                Criteria.where("is_deleted").is(false),
                Criteria.where("academic_lectures").is(new ArrayList<>())
        ));
        qry.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));


//
//        for (Student data : studentList) {
//            if (data.getAcademicLectures().isEmpty() && data.getEntryYear().equalsIgnoreCase(period)) {
//                studentsNeeded.add(data);
//            }
//        }
//
//        if (studentsNeeded.isEmpty()) {
//            throw new AcceptedException("no student needed PA");
//        } else {
            return new ArrayList<>(mongoOperations.find(qry, Student.class));
//        }

//        return new ArrayList<>();
    }
}
