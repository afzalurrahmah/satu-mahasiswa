package com.usu.satu.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class StudentAttendance {
    private String nim;

    @Field("is_present")
    private boolean isPresent;

    public StudentAttendance(String nim, boolean isPresent) {
        this.nim = nim;
        this.isPresent = isPresent;
    }

    public StudentAttendance() {
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    @Override
    public String toString() {
        return "PresenceDetail{" +
                "nim='" + nim + '\'' +
                ", isPresent=" + isPresent +
                '}';
    }
}
