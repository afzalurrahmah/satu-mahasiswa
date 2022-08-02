package com.usu.satu.service;

import com.usu.satu.dto.StudentCourse;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TranscriptService {

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StudyCardService studyCardService;

    // transkrip belum selesai
    // get semua matkul
    // hitung ipk
    public List<HashMap<String, Object>> getTranscript(String nim) {
        List<StudyCard> studyCardList = studyCardRepository.findStudyCardsByNimAndIsDeletedIsFalse(nim);

        if(studyCardList.isEmpty()){
            return null;
        }else {


            // filter isTranscript is true //
//            List<StudyCard> studyCards = new ArrayList<>();
//            for (int i=0;i<studyCardList.size();i++) {
//                StudyCard studyCard = studyCardList.get(i);
//                List<StudentCourse> courseList = studyCardList.get(i).getStudentCourses();
//
//                StudyCard newCard = new StudyCard();
//                ArrayList<StudentCourse> newCourses = new ArrayList<>();
//
//                courseList.forEach(e->{
//                    if (e.isTranscript()) {
//                        newCourses.add(e);
//                    }
//                });
//
//                System.out.println("newCourses : "+ newCourses);
//
//                newCard.setId(studyCard.getId());
//                newCard.setNim(studyCard.getNim());
//                newCard.setScheduleId(studyCard.getScheduleId());
//                newCard.setPeriodTaken(studyCard.getPeriodTaken());
//                newCard.setStudentCourses(newCourses);
//                newCard.setDeleted(studyCard.isDeleted());
//                newCard.setProcessedAt(studyCard.getProcessedAt());
//                newCard.setProcessedByPA(studyCard.isProcessedByPA());
//                newCard.setCreatedBy(studyCard.getCreatedBy());
//                newCard.setCreatedAt(studyCard.getCreatedAt());
//                newCard.setUpdatedBy(studyCard.getUpdatedBy());
//                newCard.setUpdatedAt(studyCard.getUpdatedAt());
//
//                studyCards.add(newCard);
//            }
            List<HashMap<String, Object>> listCourses = new ArrayList<>();
//            studyCards.forEach(datum->{
//                HashMap<String, Object> hashMapCourse = studyCardService.getStudyPeriod(datum);
//                listCourses.add(hashMapCourse);
//
//                System.out.println("ip :: "+hashMapCourse.get("ip"));
//            });

            return listCourses;
        }
    }

}
