package com.iexamcenter.calendarweather.tools;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AppDateTimeDialog;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.panchang.PanchangUtility;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;

public class BirthAnniversaryFrag extends Fragment implements LocationDialog.LocationChangeEvents, AdapterView.OnItemSelectedListener {
    public boolean nextClicked = false, prevClicked = false;
    public static final String ARG_POSITION = "POSITION";
    int bYear, bMonth, bDate, bHour, bMin;
    int noDobMonthIndex, noDobRashiIndex, noDobNakshetraIndex, noDobEngMonthIndex,noDobTithiIndex;
    //int PAGE_TYPE;
    LinearLayout nodobcntr;
    CheckBox nodob;
    Spinner spinner1, spinner2;

    String lbl_place_txt, lbl_date_txt, lbl_spinner1Txt, lbl_spinner2Txt, nodobTxt, spinner1Prompt, spinner2Prompt;


    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView lbl_place, lbl_date, lbl_spinner1, lbl_spinner2;
    MaterialButton sbmt, next, prev, reminder;
    View view1;
    String titleval, subTitleVal;

    private MainActivity mContext;
    int calcType = 1;
    int anniType = 1;
    String anniName = "Birth";
    String pageTitle = "Vedic Birthday", subTitle = "Know when to celebrate";

    int selYear, selMonth, selDate, selHour, selMin;
    String[] le_arr_rasi_kundali;

    int solarDay, lunarDay, paksha, weekDay, solarMonth, lunarMonth, tithiKundali, nakshetraKundali;
    String[] le_arr_tithi, le_arr_nakshatra, le_arr_month, le_arr_bara, le_arr_paksha, le_arr_masa;
    String headerStr;
    String le_dina, le_paksha;
    String le_tithi, le_nakshetra, le_lunar_rashi;
    String latitude, longitude;
    int mDiffYear = 0;
    Calendar selCalPrevday;
    ArrayList<String> arrayList1 = new ArrayList<String>();
    ArrayList<String> arrayList2 = new ArrayList<String>();
    boolean considerPrevday = false;

    int year, month, dayOfmonth, hour24, min;
    String area;

    Resources res;
    int mType;
    int currDate, currMonth, currYear;
    TextView txt1, txt, city, latLng, date, time;
    LinearLayout placeCntr, dateCntr;
    public final String DATE_FORMAT_1 = "EEEE, dd MMM yyyy";
    public final String DATE_FORMAT_2 = "hh:mm a z";
    DialogFragment appLangDialog;

    public static BirthAnniversaryFrag newInstance() {
        BirthAnniversaryFrag myFragment = new BirthAnniversaryFrag();

       // Bundle args = new Bundle();
       // args.putInt("PAGE_TYPE", type);
       // myFragment.setArguments(args);

        return myFragment;

        //  return new AnniversaryFrag();
    }


    public void getMyResource() {
        res = mContext.getResources();
        if (mType == 0) {

            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.l_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.l_arr_nakshatra);
            le_arr_bara = mContext.getResources().getStringArray(R.array.l_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.l_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.l_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.l_paksha);
            le_dina = mContext.getResources().getString(R.string.l_dina);
            le_tithi = mContext.getResources().getString(R.string.l_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.l_nakshetra);
            le_lunar_rashi = mContext.getResources().getString(R.string.l_planet_chandra);

        } else {

            le_arr_rasi_kundali = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            le_arr_tithi = mContext.getResources().getStringArray(R.array.e_arr_tithi);
            le_arr_nakshatra = mContext.getResources().getStringArray(R.array.e_arr_nakshatra);
            le_arr_month = mContext.getResources().getStringArray(R.array.e_arr_month);
            le_arr_bara = mContext.getResources().getStringArray(R.array.e_arr_bara);
            le_arr_paksha = mContext.getResources().getStringArray(R.array.e_arr_paksha);
            le_arr_masa = mContext.getResources().getStringArray(R.array.e_arr_masa);
            le_paksha = mContext.getResources().getString(R.string.e_paksha);
            le_dina = mContext.getResources().getString(R.string.e_dina);
            le_tithi = mContext.getResources().getString(R.string.e_tithi);
            le_nakshetra = mContext.getResources().getString(R.string.e_nakshetra);
            le_lunar_rashi = mContext.getResources().getString(R.string.e_planet_chandra);

        }
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {


            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            latitude = mPref.getLatitude();
            longitude = mPref.getLongitude();
            area = mPref.getAreaAdmin();

            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
            getMyResource();

            Calendar cal = Calendar.getInstance();

            bDate = selDate = currDate = cal.get(Calendar.DAY_OF_MONTH);
            bMonth = selMonth = currMonth = cal.get(Calendar.MONTH);
            bYear = selYear = currYear = cal.get(Calendar.YEAR);

            bHour = selHour = cal.get(Calendar.HOUR_OF_DAY);
            bMin = selMin = cal.get(Calendar.MINUTE);
            setUp(rootView);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);
            viewModel.getDateTimePicker().observe(getViewLifecycleOwner(), picker -> {
                        String datePicker = viewModel.getDatePicker();
                        setval(datePicker, 1);
                        String timePicker = viewModel.getTimePicker();
                        setval(timePicker, 2);


                    }
            );
            viewModel.getLocationChanged().observe(getViewLifecycleOwner(), picker -> {
                        if (picker)
                            updateLatLng();
                    }
            );

            viewModel.getBirthPlace().observe(getViewLifecycleOwner(), picker -> {
                if (!picker.isEmpty()) {
                    String[] place = picker.split(",", 3);
                    latitude = place[0];
                    longitude = place[1];
                    area = place[2];
                    updateLatLng();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void updateLatLng() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String latVal = df.format(Double.parseDouble(latitude));
        String lngVal = df.format(Double.parseDouble(longitude));
        String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
        latLng.setText(latLngTxt);
        city.setText(area);
        city.setSelected(true);
    }

    protected void setUp(View rootView) {
        lbl_place = rootView.findViewById(R.id.lbl_place);
        lbl_date = rootView.findViewById(R.id.lbl_date);
        lbl_spinner1 = rootView.findViewById(R.id.lbl_spinner1);
        lbl_spinner2 = rootView.findViewById(R.id.lbl_spinner2);
        sbmt = rootView.findViewById(R.id.sbmt);
        reminder = rootView.findViewById(R.id.reminder);
        view1 = rootView.findViewById(R.id.view1);
        next = rootView.findViewById(R.id.next);
        prev = rootView.findViewById(R.id.prev);
        next.setVisibility(GONE);
        prev.setVisibility(GONE);
        view1.setVisibility(GONE);
        reminder.setVisibility(GONE);

        nodob = rootView.findViewById(R.id.noDateTime);
        nodobcntr = rootView.findViewById(R.id.noDateTimeSpinner);
        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner2 = rootView.findViewById(R.id.spinner2);
       String placeDateHelp = "Tap <BIRTH PLACE> & <BIRTH DATE> to change Birth Details";
        TextView placeDateHelpTV=rootView.findViewById(R.id.placeDateHelp);
        placeDateHelpTV.setText(placeDateHelp);

        lbl_spinner1.setText(lbl_spinner1Txt);
        lbl_spinner2.setText(lbl_spinner2Txt);
        lbl_place.setText(lbl_place_txt);
        lbl_date.setText(lbl_date_txt);
        nodob.setText(nodobTxt);
        spinner1.setPrompt(spinner1Prompt);
        spinner2.setPrompt(spinner2Prompt);

        int index = 0;
        int count = 0;


        arrayList1.clear();
        arrayList2.clear();

        if (anniType == 1 || anniType == 3) {
            for (int i = 0; i < le_arr_rasi_kundali.length; i++) {

                for (int j = index; j < (index + 3); j++) {
                    String rashi = le_arr_rasi_kundali[i];
                    String nakshetra = le_arr_nakshatra[j];
                    arrayList1.add(nakshetra + " - " + rashi);
                    arrayList2.add(j + "-" + i);
                    index++;
                    count++;
                    if (count == 3) {
                        count = 0;
                        if (i != 3 && i != 7) {
                            index--;
                        }
                        break;
                    }
                }
            }
        } else {

            for (int j = 0; j < le_arr_tithi.length; j++) {
                String pakshya;
                String tithi = le_arr_tithi[j];
                if (j < 15) {
                    pakshya = le_arr_paksha[0];
                    arrayList2.add(0 + "-" + j);
                } else {
                    pakshya = le_arr_paksha[1];
                    arrayList2.add(1 + "-" + j);
                }
                arrayList1.add(pakshya + " - " + tithi);
            }
        }


        ArrayAdapter adapter1 = new ArrayAdapter(mContext, android.R.layout.select_dialog_item, le_arr_masa);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);


        ArrayAdapter adapter2 = new ArrayAdapter(mContext, android.R.layout.select_dialog_item, arrayList1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(this);
        if (nodob.isChecked()) {
            nodobcntr.setVisibility(View.VISIBLE);
        } else {
            nodobcntr.setVisibility(GONE);
        }
        nodob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txt.setText("");
                txt1.setText("");
                next.setVisibility(GONE);
                prev.setVisibility(GONE);
                reminder.setVisibility(GONE);
                if (isChecked) {
                    nodobcntr.setVisibility(View.VISIBLE);
                } else {
                    nodobcntr.setVisibility(GONE);
                }
            }
        });


        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endTime, beginTime;

                DataCls obj= (DataCls) v.getTag();
                endTime = beginTime = obj.cal;

                try {
                    Intent intentEvent = new Intent(Intent.ACTION_INSERT)
                            .setType("vnd.android.cursor.item/event")
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, obj.title)
                            .putExtra(CalendarContract.Events.DESCRIPTION, obj.desc)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                            .putExtra(CalendarContract.Events.VISIBLE, false)
                            .putExtra(CalendarContract.Events.ALL_DAY, false)
                            .putExtra(CalendarContract.Reminders.METHOD, true)
                            .putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    startActivity(intentEvent);
                } catch (Exception e) {
                    Toast.makeText(mContext, "No Event Calendar Found..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        txt = rootView.findViewById(R.id.txt);
        txt1 = rootView.findViewById(R.id.txt1);
        city = rootView.findViewById(R.id.city);
        latLng = rootView.findViewById(R.id.latLng);
        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        placeCntr = rootView.findViewById(R.id.placeCntr);
        dateCntr = rootView.findViewById(R.id.dateCntr);
        sbmt.setOnClickListener(v -> {
            txt.setText("");
            txt1.setText("");
            if (nodob.isChecked()) {
                view1.setVisibility(GONE);
                int engMonth = noDobMonthIndex + 4;
                if (engMonth > 12) {
                    engMonth = engMonth - 12;
                }

                noDobEngMonthIndex = engMonth - 1;
                mNakshetraIndex = noDobNakshetraIndex + 1;
                mMoonSignIndex = noDobRashiIndex + 1;
                mTithiIndex = noDobTithiIndex + 1;

                mMonthIndex = noDobMonthIndex;
                selYear = currYear;
                selMonth = engMonth - 1;
                selDate = 1;
                mDiffYear = 0;
                observerPlanetInfo(selYear, selMonth, selDate, 1);


            } else {
                view1.setVisibility(View.VISIBLE);
                prev.setVisibility(GONE);
                next.setVisibility(View.VISIBLE);

                nextClicked = prevClicked = false;
                mDiffYear = currYear - selYear;

                if (mDiffYear >= 0) {
                    observerPlanetInfo(selYear, selMonth, selDate, 0);
                } else {
                    Toast.makeText(mContext, "Invalid date", Toast.LENGTH_LONG).show();
                }
            }

        });
        next.setOnClickListener(v -> {
            nextClicked = true;
            prevClicked = false;
            prev.setVisibility(View.VISIBLE);
            observerPlanetInfo(selYear + (++mDiffYear), selMonth, selDate, 1);
        });
        prev.setOnClickListener(v -> {

            next.setVisibility(View.VISIBLE);
            nextClicked = false;
            prevClicked = true;
            observerPlanetInfo(selYear + (--mDiffYear), selMonth, selDate, 1);
        });


        txt = rootView.findViewById(R.id.txt);


        updateLatLng();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        date.setText(getSetSelectedDateTime(1, cal));
        time.setText(getSetSelectedDateTime(2, cal));
        dateCntr.setOnClickListener(view -> openPicker(0));
        date.setOnClickListener(view -> openPicker(0));
        time.setOnClickListener(view -> openPicker(1));
        placeCntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // changelatlng();

                FragmentManager fm = mContext.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                frag = fm.findFragmentByTag("locationDialog");
                if (frag != null) {
                    ft.remove(frag);
                }
                LocationDialog shareDialog = LocationDialog.newInstance(mContext, 1);
                shareDialog.show(ft, "locationDialog");

                shareDialog.setLocationChangeEvents(BirthAnniversaryFrag.this);
            }
        });
    }

    public void openPicker(int page) {
        nodob.setChecked(false);
        txt.setText("");
        txt1.setText("");
        reminder.setVisibility(GONE);

        next.setVisibility(GONE);
        prev.setVisibility(GONE);

        FragmentTransaction ft0 = getChildFragmentManager().beginTransaction();
        Fragment prev0 = getChildFragmentManager().findFragmentByTag("MYDATETIME");
        if (prev0 != null) {
            ft0.remove(prev0);
        }
        appLangDialog = AppDateTimeDialog.newInstance(mContext);
        Bundle args = new Bundle();
        args.putInt(AppDateTimeDialog.ARG_POSITION, page);
        appLangDialog.setArguments(args);
        appLangDialog.setCancelable(true);
        appLangDialog.show(getChildFragmentManager(), "MYDATETIME");
    }

    public String getSetSelectedDateTime(int type, Calendar selCal) {
        year = selCal.get(Calendar.YEAR);
        month = selCal.get(Calendar.MONTH);
        dayOfmonth = selCal.get(Calendar.DAY_OF_MONTH);
        hour24 = selCal.get(Calendar.HOUR_OF_DAY);
        min = selCal.get(Calendar.MINUTE);
        SimpleDateFormat dateFormat;
        if (type == 1) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        }
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    public void setval(String picker, int type) {
        if (type == 1) {
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                selYear = year = Integer.parseInt(dateArr[0]);
                selMonth = month = Integer.parseInt(dateArr[1]);
                selDate = dayOfmonth = Integer.parseInt(dateArr[2]);
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);
            date.setText(getSetSelectedDateTime(1, cal));


        } else {
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                selHour = hour24 = Integer.parseInt(timeArr[0]);
                selMin = min = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);

            time.setText(getSetSelectedDateTime(2, cal));
        }

        bYear = year;
        bMonth = month;
        bDate = dayOfmonth;
        bHour = selHour;
        bMin = selMin;


    }

    public int mTithiIndex, mNakshetraIndex, mMonthIndex, mNakshetraKundali, mPakshya, mMoonSignIndex;

    public CoreDataHelper getCoreData(HashMap<String, CoreDataHelper> mAllPanchangHashMap, int selYear, int selMonth, int selDate, int selHour, int selMin, int type) {

        Iterator it = mAllPanchangHashMap.entrySet().iterator();

        CoreDataHelper retVal = null;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            CoreDataHelper myCoreData = (CoreDataHelper) pair.getValue();
            CoreDataHelper.Panchanga tithi = myCoreData.getTithi();
            CoreDataHelper.Panchanga nakshetra = myCoreData.getNakshetra();
            CoreDataHelper.Panchanga moonSign = myCoreData.getMoonSign();
            if (anniType == 1 || anniType == 3) {
                if ((mMoonSignIndex == moonSign.currVal || mMoonSignIndex == moonSign.nextVal) && (nakshetra.currVal == mNakshetraIndex) && (getHinduMonth(myCoreData) == mMonthIndex)) {
                    retVal = myCoreData;
                    break;
                } else if ((mMoonSignIndex == moonSign.currVal || mMoonSignIndex == moonSign.nextVal) && (nakshetra.nextVal == mNakshetraIndex && nakshetra.nextToNextVal != 0) && (getHinduMonth(myCoreData) == mMonthIndex)) {
                    retVal = myCoreData;
                    break;
                }
            } else {
                if ((tithi.currVal == mTithiIndex) && (getHinduMonth(myCoreData) == mMonthIndex)) {
                    retVal = myCoreData;
                    break;
                } else if ((tithi.currVal == mTithiIndex || tithi.nextVal == mTithiIndex) && (getHinduMonth(myCoreData) == mMonthIndex)) {
                    retVal = myCoreData;
                    break;
                }
            }

        }
        if (retVal == null) {
            it = mAllPanchangHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                CoreDataHelper myCoreData = (CoreDataHelper) pair.getValue();
                CoreDataHelper.Panchanga nakshetra = myCoreData.getNakshetra();
                CoreDataHelper.Panchanga tithi = myCoreData.getTithi();
                CoreDataHelper.Panchanga moonSign = myCoreData.getMoonSign();
                if (anniType == 1 || anniType == 3) {
                    if (((mMoonSignIndex == moonSign.currVal || mMoonSignIndex == moonSign.nextVal) && (nakshetra.currVal == mNakshetraIndex || nakshetra.nextVal == mNakshetraIndex || nakshetra.nextToNextVal == mNakshetraIndex) && (getHinduMonth(myCoreData) == mMonthIndex))) {
                        retVal = myCoreData;
                        break;
                    }
                } else {
                    if ((tithi.currVal == mTithiIndex || tithi.nextVal == mTithiIndex || tithi.nextToNextVal == mTithiIndex) && (getHinduMonth(myCoreData) == mMonthIndex)) {
                        retVal = myCoreData;
                        break;
                    }
                }
            }
        }
        return retVal;


    }

    public void setCalendarData(HashMap<String, CoreDataHelper> mAllPanchangHashMap, int selYear, int selMonth, int selDate, int selHour, int selMin, int type) {
        try {

            if (!mAllPanchangHashMap.isEmpty()) {
                CoreDataHelper myCoreData;
                if (type == 0) {
                    Calendar selCal = Calendar.getInstance();
                    selCal.set(Calendar.YEAR, selYear);
                    selCal.set(Calendar.MONTH, selMonth);
                    selCal.set(Calendar.DAY_OF_MONTH, selDate);
                    selCal.set(Calendar.HOUR_OF_DAY, selHour);
                    selCal.set(Calendar.MINUTE, selMin);
                    selCal.set(Calendar.SECOND, 0);
                    selCal.set(Calendar.MILLISECOND, 0);
                    Utility.SunDetails sun = Utility.getInstance(mContext).getTodaySunDetails(selCal, true);
                    String[] HrMinArr = sun.sunRise.split(" ")[0].split(":");
                    Calendar selCalSR = Calendar.getInstance();
                    selCalSR.set(Calendar.YEAR, selYear);
                    selCalSR.set(Calendar.MONTH, selMonth);
                    selCalSR.set(Calendar.DAY_OF_MONTH, selDate);
                    selCalSR.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HrMinArr[0]));
                    selCalSR.set(Calendar.MINUTE, Integer.parseInt(HrMinArr[1]));
                    selCalSR.set(Calendar.SECOND, Integer.parseInt(HrMinArr[2]));
                    selCalSR.set(Calendar.MILLISECOND, 0);

                    if (selCal.getTimeInMillis() < selCalSR.getTimeInMillis()) {
                        considerPrevday = true;
                        selCalPrevday = Calendar.getInstance();
                        selCalPrevday.setTimeInMillis(selCal.getTimeInMillis() - 24 * 60 * 60 * 1000);
                        int selYear1 = selCalPrevday.get(Calendar.YEAR);
                        int selMonth1 = selCalPrevday.get(Calendar.MONTH);
                        int selDate1 = selCalPrevday.get(Calendar.DAY_OF_MONTH);
                        String key1 = selYear1 + "-" + selMonth1 + "-" + selDate1;
                        myCoreData = mAllPanchangHashMap.get(key1);

                    } else {
                        considerPrevday = false;
                        String key = selYear + "-" + selMonth + "-" + selDate;
                        myCoreData = mAllPanchangHashMap.get(key);
                    }

                    CoreDataHelper.Panchanga tithi = myCoreData.getTithi();
                    if (selCal.getTimeInMillis() <= tithi.currValEndTime.getTimeInMillis()) {
                        tithiKundali = tithi.currVal;
                    } else if (selCal.getTimeInMillis() <= tithi.le_nextValEndTime.getTimeInMillis()) {
                        tithiKundali = tithi.nextVal;
                    } else {
                        tithiKundali = tithi.nextToNextVal;
                    }

                    CoreDataHelper.Panchanga nakshetra = myCoreData.getNakshetra();
                    if (selCal.getTimeInMillis() <= nakshetra.currValEndTime.getTimeInMillis()) {
                        nakshetraKundali = nakshetra.currVal;
                    } else if (selCal.getTimeInMillis() <= nakshetra.le_nextValEndTime.getTimeInMillis()) {
                        nakshetraKundali = nakshetra.nextVal;
                    } else {
                        nakshetraKundali = nakshetra.nextToNextVal;

                    }
                    CoreDataHelper.Panchanga moonSign = myCoreData.getMoonSign();
                    if (selCal.getTimeInMillis() <= moonSign.currValEndTime.getTimeInMillis()) {
                        mMoonSignIndex = moonSign.currVal;
                    } else if (selCal.getTimeInMillis() <= moonSign.le_nextValEndTime.getTimeInMillis()) {
                        mMoonSignIndex = moonSign.nextVal;
                    } else {
                        mMoonSignIndex = moonSign.nextToNextVal;

                    }


                    mTithiIndex = tithiKundali;
                    mNakshetraIndex = nakshetraKundali;
                    mPakshya = myCoreData.getPaksha();
                    lunarMonth = mMonthIndex = getHinduMonth(myCoreData);


                } else {
                    myCoreData = getCoreData(mAllPanchangHashMap, selYear, selMonth, selDate, selHour, selMin, type);
                }
                if (nodob.isChecked()) {
                    tithiKundali = mTithiIndex = myCoreData.getTithi().currVal;
                    paksha = mPakshya = myCoreData.getPaksha();
                    lunarMonth = mMonthIndex;


                    mNakshetraKundali = nakshetraKundali = mNakshetraIndex;
                } else {
                    tithiKundali = mTithiIndex;
                    mNakshetraKundali = mNakshetraIndex;
                    paksha = mPakshya;
                    lunarMonth = mMonthIndex;
                }


                weekDay = myCoreData.getDayOfWeek();
                solarMonth = myCoreData.getAdjustSolarMonth();
                solarDay = myCoreData.getSolarDayVal();
                lunarDay = myCoreData.getFullMoonLunarDay();


                PanchangUtility panchangUtility = new PanchangUtility();
                PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), latitude, longitude, mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());

//private String getShraddha(CoreDataHelper.Panchanga tithiObj, Calendar sunSetCal, Calendar sunNoonCal)
                String tithiVal = le_arr_tithi[tithiKundali - 1] + " " + le_tithi;

                String nakshetraVal = le_arr_nakshatra[nakshetraKundali - 1] + " " + le_nakshetra;
                String moonSignVal = le_arr_rasi_kundali[mMoonSignIndex - 1] + " " + le_lunar_rashi;

                headerStr = myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + "-" + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + "," + tithiVal + "," + nakshetraVal + "," + moonSignVal;
                Calendar calculatedBirthCal = Calendar.getInstance(Locale.ENGLISH);
                calculatedBirthCal.set(Calendar.YEAR, myCoreData.getmYear());
                calculatedBirthCal.set(Calendar.MONTH, myCoreData.getmMonth());
                calculatedBirthCal.set(Calendar.DAY_OF_MONTH, myCoreData.getmDay());
                Calendar actualBirthCal = Calendar.getInstance(Locale.ENGLISH);
                actualBirthCal.set(Calendar.YEAR, bYear);
                actualBirthCal.set(Calendar.MONTH, bMonth);
                actualBirthCal.set(Calendar.DAY_OF_MONTH, bDate);
                actualBirthCal.set(Calendar.HOUR_OF_DAY, bHour);
                actualBirthCal.set(Calendar.MINUTE, bMin);
                long days = 0;

                if (nodob.isChecked()) {
                    SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM, YYYY", Locale.ENGLISH);
                    String formatted = format1.format(calculatedBirthCal.getTime());
                    String txt2Str = anniName + " Anniversary Date : " + formatted + "\n" + headerStr;
                    txt1.setGravity(Gravity.CENTER);
                    DataCls obj= new DataCls();
                    obj.cal=calculatedBirthCal;
                    obj.desc= txt2Str;
                    obj.title="Birthday Reminder";
                    reminder.setTag(obj);
                    reminder.setVisibility(View.VISIBLE);
                    txt1.setText(txt2Str);
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);
                } else {
                    days = ((calculatedBirthCal.getTimeInMillis() > actualBirthCal.getTimeInMillis()) ? (calculatedBirthCal.getTimeInMillis() - actualBirthCal.getTimeInMillis()) : (actualBirthCal.getTimeInMillis() - calculatedBirthCal.getTimeInMillis())) / (24 * 60 * 60 * 1000L);
                    SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM, YYYY", Locale.ENGLISH);
                    String formatted = format1.format(actualBirthCal.getTime());
                    String txt1Str = anniName + " Date : " + formatted + "\n" + headerStr;
                    formatted = format1.format(calculatedBirthCal.getTime());
                    String txt2Str =  anniName + " Anniversary Date : " + formatted + "\n" + headerStr;
                    txt.setGravity(Gravity.CENTER);
                    txt1.setGravity(Gravity.CENTER);
                    DataCls obj= new DataCls();
                    obj.cal=calculatedBirthCal;
                    obj.desc= txt1Str+"\n\n"+txt2Str;
                    obj.title="Birthday Reminder for "+formatted;
                    reminder.setTag(obj);

                   // reminder.setTag(calculatedBirthCal);
                    reminder.setVisibility(View.VISIBLE);
                    if (type == 0) {
                        txt.setText(txt1Str);
                        observerPlanetInfo(selYear + mDiffYear, selMonth, selDate, 1);
                        view1.setVisibility(View.VISIBLE);
                    } else if (type == 1 && (actualBirthCal.getTimeInMillis() > calculatedBirthCal.getTimeInMillis())) {
                        observerPlanetInfo((selYear + (++mDiffYear)), selMonth, selDate, 1);
                    } else if (type == 1 && days < 300) {
                        observerPlanetInfo((selYear + (++mDiffYear)), selMonth, selDate, 1);
                    }
                    txt1.setText(txt2Str);

                }


                if (myCoreData.getmYear() > 2050) {
                    Toast.makeText(mContext, "Invalid", Toast.LENGTH_LONG).show();

                    next.setVisibility(GONE);
                    prev.setVisibility(View.VISIBLE);
                } else if (myCoreData.getmYear() < 1970) {

                    Toast.makeText(mContext, "Invalid", Toast.LENGTH_LONG).show();

                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(GONE);

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.fragment_tool_birth_day, null);
        setRetainInstance(true);
        setHasOptionsMenu(true);
       // PAGE_TYPE = getArguments().getInt("PAGE_TYPE", 0);

                calcType = 1;
                anniName = "Birth";
                anniType = 1;
                pageTitle = "Vedic Birthday";
                subTitle = "Know when to celebrate";
                lbl_spinner1Txt = "Select Birth Month";
                lbl_spinner2Txt = "Select Birth Nakshetra-Rashi";
                nodobTxt = "Check here, If you don't know birth place, date & time";
                spinner1Prompt = "LUNAR MONTH";
                spinner2Prompt = "NAKSHETRA-RASHI";
                lbl_place_txt = "BIRTH PLACE";
                lbl_date_txt = "BIRTH DATE";

        titleval = mContext.getSupportActionBar().getTitle().toString();
        subTitleVal = mContext.getSupportActionBar().getSubtitle().toString();

        mContext.getSupportActionBar().setTitle(pageTitle);
        mContext.getSupportActionBar().setSubtitle(subTitle);
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
        mContext.getSupportActionBar().setTitle(titleval);
        mContext.getSupportActionBar().setSubtitle(subTitleVal);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void observerPlanetInfo(int selYear1, int selMonth1, int selDate1, int type) {

        Calendar selCal1 = Calendar.getInstance();
        selCal1.set(Calendar.YEAR, selYear1 + 100);
        selCal1.set(Calendar.MONTH, selMonth1);
        selCal1.set(Calendar.DAY_OF_MONTH, selDate1);
        selCal1.set(Calendar.HOUR_OF_DAY, 0);
        selCal1.set(Calendar.MINUTE, 0);

        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 3).removeObservers(this);
        viewModel.getEphemerisData(selCal1.getTimeInMillis(), 3).observe(getViewLifecycleOwner(), obj -> {

            if (obj != null && obj.size() != 0) {
                PanchangTask ptObj = new PanchangTask();
                HashMap<String, CoreDataHelper> myPanchangHashMap = ptObj.panchangMapping(obj, mPref.getMyLanguage(), latitude, longitude, mContext);
                setCalendarData(myPanchangHashMap, selYear1, selMonth1, selDate1, selHour, selMin, type);
            }

        });

    }


    @Override
    public void onLocationChange(String val, int type) {
        String[] tmp = val.split(" ", 4);

        latitude = String.valueOf(tmp[1]);
        longitude = String.valueOf(tmp[2]);
        area = tmp[3];
        updateLatLng();
        viewModel.setBirthPlace(latitude + "," + longitude + "," + area);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner1:
                noDobMonthIndex = position;
                break;
            case R.id.spinner2:
                if(anniType==1 || anniType==3) {
                    noDobNakshetraIndex = Integer.parseInt(arrayList2.get(position).split("-")[0]);
                    noDobRashiIndex = Integer.parseInt(arrayList2.get(position).split("-")[1]);
                }else{
                    noDobTithiIndex = Integer.parseInt(arrayList2.get(position).split("-")[1]);
                }

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private String le_shraddha;
    private String le_shraddha_parban;
    private String le_shraddha_eko;
    private String le_shraddha_ra;
    private String prevShraddha = "";
    private String getShraddha(CoreDataHelper.Panchanga tithiObj, Calendar sunSetCal, Calendar sunNoonCal) {

        String efullTmp = "";
        String eko = "", par = "", full = "";
        String eeko = "", epar = "", efull = "";

        try {

            if (tithiObj.currValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 + (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];

                efull = le_arr_tithi[tithiObj.currVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 24 * 60 * 60 * 1000 - (15 * 60 * 1000) /* 15 min buffer as next tithi end timing is not accurate*/)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000) && tithiObj.le_nextValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + (24 + 1.5) * 60 * 60 * 1000)) {
                full = le_arr_tithi[tithiObj.currVal - 1];
                efull = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];
            } else if (tithiObj.currValEndTime.getTimeInMillis() > sunNoonCal.getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() + 1.5 * 60 * 60 * 1000)) {
                eko = le_arr_tithi[tithiObj.currVal - 1];
                eeko = le_arr_tithi[tithiObj.currVal - 1];
                par = le_arr_tithi[tithiObj.nextVal - 1];
                epar = le_arr_tithi[tithiObj.nextVal - 1];


            } else if (tithiObj.currValEndTime.getTimeInMillis() < sunNoonCal.getTimeInMillis() && tithiObj.le_nextValEndTime.getTimeInMillis() > sunSetCal.getTimeInMillis()) {
                efullTmp = le_arr_tithi[tithiObj.currVal - 1];
                if (prevShraddha.contains(efullTmp)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    efull = le_arr_tithi[tithiObj.nextVal - 1];

                } else if (tithiObj.currValEndTime.getTimeInMillis() < (sunNoonCal.getTimeInMillis() - 3 * 60 * 60 * 1000)) {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    efull = le_arr_tithi[tithiObj.nextVal - 1];
                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 1.5 * 60 * 60 * 1000) {
                    eko = le_arr_tithi[tithiObj.currVal - 1];
                    eeko = le_arr_tithi[tithiObj.currVal - 1];

                    par = le_arr_tithi[tithiObj.nextVal - 1];
                    epar = le_arr_tithi[tithiObj.nextVal - 1];

                } else if ((sunNoonCal.getTimeInMillis() - tithiObj.currValEndTime.getTimeInMillis()) < 2 * 60 * 60 * 1000) {

                    par = le_arr_tithi[tithiObj.nextVal - 1];
                    epar = le_arr_tithi[tithiObj.nextVal - 1];

                } else {
                    full = le_arr_tithi[tithiObj.nextVal - 1];
                    eko = le_arr_tithi[tithiObj.currVal - 1];

                    efull = le_arr_tithi[tithiObj.nextVal - 1];
                    eeko = le_arr_tithi[tithiObj.currVal - 1];
                }

            }


            prevShraddha = efullTmp;


        } catch (Exception e) {
            e.printStackTrace();
            if (!full.isEmpty()) {
                full = full + " " + le_shraddha_ra + " " + le_shraddha;
            }
            if (!eko.isEmpty()) {
                eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
            }
            if (!par.isEmpty()) {
                par = par + " " + le_shraddha_ra + " " + le_shraddha_parban;
            }

            if (!efull.isEmpty()) {
                efull = efull + " " + le_shraddha_ra + " " + le_shraddha;
            }
            if (!eko.isEmpty()) {
                eeko = eeko + " " + le_shraddha_ra + " " + le_shraddha_eko;
            }
            if (!epar.isEmpty()) {
                epar = epar + " " + le_shraddha_ra + " " + le_shraddha_parban;
            }
            return eko + " " + par + " " + full + "__" + eeko + " " + epar + " " + efull;

        }

        if (!full.isEmpty()) {
            full = full + " " + le_shraddha_ra + " " + le_shraddha;
        }
        if (!eko.isEmpty()) {
            eko = eko + " " + le_shraddha_ra + " " + le_shraddha_eko;
        }
        if (!par.isEmpty()) {
            par = par + " " + le_shraddha_ra + " " + le_shraddha_parban;
        }

        if (!efull.isEmpty()) {
            efull = efull + " " + le_shraddha_ra + " " + le_shraddha;
        }
        if (!eko.isEmpty()) {
            eeko = eeko + " " + le_shraddha_ra + " " + le_shraddha_eko;
        }
        if (!epar.isEmpty()) {
            epar = epar + " " + le_shraddha_ra + " " + le_shraddha_parban;
        }


        return eko + " " + par + " " + full + "__" + eeko + " " + epar + " " + efull;
    }


    public int getHinduMonth(CoreDataHelper myCoreData){
        String lang=mPref.getMyLanguage();
        if(lang.contains("bn") || lang.contains("ta") || lang.contains("ml") ||lang.contains("pa"))
            return myCoreData.getAdjustSolarMonth();
        else  if(lang.contains("te") || lang.contains("mr")  || lang.contains("kn") || lang.contains("gu")){
            return myCoreData.getLunarMonthAmantIndex();
        }else if(lang.contains("hi") || lang.contains("or") || lang.contains("en")){
            return myCoreData.getLunarMonthPurnimantIndex();
        }else{
            return myCoreData.getLunarMonthPurnimantIndex();
        }
    }

public static class DataCls{
        public Calendar cal;
        public String desc;
        public String title;
}
}

