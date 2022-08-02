package com.usu.satu.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class StatusList {
    @Field("updated_at")
    private LocalDateTime updatedAt;

    private String name;
    private boolean status;

    @Field("schema_id")
    private String schemaId;

    public StatusList() {
    }

    public StatusList(LocalDateTime updatedAt, String name, boolean status, String schemaId) {
        this.updatedAt = updatedAt;
        this.name = name;
        this.status = status;
        this.schemaId = schemaId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    @Override
    public String toString() {
        return "StatusList{" +
                "updatedAt='" + updatedAt + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", schemaId='" + schemaId + '\'' +
                '}';
    }
}
