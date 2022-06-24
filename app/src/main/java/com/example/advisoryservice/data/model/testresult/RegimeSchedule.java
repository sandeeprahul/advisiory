package com.example.advisoryservice.data.model.testresult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegimeSchedule {

    @SerializedName("regimeCategoryId")
    @Expose
    private String regimeCategoryId;
    @SerializedName("regimeCategoryName")
    @Expose
    private String regimeCategoryName;
    @SerializedName("headerColorCode")
    @Expose
    private String headerColorCode;
    @SerializedName("subSectionColorCode")
    @Expose
    private String subSectionColorCode;
    @SerializedName("regimeSubCategory")
    @Expose
    private List<RegimeSubCategory> regimeSubCategory = null;

    public String getRegimeCategoryId() {
        return regimeCategoryId;
    }

    public void setRegimeCategoryId(String regimeCategoryId) {
        this.regimeCategoryId = regimeCategoryId;
    }

    public String getRegimeCategoryName() {
        return regimeCategoryName;
    }

    public void setRegimeCategoryName(String regimeCategoryName) {
        this.regimeCategoryName = regimeCategoryName;
    }

    public String getHeaderColorCode() {
        return headerColorCode;
    }

    public void setHeaderColorCode(String headerColorCode) {
        this.headerColorCode = headerColorCode;
    }

    public String getSubSectionColorCode() {
        return subSectionColorCode;
    }

    public void setSubSectionColorCode(String subSectionColorCode) {
        this.subSectionColorCode = subSectionColorCode;
    }

    public List<RegimeSubCategory> getRegimeSubCategory() {
        return regimeSubCategory;
    }

    public void setRegimeSubCategory(List<RegimeSubCategory> regimeSubCategory) {
        this.regimeSubCategory = regimeSubCategory;
    }

}
