package com.iexamcenter.calendarweather.news;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.net.URL;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

public class ChannelPublishListAdapter extends RecyclerView.Adapter<ChannelPublishListAdapter.ItemViewHolder> {
    private ArrayList<RssEntity> mItems;
    private Context mContext;

    PrefManager mPref;

    public ChannelPublishListAdapter(Context context, ArrayList<RssEntity> arrayList) {
        mItems = arrayList;
        mContext = context;
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_news_publish, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder ll, final int position) {
        RssEntity obj = mItems.get(position);

        if (obj.rssPublish.contains("1")) {
            ll.toggle.setChecked(true);
            ll.toggle.setBackgroundResource(R.drawable.rounded_bg_page3_btn1);
        } else {
            ll.toggle.setChecked(false);
            ll.toggle.setBackgroundResource(R.drawable.rounded_bg_page3_btn2);
        }
        ll.channel.setText(obj.rssChannel);

        ll.toggle.setTag(obj);

        ll.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View buttonView) {
                buttonView.setEnabled(false);
                Observable.just(1)
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                boolean isChecked = ((ToggleButton) buttonView).isChecked();
                                RssEntity obj = (RssEntity) buttonView.getTag();

                                // RssEntity rssById = AppDatabase.getInstance(mContext).rssDao().getRssById(obj.row_id);


                               /* Uri url = SqliteHelper.CONTENT_URI_RSS;
                                final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.RssEntry.TABLE_RSS);

                                String selection = SqliteHelper.RssEntry._ID + "=?";
                                String[] selectionArgs = {"" + obj.row_id};
                                ContentValues cv = new ContentValues();*/
                                String pub = "";

                                if (isChecked) {

                                    // cv.put(SqliteHelper.RssEntry.KEY_RSS_PUBLISH, "1");
                                    mItems.get(position).rssPublish = "1";
                                    pub = "1";
                                    obj.rssPublish = "1";
                                    buttonView.setBackgroundResource(R.drawable.rounded_bg_page3_btn1);

                                } else {
                                    obj.rssPublish = "0";
                                    //cv.put(SqliteHelper.RssEntry.KEY_RSS_PUBLISH, "0");
                                    pub = "0";
                                    mItems.get(position).rssPublish = "0";
                                    buttonView.setBackgroundResource(R.drawable.rounded_bg_page3_btn2);

                                }

                                new UpdateTask(obj.row_id, pub).execute();
                                // mContext.getContentResolver().update(contenturi, cv, selection, selectionArgs);
                                buttonView.setTag(obj);
                                Intent intent = new Intent("RSS_DATA_UPDATE");
                                intent.putExtra("KEY", "PUBLISH");
                                //  mContext.sendBroadcast(intent);
                                buttonView.setEnabled(true);
                            }
                        });


            }
        });
        /*
        ll.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });*/

    }

    public  class UpdateTask extends AsyncTask<Void, Integer, Long> {
        int row_id;
        String pub;

        public UpdateTask(int row_id, String pub) {
            this.row_id = row_id;
            this.pub = pub;
        }

        protected Long doInBackground(Void... urls) {
            AppDatabase.getInstance(mContext).rssDao().updateById(row_id, pub);
            return 1L;
        }


        protected void onPostExecute(Long result) {

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {


        private TextView channel;

        ToggleButton toggle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            channel = itemView.findViewById(R.id.channel);
            toggle = itemView.findViewById(R.id.toggle);

        }

    }
}
