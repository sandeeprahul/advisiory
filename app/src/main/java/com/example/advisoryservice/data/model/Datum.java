package com.example.advisoryservice.data.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("introTitle")
    @Expose
    private String introTitle;
    @SerializedName("introduction")
    @Expose
    private String introduction;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("introImageUrl")
    @Expose
    private String introImageUrl;
    @SerializedName("skinexpertLogo")
    @Expose
    private String skinexpertLogo;
    @SerializedName("imageDetail")
    @Expose
    private List<ImageDetail> imageDetail = null;

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroImageUrl() {
        return introImageUrl;
    }

    public void setIntroImageUrl(String introImageUrl) {
        this.introImageUrl = introImageUrl;
    }

    public List<ImageDetail> getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(List<ImageDetail> imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getSkinexpertLogo() {
        return skinexpertLogo;
    }

    public void setSkinexpertLogo(String skinexpertLogo) {
        this.skinexpertLogo = skinexpertLogo;
    }
}
