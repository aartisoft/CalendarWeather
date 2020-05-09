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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BirthDayAdapter extends RecyclerView.Adapter<BirthDayAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<ThisDayResponse.BIRTHDAY> mItems;
    private int lastPosition = -1;
    int mPage = 0;
    PrefManager mPref;
    public BirthDayAdapter(MainActivity context, ArrayList<ThisDayResponse.BIRTHDAY> pglist, int page) {
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

        layoutRes = R.layout.inflate_birthdaylist;




        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);




        ItemViewHolder vh = new ItemViewHolder(view, new BirthDayAdapter.ItemViewHolder.IMyViewHolderInterface() {

            @Override
            public void details(View v) {
                ThisDayResponse.BIRTHDAY obj = (ThisDayResponse.BIRTHDAY) v.getTag();
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
        final ThisDayResponse.BIRTHDAY obj = mItems.get(position);
        String IMAGE_URL = mPref.getThisDayImageBaseUrl() + obj.getImagelink();

        holder.title.setText(obj.getName());
        if(obj.getDeathday().trim().isEmpty()) {
            holder.age.setText(getAge(obj.getBirthday()) + " Birthday");
        }else{
            holder.age.setText(getYear(obj.getBirthday(),obj.getDeathday()));
        }
        if (!IMAGE_URL.isEmpty())
            Glide.with(mContext).load(IMAGE_URL).into(holder.profileImage);
        holder.container.setTag(obj);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title, age;
        public ImageView profileImage;
        BirthDayAdapter.ItemViewHolder.IMyViewHolderInterface mListener;
        public LinearLayout container;

        public ItemViewHolder(View itemView , IMyViewHolderInterface listener) {
            super(itemView);
            mListener = listener;
            container = itemView.findViewById(R.id.container);

            profileImage = itemView.findViewById(R.id.profileImage);
            title = itemView.findViewById(R.id.title);
            age = itemView.findViewById(R.id.age);
            container.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container:
                    mListener.details(v);
                    break;

            }
        }

        public interface IMyViewHolderInterface {
            void details(View v);

        }

    }

    private String getYear(String bDay,String dDay){
        // String string = "January 2, 2010";
        long timeInMillis;
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(bDay);
        } catch (ParseException e) {

            e.printStackTrace();
            return "";
        }
        timeInMillis = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis (timeInMillis);
        String bYear= ""+calendar.get(Calendar.YEAR);
        try {
            date = format.parse(dDay);
        } catch (ParseException e) {

            e.printStackTrace();
            return "";
        }
        timeInMillis = date.getTime();
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis (timeInMillis);
        String dYear= ""+calendar.get(Calendar.YEAR);
        return  bYear+" - "+dYear;



    }
    private String getAge(String bDay){
        // String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(bDay);
        } catch (ParseException e) {

            e.printStackTrace();
            return "";
        }
        long timeInMillis = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis (timeInMillis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}
