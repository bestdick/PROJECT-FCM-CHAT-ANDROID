package com.parkbros.project_fcm;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class LatestFirebase extends  FirebaseMessagingService {
    //여기서 생성된 데이터를 다른 곳으로 보낼때 사용된다.
    private LocalBroadcastManager broadcaster;
    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("TOKEN",mToken);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

        Log.e("notif receive data:: " , remoteMessage.getData().toString());
        if(remoteMessage.getNotification() != null){

        }

        if(remoteMessage.getData().size() > 0 ){
            Map<String, String> params = remoteMessage.getData();
            String messageType = params.get("type");
            String messageToken = params.get("token");
            String message = params.get("message");

            Log.e("ReadListActivity Run?:", isActivityRunning(ReadListActivity.class).toString());
            Log.e("notif type :: ", messageType);
            Log.e("notif token :: ", messageToken);

            Intent intent = new Intent("notificationIntent");
            intent.putExtra("type", messageType);
            intent.putExtra("token", messageToken);
            intent.putExtra("message", message);
            broadcaster.sendBroadcast(intent);
        }

        SharedPreferences pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("NotificationEnable", true)){

            Intent _intent = new Intent(this, MainActivity.class);
            _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , _intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "one-channel";
            String channelName = "My Channel One1";
            String channelDescription = "My Channel One Description";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription(channelDescription);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.sc_icon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(333 /* ID of notification */, notificationBuilder.build());
        }
    }

    protected Boolean isActivityRunning(Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            Log.e("11name :", activityClass.getCanonicalName().toString());
            Log.e("22name :", task.topActivity.getClassName());
            Log.e("33name :", task.baseActivity.getClassName());
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.topActivity.getClassName()))
                return true;
        }

        return false;
    }
}
