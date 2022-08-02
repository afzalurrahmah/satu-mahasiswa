package com.usu.satu.service;

import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.mapper.PresenceMapper;
import com.usu.satu.model.Presence;
import com.usu.satu.repository.PresenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
public class PresenceService {

    @Autowired
    PresenceRepository presenceRepository;

    @Autowired
    PresenceMapper presenceMapper;

    @Autowired
    StudyCardService studyCardService;

    @Autowired
    StudentService studentService;

    @Autowired
    SIAGenerate siaGenerate;

    public Presence savePresence(Presence presence){
        List<Presence> classList = presenceRepository.findAllByClassIdAndIsDeletedIsFalse(presence.getClassId());
        AtomicBoolean readyUsedClass = new AtomicBoolean(false);    // checking created class

        classList.forEach( e -> {
            if(e.getClassId().equalsIgnoreCase(presence.getClassId()) && e.getPresentAt().equalsIgnoreCase(presence.getPresentAt())) {
                readyUsedClass.set(true);
            }
        });

        if(readyUsedClass.get()){
                return null;
        } else {
            Presence entity = new Presence();
            presenceMapper.updatePresence(presence, entity);
            presenceRepository.save(entity);

            return entity;
        }
    }

    public Presence editPresence(String id, Presence presence){
        Optional<Presence> data = presenceRepository.findByIdAndIsDeletedFalse(id);
        if(data.isPresent()){
            Presence entity = data.get();
            presenceMapper.updatePresence(presence, entity);
            presenceRepository.save(entity);

            return entity;
        } else {
            return null;
        }
    }

    public Presence removePresence(String id){
        Optional<Presence> data = presenceRepository.findByIdAndIsDeletedFalse(id);
        if(data.isPresent()){
            Presence entity = data.get();
            entity.setDeleted(true);
            presenceRepository.save(entity);
            return entity;
        } else {
            return null;
        }
    }

    public List<Presence> getAll(){
        return presenceRepository.findAllByIsDeletedFalse();
    }

    public List<Presence> getBasedClass(String classId){
        return presenceRepository.findAllByClassIdAndIsDeletedIsFalse(classId);
    }

    public List<HashMap<String, String>> myPresence(String nim){
        HashMap<String,Object> studyCards = studyCardService.getActStudy(nim);

        List<HashMap<String, String>> courses = new ArrayList<>();

        if (studyCards == null) {
            HashMap<String, String> count = new HashMap<>();
            count.put("nim", nim);
            count.put("class_id", null);
            count.put("course_name", null);
            count.put("total_meeting", String.valueOf(Integer.parseInt("0")));
            count.put("total_attendance", String.valueOf(Integer.parseInt("0")));
            count.put("percent", String.valueOf(Integer.parseInt("0")));
            courses.add(count);
        } else {
            List<HashMap<String, Object>> arrCourse = (List<HashMap<String, Object>>) studyCards.get("student_courses");

//        List<String> arrCourse = presenceRepository.courseList();

            for (HashMap<String, Object> stringObjectHashMap : arrCourse) {
                String classId = stringObjectHashMap.get("id").toString();

                HashMap<String, Object> jsonCourseName = (HashMap<String, Object>) stringObjectHashMap.get("class_info");
                System.out.println("jsonCourseName : "+jsonCourseName);
                String courseName = jsonCourseName.get("class_name").toString();
//            String courseCode = jsonCourseName.get("class_code").toString();

                float totalMeeting;
                String meeting = presenceRepository.countMeeting(classId);
                if (meeting == null) {
                    totalMeeting = 0;
                } else {
                    totalMeeting = Integer.parseInt(presenceRepository.countMeeting(classId));
                }

                float totalAttendance;
                String attendance = presenceRepository.countAttendance(nim, classId);
                if (attendance == null) {
                    totalAttendance = 0;
                } else {
                    totalAttendance = Integer.parseInt(presenceRepository.countAttendance(nim, classId));
                }

                float percent = totalAttendance / totalMeeting * 100;

                HashMap<String, String> count = new HashMap<>();
                count.put("nim", nim);
                count.put("class_id", classId);
//            count.put("course_code", courseCode);
                count.put("course_name", courseName);
                count.put("total_meeting", String.valueOf(Math.round(totalMeeting)));
                count.put("total_attendance", String.valueOf(Math.round(totalAttendance)));
                count.put("percent", String.valueOf(Math.round(percent)));
                courses.add(count);
            }
        }

        return courses;
    }
}
