package com.usu.satu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usu.satu.dto.StudentAttendance;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "presences")
public class Presence {
    @Id
    private String id;

    @Field("class_id")
    private String classId;

    private List<StudentAttendance> student;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @Field("present_at")
    private String presentAt;

    @CreatedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy;

    @Field("is_deleted")
    private boolean isDeleted;

    public Presence(String id, String classId, List<StudentAttendance> student, String presentAt, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, boolean isDeleted) {
        this.id = id;
        this.classId = classId;
        this.student = student;
        this.presentAt = presentAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isDeleted = isDeleted;
    }

    public Presence() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<StudentAttendance> getStudent() {
        return student;
    }

    public void setStudent(List<StudentAttendance> student) {
        this.student = student;
    }

    public String getPresentAt() {
        return presentAt;
    }

    public void setPresentAt(String presentAt) {
        this.presentAt = presentAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Presence{" +
                "id='" + id + '\'' +
                ", classId='" + classId + '\'' +
                ", student=" + student +
                ", presentAt='" + presentAt + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
