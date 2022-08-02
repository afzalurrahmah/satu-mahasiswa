package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.model.StudyCard;
import com.usu.satu.service.ExportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    ExportService exportService;

    private static final Logger logger = LogManager.getLogger(ExportController.class);

    @RequestMapping(value = "/krs/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> printStudyCardPlan(@RequestAttribute UserJWT userdata, @RequestParam String period_id) {
        String nim = userdata.getIdentity();

        ByteArrayInputStream bis =  exportService.printKRS(nim, period_id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=krs-"+nim+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/krs/{nim}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> printStudyCardPlanByNim(@PathVariable String nim, @RequestParam String period_id) {
        ByteArrayInputStream bis =  exportService.printKRS(nim, period_id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=krs-"+nim+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/khs/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> printStudyCardResult(@RequestAttribute UserJWT userdata, @RequestParam String period_id) {
        String nim = userdata.getIdentity();

        ByteArrayInputStream bis =  exportService.printKHS(nim, period_id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=khs-"+nim+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/khs/{nim}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> printStudyCardResultByNim(@PathVariable String nim, @RequestParam String period_id) {
        ByteArrayInputStream bis =  exportService.printKHS(nim, period_id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=khs-"+nim+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

//    @RequestMapping(value = "/transcript/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<InputStreamResource> printTranscript(@RequestAttribute UserJWT userdata) {
//        String nim = userdata.getIdentity();
//
//        ByteArrayInputStream bis =  exportService.printTranscript(nim);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "inline; filename=khs-"+nim+".pdf");
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(new InputStreamResource(bis));
//    }
}
