package com.example.project_fcm;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class $Dialog_Custom_01 {

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
        this.context = context;
    }

    public Dialog callFunction( String title,  String content,  String pos,  String neg){
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout._dialog_custom_01);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        titleTextView = (TextView) dlg.findViewById(R.id.titleTextView);
        contentTextView = (TextView) dlg.findViewById(R.id.contentTextView);
        posBtn = (Button) dlg.findViewById(R.id.positiveBtn);
        negBtn = (Button) dlg.findViewById(R.id.negativeBtn);

        titleTextView.setText(title);
        contentTextView.setText(content);
        posBtn.setText(pos);
        negBtn.setText(neg);

        return dlg;
    }
}
