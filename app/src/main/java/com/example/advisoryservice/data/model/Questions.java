package com.example.advisoryservice.data.model;

import java.util.List;

import com.example.advisoryservice.data.model.feedback.Answer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Questions {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<com.example.advisoryservice.data.model.questions.Datum> data = null;
    @SerializedName("answer")
    @Expose
    private List<Answer> answer = null;
    @SerializedName("defaultImage")
    @Expose
    private String defaultImage;

    @SerializedName("questionName")
    @Expose
    private String questionName;

    @SerializedName("questionId")
    @Expose
    private String questionId;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<com.example.advisoryservice.data.model.questions.Datum> getData() {
        return data;
    }

    public void setData(List<com.example.advisoryservice.data.model.questions.Datum> data) {
        this.data = data;
    }

}