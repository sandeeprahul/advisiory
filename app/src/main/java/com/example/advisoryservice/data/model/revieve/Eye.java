package com.example.advisoryservice.data.model.revieve;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Eye {

    @SerializedName("LINE_NO")
    @Expose
    private String lINENO;
    @SerializedName("CATEGORY")
    @Expose
    private String cATEGORY;
    @SerializedName("PERCENTAGE")
    @Expose
    private String pERCENTAGE;
    @SerializedName("OPTION_VALUE")
    @Expose
    private String oPTIONVALUE;

    public String getLINENO() {
        return lINENO;
    }

    public void setLINENO(String lINENO) {
        this.lINENO = lINENO;
    }

    public String getCATEGORY() {
        return cATEGORY;
    }

    public void setCATEGORY(String cATEGORY) {
        this.cATEGORY = cATEGORY;
    }

    public String getPERCENTAGE() {
        return pERCENTAGE;
    }

    public void setPERCENTAGE(String pERCENTAGE) {
        this.pERCENTAGE = pERCENTAGE;
    }

    public String getOPTIONVALUE() {
        return oPTIONVALUE;
    }

    public void setOPTIONVALUE(String oPTIONVALUE) {
        this.oPTIONVALUE = oPTIONVALUE;
    }
}