package com.example.project_fcm;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class _Adapter_ReadMessage extends BaseAdapter {
    Context context;
    ArrayList<_Bean_ReadMessage> list;
    String deviceID;
    Long timeNow;

    public _Adapter_ReadMessage(Context context, ArrayList<_Bean_ReadMessage> list, String deviceID, Long timeNow) {
        this.context = context;
        this.list = list;
        this.deviceID = deviceID;
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
        View meView = View.inflate(context, R.layout._container_element_read_me, null);
        View youView = View.inflate(context, R.layout._container_element_read_you, null);
        String sender_id = list.get(position).getSender_id();
        String receiver_id = list.get(position).getReceiver_id();
        String sender_token = list.get(position).getSender_token();
        String receiver_token = list.get(position).getReceiver_token();
        String instant_sender = list.get(position).getInstant_sender();
        String instant_sender_token  = list.get(position).getInstant_sender_token();
        String message = list.get(position).getMessage();
        String time = list.get(position).getTime();


        String LongAgo =  HowLongAgo(timeNow, Long.parseLong(time));

        if(instant_sender.equals(deviceID)){
            TextView messageTextView = meView.findViewById(R.id.messageTextView);
            TextView meTimeTextView = meView.findViewById(R.id.timeTextView);
            messageTextView.setText(message);
            meTimeTextView.setText(LongAgo);
            return meView;
        }else{
            TextView infoTextView = youView.findViewById(R.id.infoTextView);
            TextView messageTextView = youView.findViewById(R.id.messageTextView);
            TextView youTimeTextView = youView.findViewById(R.id.timeTextView);

            if(instant_sender.equals(sender_id)){
                String info_str = "From. " + list.get(position).getSender_age()+"세 ";
                info_str += gender(list.get(position).getSender_gender())+" ";
                info_str += list.get(position).getSender_location();

                infoTextView.setText(info_str);
//                youTimeTextView.setText(time(time));
                youTimeTextView.setText(LongAgo);
                messageTextView.setText(message);
                return youView;
            }else{
                String info_str = "From. " + list.get(position).getReceiver_age()+"세 ";
                info_str += gender(list.get(position).getReceiver_gender())+" ";
                info_str += list.get(position).getReceiver_location();

                infoTextView.setText(info_str);
//                youTimeTextView.setText(time(time));
                youTimeTextView.setText(LongAgo);
                messageTextView.setText(message);
                return youView;
            }
        }
    }


    private String gender(String input){
        String output = "여성";
        if(input.equals("m")){
            output = "남성";
        }
        return output;
    }
    private String time(String time){
        Date int_time = new Date(Long.parseLong(time));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");
        String dateString = formatter.format(int_time);
        return dateString;
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
