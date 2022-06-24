package com.example.advisoryservice.data.model.instructionDetail;

import com.example.advisoryservice.data.model.Questions;
import com.example.advisoryservice.data.model.Service;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("introImageUrl")
    @Expose
    private String introImage;
    @SerializedName("question")
    @Expose
    private List<Questions> question = null;

    @SerializedName("service")
    @Expose
    private List<Service> service = null;

    @SerializedName("serviceTittle")
    @Expose
    private String serviceTittle;

    public String getServiceBackgroundImage() {
        return serviceBackgroundImage;
    }

    public void setServiceBackgroundImage(String serviceBackgroundImage) {
        this.serviceBackgroundImage = serviceBackgroundImage;
    }

    @SerializedName("serviceBackgroundImage")
    @Expose
    private String serviceBackgroundImage;


    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public String getServiceTittle() {
        return serviceTittle;
    }

    public void setServiceTittle(String serviceTittle) {
        this.serviceTittle = serviceTittle;
    }

    public List<Questions> getQuestion() {
        return question;
    }

    public void setQuestion(List<Questions> question) {
        this.question = question;
    }

    public String getIntroImage() {
        return introImage;
    }

    public void setIntroImage(String introImage) {
        this.introImage = introImage;
    }
}
