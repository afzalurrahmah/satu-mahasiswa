package com.usu.satu.controller;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudyCard;
import com.usu.satu.service.GradeService;
import com.usu.satu.service.LectureService;
import com.usu.satu.service.SIAService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    LectureService lectureService;

    @Autowired
    SIAService siaService;

    @Autowired
    GradeService gradeService;

    private static final Logger logger = LogManager.getLogger(LectureController.class);

    @GetMapping("/class")
    public ResponseEntity getStudentClass(@RequestParam(required = false) String class_id, @RequestParam(required = false) String period_id){
        try {
            HashMap<String, Object> data = siaService.getStudent(class_id, period_id);
            if (data == null) {
                return new ResponseBody().notFound("no student data", null);
            } else {
                return new ResponseBody().found("student data list", data);
            }
        } catch (Exception e) {
            logger.error("getStudentClass : "+e.getMessage());
            return new ResponseBody().failed("failed get my status", e.getMessage());
        }
    }

    @PutMapping("/grade")
    public ResponseEntity editGrade(@RequestBody HashMap<String,Object> hashMap){
        try {
            StudyCard data = gradeService.updateGrade(hashMap);
            if (data != null){
                return new ResponseBody().created("success get grade data", data);
            } else {
                return new ResponseBody().notFound("no grade data", null);
            }
        }
        catch (Exception e){
            logger.error("updateGrade : "+e.getMessage());
            return new ResponseBody().failed("failed get grade data", e.getMessage());
        }
    }

    @GetMapping("/study-card")
    public ResponseEntity getStudyCardForGrade(
            @RequestParam(required = false) String lecture_id,
            @RequestParam(required = false) String period_id
    ){
        try {
            HashMap<String,Object> data = lectureService.getStudyCardGrade(lecture_id, period_id);
            if (data == null) {
                return new ResponseBody().notFound("no study card data", null);
            } else {
                return new ResponseBody().found("study card data list", data);
            }
        } catch (Exception e) {
            logger.error("getStudyCardForGrade : "+e.getMessage());
            return new ResponseBody().failed("failed get study card", e.getMessage());
        }
    }

    @PutMapping("/academic-lecture")
    public ResponseEntity editAcademicLecture(@RequestBody HashMap<String,String> academic){
        try {
            HashMap<String,String> data = lectureService.updateAcademicLecture(academic);
            if (data == null) {
                return new ResponseBody().notFound("no academic lecture", null);
            } else {
                return new ResponseBody().found("success to add academic lecture", data);
            }
        } catch (Exception e) {
            logger.error("editAcademicLecture : "+e.getMessage());
            return new ResponseBody().failed("failed to add academic lecture", e.getMessage());
        }
    }

    @GetMapping("/academic-lecture")
    public ResponseEntity getAcademicLecture(@RequestParam String lecture_id, @RequestParam String period_id){
        try {
            List<Student> data = lectureService.getAcademicLecture(lecture_id, period_id);
            if (data == null) {
                return new ResponseBody().notFound("no academic lecture", null);
            } else {
                return new ResponseBody().found("success to get academic lecture", data);
            }
        } catch (Exception e) {
            logger.error("getAcademicLecture : "+e.getMessage());
            return new ResponseBody().failed("failed to get academic lecture", e.getMessage());
        }
    }

    @PutMapping("/remove-course")
    public ResponseEntity deleteCourseByPA(@RequestParam String study_card_id, @RequestParam String class_id){
        try {
            StudyCard data = lectureService.removeCourseByPA(study_card_id, class_id);
            if (data == null) {
                return new ResponseBody().notFound("no course to remove", null);
            } else {
                return new ResponseBody().found("success to remove course", data);
            }
        } catch (Exception e) {
            logger.error("deleteCourseByPA : "+e.getMessage());
            return new ResponseBody().failed("failed to remove course", e.getMessage());
        }
    }

    @PutMapping("/acc")
    public ResponseEntity approvedStudyCardByPA(@RequestParam String study_card_id){
        try {
            StudyCard data = lectureService.accStudyCardByPA(study_card_id);
            if (data == null) {
                return new ResponseBody().failed("no study card to approved", null);
            } else {
                return new ResponseBody().found("success to approved study card by PA", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().failed(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("approvedStudyCardByPA : "+e.getMessage());
            return new ResponseBody().failed("failed to approved study card by PA", e.getMessage());
        }
    }

    @GetMapping("/needed")
    public ResponseEntity neededLecturePA(@RequestParam Map<String,String> request){
        try {
            List<Student> data = lectureService.studentNeedPA(request);
            if (data == null) {
                return new ResponseBody().notFound("no student need PA", null);
            } else {
                return new ResponseBody().found("success to get student need PA list", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        }catch (Exception e) {
            logger.error("deleteCourseByPA : "+e.getMessage());
            return new ResponseBody().failed("failed to get student need PA list", e.getMessage());
        }
    }
}
