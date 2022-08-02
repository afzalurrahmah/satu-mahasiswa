package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.service.PeriodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/period")
public class PeriodController {

    @Autowired
    PeriodService periodService;

    private static final Logger logger = LogManager.getLogger(PeriodController.class);

    @GetMapping("/list")
    public ResponseEntity periodList(@RequestAttribute UserJWT userdata) {
        try {
            List<HashMap<String, Object>> data = periodService.getPeriodList(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("success get period list", data);
            } else {
                return new ResponseBody().notFound("no period list", null);
            }
        } catch (Exception e){
            logger.error("periodList : "+e.getMessage());
            return new ResponseBody().failed("failed get period list", e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity periodActive(@RequestAttribute UserJWT userdata) {
        try {
            HashMap<String, Object> data = periodService.periodActive(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("success get active period", data);
            } else {
                return new ResponseBody().notFound("no active period", null);
            }
        } catch (Exception e){
            logger.error("periodActive : "+e.getMessage());
            return new ResponseBody().failed("failed get active period", e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getDetailPeriod(@RequestAttribute UserJWT userdata, @PathVariable String id) {
        try {
            HashMap<String, Object> data = periodService.getDetail(userdata.getIdentity(), id);
            if(data != null){
                return new ResponseBody().found("success get detail period", data);
            } else {
                return new ResponseBody().notFound("no period data", null);
            }
        } catch (Exception e){
            logger.error("getDetailPeriod : "+e.getMessage());
            return new ResponseBody().failed("failed get detail period", e.getMessage());
        }
    }
}
