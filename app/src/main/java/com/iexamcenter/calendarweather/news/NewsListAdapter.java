package com.iexamcenter.calendarweather.news;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;
import com.iexamcenter.calendarweather.youtube.RssVideoFragment;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ItemViewHolder> {
    private ArrayList<NewsEntity> mItems;
    private MainActivity mContext;
    boolean isConnected, isMobileData;
    PrefManager mPref;

    public NewsListAdapter(MainActivity context, ArrayList<NewsEntity> arrayList) {
        mItems = arrayList;
        mContext = context;
        mPref = PrefManager.getInstance(mContext);
        mPref.load();

        isMobileData = Utility.getInstance(mContext).isConnectedMobile();

        isConnected = Connectivity.isConnected(context);

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_newsitem, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {

        NewsEntity obj = mItems.get(position);

        String title =obj.newsTitle;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_TITLE));
        String IMAGE_URL =obj.newsImage;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_IMAGE));
        String link =obj.newsLink;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_LINK));
        int row_id =obj.row_id;// cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry._ID));
        String category =obj.newsCat;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_CAT));
        int rssType =Integer.parseInt(obj.newsType);// cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_TYPE));
        String pubTimeStamp =""+obj.newsTimeStamp;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP));
        String videoId =obj.newsVideoId;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_VIDEO_ID));
        String pubDate =obj.newsPubData;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE));

        String channel =obj.newsChannel;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL));
        mydata md = new mydata();
        md.link = link;
        md.news_type = rssType;
        md.row_id = row_id;
        md.title = title;
        md.image = IMAGE_URL;
        md.videoId = videoId;

        if (IMAGE_URL != null && !IMAGE_URL.isEmpty()) {
            Glide.with(mContext)
                    .load(IMAGE_URL)
                    .into(viewHolder.imageView);
        }

        String timestamp;
        if (rssType == 1)
            timestamp = Utility.getInstance(mContext).getTimeAgo(Long.parseLong(pubTimeStamp) * 1000);
        else
            timestamp = Utility.getInstance(mContext).getMyDateTimeFormat(pubDate);


        viewHolder.itemView.setTag(md);
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDomain.setText(category + ", " + channel + " " + timestamp);

        viewHolder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvTitle.setMaxLines(2);
        if (rssType == 2) {
            viewHolder.btnYoutube_player.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnYoutube_player.setVisibility(View.GONE);

        }
        viewHolder.itemView.setOnClickListener(v -> {
            mydata md1 = (mydata) v.getTag();
            FragmentManager fm = mContext.getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_RSS_VIDEO_TAG);

            if (frag != null) {
                ((RssVideoFragment) frag).playVideo(md1.videoId, md1.title, md1.title, true);
            } else {
                frag = RssVideoFragment.newInstance();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle arg = new Bundle();
                arg.putInt("ROW_ID", md1.row_id);
                arg.putString("VIDEO_ID", md1.videoId);
                arg.putString("CURR_PAGE", "1");
                arg.putString("TITLE", md1.title);
                frag.setArguments(arg);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_RSS_VIDEO_TAG);

                ft.addToBackStack(AppConstants.FRAG_RSS_VIDEO_TAG);
                ft.commit();
            }


        });



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitle, tvDomain;
        ImageView imageView;
        ImageView btnYoutube_player;
        CardView main;


        ToggleButton toggle;

        public ItemViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            main = view.findViewById(R.id.main);
            tvDomain = view.findViewById(R.id.domain);
            btnYoutube_player = view.findViewById(R.id.btnYoutube_player);


        }

    }
    public static class mydata {
        int row_id;
        int news_type;
        String link, title, image, videoId;
    }
}
