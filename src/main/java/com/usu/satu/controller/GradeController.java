package com.usu.satu.controller;

import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.StudyCard;
import com.usu.satu.service.GradeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    GradeService gradeService;

    private static final Logger logger = LogManager.getLogger(GradeController.class);

    @PutMapping
    public ResponseEntity updateGrade(@RequestBody HashMap<String,Object> input){
        try {
            StudyCard data = gradeService.updateGrade(input);
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
}
