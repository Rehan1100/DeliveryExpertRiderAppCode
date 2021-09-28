package com.DeliverExpertRiderApp.Communications.response.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountOrder implements Serializable {

    @SerializedName("assigned_order")
    @Expose
    private String assigned_order;

    @SerializedName("picked")
    @Expose
    private String picked_order;

    @SerializedName("droped")
    @Expose
    private String droped_order;

    @SerializedName("reached")
    @Expose
    private String reached_order;

    @SerializedName("remaining")
    @Expose
    private String remaining_order;

    @SerializedName("cash_collected")
    @Expose
    private String cash_collected;


    public String getReached_order() {
        return reached_order;
    }

    public String getAssigned_order() {
        return assigned_order;
    }

    public String getDroped_order() {
        return droped_order;
    }

    public String getCash_collected() {
        return cash_collected;
    }

    public String getPicked_order() {
        return picked_order;
    }

    public String getRemaining_order() {
        return remaining_order;
    }

}
