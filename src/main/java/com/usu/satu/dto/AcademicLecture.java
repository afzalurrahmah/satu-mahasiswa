package com.usu.satu.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class AcademicLecture {

    @Field("lecture_id")
    private String lectureId;

    @Field("period_id")
    private String periodId;

    @Field("created_at")
    private String createdAt;

    public AcademicLecture(String lectureId, String periodId, String createdAt) {
        this.lectureId = lectureId;
        this.periodId = periodId;
        this.createdAt = createdAt;
    }

    public AcademicLecture() {
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AcademicLecture{" +
                "lectureId='" + lectureId + '\'' +
                ", periodId='" + periodId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
