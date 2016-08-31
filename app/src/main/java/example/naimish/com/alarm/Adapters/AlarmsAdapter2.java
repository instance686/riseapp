package example.naimish.com.alarm.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import example.naimish.com.alarm.Pojo.AlarmsObject;
import example.naimish.com.alarm.R;

public class AlarmsAdapter2 extends RecyclerView.Adapter {

    private List<AlarmsObject> alarmList;

    Context c;

    public AlarmsAdapter2(Context context, List<AlarmsObject> alarms, RecyclerView recyclerView) {
        alarmList = alarms;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            c = context;


        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,        //contens of recyclerview
                                                      int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.alarm_row, parent, false);
        vh = new AlarmViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlarmViewHolder) {

            AlarmsObject alarmsObject = (AlarmsObject) alarmList.get(position);

            ((AlarmViewHolder) holder).Time.setText(alarmsObject.getTime());
            if(alarmsObject.isCheckedSwitch()==1){
                ((AlarmViewHolder) holder).toggle.setChecked(true);
            }
            else{
                ((AlarmViewHolder) holder).toggle.setChecked(false);

            }
            if(alarmsObject.getRepeat()==1){
                ((AlarmViewHolder)holder).checkBox.setChecked(true);
                ((AlarmViewHolder)holder).linearLayout.setVisibility(View.VISIBLE);

            }
            else if(alarmsObject.getRepeat()==0){
                ((AlarmViewHolder)holder).checkBox.setChecked(false);
                ((AlarmViewHolder)holder).linearLayout.setVisibility(View.GONE);
            }

            if(alarmsObject.getDays_to_repeat()[0])
                ((AlarmViewHolder)holder).sun.setImageResource(R.drawable.sunday_on);
            else
                ((AlarmViewHolder)holder).sun.setImageResource(R.drawable.sunday_off);

            if(alarmsObject.getDays_to_repeat()[1])
                ((AlarmViewHolder)holder).mon.setImageResource(R.drawable.monday_on);
            else
                ((AlarmViewHolder)holder).mon.setImageResource(R.drawable.monday_off);

            if(alarmsObject.getDays_to_repeat()[2])
                ((AlarmViewHolder)holder).tue.setImageResource(R.drawable.tuesday_on);
            else
                ((AlarmViewHolder)holder).tue.setImageResource(R.drawable.tuesday_off);

            if(alarmsObject.getDays_to_repeat()[3])
                ((AlarmViewHolder)holder).wed.setImageResource(R.drawable.wednesday_on);
            else
                ((AlarmViewHolder)holder).wed.setImageResource(R.drawable.wednesday_off);

            if(alarmsObject.getDays_to_repeat()[4])
                ((AlarmViewHolder)holder).thurs.setImageResource(R.drawable.thursday_on);
            else
                ((AlarmViewHolder)holder).thurs.setImageResource(R.drawable.thursday_off);

            if(alarmsObject.getDays_to_repeat()[5])
                ((AlarmViewHolder)holder).fri.setImageResource(R.drawable.friday_on);
            else
                ((AlarmViewHolder)holder).fri.setImageResource(R.drawable.friday_off);

            if(alarmsObject.getDays_to_repeat()[6])
                ((AlarmViewHolder)holder).sat.setImageResource(R.drawable.saturday_on);
            else
                ((AlarmViewHolder)holder).sat.setImageResource(R.drawable.saturday_off);






            ((AlarmViewHolder) holder).alarm = alarmsObject;

        }
    }


    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public List<AlarmsObject> delete(int position) {
        alarmList.remove(position);
        notifyItemRemoved(position);
        return alarmList;
    }


    //
    public class AlarmViewHolder extends RecyclerView.ViewHolder{
        public TextView Time;
        public ImageView delete;
        public Switch toggle;
        public CheckBox checkBox;
        LinearLayout linearLayout;
        ImageButton sun, mon, tue, wed, thurs, fri, sat;
        public Button viewAlarm;

        public AlarmsObject alarm;

        public AlarmViewHolder(View v) {
            super(v);

            Time = (TextView) v.findViewById(R.id.Contact);
            delete = (ImageView) v.findViewById(R.id.delete);
            toggle= (Switch) v.findViewById(R.id.switch1);
            checkBox= (CheckBox) v.findViewById(R.id.repeat);
            linearLayout= (LinearLayout) v.findViewById(R.id.repeat_day);
            sun = (ImageButton) v.findViewById(R.id.Sun);
            mon = (ImageButton) v.findViewById(R.id.Mon);
            tue = (ImageButton) v.findViewById(R.id.Tue);
            wed = (ImageButton) v.findViewById(R.id.Wed);
            thurs = (ImageButton) v.findViewById(R.id.Thurs);
            fri = (ImageButton) v.findViewById(R.id.Fri);
            sat = (ImageButton) v.findViewById(R.id.Sat);
            viewAlarm= (Button) v.findViewById(R.id.viewAlarm);


        }
    }
}


