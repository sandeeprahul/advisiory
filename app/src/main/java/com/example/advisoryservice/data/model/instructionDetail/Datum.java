package com.example.advisoryservice.data.model.instructionDetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("headerText")
    @Expose
    private String headerText;
    @SerializedName("imageDetail")
    @Expose
    private List<ImageDetail> imageDetail = null;
    @SerializedName("footerText")
    @Expose
    private String footerText;

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public List<ImageDetail> getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(List<ImageDetail> imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

}
