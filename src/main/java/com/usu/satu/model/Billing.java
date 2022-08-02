package com.usu.satu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.usu.satu.dto.BillingItem;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "billings")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Billing {

    @Id
    private String id;

    @Field("period_id")
    private String periodId;

    @Field("bill_items")
    private List<BillingItem> billingItems;

    private String nim;

    @Field("trx_id")
    private String trxId;

    @Field("virtual_account")
    private String virtualAccount;

    @Field("bank_code")
    private String bankCode;

    @Field("trx_amount")
    private Decimal128 trxAmount;   // total bill

    @Field("datetime_payment")
    private LocalDateTime datetimePayment;

    @Field("datetime_created")
    private LocalDateTime datetimeCreated;

    @Field("datetime_expired")
    private LocalDateTime datetimeExpired;

    @Field("payment_ntb")
    private String paymentNtb;      // billing code from bank

    @Field("payment_amount")
    private Decimal128 paymentAmount;   // total paid

    @Field("payment_type")
    private String paymentType;     // h2h, va

    @Field("is_paid")
    private boolean isPaid;

    @Field("ukt_level")
    private String uktLevel;

    @CreatedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Billing() {
    }

    public Billing(String id, String periodId, List<BillingItem> billingItems, String nim, String trxId, String virtualAccount, String bankCode, Decimal128 trxAmount, LocalDateTime datetimePayment, LocalDateTime datetimeCreated, LocalDateTime datetimeExpired, String paymentNtb, Decimal128 paymentAmount, String paymentType, boolean isPaid, String uktLevel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.periodId = periodId;
        this.billingItems = billingItems;
        this.nim = nim;
        this.trxId = trxId;
        this.virtualAccount = virtualAccount;
        this.bankCode = bankCode;
        this.trxAmount = trxAmount;
        this.datetimePayment = datetimePayment;
        this.datetimeCreated = datetimeCreated;
        this.datetimeExpired = datetimeExpired;
        this.paymentNtb = paymentNtb;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.isPaid = isPaid;
        this.uktLevel = uktLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<BillingItem> getBillingItems() {
        return billingItems;
    }

    public void setBillingItems(List<BillingItem> billingItems) {
        this.billingItems = billingItems;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(String virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Decimal128 getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(Decimal128 trxAmount) {
        this.trxAmount = trxAmount;
    }

    public LocalDateTime getDatetimePayment() {
        return datetimePayment;
    }

    public void setDatetimePayment(LocalDateTime datetimePayment) {
        this.datetimePayment = datetimePayment;
    }

    public LocalDateTime getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(LocalDateTime datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public LocalDateTime getDatetimeExpired() {
        return datetimeExpired;
    }

    public void setDatetimeExpired(LocalDateTime datetimeExpired) {
        this.datetimeExpired = datetimeExpired;
    }

    public String getPaymentNtb() {
        return paymentNtb;
    }

    public void setPaymentNtb(String paymentNtb) {
        this.paymentNtb = paymentNtb;
    }

    public Decimal128 getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Decimal128 paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getUktLevel() {
        return uktLevel;
    }

    public void setUktLevel(String uktLevel) {
        this.uktLevel = uktLevel;
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

    @Override
    public String toString() {
        return "Billing{" +
                "id='" + id + '\'' +
                ", periodId='" + periodId + '\'' +
                ", billingItems=" + billingItems +
                ", nim='" + nim + '\'' +
                ", trxId='" + trxId + '\'' +
                ", virtualAccount='" + virtualAccount + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", trxAmount=" + trxAmount +
                ", datetimePayment=" + datetimePayment +
                ", datetimeCreated=" + datetimeCreated +
                ", datetimeExpired=" + datetimeExpired +
                ", paymentNtb='" + paymentNtb + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentType='" + paymentType + '\'' +
                ", isPaid=" + isPaid +
                ", uktLevel='" + uktLevel + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
