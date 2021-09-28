package com.DeliverExpertRiderApp.Communications.response;

import com.DeliverExpertRiderApp.Communications.response.Model.UserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDataResponse extends CommonResponse implements Serializable {


    @SerializedName("data")
    @Expose
    public UserData Userdata;

    public void setUserdata(UserData userdata) {
        Userdata = userdata;
    }

    public UserData getUserdata() {
        return Userdata;
    }


}
