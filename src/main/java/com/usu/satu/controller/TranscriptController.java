package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.service.TranscriptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/transcript")
public class TranscriptController {

    @Autowired
    TranscriptService transcriptService;

    private static final Logger logger = LogManager.getLogger(TranscriptController.class);

    @GetMapping("/me")
    public ResponseEntity getTranscript(@RequestAttribute UserJWT userdata){
        try {
            List<HashMap<String, Object>> data = transcriptService.getTranscript(userdata.getIdentity());
            if(data != null){
                return new com.usu.satu.helper.ResponseBody().found("success get my transcript", data);
            } else {
                return new com.usu.satu.helper.ResponseBody().notFound("no my transcript data", null);
            }
        }
        catch (Exception e){
            logger.error("getTranscript : "+e.getMessage());
            return new com.usu.satu.helper.ResponseBody().failed("failed get my transcript data", e.getMessage());
        }
    }

    @GetMapping("/nim/{nim}")
    public ResponseEntity getTranscriptBasedNim(@PathVariable String nim){
        try {
            List<HashMap<String, Object>> data = transcriptService.getTranscript(nim);
            if(data != null){
                return new com.usu.satu.helper.ResponseBody().found("success get transcript "+nim, data);
            } else {
                return new com.usu.satu.helper.ResponseBody().notFound("no transcript data "+nim, null);
            }
        }
        catch (Exception e){
            logger.error("getTranscriptBasedNim : "+e.getMessage());
            return new ResponseBody().failed("failed get transcript data "+nim, e.getMessage());
        }
    }
}
