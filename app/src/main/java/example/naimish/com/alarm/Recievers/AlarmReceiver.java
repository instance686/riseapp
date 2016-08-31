package example.naimish.com.alarm.Recievers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import example.naimish.com.alarm.Adapters.AlarmsAdapter2;
import example.naimish.com.alarm.MyApplication;
import example.naimish.com.alarm.Services.MyAlarmService;
import example.naimish.com.alarm.Utils;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            long id=intent.getExtras().getLong("id");
            String s = new MyApplication(context).getWritableDatabase().readArray(id);
            Log.i("Days", s);

            int f = 0;

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            switch (day) {
                case Calendar.SUNDAY:
                    if (s.contains("Su/")) {
                        Log.i("App", "Sunday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.MONDAY:
                    if (s.contains("M/")) {
                        Log.i("App", "Monday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.TUESDAY:
                    if (s.contains("T/")) {
                        Log.i("App", "Tuesday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.WEDNESDAY:
                    if (s.contains("W/")) {
                        Log.i("App", "Wednesday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.THURSDAY:
                    if (s.contains("Th/")) {
                        Log.i("App", "Thursday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.FRIDAY:
                    if (s.contains("F/")) {
                        Log.i("App", "Friday Alarm");
                        f = 1;
                    }
                    break;

                case Calendar.SATURDAY:
                    if (s.contains("Sa/")) {
                        Log.i("App", "Saturday Alarm");
                        f = 1;
                    }
                    break;

            }

            if (f == 1) {
                Intent service_intent = new Intent(context, MyAlarmService.class);
                context.startService(service_intent);

                Intent i = new Intent();
                i.putExtra("id",id);
                i.setClassName("example.naimish.com.alarm", "example.naimish.com.alarm.Activities.WakeActivity1");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }


    }
}
