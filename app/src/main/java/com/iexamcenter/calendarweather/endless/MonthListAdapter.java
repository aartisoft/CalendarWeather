package com.iexamcenter.calendarweather.endless;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangUtilityLighter;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MonthListAdapter extends RecyclerView.Adapter<MonthListAdapter.ItemViewHolder> {
    int cellPad, viewHeight, iconDim, sunDim, moonWDim, moonHDim, dateSize;
    String le_dina;
    FrameLayout.LayoutParams flHighLight, flMoonTxt, flSunTxt, flSun, flMoon, flParamCenter, flParamTopCenter, flParamBottomCenter, flParamStartTop, flParamStartBottom;
    LinearLayout.LayoutParams llParamCenter;
    LinearLayout.LayoutParams param, titleParam, festivalParam;
    Resources res;
    private static MainActivity mContext;
    ArrayList<MonthListFragment.MonthData> mItems;
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
    String sansalStart = "", sansalEnd = "", samvatStart = "", samvatEnd = "", sakaddhaStart = "", sakaddhaEnd = "", monthHeaderStr, le_time_tharu, le_time_to, le_samvat, le_shakaddha, le_sal, le_san;
    PanchangUtilityLighter panchangUtility;
    int mType;
    String[] le_arr_bara2;

    public MonthListAdapter(MainActivity context, ArrayList<MonthListFragment.MonthData> pglist, int pagePosition) {
        res = context.getResources();
        mContext = context;
        mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
        getMyResource();
        mPagePosition = pagePosition;
        mItems = pglist;
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        moonImageArr = getMoonImage();
        panchangUtility = new PanchangUtilityLighter(CalendarWeatherApp.isPanchangEng ? 1 : 0, mContext, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude());
        basicSetup();
    }

    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {
            le_arr_bara2 = res.getStringArray(R.array.l_arr_bara2);
            le_dina = res.getString(R.string.l_dina);
            le_sal = res.getString(R.string.l_sal);
            le_shakaddha = res.getString(R.string.l_shakaddha);
            le_samvat = res.getString(R.string.l_samvat);
            le_san = res.getString(R.string.l_san);
            le_time_tharu = res.getString(R.string.l_time_tharu);
            le_time_to = res.getString(R.string.l_time_to);
        } else {
            le_arr_bara2 = res.getStringArray(R.array.e_arr_bara2);
            le_dina = res.getString(R.string.e_dina);
            le_sal = res.getString(R.string.e_sal);
            le_shakaddha = res.getString(R.string.e_shakaddha);
            le_samvat = res.getString(R.string.e_samvat);
            le_san = res.getString(R.string.e_san);
            le_time_tharu = res.getString(R.string.e_time_tharu);
            le_time_to = res.getString(R.string.e_time_to);
        }
    }

    private void basicSetup() {
        viewHeight = Utility.getInstance(mContext).dpToPx(80);
        iconDim = Utility.getInstance(mContext).dpToPx(60);
        moonHDim = Utility.getInstance(mContext).dpToPx(30);
        moonWDim = Utility.getInstance(mContext).dpToPx(35);
        //sunDim = Utility.getInstance(activity).dpToPx(30);
        cellPad = Utility.getInstance(mContext).dpToPx(5);

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

        dateSize = Utility.getInstance(mContext).dpToPx(8);
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

    private void addTitle(ItemViewHolder holder) {
        holder.daysContainer.removeAllViews();
        String[] bara;
      /*  if (mPref.getMyLanguage().contains("or") && mType==0) {
            bara = new String[]{"ରବି", "ସୋମ", "ମଙ୍ଗଳ", "ବୁଧ", "ଗୁରୁ", "ଶୁକ୍ର", "ଶନି"};

        } else {
            bara = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        }*/
        for (int i = 0; i < 7; i++) {
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(titleParam);
            String day = "";
            day = le_arr_bara2[i];

            tv.setText(day);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.RED);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            holder.daysContainer.addView(tv);
        }

    }

    public void setCalendarCellData(ItemViewHolder holder) {
        holder.week1Container.removeAllViews();
        holder.week2Container.removeAllViews();
        holder.week3Container.removeAllViews();
        holder.week4Container.removeAllViews();
        holder.week5Container.removeAllViews();
        holder.week6Container.removeAllViews();
        keyList.clear();
        int currYear, prevYear, nextYear;
        int currMonth, prevMonth, nextMonth;

        currMonthInt = displayMonthInt;
        currYearInt = displayYearInt;
        currDayInt = displayDayInt;
        Calendar cal = Calendar.getInstance();

        cal.set(displayYearInt, displayMonthInt, displayDayInt);

        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        dayOfCurrMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        currYear = cal.get(Calendar.YEAR);
        currMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeek1st = cal.get(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        cal.add(Calendar.MONTH, -1);
        prevYear = cal.get(Calendar.YEAR);
        prevMonth = cal.get(Calendar.MONTH);
        dayOfLastMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.add(Calendar.MONTH, 2);
        nextYear = cal.get(Calendar.YEAR);
        nextMonth = cal.get(Calendar.MONTH);

        try {
            int index1;
            index1 = 0;
            dayList = new ArrayList<>();
            typeList = new ArrayList<>();
            for (int i = dayOfWeek1st - 2; i >= 0; i--) {
                try {
                    int day = (dayOfLastMonth - i);
                    keyList.add("" + prevYear + "_" + prevMonth + "_" + day);
                    dayList.add(day);
                    typeList.add(-1);
                    index1++;
                } catch (Exception e) {
                    index1++;
                    e.printStackTrace();

                }

            }

            for (int i = 1; i <= dayOfCurrMonth; i++) {
                try {
                    keyList.add("" + currYear + "_" + currMonth + "_" + i);
                    dayList.add(i);
                    typeList.add(0);


                    index1++;
                } catch (Exception e) {
                    index1++;
                    e.printStackTrace();
                }
            }


            int dayMax1 = 42;
            if ((dayOfCurrMonth + dayOfWeek1st) <= 36)
                dayMax1 = 35;
            for (int k = 1, i = index1; i <= dayMax1; i++) {
                try {
                    keyList.add("" + nextYear + "_" + nextMonth + "_" + k);
                    dayList.add(k);
                    typeList.add(1);

                    k++;
                    index1++;
                } catch (Exception e) {
                    index1++;
                    e.printStackTrace();
                }
            }


            for (int i = 0; i < index1 - 1; i++) {
                int day = dayList.get(i);

                String keyTag = keyList.get(i);
                FrameLayout fl;
                FrameLayout fll;
                if (typeList.get(i) == -1) {
                    fl = getLayout();
                    fll = setDate(fl, day);
                    fll.setTag(keyTag);

                    addChild(i, fll, holder);

                    fl.setAlpha(0.2f);
                } else if (typeList.get(i) == 0) {
                    fl = getLayout();
                    fll = setDate(fl, day);
                    fll.setTag(keyTag);
                    addChild(i, fll, holder);
                    if (day == currDayInt) {
                        (fl.getChildAt(0)).setVisibility(View.VISIBLE);
                        ((TextView) fl.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) fl.getChildAt(4)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) fl.getChildAt(5)).setTypeface(Typeface.DEFAULT_BOLD);

                    }


                } else if (typeList.get(i) == 1) {
                    fl = getLayout();
                    fll = setDate(fl, day);
                    fll.setTag(keyTag);
                    fl.setAlpha(0.2f);

                    addChild(i, fll, holder);

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrameLayout setDate(FrameLayout fl, int day) {

        try {

            ImageView ivs = new ImageView(mContext);
            ivs.setImageResource(R.drawable.ic_today_highlight);

            ivs.setVisibility(View.GONE);
            ivs.setLayoutParams(flHighLight);
            fl.addView(ivs);
            ImageView iv = new ImageView(mContext);
            iv.setColorFilter(ContextCompat.getColor(mContext, R.color.sunMoon), android.graphics.PorterDuff.Mode.MULTIPLY);

            iv.setImageResource(R.drawable.ic_calsun);
            iv.setLayoutParams(flSun);
            iv.setAlpha(0.2f);
            fl.addView(iv);


            ImageView iv1 = new ImageView(mContext);
            iv1.setColorFilter(ContextCompat.getColor(mContext, R.color.sunMoon), android.graphics.PorterDuff.Mode.MULTIPLY);

            iv1.setImageResource(R.drawable.wmoon9);
            iv1.setLayoutParams(flMoon);
            iv1.setAlpha(0.2f);
            fl.addView(iv1);

            TextView tv1 = new TextView(mContext);
            tv1.setLayoutParams(flSunTxt);
            tv1.setText("" + day);
            tv1.setAlpha(0.7f);
            tv1.setGravity(Gravity.CENTER);
            fl.addView(tv1);

            TextView tv2 = new TextView(mContext);
            tv2.setLayoutParams(flMoonTxt);
            tv2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tv2.setMarqueeRepeatLimit(-1);
            tv2.setSelected(true);
            tv2.setSingleLine(true);
            tv2.setText("" + day);
            tv2.setAlpha(0.7f);
            tv2.setGravity(Gravity.CENTER);
            fl.addView(tv2);


            TextView tv = new TextView(mContext);
            tv.setLayoutParams(flParamCenter);
            tv.setText("" + day);
            tv.setPadding(0, cellPad, 0, cellPad);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(dateSize);
            fl.addView(tv);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return fl;


    }

    private void addChild(int index, FrameLayout fl, ItemViewHolder holder) {
        if (index < 7) {
            holder.week1Container.addView(fl);
        } else if (index < 14) {
            holder.week2Container.addView(fl);
        } else if (index < 21) {
            holder.week3Container.addView(fl);
        } else if (index < 28) {
            holder.week4Container.addView(fl);

        } else if (index < 35) {
            holder.week5Container.addView(fl);

        } else if (index < 42) {
            holder.week6Container.addView(fl);

        }

        fl.setOnClickListener(view -> {

            String key = (String) view.getTag();

            String[] keyArr = key.split("_");
            if (displayMonth != Integer.parseInt(keyArr[1])) {
                return;
            }

            String day = keyArr[2];


            FragmentManager fm = mContext.getSupportFragmentManager();
            Fragment frag = DayViewMainFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle args = new Bundle();
            args.putInt("DAY", Integer.parseInt(day));
            args.putInt("MONTH", displayMonth);
            args.putInt("YEAR", displayYearInt);
            CalendarWeatherApp.setHashMapAllPanchang1(mPanchangHashMap);
            CalendarWeatherApp.setHashMapAllFest(monthFestivalmap);

            frag.setArguments(args);
            ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_DAYINFO_TAG);

            ft.addToBackStack(AppConstants.FRAG_DAYINFO_TAG);
            ft.commit();


        });


    }


    public FrameLayout getLayout() {
        FrameLayout fl = new FrameLayout(mContext);
        fl.setLayoutParams(param);
        fl.setBackground(mContext.getResources().getDrawable(R.drawable.border));
        return fl;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_monthlist;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final MonthListFragment.MonthData obj = mItems.get(position);

        dayList = new ArrayList<>();
        keyList = new ArrayList<>();
        typeList = new ArrayList<>();

        mPanchangHashMap = new HashMap<>();
        monthFestivalmap = new HashMap<>();
        mPanchangHashMap.putAll(obj.mDayViewHashMap);
        monthFestivalmap.putAll(obj.monthFestivalmap);


        addTitle(holder);


        Calendar cal = Calendar.getInstance();
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


        setCalendarCellData(holder);


        try {


            if (!mPanchangHashMap.isEmpty()) {


                if (holder.week1Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week1Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }
                if (holder.week2Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week2Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }
                if (holder.week3Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week3Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }
                if (holder.week4Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week4Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }
                if (holder.week5Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week5Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }
                if (holder.week6Container.getChildCount() > 0) {
                    for (int i = 0; i < 7; i++) {
                        FrameLayout f1 = (FrameLayout) holder.week6Container.getChildAt(i);
                        setCellData(f1, holder);
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCellData(FrameLayout f1, ItemViewHolder holder) {

        try {

            String key = f1.getTag().toString().replace("_", "-");
            CoreDataHelper myCoreData = mPanchangHashMap.get(key);

            PanchangUtilityLighter.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData);
            int lunarMonthType = myPanchangObj.le_lunarMonthType;
            TextView t1 = (TextView) f1.getChildAt(3);
            String solarDay = myPanchangObj.le_solarDay;
            t1.setText("" + solarDay);

            TextView t2 = (TextView) f1.getChildAt(4);
            int currTithiIndex = myPanchangObj.le_currTithiIndex;
            String lunarDay = myPanchangObj.le_lunarDayPurimant;
            if (!mPref.getMyLanguage().contains("or")) {

                String tithiIndex = "";

                if (currTithiIndex > 14)
                    tithiIndex = "" + ((currTithiIndex - 15) + 1);
                else
                    tithiIndex = "" + (currTithiIndex + 1);
                tithiIndex = Utility.getInstance(mContext).getDayNo(tithiIndex);
                if (myPanchangObj.le_nextToNextTithiIndex != -1) {
                    if (myPanchangObj.le_nextTithiIndex > 14)
                        tithiIndex = tithiIndex + "," + Utility.getInstance(mContext).getDayNo("" + ((myPanchangObj.le_nextTithiIndex - 15) + 1));
                    else
                        tithiIndex = tithiIndex + "," + Utility.getInstance(mContext).getDayNo("" + (myPanchangObj.le_nextTithiIndex + 1));
                }

                t2.setText(tithiIndex);

            } else {

                t2.setText(lunarDay);
            }
            ImageView i2 = (ImageView) f1.getChildAt(2);
            ImageView i1 = (ImageView) f1.getChildAt(1);

            if (currTithiIndex == 29) {
                i2.setAlpha(1f);
                i2.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
            if (currTithiIndex == 14) {
                i2.setAlpha(1f);
                i2.setColorFilter(ContextCompat.getColor(mContext, R.color.sunMoon), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
            if (myPanchangObj.le_solarDayIndex == 1) {
                i1.setAlpha(1f);
                i1.setColorFilter(ContextCompat.getColor(mContext, R.color.yellowDark), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
            if (lunarMonthType == 1) {
                //adhika
                // i2.setAlpha(1f);
                // i2.setColorFilter(ContextCompat.getColor(activity, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);

                ((View) i2.getParent()).setAlpha(0.5f);

            }


            //  holder.header.setText(displayMonthInt+"xxxxx"+(myPanchangObj.monthIndex-1)+"::"+myPanchangObj.bara+"::"+myPanchangObj.solarMonth);
            if (displayMonthInt == myPanchangObj.le_monthIndex) {


                if (Integer.parseInt(myPanchangObj.le_day) == 1) {
                    String lgajapati = "";
                    if (!myPanchangObj.le_sanSalAnka.equals("0") && mPref.getMyLanguage().contains("or")) {

                        lgajapati = mType == 0 ? ", ଗଜପତ୍ୟାଙ୍କ-" + myPanchangObj.le_sanSalAnka : ", Gajapatyanka-" + myPanchangObj.le_sanSalAnka;
                    }
                    sakaddhaStart = myPanchangObj.le_sakaddha;
                    samvatStart = myPanchangObj.le_samvata;
                    sansalStart = myPanchangObj.le_sanSal;
                    monthHeaderStr = le_samvat + "-" + samvatStart + ", " + le_shakaddha + "-" + sakaddhaStart + ", " + le_sal + "-" + sansalStart + lgajapati + ", " + myPanchangObj.le_solarMonth + "" + myPanchangObj.le_solarDay + " " + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_paksha + " " + myPanchangObj.le_currTithi + ", " + myPanchangObj.le_bara + " " + le_time_tharu + " ";
                    holder.header.setVisibility(View.GONE);
                } else if (dayOfCurrMonth == Integer.parseInt(myPanchangObj.le_day) /*&& mPref.getMyLanguage().contains("or") */) {

                    sakaddhaEnd = myPanchangObj.le_sakaddha;
                    samvatEnd = myPanchangObj.le_samvata;
                    sansalEnd = myPanchangObj.le_sanSal;


                    monthHeaderStr += monthHeaderStr = myPanchangObj.le_solarMonth + "" + myPanchangObj.le_solarDay + " " + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_paksha + " " + myPanchangObj.le_currTithi + ", " + myPanchangObj.le_bara + " " + le_time_to;


                    holder.header.setVisibility(View.VISIBLE);
                    if (!sakaddhaStart.equalsIgnoreCase(sakaddhaEnd)) {
                        monthHeaderStr = monthHeaderStr.replace(sakaddhaStart, sakaddhaStart + "(" + sakaddhaEnd.substring(2) + ")");

                    }
                    if (!samvatStart.equalsIgnoreCase(samvatEnd)) {
                        monthHeaderStr = monthHeaderStr.replace(samvatStart, samvatStart + "(" + samvatEnd.substring(2) + ")");

                    }
                    if (!sansalStart.equalsIgnoreCase(sansalEnd)) {
                        monthHeaderStr = monthHeaderStr.replace(sansalStart, sansalStart + "(" + sansalEnd.substring(2) + ")");

                    }
                    holder.header.setText(monthHeaderStr);

                }


            }

            i2.setImageResource(moonImageArr[currTithiIndex]);

            if (myCoreData.getmMonth() == currMonthInt) {
                ArrayList<String> festObj = monthFestivalmap.get(key);
                if (festObj != null && festObj.size() > 0) {
                    String headerStr = myPanchangObj.le_month_short + " " + myPanchangObj.le_day + ", " + myPanchangObj.le_year + "(" + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + " " + le_dina + ")";
                    float alpha = myPanchangObj.le_lunarMonthType != 0 ? 0.5f : 1f;

                    addFestival(headerStr, 1, holder, alpha);

                    if (!mPref.getMyLanguage().contains("en") && !mPref.getMyLanguage().contains("kn")) {
                        addFestival(festObj.get(0), 0, holder, alpha);


                    }
                    addFestival(festObj.get(1), 0, holder, alpha);

                    addDivider(holder);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addDivider(ItemViewHolder holder) {
        View tv1 = new View(mContext);
        int dp = Utility.getInstance(mContext).dpToPx(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp);
        params.setMargins(dp * 5, dp * 5, dp * 5, dp * 5);
        tv1.setLayoutParams(params);

        TypedValue typedValue = new TypedValue();
        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[]{R.attr.divider});
        int color = a.getColor(0, 0);
        tv1.setBackgroundColor(color);
        holder.festivalContainer.addView(tv1);
    }

    private void addFestival(String str, int type, ItemViewHolder holder, float alpha) {
        int festivalGap = 0;
        TextView tv1 = new TextView(mContext);
        int dp = Utility.getInstance(mContext).dpToPx(2);
        festivalParam.setMargins(dp, dp, dp, dp);
        tv1.setLayoutParams(festivalParam);
        tv1.setText("" + str);
        if (type == 1) {
            tv1.setPadding(0, festivalGap, 0, festivalGap);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
        }
        tv1.setAlpha(alpha);
        holder.festivalContainer.addView(tv1);
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

        LinearLayout daysContainer, festivalContainer, week1Container, week2Container, week3Container, week4Container, week5Container, week6Container;
        // FrameLayout progressBar;
        TextView header;

        public ItemViewHolder(View rootView) {
            super(rootView);

            daysContainer = rootView.findViewById(R.id.daysContainer);
            week1Container = rootView.findViewById(R.id.week1Container);
            week2Container = rootView.findViewById(R.id.week2Container);
            week3Container = rootView.findViewById(R.id.week3Container);
            week4Container = rootView.findViewById(R.id.week4Container);
            week5Container = rootView.findViewById(R.id.week5Container);
            week6Container = rootView.findViewById(R.id.week6Container);
            festivalContainer = rootView.findViewById(R.id.festContainer);
            header = rootView.findViewById(R.id.header);

            //  progressBar = rootView.findViewById(R.id.progressCntr);


        }


    }

    public Integer[] getMoonImage() {

        Integer[] moonImgList = {R.drawable.wmoon_1, R.drawable.wmoon_2, R.drawable.wmoon_3, R.drawable.wmoon_4, R.drawable.wmoon_5, R.drawable.wmoon_6, R.drawable.wmoon_7, R.drawable.wmoon_8, R.drawable.wmoon_9, R.drawable.wmoon_10, R.drawable.wmoon_11, R.drawable.wmoon_12, R.drawable.wmoon_13, R.drawable.wmoon_14, R.drawable.wmoon_15, R.drawable.wmoon1, R.drawable.wmoon2, R.drawable.wmoon3, R.drawable.wmoon4, R.drawable.wmoon5, R.drawable.wmoon6, R.drawable.wmoon7, R.drawable.wmoon8, R.drawable.wmoon9, R.drawable.wmoon10, R.drawable.wmoon11, R.drawable.wmoon12, R.drawable.wmoon13, R.drawable.wmoon14, R.drawable.wmoon15};
        return moonImgList;
    }
}
