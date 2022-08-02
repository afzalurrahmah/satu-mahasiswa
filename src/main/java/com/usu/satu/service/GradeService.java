package com.usu.satu.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.usu.satu.dto.Grade;
import com.usu.satu.dto.StudentCourse;
import com.usu.satu.helper.FormatData;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    FormatData formatData;

    public StudyCard updateGrade(HashMap<String,Object> input){
        Optional<StudyCard> data = studyCardRepository.findById(input.get("study_card_id").toString());

        if(data.isEmpty()){
            return null;
        }else {
            StudyCard studyCardOld = data.get();

            StudyCard studyCardNew = new StudyCard();
            ArrayList<StudentCourse> studentCourseList = new ArrayList<>();

            GsonBuilder gsonBuilder         = new GsonBuilder();
            Gson gson                       = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (StudentCourse course : studyCardOld.getStudentCourses()) {
                StudentCourse studentCourseNew = new StudentCourse();
                if (course.getClassId().equalsIgnoreCase(input.get("class_id").toString())) {
                    Grade grade = gson.fromJson(input.get("grade").toString(), Grade.class);

                    studentCourseNew.setGrade(grade);
                    studentCourseNew.setGradedAt(formatData.getCurrentTime());
                } else {
                    studentCourseNew.setGrade(course.getGrade());
                    studentCourseNew.setGradedAt(course.getGradedAt());
                }
//                studentCourseNew.setApproved(course.isApproved());
                studentCourseNew.setMbkm(course.isMbkm());
                studentCourseNew.setTranscript(course.isTranscript());
                studentCourseNew.setCreatedAt(course.getCreatedAt());
                studentCourseNew.setClassId(course.getClassId());
                studentCourseList.add(studentCourseNew);
            }
            studyCardNew.setNim(studyCardOld.getNim());
            studyCardNew.setPeriodTaken(studyCardOld.getPeriodTaken());
            studyCardNew.setPeriodName(studyCardOld.getPeriodName());
            studyCardNew.setScheduleId(studyCardOld.getScheduleId());
            studyCardNew.setDeleted(studyCardOld.isDeleted());
            studyCardNew.setId(studyCardOld.getId());
            studyCardNew.setStudentCourses(studentCourseList);
            studyCardNew.setUpdatedAt(studyCardOld.getUpdatedAt());
            studyCardNew.setUpdatedBy(studyCardOld.getUpdatedBy());
            studyCardNew.setCreatedAt(studyCardOld.getCreatedAt());
            studyCardNew.setCreatedBy(studyCardOld.getCreatedBy());
            studyCardNew.setProcessedAt(studyCardOld.getProcessedAt());
            studyCardNew.setProcessedByPA(studyCardOld.isProcessedByPA());
            studyCardRepository.save(studyCardNew);
            return studyCardNew;
        }
    }

}
