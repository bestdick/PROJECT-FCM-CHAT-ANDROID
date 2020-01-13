package com.example.project_fcm;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_GET;
import static com.example.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_GET_UPDATE;
import static com.example.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_UPDATE;
import static com.example.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_UPDATE_GET;
import static com.example.project_fcm.__GlobalVariables.URLBase;

public class $PointController {
    Context context;
    String url;
    int integer_request_type;
    String request_type;
    String request_data;
    int reward_point;
    TextView textView;
    String deviceID;
    String deviceToken;

    public int getReward_point() {
        return reward_point;
    }

    public void setReward_point(int reward_point) {
        this.reward_point = reward_point;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_data() {
        return request_data;
    }

    public void setRequest_data(String request_data) {
        this.request_data = request_data;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getInteger_request_type() {
        return integer_request_type;
    }

    public void setInteger_request_type(int integer_request_type) {
        this.integer_request_type = integer_request_type;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public $PointController(Context context, String url, int integer_request_type, String request_type, String request_data, int reward_point, TextView textView, String deviceID, String deviceToken) {
        this.context = context;
        this.url = url;
        this.integer_request_type = integer_request_type;
        this.request_type = request_type;
        this.request_data = request_data;
        this.reward_point = reward_point;
        this.textView = textView;
        this.deviceID = deviceID;
        this.deviceToken = deviceToken;

        PointController(integer_request_type, textView);
    }

    public void PointController(final int input, final TextView textView){
        String url = URLBase + "/point/Reward";
        String request_type = ""; // update, get, update_and_get, get_and_update
        if(input == INT_REQUEST_TYPE_GET){
            request_type = "get";
        }else if(input == INT_REQUEST_TYPE_GET_UPDATE){
            request_type = "get_update";
        }else if(input == INT_REQUEST_TYPE_UPDATE){
            request_type = "update";
        }else{
            // input == int_request_type_update_get
            request_type = "update_get";
        }
        String request_date =toJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(getContext(), getUrl());
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("POINT RESULT ::", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(input == INT_REQUEST_TYPE_GET){
                        JSONArray jsonArray = jsonObject.getJSONArray("response_data");
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            String m_point = jsonArray.getJSONObject(i).getString("m_point");
                            textView.setText(m_point + " 점");
                        }
                    }else if(input == INT_REQUEST_TYPE_GET_UPDATE){

                    }else if(input == INT_REQUEST_TYPE_UPDATE){

                    }else{
                        // input == int_request_type_update_get
                        reward_point = 0;
                        JSONArray jsonArray = jsonObject.getJSONArray("response_data");
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            String m_point = jsonArray.getJSONObject(i).getString("m_point");
                            textView.setText(m_point + " 점");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_date);
    }

    private String toJSON(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("deviceToken", deviceToken);
            jsonObject.put("rewardPoint", reward_point);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
