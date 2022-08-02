package com.usu.satu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usu.satu.dto.StudentCourse;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document(collection = "study_cards")
public class StudyCard {
    @Id
    private String id;
    private String nim;

    @Field("period_taken")
    private String periodTaken;

    private String periodName;

    @Field("schedule_id")
    private String scheduleId;

    @Field("student_courses")
    private ArrayList<StudentCourse> studentCourses;

    @Field("is_deleted")
    private boolean isDeleted;

    @Field("is_processed_by_PA")
    private boolean isProcessedByPA = false;

    @Field("processed_at")
    private String processedAt;

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

    public StudyCard(String id, String nim, String periodTaken, String periodName, String scheduleId, ArrayList<StudentCourse> studentCourses, boolean isDeleted, boolean isProcessed, String processedAt, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.nim = nim;
        this.periodTaken = periodTaken;
        this.periodName = periodName;
        this.scheduleId = scheduleId;
        this.studentCourses = studentCourses;
        this.isDeleted = isDeleted;
        this.isProcessedByPA = isProcessed;
        this.processedAt = processedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public StudyCard() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getPeriodTaken() {
        return periodTaken;
    }

    public void setPeriodTaken(String periodTaken) {
        this.periodTaken = periodTaken;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ArrayList<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(ArrayList<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isProcessedByPA() {
        return isProcessedByPA;
    }

    public void setProcessedByPA(boolean processedByPA) {
        isProcessedByPA = processedByPA;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
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

    @Override
    public String toString() {
        return "StudyCard{" +
                "id='" + id + '\'' +
                ", nim='" + nim + '\'' +
                ", periodTaken='" + periodTaken + '\'' +
                ", periodName='" + periodName + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", studentCourses=" + studentCourses +
                ", isDeleted=" + isDeleted +
                ", isProcessed=" + isProcessedByPA +
                ", processedAt='" + processedAt + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
