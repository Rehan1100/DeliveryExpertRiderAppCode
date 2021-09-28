package com.DeliverExpertRiderApp.Communications.response;

import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class OrderListResponse extends CommonResponse{

    @SerializedName("data")
    @Expose
    private ArrayList<OrderDetail> order_List;

    public ArrayList<OrderDetail> getOrder_List() {
        return order_List;
    }
}
