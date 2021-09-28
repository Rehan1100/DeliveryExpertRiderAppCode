package com.DeliverExpertRiderApp.Communications.response.Model;

import com.DeliverExpertRiderApp.Communications.response.CommonResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsResponse extends CommonResponse {

    @SerializedName("data")
    @Expose
    private SettingClass Settings;

    public void setSettings(SettingClass Settings) {
        this.Settings = Settings;
    }

    public SettingClass getSettings() {
        return Settings;
    }
}
