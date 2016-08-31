package example.naimish.com.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import example.naimish.com.alarm.Activities.MainActivity;
import example.naimish.com.alarm.Recievers.AlarmCancelReceiver;


public class Utils {

    public static void generateNotification(Context context,String time,int pid,long id){

        Intent intent = new Intent(context, MainActivity.class);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.alarm);
        mBuilder.setContentTitle("Alarm Set");
        mBuilder.setContentText("Time : "+time);

        Intent i = new Intent(context, AlarmCancelReceiver.class);
        i.putExtra("Pid",pid);
        i.putExtra("Alarmid",id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,pid, i, PendingIntent.FLAG_UPDATE_CURRENT);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.addAction(R.drawable.close, "Switch Off", pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(pid, mBuilder.build());


    }
}