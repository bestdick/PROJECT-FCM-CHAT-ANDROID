package com.parkbros.project_fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
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

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class ReadListActivity extends AppCompatActivity {
    final String STRING_OFFILINE = "offline";
    final String STRING_ONLINE = "online";
    int INITIAL_START = 0;
    AdView mAdView;

    String deviceID, deviceToken, type;
    ListView listView;
    ArrayList<_Bean_ReadList_Unique> list;
    ArrayList<_Bean_DistanceList> disList;
    ImageView backspaceBtn;

    ProgressBar MainProgressBar;
    androidx.appcompat.widget.Toolbar toolbar;

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
            GetList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_list);
        UIElement();
        getDeviceInfoIntent();
        BackSpaceAction();
        OnlineOffLineChecker(STRING_ONLINE);
//        GetList();
//        ListController();
//        AdView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        TouchDisable();
            //INITIAL_START == 1;
            if(type.equals("readList")){
                GetList();
            }else{
                // distanceList
                GetDistanceList();
            }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("this activity ", "destroy");
        OnlineOffLineChecker(STRING_OFFILINE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //INITIAL_START == 1;
        if(type.equals("readList")){
        }else{
            // distanceList
            inflater.inflate(R.menu.menu_distancelist, menu);
         }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu1:
                // 메시지 설정
                final $Dialog_Custom_03 dialog = new $Dialog_Custom_03(this);
                dialog.callFunction("Status Setting");
                dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialog.getLength() > 30){
                            Toast.makeText(ReadListActivity.this, "상태 메시지가 너무 길어요", Toast.LENGTH_LONG).show();
                        }else if(dialog.getLength() ==0){
                            Toast.makeText(ReadListActivity.this, "상태 메시지가 너무 짧아요", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ReadListActivity.this, "업로드 성공", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public void OnlineOffLineChecker(String input){
        // online checker
        if(type.equals("readList")){

        }else{
            // distanceList
            String url = URLBase + "/online";
            String request_type = input;
            String request_data = toJSON();
            _ServerCommunicator serverCommunicator = new _ServerCommunicator(ReadListActivity.this, url);
            serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                @Override
                public void onSuccess(String result, String connection) {

                }
            }, request_type, request_data);
        }
    }

    //distance list usage
    private void GetDistanceList(){
        String url = URLBase +"/gps";
        String request_type = "get";
        String request_data = toJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("distance ::", result);
                long _timeNow = System.currentTimeMillis();
                disList = new ArrayList<_Bean_DistanceList>();
                _Adapter_DistanceList adapter =new _Adapter_DistanceList(deviceID, deviceToken, ReadListActivity.this, disList, _timeNow);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i = 0 ; i < jsonArray.length(); i++ ){
                        String id = jsonArray.getJSONObject(i).getString("m_f_id");
                        String token = jsonArray.getJSONObject(i).getString("F_Token");
                        String location = jsonArray.getJSONObject(i).getString("m_location");
                        String age = jsonArray.getJSONObject(i).getString("m_age");
                        String gender = jsonArray.getJSONObject(i).getString("m_gender");
                        String distance = jsonArray.getJSONObject(i).getString("distance");
                        String last = jsonArray.getJSONObject(i).getString("F_date");

                        _Bean_DistanceList element = new _Bean_DistanceList(id, token, location, age, gender, last, distance);
                        disList.add(element);
                    }
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TouchEnable();
            }
        }, request_type, request_data);
    }

    //read list useage
    public void GetList(){
        String url = URLBase+"/message/list";
        String request_type = "list";
        String request_data = toJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("data result : ", result);
                try {
                    list = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(result);

                    for(int i = jsonArray.length()-1 ; i >= 0; i--){
                        String token = jsonArray.getJSONObject(i).getString("token");
                        String messageStatus = jsonArray.getJSONObject(i).getString("messageStatus");

                        JSONArray items = jsonArray.getJSONObject(i).getJSONArray("items");
                        _Bean_ReadList element = null;
                        for(int j  = 0 ; j < items.length(); j++){
                            String sender = items.getJSONObject(j).getString("sender");
                            String s_location = items.getJSONObject(j).getJSONObject("sender_info").getString("m_location");
                            String s_age = items.getJSONObject(j).getJSONObject("sender_info").getString("m_age");
                            String s_gender = items.getJSONObject(j).getJSONObject("sender_info").getString("m_gender");
                            String receiver = items.getJSONObject(j).getString("receiver");
                            String r_location = items.getJSONObject(j).getJSONObject("receiver_info").getString("m_location");
                            String r_age = items.getJSONObject(j).getJSONObject("receiver_info").getString("m_age");
                            String r_gender = items.getJSONObject(j).getJSONObject("receiver_info").getString("m_gender");
                            String message = items.getJSONObject(j).getString("message");
                            String instant_sender = items.getJSONObject(j).getString("instant_sender");
                            String isRead = items.getJSONObject(j).getString("isRead");
                            String time = items.getJSONObject(j).getString("m_time");


                            element = new _Bean_ReadList(token, sender, s_location, s_age, s_gender, receiver, r_location, r_age, r_gender, message, instant_sender, isRead, time);
                        }
                        String initial_s_id = jsonArray.getJSONObject(i).getJSONObject("initial_s_info").getString("m_f_id");
                        String initial_s_location = jsonArray.getJSONObject(i).getJSONObject("initial_s_info").getString("m_location");
                        String initial_s_age = jsonArray.getJSONObject(i).getJSONObject("initial_s_info").getString("m_age");
                        String initial_s_gender = jsonArray.getJSONObject(i).getJSONObject("initial_s_info").getString("m_gender");

                        String initial_r_id = jsonArray.getJSONObject(i).getJSONObject("initial_r_info").getString("m_f_id");
                        String initial_r_location = jsonArray.getJSONObject(i).getJSONObject("initial_r_info").getString("m_location");
                        String initial_r_age = jsonArray.getJSONObject(i).getJSONObject("initial_r_info").getString("m_age");
                        String initial_r_gender = jsonArray.getJSONObject(i).getJSONObject("initial_r_info").getString("m_gender");

                        String last_message_content = jsonArray.getJSONObject(i).getJSONObject("last_message_info").getString("message");
                        String last_message_time = jsonArray.getJSONObject(i).getJSONObject("last_message_info").getString("m_time");


                        _Bean_ReadList_Unique unique_element = new _Bean_ReadList_Unique(token, messageStatus, initial_s_id, initial_s_location, initial_s_age, initial_s_gender,
                                initial_r_id, initial_r_location, initial_r_age, initial_r_gender, last_message_content, last_message_time, element);
                        list.add(unique_element);
                    }

                    long _timeNow = System.currentTimeMillis();
                    _Adapter_ReadList adapter = new _Adapter_ReadList(deviceID, deviceToken, ReadListActivity.this, list, _timeNow);
                    if(listView.getHeaderViewsCount() == 0){
                        View noticeHeaderView = getLayoutInflater().inflate(R.layout._container_header_dummy, null);
                        TextView noticeTextView = (TextView) noticeHeaderView.findViewById(R.id.noticeTextView);
                        noticeTextView.setTextColor(getResources().getColor(R.color.colorWhite));
//                        String str_notice = "*상대방에게서 답장이 온 후 다시 메시지를 보낼 수 있습니다.";
//                        str_notice+= "욕설 및 음란 발언은 신고의 대상이 되며 불이익을 당 할 수 있습니다.";
//                        str_notice+= "\n*3일(72시간)동안 대화가 없으면 3일(72시간) 후 자동으로 영구히 삭제 됩니다.";
//                        str_notice+= "\n*삭제된 대화는 1일(24시간) 후 자동으로 영구히 삭제됩니다.";
                        String str_notice = getResources().getString(R.string.string_notice_1);
                        noticeTextView.setText(str_notice);
                        noticeTextView.setGravity(Gravity.LEFT);
                        noticeTextView.setTextColor(ContextCompat.getColor(ReadListActivity.this, (R.color.colorString)));
                        listView.addHeaderView(noticeHeaderView);
                    }
                    listView.setAdapter(adapter);
                    TouchEnable();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }

    // global usage
    private void UIElement(){
        listView = (ListView) findViewById(R.id.listView);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        MainProgressBar = (ProgressBar) findViewById(R.id.MainProgressBar);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
        type = getIntent.getStringExtra("sendMessageType");
    }
    private String toJSON(){
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
    private void BackSpaceAction(){
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void TouchDisable(){
        MainProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void TouchEnable(){
        MainProgressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
