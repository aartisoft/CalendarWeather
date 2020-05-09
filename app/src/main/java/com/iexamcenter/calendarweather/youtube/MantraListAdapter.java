package com.iexamcenter.calendarweather.youtube;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

import java.util.ArrayList;

public class MantraListAdapter extends RecyclerView.Adapter<MantraListAdapter.ItemViewHolder> {
    Context mContext;
    ArrayList<MantraFragment.YoutubeList> mItems;

    public MantraListAdapter(Context context, ArrayList<MantraFragment.YoutubeList> pglist) {

        mContext = context;
        mItems = pglist;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_videoitem;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final MantraFragment.YoutubeList obj = mItems.get(position);
        holder.tvTitle.setText((position + 1) + ")" + obj.lTitle);
        holder.youtube.setTag(obj);
        String IMAGE_URL = "https://i1.ytimg.com/vi/" + obj.vKey + "/hqdefault.jpg";
        Glide.with(mContext).load(IMAGE_URL).into(holder.youTubeThumbnailView);
        // https://i1.ytimg.com/vi/2R8EwjM3WWY/hqdefault.jpg
      //  holder.youTubeThumbnailView.setTag(obj);



        holder.youtube.setTag(obj);
       // holder.youTubeThumbnailView.setTag(obj);
        holder.youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MantraFragment.YoutubeList md =(MantraFragment.YoutubeList)  view.getTag();
                FragmentManager fm = ((MainActivity) mContext).getSupportFragmentManager();
                Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_YOUTUBE_VIDEO_TAG);

                if (frag != null){
                    ((YoutubeFragment) frag).playVideo(md.vKey, md.lTitle, md.eTitle, true);
                }

                else {
                    frag = YoutubeFragment.newInstance();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle arg = new Bundle();
                    arg.putInt("ROW_ID", 1);
                    arg.putString("VIDEO_ID", md.vKey);
                    arg.putString("CURR_PAGE", "1");
                    arg.putString("TITLE", md.eTitle);
                    frag.setArguments(arg);
                    ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_YOUTUBE_VIDEO_TAG);

                    ft.addToBackStack(AppConstants.FRAG_YOUTUBE_VIDEO_TAG);
                    ft.commit();
                    Log.e("VIDEO_ID","VIDEO_ID:"+md.vKey);

                }





            }
        });




    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView youTubeThumbnailView;
        public CardView youtube;
        public ImageView playButton;
        public RelativeLayout relativeLayoutOverYouTubeThumbnailView;


        public ItemViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title1);
            youTubeThumbnailView = view.findViewById(R.id.youtube_thumbnail);
            relativeLayoutOverYouTubeThumbnailView = view.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youtube = view.findViewById(R.id.youtube);
            playButton = view.findViewById(R.id.btnYoutube_player);

        }


    }
}
