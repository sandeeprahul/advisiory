package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreLoginResponse {
    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("$type")
    @Expose
    private String $type;
    @SerializedName("IsAutheticated")
    @Expose
    private Boolean isAutheticated;
    @SerializedName("AccountId")
    @Expose
    private Integer accountId;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("StoreId")
    @Expose
    private Integer storeId;
    @SerializedName("storeName")
    @Expose
    private String storeName;
    @SerializedName("StoreOutletId")
    @Expose
    private Integer storeOutletId;
    @SerializedName("CustomerId")
    @Expose
    private Integer customerId;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("AccountType")
    @Expose
    private String accountType;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("Logo")
    @Expose
    private String logo;
    @SerializedName("LoyaltyName")
    @Expose
    private String loyaltyName;
    @SerializedName("LoyaltyProgramType")
    @Expose
    private String loyaltyProgramType;
    @SerializedName("Url")
    @Expose
    private Object url;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public Boolean getIsAutheticated() {
        return isAutheticated;
    }

    public void setIsAutheticated(Boolean isAutheticated) {
        this.isAutheticated = isAutheticated;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreOutletId() {
        return storeOutletId;
    }

    public void setStoreOutletId(Integer storeOutletId) {
        this.storeOutletId = storeOutletId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLoyaltyName() {
        return loyaltyName;
    }

    public void setLoyaltyName(String loyaltyName) {
        this.loyaltyName = loyaltyName;
    }

    public String getLoyaltyProgramType() {
        return loyaltyProgramType;
    }

    public void setLoyaltyProgramType(String loyaltyProgramType) {
        this.loyaltyProgramType = loyaltyProgramType;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

}
