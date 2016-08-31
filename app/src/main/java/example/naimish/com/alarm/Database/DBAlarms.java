package example.naimish.com.alarm.Database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import example.naimish.com.alarm.Pojo.AlarmsObject;

/**
 * Created by kanishk on 2/8/16.
 */
public class DBAlarms {

    AlarmHelper alarmHelper;
    private SQLiteDatabase mDatabase;
    Context c;

    public DBAlarms(Context context) {
        c = context;
        alarmHelper = new AlarmHelper(context);
        mDatabase = alarmHelper.getWritableDatabase();
    }

    public int updateVal(int position, int val) {
        SQLiteDatabase db = alarmHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlarmHelper.COLUMN_CHECKSTATUS, val);
        String[] args = {"" + (position + 1)};
        int count = db.update(AlarmHelper.TABLE_NAME, contentValues, AlarmHelper.COLUMN_UID + " =? ", args);
        return count;
    }


    public void insertAlarms(ArrayList<AlarmsObject> listAlarm, boolean clearPrevious) {
        if (clearPrevious) {
            deleteMovies();
        }


        String sql = "INSERT INTO " + AlarmHelper.TABLE_NAME + " (" + AlarmHelper.COLUMN_TIME + "," + AlarmHelper.COLUMN_CHECKSTATUS + "," +
                AlarmHelper.COLUMN_ALARM_ID + "," + AlarmHelper.COLUMN_ALARM_TIME + "," + AlarmHelper.COLUMN_REPEAT_CHECK + "," + AlarmHelper.COLUMN_DAYS_TO_REPEAT + ") VALUES(?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listAlarm.size(); i++) {
            AlarmsObject currentAlarm = listAlarm.get(i);
            String s = "";

            for (int j = 0; j < currentAlarm.getDays_to_repeat().length; j++) {

                if (currentAlarm.getDays_to_repeat()[j] == true && j == 0)
                    s = s + "Su/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 1)
                    s = s + "M/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 2)
                    s = s + "T/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 3)
                    s = s + "W/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 4)
                    s = s + "Th/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 5)
                    s = s + "F/";
                if (currentAlarm.getDays_to_repeat()[j] == true && j == 6)
                    s = s + "Sa/";
            }

           // Toast.makeText(c, "" + s, Toast.LENGTH_LONG).show();
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(1, currentAlarm.getTime());
            statement.bindLong(2, currentAlarm.isCheckedSwitch());
            statement.bindLong(3, currentAlarm.getAlarm_id());
            statement.bindLong(4, currentAlarm.getT());
            statement.bindLong(5, currentAlarm.getRepeat());
            statement.bindString(6, s);

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

    }

    public void deleteMovies() {
        mDatabase.delete(AlarmHelper.TABLE_NAME, null, null);
    }

    public ArrayList<AlarmsObject> readAlarms(Intent intent) {
        ArrayList<AlarmsObject> listAlarm = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                AlarmHelper.COLUMN_TIME,
                AlarmHelper.COLUMN_CHECKSTATUS,
                AlarmHelper.COLUMN_ALARM_ID,
                AlarmHelper.COLUMN_ALARM_TIME,
                AlarmHelper.COLUMN_REPEAT_CHECK,
                AlarmHelper.COLUMN_DAYS_TO_REPEAT
        };
        long id = 0;
        int t = 1;
        Cursor cursor = mDatabase.query(AlarmHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                AlarmsObject alarm = new AlarmsObject();

                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank movie object to contain our data
                alarm.setTime(cursor.getString(cursor.getColumnIndex(AlarmHelper.COLUMN_TIME)));
                alarm.setCheckedSwitch(cursor.getInt(cursor.getColumnIndex(AlarmHelper.COLUMN_CHECKSTATUS)));
                id = cursor.getLong(cursor.getColumnIndex(AlarmHelper.COLUMN_ALARM_ID));
                alarm.setAlarm_id(id);
                alarm.setAlarmManager((AlarmManager) c.getSystemService(Context.ALARM_SERVICE));
                alarm.setPi(PendingIntent.getBroadcast(c, (int) id
                        , intent, 0));
                alarm.setT(cursor.getLong(cursor.getColumnIndex(AlarmHelper.COLUMN_ALARM_TIME)));
                alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(AlarmHelper.COLUMN_REPEAT_CHECK)));
                try {
                    boolean[] b = {false, false, false, false, false, false, false};

                    String sv = cursor.getString(cursor.getColumnIndex(AlarmHelper.COLUMN_DAYS_TO_REPEAT));
                    // Toast.makeText(c,""+sv,Toast.LENGTH_LONG).show();

                    if (sv.contains("Su/"))
                        b[0] = true;
                    if (sv.contains("M/"))
                        b[1] = true;
                    if (sv.contains("T/"))
                        b[2] = true;
                    if (sv.contains("W/"))
                        b[3] = true;
                    if (sv.contains("Th/"))
                        b[4] = true;
                    if (sv.contains("F/"))
                        b[5] = true;
                    if (sv.contains("Sa/"))
                        b[6] = true;
                    alarm.setDays_to_repeat(b);

                } catch (NullPointerException e) {

                } catch (Exception e) {

                }

                //add the movie to the list of movie objects which we plan to return
                listAlarm.add(alarm);
            }
            while (cursor.moveToNext());
        }
        return listAlarm;
    }


    public String readArray(long id) {
        String[] columns = {
                AlarmHelper.COLUMN_DAYS_TO_REPEAT
        };
        String whereClause = "ALARM_ID = ?";
        String whereArgs[] = {
                "" + id
        };
        Cursor cursor = mDatabase.query(AlarmHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        String s = "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
                s = cursor.getString(cursor.getColumnIndex(AlarmHelper.COLUMN_DAYS_TO_REPEAT));
            } while (cursor.moveToNext());
        }
        return s;
    }
    public String readRepeat(long id) {

        String[] columns = {
                AlarmHelper.COLUMN_REPEAT_CHECK
        };
        String whereClause = "ALARM_ID = ?";
        String whereArgs[] = {
                "" + id
        };
        Cursor cursor = mDatabase.query(AlarmHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        String s = "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
                s = cursor.getString(cursor.getColumnIndex(AlarmHelper.COLUMN_REPEAT_CHECK));
            } while (cursor.moveToNext());
        }
        return s;
    }

    public void switchOffAlarm(long id){

    }

    private static class AlarmHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "alarmdb";
        private static final int DB_VERSION = 33;
        private Context mContext;

        public static final String TABLE_NAME = "ALARM_LIST";
        public static final String COLUMN_UID = "UID";
        public static final String COLUMN_TIME = "TIME";
        public static final String COLUMN_CHECKSTATUS = "CHECKSTATUS";
        public static final String COLUMN_ALARM_ID = "ALARM_ID";
        public static final String COLUMN_ALARM_TIME = "ALARM_TIME";
        public static final String COLUMN_REPEAT_CHECK = "REPEAT_CHECK";
        public static final String COLUMN_DAYS_TO_REPEAT = "DAYS_TO_REPEAT";
        public static final String CREATE_TABLE_ALARM_LIST = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TIME + " VARCHAR(20)," +
                COLUMN_CHECKSTATUS + " INTEGER," + COLUMN_ALARM_ID + " INTEGER," +
                COLUMN_ALARM_TIME + " INTEGER," +
                COLUMN_REPEAT_CHECK + " INTEGER," +
                COLUMN_DAYS_TO_REPEAT + " VARCHAR(20)" +
                ");";


        public AlarmHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ALARM_LIST);
            } catch (SQLiteException exception) {
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
                onCreate(db);
            } catch (SQLiteException e) {

            }


        }
    }
}
