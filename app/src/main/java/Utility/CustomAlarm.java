package Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;

/**
 * Created by kanishk on 4/8/16.
 */
public class CustomAlarm {
    int alarm_id;
    AlarmManager alarmManager;
    PendingIntent pi;

    public CustomAlarm(int alarm_id, AlarmManager alarmManager){
        this.alarm_id=alarm_id;
        this.alarmManager=alarmManager;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public PendingIntent getPi() {
        return pi;
    }

    public void setPi(PendingIntent pi) {
        this.pi = pi;
    }
}
