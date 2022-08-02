package com.usu.satu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;

public class StudentCourse {

    @Field("class_id")
    private String classId;
    private Grade grade;           // nilai

    @Field("graded_at")
    private String gradedAt;

    @Field("is_transcript")
    private boolean isTranscript = true;

    @Field("is_mbkm")
    private boolean isMbkm;

    @CreatedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("created_at")
    private String createdAt;

    public StudentCourse(String classId, Grade grade, String gradedAt, boolean isTranscript, boolean isMbkm, String createdAt) {
        this.classId = classId;
        this.grade = grade;
        this.gradedAt = gradedAt;
        this.isTranscript = isTranscript;
        this.isMbkm = isMbkm;
        this.createdAt = createdAt;
    }

    public StudentCourse() {
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(String gradedAt) {
        this.gradedAt = gradedAt;
    }

    public boolean isTranscript() {
        return isTranscript;
    }

    public void setTranscript(boolean transcript) {
        isTranscript = transcript;
    }

    public boolean isMbkm() {
        return isMbkm;
    }

    public void setMbkm(boolean mbkm) {
        isMbkm = mbkm;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StudentCourse{" +
                "classId='" + classId + '\'' +
                ", grade=" + grade +
                ", gradedAt='" + gradedAt + '\'' +
                ", isTranscript=" + isTranscript +
                ", isMbkm=" + isMbkm +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
