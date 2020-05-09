package com.iexamcenter.calendarweather;

/**
 * Created by sasikanta on 11/16/2017.
 * BaseFragment
 */

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;


public abstract class BaseFragment extends Fragment {

    protected abstract void setUp(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setUp(view);
    }

}
