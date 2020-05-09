package com.iexamcenter.calendarweather.thisday;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.response.ThisDayResponse;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<ThisDayResponse.HISTORY> mItems;
    private int lastPosition = -1;
    int mPage = 0;
    PrefManager mPref;

    public HistoryAdapter(MainActivity context, ArrayList<ThisDayResponse.HISTORY> pglist, int page) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;
        mPref=PrefManager.getInstance(mContext);
        mPref.load();
        mPage = page;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_history;




        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);




        ItemViewHolder vh = new ItemViewHolder(view, new HistoryAdapter.ItemViewHolder.IMyViewHolderInterface() {

            @Override
            public void details(View v) {
                ThisDayResponse.HISTORY obj = (ThisDayResponse.HISTORY) v.getTag();
                // obj.
                FragmentManager fm = mContext.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_PROFILE_DETAILS);
                if (frag == null)
                    frag = ProfileDeatilsFragment.newInstance();


                Bundle b = new Bundle();
                b.putString("PKEY", obj.getPkey());
                b.putString("NAME", obj.getName());
                b.putString("FULLNAME", obj.getFullname());
                b.putString("BORN", obj.getBirthday());
                b.putString("BORNPLACE", obj.getBirthplace());
                b.putString("DEATH", obj.getDeathday());
                b.putString("JOBTITLE", obj.getJobtitle());
                b.putString("FAMOUSFOR", obj.getFamousfor());
                b.putString("HISTORY", obj.getEvent());
                b.putString("IMAGELINK", obj.getImagelink());
                b.putString("WIKI", obj.getWikilink());
                frag.setArguments(b);
                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_PROFILE_DETAILS);
                ft.addToBackStack(AppConstants.FRAG_PROFILE_DETAILS);
                ft.commit();

            }

        });
        return vh;


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ThisDayResponse.HISTORY obj = mItems.get(position);
        String IMAGE_URL = mPref.getThisDayImageBaseUrl() + obj.getImagelink();
        holder.container.setTag(obj);
        holder.title.setText(obj.getName());
        holder.event.setText(obj.getEventlist().replace(obj.getEventdate(),"").trim());
        holder.eventDate.setText("Happened On: "+obj.getEventdate());
        if (!IMAGE_URL.isEmpty())
            Glide.with(mContext).load(IMAGE_URL).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title, event,eventDate;
        public ImageView profileImage;

        HistoryAdapter.ItemViewHolder.IMyViewHolderInterface mListener;
        public LinearLayout container;

        public ItemViewHolder(View itemView , IMyViewHolderInterface listener) {
            super(itemView);
            mListener = listener;
            container = itemView.findViewById(R.id.profileContainer);

            profileImage = itemView.findViewById(R.id.profileImage);
            title = itemView.findViewById(R.id.title);
            event = itemView.findViewById(R.id.event);
            eventDate= itemView.findViewById(R.id.eventDate);
            container.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profileContainer:
                    mListener.details(v);
                    break;

            }
        }

        public interface IMyViewHolderInterface {
            void details(View v);

        }


    }
}
