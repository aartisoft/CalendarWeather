package com.iexamcenter.calendarweather.kundali;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.tabs.TabLayout;
import com.iexamcenter.calendarweather.AppDateTimeDialog;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.LocationDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * ChoghadiaMainFragment
 */

public class KundaliMainFragment extends Fragment implements LocationDialog.LocationChangeEvents {
    public static final String ARG_POSITION = "POSITION";
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    TextView city, latLng, date, time;
    LinearLayout placeCntr, dateCntr;
    PrefManager mPref;
    public final String DATE_FORMAT_1 = "EEEE, dd MMM yyyy";
    public final String DATE_FORMAT_2 = "hh:mm a z";
    MainViewModel viewModel;
    DialogFragment appLangDialog;
    int year, month, dayOfmonth, hour24, min;
    String latitude, longitude, area;
    ViewPager tabViewPager;
    KundaliPagerAdapter adapter;

    public static KundaliMainFragment newInstance() {

        return new KundaliMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  activity.showHideBottomNavigationView(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_kundali_main, container, false);
        viewModel =new ViewModelProvider(this).get(MainViewModel.class);
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        latitude = mPref.getLatitude();
        longitude = mPref.getLongitude();
        area = mPref.getAreaAdmin();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        res = activity != null ? activity.getResources() : null;

        setUp(rootView);
        // setObserver();
        viewModel.getDateTimePicker().observe(getViewLifecycleOwner(), picker -> {
                    Log.e("picker", "pick:erpicker:" + picker);
                    String datePicker = viewModel.getDatePicker();
                    Log.e("picker", "pick:erpicker:datePicker" + datePicker);
                    setval(datePicker, 1);
                    String timePicker = viewModel.getTimePicker();
                    Log.e("picker", "pick:erpicker:timePicker" + timePicker);
                    setval(timePicker, 2);
                    adapter.notifyDataSetChanged();
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

        return rootView;
    }

    public void setval(String picker, int type) {
        if (type == 1) {
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                year = Integer.parseInt(dateArr[0]);
                month = Integer.parseInt(dateArr[1]);
                dayOfmonth = Integer.parseInt(dateArr[2]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);

            date.setText(getSetSelectedDateTime(1, cal));
        } else {
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                hour24 = Integer.parseInt(timeArr[0]);
                min = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfmonth, hour24, min);

            time.setText(getSetSelectedDateTime(2, cal));
        }

    }

    public void setObserver() {

       /* viewModel.getDatePicker().observe(this, picker -> {
            Log.e("getDatePicker", "DATEPicker:KundaliMAINFrag" + picker);
            if (!picker.isEmpty() && picker.contains("-")) {
                String[] dateArr = picker.split("-");
                year1 = Integer.parseInt(dateArr[0]);
                month1 = Integer.parseInt(dateArr[1]);
                dayOfmonth1 = Integer.parseInt(dateArr[2]);
                Log.e("DATEE", "Dateee:D:" + picker);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1, dayOfmonth1, hour1, min1);
            date1.setText(getSetSelectedDateTime1(1, cal));

            // if (appLangDialog != null)
            //   appLangDialog.dismiss();
            viewModel.getDatePicker().removeObservers(this);
        });


        viewModel.getTimePicker().observe(this, picker -> {
            Log.e("getDatePicker", "TIMEPicker:KundaliMAINFrag" + picker);
            if (!picker.isEmpty() && picker.contains(":")) {
                String[] timeArr = picker.split(":");
                hour1 = Integer.parseInt(timeArr[0]);
                min1 = Integer.parseInt(timeArr[1]);

            }
            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1, dayOfmonth1, hour1, min1);

            time1.setText(getSetSelectedDateTime1(2, cal));
            //  if (appLangDialog != null)
            //    appLangDialog.dismiss();
            viewModel.getTimePicker().removeObservers(this);
        });*/
    }

    public void updateLatLng() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        String latVal = df.format(Double.parseDouble(latitude));
        String lngVal = df.format(Double.parseDouble(longitude));
        Log.e("lngVal", longitude + "::lngVal::" + lngVal);
        String latLngTxt = "Lat:" + latVal + " Lng:" + lngVal;
        latLng.setText(latLngTxt);
        city.setText(area);
        city.setSelected(true);
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    protected void setUp(View rootView) {

        city = rootView.findViewById(R.id.city);
        latLng = rootView.findViewById(R.id.latLng);
        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        placeCntr = rootView.findViewById(R.id.placeCntr);
        dateCntr = rootView.findViewById(R.id.dateCntr);

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

                FragmentManager fm = activity.getSupportFragmentManager();
                Fragment frag;
                FragmentTransaction ft = fm.beginTransaction();
                frag = fm.findFragmentByTag("locationDialog");
                if (frag != null) {
                    ft.remove(frag);
                }
                LocationDialog shareDialog = LocationDialog.newInstance(activity,1);
                shareDialog.show(ft, "locationDialog");

                shareDialog.setLocationChangeEvents(KundaliMainFragment.this);
            }
        });

        tabViewPager = rootView.findViewById(R.id.tabViewPager);
        adapter = KundaliPagerAdapter.newInstance(getChildFragmentManager(), activity);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(adapter);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == Constant.REQUEST_CODE_BIRTH_LOCATION_PICKER_MAP && resultCode == Activity.RESULT_OK) {
            String[] retVal = Helper.getInstance().getAddressLatLon(data, activity);
            if (retVal != null && retVal.length > 2) {
                latitude = String.valueOf(retVal[0]);
                longitude = String.valueOf(retVal[1]);
                area = retVal[2];
                updateLatLng();
                Log.e("observerPlanetInfo", "observerPlanetInfo::::" + latitude + "::" + longitude);
                viewModel.setBirthPlace(latitude + "," + longitude + "," + area);
            }

        }*/

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
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = selCal.getTime();
        return dateFormat.format(today);
    }

    @Override
    public void onLocationChange(String val,int type) {
        System.out.println("::LOCATIONVAL::"+val);
        String[] tmp = val.split(" ", 4);

        latitude = String.valueOf(tmp[1]);
        longitude = String.valueOf(tmp[2]);
        area = tmp[3];
        updateLatLng();
        Log.e("observerPlanetInfo", "observerPlanetInfo::::" + latitude + "::" + longitude);
        viewModel.setBirthPlace(latitude + "," + longitude + "," + area);


    }
}