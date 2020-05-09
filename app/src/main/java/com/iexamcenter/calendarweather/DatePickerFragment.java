package com.iexamcenter.calendarweather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;
    FragmentActivity mContext;
    PrefManager mPref;

    public DatePickerFragment() {


    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);//args.getInt("year");
        month = cal.get(Calendar.MONTH);//args.getInt("month");
        day = cal.get(Calendar.DAY_OF_MONTH);//args.getInt("day");
        // DatePickerFragment.this.dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        Calendar maxCal = Calendar.getInstance();
        String maxYear = mContext.getResources().getString(R.string.maxYear);
        String minYear = mContext.getResources().getString(R.string.minYear);
        maxCal.set(Calendar.YEAR, Integer.parseInt(maxYear));
        maxCal.set(Calendar.MONTH, 0);
        maxCal.set(Calendar.DAY_OF_MONTH, 1);
        maxCal.set(Calendar.HOUR_OF_DAY, 0);
        maxCal.set(Calendar.MINUTE, 0);
        maxCal.set(Calendar.SECOND, 1);
        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, Integer.parseInt(minYear));
        minCal.set(Calendar.MONTH, 0);
        minCal.set(Calendar.DAY_OF_MONTH, 1);
        minCal.set(Calendar.HOUR_OF_DAY, 0);
        minCal.set(Calendar.MINUTE, 0);
        minCal.set(Calendar.SECOND, 1);
      // int theme =AlertDialog.THEME_HOLO_LIGHT;// AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
       /*  if (Integer.parseInt(mContext.getResources().getString(R.string.isNightMode)) == 1) {
            theme = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        }*/
        DatePickerDialog dialog;
        dialog = new DatePickerDialog(mContext,R.style.datepicker, ondateSet, year, month, day);

        dialog.getDatePicker().setMaxDate(maxCal.getTime().getTime());
        dialog.getDatePicker().setMinDate(minCal.getTime().getTime());
        return dialog;

    }
}