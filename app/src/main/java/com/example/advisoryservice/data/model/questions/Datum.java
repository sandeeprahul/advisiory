package com.example.advisoryservice.data.model.questions;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("defaultImage")
    @Expose
    private String defaultImage;

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    @SerializedName("questionId")
    @Expose
    private String questionId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("defaultImageMale")
    @Expose
    private String defaultImageMale;
    @SerializedName("defaultImageFemale")
    @Expose
    private String defaultImageFemale;
    @SerializedName("subQuestionStatus")
    @Expose
    private String subQuestionStatus;
    @SerializedName("multipleOption")
    @Expose
    private String multipleOption;
    @SerializedName("mandatory")
    @Expose
    private String mandatory;
    @SerializedName("isValidateFlag")
    @Expose
    private String isValidateFlag;
    @SerializedName("isInfoFlag")
    @Expose
    private String isInfoFlag;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;



    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDefaultImageMale() {
        return defaultImageMale;
    }

    public void setDefaultImageMale(String defaultImageMale) {
        this.defaultImageMale = defaultImageMale;
    }

    public String getDefaultImageFemale() {
        return defaultImageFemale;
    }

    public void setDefaultImageFemale(String defaultImageFemale) {
        this.defaultImageFemale = defaultImageFemale;
    }

    public String getSubQuestionStatus() {
        return subQuestionStatus;
    }

    public void setSubQuestionStatus(String subQuestionStatus) {
        this.subQuestionStatus = subQuestionStatus;
    }

    public String getMultipleOption() {
        return multipleOption;
    }

    public void setMultipleOption(String multipleOption) {
        this.multipleOption = multipleOption;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getIsValidateFlag() {
        return isValidateFlag;
    }

    public void setIsValidateFlag(String isValidateFlag) {
        this.isValidateFlag = isValidateFlag;
    }

    public String getIsInfoFlag() {
        return isInfoFlag;
    }

    public void setIsInfoFlag(String isInfoFlag) {
        this.isInfoFlag = isInfoFlag;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

}
