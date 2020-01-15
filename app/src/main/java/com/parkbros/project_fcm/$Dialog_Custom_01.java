package com.parkbros.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class $Dialog_Custom_01 extends Dialog{

    Context context;
    Button posBtn;
    Button negBtn;
    TextView titleTextView , contentTextView;
    Dialog dlg;

    public Button getPosBtn() {
        return posBtn;
    }

    public Button getNegBtn() {
        return negBtn;
    }

    public $Dialog_Custom_01(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout._dialog_custom_01);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.context = context;
    }

    public void callFunction( String title,  String content,  String pos,  String neg){



        titleTextView = (TextView) findViewById(R.id.titleTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        posBtn = (Button) findViewById(R.id.positiveBtn);
        negBtn = (Button) findViewById(R.id.negativeBtn);

        titleTextView.setText(title);
        contentTextView.setText(content);
        posBtn.setText(pos);
        negBtn.setText(neg);

    }
}
