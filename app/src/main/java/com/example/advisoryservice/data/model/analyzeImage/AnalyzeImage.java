package com.example.advisoryservice.data.model.analyzeImage;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnalyzeImage {

    @SerializedName("face_area")
    @Expose
    private FaceArea faceArea;
    /*@SerializedName("inverse_color_balance_rb")
    @Expose
    private List<Double> inverseColorBalanceRb = null;*/
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private List<Status> status = null;

    public FaceArea getFaceArea() {
        return faceArea;
    }

    public void setFaceArea(FaceArea faceArea) {
        this.faceArea = faceArea;
    }

   /* public List<Double> getInverseColorBalanceRb() {
        return inverseColorBalanceRb;
    }

    public void setInverseColorBalanceRb(List<Double> inverseColorBalanceRb) {
        this.inverseColorBalanceRb = inverseColorBalanceRb;
    }*/

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

}
