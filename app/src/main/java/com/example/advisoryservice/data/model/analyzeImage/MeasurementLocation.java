package com.example.advisoryservice.data.model.analyzeImage;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeasurementLocation {

    @SerializedName("AOI_radius_o")
    @Expose
    private int aOIRadiusO;
    @SerializedName("AOI_x_o")
    @Expose
    private int aOIXO;
    @SerializedName("AOI_y_o")
    @Expose
    private int aOIYO;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("measures")
    @Expose
    private List<Measure> measures = null;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("value_raw")
    @Expose
    private Object valueRaw;
    @SerializedName("visualization_data")
    @Expose
    private Object visualizationData;

    public int getAOIRadiusO() {
        return aOIRadiusO;
    }

    public void setAOIRadiusO(int aOIRadiusO) {
        this.aOIRadiusO = aOIRadiusO;
    }

    public int getAOIXO() {
        return aOIXO;
    }

    public void setAOIXO(int aOIXO) {
        this.aOIXO = aOIXO;
    }

    public int getAOIYO() {
        return aOIYO;
    }

    public void setAOIYO(int aOIYO) {
        this.aOIYO = aOIYO;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Object getValueRaw() {
        return valueRaw;
    }

    public void setValueRaw(Object valueRaw) {
        this.valueRaw = valueRaw;
    }

    public Object getVisualizationData() {
        return visualizationData;
    }

    public void setVisualizationData(Object visualizationData) {
        this.visualizationData = visualizationData;
    }

}
