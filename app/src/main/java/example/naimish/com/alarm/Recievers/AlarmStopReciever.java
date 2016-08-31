package example.naimish.com.alarm.Recievers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import example.naimish.com.alarm.MyApplication;
import example.naimish.com.alarm.Services.MyAlarmService;

/**
 * Created by naimish on 28/8/16.
 */
public class AlarmStopReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        context.stopService(new Intent(context, MyAlarmService.class));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);

        long id=intent.getExtras().getLong("id");
       String s=new MyApplication(context).getWritableDatabase().readRepeat(id);
        if(Integer.parseInt(s)==0) {
            mNotificationManager.cancel((int) id);
            // new MyApplication(context).getWritableDatabase().switchOffAlarm(id);
        }

    }
}

