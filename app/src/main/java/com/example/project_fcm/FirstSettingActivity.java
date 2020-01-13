package com.example.project_fcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.project_fcm.__GlobalVariables.URLBase;

public class FirstSettingActivity extends AppCompatActivity {
    String deviceID;
    String deviceToken;
    String type;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;


    //UI elements
    EditText ageEditText;
    RadioGroup genderRadioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner locationSpinner;
    ConstraintLayout toolConLayout;
    Button submitButton;
    ImageView backspaceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);
        UIElement();
        getDeviceInfoIntent();
        setLocationSpinner();
        BackSpaceAction();

        if(type.equals("initial")){
            toolConLayout.setVisibility(View.GONE);
            InitialSubmitController();
        }else{
            GetUserInfo();
            ChangeSubmitController();
        }

    }
//    ininital 에서만 사용되는 function 들

//    initial 이 아닌 곳에서 사용되는 function 들
    private void GetUserInfo(){
        String url = URLBase + "/setting";
        final String request_type = "profile";
        String request_data = toJSON();
        _ServerCommunicator serverCommunicator = new _ServerCommunicator(this, url);
        serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback() {
            @Override
            public void onSuccess(String result, String connection) {
                Log.e("GET PROFILE : ", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String response_type = jsonObject.getString("response_type");
                    if(response_type.equals(request_type)){
                        JSONArray jsonArray = jsonObject.getJSONArray("response_data");
                        String location = jsonArray.getJSONObject(0).getString("m_location");
                        String age = jsonArray.getJSONObject(0).getString("m_age");
                        String gender = jsonArray.getJSONObject(0).getString("m_gender");
                        locationSpinner.getAdapter().getItem(search(location));
                        ageEditText.setText(age);
                        if(gender.equals("m")){
                            radioButton1.setChecked(true);
                        }else{
                            radioButton2.setChecked(true);
                        }

                        for(int i = 0 ; i < list.size(); i++){
                            if(list.get(i).equals(location)){
                                list.remove(i);
                            }
                        }
                        list.add(0, location);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, request_type, request_data);
    }
    private void ChangeSubmitController(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = URLBase+"/register/upload";
                String request_type ="additionals_register_update";
                String request_data = toJSON();
                _ServerCommunicator serverCommunicator = new _ServerCommunicator(FirstSettingActivity.this, url);
                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback(){
                    @Override
                    public void onSuccess(String result, String connection) {
                        Log.e("register ::", result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String jsonResult = jsonObject.getString("result");
                            if(jsonResult.equals("success")){
                                finish();
                            }else{
                                //
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },request_type, request_data);
            }
        });
    }
//    모든 작동에서 다 사용되는 function 들
    private int search(String string){
        for(int i = 0 ; i < list.size(); i ++){
            if(list.get(i).equals(string)){
                return i;
            }
        }
        return -1;
    }
    private void InitialSubmitController(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = URLBase+"/register/upload";
                String request_type ="additionals_register_upload";
                String request_data = toJSON();
                _ServerCommunicator serverCommunicator = new _ServerCommunicator(FirstSettingActivity.this, url);
                serverCommunicator.Communicator(new _ServerCommunicator.VolleyCallback(){
                    @Override
                    public void onSuccess(String result, String connection) {
                        Log.e("register ::", result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String jsonResult = jsonObject.getString("result");
                            if(jsonResult.equals("success")){
                                Intent intent = new Intent(FirstSettingActivity.this, LoungeActivity.class);
                                intent.putExtra("deviceID", deviceID);
                                intent.putExtra("deviceToken", deviceToken);
                                startActivity(intent);
                                finish();
                            }else{
                                //
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },request_type, request_data);
            }
        });
    }
    private String toJSON(){
        String age_str = ageEditText.getText().toString();
        String gender_str = "m";
        if(genderRadioGroup.getCheckedRadioButtonId() != R.id.rg_btn1){
            gender_str = "f";
        }
        String loc_str = locationSpinner.getSelectedItem().toString();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("age", age_str);
            jsonObject.put("gender", gender_str);
            jsonObject.put("location", loc_str);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void setLocationSpinner(){
        list = new ArrayList<>();
        list.add("서울");
        list.add("경기도");
        list.add("강원도");
        list.add("충청남도");
        list.add("충청북도");
        list.add("전라북도");
        list.add("전라남도");
        list.add("경상북도");
        list.add("경상남도");
        list.add("제주도");
        adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list);
        locationSpinner.setAdapter(adapter);
    }
    private void UIElement(){
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.rg_btn1);
        radioButton2 = (RadioButton) findViewById(R.id.rg_btn1);
        locationSpinner = (Spinner) findViewById(R.id.locationSpin);
        toolConLayout = (ConstraintLayout) findViewById(R.id.toolConLayout);
        submitButton = (Button) findViewById(R.id.submitBtn);
        backspaceBtn = (ImageView) findViewById(R.id.backSpaceBtn);
    }
    private void getDeviceInfoIntent(){
        Intent getIntent = getIntent();
        type = getIntent.getStringExtra("type");
        deviceID = getIntent.getStringExtra("deviceID");
        deviceToken = getIntent.getStringExtra("deviceToken");
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
