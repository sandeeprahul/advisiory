package com.example.advisoryservice.data.model.revieve;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("LINE_NO")
    @Expose
    private String lINENO;

    @SerializedName("QUESTION_NO")
    @Expose
    private String qUESTIONNO;

    public String getqUESTIONNO() {
        return qUESTIONNO;
    }

    public void setqUESTIONNO(String qUESTIONNO) {
        this.qUESTIONNO = qUESTIONNO;
    }

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
