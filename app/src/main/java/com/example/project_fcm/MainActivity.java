package com.example.project_fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final static String URLBase = "http://192.168.219.117:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceInfoManager();
    }

    private void Goto(String deviceID, String deviceToken){
        Intent intent = new Intent(MainActivity.this, FirstSettingActivity.class);
        intent.putExtra("deviceID", deviceID);
        intent.putExtra("deviceToken", deviceToken);
        startActivity(intent);
        finish();
    }


    private void DeviceInfoManager(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                final String deviceID = mToken.split(":")[0].trim();
                final String deviceToken = mToken.trim();
                if(getDeviceInfo()==null){
                    Log.e("Device info :: ",  "null");
                    setDeviceInfo(deviceID, deviceToken);

                    String url = URLBase+ "/fcm/upload";
                    String request_type = "fcm_token_upload";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("deviceID", deviceID);
                        jsonObject.put("deviceToken", deviceToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String request_data = jsonObject.toString();
                    _ServerCommunicator serverCommunicator = new _ServerCommunicator(MainActivity.this, url);
                    serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("connection response", result);
                            try {
                                JSONObject resultJsonObject = new JSONObject(result);
                                String response_result = resultJsonObject.getString("result");
                                if(response_result.equals("success")){
                                    Goto(deviceID, deviceToken);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, request_type, request_data);
                }else{

                    Bundle deviceInfo = getDeviceInfo();
                    String g_deviceID = deviceInfo.getString("deviceID").trim();
                    String g_deviceToken = deviceInfo.getString("deviceToken").trim();
                    Log.e("Device Token :: ", deviceToken);
                    Log.e("G Device Token :: ", g_deviceToken);
                    if(deviceID.equals(g_deviceID) && deviceToken.equals(g_deviceToken)){
                        Log.e("Device Info", "현재 디바이스 id 와 token의 변경값이 없다");
                        String url = URLBase+ "/fcm/upload";
                        String request_type = "fcm_update_date";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("deviceID", deviceID);
                            jsonObject.put("deviceToken", deviceToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String request_data = jsonObject.toString();
                        _ServerCommunicator serverCommunicator = new _ServerCommunicator(MainActivity.this, url);
                        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("connection response", result);
                                try {
                                    JSONObject resultJsonObject = new JSONObject(result);
                                    String response_result = resultJsonObject.getString("result");
                                    if(response_result.equals("success")){
                                        Goto(deviceID, deviceToken);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, request_type, request_data);
                    }else{
                        //token update
                        Log.e("Device Info", "현재 디바이스 id 와 token 의 변경값이 있다.");

                        setDeviceInfo(deviceID, deviceToken);
                        String url = URLBase+ "/fcm/upload";
                        String request_type = "fcm_token_update";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("deviceID", deviceID);
                            jsonObject.put("deviceToken", deviceToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String request_data = jsonObject.toString();
                        _ServerCommunicator serverCommunicator = new _ServerCommunicator(MainActivity.this, url);
                        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("connection response", result);
                                try {
                                    JSONObject resultJsonObject = new JSONObject(result);
                                    String response_result = resultJsonObject.getString("result");
                                    if(response_result.equals("success")){
                                        Goto(deviceID, deviceToken);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, request_type, request_data);
                    }
                }
            }
        });
    }

    //Preference 읽기
    private Bundle getDeviceInfo() {
        SharedPreferences pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getString("deviceID", null) == null){
            return null;
        }else{
            Bundle temp = new Bundle();
            temp.putString("deviceID", pref.getString("deviceID", null));
            temp.putString("deviceToken", pref.getString("deviceToken", null));
            return temp;
        }

    }

    // Preference 쓰기
    private void setDeviceInfo(String value1, String value2){
        SharedPreferences pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("deviceID", value1);
        editor.putString("deviceToken", value2);
        editor.commit();
    }

}
