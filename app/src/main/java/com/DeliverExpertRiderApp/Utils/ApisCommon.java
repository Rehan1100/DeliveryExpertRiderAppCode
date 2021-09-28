package com.DeliverExpertRiderApp.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.DeliverExpertRiderApp.Communications.RestClient;
import com.DeliverExpertRiderApp.Communications.response.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApisCommon {

    public static void Call_Online_Status_Api(Context context, String riderId, String status, View v){
        Call<CommonResponse> call = RestClient.getClient().online_status(riderId+"",status+"");
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        v.setVisibility(View.GONE);
                        if (status.equalsIgnoreCase("1")){
                            AppTypeDetails.getInstance(context).setIsOnline(true);
                        }else
                        if (status.equalsIgnoreCase("0")){
                            AppTypeDetails.getInstance(context).setIsOnline(false);
                        }
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        v.setVisibility(View.GONE);
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    v.setVisibility(View.GONE);
                    Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                v.setVisibility(View.GONE);
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
