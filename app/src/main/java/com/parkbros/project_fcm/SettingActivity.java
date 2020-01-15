package com.parkbros.project_fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;
import static com.parkbros.project_fcm.__GlobalVariables.version;

public class SettingActivity extends AppCompatActivity {
    AdView mAdView;
    String deviceID;
    String deviceToken;

    TextView locationTextView, ageTextView, genderTextView, pointTextView, versionTextView;
    ImageView backspaceBtn;
    Button changeInfoBtn, aboutBtn, err_suggest_Btn;
    Switch NotificationSwitch;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        UIElement();
        getDeviceInfoIntent();
        GetInfoAboutMe();
        BackSpaceAction();
        ButtonClickAction();
        NotificationController();

        AdView();
    }

    private void NotificationController(){
        NotificationSwitch.setChecked(pref.getBoolean("NotificationEnable", true));
        if(pref.getBoolean("NotificationEnable", true)){
            NotificationSwitch.setText("On");
        }else{
            NotificationSwitch.setText("Off");
        }

        NotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String temp = (isChecked) ? "true" : "false";
                Log.e("notif ::", temp);
                SetNotifPreference(isChecked);
                if(isChecked){
                    NotificationSwitch.setText("On");
                }else{
                    NotificationSwitch.setText("Off");
                }

            }
        });
    }

    private void SetNotifPreference(boolean input){
        pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("NotificationEnable", input);
        editor.commit();
    }

    private void UIElement(){
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        ageTextView  = (TextView) findViewById(R.id.ageTextView);
        genderTextView = (TextView) findViewById(R.id.genderTextView);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        changeInfoBtn = (Button) findViewById(R.id.changeInfoBtn);
        aboutBtn = (Button) findViewById(R.id.aboutBtn);
        NotificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        versionTextView = (TextView) findViewById(R.id.versionTextView);
        versionTextView.setText("버젼 : "+version);

        err_suggest_Btn = (Button) findViewById(R.id.err_suggest_Btn);

    }

    private void ButtonClickAction(){
        changeInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, FirstSettingActivity.class);
                intent.putExtra("type", "change");
                intent.putExtra("deviceID", deviceID);
                intent.putExtra("deviceToken", deviceToken);
                startActivity(intent);
                finish();
            }
        });
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ExtraActivity.class);
                intent.putExtra("type", "about_us");
                startActivity(intent);
                finish();
            }
        });

        err_suggest_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ExtraActivity.class);
                intent.putExtra("type", "err_suggestion");
                intent.putExtra("deviceID", deviceID);
                intent.putExtra("deviceToken", deviceToken);
                startActivity(intent);
                finish();
            }
        });
    }
    private void BackSpaceAction(){
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void GetInfoAboutMe(){
        String url = URLBase + "/setting";
        final String request_type = "profile";
        String request_data = MyInfoToJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this ,url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("my info ::", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(request_type.equals(response_type)){
                        JSONArray jsonArray = jsonObject.getJSONArray("response_data");
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            String location = jsonArray.getJSONObject(i).getString("m_location");
                            String age  = jsonArray.getJSONObject(i).getString("m_age");
                            String gender  = jsonArray.getJSONObject(i).getString("m_gender");
                            String point = jsonArray.getJSONObject(i).getString("m_point");



                            pointTextView.setText("내 포인트 : "+ point);
                            locationTextView.setText("지역 : "+location);
                            ageTextView.setText("나이 : "+ age);
                            genderTextView.setText("성별 : "+gender(gender));



                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, request_type, request_data);
    }



    private String MyInfoToJSON(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
    }

    private String gender(String input){
        String output = "여성";
        if(input.equals("m")){
            output = "남성";
        }
        return output;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void AdView(){

        MobileAds.initialize(SettingActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}

