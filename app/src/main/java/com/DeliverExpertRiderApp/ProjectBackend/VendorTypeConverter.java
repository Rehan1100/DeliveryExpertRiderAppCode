/*
package com.expertorider.ProjectBackend;

import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.expertorider.Communications.response.Model.VendorInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
public class VendorTypeConverter {

    @TypeConverter
    public static List<VendorInfo> fromString(String value) {
        Type listType = new TypeToken<List<VendorInfo>>() {}.getType();
        List<VendorInfo> notifications = new Gson().fromJson(value,listType);
        return notifications;
    }

    @TypeConverter
    public static String listToString(List<VendorInfo> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}*/
