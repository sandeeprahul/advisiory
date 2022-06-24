package com.example.advisoryservice.data.model.analyzeImage;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("local_warnings")
    @Expose
    private List<Object> localWarnings = null;
    @SerializedName("measurement_locations")
    @Expose
    private List<MeasurementLocation> measurementLocations = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sum_measures")
    @Expose
    private List<SumMeasure> sumMeasures = null;
    @SerializedName("value")
    @Expose
    private double value;
    @SerializedName("value_raw")
    @Expose
    private Object valueRaw;
    @SerializedName("visualization_data")
    @Expose
    private Object visualizationData;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getLocalWarnings() {
        return localWarnings;
    }

    public void setLocalWarnings(List<Object> localWarnings) {
        this.localWarnings = localWarnings;
    }

    public List<MeasurementLocation> getMeasurementLocations() {
        return measurementLocations;
    }

    public void setMeasurementLocations(List<MeasurementLocation> measurementLocations) {
        this.measurementLocations = measurementLocations;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SumMeasure> getSumMeasures() {
        return sumMeasures;
    }

    public void setSumMeasures(List<SumMeasure> sumMeasures) {
        this.sumMeasures = sumMeasures;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
