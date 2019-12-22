package com.example.project_fcm;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class _ServerCommunicator {
    Context context;
    String url;


    public _ServerCommunicator(Context context, String url) {
        this.context = context;
        this.url = url;

    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public void Communicator(final VolleyCallback callback, final String request_type, final String request_data) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    callback.onSuccess(response);
            }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley error", error.getMessage().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params= new HashMap<>();
                params.put("request_type", request_type);
                params.put("request_data", request_data);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
