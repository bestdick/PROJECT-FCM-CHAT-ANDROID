package com.parkbros.project_fcm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class _Adapter_DistanceList extends BaseAdapter {
    String deviceID;
    String deviceToken;
    Context context;
    ArrayList<_Bean_DistanceList> list;
    Long timeNow;

    public _Adapter_DistanceList(String deviceID, String deviceToken, Context context, ArrayList<_Bean_DistanceList> list, Long timeNow) {
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
        View v = View.inflate(context, R.layout._container_element_distancelist, null);

        TextView infoTextView = v.findViewById(R.id.infoTextView);
        TextView distanceTextView = v.findViewById(R.id.distanceTextView);
        TextView timeTextView = v.findViewById(R.id.loginTimeTextView);

        String gender = gender(list.get(position).getGender());
        String age = list.get(position).getAge();
        String info_str = " ( "+gender + " / "+ age + "세 ) ";
        String distance = list.get(position).getDistance();
        String time = HowLongAgo(timeNow, Long.parseLong(list.get(position).getLastLogin()));

        infoTextView.setText(info_str);
        distanceTextView.setText(distance(distance));
        timeTextView.setText(time);
        return v;
    }

    private String distance(String input){
        float dis = Float.parseFloat(input);

        if(dis < 1){
            return new DecimalFormat("#").format(dis *100f) + " m";
        }else {
            return new DecimalFormat("#").format(dis) + " km";
        }
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
