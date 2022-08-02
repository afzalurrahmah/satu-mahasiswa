package com.usu.satu.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillingItem {
    private String name;
    private Decimal128 amount;
    @Field("fee_type_slug")
    private String feeTypeSlug;

    public BillingItem(String name, Decimal128 amount, String feeTypeSlug) {
        this.name = name;
        this.amount = amount;
        this.feeTypeSlug = feeTypeSlug;
    }

    public BillingItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Decimal128 getAmount() {
        return amount;
    }

    public void setAmount(Decimal128 amount) {
        this.amount = amount;
    }

    public String getFeeTypeSlug() {
        return feeTypeSlug;
    }

    public void setFeeTypeSlug(String feeTypeSlug) {
        this.feeTypeSlug = feeTypeSlug;
    }

    @Override
    public String toString() {
        return "BillingItem{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", feeTypeSlug='" + feeTypeSlug + '\'' +
                '}';
    }
}
