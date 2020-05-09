package com.iexamcenter.calendarweather.wallcalendar;


import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;

import static android.os.Looper.getMainLooper;

/**
 * Created by sasikanta on 9/27/2016.O
 */
public class WallCalendarFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";

    PrefManager mPref;
    String language_code;
    int pagerPos;
     private MainActivity mContext;
    View rootView;

    public static WallCalendarFragment newInstance() {

        return new WallCalendarFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        Bundle args = getArguments();
        pagerPos = args.getInt(WallCalendarFragment.ARG_POSITION);


    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            CalendarWeatherApp.updateAppResource(getResources(), mContext);
            mPref = PrefManager.getInstance(mContext);
            mPref.load();

            language_code = mPref.getMyLanguage();

            mContext.getSupportActionBar().setTitle("Wall Calendar");
           // mContext.getSupportActionBar().setSubtitle("");

            RecyclerView panchangInfo = rootView.findViewById(R.id.endlesscal);
            WallCalListAdapter mPanchangAdapter = new WallCalListAdapter(mContext, pagerPos);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            panchangInfo.setLayoutManager(mLayoutManager);
            panchangInfo.setAdapter(mPanchangAdapter);
            if (language_code.contains("or") && !mPref.getPublication().contains("govtcal")) {


                Handler mainHandler = new Handler(getMainLooper());
                Runnable runnable = () -> panchangInfo.scrollToPosition(Calendar.getInstance().get(Calendar.MONTH));
                mainHandler.postDelayed(runnable, 200);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_wall_calendar, container, false);

        return rootView;
    }


}
