package com.DeliverExpertRiderApp.Communications.response;

import com.DeliverExpertRiderApp.Communications.response.Model.OrderDetail;
import com.DeliverExpertRiderApp.Communications.response.Model.OrderItem;
import com.DeliverExpertRiderApp.Communications.response.Model.UserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetailsResponse extends CommonResponse implements Serializable {


    @SerializedName("data")
    @Expose
    private dataObj data;


    public void setData(dataObj data) {
        this.data = data;
    }

    public dataObj getData() {
        return data;
    }

    public class dataObj {

        @SerializedName("order_detail")
        @Expose
        private OrderDetail order_detail;

        @SerializedName("customer")
        @Expose
        private UserData customerData;

        @SerializedName("order_item")
        @Expose
        private ArrayList<OrderItem> order_items_list;

        public void setCustomerData(UserData customerData) {
            this.customerData = customerData;
        }

        public void setOrder_detail(OrderDetail order_detail) {
            this.order_detail = order_detail;
        }

        public ArrayList<OrderItem> getOrder_items_list() {
            return order_items_list;
        }

        public OrderDetail getOrder_detail() {
            return order_detail;
        }

        public UserData getCustomerData() {
            return customerData;
        }
    }


}
