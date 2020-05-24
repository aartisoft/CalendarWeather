package com.iexamcenter.calendarweather.tools;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgeCalculatorFrag extends Fragment{
    public static final String ARG_POSITION = "POSITION";

    View clickId;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView lbl_date, lbl_date1;
    MaterialButton sbmt;
    View view1;
    String titleval, subTitleVal;

    private MainActivity mContext;

    String pageTitle, subTitle;

    Resources res;
    int mType;
    TextView txt, date, date1;
    LinearLayout dateCntr, dateCntr1;
    public final String DATE_FORMAT_1 = "dd MMM yyyy";
    public final String DATE_FORMAT_2 = "dd MMM yyyy hh:mm a";

    Calendar cal;

    public static AgeCalculatorFrag newInstance() {
        AgeCalculatorFrag myFragment = new AgeCalculatorFrag();

        return myFragment;
    }


    public void getMyResource() {
        res = mContext.getResources();

    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {


            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();

            mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;




            setUp(rootView);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    protected void setUp(View rootView) {
        cal = Calendar.getInstance();
        lbl_date = rootView.findViewById(R.id.lbl_date);
        lbl_date1 = rootView.findViewById(R.id.lbl_date1);
        sbmt = rootView.findViewById(R.id.sbmt);
        view1 = rootView.findViewById(R.id.view1);

        txt = rootView.findViewById(R.id.txt);
        date = rootView.findViewById(R.id.date);
        date1 = rootView.findViewById(R.id.date1);
        dateCntr = rootView.findViewById(R.id.dateCntr);
        dateCntr1 = rootView.findViewById(R.id.dateCntr1);
        date.setText( dateFormat(cal));
        date1.setText( dateFormat(cal));
        dateCntr.setTag(cal);
        dateCntr1.setTag(cal);
        sbmt.setOnClickListener(v -> {
            String age = AgeCalculator.calculateAge((Calendar) dateCntr.getTag(), (Calendar) dateCntr1.getTag()).toString();

            txt.setText("Age : " + age);
        });


        dateCntr.setOnClickListener(v -> {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.datepicker, dateCal, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        });

        dateCntr1.setOnClickListener(v -> {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.datepicker, dateCal1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        });

    }

    DatePickerDialog.OnDateSetListener dateCal = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar cal = Calendar.getInstance();

            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            date.setText( dateFormat(cal));
            dateCntr.setTag(cal);
        }

    };
    DatePickerDialog.OnDateSetListener dateCal1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            date1.setText( dateFormat(cal));
            dateCntr1.setTag(cal);
        }

    };
public String dateFormat(Calendar calendar){

    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
    return format.format(calendar.getTime());
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_age_calculator, null);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        pageTitle = "Age Calculator";
        subTitle = "Know age in years, months & days";

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


}

