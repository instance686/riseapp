package example.naimish.com.alarm.Fragments;

/**
 * Created by naimish on 25/7/16.
 */

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.Calendar;

import example.naimish.com.alarm.Activities.AccpetedAlarm;
import example.naimish.com.alarm.Activities.GPLoginActivity;
import example.naimish.com.alarm.Activities.SetAlarm;
import example.naimish.com.alarm.Adapters.AlarmsAdapter2;
import example.naimish.com.alarm.MyApplication;
import example.naimish.com.alarm.Pojo.AlarmsObject;
import example.naimish.com.alarm.R;
import example.naimish.com.alarm.Recievers.AlarmReceiver;
import example.naimish.com.alarm.Utils;


public class Alarms extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView mRecyclerView;
    private static AlarmsAdapter2 mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static ArrayList<AlarmsObject> alarmsList = new ArrayList<>();
    private FloatingActionButton fab,sendalarm,sendreminder,addalarm;
    private Switch sw;
    private CheckBox repeat;
    private LinearLayout days;
    private ImageButton sun, mon, tue, wed, thurs, fri, sat;
    private Button viewAlarm;
    ImageView del;
    private SharedPreferences sp;
  private GoogleApiClient mGoogleApiClient;


    public Alarms() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("alarm_row", alarmsList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alarms, container, false);
        sp = getActivity().getSharedPreferences("al", Context.MODE_PRIVATE);
        fab = (FloatingActionButton) v.findViewById(R.id.fabBtn);
        addalarm= (FloatingActionButton) v.findViewById(R.id.fab1);
        sendalarm= (FloatingActionButton) v.findViewById(R.id.fab2);
        sendreminder= (FloatingActionButton) v.findViewById(R.id.fab3);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        fab.setTag("Off");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fab.getTag()=="Off"){
                    fab.setTag("On");
                    addalarm.show();
                    sendalarm.show();
                    sendreminder.show();
                    final Animation myRotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_fwd);
                    fab.startAnimation(myRotation);
                }
                else{
                    fab.setTag("Off");
                    addalarm.hide();
                    sendalarm.hide();
                    sendreminder.hide();
                    final Animation myRotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_bck);
                    fab.startAnimation(myRotation);
                }


            }
        });
        addalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = TimePickerDialogFragment.newInstance();
                showDialogFragment(newFragment);
            }
        });
        sendalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SetAlarm.class));
                Toast.makeText(getContext(),"Send Alarm",Toast.LENGTH_SHORT).show();
            }
        });
        sendreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // [START_EXCLUDE]
                                    //updateUI(false);
                                    startActivity(new Intent(getActivity(), GPLoginActivity.class));
                                    // [END_EXCLUDE]
                                }
                            });

                    //Toast.makeText(getContext(),"Send Reminder",Toast.LENGTH_SHORT).show();

            }
        });



       /* sendalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.close(true);
                 startActivity(new Intent(getActivity(),SetAlarm.class));
            }
        });*/


        mRecyclerView = (RecyclerView) v.findViewById(R.id.My_alarms);   //recycler view
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (savedInstanceState != null) {
            alarmsList = savedInstanceState.getParcelableArrayList("alarm_row");

        } else {
            alarmsList = new MyApplication(getActivity()).getWritableDatabase().readAlarms(new Intent(getActivity(), AlarmReceiver.class));
        }

        mAdapter = new AlarmsAdapter2(getActivity(), alarmsList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(getContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                sw = (Switch) view.findViewById(R.id.switch1);

                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            alarmsList.get(position).setCheckedSwitch(1);
                            String time[] = alarmsList.get(position).getTime().split(":");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

                            long d = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));

                            PendingIntent pendingIntent = alarmsList.get(position).getPi();
                            if (System.currentTimeMillis() >= d) {
                                d = d + (1000 * 60 * 60 * 24);
                                Toast.makeText(getContext(), "Alarm set for next day", Toast.LENGTH_SHORT).show();
                            }

                            if (alarmsList.get(position).getRepeat() == 0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                                    alarmsList.get(position).getAlarmManager().setExact(AlarmManager.RTC_WAKEUP, d, pendingIntent);
                                else
                                    alarmsList.get(position).getAlarmManager().set(AlarmManager.RTC_WAKEUP, d, pendingIntent);

                            } else {
                                alarmsList.get(position).getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, d, 1000 * 60 * 60 * 24, pendingIntent);
                            }
                            alarmsList.get(position).setT(d);


                                Utils.generateNotification(getContext(),alarmsList.get(position).getTime(),(int)d,d);

                            Toast.makeText(getActivity(), "Alarm on", Toast.LENGTH_SHORT).show();
                        } else {
                            alarmsList.get(position).setCheckedSwitch(0);
                            alarmsList.get(position).getAlarmManager().cancel(alarmsList.get(position).getPi());
                            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.cancel((int) alarmsList.get(position).getAlarm_id());
                            Toast.makeText(getActivity(), "Alarm off", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                repeat = (CheckBox) view.findViewById(R.id.repeat);
                days = (LinearLayout) view.findViewById(R.id.repeat_day);
                sun = (ImageButton) view.findViewById(R.id.Sun);
                mon = (ImageButton) view.findViewById(R.id.Mon);
                tue = (ImageButton) view.findViewById(R.id.Tue);
                wed = (ImageButton) view.findViewById(R.id.Wed);
                thurs = (ImageButton) view.findViewById(R.id.Thurs);
                fri = (ImageButton) view.findViewById(R.id.Fri);
                sat = (ImageButton) view.findViewById(R.id.Sat);
                viewAlarm= (Button) view.findViewById(R.id.viewAlarm);

                repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        alarmsList.get(position).getAlarmManager().cancel(alarmsList.get(position).getPi());
                        String time[] = alarmsList.get(position).getTime().split(":");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

                        long d = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                        PendingIntent pendingIntent = alarmsList.get(position).getPi();
                        if (System.currentTimeMillis() >= d) {
                            d+=1000 * 60 * 60 * 24;
                            Toast.makeText(getContext(), "Alarm set for next day", Toast.LENGTH_SHORT).show();
                        }

                        if (b) {
                            days.setVisibility(View.VISIBLE);
                            alarmsList.get(position).setRepeat(1);

                            alarmsList.get(position).getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, d, 1000 * 60 * 60 * 24, pendingIntent);
                            Toast.makeText(getContext(), "Repeat Alarm set", Toast.LENGTH_SHORT).show();

                        } else {
                            days.setVisibility(View.GONE);
                            alarmsList.get(position).setRepeat(0);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                                alarmsList.get(position).getAlarmManager().setExact(AlarmManager.RTC_WAKEUP, d, pendingIntent);
                            else
                                alarmsList.get(position).getAlarmManager().set(AlarmManager.RTC_WAKEUP, d, pendingIntent);
                            Toast.makeText(getContext(), "Non-repeat Alarm set", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                View.OnClickListener buttonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.Sun) {
                            if (alarmsList.get(position).getDays_to_repeat()[0])
                                sun.setImageResource(R.drawable.sunday_off);
                            else
                                sun.setImageResource(R.drawable.sunday_on);
                            alarmsList.get(position).setDayArray(0, !alarmsList.get(position).getDays_to_repeat()[0]);
                        } else if (v.getId() == R.id.Mon) {
                            if (alarmsList.get(position).getDays_to_repeat()[1])
                                mon.setImageResource(R.drawable.monday_off);
                            else
                                mon.setImageResource(R.drawable.monday_on);

                            alarmsList.get(position).setDayArray(1, !alarmsList.get(position).getDays_to_repeat()[1]);
                        } else if (v.getId() == R.id.Tue) {
                            if (alarmsList.get(position).getDays_to_repeat()[2])
                                tue.setImageResource(R.drawable.tuesday_off);
                            else
                                tue.setImageResource(R.drawable.tuesday_on);

                            alarmsList.get(position).setDayArray(2, !alarmsList.get(position).getDays_to_repeat()[2]);

                        } else if (v.getId() == R.id.Wed) {
                            if (alarmsList.get(position).getDays_to_repeat()[3])
                                wed.setImageResource(R.drawable.wednesday_off);
                            else
                                wed.setImageResource(R.drawable.wednesday_on);

                            alarmsList.get(position).setDayArray(3, !alarmsList.get(position).getDays_to_repeat()[3]);
                        } else if (v.getId() == R.id.Thurs) {
                            if (alarmsList.get(position).getDays_to_repeat()[4])
                                thurs.setImageResource(R.drawable.thursday_off);
                            else
                                thurs.setImageResource(R.drawable.thursday_on);

                            alarmsList.get(position).setDayArray(4, !alarmsList.get(position).getDays_to_repeat()[4]);
                        } else if (v.getId() == R.id.Fri) {
                            if (alarmsList.get(position).getDays_to_repeat()[5])
                                fri.setImageResource(R.drawable.friday_off);
                            else
                                fri.setImageResource(R.drawable.friday_on);

                            alarmsList.get(position).setDayArray(5, !alarmsList.get(position).getDays_to_repeat()[5]);
                        } else if (v.getId() == R.id.Sat) {
                            if (alarmsList.get(position).getDays_to_repeat()[6])
                                sat.setImageResource(R.drawable.saturday_off);
                            else
                                sat.setImageResource(R.drawable.saturday_on);

                            alarmsList.get(position).setDayArray(6, !alarmsList.get(position).getDays_to_repeat()[6]);
                        }
                        else if(v.getId()==R.id.viewAlarm){
                            startActivity(new Intent(getActivity(), AccpetedAlarm.class));
                        }

                    }
                };
                sun.setOnClickListener(buttonListener);
                mon.setOnClickListener(buttonListener);
                tue.setOnClickListener(buttonListener);
                wed.setOnClickListener(buttonListener);
                thurs.setOnClickListener(buttonListener);
                fri.setOnClickListener(buttonListener);
                sat.setOnClickListener(buttonListener);
                viewAlarm.setOnClickListener(buttonListener);


                del = (ImageView) view.findViewById(R.id.delete);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancel((int) alarmsList.get(position).getAlarm_id());
                        alarmsList.get(position).getAlarmManager().cancel(alarmsList.get(position).getPi());
                        alarmsList = (ArrayList<AlarmsObject>) mAdapter.delete(position);


                        //new MyApplication(getActivity()).getWritableDatabase().insertAlarms(alarmsList, true);

                    }
                });


            }

            @Override
            public void onLongClick(View view, final int position) {


            }
        }));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public static class TimePickerDialogFragment extends DialogFragment {
        public static TimePickerDialogFragment newInstance() {
            TimePickerDialogFragment newInstance = new TimePickerDialogFragment();
            return newInstance;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Handle any TimePickerDialog initialization here
            int iHour, iMinute;
            // Always init the time picker to current time

            Calendar cal1 = Calendar.getInstance();
            iHour = cal1.get(Calendar.HOUR_OF_DAY);
            iMinute = cal1.get(Calendar.MINUTE);
            TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), timeListener, iHour, iMinute, true);
            return timeDialog;
        }

        private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);


                long d = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));

                int a = (int) d;
                myIntent.putExtra("id", d);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), a, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                int p = alarmsList.size();

                boolean[] b = {true, true, true, true, true, true, true};
                if (minute < 10) {
                    alarmsList.add(new AlarmsObject((hourOfDay) + ":0" + minute, 1, d,
                            alarmManager, pendingIntent, calendar.getTimeInMillis(), b, 0));
                    Utils.generateNotification(getContext(),hourOfDay+":0"+minute,a,d);

                } else {
                    alarmsList.add(new AlarmsObject((hourOfDay) + ":" + minute, 1, d,
                            alarmManager, pendingIntent, calendar.getTimeInMillis(), b, 0));
                    Utils.generateNotification(getContext(),hourOfDay+":"+minute,a,d);
                }


                if (System.currentTimeMillis() >= d) {
                    d = d + (1000 * 60 * 60 * 24);
                    Toast.makeText(getContext(), "Alarm set for next day", Toast.LENGTH_SHORT).show();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, d, pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, d, pendingIntent);

                Log.d("srivastava", String.valueOf(d));

                new MyApplication(getActivity()).getWritableDatabase().insertAlarms(alarmsList, true);
                mAdapter.notifyItemInserted(alarmsList.size() - 1);


            }
        };
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // Toast.makeText(getActivity(), "OnDestroyView", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPause() {
        super.onPause();
       // Toast.makeText(getActivity(), "OnPause", Toast.LENGTH_SHORT).show();
        new MyApplication(getActivity()).getWritableDatabase().insertAlarms(alarmsList, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Toast.makeText(getActivity(),"OnStop",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(getActivity(),"OnDestroy",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Toast.makeText(getActivity(),"OnDetach",Toast.LENGTH_SHORT).show();
    }

    void showDialogFragment(DialogFragment newFragment) {
        newFragment.show(getFragmentManager(), null);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }


    class RecycleTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecycleTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));

                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {


        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


}


