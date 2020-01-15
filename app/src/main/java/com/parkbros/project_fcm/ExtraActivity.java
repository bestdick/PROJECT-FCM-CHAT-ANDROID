package com.parkbros.project_fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.parkbros.project_fcm.__GlobalVariables.URLBase;

public class ExtraActivity extends AppCompatActivity {
    String INTENT_TYPE_SERVER_CHECK = "server_check";
    String INTENT_TYPE_ONE= "about_us";
    String INTENT_TYPE_ERR_SUGGESTION = "err_suggestion";

    ConstraintLayout toolbarConLayout;
    LinearLayout mainContentConLayout;
    ImageView backspaceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
        UIElement();
        BackSpaceAction();
        Intent intentIn = getIntent();
        String type = intentIn.getStringExtra("type");
        if(type.equals(INTENT_TYPE_ONE)){
            TypeOneAction();
        }else if (type.equals(INTENT_TYPE_SERVER_CHECK)){
            String title = intentIn.getStringExtra("title");
            String content = intentIn.getStringExtra("content");
            ServerCHeck(title, content);
        }
//        else if(type.equals(INTENT_TYPE_ERR_SUGGESTION)){
//
//        }

    }


    private void UIElement(){
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
        mainContentConLayout = (LinearLayout) findViewById(R.id.mainContentConLayout);
    }
    private void TypeOneAction(){
        String url = URLBase + "/setting";
        final String request_type = "about_us";
        String request_data = "";
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("about us :: ", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(response_type.equals(request_type)){
                        Log.e("about us :: ", "here? 1 ");
                        String response_data = jsonObject.getString("response_data");
                        View element = getLayoutInflater().inflate(R.layout._container_4_extraactivity_one, null);
                        TextView aboutUsTextView = element.findViewById(R.id.aboutUsTextView);
                        aboutUsTextView.setText(response_data);

                        mainContentConLayout.addView(element);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, request_type, request_data);
    }
    private void ServerCHeck(String title, String content){
        View element = getLayoutInflater().inflate(R.layout._container_4_extraactivity_servercheck, null);
        TextView titleTextView = (TextView) element.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) element.findViewById(R.id.contentTextView);

        titleTextView.setText(title);
        contentTextView.setText(content);

        mainContentConLayout.addView(element);
    }




    public void BackSpaceAction(){
        backspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
