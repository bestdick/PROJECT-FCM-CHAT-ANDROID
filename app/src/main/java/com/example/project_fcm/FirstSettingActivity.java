package com.example.project_fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import static com.example.project_fcm.MainActivity.URLBase;

public class FirstSettingActivity extends AppCompatActivity {
    String deviceID;
    String deviceToken;


    //UI elements
    EditText ageEditText;
    RadioGroup genderRadioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner locationSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);
        getDeviceInfoIntent();

        Button submitButton = (Button) findViewById(R.id.submitBtn);
        UIElement();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = URLBase+"/register/upload";
                String request_type ="additionals_register_upload";
                String request_data = "";
                _ServerCommunicator serverCommunicator = new _ServerCommunicator(FirstSettingActivity.this, url);
                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback(){
                    @Override
                    public void onSuccess(String result) {

                    }
                },request_type, request_data);
            }
        });
    }

    private void UIElement(){
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.rg_btn1);
        radioButton2 = (RadioButton) findViewById(R.id.rg_btn1);
        locationSpinner = (Spinner) findViewById(R.id.locationSpin);
    }
    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
    }
}
