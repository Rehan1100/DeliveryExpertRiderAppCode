package com.DeliverExpertRiderApp.Communications.response.Model;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
@Entity(tableName = "Orders")
public class OrderDetail implements Serializable {






    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("total_order_price")
    @Expose
    private String total_order_price;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("coupon")
    @Expose
    private String coupon;

    @SerializedName("business_id")
    @Expose
    private String business_id;

    @SerializedName("rider_id")
    @Expose
    private String rider_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("drop_lat")
    @Expose
    private String drop_lat;

    @SerializedName("drop_lng")
    @Expose
    private String drop_lng;

    @SerializedName("vendor_info")
    @Expose
    private VendorInfo vendor_info;

    public OrderDetail(String userId, String id, String total_order_price, String message, String address, String status, String coupon, String business_id, String rider_id, String created_at, String drop_lat, String drop_lng, VendorInfo vendor_info) {
        this.userId = userId;
        this.id = id;
        this.total_order_price = total_order_price;
        this.message = message;
        Address = address;
        this.status = status;
        this.coupon = coupon;
        this.business_id = business_id;
        this.rider_id = rider_id;
        this.created_at = created_at;
        this.drop_lat = drop_lat;
        this.drop_lng = drop_lng;
        this.vendor_info = vendor_info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_order_price() {
        return total_order_price;
    }

    public void setTotal_order_price(String total_order_price) {
        this.total_order_price = total_order_price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDrop_lat() {
        return drop_lat;
    }

    public void setDrop_lat(String drop_lat) {
        this.drop_lat = drop_lat;
    }

    public String getDrop_lng() {
        return drop_lng;
    }

    public void setDrop_lng(String drop_lng) {
        this.drop_lng = drop_lng;
    }

    public VendorInfo getVendor_info() {
        return vendor_info;
    }

    public void setVendor_info(VendorInfo vendor_info) {
        this.vendor_info = vendor_info;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                ", userId='" + userId + '\'' +
                ", id='" + id + '\'' +
                ", total_order_price='" + total_order_price + '\'' +
                ", message='" + message + '\'' +
                ", Address='" + Address + '\'' +
                ", status='" + status + '\'' +
                ", coupon='" + coupon + '\'' +
                ", business_id='" + business_id + '\'' +
                ", rider_id='" + rider_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", drop_lat='" + drop_lat + '\'' +
                ", drop_lng='" + drop_lng + '\'' +
                ", vendor_info=" + vendor_info +
                '}';
    }
}
