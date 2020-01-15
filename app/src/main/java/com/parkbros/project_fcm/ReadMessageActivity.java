package com.parkbros.project_fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class ReadMessageActivity extends AppCompatActivity {
    int PreventDoubleClick =1;
    String deviceID;
    String deviceToken;
    String messageToken;
    String status;

    ListView listView;
    _Adapter_ReadMessage adapter_readMessage;
    Button sendBtn;
    EditText sendEditText;
    ImageView backspaceBtn;
    androidx.appcompat.widget.Toolbar toolbar;
    ProgressBar progressBar;
    TextView countTextView;


    ArrayList<_Bean_ReadMessage> list;

    AdView mAdView;
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

            String type  = intent.getStringExtra("type");
            if(type.equals("continuous")){
                String token = intent.getStringExtra("token");
                if(token.equals(messageToken)){
                    Log.e("현재 접속해는곳", "으로 문자가 들어옴");
                    RecevingAdditionalMessageController();
                }else{
                    Log.e("현재 접속해는곳", " 으로 오지않음");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        getDeviceInfoIntent();
        UIElement();
        RecevingMessageController();
        BackSpaceAction();
//        AdView();
    }
    private void UIElement(){
        listView = (ListView) findViewById(R.id.listView);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendEditText = (EditText) findViewById(R.id.sendEditText);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TouchDisable();
//        progressBar.setVisibility(View.VISIBLE);
//        optionBtn = (ImageView) findViewById(R.id.optionImageView);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        countTextView = (TextView) findViewById(R.id.countTextView);
    }
    private void SendingMessageController(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(PreventDoubleClick == 1){
                    PreventDoubleClick = -1;
                    if(status.equals("dead")){
                        String message = "상대방이 삭제한 대화방 입니다";
                        new $Toast_Custom_01(ReadMessageActivity.this, ReadMessageActivity.this, message).show();
                        //Toast.makeText(ReadMessageActivity.this, "상대방이 삭제한 대화입니다", Toast.LENGTH_LONG).show();
                        PreventDoubleClick = 1;

                        TouchEnable();
                    }else{
                        if(deviceID.equals(list.get(list.size()-1).getInstant_sender())){
                            // 내가 보냈기 때문에 더이상 보낼수 없다.
                            TouchEnable();
                            String title_str ="전송 실패";
                            String message_str = "상대방이 아직 메시지를 보내지 않았습니다\n메시지가 올때까지 기다려주세요";
                            String pos_str = "확인";
                            SendErrorAlertDialog(title_str, message_str, pos_str);
                            PreventDoubleClick = 1;

                        }else{
                            // 보낼수 있는 상태
                            if(sendEditText.getText().length() == 0){
                                //메시지가 없다
                                String message = "메시지를 입력해 주세요";
                                new $Toast_Custom_01(ReadMessageActivity.this, ReadMessageActivity.this, message).show();
                             //   Toast.makeText(ReadMessageActivity.this, "메시지를 입력해 주세요", Toast.LENGTH_LONG).show();
                                PreventDoubleClick = 1;
                                TouchEnable();
                            }else{
                                // 메시지를 보낸다.
                                String url = URLBase+"/message/send";
                                String request_type = "send_particular";
                                String request_data = toJSON_send();
                                final String sendMessage_str = sendEditText.getText().toString();
                                _ServerCommunicator serverCommunicator = new _ServerCommunicator(ReadMessageActivity.this, url);
                                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result, String connection) {
                                        Log.e("message sent ::" , result);
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String response_type = jsonObject.getString("result_type");
                                            String response_data = jsonObject.getString("result_data");
                                            if(response_data.equals("message_send_success")){
                                                //메시지 전송을 성공한 상태이다.
                                                sendEditText.setText("");
                                                sendEditText.setHint("상대방의 메시지를 기다려주세요");
                                                Time time = new Time();
                                                time.setToNow();
                                                _Bean_ReadMessage get_bean_readMessage = list.get(list.size()-1);
                                                _Bean_ReadMessage set_bean_readMessage = new _Bean_ReadMessage(get_bean_readMessage.getSender_id(),
                                                        get_bean_readMessage.getReceiver_id(), get_bean_readMessage.getSender_token(),
                                                        get_bean_readMessage.getReceiver_token(), deviceID,
                                                        deviceToken, get_bean_readMessage.getSender_location(), get_bean_readMessage.getReceiver_location(),
                                                        get_bean_readMessage.getSender_age(), get_bean_readMessage.getReceiver_age(),
                                                        get_bean_readMessage.getSender_gender(),get_bean_readMessage.getReceiver_gender(),
                                                        "-1", sendMessage_str, String.valueOf(time.toMillis(false)));
                                                list.add((list.size()), set_bean_readMessage);
                                                adapter_readMessage.notifyDataSetChanged();
                                                PreventDoubleClick = 1;
                                                TouchEnable();
                                                countTextView.setText("( "+String.valueOf(list.size())+" )");
                                            }else{
                                                //메시지 전송 실패
                                                Toast.makeText(ReadMessageActivity.this, "메시지 전송하지 못 했습니다.", Toast.LENGTH_LONG).show();
                                                PreventDoubleClick = 1;
                                                TouchEnable();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            TouchEnable();
                                        }

                                    }
                                }, request_type, request_data);
                            }
                        }
                    }
                }else{
                    // PreventDoubleClick = -1 인상태이다
                    TouchEnable();
                }
            }
        });
    }
    private void SendErrorAlertDialog(String title, String message, String pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
;
                    }
                })
                .show();
    }
    private String toJSON_send(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("messageToken", messageToken);
            jsonObject.put("toSenderID", list.get(list.size()-1).getInstant_sender());
            jsonObject.put("toSenderToken", list.get(list.size()-1).getInstant_sender_token());
            jsonObject.put("message", sendEditText.getText().toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void RecevingAdditionalMessageController() {
        String url = URLBase + "/message/read";
        String request_type = "additional";
        String request_data = toJSON_read();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(ReadMessageActivity.this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("additional : ", result);

                try {
                    sendEditText.setHint("메시지를 입력해 주세요");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray token_jsonArray = jsonObject.getJSONArray("token_info");
                    Log.e("token info ::", token_jsonArray.toString());
                    _Bean_ReadMessage bean_readMessage;

                    JSONArray message_jsonArray = jsonObject.getJSONArray("items");
                    for(int j = 0 ; j < message_jsonArray.length(); j ++){
                        String i_sender_id = message_jsonArray.getJSONObject(j).getString("r_sender_id");
                        String i_receiver_id = message_jsonArray.getJSONObject(j).getString("r_receiver_id");
                        String message = message_jsonArray.getJSONObject(j).getString("r_message");
                        String instant_sender = message_jsonArray.getJSONObject(j).getString("r_instant_sender");
                        String time = message_jsonArray.getJSONObject(j).getString("r_date");
                        String isRead = message_jsonArray.getJSONObject(j).getString("r_receiver_read");
                        bean_readMessage = new _Bean_ReadMessage(i_sender_id, i_receiver_id, null,null,
                                instant_sender, null, null, null, null,null,
                                null, null, isRead, message, time);
                        list.add((list.size()), bean_readMessage);
                    }
                    for(int k = 0; k < list.size(); k++){
                        for(int i = 0 ; i < token_jsonArray.length(); i++){
                            String id = token_jsonArray.getJSONObject(i).getString("F_id");
                            String fcm_token = token_jsonArray.getJSONObject(i).getString("F_Token");
                            String location  = token_jsonArray.getJSONObject(i).getString("m_location");
                            String m_age  = token_jsonArray.getJSONObject(i).getString("m_age");
                            String m_gender  = token_jsonArray.getJSONObject(i).getString("m_gender");
                            if(id.equals(list.get(k).getSender_id())){
                                list.get(k).setSender_token(fcm_token);
                                list.get(k).setSender_location(location);
                                list.get(k).setSender_age(m_age);
                                list.get(k).setSender_gender(m_gender);
                            }else{
                                list.get(k).setReceiver_token(fcm_token);
                                list.get(k).setReceiver_location(location);
                                list.get(k).setReceiver_age(m_age);
                                list.get(k).setReceiver_gender(m_gender);
                            }
                            if(id.equals(list.get(k).getInstant_sender())){
                                list.get(k).setInstant_sender_token(fcm_token);
                            }
                        }
                    }

                    adapter_readMessage.notifyDataSetChanged();
                    countTextView.setText("( "+String.valueOf(list.size())+" )");
//                    adapter_readMessage = new _Adapter_ReadMessage(ReadMessageActivity.this, list, deviceID);
//                    listView.setAdapter(adapter_readMessage);
//                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//                    listView.setStackFromBottom(true);
//
//                    SendingMessageController();

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }
    private void RecevingMessageController(){
        String url = URLBase+"/message/read";
        String request_type = "read";
        String request_data = toJSON_read();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(ReadMessageActivity.this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                try {
                    list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray token_jsonArray = jsonObject.getJSONArray("token_info");
                    status = jsonObject.getString("status");
                    if(status.equals("dead")){
                        sendEditText.setHint("삭제한 대화방 입니다.");
                    }

                    _Bean_ReadMessage bean_readMessage;
                    JSONArray message_jsonArray = jsonObject.getJSONArray("items");

                    for(int j = 0 ; j < message_jsonArray.length(); j ++){
                        String i_sender_id = message_jsonArray.getJSONObject(j).getString("r_sender_id");
                        String i_receiver_id = message_jsonArray.getJSONObject(j).getString("r_receiver_id");
                        String message = message_jsonArray.getJSONObject(j).getString("r_message");
                        String instant_sender = message_jsonArray.getJSONObject(j).getString("r_instant_sender");
                        String time = message_jsonArray.getJSONObject(j).getString("r_date");
                        String isRead = message_jsonArray.getJSONObject(j).getString("r_receiver_read");
                         bean_readMessage = new _Bean_ReadMessage(i_sender_id, i_receiver_id, null,null,
                                 instant_sender, null, null, null, null,null,
                                 null, null, isRead, message, time);
                        list.add(bean_readMessage);
                    }
                    for(int k = 0; k < list.size(); k++){
                        for(int i = 0 ; i < token_jsonArray.length(); i++){
                            String id = token_jsonArray.getJSONObject(i).getString("F_id");
                            String fcm_token = token_jsonArray.getJSONObject(i).getString("F_Token");
                            String location  = token_jsonArray.getJSONObject(i).getString("m_location");
                            String m_age  = token_jsonArray.getJSONObject(i).getString("m_age");
                            String m_gender  = token_jsonArray.getJSONObject(i).getString("m_gender");
                                if(id.equals(list.get(k).getSender_id())){
                                    list.get(k).setSender_token(fcm_token);
                                    list.get(k).setSender_location(location);
                                    list.get(k).setSender_age(m_age);
                                    list.get(k).setSender_gender(m_gender);
                                }else{
                                    list.get(k).setReceiver_token(fcm_token);
                                    list.get(k).setReceiver_location(location);
                                    list.get(k).setReceiver_age(m_age);
                                    list.get(k).setReceiver_gender(m_gender);
                                }
                                if(id.equals(list.get(k).getInstant_sender())){
                                    list.get(k).setInstant_sender_token(fcm_token);
                                }
                        }
                    }
                    long _timeNow= System.currentTimeMillis();
                     adapter_readMessage = new _Adapter_ReadMessage(ReadMessageActivity.this, list, deviceID, _timeNow);
                    if(deviceID.equals(list.get(list.size()-1).getInstant_sender())){
                        sendEditText.setHint("상대방의 메시지를 기다려주세요.");
                    }
                    countTextView.setText("( "+String.valueOf(list.size())+" )");
                    listView.setAdapter(adapter_readMessage);
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                    listView.setStackFromBottom(true);
                    SendingMessageController();
                    TouchEnable();

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, request_type, request_data);
    }
    private String toJSON_read(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("messageToken", messageToken);
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
        messageToken = getIntent.getStringExtra("messageToken");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_readmessage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu1:
                //신고
                final $Dialog_Custom_01 report_dialog = new $Dialog_Custom_01(ReadMessageActivity.this);
                String title = "신고";
                String content = "욕설, 음란성, 광고(스팸), 비방, 개인정보 유출등 상대방에게 불쾌감을 조성할 수 있는 메시지는 신고의 대상이 됩니다."+
                        "해당 사용자를 신고하시겠습니까? 단, 허위 신고 및 악의적 신고는 서비스 제한이 있을수 있습니다.\n";
                String pos = "신고하기";
                String neg = "취소";
                report_dialog.callFunction(title, content,pos, neg);
                report_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                        AccuseRequest();
                    }
                });
                report_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                    }
                });
                report_dialog.show();
                break;
            case R.id.menu2:
                //삭제
                final $Dialog_Custom_01 delete_dialog = new $Dialog_Custom_01(ReadMessageActivity.this);
                String delete_title = "삭제";
                final String delete_content = "대화를 삭제하시겠습니까?";
                String delete_pos = "삭제";
                String delete_neg = "취소";
                delete_dialog.callFunction(delete_title, delete_content,delete_pos, delete_neg);
                delete_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete_dialog.dismiss();
                        DeleteRequest();
                    }
                });
                delete_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //취소소
                        delete_dialog.dismiss();
                        }
                });
                delete_dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteRequest(){
        String url = URLBase+"/message/delete";
        String request_type = "delete";
        String request_data = DeleteToJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(response_type.equals("delete")){
                        String response_data = jsonObject.getString("response_data");
                        if(response_data.equals("deleted")){
                            finish();
                        }else{
                            Toast.makeText(ReadMessageActivity.this, "Error : 삭제 실패", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(ReadMessageActivity.this, "Error : 삭제 실패", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }
    private String DeleteToJSON(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("messageToken", messageToken);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void AccuseRequest(){
        String url = URLBase+"/report";
        final String request_type = "whole_message"; // report type은  whole_message 와 single_message 로 나눈다.
        String request_data = AccuseToJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(ReadMessageActivity.this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("accus :: ", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(response_type.equals(request_type)){
                        String response_data = jsonObject.getString("response_data");
                        if(response_data.equals("success")){
                            String message = "해당 사용자를 신고 했습니다";
                            new $Toast_Custom_01(ReadMessageActivity.this, ReadMessageActivity.this, message).show();
//                            Toast.makeText(ReadMessageActivity.this, "신고 : 해당 사용자 신고 완료", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ReadMessageActivity.this, "Error : 신고 실패", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(ReadMessageActivity.this, "Error : 신고 실패", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }
    private String AccuseToJSON(){
        try {
            String accusingDeviceID = (deviceID.equals(list.get(0).getSender_id())) ? list.get(0).getReceiver_id() : list.get(0).getSender_id();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("accusingDeviceID", accusingDeviceID);
            jsonObject.put("messageToken", messageToken);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    public void BackSpaceAction(){
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
