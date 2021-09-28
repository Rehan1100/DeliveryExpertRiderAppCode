package com.DeliverExpertRiderApp.Communications;


import com.expertorider.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IRestClient retrofitCalls,restClient;

    public static IRestClient getClient() {
        if (retrofitCalls == null) {
            OkHttpClient okHttpClient;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
                    HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();
            // okHttpClient.interceptors().add(loggingInterceptor);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit.Builder builder = new Retrofit.Builder();
            Retrofit client = builder.baseUrl(ApiConstants.BASE_SERVER_URL)
                    .client(okHttpClient)
                    //  .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            retrofitCalls = client.create(IRestClient.class);
        }
        return retrofitCalls;
    }



//    public static IRestClient getClient() {
//        if (retrofitCalls == null) {
//            OkHttpClient okHttpClient;
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY :
//                    HttpLoggingInterceptor.Level.NONE);
//            okHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .addInterceptor(loggingInterceptor)
//                    .build();
//           // okHttpClient.interceptors().add(loggingInterceptor);
//            Retrofit.Builder builder = new Retrofit.Builder();
//            Retrofit client = builder.baseUrl(ApiConstants.BASE_SERVER_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            retrofitCalls = client.create(IRestClient.class);
//        }
//        return retrofitCalls;
//    }

}
