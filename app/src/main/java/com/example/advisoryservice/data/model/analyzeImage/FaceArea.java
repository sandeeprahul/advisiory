package com.example.advisoryservice.data.model.analyzeImage;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceArea {

    @SerializedName("bounding_box")
    @Expose
    private List<Double> boundingBox = null;
    @SerializedName("rotation")
    @Expose
    private Double rotation;

    public List<Double> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(List<Double> boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Double getRotation() {
        return rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

}
