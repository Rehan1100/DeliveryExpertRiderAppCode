package com.DeliverExpertRiderApp.Communications.response.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {

    @SerializedName("id")
    @Expose
    private String userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("online_status")
    @Expose
    private String online_status;

    @SerializedName("image")
    @Expose
    private String image;


    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }

    public String getOnline_status() {
        return online_status;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public UserData(String userId, String name, String email, String phone, String address, String password, String online_status, String image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        Address = address;
        this.password = password;
        this.online_status = online_status;
        this.image = image;
    }
}