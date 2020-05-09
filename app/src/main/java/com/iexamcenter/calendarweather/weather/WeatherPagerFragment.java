package com.iexamcenter.calendarweather.weather;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class WeatherPagerFragment extends Fragment {
    public static String ARG_POSITION;

    ViewGroup rootView;
    RecyclerView recyclerView;
    int num;
    int pageType = 1;
    MainActivity activity;

    public static WeatherPagerFragment newInstance() {

        return new WeatherPagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home_sub_page, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;

        return rootView;
    }



    protected void setUp(View view) {


    }
}
