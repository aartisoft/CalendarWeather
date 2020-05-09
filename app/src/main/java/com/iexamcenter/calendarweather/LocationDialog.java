package com.iexamcenter.calendarweather;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.compat.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;
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
import java.util.LinkedHashMap;


public class LocationDialog extends DialogFragment {
    PrefManager mPref;
    String lang;
    Resources res;
    static int mtype;
    static private MainActivity mContext;
    MainViewModel viewModel;
    LinearLayout textCntr, langCntr;
    MaterialButton cont;
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();

    private CustomAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    ArrayList<String> arList;
    ArrayList<String> arFullList;
    ArrayList<String> arLocalList;
    AutoCompleteTextView editTextFilledExposedDropdown;

    public interface LocationChangeEvents {
        void onLocationChange(String date, int type);
    }

    LocationChangeEvents lce;

    public void setLocationChangeEvents(LocationChangeEvents lce) {
        this.lce = lce;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());

    }

    public static LocationDialog newInstance(MainActivity ctx, int type) {
        LocationDialog f = new LocationDialog();
        mContext = ctx;
        mtype = type;
        return f;
    }

    @Override
    public void onStart() {

        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constant.REQUEST_CODE_LOCATION_PICKER_MAP && resultCode == Activity.RESULT_OK) {
            String[] retVal = Helper.getInstance().getAddressLatLon(data, mContext);
            if (retVal != null && retVal.length > 2) {
                String weatherStationId = "x";
                String latitude = String.valueOf(retVal[0]);
                String longitude = String.valueOf(retVal[1]);
                String area = retVal[2];
                String tmp = weatherStationId + " " + latitude + " " + longitude + " " + area;
                lce.onLocationChange(tmp, mtype);
                LocationDialog.this.dismiss();
            }

        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mContext == null)
            return null;
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.location_dialog, null);
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

       ImageView title =
                rootView.findViewById(R.id.close);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialog.this.dismiss();
            }
        });
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

                        startActivityForResult(builder.build(mContext), Constant.REQUEST_CODE_LOCATION_PICKER_MAP);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }


                } else {
                    Utility.getInstance(mContext).newToast("Please check internet connection.");
                }


            }
        });

        AutoCompleteAdapter autoCompAdapter = new AutoCompleteAdapter(mContext, R.layout.dropdown_menu_popup_item, R.id.txt1, arFullList);
        editTextFilledExposedDropdown.setAdapter(autoCompAdapter);
        editTextFilledExposedDropdown.setOnItemClickListener((adapterView, view, i, l) -> {
            try {
                // hideSoftKeyboard();
                // hideKeyboardFrom(mContext,editTextFilledExposedDropdown);
                View view1 = adapterView.getAdapter().getView(i, view, adapterView);
                updateLoc(view1);
                /*
                String[] tmp = view1.getTag().toString().split(" ", 4);
                editTextFilledExposedDropdown.setText(tmp[3]);
                System.out.println("arFullList::" + i + "::" + l + ":" + view1.getTag().toString());

                lce.onLocationChange(view1.getTag().toString(),mtype);

                viewModel.isLocationChanged(true);
                mPref.setLatitude(tmp[1]);
                mPref.setLongitude(tmp[2]);
                mPref.setWeatherCityId("");
                mPref.setAreaAdmin(tmp[3]);
                 LocationDialog.this.dismiss();
                 */

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

        CityListAdapter cityListAdapter = new CityListAdapter(mContext, arLocalList, 0, viewModel, LocationDialog.this);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
        cityList.setLayoutManager(mLayoutManager);
        cityList.setAdapter(cityListAdapter);
        editTextFilledExposedDropdown.clearFocus();


        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);


        return dialog;

    }

    public void updateLoc(View view1) {
        String[] tmp = view1.getTag().toString().split(" ", 4);
        editTextFilledExposedDropdown.setText(tmp[3]);

        lce.onLocationChange(view1.getTag().toString(), mtype);

   /* viewModel.isLocationChanged(true);
    mPref.setLatitude(tmp[1]);
    mPref.setLongitude(tmp[2]);
    mPref.setWeatherCityId("");
    mPref.setAreaAdmin(tmp[3]);*/
        LocationDialog.this.dismiss();

    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm =
                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive())
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

}

