package com.example.advisoryservice.data.model.analyzeImage;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SumMeasure {

    @SerializedName("description")
    @Expose
    private String description;
    /*@SerializedName("value")
    @Expose
    private Double value;*/
    @SerializedName("value_raw")
    @Expose
    private Object valueRaw;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   /* public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }*/

    public Object getValueRaw() {
        return valueRaw;
    }

    public void setValueRaw(Object valueRaw) {
        this.valueRaw = valueRaw;
    }

}
