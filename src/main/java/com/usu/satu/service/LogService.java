package com.usu.satu.service;

import com.usu.satu.helper.FormatData;
import com.usu.satu.model.Log;
import com.usu.satu.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    LogRepository logRepository;

    FormatData formatData = new FormatData();

    public void post(String module, String recordId){
        Log log = new Log();
        log.setAction("create");
        log.setModule(module);
        log.setRecordId(recordId);
        log.setCreatedAt(formatData.getNowLocal());
//        logRepository.save(log);
    }

    public void put(String module, String recordId){
        Log log = new Log();
        log.setAction("update");
        log.setModule(module);
        log.setRecordId(recordId);
        log.setCreatedAt(formatData.getNowLocal());
//        logRepository.save(log);
    }

    public void delete(String module, String recordId){
        Log log = new Log();
        log.setAction("delete");
        log.setModule(module);
        log.setRecordId(recordId);
        log.setCreatedAt(formatData.getNowLocal());
//        logRepository.save(log);
    }
}
