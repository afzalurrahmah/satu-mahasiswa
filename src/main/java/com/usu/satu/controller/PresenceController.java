package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.Presence;
import com.usu.satu.service.PresenceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/presence")
public class PresenceController {

    @Autowired
    PresenceService presenceService;

    private static final Logger logger = LogManager.getLogger(PresenceController.class);

    @PostMapping
    public ResponseEntity createPresence(@RequestBody Presence presence){
        try {
            Presence data = presenceService.savePresence(presence);
            if (data == null){
                return new ResponseBody().notFound("Attendance for this class has been created, please update..", null);
            } else {
                return new ResponseBody().created("success created", data);
            }
        }
        catch (Exception e){
            logger.error("postPresence : "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePresence(@PathVariable String id, @RequestBody Presence presence){
        try {
            Presence data = presenceService.editPresence(id, presence);
            if (data != null){
                return new ResponseBody().created("success update data", data);
            } else {
                return new ResponseBody().notFound("no data", null);
            }
        }
        catch (Exception e){
            logger.error("putPresence : "+e.getMessage());
            return new ResponseBody().failed("failed update data", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePresence(@PathVariable String id){
        try {
            Presence data = presenceService.removePresence(id);
            if (data != null){
                return new ResponseBody().created("success delete data", data);
            } else {
                return new ResponseBody().notFound("no presence data", null);
            }
        }
        catch (Exception e){
            logger.error("deletePresence : "+e.getMessage());
            return new ResponseBody().failed("failed delete data", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllPresence(){
        try {
            List<Presence> data = presenceService.getAll();
            if (data.isEmpty()){
                return new ResponseBody().notFound("no presence data", null);
            } else {
                return new ResponseBody().created("success get data", data);
            }
        }
        catch (Exception e){
            logger.error("getAllPresence : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity getBasedClass(@PathVariable String classId){
        try {
            List<Presence> data = presenceService.getBasedClass(classId);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no presence class data", null);
            } else {
                return new ResponseBody().created("success get data", data);
            }
        }
        catch (Exception e){
            logger.error("getPresenceBasedClass : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity getMyPresence(@RequestAttribute UserJWT userdata
    ){
        try {
            List<HashMap<String, String>> data = presenceService.myPresence(userdata.getIdentity());
            if (data.isEmpty()){
                return new ResponseBody().notFound("no presence data", null);
            } else {
                return new ResponseBody().created("success get data", data);
            }
        }
        catch (Exception e){
            logger.error("getPresenceBasedClass : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }
}
