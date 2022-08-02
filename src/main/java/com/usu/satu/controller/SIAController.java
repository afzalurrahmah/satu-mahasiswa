package com.usu.satu.controller;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.service.SIAService;
import com.usu.satu.service.StudentService;
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
@RequestMapping("/sia")
public class SIAController {

    @Autowired
    SIAService siaService;

    @Autowired
    StudentService studentService;

    private static final Logger logger = LogManager.getLogger(SIAController.class);

    @GetMapping("/student")
    public ResponseEntity getStudentMajor(@RequestParam(required = false) Map<String, Object> params){
        try {
            List<HashMap<String, String>> data = siaService.getFilterStudent(params);
            if (data == null) {
                return new ResponseBody().notFound("no student data", null);
            } else {
                return new ResponseBody().found("student data list", data);
            }
        } catch (Exception e) {
            logger.error("getStudentMajor : "+e.getMessage());
            return new ResponseBody().failed("failed get my status", e.getMessage());
        }
    }

    @GetMapping("/student/id/{id}")
    public ResponseEntity getStudentId(@PathVariable String id){
        try {
            Student data = studentService.getStudentId(id);
            if (data == null) {
                return new ResponseBody().notFound("no student data id "+id, null);
            } else {
                return new ResponseBody().found("student data id "+id, data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        }catch (Exception e) {
            logger.error("getStudentId : "+e.getMessage());
            return new ResponseBody().failed("failed get status id "+id, e.getMessage());
        }
    }

    @GetMapping("/class")
    public ResponseEntity getStudentBasedClassId(@RequestParam(required = false) String class_id, @RequestParam(required = false) String period_id){
        try {
            HashMap<String, Object> data = siaService.getStudent(class_id, period_id);
            if(data != null){
                return new ResponseBody().found("success get class", data);
            } else {
                return new ResponseBody().notFound("no class data", null);
            }
        }
        catch (Exception e){
            logger.error("getStudentBasedClassId : "+e.getMessage());
            return new ResponseBody().failed("failed get period data", e.getMessage());
        }
    }

//    @GetMapping("/total")
//    public ResponseEntity getTotalStudentStatus(
////            @RequestParam(required = false) String major,
////            @RequestParam(required = false) String period
//            @RequestParam Map<String, Object> params
//    ){
//        try {
//            List<HashMap<String, Object>> data = siaService.getTotalStatus(params);
//            if(data != null){
//                return new ResponseBody().found("success get status total", data);
//            } else {
//                return new ResponseBody().notFound("no status data", null);
//            }
//        } catch (AcceptedException e) {
//            return new ResponseBody().notFound(e.getMessage(), null);
//        } catch (Exception e){
//            logger.error("getTotalStudentStatus : "+e.getMessage());
//            return new ResponseBody().failed("failed get status total data", e.getMessage());
//        }
//    }

    @PostMapping("/status/new")
    public ResponseEntity createAllStatus(
            @RequestParam String semesterId,
            @RequestParam String schemaId
//            @RequestParam String facul
    ) {
        try {
            List<StudentStatus> data = siaService.saveAllStatus(semesterId, schemaId);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no input status", null);
            } else {
                return new ResponseBody().created("success create all status", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("createStatus : "+e.getMessage());
            return new ResponseBody().failed("failed create all status", e.getMessage());
        }
    }
}
