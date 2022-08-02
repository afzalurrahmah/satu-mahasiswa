package com.usu.satu.mapper;

import com.usu.satu.dto.StudentCourse;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.model.StudyCard;
import com.usu.satu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class StudyCardMapperImpl implements StudyCardMapper {

    @Autowired
    FormatData formatData;

    @Autowired
    StudentService studentService;

    @Autowired
    SIAGenerate siaGenerate;

    @Override
    public void updateStudyCard(StudyCard studyCard, StudyCard entity, String periodId) {
        String now = formatData.getCurrentTime();

        if ( studyCard == null ) {
            return;
        }

        if ( studyCard.getId() != null ) {
            entity.setId( studyCard.getId() );
        }

        if( studyCard.getNim() != null ){
            entity.setNim( studyCard.getNim() );
        }else {
            entity.setNim( entity.getNim() );
        }

        if( studyCard.getPeriodTaken() != null ){
            entity.setPeriodTaken( studyCard.getPeriodTaken() );
        }else {
            entity.setPeriodTaken( periodId );
        }

        if( studyCard.getScheduleId() != null ){
            entity.setScheduleId( studyCard.getScheduleId() );
        }else {
            entity.setScheduleId( entity.getScheduleId() );
        }

        ArrayList<StudentCourse> studentCoursesList = studyCard.getStudentCourses();
        ArrayList<StudentCourse> entityList = entity.getStudentCourses();
        if (studentCoursesList != null && entityList == null){   // create //
            studentCoursesList.forEach(e->{
                e.setCreatedAt( now );
            });
            entity.setStudentCourses( studentCoursesList );
        } else {                                                        // update //

            ArrayList<StudentCourse> cards = new ArrayList<>();

            // get same class from old data //
            entityList.removeIf(oldClass->{
               if (studentCoursesList.stream().anyMatch(newClass->newClass.getClassId().equalsIgnoreCase(oldClass.getClassId()))){
                   return false;
               }
                return true;
            });

            // get new class from data input //
            studentCoursesList.removeIf(newClass->{
                if (entityList.stream().anyMatch(oldClass->oldClass.getClassId().equalsIgnoreCase(newClass.getClassId()))){
                    return true;
                }
                return false;
            });

            cards.addAll(entityList);
            cards.addAll(studentCoursesList);

            for (StudentCourse data : cards) {
                StudentCourse studentCourse = data;
                // if data is not existing, set createdAt
                if ( studentCourse.getCreatedAt() == null ) {
                    studentCourse.setCreatedAt( now );
                }
            }
            entity.setStudentCourses( cards );
        }

        entity.setDeleted( false );
    }
}
