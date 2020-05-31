package com.iexamcenter.calendarweather;

import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iexamcenter.calendarweather.utility.MyTheme;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MySettingsActivity extends AppCompatActivity {
    private PrefManager mPref;
    Resources res;
    RecyclerView mTabList;
    FrameLayout langflContainer, themeContainer, clockTypeContainer;
    TextView langTxt, themeTypeTxt, calendarTypeValTxt;
    TextView reporting1tv;
    int currPage = 1;
    MainViewModel viewModel;

    public void openSettingDialog(int type, Bundle args) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("SettingDialog");
        DialogFragment filterDialog;

        if (prev != null) {
            ft.remove(prev);
        }
        switch (type) {
            case 0:
                filterDialog = SettingsLangDialog.newInstance();
                filterDialog.setArguments(args);
                filterDialog.show(ft, "SettingDialog");
                break;
            case 1:
                filterDialog = SettingsThemeTypeDialog.newInstance();
                filterDialog.setArguments(args);
                filterDialog.show(ft, "SettingDialog");
                break;
            case 2:
                filterDialog = SettingsClockTypeDialog.newInstance();
                filterDialog.setArguments(args);
                filterDialog.show(ft, "SettingDialog");
                break;

        }


    }

    public void refreshLang() {
        mPref.load();
        viewModel.setCurrLang(mPref.getMyLanguage());


        String myLang = mPref.getMyLanguage();
        Locale locale = new Locale(myLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setDefaultCalendar();
        updateData();


    }

    public void refreshClockType() {

        setDefaultClockType();
        mPref.load();
        viewModel.setCurrLang(mPref.getMyLanguage());
    }

    public void refreshThemeType() {

        setDefaultThemeType();

        MyTheme.changeToTheme(this);



    }

    public void setDefaultThemeType() {

        mPref.load();
        int myTheme = mPref.getThemeType();
        switch (myTheme) {
            case 0:
                themeTypeTxt.setText("Automatically");
                break;
            case 1:
                themeTypeTxt.setText("Always");
                break;
            case 2:
                themeTypeTxt.setText("Never");
                break;
        }

    }

    public void setDefaultClockType() {

        mPref.load();
        int myCalendarType = mPref.getClockFormat();
        switch (myCalendarType) {
            case 0:
                calendarTypeValTxt.setText("12hr");
                break;
            case 1:
                calendarTypeValTxt.setText("24hr");
                break;
            case 2:
                calendarTypeValTxt.setText("24hr+");
                break;

        }

    }

    public void setDefaultCalendar() {

        String myLang = mPref.getMyLanguage();
        if (myLang.contains(res.getString(R.string.code_bengali))) {
            langTxt.setText(res.getString(R.string.en_action_bengali));
        } else if (myLang.contains(res.getString(R.string.code_gujarati))) {
            langTxt.setText(res.getString(R.string.en_action_gujarati));
        } else if (myLang.contains(res.getString(R.string.code_hindi))) {
            langTxt.setText(res.getString(R.string.en_action_hindi));
        } else if (myLang.contains(res.getString(R.string.code_kannada))) {
            langTxt.setText(res.getString(R.string.en_action_kannada));
        } else if (myLang.contains(res.getString(R.string.code_malayalam))) {
            langTxt.setText(res.getString(R.string.en_action_malayalam));
        } else if (myLang.contains(res.getString(R.string.code_marathi))) {
            langTxt.setText(res.getString(R.string.en_action_marathi));
        } else if (myLang.contains(res.getString(R.string.code_odia))) {
            langTxt.setText(res.getString(R.string.en_action_odia));
        } else if (myLang.contains(res.getString(R.string.code_punjabi))) {
            langTxt.setText(res.getString(R.string.en_action_punjabi));
        } else if (myLang.contains(res.getString(R.string.code_tamil))) {
            langTxt.setText(res.getString(R.string.en_action_tamil));
        } else if (myLang.contains(res.getString(R.string.code_telugu))) {
            langTxt.setText(res.getString(R.string.en_action_telugu));
        } else if (myLang.contains(res.getString(R.string.code_english))) {
            langTxt.setText(res.getString(R.string.en_action_english));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  MyTheme.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity_settings);
        mPref = PrefManager.getInstance(getApplicationContext());
        mPref.load();
        res = getResources();
        CalendarWeatherApp.updateAppResource(getResources(), getApplicationContext());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Bundle b = getIntent().getExtras();
        if (b != null)
            currPage = b.getInt("CURR_PAGE");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        langTxt = findViewById(R.id.langVal);
        langflContainer = findViewById(R.id.langContainer);

        themeTypeTxt = findViewById(R.id.themeTypeVal);
        themeContainer = findViewById(R.id.themeTypeContainer);
        calendarTypeValTxt = findViewById(R.id.calendarTypeVal);
        clockTypeContainer = findViewById(R.id.clockTypeContainer);

        reporting1tv = findViewById(R.id.reporting1tv);
        setDefaultCalendar();
        //  setDefaultThemeType();
        setDefaultClockType();
        langflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Bundle args = new Bundle();
                args.putInt("type", id);

                openSettingDialog(0, args);


            }
        });
        themeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Bundle args = new Bundle();
                args.putInt("type", id);

                openSettingDialog(1, args);


            }
        });

        clockTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Bundle args = new Bundle();
                args.putInt("type", id);

                openSettingDialog(2, args);


            }
        });
        // mainCntr = findViewById(R.id.mainCntr);


        FrameLayout reporting1fl;
        reporting1fl = findViewById(R.id.reporting1fl);

        reporting1fl.setOnClickListener(v -> {


            Calendar mcurrentTime = Calendar.getInstance();
            int hr = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int min = mcurrentTime.get(Calendar.MINUTE);

            int hrmin = mPref.getSettingReporting1();
            // theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
            if (hrmin > 0) {
                hr = hrmin / 60;
                min = (hrmin - hr * 60);
            }
           /* int theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
            if(Integer.parseInt(res.getString(R.string.isNightMode))==1){
                theme = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
            }*/
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(MySettingsActivity.this, R.style.datepicker, (TimePicker timePicker, int selectedHour, int selectedMinute) -> {
                String dispHrMin;
                int hr1 = selectedHour;
                int min1 = selectedMinute;
                String hrStr, minStr, ampm;
                if (hr1 >= 12) {
                    hr1 = hr1 - 12;
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                hrStr = "" + hr1;
                minStr = "" + min1;
                if (hr1 < 10) {
                    hrStr = "0" + hr1;
                }
                if (min1 < 10) {
                    minStr = "0" + min1;
                }
                dispHrMin = hrStr + ":" + minStr + " " + ampm;
                reporting1tv.setText(dispHrMin);
                mPref.setSettingReporting1(selectedHour * 60 + selectedMinute);


                mPref.load();
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                cal1.set(Calendar.HOUR_OF_DAY, selectedHour);
                cal1.set(Calendar.MINUTE, selectedMinute);

                long diff = cal1.getTimeInMillis() - cal.getTimeInMillis();
                long duration;
                if (diff > 0) {
                    duration = diff / (1000 * 60 );
                } else {
                    duration = (24 * 60) + (diff / (1000 * 60));
                }

                String TAG="MY_APP_ALARM";
                OneTimeWorkRequest mywork =
                        new OneTimeWorkRequest.Builder(MyAlaramWorker.class)
                                .addTag(TAG)
                                .setInitialDelay(duration, TimeUnit.MINUTES)
                                .build();

                WorkManager.getInstance(this).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, mywork);



            }, hr, min, false);
            mTimePicker.setTitle("Select Notification Time");
            mTimePicker.show();

        });

        updateData();

        refreshReporting();


    }


    @Override
    protected void onResume() {
        super.onResume();
        CalendarWeatherApp.isForeground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        CalendarWeatherApp.isForeground = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateData() {
        Bundle b = getIntent().getExtras();
        String[] l_tithi_arr = res.getStringArray(R.array.l_arr_tithi15);
        String[] tithi_arr = res.getStringArray(R.array.e_arr_tithi15);
        ArrayList<notif> notifArrayList = new ArrayList<>();

        notif obj0 = new notif();
        obj0.eName = res.getString(R.string.e_dina);
        obj0.lName = res.getString(R.string.l_dina);
        notifArrayList.add(obj0);


        notif obj1 = new notif();
        obj1.eName = res.getString(R.string.e_sankranti);
        obj1.lName = res.getString(R.string.l_sankranti);
        notifArrayList.add(obj1);


        notif obj2 = new notif();
        obj2.eName = res.getString(R.string.e_festival);
        obj2.lName = res.getString(R.string.l_festival);
        notifArrayList.add(obj2);
        for (int i = 0; i < l_tithi_arr.length; i++) {
            notif obj = new notif();
            obj.eName = tithi_arr[i];
            obj.lName = l_tithi_arr[i];
            notifArrayList.add(obj);
        }

        mTabList = findViewById(R.id.notif_list);

        NotifAdapter mTabListAdapter = new NotifAdapter(MySettingsActivity.this, notifArrayList);
        mTabList.setLayoutManager(new LinearLayoutManager(this));
        mTabList.setHasFixedSize(true);
        mTabList.setAdapter(mTabListAdapter);

    }


    public void refreshReporting() {
        mPref.load();
        int hrmin = 0;
        if ((hrmin = mPref.getSettingReporting1()) > 0) {
            String dispHrMin;

            int hr = hrmin / 60;
            int min = hrmin - hr * 60;
            String hrStr, minStr, ampm;
            if (hr >= 12) {
                hr = hr - 12;
                ampm = "PM";
            } else {
                ampm = "AM";
            }
            hrStr = "" + hr;
            minStr = "" + min;
            if (hr < 10) {
                hrStr = "0" + hr;
            }
            if (min < 10) {
                minStr = "0" + min;
            }
            dispHrMin = hrStr + ":" + minStr + " " + ampm;
            reporting1tv.setText(dispHrMin);

        } else {
            String dispHrMin;
            dispHrMin = "08" + ":" + "00" + " AM";
            reporting1tv.setText(dispHrMin);
            // reporting1tv.setText(String.format("%02d", hrmin / 60) + ":" + String.format("%02d", (hrmin - (hrmin / 60) * 60)));
        }


    }

    public static class notif {
        public String eName;
        public String lName;
    }
}
