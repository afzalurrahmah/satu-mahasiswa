package com.usu.satu.controller;

import com.usu.satu.service.MigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.usu.satu.helper.ResponseBody;

import java.util.HashMap;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/migration")
public class MigrationController {

    private static final Logger logger = LogManager.getLogger(MigrationController.class);

    @Autowired
    MigrationService migrationService;

    @PostMapping( "/student-profile")
    public ResponseEntity importStudentProfile(@RequestParam MultipartFile csv_file) {
        try {
            HashMap<String,Object> data = migrationService.importStudentProfile(csv_file);
            return new ResponseBody().found(String.format("Berhasil mengimport %d mahasiswa",Integer.parseInt(data.get("amount_saved").toString())), data.get("duplicate_data"));
        } catch (Exception e) {
            logger.error("importStudentProfile: "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PostMapping( "/student-payment/h2h")
    public ResponseEntity importH2hStudentPayment(@RequestParam MultipartFile csv_file) {
        try {
            return new ResponseBody().found(String.format("Berhasil mengimport %d billing",migrationService.importStudentPaymentH2h(csv_file)) , null);
        } catch (Exception e) {
            logger.error("importH2hStudentPayment: "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }

    @PostMapping( "/student-payment/tagihan")
    public ResponseEntity importTagihanStudentPayment(@RequestParam MultipartFile csv_file) {
        try {
            return new ResponseBody().found(String.format("Berhasil mengimport %d billing",migrationService.importStudentPaymentTagihan(csv_file)) , null);
        } catch (Exception e) {
            logger.error("importTagihanStudentPayment: "+e.getMessage());
            return new ResponseBody().failed("failed create data", e.getMessage());
        }
    }
}
