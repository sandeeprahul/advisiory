package com.example.advisoryservice.data.model.testresult;

import com.example.advisoryservice.data.model.revieveDetail.ApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageOutput {
    public String getSkinScoreDescription() {
        return skinScoreDescription;
    }

    public void setSkinScoreDescription(String skinScoreDescription) {
        this.skinScoreDescription = skinScoreDescription;
    }

    @SerializedName("skinScoreDescription")
    @Expose
    private String skinScoreDescription;
    @SerializedName("apiResponse")
    @Expose
    private List<ApiResponse> apiResponse = null;

    public List<ApiResponse> getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(List<ApiResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }
}
