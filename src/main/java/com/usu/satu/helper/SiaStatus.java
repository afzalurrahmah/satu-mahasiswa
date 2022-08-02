package com.usu.satu.helper;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SiaStatus {
    public List<String> status() {
        List<String> statusType = new ArrayList<>();
        statusType.add("non_active");
        statusType.add("registered");
        statusType.add("spp");
        statusType.add("krs");
        statusType.add("pka");
        statusType.add("drop_out");
        statusType.add("graduate");
        statusType.add("resign");

        return statusType;
    }
}
