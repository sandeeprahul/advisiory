package com.example.advisoryservice.data.model.testresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegimeSubCategory {

    @SerializedName("regimeSubCategoryId")
    @Expose
    private String regimeSubCategoryId;
    @SerializedName("regimeSubCategoryName")
    @Expose
    private String regimeSubCategoryName;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getRegimeSubCategoryId() {
        return regimeSubCategoryId;
    }

    public void setRegimeSubCategoryId(String regimeSubCategoryId) {
        this.regimeSubCategoryId = regimeSubCategoryId;
    }

    public String getRegimeSubCategoryName() {
        return regimeSubCategoryName;
    }

    public void setRegimeSubCategoryName(String regimeSubCategoryName) {
        this.regimeSubCategoryName = regimeSubCategoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
