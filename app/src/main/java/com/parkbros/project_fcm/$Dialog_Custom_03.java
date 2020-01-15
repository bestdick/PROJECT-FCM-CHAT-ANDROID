package com.parkbros.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class $Dialog_Custom_03 extends Dialog {
    Context context;
    int length;
    EditText statusEditText;
    Button posBtn;
    TextView strLengthTextView;
    _$function_String_Controller string_controller;

    public int getLength() {
        return length;
    }

    public EditText getStatusEditText() {
        return statusEditText;
    }

    public Button getPosBtn() {
        return posBtn;
    }

    public $Dialog_Custom_03(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout._dialog_custom_03);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void callFunction(String title){
        string_controller = new _$function_String_Controller();

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        statusEditText = (EditText) findViewById(R.id.statusEditText);
        strLengthTextView = (TextView) findViewById(R.id.strLengthTextView);
        posBtn = (Button) findViewById(R.id.positiveBtn);
        Button negBtn = (Button) findViewById(R.id.negativeBtn);

        titleTextView.setText(title);

        statusEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                length = string_controller.StrLength(content);
                String strLength = length + " / 30 ";
                if(length > 30){
                    strLengthTextView.setText(strLength);
                    strLengthTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                }else{
                    strLengthTextView.setText(strLength);
                    strLengthTextView.setTextColor(ContextCompat.getColor(context, R.color.colorLightLetter));
                }


            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
