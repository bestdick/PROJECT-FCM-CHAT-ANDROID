package com.parkbros.project_fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.parkbros.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_GET;
import static com.parkbros.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_GET_UPDATE;
import static com.parkbros.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_UPDATE;
import static com.parkbros.project_fcm.__GlobalVariables.INT_REQUEST_TYPE_UPDATE_GET;
import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class PointsActivity extends AppCompatActivity {


    int reward_point = 0 ;

    String deviceID;
    String deviceToken;
    private RewardedAd rewardedAd;
    Button rewardBtn;
    ImageView backspaceBtn;
    ProgressBar reward_progressBar;
    TextView pointTextView;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        getDeviceInfoIntent();
        UIElement();
        BackSpaceAction();


        AdView();
        InitializeReward();
        RewardADController();
        PointController(INT_REQUEST_TYPE_GET);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Activity State :", "pasue");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Activity State :", "resume");
    }

    private void UIElement(){
        rewardBtn = (Button) findViewById(R.id.rewardBtn);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        reward_progressBar = (ProgressBar) findViewById(R.id.reward_progressBar);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
    }

    private void PointController(final int input){
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
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
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
                            pointTextView.setText(m_point + " 점");
                        }
                    }else if(input == INT_REQUEST_TYPE_GET_UPDATE){

                    }else if(input == INT_REQUEST_TYPE_UPDATE){

                    }else{
                        // input == int_request_type_update_get
                        reward_point = 0;
                        JSONArray jsonArray = jsonObject.getJSONArray("response_data");
                        for(int i = 0 ; i < jsonArray.length(); i++){
                            String m_point = jsonArray.getJSONObject(i).getString("m_point");
                            pointTextView.setText(m_point + " 점");
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


    private void InitializeReward(){
        rewardedAd = new RewardedAd(this, getString(R.string.admob_APP_id));

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                reward_progressBar.setVisibility(View.GONE);
                rewardBtn.setClickable(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                reward_progressBar.setVisibility(View.GONE);
                rewardBtn.setClickable(false);

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }
    private void RewardADController(){
        rewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = PointsActivity.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                            reward_progressBar.setVisibility(View.VISIBLE);
                            InitializeReward();
                            PointController(INT_REQUEST_TYPE_UPDATE_GET);
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            Log.e("REWARD :: ", reward.toString());
                            reward_point = 9;

                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });
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
    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
    }
    public void AdView(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
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
