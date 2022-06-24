package com.example.advisoryservice.data.model.questions;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubQuestion {

    @SerializedName("subQuestionId")
    @Expose
    private String subQuestionId;
    @SerializedName("subQuestion")
    @Expose
    private String subQuestion;
    @SerializedName("subQuestionOptions")
    @Expose
    private List<SubQuestionOption> subQuestionOptions = null;

    @SerializedName("mandatory")
    @Expose
    private String mandatory;



    public String getSubQuestionId() {
        return subQuestionId;
    }

    public void setSubQuestionId(String subQuestionId) {
        this.subQuestionId = subQuestionId;
    }

    public String getSubQuestion() {
        return subQuestion;
    }

    public void setSubQuestion(String subQuestion) {
        this.subQuestion = subQuestion;
    }

    public List<SubQuestionOption> getSubQuestionOptions() {
        return subQuestionOptions;
    }

    public void setSubQuestionOptions(List<SubQuestionOption> subQuestionOptions) {
        this.subQuestionOptions = subQuestionOptions;
    }
    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }
}
