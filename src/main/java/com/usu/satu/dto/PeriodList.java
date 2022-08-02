package com.usu.satu.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class PeriodList {
    @Field("period_id")
    private String periodId;

    @Field("status_lists")
    private List<StatusList> statusLists;

    public PeriodList() {
    }

    public PeriodList(String periodId, List<StatusList> statusLists) {
        this.periodId = periodId;
        this.statusLists = statusLists;
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

    @Override
    public String toString() {
        return "PeriodList{" +
                "periodId='" + periodId + '\'' +
                ", statusLists=" + statusLists +
                '}';
    }
}
