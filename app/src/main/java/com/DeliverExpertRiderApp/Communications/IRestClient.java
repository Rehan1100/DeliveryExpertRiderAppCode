package com.DeliverExpertRiderApp.Communications;



import com.DeliverExpertRiderApp.Communications.response.CommonResponse;
import com.DeliverExpertRiderApp.Communications.response.OrderDetailsResponse;
import com.DeliverExpertRiderApp.Communications.response.OrderListResponse;
import com.DeliverExpertRiderApp.Communications.response.CountOrderResponse;
import com.DeliverExpertRiderApp.Communications.response.Model.SettingsResponse;
import com.DeliverExpertRiderApp.Communications.response.UserDataResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IRestClient {

    @FormUrlEncoded
    @POST(ApiConstants.SIGN_IN_API)
    Call<UserDataResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ApiConstants.SIGN_UP_API)
    Call<UserDataResponse> signUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("number") String number,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST(ApiConstants.get_settings)
    Call<SettingsResponse> get_settings(
            @Field("val") String val

    );

    @FormUrlEncoded
    @POST(ApiConstants.Orders_List)
    Call<OrderListResponse> order_list(
            @Field("rider_id") String rider_id,
            @Field("status") String status
    );



    @FormUrlEncoded
    @POST(ApiConstants.COUNT_Orders)
    Call<CountOrderResponse> CountOrders(
            @Field("rider_id") String rider_id
    );


    @FormUrlEncoded
    @POST(ApiConstants.Orders_Details)
    Call<OrderDetailsResponse> order_details(
            @Field("order_id") String order_id
    );

    @FormUrlEncoded
    @POST(ApiConstants.Orders_Status_Update)
    Call<CommonResponse> order_status_change(
            @Field("order_id") String order_id,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST(ApiConstants.Online_Status)
    Call<CommonResponse> online_status(
            @Field("rider_id") String rider_id,
            @Field("online_status") String online_status
    );



    @FormUrlEncoded
    @POST(ApiConstants.UpdateProfile)
    Call<CommonResponse> update_profile(
            @Field("rider_id") String rider_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("Address") String Address,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST(ApiConstants.UpdateProfile)
    Call<CommonResponse> update_profile(
            @Field("rider_id") String rider_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("Address") String Address
    );


    @FormUrlEncoded
    @POST(ApiConstants.UpdateProfile)
    Call<CommonResponse> update_password(
            @Field("rider_id") String rider_id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ApiConstants.ForgetPassword)
    Call<CommonResponse> forget_password(
            @Field("email") String email
    );


    @Multipart
    @POST(ApiConstants.UpdateProfile)
    Call<UserDataResponse> update_profile_image(
            @Part("rider_id") RequestBody rider_id,
            @Part MultipartBody.Part files

    );


    @FormUrlEncoded
    @POST(ApiConstants.Orders_List)
    Call<OrderListResponse> report_list(
            @Field("rider_id") String rider_id,
            @Field("status") String status,
            @Field("to_date") String to_date,
            @Field("from_date") String from_date
    );
    @FormUrlEncoded
    @POST(ApiConstants.Orders_List)
    Call<OrderListResponse> report_list(
            @Field("rider_id") String rider_id,
            @Field("status") String status,
            @Field("to_date") String to_date
    );
    @FormUrlEncoded
    @POST(ApiConstants.Orders_List)
    Call<OrderListResponse> report_list(
            @Field("rider_id") String rider_id,
            @Field("status") String status
    );



//    @Multipart
//    @POST(ApiConstants.POST_ADS_API)
//    Call<CommonResponse> postAd(
//            @Part("name") String name,
//            @Part("phone") String phone,
//            @Part("reg") String reg,
//            @Query("user_id") String userId,
//            @Part("ad_title") String title,
//            @Part("ad_desc") String description,
//            @Part("province") String province,
//            @Part("city") String city,
//            @Part("area") String area,
//            @Part("model") String model,
//            @Part("brand") String brand,
//            @Part("condition") String condition,
//            @Part("price") String price,
//            @Part("cat_id") String catId,
//            @Part("sub_cat_id") String subCatId,
//            @Part("color") String color,
//            @Part("featured_ad") String featuredAd,
//            @Part List<MultipartBody.Part> images);
}
