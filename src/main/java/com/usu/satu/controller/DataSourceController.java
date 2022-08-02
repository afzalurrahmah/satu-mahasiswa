package com.usu.satu.controller;

import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.service.DataSourceService;
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
@RequestMapping("/data")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    PeriodService periodService;

    private static final Logger logger = LogManager.getLogger(DataSourceController.class);

    @GetMapping("/billing")
    public ResponseEntity getBilling(@RequestAttribute UserJWT userdata) {
        try {
            List<HashMap<String,String>> billings = dataSourceService.getBillings(userdata.getIdentity());
            if (billings.isEmpty()){
                return new ResponseBody().notFound(userdata.getIdentity()+"'s billing is empty", null);
            } else {
                return new ResponseBody().found("success get billing", billings);
            }
        } catch (Exception e){
            logger.error("getBilling : "+e.getMessage());
            return new ResponseBody().failed("failed get billing", e.getMessage());
        }
    }

    @GetMapping("/asa")
    public ResponseEntity getAsa(@RequestAttribute UserJWT userdata, @RequestAttribute String ssoToken) {
        try {
            List<HashMap<String, String>> asa = dataSourceService.getAsa(userdata.getIdentity(), ssoToken);
            if (asa.isEmpty()){
                return new ResponseBody().notFound(userdata.getIdentity()+"'s asa is empty", null);
            } else {
                return new ResponseBody().found("success get asa", asa);
            }
        } catch (Exception e){
            logger.error("getAsa : "+e.getMessage());
            return new ResponseBody().failed("failed get asa", e.getMessage());
        }
    }

    @GetMapping("/faculties")
    public ResponseEntity getFaculties() {
        try {
            List<HashMap<String, Object>> data = dataSourceService.getFaculties();
            if (data.isEmpty()){
                return new ResponseBody().notFound("faculties is empty", null);
            } else {
                return new ResponseBody().found("success get faculties", data);
            }
        } catch (Exception e){
            logger.error("getFaculties : "+e.getMessage());
            return new ResponseBody().failed("failed get faculties", e.getMessage());
        }
    }

    @GetMapping("/faculties/{id}")
    public ResponseEntity getMajors(@PathVariable String id) {
        try {
            List<HashMap<String, Object>> data = dataSourceService.getMajors(id);
            if (data.isEmpty()){
                return new ResponseBody().notFound("majors is empty", null);
            } else {
                return new ResponseBody().found("success get majors", data);
            }
        } catch (Exception e){
            logger.error("getFaculties : "+e.getMessage());
            return new ResponseBody().failed("failed get majors", e.getMessage());
        }
    }

    @GetMapping("/country")
    public ResponseEntity getCountries() {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataCollect("country");
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data countries", null);
            } else {
                return new ResponseBody().found("success get countries", data);
            }
        } catch (Exception e){
            logger.error("getCountries : "+e.getMessage());
            return new ResponseBody().failed("failed get countries", e.getMessage());
        }
    }

    @GetMapping("/province/{id}")
    public ResponseEntity getProvinces(@PathVariable String id) {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataCollect("province&ref_id="+id);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data provinces", null);
            } else {
                return new ResponseBody().found("success get provinces", data);
            }
        } catch (Exception e){
            logger.error("getProvinces : "+e.getMessage());
            return new ResponseBody().failed("failed get provinces", e.getMessage());
        }
    }

    @GetMapping("/city/{id}")
    public ResponseEntity getCities(@PathVariable String id) {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataCollect("district&ref_id="+id);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data cities", null);
            } else {
                return new ResponseBody().found("success get cities", data);
            }
        } catch (Exception e){
            logger.error("getCities : "+e.getMessage());
            return new ResponseBody().failed("failed get cities", e.getMessage());
        }
    }

    @GetMapping("/subdistrict/{id}")
    public ResponseEntity getSubDistrict(@PathVariable String id) {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataCollect("sub_district&ref_id="+id);
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data subDistricts", null);
            } else {
                return new ResponseBody().found("success get subDistricts", data);
            }
        } catch (Exception e){
            logger.error("getSubDistrict : "+e.getMessage());
            return new ResponseBody().failed("failed get subDistricts", e.getMessage());
        }
    }

    @GetMapping("/marital-parent")
    public ResponseEntity getMaritalStatusParent() {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataSource("marital-status/parent");
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data marital status parent", null);
            } else {
                return new ResponseBody().found("success get marital status parent", data);
            }
        } catch (Exception e){
            logger.error("getSubDistrict : "+e.getMessage());
            return new ResponseBody().failed("failed get marital status parent", e.getMessage());
        }
    }

    @GetMapping("/marital-status")
    public ResponseEntity getMaritalStatus() {
        try {
            List<HashMap<String,String>> data = dataSourceService.getDataSource("marital-status");
            if (data.isEmpty()){
                return new ResponseBody().notFound("no data marital status", null);
            } else {
                return new ResponseBody().found("success get marital status", data);
            }
        } catch (Exception e){
            logger.error("getSubDistrict : "+e.getMessage());
            return new ResponseBody().failed("failed get marital status", e.getMessage());
        }
    }

    @GetMapping("/period")
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
}
