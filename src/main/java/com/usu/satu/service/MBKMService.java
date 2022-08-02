package com.usu.satu.service;

import com.usu.satu.helper.SIAGenerate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MBKMService {

    SIAGenerate siaGenerate = new SIAGenerate();

    public List<HashMap<String,Object>> getList() {
        return siaGenerate.getMBKMList();
    }

    public List<HashMap<String, Object>> getDetail(String url) {
        return siaGenerate.getMBKMDetail(url);
    }
}
