package com.example.project_fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.project_fcm.__GlobalVariables.URLBase;

public class LoungeActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;


    String deviceID, deviceToken;
    ImageButton sendBtn, readBtn, pointBtn, settingBtn, distanceSearchBtn;
    ImageView newImageView;
    ConstraintLayout sendCon, readCon, pointCon, settingCon, distanceSearchCon;
    TextView pointTextView;
    AdView mAdView;
    ArrayList<_Bean_Notice> noticeList;


    ImageView noticeAddBtn, seeListBtn;

    AdapterViewFlipper adapterViewFlipper;
    _Adapter_Notice adapter_notice;


    ProgressBar MainProgressBar;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("notificationIntent")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // action controll  under here
            if (intent != null) {
                newImageView.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lounge);

        location();
        getDeviceInfoIntent();
        UIElement();
        ActionController();
//        AdView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "activity on resume");
        TouchDisable();
        NewMessageChecker();


    }

    @Override
    protected void onPause() {
        super.onPause();
//        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getDeviceInfoIntent() {
        Intent getIntent = getIntent();
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
    }

    private void UIElement() {
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        readBtn = (ImageButton) findViewById(R.id.readBtn);
        pointBtn = (ImageButton) findViewById(R.id.pointBtn);
        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        newImageView = (ImageView) findViewById(R.id.newImageView);
        distanceSearchBtn = (ImageButton) findViewById(R.id.distanceSearchBtn);

        sendCon = (ConstraintLayout) findViewById(R.id.sendConLayout);
        readCon = (ConstraintLayout) findViewById(R.id.readConLayout);
        pointCon = (ConstraintLayout) findViewById(R.id.pointConLayout);
        settingCon = (ConstraintLayout) findViewById(R.id.settingConLayout);
        distanceSearchCon = (ConstraintLayout) findViewById(R.id.distanceSearchConLayout);


        pointTextView = (TextView) findViewById(R.id.pointTextView);
        noticeAddBtn = (ImageView) findViewById(R.id.noticeAddBtn);
        seeListBtn = (ImageView) findViewById(R.id.seeListBtn);

        adapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper);

        MainProgressBar = (ProgressBar) findViewById(R.id.MainProgressBar);
    }

    private void NewMessageChecker() {
        String url = URLBase + "/lounge";
        final String request_type = "all";
        String request_data = toJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("Loung Result :: ", result);
                try {
                    final JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if (response_type.equals(request_type)) {
                        String count = jsonObject.getString("response_data_count");
                        int unread = Integer.parseInt(count);
                        if (unread > 0) {
                            newImageView.setVisibility(View.VISIBLE);
                        } else {
                            newImageView.setVisibility(View.INVISIBLE);
                        }
                        String point = jsonObject.getString("response_data_point");
                        pointTextView.setText(point + " 점");

                        noticeList = new ArrayList<>();
                        JSONArray noticeJSONArray = jsonObject.getJSONArray("response_data_notice");

                        for (int i = 0; i < noticeJSONArray.length(); i++) {
                            String type = noticeJSONArray.getJSONObject(i).getString("type");
                            if (type.equals("announcement")) {
                                String content = noticeJSONArray.getJSONObject(i).getString("content");
                                String senderID = null;
                                String senderToken = null;
                                String senderLocation = null;
                                String senderAge = null;
                                String senderGender = null;
                                String time = null;
                                _Bean_Notice element = new _Bean_Notice(type, content, senderID, senderToken, senderLocation, senderAge, senderGender, time);
                                noticeList.add(element);
                            } else {
                                String content = noticeJSONArray.getJSONObject(i).getString("content");
                                String senderID = noticeJSONArray.getJSONObject(i).getString("senderID");
                                String senderToken = noticeJSONArray.getJSONObject(i).getString("senderToken");
                                String senderLocation = noticeJSONArray.getJSONObject(i).getString("senderLocation");
                                String senderAge = noticeJSONArray.getJSONObject(i).getString("senderAge");
                                String senderGender = noticeJSONArray.getJSONObject(i).getString("senderGender");
                                String time = noticeJSONArray.getJSONObject(i).getString("time");
                                _Bean_Notice element = new _Bean_Notice(type, content, senderID, senderToken, senderLocation, senderAge, senderGender, time);
                                noticeList.add(element);
                            }
                        }
                        adapter_notice = new _Adapter_Notice(LoungeActivity.this, noticeList, deviceID, deviceToken);
                        adapterViewFlipper.setAdapter(adapter_notice);
                        adapterViewFlipper.setFlipInterval(7000);
                        adapterViewFlipper.setAutoStart(true);
                        adapterViewFlipper.setOutAnimation(getApplicationContext(), R.animator.left_out);
                        adapterViewFlipper.setInAnimation(getApplicationContext(), R.animator.right_in);
                        adapterViewFlipper.startFlipping();


                        seeListBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    JSONArray myListJSONArray =  jsonObject.getJSONArray("response_data_notice_my_list");
                                    $Dialog_Custom_02 dialog2 = new $Dialog_Custom_02(LoungeActivity.this);
                                    dialog2.callFunction("당신이 등록한 리스트","확인", myListJSONArray);
                                    dialog2.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });



                        TouchEnable();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }

    private String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("deviceToken", deviceToken);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void ActionController() {
        readCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, ReadListActivity.class, "readList");
            }
        });
        sendCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, SendMessageActivity.class, null);
            }
        });
        pointCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, PointsActivity.class, null);
            }
        });
        settingCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, SettingActivity.class, null);
            }
        });
        noticeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, SendMessageActivity.class, "GlobalMessage");
            }
        });

        distanceSearchCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto(deviceID, deviceToken, ReadListActivity.class, "distanceList");


//                String title = "준비중";
//                String content = "업데이트 준비중 입니다";
//                String pos = "확인";
//                String neg = "취소";
//
//                $Dialog_Custom_01 dialog = new $Dialog_Custom_01(LoungeActivity.this);
//                Dialog dlg = dialog.callFunction(title, content, pos, neg);
//                final Dialog _dlg = dlg;
//                dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        _dlg.dismiss();
//                    }
//                });
//                dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        _dlg.dismiss();
//                    }
//                });
            }
        });
    }

    private void Goto(String deviceID, String deviceToken, Class toClass, String SendMessageType) {
        if (SendMessageType == null) {
            Intent intent = new Intent(LoungeActivity.this, toClass);
            intent.putExtra("deviceID", deviceID);
            intent.putExtra("deviceToken", deviceToken);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoungeActivity.this, toClass);
            intent.putExtra("sendMessageType", SendMessageType);
            intent.putExtra("deviceID", deviceID);
            intent.putExtra("deviceToken", deviceToken);
            startActivity(intent);
        }

    }
//    public void AdView(){
//
//        MobileAds.initialize(LoungeActivity.this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });
//    }

    private void TouchDisable() {
        MainProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void TouchEnable() {
        MainProgressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private String GPSToJSON(double lat, double lng){
        try {
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("deviceToken", deviceToken);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // Toast.makeText(LoungeActivity.this, "" + location.getLatitude() + " :: " + location.getLongitude() + "", Toast.LENGTH_LONG).show();
                String url = URLBase + "/gps";
                String request_type = "update";
                String request_data = GPSToJSON(location.getLatitude(), location.getLongitude());
                _ServerCommunicator serverCommunicator = new _ServerCommunicator(LoungeActivity.this, url);
                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                    @Override
                    public void onSuccess(String result, String connection) {
                        Log.e("GPS::", result);
                    }
                }, request_type, request_data);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                return;
            } else {
                configuration();
//                locationManager.requestSingleUpdate("gps", locationListener, null);
                //locationManager.requestLocationUpdates("gps", 0, 100 , locationListener);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configuration();
                    Toast.makeText(LoungeActivity.this, "permission result", Toast.LENGTH_LONG).show();
//                    locationManager.requestLocationUpdates("gps", 0, 100 , locationListener);
                    return;
                }
        }
    }

    private void configuration() {
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener,null);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener,null);
        //locationManager.requestSingleUpdate("gps", locationListener, null);
    }
}
