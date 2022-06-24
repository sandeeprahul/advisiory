package com.example.advisoryservice.data.rest;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiCall extends AppCompatActivity {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;


    public ApiCall(Context ctx) {
        mRequestQueue = getRequestQueue(ctx);
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context ctx) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.add(req);
    }

    public static void make(Context ctx, String URLpath, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {

        RequestQueue requestQueue = AppController.getInstance().getRequestQueue();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLpath,
                listener, errorListener);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //  ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        requestQueue.add(stringRequest);
    }
}
