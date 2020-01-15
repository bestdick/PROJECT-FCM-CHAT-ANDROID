package com.parkbros.project_fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.parkbros.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_GET;
import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class SendMessageActivity extends AppCompatActivity {
    long PreventDoubleClick = 0;
    int ageType = -1;
    int locType = -1;
    int genderType = -1;

    String deviceID, deviceToken, sendMessageType;
    CheckBox ageBtn, locBtn, genderBtn;
    Button sendSubmitBtn;
    EditText messageEditText;
    ImageView backspaceBtn;
    TextView pointTextView, noticeTextView, toolbarTitleTextView;
    ConstraintLayout checkboxConLayout;
    ProgressBar progressBar;

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        getDeviceInfoIntent();
            UIElement();
            ActionController();
            BackSpaceAction();
//            AdView();
            PointController();


    }

    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        sendMessageType = getIntent.getStringExtra("sendMessageType");
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
    }

    private void UIElement(){
        ageBtn = (CheckBox) findViewById(R.id.sameAgeBtn);
        locBtn = (CheckBox) findViewById(R.id.sameLocBtn);
        genderBtn = (CheckBox) findViewById(R.id.sameGenderBtn);
        sendSubmitBtn = (Button) findViewById(R.id.sendSubmitBtn);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbarTitleTextView);

        checkboxConLayout = (ConstraintLayout) findViewById(R.id.checkboxConLayout);
        noticeTextView = (TextView) findViewById(R.id.noticeTextView);
        if(sendMessageType != null){
            checkboxConLayout.setVisibility(View.GONE);
            noticeTextView.setText("*전체 메시지를 보내면 7 포인트가 차감되며 1시간동안 노출이 유지 됩니다.");
            toolbarTitleTextView.setText("모두에게");

        }else{
            genderBtn.setChecked(true);
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
    private void ActionController(){
        sendSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TouchDisable();
                    if (messageEditText.getText().length() <= 1) {
                        //메시지가 짧아 메시지를 보내지 않았습니다.
                        String message = "보낼 내용이 너무 짧습니다";
                        new $Toast_Custom_01(SendMessageActivity.this, SendMessageActivity.this, message).show();
//                        Toast.makeText(SndMessageActivity.this, "message too short", Toast.LENGTH_LONG).show();
                        TouchEnable();
                    } else {
                        String url = URLBase;
                        String request_type = "";
                        if (sendMessageType != null) {
                            url += "/message/global";
                            request_type = "upload";
                        } else {
                            url += "/message/send";
                            request_type = "send_random";
                        }
                        final String request_type_compare = request_type;
                        String request_data = toJSON();
                        _ServerCommunicator serverCommunicator = new _ServerCommunicator(SendMessageActivity.this, url);
                        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                            @Override
                            public void onSuccess(String result, String connection) {
                                Log.e("message send result", result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (sendMessageType != null) {
                                        String response_type = jsonObject.getString("response_type");
                                        if (response_type.equals(request_type_compare)) {
                                            String response_data = jsonObject.getString("response_data");
                                            if (response_data.equals("success")) {
                                                String message = "메시지를 보냈습니다";
                                                new $Toast_Custom_01(SendMessageActivity.this, SendMessageActivity.this, message).show();

                                                //                                                Toast.makeText(SendMessageActivity.this, "메시지 전송 : 성공", Toast.LENGTH_SHORT).show();
                                                onBackPressed();

                                            } else if (response_data.equals("not_enough_point")) {
                                                String message = "포인트가 부족해 메시지를 보내지 못 했습니다";
                                                new $Toast_Custom_01(SendMessageActivity.this, SendMessageActivity.this, message).show();
//                                                Toast.makeText(SendMessageActivity.this, "Error : 포인트가 모자라 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();

                                            } else if(response_data.equals("success_random")){
                                                final $Dialog_Custom_01 custom_dialog = new $Dialog_Custom_01(SendMessageActivity.this);
                                                String title = "메시지";
                                                String content = "상대방을 찾을 수 없어 포인트 차감 없이 랜덤하게 보냈습니다.";
                                                String pos = "확인";
                                                String neg =  "취소";
                                                custom_dialog.callFunction(title, content, pos, neg);
                                                custom_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        custom_dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                                custom_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        custom_dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                                custom_dialog.show();
                                            }else{
                                                Toast.makeText(SendMessageActivity.this, "Error : 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(SendMessageActivity.this, "Error : 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        String response_type = jsonObject.getString("response_type");
                                        if (response_type.equals(request_type_compare)) {
                                            String response_data = jsonObject.getString("response_data");
                                            if (response_data.equals("success")) {
                                                String message = "메시지를 보냈습니다";
                                                new $Toast_Custom_01(SendMessageActivity.this, SendMessageActivity.this, message).show();
                                              //  Toast.makeText(SendMessageActivity.this, "메시지 전송 : 성공", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            } else if (response_data.equals("not_enough_point")) {
                                                String message = "포인트가 부족해 메시지를 보내지 못 했습니다";
                                                new $Toast_Custom_01(SendMessageActivity.this, SendMessageActivity.this, message).show();
//                                                Toast.makeText(SendMessageActivity.this, "Error : 포인트가 모자라 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();

                                            } else if(response_data.equals("success_random")){
                                                final $Dialog_Custom_01 custom_dialog = new $Dialog_Custom_01(SendMessageActivity.this);
                                                String title = "메시지";
                                                String content = "상대방을 찾을 수 없어 포인트 차감 없이 랜덤하게 보냈습니다.";
                                                String pos = "확인";
                                                String neg =  "취소";
                                                custom_dialog.callFunction(title, content, pos, neg);
                                                custom_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        custom_dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                                custom_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        custom_dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(SendMessageActivity.this, "Error : 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(SendMessageActivity.this, "Error : 메시지를 전송하지 못했습니다", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                TouchEnable();
                            }
                        }, request_type, request_data);
                    }

             }
        });
    }
    private int CheckBox(boolean bool){
        if(bool){
            return 1;
        }else{
            return -1;
        }
    }
    private String toJSON(){
        try {
            JSONObject jsonObject = new JSONObject();
            if(sendMessageType != null){
                jsonObject.put("type", "global_message");
                jsonObject.put("deviceID", deviceID);
                jsonObject.put("deviceToken", deviceToken);
                jsonObject.put("message", messageEditText.getText().toString());
            }else{
                jsonObject.put("deviceID", deviceID);
                jsonObject.put("age_type", CheckBox(ageBtn.isChecked()));
                jsonObject.put("location_type", CheckBox(locBtn.isChecked()));
                jsonObject.put("gender_type", CheckBox(genderBtn.isChecked()));
                jsonObject.put("message", messageEditText.getText().toString());
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void PointController(){
        String url = URLBase + "/point/Reward";
        String request_type = "";
        String request_data = "";
        int reward_point = 0;
        $PointController pointController = new $PointController(this, url, INT_REQUEST_TYPE_GET,
                null, null, reward_point, pointTextView, deviceID, deviceToken);
    }
    private void TouchDisable(){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void TouchEnable(){
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void BackSpaceAction(){
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//    public void AdView(){
//
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
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
}
