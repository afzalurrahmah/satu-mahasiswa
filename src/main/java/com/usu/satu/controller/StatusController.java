package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.service.StatusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    StatusService statusService;

    private static final Logger logger = LogManager.getLogger(StatusController.class);

    // create and update //
    @PostMapping
    public ResponseEntity createStatus(
            @RequestAttribute UserJWT userdata,
            @RequestParam String period_id,
            @RequestParam String schema_id
    ) {
        try {
            StudentStatus data = statusService.saveStatus(userdata.getIdentity(), period_id, schema_id);
            if (data == null){
                return new ResponseBody().notFound("no input status", null);
            } else {
                return new ResponseBody().created("success create status", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("createStatus : "+e.getMessage());
            return new ResponseBody().failed("failed create status", e.getMessage());
        }
    }

    // edit //
    @PutMapping
    public ResponseEntity updateStatus(
            @RequestParam(required = false) String nim,
            @RequestParam(required = false) String period_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) boolean value,
            @RequestParam(required = false) String schema_id
    ) {
        try {
            logger.info("updateStatus");
            StudentStatus data = statusService.updateStatus(nim, period_id, status, value, schema_id);
            if (data == null){
                return new ResponseBody().notFound("no input status", null);
            } else {
                return new ResponseBody().created("success update status", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("updateStatus : "+e.getMessage());
            return new ResponseBody().failed("failed update status", e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllStatus() {
        try {
            List<StudentStatus> data = statusService.getAll();
            if (data == null){
                return new ResponseBody().notFound("no input status", null);
            } else {
                return new ResponseBody().created("success get status", data);
            }
        } catch (Exception e) {
            logger.error("getAllStatus : "+e.getMessage());
            return new ResponseBody().failed("failed get status", e.getMessage());
        }

    }

    @GetMapping("/me")
    public ResponseEntity myStatus(@RequestAttribute UserJWT userdata) {
        try {
            List<StudentStatus> data = statusService.getMyStatus(userdata.getIdentity());
            if (data == null){
                return new ResponseBody().notFound("no status", null);
            } else {
                return new ResponseBody().created("success get my status", data);
            }
        } catch (Exception e) {
            logger.error("myStatus : "+e.getMessage());
            return new ResponseBody().failed("failed get my status", e.getMessage());
        }
    }

    @GetMapping("/nim/{nim}")
    public ResponseEntity myStatusNim(@PathVariable String nim) {
        try {
            List<StudentStatus> data = statusService.getMyStatus(nim);
            if (data == null){
                return new ResponseBody().notFound("no status", null);
            } else {
                return new ResponseBody().created("success get my status", data);
            }
        } catch (Exception e) {
            logger.error("myStatus : "+e.getMessage());
            return new ResponseBody().failed("failed get my status", e.getMessage());
        }
    }
}
