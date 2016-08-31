package example.naimish.com.alarm.Services;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import example.naimish.com.alarm.Activities.MainActivity;
import example.naimish.com.alarm.Activities.WakeActivity1;
import example.naimish.com.alarm.R;

import static example.naimish.com.alarm.R.raw;


public class MyAlarmService extends Service

{
    MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        mediaPlayer = MediaPlayer.create(this, raw.flute);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(1.0f, 1.0f);


        Intent i = new Intent(this, WakeActivity1.class);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.alarm);
        mBuilder.setContentTitle("Alarm going off!");
        mBuilder.setContentText("Click to go back ");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(WakeActivity1.class);
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        return START_NOT_STICKY;

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.reset();
mediaPlayer.release();
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
