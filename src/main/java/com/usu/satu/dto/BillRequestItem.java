package com.usu.satu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;

public class BillRequestItem {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("item_price")
    private Decimal128 itemPrice;

    @JsonProperty("item_qty")
    private int itemQty;

    @JsonProperty("code_ref")
    private String codeRef;

    private String ref;

    public BillRequestItem(String itemName, Decimal128 itemPrice, int itemQty, String codeRef, String ref) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
        this.codeRef = codeRef;
        this.ref = ref;
    }

    public BillRequestItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Decimal128 getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Decimal128 itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "BillRequestItem{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemQty=" + itemQty +
                ", codeRef='" + codeRef + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
