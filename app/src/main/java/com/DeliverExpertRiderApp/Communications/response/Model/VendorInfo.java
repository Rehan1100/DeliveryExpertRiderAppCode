package com.DeliverExpertRiderApp.Communications.response.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorInfo implements Serializable {


    //    vendor_info
    @SerializedName("hotel_name")
    @Expose
    private String hotel_name;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("delivery_charge")
    @Expose
    private String delivery_charge;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("Min_order_price")
    @Expose
    private String Min_order_price;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("timing")
    @Expose
    private String timing;


    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;


    public String getImage() {
        return image;
    }

    public String getAddress() {
        return Address;
    }

    public String getDetails() {
        return details;
    }

    public String getEmail() {
        return email;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public String getPhone() {
        return phone;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getStatus() {
        return status;
    }

    public String getMin_order_price() {
        return Min_order_price;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getRating() {
        return rating;
    }

    public String getTiming() {
        return timing;
    }

}
