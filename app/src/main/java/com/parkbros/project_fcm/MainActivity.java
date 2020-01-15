package com.parkbros.project_fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;
import static com.parkbros.project_fcm.__GlobalVariables.version;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchServerIP();
        getNotificationIntent();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);




    }



    private void getNotificationIntent(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("token") != null) {
            //bundle must contain all info sent in "data" field of the notificationo
           Log.e("In bundle ::", bundle.getString("token"));
        }else{
            Log.e("nothing in ", "nothing");
        }
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
                        public void onSuccess(String result, String connection) {
                            Log.e("connection response", result);
                            try {
                                JSONObject resultJsonObject = new JSONObject(result);
                                String response_result = resultJsonObject.getString("result");
                                if(response_result.equals("success")){
                                    OffProgressBar();
                                    GotoRegister(deviceID, deviceToken);
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
                            public void onSuccess(String result, String connection) {
                                Log.e("connection response", result);
                                try {
                                    JSONObject resultJsonObject = new JSONObject(result);
                                    String response_result = resultJsonObject.getString("result");
                                    if(response_result.equals("success")){
                                        OffProgressBar();
                                        GotoLoung(deviceID, deviceToken);
                                    }else if(response_result.equals("not_registered")){
                                        OffProgressBar();
                                        GotoRegister(deviceID, deviceToken);
                                    }else{
                                        // fail
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
                            public void onSuccess(String result, String connection) {
                                Log.e("connection response", result);
                                try {
                                    JSONObject resultJsonObject = new JSONObject(result);
                                    String response_result = resultJsonObject.getString("result");
                                    if(response_result.equals("success")){
                                        OffProgressBar();
                                        GotoLoung(deviceID, deviceToken);
                                    }else if(response_result.equals("not_registered")){
                                        OffProgressBar();
                                        GotoRegister(deviceID, deviceToken);
                                    }else{
                                        // fail
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


    private void GotoLoung(String deviceID, String deviceToken){
        Intent intent = new Intent(MainActivity.this, LoungeActivity.class);
        intent.putExtra("deviceID", deviceID);
        intent.putExtra("deviceToken", deviceToken);
        startActivity(intent);
        finish();
    }

    private void GotoRegister(String deviceID, String deviceToken){
        Intent intent = new Intent(MainActivity.this, FirstSettingActivity.class);
        intent.putExtra("type", "initial");
        intent.putExtra("deviceID", deviceID);
        intent.putExtra("deviceToken", deviceToken);
        startActivity(intent);
        finish();
    }

    //Preference 읽기
    private Bundle getDeviceInfo() {
        SharedPreferences pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getString("deviceID", null) == null){
//            Toast.makeText(this, "device id not exist", Toast.LENGTH_LONG).show();
            return null;
        }else{
//            Toast.makeText(this, "device id already exist", Toast.LENGTH_LONG).show();
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


    private void OffProgressBar(){
        progressBar.setVisibility(View.GONE);
    }



    private void SearchServerIP(){
        String url = "http://www.joonandhoon.com/chat/search_server.php";
        String request_type = "search_server";
        String request_data = "chaegangjaji";
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                if(connection == null){
                    Log.e("searchServer ip ", "null");
                    try {
                        Log.e("SEARCH SERVER IP::", result);
                        JSONObject jsonObject = new JSONObject(result);
                        String type = jsonObject.getString("type");
                        if(type.equals("normal")){
                            URLBase = jsonObject.getString("serverIP");
                            version = jsonObject.getString("version");
                            ServerIP();
                        }else{
                            // server check
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("content");
                            version = jsonObject.getString("version");
                            Intent intent = new Intent(MainActivity.this, ExtraActivity.class);
                            intent.putExtra("type", "server_check");
                            intent.putExtra("title", title);
                            intent.putExtra("content", content);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("searchServer ip ", "ERROR !!!!");
                    // connection 에 문제가 있을떄
                    // real server
                    //URLBase = "http://122.46.245.107:50003";
                    //test server
                    URLBase = "http://122.46.245.107:50001";
                    DeviceInfoManager();
                    getNotificationIntent();
                }
            }
        }, request_type, request_data);
    }
    public void ServerIP(){
        String url = URLBase + "/init";
        String request_type = "";
        String request_data = "";
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
//                Log.e("ip :: ", connection.toString());
                if(connection == null){
                    DeviceInfoManager();
                    getNotificationIntent();
                }else{
//                    Toast.makeText(MainActivity.this, "connection to new server", Toast.LENGTH_LONG).show();
                    // REAL SERVER
//                    URLBase = "http://122.46.245.107:50003";
                    URLBase = "http://192.168.219.178:3000";

                    // TEST SERVER
//                    URLBase = "http://122.46.245.107:50001";
//                    URLBase = "http://192.168.219.104:3000";// 내부 아이피
                    DeviceInfoManager();
                    getNotificationIntent();
                }
            }
        }, request_type, request_data);
    }




}
