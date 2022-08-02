package com.usu.satu.model;

import com.usu.satu.dto.StatusList;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "student_status")
public class StudentStatus {

    @Id
    private ObjectId id;
    private String nim;

    @Field("period_id")
    private String periodId;

    @Field("status_lists")
    private List<StatusList> statusLists;

    @Field("created_at")
    private LocalDateTime createdAt;

    public StudentStatus() {
    }

    public StudentStatus(ObjectId id, String nim, String periodId, List<StatusList> statusLists, LocalDateTime createdAt) {
        this.id = id;
        this.nim = nim;
        this.periodId = periodId;
        this.statusLists = statusLists;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<StatusList> getStatusLists() {
        return statusLists;
    }

    public void setStatusLists(List<StatusList> statusLists) {
        this.statusLists = statusLists;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StudentStatus{" +
                "id=" + id +
                ", nim='" + nim + '\'' +
                ", periodId='" + periodId + '\'' +
                ", statusLists=" + statusLists +
                ", createdAt=" + createdAt +
                '}';
    }
}
