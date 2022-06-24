package com.example.advisoryservice.data.model.questions;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("optionId")
    @Expose
    private String optionId;
    @SerializedName("optionsName")
    @Expose
    private String optionsName;
    @SerializedName("optionsValue")
    @Expose
    private String optionsValue;
    @SerializedName("maleInfoText")
    @Expose
    private String maleInfoText;
    @SerializedName("femaleInfoText")
    @Expose
    private String femaleInfoText;
    @SerializedName("infoImgUrl")
    @Expose
    private String infoImgUrl;
    @SerializedName("maleImgUrl")
    @Expose
    private String maleImgUrl;
    @SerializedName("femaleImgUrl")
    @Expose
    private String femaleImgUrl;
    @SerializedName("subQuestionStatus")
    @Expose
    private String subQuestionStatus;
    @SerializedName("subQuestion")
    @Expose
    private List<SubQuestion> subQuestion = null;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    @SerializedName("infoText")
    @Expose
    private String infoText;

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionsName() {
        return optionsName;
    }

    public void setOptionsName(String optionsName) {
        this.optionsName = optionsName;
    }

    public String getOptionsValue() {
        return optionsValue;
    }

    public void setOptionsValue(String optionsValue) {
        this.optionsValue = optionsValue;
    }

    public String getMaleInfoText() {
        return maleInfoText;
    }

    public void setMaleInfoText(String maleInfoText) {
        this.maleInfoText = maleInfoText;
    }

    public String getFemaleInfoText() {
        return femaleInfoText;
    }

    public void setFemaleInfoText(String femaleInfoText) {
        this.femaleInfoText = femaleInfoText;
    }

    public String getInfoImgUrl() {
        return infoImgUrl;
    }

    public void setInfoImgUrl(String infoImgUrl) {
        this.infoImgUrl = infoImgUrl;
    }

    public String getMaleImgUrl() {
        return maleImgUrl;
    }

    public void setMaleImgUrl(String maleImgUrl) {
        this.maleImgUrl = maleImgUrl;
    }

    public String getFemaleImgUrl() {
        return femaleImgUrl;
    }

    public void setFemaleImgUrl(String femaleImgUrl) {
        this.femaleImgUrl = femaleImgUrl;
    }

    public String getSubQuestionStatus() {
        return subQuestionStatus;
    }

    public void setSubQuestionStatus(String subQuestionStatus) {
        this.subQuestionStatus = subQuestionStatus;
    }

    public List<SubQuestion> getSubQuestion() {
        return subQuestion;
    }

    public void setSubQuestion(List<SubQuestion> subQuestion) {
        this.subQuestion = subQuestion;
    }

}
