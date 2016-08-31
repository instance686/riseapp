package example.naimish.com.alarm.Recievers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import example.naimish.com.alarm.MyApplication;

/**
 * Created by naimish on 16/8/16.
 */
public class AlarmCancelReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        int pid=intent.getIntExtra("Pid",0);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,pid , myIntent, 0);
        alarmManager.cancel(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(pid);

     //   long alarmid=intent.getLongExtra("Alarmid",0);
      // new MyApplication(context).getWritableDatabase().switchOffAlarm(alarmid);
    }
}
