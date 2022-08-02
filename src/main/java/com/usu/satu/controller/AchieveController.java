package com.usu.satu.controller;

import com.usu.satu.dto.AchieveRequest;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.Achievement;
import com.usu.satu.service.AchieveService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/achievement")
public class AchieveController {

    @Autowired
    AchieveService achieveService;

    private static final Logger logger = LogManager.getLogger(AchieveController.class);

    @PostMapping
    public ResponseEntity createAchievement(@ModelAttribute AchieveRequest achieveRequest){
        try {
            Achievement data = achieveService.saveAchievement(achieveRequest);
            if (data != null){
                return new ResponseBody().created("success created", data);
            } else {
                return new ResponseBody().notFound("no insert data", null);
            }
        }
        catch (Exception e){
            logger.error("postAchievement : "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAchievement(@PathVariable String id, @ModelAttribute AchieveRequest achieveRequest){
        try {
            Achievement data = achieveService.editAchievement(id, achieveRequest);
            if (data != null){
                return new ResponseBody().found("success update data", data);
            } else {
                return new ResponseBody().notFound("no achievement data", null);
            }
        }
        catch (Exception e){
            logger.error("putAchievement : "+e.getMessage());
            return new ResponseBody().failed("failed remove data", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAchievement(@PathVariable String id){
        try {
            Achievement data = achieveService.removeAchievement(id);
            if (data != null){
                return new ResponseBody().found("success remove data", data);
            } else {
                return new ResponseBody().notFound("no achievement data", null);
            }
        }
        catch (Exception e){
            logger.error("deleteAchievement : "+e.getMessage());
            return new ResponseBody().failed("failed remove data", e.getMessage());
        }
    }

    @PutMapping("/valid/{id}")
    public ResponseEntity validAchievement(@PathVariable String id){
        try {
            Achievement data = achieveService.validationAchieve(id);
            if (data != null){
                return new ResponseBody().found("success validate data", data);
            } else {
                return new ResponseBody().notFound("no achievement data", null);
            }
        }
        catch (Exception e){
            logger.error("validationAchievement : "+e.getMessage());
            return new ResponseBody().failed("failed remove data", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllAchievements(){
        try {
            List<Achievement> data = achieveService.getAllAchievements();
            if (data.isEmpty()){
                return new ResponseBody().notFound("no achievement data", null);
            } else {
                return new ResponseBody().found("success get data", data);
            }
        }
        catch (Exception e){
            logger.error("getAllAchievement : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/mhs/{nim}")
    public ResponseEntity getAchieveBasedNim(@PathVariable String nim){
        try {
            List<Achievement> data = achieveService.getAchieveNim(nim);
            System.out.println(data);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no achievement data", null);
            } else {
                return new ResponseBody().found("success get data nim "+nim, data);
            }
        }
        catch (Exception e){
            logger.error("getAchievementBasedNim : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getAchieveBasedID(@PathVariable String id){
        try {
            Optional<Achievement> data = achieveService.getAchieveID(id);
            if (data.isPresent()){
                return new ResponseBody().found("success get data", data);
            } else {
                return new ResponseBody().notFound("no data", null);
            }
        }
        catch (Exception e){
            logger.error("getAchievementBasedId : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }
}
