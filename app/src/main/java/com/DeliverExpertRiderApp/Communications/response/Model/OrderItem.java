package com.DeliverExpertRiderApp.Communications.response.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItem implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("cat_name")
    @Expose
    private String cat_name;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("qty")
    @Expose
    private String quantity;

    @SerializedName("create_at")
    @Expose
    private String date;

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }


    //    @SerializedName("image")
//    @Expose
//    private String image;


//    public OrderItem(String id,String cat_name,String price,String qty,String image){
//        this.quantity=qty;
//        this.image=image;
//        this.title=cat_name;
//        this.price=price;
//        this.id=id;
//    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public OrderItem(String id, String order_id, String title, String cat_name, String price, String quantity, String date, String image) {
        this.id = id;
        this.order_id = order_id;
        this.title = title;
        this.cat_name = cat_name;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
        this.image = image;
    }

    //    public String getImage() {
//        return image;
//    }
}
