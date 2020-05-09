package com.iexamcenter.calendarweather.kundali;


import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;

public class DateFragment extends Fragment {
    MainViewModel viewModel;
    public static final String ARG_POSITION = "POSITION";
    MainActivity mContext;
    PrefManager mPref;
    int minYear,maxYear;
    Resources res;
    public static DateFragment newInstance() {
        return new DateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
        res=mContext.getResources();
         minYear = Integer.parseInt(res.getString(R.string.minYear));
         maxYear =Integer.parseInt(res.getString(R.string.maxYear));
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Bundle b=getArguments();
        int type = b.getInt(ARG_POSITION);


        View rootView = inflater.inflate(R.layout.frag_date, container, false);
        DatePicker datePicker = rootView.findViewById(R.id.datePicker);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(maxYear,12,31);
        datePicker.setMaxDate(maxDate.getTimeInMillis());
        Calendar minDate = Calendar.getInstance();
        minDate.set(minYear,0,1);
        datePicker.setMinDate(minDate.getTimeInMillis());
        Calendar calendar = Calendar.getInstance();
        if (!viewModel.getDatePicker1().isEmpty() && viewModel.getDatePicker1().contains("-") && (type == 100 || type == 101)) {
            String[] dateArr = viewModel.getDatePicker1().split("-");
            int selYear = Integer.parseInt(dateArr[0]);
            int selMonth = Integer.parseInt(dateArr[1]);
            int selDate = Integer.parseInt(dateArr[2]);
            calendar.set(selYear, selMonth, selDate);
        }else   if (!viewModel.getDatePicker2().isEmpty() && viewModel.getDatePicker2().contains("-") && (type == 200 || type == 201)) {
            String[] dateArr = viewModel.getDatePicker2().split("-");
            int selYear = Integer.parseInt(dateArr[0]);
            int selMonth = Integer.parseInt(dateArr[1]);
            int selDate = Integer.parseInt(dateArr[2]);
            calendar.set(selYear, selMonth, selDate);
        }else if (!viewModel.getDatePicker().isEmpty() && viewModel.getDatePicker().contains("-")) {
            String[] dateArr = viewModel.getDatePicker().split("-");
            int selYear = Integer.parseInt(dateArr[0]);
            int selMonth = Integer.parseInt(dateArr[1]);
            int selDate = Integer.parseInt(dateArr[2]);
            calendar.set(selYear, selMonth, selDate);
        }

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                if(type == 100 || type == 101){
                    viewModel.setDatePicker1(year + "-" + month + "-" + dayOfMonth);
                }else  if(type == 200 || type == 201){
                    viewModel.setDatePicker2(year + "-" + month + "-" + dayOfMonth);
                }else{
                    viewModel.setDatePicker(year + "-" + month + "-" + dayOfMonth);
                }


            }
        });


        return rootView;

    }


}

