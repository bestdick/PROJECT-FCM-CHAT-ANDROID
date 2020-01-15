package com.parkbros.project_fcm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class _Adapter_ReadList extends BaseAdapter {
    String deviceID;
    String deviceToken;
    Context context;
    ArrayList<_Bean_ReadList_Unique> list;
//    ArrayList<_Bean_ReadList> list;
    Long timeNow;

    public _Adapter_ReadList(String deviceID, String deviceToken, Context context, ArrayList<_Bean_ReadList_Unique> list, Long timeNow) {
        this.deviceID = deviceID;
        this.deviceToken = deviceToken;
        this.context = context;
        this.list = list;
        this.timeNow = timeNow;
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
        View v = View.inflate(context, R.layout._container_element_readlist, null);
        ConstraintLayout containerConLayout = v.findViewById(R.id.containerConLayout);
        ConstraintLayout conLayout =  v.findViewById(R.id.constraintLayout6);
        ImageView newImageView = v.findViewById(R.id.newImageView);
        TextView infoTextView = v.findViewById(R.id.infoTextView);
        TextView messageTextView = v.findViewById(R.id.messageTextView);
        TextView timeTextView = v.findViewById(R.id.timeTextView);
        Button accBtn = v.findViewById(R.id.accuseBtn);
        Button delBtn = v.findViewById(R.id.delBtn);

        if(Integer.parseInt(list.get(position).getBean_readList().getIsRead()) == 1 || list.get(position).getBean_readList().getInstant_sender().equals(deviceID)){
            newImageView.setVisibility(View.GONE);
        }

        String str_info = "";
        if(deviceID.equals(list.get(position).getS_id())){
            //내가 보낸 사람이다. 그러므로 받는 상대방의 정보 필요
            if(list.get(position).getMessageStatus().equals("dead")){
                containerConLayout.setBackgroundResource(R.drawable.__ripple_message_readlist_delete);
                str_info = "[ 삭제 ]: To. "+list.get(position).getR_age() +"세 "+ gender(list.get(position).getR_gender()) +" "+ list.get(position).getR_location();
                infoTextView.setPaintFlags(infoTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                messageTextView.setPaintFlags(messageTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                timeTextView.setPaintFlags(timeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                str_info = "To. "+list.get(position).getR_age() +"세 "+ gender(list.get(position).getR_gender()) +" "+ list.get(position).getR_location();
            }

        }else{
            //내가 받는 사람이다 그러므로 보낸 상대방의 정보 필요
            if(list.get(position).getMessageStatus().equals("dead")){
                containerConLayout.setBackgroundResource(R.drawable.__ripple_message_readlist_delete);
                str_info = "[ 삭제 ]: To. "+list.get(position).getS_age() +"세 "+ gender(list.get(position).getS_gender()) +" "+ list.get(position).getS_location();
                infoTextView.setPaintFlags(infoTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                messageTextView.setPaintFlags(messageTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                timeTextView.setPaintFlags(timeTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                str_info = "To. "+list.get(position).getS_age() +"세 "+ gender(list.get(position).getS_gender()) +" "+ list.get(position).getS_location();
            }

        }


        //정보란
        infoTextView.setText(str_info);

        //메시지 처리란
        _$function_String_Controller functionStringController = new _$function_String_Controller();
        String temp = functionStringController.TrimRemoveLineSpace(list.get(position).getMessage());
        temp = functionStringController.CutString(temp, 120, 3); // sizeperletter utf- 8 일 경우 3 // euc-kr 일 경우 2 로한다.
        messageTextView.setText(temp);

        //시간 처리 란
        String LongAgo = HowLongAgo(timeNow, Long.parseLong(list.get(position).getTime()));
        timeTextView.setText(LongAgo);

       btnController(containerConLayout, conLayout, accBtn, delBtn, position);

        return v;
    }

    private void btnController(ConstraintLayout mainConLayout, ConstraintLayout conLayout, Button accBtn, Button delBtn, final int position){
        mainConLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item clicked", String.valueOf(position)+" clicked");
                Intent intent = new Intent(context, ReadMessageActivity.class);
                intent.putExtra("deviceID", deviceID);
                intent.putExtra("deviceToken", deviceToken);
                intent.putExtra("messageToken", list.get(position).getToken());
                context.startActivity(intent);
                ((ReadListActivity) context).finish();
            }
        });

        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final $Dialog_Custom_01 report_dialog = new $Dialog_Custom_01(context);
                String title = "신고";
                String content = "욕설, 음란성, 광고(스팸), 비방, 개인정보 유출등 상대방에게 불쾌감을 조성할 수 있는 메시지는 신고의 대상이 됩니다."+
                        "해당 사용자를 신고하시겠습니까? 단, 허위 신고 및 악의적 신고는 서비스 제한이 있을수 있습니다.";
                String pos = "신고하기";
                String neg = "취소";
                report_dialog.callFunction(title, content,pos, neg);
                report_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                        AccuseRequest(position);
                    }
                });
                report_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                    }
                });
                report_dialog.show();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final $Dialog_Custom_01 report_dialog = new $Dialog_Custom_01(context);
                String title = "삭제";
                String content = "삭제하시겠습니까?";
                String pos = "삭제";
                String neg = "취소";
                report_dialog.callFunction(title, content,pos, neg);
                report_dialog.getPosBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                        DeleteRequest(position);
                    }
                });
                report_dialog.getNegBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        report_dialog.dismiss();
                    }
                });
                report_dialog.show();
            }
        });
    }


    private void delete_dialog(String title, String message, String pos, String neg, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteRequest(position);
                    }
                })
                .setNegativeButton(neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void DeleteRequest(int position){
        String url = URLBase+"/message/delete";
        String request_type = "delete";
        String request_data = DeleteToJSON(position);
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(context, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(response_type.equals("delete")){
                        String response_data = jsonObject.getString("response_data");
                        if(response_data.equals("deleted")){
                            ((ReadListActivity) context).onResume();
                        }else{
                            Toast.makeText(context, "Error : 삭제 실패", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "Error : 삭제 실패", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }
    private String DeleteToJSON(int position){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("messageToken", list.get(position).getToken());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void AccuseRequest( int position){
        String url = URLBase+"/report";
        final String request_type = "whole_message"; // report type은  whole_message 와 single_message 로 나눈다.
        String request_data = AccuseToJSON( position);
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(context, url);
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
                            Toast.makeText(context, "신고 : 해당 사용자 신고 완료", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "Error : 신고 실패", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "Error : 신고 실패", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, request_type, request_data);
    }
    private String AccuseToJSON( int position){
        try {
            String accusingDeviceID = (deviceID.equals(list.get(position).getS_id())) ? list.get(position).getR_id() : list.get(position).getS_id();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("accusingDeviceID", accusingDeviceID);
            jsonObject.put("messageToken", list.get(position).getToken());
//            jsonObject.put("accsingMessage", accusing_message);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String gender(String input){
        String output = "여성";
        if(input.equals("m")){
            output = "남성";
        }
        return output;
    }
    private String HowLongAgo(Long _timeNow, Long time){
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        Long diff = _timeNow - time;


        if (diff < MINUTE_MILLIS) {
            return "방금전";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 분 전";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " 분 전";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 시간 전";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " 시간 전";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "어제";
        } else {
            return diff / DAY_MILLIS + " 일 전";
        }

    }
}
