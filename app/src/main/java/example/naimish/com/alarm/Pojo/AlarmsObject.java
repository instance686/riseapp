package example.naimish.com.alarm.Pojo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naimish on 27/7/16.
 */
public class AlarmsObject implements Parcelable {

    private String Time;
    private int checkedSwitch;
    private long alarm_id;
    AlarmManager alarmManager;
    PendingIntent pi;
    long t;
    int repeat;
    private boolean[]  days_to_repeat;


    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public boolean[] getDays_to_repeat() {
        return days_to_repeat;
    }

    public void setDays_to_repeat(boolean[] days_to_repeat) {
        this.days_to_repeat = days_to_repeat;
    }

    public void setDayArray(int pos,boolean b){
        boolean x[]=getDays_to_repeat();
        x[pos]=b;
        setDays_to_repeat(x);
    }

    public void setT(long t) {
        this.t = t;
    }

    public long getT() {
        return t;
    }

    public PendingIntent getPi() {
        return pi;
    }

    public void setPi(PendingIntent pi) {
        this.pi = pi;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public AlarmsObject(String time, int checkedSwitch, long alarm_id,AlarmManager alarmManager,PendingIntent pi,long t,
                        boolean[] days,int repeat) {
        this.Time = time;
        this.checkedSwitch=checkedSwitch;
        this.alarm_id=alarm_id;
        this.alarmManager=alarmManager;
        this.pi=pi;
        this.t=t;
        this.days_to_repeat=days;
        this.repeat=repeat;
    }

    public AlarmsObject(){

    }


    public long getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(long alarm_id) {
        this.alarm_id = alarm_id;
    }

    public AlarmsObject(Parcel in){
        Time=in.readString();
        checkedSwitch=in.readInt();
        alarm_id=in.readLong();
        repeat=in.readInt();
    }

    public int isCheckedSwitch() {
        return checkedSwitch;
    }

    public void setCheckedSwitch(int checkedSwitch) {
        this.checkedSwitch = checkedSwitch;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Time);
        parcel.writeInt(checkedSwitch);
        parcel.writeLong(alarm_id);
        parcel.writeInt(repeat);


    }

    public static final Parcelable.Creator<AlarmsObject> CREATOR
            = new Parcelable.Creator<AlarmsObject>() {
        public AlarmsObject createFromParcel(Parcel in) {
            return new AlarmsObject(in);
        }

        public AlarmsObject[] newArray(int size) {
            return new AlarmsObject[size];
        }
    };

}
