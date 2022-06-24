package com.example.advisoryservice.data.model.feedback;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("questionId")
    @Expose
    private String questionId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("subQuestionStatus")
    @Expose
    private String subQuestionStatus;
    @SerializedName("multipleOption")
    @Expose
    private String multipleOption;
    @SerializedName("mandatory")
    @Expose
    private String mandatory;
    @SerializedName("answer")
    @Expose
    private List<Answer> answer = null;

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

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

}
