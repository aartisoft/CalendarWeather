package com.iexamcenter.calendarweather.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.compat.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AutoCompleteAdapter;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.festival.CityListAdapter;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LocationFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;
    MainViewModel viewModel;
    LinearLayout textCntr, langCntr;
    MaterialButton cont;
    ArrayList<String> arList;
    ArrayList<String> arFullList;
    ArrayList<String> arLocalList;
    AutoCompleteTextView editTextFilledExposedDropdown;

    public static LocationFrag newInstance() {

        return new LocationFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.showHideBottomNavigationView(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mContext.showHideBottomNavigationView(false);
            setHasOptionsMenu(true);
            mContext.enableBackButtonViews(true);
            res = mContext.getResources();
            mContext.getSupportActionBar().setTitle("Location");

            viewModel = new ViewModelProvider(this).get(MainViewModel.class);
            langCntr = rootView.findViewById(R.id.langCntr);
            textCntr = rootView.findViewById(R.id.textCntr);
            cont = rootView.findViewById(R.id.proceed);
            res = mContext.getResources();
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            lang = mPref.getMyLanguage();


            arList = new ArrayList<>();
            arFullList = new ArrayList<>();
            arLocalList = new ArrayList<>();
            loadData();


            editTextFilledExposedDropdown =
                    rootView.findViewById(R.id.filled_exposed_dropdown);
            MaterialButton map = rootView.findViewById(R.id.map);

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Connectivity.isConnected(mContext)) {

                        try {

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            builder.setLatLngBounds(CalendarWeatherApp.getInstance().getLatLngBounds());

                            mContext.startActivityForResult(builder.build(mContext), Constant.REQUEST_CODE_LOCATION_PICKER_MAP);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }

                        mContext.onBackPressed();
                    } else {
                        Utility.getInstance(mContext).newToast("Please check internet connection.");
                    }


                }
            });

            AutoCompleteAdapter autoCompAdapter = new AutoCompleteAdapter(mContext, R.layout.dropdown_menu_popup_item, R.id.txt1, arFullList);
            editTextFilledExposedDropdown.setAdapter(autoCompAdapter);
            editTextFilledExposedDropdown.setOnItemClickListener((adapterView, view, i, l) -> {
                try {
                    hideSoftKeyboard();
                    View view1 = adapterView.getAdapter().getView(i, view, adapterView);

                    String[] tmp = view1.getTag().toString().split(" ", 4);
                    editTextFilledExposedDropdown.setText(tmp[3]);
                    System.out.println("arFullList::" + i + "::" + l + ":" + view1.getTag().toString());

                    mPref.setLatitude(tmp[1]);
                    mPref.setLongitude(tmp[2]);
                    mPref.setWeatherCityId("");
                    mPref.setAreaAdmin(tmp[3]);

                    viewModel.isLocationChanged(true);
                    viewModel.setBirthPlace(tmp[1] + "," + tmp[2]+","+tmp[3]);
                    mContext.onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            Collections.sort(arLocalList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            RecyclerView cityList = rootView.findViewById(R.id.citylist);

          //  CityListAdapter cityListAdapter = new CityListAdapter(mContext, arLocalList, 0,viewModel);
          //  LinearLayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
           // cityList.setLayoutManager(mLayoutManager);
           // cityList.setAdapter(cityListAdapter);

            editTextFilledExposedDropdown.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("MYDATETIME", "::onActivityResult:kk:" + requestCode + "::" + resultCode);
        String latitude, longitude, area;
        if (requestCode == Constant.REQUEST_CODE_BIRTH_LOCATION_PICKER_MAP && resultCode == Activity.RESULT_OK) {
            String[] retVal = Helper.getInstance().getAddressLatLon(data, mContext);
            if (retVal != null && retVal.length > 2) {
                latitude = String.valueOf(retVal[0]);
                longitude = String.valueOf(retVal[1]);
                area = retVal[2];
                mPref.setLatitude(latitude);
                mPref.setLongitude(longitude);
                mPref.setWeatherCityId("");
                mPref.setAreaAdmin(area);
                viewModel.isLocationChanged(true);
                viewModel.setBirthPlace(latitude + "," + longitude+","+area);
            }

        }

    }


    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mContext.toolbar.getWindowToken(), 0);
        /*
        InputMethodManager imm =
                (InputMethodManager) editTextFilledExposedDropdown.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

         */

    }

    //load some initial data into out list
    private void loadData() {

        arList.clear();
        arFullList.clear();
        arLocalList.clear();
        BufferedReader reader = null;
        String receiveString;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("citylist.txt")));

            while ((receiveString = reader.readLine()) != null) {
                String cityName = receiveString;

                String[] tmp, tmp1;
                if (cityName.contains("[Y]")) {
                    cityName = cityName.replace("[Y]", "");
                    tmp = cityName.split(" ", 4);
                    tmp1 = tmp[3].split(",");
                    arLocalList.add(tmp1[0] + "|" + tmp1[1] + "|" + cityName);

                } else {
                    tmp = cityName.split(" ", 4);
                    tmp1 = tmp[3].split(",");
                }

                arList.add(tmp1[0].trim());
                arFullList.add(cityName.trim());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location_dialog, null);
        setHasOptionsMenu(true);
        return rootView;

    }

}

