package com.example.advisoryservice.data.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    // public static final String BASE_URL = "https://apiservices1.healthandglowonline.co.in/skinapi_test/api/";  //test Url
    //public static final String BASE_URL = "https://apiservices1.healthandglowonline.co.in/skin_api_store_uat/api/"; //test Url
    //  public static final String BASE_URL = "https://apiservices1.healthandglowonline.co.in/skintestapi_store/api/"; //live Url

    public static final String BASE_URL = "https://apiservices1.healthandglowonline.co.in/advisoryservice_uat/api/";
    public static final String CUSTOMER_BASE_URL = "http://api.directdialogs.com/api/";
    public static final String SKIN_ANALYSIS_BASE_URL = "https://partner-test.revieve.com/api/3/";
    public static final String LOGIN_BASE_URL = "http://199.healthandglowonline.in/selfcheck/api/checkout/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new ErrorInterceptor()).build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getRetrofitForCustomer() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(CUSTOMER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getRetrofitForSkinAnalysis() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        List<Protocol> list = new ArrayList<>();
        list.add(Protocol.HTTP_1_1);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .protocols(list)
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SKIN_ANALYSIS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getRetrofitForLogin() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        List<Protocol> list = new ArrayList<>();
        list.add(Protocol.HTTP_1_1);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .protocols(list)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(LOGIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    static APIInterface provideRetrofitService() {
        return retrofit.create(APIInterface.class);
    }

    static APIInterface provideCustomerRetrofitService() {
        return getRetrofitForCustomer().create(APIInterface.class);
    }

    static APIInterface provideSkinAnalysisRetrofitService() {
        return getRetrofitForSkinAnalysis().create(APIInterface.class);
    }

    static APIInterface provideLoginRetrofitService() {
        return getRetrofitForLogin().create(APIInterface.class);
    }
}
