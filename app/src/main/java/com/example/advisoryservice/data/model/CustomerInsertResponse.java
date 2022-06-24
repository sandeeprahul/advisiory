package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerInsertResponse {

    @SerializedName("Message")
    @Expose
    private String Messages;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("custTransId")
    @Expose
    private String custTransId;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;

    public String getMessages() {
        return Messages;
    }

    public void setMessages(String messages) {
        Messages = messages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustTransId() {
        return custTransId;
    }

    public void setCustTransId(String custTransId) {
        this.custTransId = custTransId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
