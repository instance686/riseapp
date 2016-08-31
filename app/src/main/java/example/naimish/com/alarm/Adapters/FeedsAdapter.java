package example.naimish.com.alarm.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.naimish.com.alarm.Pojo.FeedsObject;
import example.naimish.com.alarm.R;

public class FeedsAdapter extends RecyclerView.Adapter {

    private List<FeedsObject> feedsList;

    Context c;

    public FeedsAdapter(Context context, List<FeedsObject> feeds, RecyclerView recyclerView) {
       feedsList = feeds;

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
                R.layout.feed_row, parent, false);
        vh = new FeedsViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedsViewHolder) {

            FeedsObject feedsObject = (FeedsObject) feedsList.get(position);

            ((FeedsViewHolder) holder).Sender.setText(feedsObject.getSender());

            ((FeedsViewHolder) holder).Label.setText(feedsObject.getLabel());


            ((FeedsViewHolder) holder).feeds = feedsObject;

        }
    }


    @Override
    public int getItemCount() {
        return feedsList.size();
    }

    public void delete(int position) {
        feedsList.remove(position);
        notifyItemRemoved(position);
    }


    //
    public class FeedsViewHolder extends RecyclerView.ViewHolder{
        public TextView Sender,Label;

        public FeedsObject feeds;

        public FeedsViewHolder(View v) {
            super(v);
            Sender= (TextView) v.findViewById(R.id.sender);
            Label= (TextView) v.findViewById(R.id.label);
        }

    }
}
