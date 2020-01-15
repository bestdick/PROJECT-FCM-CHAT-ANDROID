package com.parkbros.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class $Dialog_Custom_04 extends Dialog {
    Context context;
    Button posBtn;
    Button negBtn;
    TextView titleTextView , contentTextView;
    EditText inputMessageEditText;

    public $Dialog_Custom_04(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout._dialog_custom_04);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public Button getPosBtn() {
        return posBtn;
    }

    public EditText getInputMessageEditText() {
        return inputMessageEditText;
    }

    public void callFunction(String title, String content, String pos, String neg){



        titleTextView = (TextView) findViewById(R.id.titleTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        inputMessageEditText = (EditText) findViewById(R.id.inputMessageEditText);
        posBtn = (Button) findViewById(R.id.positiveBtn);
        negBtn = (Button) findViewById(R.id.negativeBtn);

        titleTextView.setText(title);
        contentTextView.setText(content);
        posBtn.setText(pos);
        negBtn.setText(neg);
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


}
