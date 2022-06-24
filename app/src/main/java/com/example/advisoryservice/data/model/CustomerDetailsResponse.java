package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailsResponse {

    @SerializedName("JoinedDate")
    @Expose
    private String joinedDate;
    @SerializedName("StoreName")
    @Expose
    private Object storeName;
    @SerializedName("BeneficiaryName")
    @Expose
    private Object beneficiaryName;
    @SerializedName("Code")
    @Expose
    private Object code;
    @SerializedName("JoinedFrom")
    @Expose
    private Integer joinedFrom;
    @SerializedName("Image")
    @Expose
    private Object image;
    @SerializedName("Pin")
    @Expose
    private Object pin;
    @SerializedName("LoyaltyTierId")
    @Expose
    private Integer loyaltyTierId;
    @SerializedName("Anniversaryday")
    @Expose
    private Object anniversaryday;
    @SerializedName("Birthday")
    @Expose
    private Object birthday;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("StateId")
    @Expose
    private Integer stateId;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
    @SerializedName("CustomerId")
    @Expose
    private Integer customerId;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Phone")
    @Expose
    private Object phone;
    @SerializedName("Gender")
    @Expose
    private Integer gender;

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Object getStoreName() {
        return storeName;
    }

    public void setStoreName(Object storeName) {
        this.storeName = storeName;
    }

    public Object getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(Object beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Integer getJoinedFrom() {
        return joinedFrom;
    }

    public void setJoinedFrom(Integer joinedFrom) {
        this.joinedFrom = joinedFrom;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getPin() {
        return pin;
    }

    public void setPin(Object pin) {
        this.pin = pin;
    }

    public Integer getLoyaltyTierId() {
        return loyaltyTierId;
    }

    public void setLoyaltyTierId(Integer loyaltyTierId) {
        this.loyaltyTierId = loyaltyTierId;
    }

    public Object getAnniversaryday() {
        return anniversaryday;
    }

    public void setAnniversaryday(Object anniversaryday) {
        this.anniversaryday = anniversaryday;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

}