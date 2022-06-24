package com.example.advisoryservice.data.model.testresult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDetailResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("skinExpertRatingUrl")
    @Expose
    private String skinExpertRatingUrl;
    @SerializedName("productRatingUrl")
    @Expose
    private String productRatingUrl;
    @SerializedName("skinType")
    @Expose
    private String skinType;
    @SerializedName("summary")
    @Expose
    private List<Summary> summary = null;
    @SerializedName("regimeHeader")
    @Expose
    private List<RegimeHeader> regimeHeader = null;
    @SerializedName("regimeSchedule")
    @Expose
    private List<RegimeSchedule> regimeSchedule = null;
    @SerializedName("genericSkin")
    @Expose
    private List<GenericSkin> genericSkin = null;
    @SerializedName("filter")
    @Expose
    private List<Filter> filter = null;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;

    @SerializedName("imageOutput")
    @Expose
    private List<ImageOutput> imageOutput = null;

    public List<ImageOutput> getImageOutput() {
        return imageOutput;
    }

    public void setImageOutput(List<ImageOutput> imageOutput) {
        this.imageOutput = imageOutput;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSkinExpertRatingUrl() {
        return skinExpertRatingUrl;
    }

    public void setSkinExpertRatingUrl(String skinExpertRatingUrl) {
        this.skinExpertRatingUrl = skinExpertRatingUrl;
    }

    public String getProductRatingUrl() {
        return productRatingUrl;
    }

    public void setProductRatingUrl(String productRatingUrl) {
        this.productRatingUrl = productRatingUrl;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public List<Summary> getSummary() {
        return summary;
    }

    public void setSummary(List<Summary> summary) {
        this.summary = summary;
    }

    public List<RegimeHeader> getRegimeHeader() {
        return regimeHeader;
    }

    public void setRegimeHeader(List<RegimeHeader> regimeHeader) {
        this.regimeHeader = regimeHeader;
    }

    public List<RegimeSchedule> getRegimeSchedule() {
        return regimeSchedule;
    }

    public void setRegimeSchedule(List<RegimeSchedule> regimeSchedule) {
        this.regimeSchedule = regimeSchedule;
    }

    public List<GenericSkin> getGenericSkin() {
        return genericSkin;
    }

    public void setGenericSkin(List<GenericSkin> genericSkin) {
        this.genericSkin = genericSkin;
    }

    public List<Filter> getFilter() {
        return filter;
    }

    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

}
