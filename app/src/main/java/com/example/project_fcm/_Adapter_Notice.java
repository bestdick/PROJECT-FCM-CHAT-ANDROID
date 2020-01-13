package com.example.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.project_fcm.__GlobalVariables.URLBase;

public class _Adapter_Notice extends BaseAdapter {
    Context context;
    ArrayList<_Bean_Notice> list;
    String deviceID;
    String deviceToken;

    public _Adapter_Notice(Context context, ArrayList<_Bean_Notice> list, String deviceID, String deviceToken) {
        this.context = context;
        this.list = list;
        this.deviceID = deviceID;
        this.deviceToken = deviceToken;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout._container_element4flipperview, null);

        TextView contentTextView = (TextView) v.findViewById(R.id.contentTextView);
        TextView infoTextView = (TextView) v.findViewById(R.id.infoTextView);

        String type = list.get(position).getType();
        String content_str = list.get(position).getContent();
        String temp_str = "";
            if(type.equals("announcement")){
                temp_str += "[공지] "+content_str;
                infoTextView.setVisibility(View.GONE);
                final String finalTemp_str = temp_str;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  title  =  "공지";
                        String message = finalTemp_str;
                        String pos = "더 보기";
                        String neg = "취소";
                        $Dialog_Custom_01 dialogCustom01 = new $Dialog_Custom_01(context);
                        Dialog dialog = dialogCustom01.callFunction(title, message, pos, neg);
                        final Dialog _dialog = dialog;
                        dialogCustom01.getPosBtn().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _dialog.dismiss();
                                //더 보기
                            }
                        });
                        dialogCustom01.getNegBtn().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _dialog.dismiss();
                                //취소
                            }
                        });
                    }
                });
            }else{
                String gender = (list.get(position).getSenderGender().equals("f"))? "여성" : "남성";
                temp_str+= "[모두에게] "+content_str.trim();
                final String info_str = "("+gender+"/"+list.get(position).getSenderAge()+"세/"+list.get(position).getSenderLocation()+")";
                infoTextView.setText(info_str);
                final int p = position;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = "대화하기";
                        String message = list.get(p).getContent();
                        message+="\n"+info_str;
                        message+= "\n\n상대방과 대화를 하시겠습니까?";
                        message+="\n(대화 참여를 하려면 3포인트가 소모됩니다)";

                        String pos = "대화하기";
                        String neg = "취소";

                        final _Bean_Notice receiverInfo = list.get(p);
                        $Dialog_Custom_01 dialogCustom01 = new $Dialog_Custom_01(context);
                        Dialog dialog = dialogCustom01.callFunction(title, message, pos, neg);
                        final Dialog _dialog = dialog;
                        dialogCustom01.getPosBtn().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = URLBase + "/message/send";
                                final String request_type = "send_shouted_target";
                                String request_data =requestData(receiverInfo.getSenderID(), receiverInfo.getSenderToken(), receiverInfo.getContent(), receiverInfo.getTime());
                                _ServerCommunicator serverCommunicator = new _ServerCommunicator(context, url);
                                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result, String connection) {
                                        Log.e("response :::", result);
                                        try {
                                            JSONObject jsonObject =new JSONObject(result);
                                            String response_type = jsonObject.getString("response_type");
                                            String response_data = jsonObject.getString("response_data");

                                            if(response_type.equals(request_type)  && response_data.equals("success")){
                                                _dialog.dismiss();
                                                String response_token = jsonObject.getString("response_token");
                                                Intent intent = new Intent(context, ReadMessageActivity.class);
                                                intent.putExtra("deviceID", deviceID);
                                                intent.putExtra("deviceToken", deviceToken);
                                                intent.putExtra("messageToken", response_token);
                                                context.startActivity(intent);
                                            }else{

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, request_type, request_data);

                            }
                        });
                        dialogCustom01.getNegBtn().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _dialog.dismiss();
                            }
                        });
                    }
                });
            }
        contentTextView.setText(temp_str);

        return v;
    }

    private String requestData(String senderID, String senderToken, String content, String time){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID",deviceID);
            jsonObject.put("deviceToken", deviceToken);
            jsonObject.put("senderID", senderID);
            jsonObject.put("senderToken", senderToken);
            jsonObject.put("content", content);
            jsonObject.put("time", time);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
