package com.iexamcenter.calendarweather.today;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

/**
 * Created by Sasikanta_Sahoo on 3/11/2018.
 */

public class DummyFragment extends Fragment {

    BottomNavigationView bottomNavigationViewHelper;
    BottomNavigationView bottomNavigationView;

    MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new DummyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_dummy, container, false);
        bottomNavigationView = activity.bottomNavigationView;
        setRetainInstance(false);

        return rootView;
    }


}
