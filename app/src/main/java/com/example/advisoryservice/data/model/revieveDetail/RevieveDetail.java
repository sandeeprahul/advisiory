package com.example.advisoryservice.data.model.revieveDetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevieveDetail {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("skinScoreDescription")
    @Expose
    private String skinScoreDescription;
    @SerializedName("apiResponse")
    @Expose
    private List<ApiResponse> apiResponse = null;
    @SerializedName("mappingResponse")
    @Expose
    private List<MappingResponse> mappingResponse = null;
    @SerializedName("resultResponse")
    @Expose
    private List<ResultResponse> resultResponse = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSkinScoreDescription() {
        return skinScoreDescription;
    }

    public void setSkinScoreDescription(String skinScoreDescription) {
        this.skinScoreDescription = skinScoreDescription;
    }

    public List<ApiResponse> getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(List<ApiResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }

    public List<MappingResponse> getMappingResponse() {
        return mappingResponse;
    }

    public void setMappingResponse(List<MappingResponse> mappingResponse) {
        this.mappingResponse = mappingResponse;
    }

    public List<ResultResponse> getResultResponse() {
        return resultResponse;
    }

    public void setResultResponse(List<ResultResponse> resultResponse) {
        this.resultResponse = resultResponse;
    }

}
