package example.naimish.com.alarm;

import android.app.Application;
import android.content.Context;

import example.naimish.com.alarm.Database.DBAlarms;

/**
 * Created by kanishk on 2/8/16.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    private Context c;

    private static DBAlarms mDatabase;

    public static MyApplication getInstance() {
        return sInstance;
    }

    /*
    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }
    */
    public MyApplication(Context c){
        this.c=c;
    }

    public synchronized  DBAlarms getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBAlarms(c);
        }
        return mDatabase;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new DBAlarms(this);
    }

}
