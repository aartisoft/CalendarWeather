package com.iexamcenter.calendarweather.planet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.iexamcenter.calendarweather.R;

import java.util.Calendar;

import static com.iexamcenter.calendarweather.planet.PlanetInfoFrag.theme;

public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String ARG_POSITION = "POSITION";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        if (Integer.parseInt(getResources().getString(R.string.isNightMode)) == 1) {
            theme = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), theme, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
       /* selDate = day;
        selMonth = month;
        selYear = year;
        observerPlanetInfo();*/
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedDate", day+"-"+month+"-"+year));

    }
}
