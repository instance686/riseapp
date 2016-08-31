package example.naimish.com.alarm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

import example.naimish.com.alarm.Pojo.ListItem;
import example.naimish.com.alarm.R;

/**
 * Created by naimish on 2/8/16.
 */
public class MyCustomAdapter extends BaseAdapter {

    private static final int LAYOUT_0 = 0;
    private static final int LAYOUT_1 = 1;
    private static final int LAYOUT_2 = 2;
    private static final int LAYOUT_3 = 3;
    private static final int MAX_LAYOUT_COUNT = 4;

    private ArrayList<ListItem> mData = new ArrayList<ListItem>();
    private LayoutInflater mInflater;

    MyCustomAdapter adapter;
    Context context;

    public MyCustomAdapter(Context context) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        //Toast.makeText(this.context,"i am adapter",Toast.LENGTH_SHORT).show();
    }

    public void addItem(final ListItem listItem) {
        //Toast.makeText(this.context,"i am addItem",Toast.LENGTH_SHORT).show();
        mData.add(listItem);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if(position==4)
            return LAYOUT_3;
        else if(position==1)
            return LAYOUT_2;
        else if(position==2)
            return LAYOUT_1;
        else
            return LAYOUT_0;
    }

    @Override
    public int getViewTypeCount() {
        return MAX_LAYOUT_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ListItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Toast.makeText(this.context,"i am getVIew",Toast.LENGTH_SHORT).show();
        ViewHolder holder = null;
        ListItem listItem = mData.get(position);
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case LAYOUT_0:
                    convertView = mInflater.inflate(R.layout.mylist, null);
                    holder.heading = (TextView) convertView.findViewById(R.id.heading);
                    holder.subheading = (TextView) convertView.findViewById(R.id.subheading);
                    holder.heading.setText(listItem.getHeading());
                    holder.subheading.setText(listItem.getsubHeading());
                    break;
                case LAYOUT_1:
                    convertView = mInflater.inflate(R.layout.mylist1, null);
                    holder.label = (EditText) convertView.findViewById(R.id.label);
                    break;
                case LAYOUT_2:
                    convertView = mInflater.inflate(R.layout.mylist2, null);
                    break;
                case LAYOUT_3:
                    convertView = mInflater.inflate(R.layout.mylist3, null);
                    break;

            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case LAYOUT_0:
                holder.heading.setText(listItem.getHeading());
                holder.subheading.setText(listItem.getsubHeading());
                break;

        }
        convertView.setTag(holder);
        return convertView;
    }


    public static class ViewHolder {
        public TextView heading;
        public TextView subheading;
        public EditText label;
    }
}