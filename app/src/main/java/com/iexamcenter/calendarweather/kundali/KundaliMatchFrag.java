package com.iexamcenter.calendarweather.kundali;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.AppDateTimeDialog;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class KundaliMatchFrag extends Fragment implements LocationDialog.LocationChangeEvents {

    public static final String ARG_POSITION = "POSITION";
    MainActivity activity;
    Resources res;
    TextView city1, latLng1, date1, time1;
    LinearLayout placeCntr1, dateCntr1;
    TextView city2, latLng2, date2, time2;
    LinearLayout placeCntr2, dateCntr2;
    PrefManager mPref;
    public final String DATE_FORMAT_1 = "EEEE, dd MMM yyyy";
    public final String DATE_FORMAT_2 = "hh:mm a z";
    MainViewModel viewModel;
    DialogFragment appLangDialog;
    int year1, month1, dayOfmonth1, hour1, min1;
    String latitude1, longitude1, area1;
    int year2, month2, dayOfmonth2, hour2, min2;
    String latitude2, longitude2, area2;
    MaterialButton submit;

    public static KundaliMatchFrag newInstance() {

        return new KundaliMatchFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.frag_kundali_match, null);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        latitude1 = mPref.getLatitude();
        longitude1 = mPref.getLongitude();
        area1 = mPref.getAreaAdmin();
        latitude2 = mPref.getLatitude();
        longitude2 = mPref.getLongitude();
        area2 = mPref.getAreaAdmin();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        res = activity != null ? activity.getResources() : null;

        submit = rootView.findViewById(R.id.submit);
        city1 = rootView.findViewById(R.id.city1);
        latLng1 = rootView.findViewById(R.id.latLng1);
        date1 = rootView.findViewById(R.id.date1);
        time1 = rootView.findViewById(R.id.time1);
        placeCntr1 = rootView.findViewById(R.id.placeCntr1);
        dateCntr1 = rootView.findViewById(R.id.dateCntr1);
        date1.setText(getSetSelectedDateTime1(1, cal));
        time1.setText(getSetSelectedDateTime1(2, cal));
        dateCntr1.setOnClickListener(view -> openPicker(100));
        date1.setOnClickListener(view -> openPicker(100));
        time1.setOnClickListener(view -> openPicker(101));
        placeCntr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  changelatlng1();
                openLocationDialog(1);
            }
        });
        updateLatLng1();

        city2 = rootView.findViewById(R.id.city2);
        latLng2 = rootView.findViewById(R.id.latLng2);
        date2 = rootView.findViewById(R.id.date2);
        time2 = rootView.findViewById(R.id.time2);
        placeCntr2 = rootView.findViewById(R.id.placeCntr2);
        dateCntr2 = rootView.findViewById(R.id.dateCntr2);
        date2.setText(getSetSelectedDateTime2(1, cal));
        time2.setText(getSetSelectedDateTime2(2, cal));
        dateCntr2.setOnClickListener(view -> openPicker(200));
        date2.setOnClickListener(view -> openPicker(200));
        time2.setOnClickListener(view -> openPicker(201));
        placeCntr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // changelatlng2();
                openLocationDialog(2);
            }
        });

        updateLatLng2();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = activity.getSupportFragmentManager();
                Fragment frag = KundaliMatchCalcFrag.newInstance();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle args = new Bundle();
                args.putString("area1", area1);
                args.putString("area2", area2);
                args.putString("latitude1", latitude1);
                args.putString("longitude1", longitude1);
                args.putString("latitude2", latitude2);
                args.putString("longitude2", longitude2);
                args.putInt("year1", year1);
                args.putInt("year2", year2);
                args.putInt("month1", month1);
                args.putInt("month2", month2);
                args.putInt("dayOfmonth1", dayOfmonth1);
                args.putInt("dayOfmonth2", dayOfmonth2);
                args.putInt("hour1", hour1);
                args.putInt("hour2", hour2);
                args.putInt("min1", min1);
                args.putInt("min2", min2);


                frag.setArguments(args);


                ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_KUNDALI_MATCH_CALC_TAG);

                ft.addToBackStack(AppConstants.FRAG_KUNDALI_MATCH_CALC_TAG);
                ft.commit();
            }
        });
        viewModel.getDateTimePicker1().observe(this, picker -> {

                    String datePicker = viewModel.getDatePicker1();
                    setval1(datePicker, 1);
                    String timePicker = viewModel.getTimePicker1();
                    setval1(timePicker, 2);
                }
        );
        viewModel.getDateTimePicker2().observe(this, picker -> {
                    Log.e("mType", "mTypemType:6time:");
                    String datePicker = viewModel.getDatePicker2();
                    setval2(datePicker, 1);
                    String timePicker = viewModel.getTimePicker2();
                    setval2(timePicker, 2);
                }
        );
        viewModel.getLocationChanged().observe(this, picker -> {
                    if (picker) {
                        updateLatLng1();
                        updateLatLng2();
                    }
                }
        );


        return rootView;

    }

    public String getSetSelectedDateTime1(int type, Calendar selCal) {
        year1 = selCal.get(Calendar.YEAR);
        month1 = selCal.get(Calendar.MONTH);
        dayOfmonth1 = selCal.get(Calendar.DAY_OF_MONTH);
        hour1 = selCal.get(Calendar.HOUR_OF_DAY);
        min1 = selCal.get(Calendar.MINUTE);

        SimpleDateFormat dateFormat;
        if (type == 1) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        }
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    public String getSetSelectedDateTime2(int type, Calendar selCal) {
        year2 = selCal.get(Calendar.YEAR);
        month2 = selCal.get(Calendar.MONTH);
        dayOfmonth2 = selCal.get(Calendar.DAY_OF_MONTH);
        hour2 = selCal.get(Calendar.HOUR_OF_DAY);
        min2 = selCal.get(Calendar.MINUTE);

        SimpleDateFormat dateFormat;
        if (type == 1) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH);
        }
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    public void openPicker(int page) {

        FragmentTransaction ft0 = getChildFragmentManager().beginTransaction();
        Fragment prev0 = getChildFragmentManager().findFragmentByTag("MYDATETIME");
        if (prev0 != null) {
            ft0.remove(prev0);
        }
        appLangDialog = AppDateTimeDialog.newInstance(activity);
        Bundle args = new Bundle();
        args.putInt(AppDateTimeDialog.ARG_POSITION, page);
        appLangDialog.setArguments(args);
        appLangDialog.setCancelable(true);

        appLangDialog.show(getChildFragmentManager(), "MYDATETIME");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {



    }

    public void openLocationDialog(int type) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment frag;
        FragmentTransaction ft = fm.beginTransaction();
        frag = fm.findFragmentByTag("locationDialog");
        if (frag != null) {
            ft.remove(frag);
        }
        LocationDialog shareDialog = LocationDialog.newInstance(activity, type);
        shareDialog.show(ft, "locationDialog");

        shareDialog.setLocationChangeEvents(KundaliMatchFrag.this);
    }




    public void updateLatLng1() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String latVal = df.format(Double.parseDouble(latitude1));
        String lngVal = df.format(Double.parseDouble(longitude1));
        Log.e("lngVal", longitude1 + "::lngVal::" + lngVal);
        String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
        latLng1.setText(latLngTxt);
        city1.setText(area1);
        city1.setSelected(true);
    }

    public void updateLatLng2() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String latVal = df.format(Double.parseDouble(latitude2));
        String lngVal = df.format(Double.parseDouble(longitude2));
        Log.e("lngVal", longitude2 + "::lngVal::" + lngVal);
        String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
        latLng2.setText(latLngTxt);
        city2.setText(area2);
        city2.setSelected(true);
    }

    public void setval1(String picker, int type) {
        if (type == 1) {
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                year1 = Integer.parseInt(dateArr[0]);
                month1 = Integer.parseInt(dateArr[1]);
                dayOfmonth1 = Integer.parseInt(dateArr[2]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1, dayOfmonth1, hour1, min1);

            date1.setText(getSetSelectedDateTime1(1, cal));
        } else {
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                hour1 = Integer.parseInt(timeArr[0]);
                min1 = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1, dayOfmonth1, hour1, min1);

            time1.setText(getSetSelectedDateTime1(2, cal));
        }

    }

    public void setval2(String picker, int type) {
        if (type == 1) {
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                year2 = Integer.parseInt(dateArr[0]);
                month2 = Integer.parseInt(dateArr[1]);
                dayOfmonth2 = Integer.parseInt(dateArr[2]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year2, month2, dayOfmonth2, hour2, min2);

            date2.setText(getSetSelectedDateTime2(1, cal));
        } else {
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                hour2 = Integer.parseInt(timeArr[0]);
                min2 = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year2, month2, dayOfmonth2, hour2, min2);

            time2.setText(getSetSelectedDateTime2(2, cal));
        }

    }

    @Override
    public void onLocationChange(String val, int type) {
        String[] tmp = val.split(" ", 4);
        if (type == 1) {
            latitude1 = String.valueOf(tmp[1]);
            longitude1 = String.valueOf(tmp[2]);
            area1 = tmp[3];
            updateLatLng1();
            viewModel.setBirthPlace1(latitude1 + "," + longitude1);
        } else {
            latitude2 = String.valueOf(tmp[1]);
            longitude2 = String.valueOf(tmp[2]);
            area2 = tmp[3];
            updateLatLng2();

            viewModel.setBirthPlace2(latitude2 + "," + longitude2);
        }

    }
}

