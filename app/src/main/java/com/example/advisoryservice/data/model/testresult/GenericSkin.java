package com.example.advisoryservice.data.model.testresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericSkin {

    @SerializedName("genericSkinTipsUrl")
    @Expose
    private String genericSkinTipsUrl;

    public String getGenericSkinTipsUrl() {
        return genericSkinTipsUrl;
    }

    public void setGenericSkinTipsUrl(String genericSkinTipsUrl) {
        this.genericSkinTipsUrl = genericSkinTipsUrl;
    }

}
