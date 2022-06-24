package com.example.advisoryservice.data.model.testresult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategory {

    @SerializedName("subCategoryId")
    @Expose
    private String subCategoryId;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("topProducts")
    @Expose
    private List<TopProduct> topProducts = null;
    @SerializedName("restProducts")
    @Expose
    private List<RestProduct> restProducts = null;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public List<TopProduct> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<TopProduct> topProducts) {
        this.topProducts = topProducts;
    }

    public List<RestProduct> getRestProducts() {
        return restProducts;
    }

    public void setRestProducts(List<RestProduct> restProducts) {
        this.restProducts = restProducts;
    }

}
