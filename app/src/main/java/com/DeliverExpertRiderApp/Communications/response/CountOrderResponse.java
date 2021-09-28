package com.DeliverExpertRiderApp.Communications.response;

import com.DeliverExpertRiderApp.Communications.response.Model.CountOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountOrderResponse extends CommonResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private CountOrder countOrder;

    public CountOrder getCountOrder() {
        return countOrder;
    }
}
