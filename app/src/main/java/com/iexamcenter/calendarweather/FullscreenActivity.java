package com.iexamcenter.calendarweather;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    PrefManager mPref;
    String lang;
    Resources res;
    static private FullscreenActivity mContext;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
 
        mContext = (FullscreenActivity.this) ;
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.location_dialog, null);
        langCntr = findViewById(R.id.langCntr);
        textCntr = findViewById(R.id.textCntr);
        cont = findViewById(R.id.proceed);
        res = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        lang = mPref.getMyLanguage();


        arList = new ArrayList<>();
        arFullList = new ArrayList<>();
        arLocalList = new ArrayList<>();
        loadData();


        editTextFilledExposedDropdown =
                findViewById(R.id.filled_exposed_dropdown);
        MaterialButton map = findViewById(R.id.map);

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
                    onBackPressed();

                  //  LocationDialog.this.dismiss();
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

                viewModel.isLocationChanged(true);
                mPref.setLatitude(tmp[1]);
                mPref.setLongitude(tmp[2]);
                mPref.setWeatherCityId("");
                mPref.setAreaAdmin(tmp[3]);
                onBackPressed();
               // LocationDialog.this.dismiss();
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

        RecyclerView cityList = findViewById(R.id.citylist);

        CityListAdapter cityListAdapter = new CityListAdapter(mContext, arLocalList, 0);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
        cityList.setLayoutManager(mLayoutManager);
        cityList.setAdapter(cityListAdapter);
        editTextFilledExposedDropdown.clearFocus();

    }



    public void hideSoftKeyboard() {
        InputMethodManager imm =
                (InputMethodManager)editTextFilledExposedDropdown.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

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
