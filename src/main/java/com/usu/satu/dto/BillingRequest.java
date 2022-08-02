package com.usu.satu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillingRequest {
    private String ref;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_email")
    private String customerEmail;

    private String phone;

    @JsonProperty("virtual_account")
    private String virtualAccount;

    private String description;

    private String cid;

    private List<BillRequestItem> items;

    @JsonProperty("ukt_info")
    private String uktInfo;

    private String currency;

    @JsonProperty("callback_bill")
    private String callbackBill;

    @JsonProperty("datetime_expired")
    private String datetimeExpired;

    public BillingRequest(String ref, String customerName, String customerEmail, String phone, String virtualAccount, String description, String cid, List<BillRequestItem> items, String uktInfo, String currency, String callbackBill, String datetimeExpired) {
        this.ref = ref;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.phone = phone;
        this.virtualAccount = virtualAccount;
        this.description = description;
        this.cid = cid;
        this.items = items;
        this.uktInfo = uktInfo;
        this.currency = currency;
        this.callbackBill = callbackBill;
        this.datetimeExpired = datetimeExpired;
    }

    public BillingRequest() {
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(String virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<BillRequestItem> getItems() {
        return items;
    }

    public void setItems(List<BillRequestItem> items) {
        this.items = items;
    }

    public String getUktInfo() {
        return uktInfo;
    }

    public void setUktInfo(String uktInfo) {
        this.uktInfo = uktInfo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCallbackBill() {
        return callbackBill;
    }

    public void setCallbackBill(String callbackBill) {
        this.callbackBill = callbackBill;
    }

    public String getDatetimeExpired() {
        return datetimeExpired;
    }

    public void setDatetimeExpired(String datetimeExpired) {
        this.datetimeExpired = datetimeExpired;
    }

    @Override
    public String toString() {
        return "BillingRequest{" +
                "ref='" + ref + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", phone='" + phone + '\'' +
                ", virtualAccount='" + virtualAccount + '\'' +
                ", description='" + description + '\'' +
                ", cid='" + cid + '\'' +
                ", items=" + items +
                ", uktInfo='" + uktInfo + '\'' +
                ", currency='" + currency + '\'' +
                ", callbackBill='" + callbackBill + '\'' +
                ", datetimeExpired='" + datetimeExpired + '\'' +
                '}';
    }
}
