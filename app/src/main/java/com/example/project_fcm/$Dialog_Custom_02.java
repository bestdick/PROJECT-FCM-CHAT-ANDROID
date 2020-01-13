package com.example.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class $Dialog_Custom_02  extends Dialog{

    Context context;
    Button posBtn;
    TextView titleTextView ;
    LinearLayout linearLayout;


    public Button getPosBtn() {
        return posBtn;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public $Dialog_Custom_02(Context context) {
        super (context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout._dialog_custom_02);
    }
    public void callFunction( String title, String pos, JSONArray jsonArray) throws JSONException{
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        posBtn = (Button) findViewById(R.id.positiveBtn);


        for(int i = 0 ; i < jsonArray.length(); i ++) {
            Log.e("here ? ", "activate?");
            View element = getLayoutInflater().inflate(R.layout._container_element_4_dialog_custom_02, null);

            TextView _c_textView =  element.findViewById(R.id.contentTextView);
            TextView _t_textView =  element.findViewById(R.id.remainTextView);

            String _content = jsonArray.getJSONObject(i).getString("content");
            String _time = jsonArray.getJSONObject(i).getString("time");

            _c_textView.setText(_content);
            _t_textView.setText(HowLongAgo(Long.parseLong(_time)));

            linearLayout.addView(element);
        }

        titleTextView.setText(title);
        posBtn.setText(pos);
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private String HowLongAgo(Long time){
        long _timeNow = System.currentTimeMillis();

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        Long diff = _timeNow - time;


        if (diff < MINUTE_MILLIS) {
            return "방금전";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 분 전";
        } else if (diff < 59 * MINUTE_MILLIS) {
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
