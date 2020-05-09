package com.iexamcenter.calendarweather.festival;

import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangUtilityLighter;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class FestivalListAdapter extends RecyclerView.Adapter<FestivalListAdapter.ItemViewHolder> {
    int cellPad, viewHeight, iconDim, sunDim, moonWDim, moonHDim, dateSize;
    String dina;
    FrameLayout.LayoutParams flHighLight, flMoonTxt, flSunTxt, flSun, flMoon, flParamCenter, flParamTopCenter, flParamBottomCenter, flParamStartTop, flParamStartBottom;
    LinearLayout.LayoutParams llParamCenter;
    LinearLayout.LayoutParams param, titleParam, festivalParam;
    Resources res;
    private static MainActivity activity;
    ArrayList<FestivalFragment.MonthData> mItems;
    private int mPagePosition = 0;
    ArrayList<Integer> dayList;
    ArrayList<String> keyList;
    ArrayList<Integer> typeList;
    PrefManager mPref;
    public Integer[] moonImageArr;
    HashMap<String, CoreDataHelper> mPanchangHashMap;
    int displayMonth, displayYearInt, displayDayInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt;
    HashMap<String, ArrayList<String>> monthFestivalmap;
    int dayOfMonth, dayOfLastMonth, dayOfCurrMonth, dayOfWeek1st;
    String sansalStart = "", sansalEnd = "", samvatStart = "", samvatEnd = "", sakaddhaStart = "", sakaddhaEnd = "", monthHeaderStr, pkey_time_tharu, pkey_time_to, pkey_samvat, pkey_shakaddha, o_sal, o_san;
    PanchangUtilityLighter panchangUtility;

    public FestivalListAdapter(MainActivity context, ArrayList<FestivalFragment.MonthData> pglist, int pagePosition) {
        res = context.getResources();
        activity = context;
        mPagePosition = pagePosition;
        mItems = pglist;
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        panchangUtility = new PanchangUtilityLighter(CalendarWeatherApp.isPanchangEng ? 1 : 0,activity, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude());


        basicSetup();
    }

    private void basicSetup() {


        dina = res.getString(R.string.l_dina);
        o_sal = res.getString(R.string.l_sal);
        pkey_shakaddha = res.getString(R.string.l_shakaddha);
        pkey_samvat = res.getString(R.string.l_samvat);
        o_san = res.getString(R.string.l_san);
        pkey_time_tharu = res.getString(R.string.l_time_tharu);
        pkey_time_to = res.getString(R.string.l_time_to);
        viewHeight = Utility.getInstance(activity).dpToPx(80);
        iconDim = Utility.getInstance(activity).dpToPx(60);
        moonHDim = Utility.getInstance(activity).dpToPx(30);
        moonWDim = Utility.getInstance(activity).dpToPx(35);
        //sunDim = Utility.getInstance(activity).dpToPx(30);
        cellPad = Utility.getInstance(activity).dpToPx(5);

        llParamCenter = new LinearLayout.LayoutParams(iconDim,
                iconDim
                , Gravity.CENTER
        );
        flMoon = new FrameLayout.LayoutParams(moonWDim,
                moonHDim
                , Gravity.TOP | Gravity.END
        );
        flSun = new FrameLayout.LayoutParams(moonWDim,
                moonHDim
                , Gravity.START | Gravity.BOTTOM
        );

        flMoonTxt = new FrameLayout.LayoutParams(moonWDim,
                moonWDim
                , Gravity.TOP | Gravity.END
        );
        flSunTxt = new FrameLayout.LayoutParams(moonWDim,
                moonWDim
                , Gravity.START | Gravity.BOTTOM
        );

        flHighLight = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.CENTER
        );
        flParamCenter = new FrameLayout.LayoutParams(iconDim,
                iconDim
                , Gravity.CENTER
        );
        titleParam = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , 1f
        );

        dateSize = Utility.getInstance(activity).dpToPx(8);
        param = new LinearLayout.LayoutParams(0,
                viewHeight
                , 1f
        );
        festivalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , 1f
        );
        flParamStartTop = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.START | Gravity.TOP
        );
        flParamTopCenter = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.TOP | Gravity.END
        );
        flParamStartBottom = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.START | Gravity.BOTTOM
        );
        flParamBottomCenter = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
        );


    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_festival_list;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    ItemViewHolder holder;

    @Override
    public void onBindViewHolder(final ItemViewHolder mholder, int position) {

        holder = mholder;

        dayList = new ArrayList<>();
        keyList = new ArrayList<>();
        typeList = new ArrayList<>();

        mPanchangHashMap = new HashMap<>();
        monthFestivalmap = new HashMap<>();
        FestivalFragment.MonthData obj = mItems.get(position);
        if(obj.mDay.getmLunarMonthType()==1){
            mholder.itemView.setAlpha(0.5f);
        }else{
            mholder.itemView.setAlpha(1f);
        }
        setCellData(obj.mDay, obj.mFestival);

        Log.e("setCellData", "setCellData::" + mItems.size());

       /* Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);

        Calendar dispcal = Calendar.getInstance();

        dispcal.add(Calendar.MONTH, mPagePosition);


        displayYearInt = dispcal.get(Calendar.YEAR);
        displayMonthInt = dispcal.get(Calendar.MONTH);
        displayDayInt = 1;
        if (currYearInt == displayYearInt && currMonthInt == displayMonthInt)
            displayDayInt = dispcal.get(Calendar.DAY_OF_MONTH);


        displayMonth = displayMonthInt;


        setCalendarCellData(holder);*/


    }

    public void setCellData(CoreDataHelper myCoreData, ArrayList<String> festObj) {

        try {


            PanchangUtilityLighter.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData);

            if (festObj != null && festObj.size() > 0) {
                String headerStr = myPanchangObj.le_month_short + " " + myPanchangObj.le_day + ", " + myPanchangObj.le_year + "(" + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + " " + dina + ")";
                holder.header.setText("" + headerStr);


                if (!mPref.getMyLanguage().contains("en") && !mPref.getMyLanguage().contains("kn")) {
                    holder.fest1.setVisibility(View.VISIBLE);
                    holder.fest1.setText("" + festObj.get(0));
                }else{
                    holder.fest1.setVisibility(View.GONE);
                }
                holder.fest2.setText("" + festObj.get(1));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);


    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView header, fest1, fest2;

        public ItemViewHolder(View rootView) {
            super(rootView);
            header = rootView.findViewById(R.id.header);
            fest1 = rootView.findViewById(R.id.fest1);
            fest2 = rootView.findViewById(R.id.fest2);


        }


    }


}
