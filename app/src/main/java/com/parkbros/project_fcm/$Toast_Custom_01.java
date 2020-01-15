package com.parkbros.project_fcm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class $Toast_Custom_01 extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public $Toast_Custom_01(Context context) {
        super(context);
    }

    public $Toast_Custom_01(Context context, Activity activity, String message) {
        super(context);

        /**
         * Create a LayoutInflater to inflate layout, here activity is useful
         */
        LayoutInflater inflater = activity.getLayoutInflater();

        /**
         * Create a view of layout which is used to show as toast
         */
        View view = inflater.inflate(R.layout._toast_custom_01, null);
        TextView ToastContentTextView = view.findViewById(R.id.ToastContentTextView);
        ToastContentTextView.setText(message);

        /**
         * Set gravity of Toast, you can set as per your need. Currently set to
         * "CENTER" of the screen
         */
        setGravity(Gravity.CENTER, 0, 0);

        /**
         * Pass how long toast will shown to the screen
         */
        setDuration(Toast.LENGTH_LONG);

        /**
         * This is main code to set custom view to Toast
         */
        setView(view);
    }
}
