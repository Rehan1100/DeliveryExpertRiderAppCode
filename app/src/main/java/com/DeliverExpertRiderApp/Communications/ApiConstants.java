package com.DeliverExpertRiderApp.Communications;


import com.DeliverExpertRiderApp.Communications.response.Model.SettingClass;

public class ApiConstants {

    public static final String BASE_SERVER_URL = "https://deliveryexpert.com.pk/";//Production Url
    public static final String BASE_IMAGE_URL = "https://deliveryexpert.com.pk/assets/cms_images/";//Production Url
    public static SettingClass settings = null;

//    public static final String LOCAL_URL = "http://192.168.100.137/sol/glx/";//Production Url


    public static final String SIGN_IN_API = "RiderApi/login";
    public static final String SIGN_UP_API = "RiderApi/signup";
    public static final String COUNT_Orders = "RiderApi/order_count";
    public static final String Orders_List = "RiderApi/order_list";
    public static final String Orders_Details = "RiderApi/order_details";
    public static final String Orders_Status_Update = "RiderApi/update_order_status";
    public static final String Online_Status = "RiderApi/online_status";
    public static final String UpdateProfile = "RiderApi/update_profile";
    public static final String ForgetPassword = "RiderApi/forget_password";
    public static final String get_settings = "VendorApi/get_settings";



}
