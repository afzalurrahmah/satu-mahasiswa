package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.StudyCard;
import com.usu.satu.service.StudyCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/study-card")
public class StudyCardController {

    @Autowired
    StudyCardService studyCardService;

    private static final Logger logger = LogManager.getLogger(StudyCardController.class);

    @PostMapping
    public ResponseEntity createStudy(@RequestBody StudyCard studyCard){
        try {
            StudyCard data = studyCardService.saveStudy(studyCard);
            if(data == null){
                return new ResponseBody().notFound("no input study card", null);
            } else {
                return new ResponseBody().created("success created study card", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        }catch (Exception e){
            logger.error("postStudyCard : "+e.getMessage());
            return new ResponseBody().failed("failed create study card", e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateStudy(@PathVariable String id, @RequestBody StudyCard studyCard){
        try {
            HashMap<String, Object> data = studyCardService.editStudy(id, studyCard);
            if(data == null){
                return new ResponseBody().notFound("no study card data", null);
            } else {
                return new ResponseBody().created("success update data", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("putStudyCard : "+e.getMessage());
            return new ResponseBody().failed("failed update data", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudy(@PathVariable String id){
        try {
            StudyCard data = studyCardService.removeStudy(id);
            if(data != null){
                return new ResponseBody().created("success delete data", data);
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        }
        catch (Exception e){
            logger.error("deleteStudyCard : "+e.getMessage());
            return new ResponseBody().failed("failed delete data", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllStudyCard(){
        try {
            List<StudyCard> data = studyCardService.getAll();
            if(data != null){
                return new ResponseBody().found("success get data", data);
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getAllStudyCard : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/nim/{nim}")
    public ResponseEntity getStudyBasedNim(@PathVariable String nim){
        try {
            List<HashMap<String, Object>> data = studyCardService.getStudyNim(nim);
            System.out.println(data);
            if(data != null){
                System.out.println("lewat");
                return new ResponseBody().found("success get data", data);
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getStudyCardBasedNim : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity getMyStudy(@RequestAttribute UserJWT userdata){
        try {
            List<HashMap<String, Object>> data = studyCardService.getStudyNim(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("success get data", data);
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getMyStudy : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getStudyID(@PathVariable String id){
        try {
            Optional<StudyCard> data = studyCardService.getStudyId(id);
            if(data != null){
                return new ResponseBody().found("success get data", data.get());
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getStudyCardBasedId : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/student")
    public ResponseEntity getStudyBasedPeriodAndNim(@RequestParam String period, @RequestParam String nim){
        try {
            HashMap<String, Object> data = studyCardService.getStudyPeriodAndNim(period, nim);
            if(data != null){
                return new ResponseBody().found("success get data", data);
            } else {
                return new ResponseBody().notFound("no study card data", null);
            }
        } catch (AcceptedException e) {
            logger.error("AcceptedException getStudyBasedPeriodAndNim : "+e.getMessage());
            return new ResponseBody().found("failed get data", null);
        } catch (Exception e){
            logger.error("getStudyBasedPeriodAndNim : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/courses-offered")
    public ResponseEntity getCoursesOffered(@RequestAttribute UserJWT userdata) {
        try {
            HashMap<String,Object> data = studyCardService.getCoursesOffered(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("courses offered list", data);
            } else {
                return new ResponseBody().notFound("no courses offered data", null);
            }
        } catch (Exception e){
            logger.error("getCoursesOffered : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/schedule")
    public ResponseEntity studySchedule(@RequestAttribute UserJWT userdata) {
        try {
            List<HashMap<String,Object>> data = studyCardService.getSchedule(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("success get schedule data", data);
            } else {
                return new ResponseBody().notFound("no schedule data", null);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e){
            logger.error("studySchedule : "+e.getMessage());
            return new ResponseBody().failed("failed get data", e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity getActiveStudy(@RequestAttribute UserJWT userdata){
        try {
            HashMap<String,Object> data = studyCardService.getActStudy(userdata.getIdentity());
            if(data != null){
                return new ResponseBody().found("success get active study card", data);
            } else {
                return new ResponseBody().notFound("no active study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getActiveStudy : "+e.getMessage());
            return new ResponseBody().failed("failed get period data", e.getMessage());
        }
    }

    @GetMapping("/active/nim/{nim}")
    public ResponseEntity getActiveStudyBasedNim(@PathVariable String nim){
        try {
            HashMap<String,Object> data = studyCardService.getActStudy(nim);
            if(data != null){
                return new ResponseBody().found("success get active study card", data);
            } else {
                return new ResponseBody().notFound("no active study card data", null);
            }
        }
        catch (Exception e){
            logger.error("getActiveStudyBasedNim : "+e.getMessage());
            return new ResponseBody().failed("failed get period data", e.getMessage());
        }
    }

    @GetMapping("/period/{period}")
    public ResponseEntity getStudyBasedPeriod(@RequestAttribute UserJWT userdata, @PathVariable String period){
        try {
            HashMap<String,Object> data = studyCardService.getStudyCardBasedPeriod(userdata.getIdentity(), period);
            if(data != null){
                return new ResponseBody().found("success get period", data);
            } else {
                return new ResponseBody().notFound("no period data", null);
            }
        }
        catch (Exception e){
            logger.error("getStudyBasedPeriod : "+e.getMessage());
            return new ResponseBody().failed("failed get period data", e.getMessage());
        }
    }
}
