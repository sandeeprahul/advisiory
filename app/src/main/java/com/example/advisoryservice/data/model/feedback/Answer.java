package com.example.advisoryservice.data.model.feedback;

import com.example.advisoryservice.data.model.questions.SubQuestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class Answer {

    @SerializedName("optionId")
    @Expose
    private String optionId;
    @SerializedName("optionsName")
    @Expose
    private String optionsName;
    @SerializedName("optionsValue")
    @Expose
    private String optionsValue;
    @SerializedName("subQuestionStatus")
    @Expose
    private String subQuestionStatus;
    @SerializedName("subQuestion")
    @Expose
    private List<SubQuestion> subQuestion = null;

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

    public String getSubQuestionStatus() {
        return subQuestionStatus;
    }

    public void setSubQuestionStatus(String subQuestionStatus) {
        this.subQuestionStatus = subQuestionStatus;
    }
    public void setSubQuestion(List<SubQuestion> subQuestion) {
        this.subQuestion = subQuestion;
    }
    public List<SubQuestion> getSubQuestion() {
        return subQuestion;
    }

}
