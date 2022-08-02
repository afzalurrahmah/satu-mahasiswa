package com.usu.satu.controller;

import com.usu.satu.helper.ResponseBody;
import com.usu.satu.service.MBKMService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/mbkm")
public class MBKMController {

    @Autowired
    MBKMService mbkmService;

    private static final Logger logger = LogManager.getLogger(MBKMController.class);

    @GetMapping("/list")
    public ResponseEntity getMBKMList() {
        try {
            List<HashMap<String, Object>> mbkmList = mbkmService.getList();
            if (mbkmList.isEmpty()){
                return new ResponseBody().notFound("no mbkm list", null);
            } else {
                return new ResponseBody().found("success get mbkm list", mbkmList);
            }
        } catch (Exception e){
            logger.error("getMBKMList : "+e.getMessage());
            return new ResponseBody().failed("failed get mbkm list", e.getMessage());
        }
    }

    @GetMapping("/{url}")
    public ResponseEntity getMBKMBasedId(@PathVariable String url) {
        try {
            List<HashMap<String, Object>> mbkm = mbkmService.getDetail(url);
            if (mbkm.isEmpty()){
                return new ResponseBody().notFound("no mbkm detail", null);
            } else {
                return new ResponseBody().found("success get mbkm detail", mbkm);
            }
        } catch (Exception e){
            logger.error("getMBKMBasedId : "+e.getMessage());
            return new ResponseBody().failed("failed get mbkm detail", e.getMessage());
        }
    }
}
