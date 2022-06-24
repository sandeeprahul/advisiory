package com.example.advisoryservice.data.model.revieveDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("resultDescription")
    @Expose
    private String resultDescription;
    @SerializedName("resultColor")
    @Expose
    private String resultColor;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getResultColor() {
        return resultColor;
    }

    public void setResultColor(String resultColor) {
        this.resultColor = resultColor;
    }

}
