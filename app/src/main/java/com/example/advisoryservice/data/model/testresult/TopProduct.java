package com.example.advisoryservice.data.model.testresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopProduct {

    @SerializedName("skuCode")
    @Expose
    private String skuCode;
    @SerializedName("skuName")
    @Expose
    private String skuName;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("productImageUrl")
    @Expose
    private String productImageUrl;
    @SerializedName("productRating")
    @Expose
    private String productRating;
    @SerializedName("skinExpertRating")
    @Expose
    private String skinExpertRating;
    @SerializedName("filterCategory")
    @Expose
    private String filterCategory;
    @SerializedName("skuUrlLink")
    @Expose
    private String skuUrlLink;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }

    public String getSkinExpertRating() {
        return skinExpertRating;
    }

    public void setSkinExpertRating(String skinExpertRating) {
        this.skinExpertRating = skinExpertRating;
    }

    public String getFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(String filterCategory) {
        this.filterCategory = filterCategory;
    }

    public String getSkuUrlLink() {
        return skuUrlLink;
    }

    public void setSkuUrlLink(String skuUrlLink) {
        this.skuUrlLink = skuUrlLink;
    }

}
