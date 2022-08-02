package com.usu.satu.controller;

import com.usu.satu.dto.StudentRequest;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.Student;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @PostMapping
    public ResponseEntity createStudent(@ModelAttribute StudentRequest studentRequest){
        try {
            Object std = studentService.saveStudent(studentRequest);
            if (std == null){
                return new ResponseBody().existed("NIM "+studentRequest.getNim()+" is exist", null);
            } else {
                return new ResponseBody().created("success created", std);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("postStudent : "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PostMapping("/sireg")
    public ResponseEntity createMultiStudent(@ModelAttribute StudentRequest studentRequest){
        try {
            Object std = studentService.saveStudent(studentRequest);

            if (std.equals("NIM "+studentRequest.getNim()+" is exist")){
                return new ResponseBody().existed("NIM "+studentRequest.getNim()+" is exist", null);
            } else {
                return new ResponseBody().created("success created", std);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PostMapping("/copyfile")
    public ResponseEntity copyFile(@RequestParam String url){
        try {
            System.out.println(url.length());
            String[] fileName = url.split("\\.");
            String name = fileName[(fileName.length)-1];

            System.out.println(name);
//            FileUtils.copyURLToFile(new URL(url), new File("D:/PSI/SATU/Mahasiswa/files/photos/"+studentRequest.getNim()), 10000, 10000);
            return new ResponseBody().notFound("no input data", url);
        }
        catch (Exception e){
            return new ResponseBody().failed("failed get file", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllStudent(){
        try {
            List<Student> std = studentService.getAllStudents();
            if (std != null){
                return new ResponseBody().found("success get data", std);
            } else {
                return new ResponseBody().notFound("no student data", null);
            }
        }
        catch (Exception e){
            logger.error("getAllStudent : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/{nim}")
    public ResponseEntity getStudentBasedNim(@PathVariable String nim){
        try {
            Student std = studentService.getStudentNim(nim);
            if (std != null){
                return new ResponseBody().found("success get data", std);
            } else {
                return new ResponseBody().notFound(nim+" is not exist", null);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("getStudentBasedNim : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity getStudentBasedId(@PathVariable String id){
        try {
           Student std = studentService.getStudentId(id);
            if (std != null){
                return new ResponseBody().found("success get data", std);
            } else {
                return new ResponseBody().notFound(id+" is not exist", null);
            }
        }
        catch (Exception e){
            logger.error("getStudentBasedId : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateStudent(@PathVariable String id, @ModelAttribute StudentRequest student){
        logger.info("updateStudent");
        try {
            Student std = studentService.editStudent(id, student);
            if (std == null){
                return new ResponseBody().notFound("student data is not exist", null);
            } else {
                return new ResponseBody().found("success update data nim "+student.getNim(), std);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("putStudent : "+e.getMessage());
            return new ResponseBody().failed("failed update data", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudent(@PathVariable String id){
        try {
            Student std = studentService.removeStudent(id);
            if (std == null){
                return new ResponseBody().notFound("student data is not exist", null);
            } else {
                return new ResponseBody().found("success remove data", std);
            }
        }
        catch (Exception e){
            logger.error("deleteStudent : "+e.getMessage());
            return new ResponseBody().failed("failed remove data", e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity getFilterStudent(@RequestParam(required = false) Map<String,String> params){
        try {
            HashMap<String, Object> data = studentService.getFilterStudent(params);
            if (data == null) {
                return new ResponseBody().notFound("no student data to filter", null);
            } else {
                return new ResponseBody().found("student data filter list", data);
            }
        } catch (Exception e) {
            logger.error("getFilterStudent : "+e.getMessage());
            return new ResponseBody().failed("failed filter student data", e.getMessage());
        }
    }

    @GetMapping("/nationality/distinct")
    public ResponseEntity getDistinctNational() {
        try {
            List<HashMap<String, Object>> data = studentService.getStudentsDistinct();
            if (data == null) {
                return new ResponseBody().notFound("no student nationality to distinct", null);
            } else {
                return new ResponseBody().found("student nationality filter", data);
            }
        } catch (Exception e) {
            logger.error("getDistinctNational : "+e.getMessage());
            return new ResponseBody().failed("failed distinct nationality", e.getMessage());
        }
    }

    @PutMapping("/nationality/replace")
    public ResponseEntity nationalReplace() {
        try {
            List<Student> data = studentService.replaceNationality();
            if (data == null) {
                return new ResponseBody().notFound("no student nationality to replace", null);
            } else {
                return new ResponseBody().found("student nationality replace", data);
            }
        } catch (Exception e) {
            logger.error("nationalReplace : "+e.getMessage());
            return new ResponseBody().failed("failed replace nationality", e.getMessage());
        }
    }
}
