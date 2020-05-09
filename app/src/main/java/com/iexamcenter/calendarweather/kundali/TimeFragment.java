package com.iexamcenter.calendarweather.kundali;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

public class TimeFragment extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    MainActivity mContext;
    PrefManager mPref;
    int min, hour;
    MainViewModel viewModel;

    public static TimeFragment newInstance() {
        Log.e("MYDATETIME", "MYDATETIME::9");
        return new TimeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("MYDATETIME", "MYDATETIME::7");
        View rootView = inflater.inflate(R.layout.frag_time, null);
        TimePicker timePicker = rootView.findViewById(R.id.timePicker);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Bundle b = getArguments();

        int type = b.getInt(ARG_POSITION);
        Log.e("mType","mTypemType:4time:"+type);

        if (!viewModel.getTimePicker1().isEmpty() && viewModel.getTimePicker1().contains(":") && (type == 100 || type == 101)) {
            String[] timeArr = viewModel.getTimePicker1().split(":");
            int selHour = Integer.parseInt(timeArr[0]);
            int selMin = Integer.parseInt(timeArr[1]);
            timePicker.setCurrentHour(selHour);
            timePicker.setCurrentMinute(selMin);
        } else if (type == 100 || type == 101) {
            min = timePicker.getCurrentMinute();
            hour = timePicker.getCurrentHour();
            viewModel.setTimePicker1(hour + ":" + min);
        } else if (!viewModel.getTimePicker2().isEmpty() && viewModel.getTimePicker2().contains(":") && (type == 200 || type == 201)) {
            String[] timeArr = viewModel.getTimePicker2().split(":");
            int selHour = Integer.parseInt(timeArr[0]);
            int selMin = Integer.parseInt(timeArr[1]);
            timePicker.setCurrentHour(selHour);
            timePicker.setCurrentMinute(selMin);
        } else if (type == 200 || type == 201) {
            min = timePicker.getCurrentMinute();
            hour = timePicker.getCurrentHour();
            viewModel.setTimePicker2(hour + ":" + min);
        } else {
            min = timePicker.getCurrentMinute();
            hour = timePicker.getCurrentHour();
            viewModel.setTimePicker(hour + ":" + min);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Log.e("mType","mTypemType:5time:"+type);
                if (type == 100 || type == 101) {
                    viewModel.setTimePicker1(i + ":" + i1);
                } else if (type == 200 || type == 201) {
                    viewModel.setTimePicker2(i + ":" + i1);
                } else {
                    viewModel.setTimePicker(i + ":" + i1);
                }
            }
        });

        /*
        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setDatePicker("");
            }
        });


        rootView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                min1=timePicker.getCurrentMinute();
                hour=timePicker.getCurrentHour();
                viewModel.setDatePicker(hour+":"+min1);

            }
        });*/
        return rootView;

    }


}

