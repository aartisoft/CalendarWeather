package com.iexamcenter.calendarweather.wallcalendar;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;

/**
 * Created by sasikanta on 9/27/2016.O
 */
public class GovtCalendarFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";

    String IMAGE_URL;
    WebView mWebView;
    PrefManager mPref;
    String fn, language_code, publicationStr, base, msg;
    int selectedMonth, selectedYear;
    FrameLayout progressbar;
    static private MainActivity mContext;
    View rootView;

    public static GovtCalendarFragment newInstance() {

        return new GovtCalendarFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();

     //   mContext.showHideBottomNavigationView(false);

     //   mContext.showHideToolBarView(true);


    }

    @Override
    public void onPause() {
        super.onPause();
       // CalendarWeatherApp.updateAppResource(mContext.getResources(), mContext);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  mContext.showHideBottomNavigationView(true);
     //   mContext.showHideToolBarView(false);

    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            CalendarWeatherApp.updateAppResource(getResources(), mContext);
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            Bundle b = getArguments();
            int position = b.getInt(ARG_POSITION);

            mContext.getSupportActionBar().setTitle("Wall Calendar");
            // mContext.getSupportActionBar().setSubtitle("");

            Calendar cal = Calendar.getInstance();
            selectedMonth = position;
            selectedYear = CalendarWeatherApp.getDisplayYearInt();

            Log.e("selectedMonth", selectedYear + ":selectedMonth:" + selectedMonth);
            language_code = mPref.getMyLanguage();
            mWebView = rootView.findViewById(R.id.webview);

            //  mWebView.getSettings().setJavaScriptEnabled(false);
               mWebView.getSettings().setBuiltInZoomControls(true);

            progressbar = rootView.findViewById(R.id.progressCntr);


            setCalendar();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_wall_calendar, container, false);
        // mContext.configMyLocalLanguage();
        msg = "Seems calendar not set yet!<br/>Please check connection &amp; try once.";
        return rootView;
    }


    private void setCalendar() {

        //  pubArr = getResources().getStringArray(R.array.publication);
        Log.e("IMAGE_URL", "IMAGE_URL:");


//https://www.calendar-365.com/2020-calendar.html
            base = mContext.getExternalFilesDir(null).toString();
            publicationStr = "govtcal";
            fn = "com.iexamcenter.calendarweather." + language_code + "_" + publicationStr + "_" + selectedYear + "_" + (selectedMonth + 1) + ".jpg";
            getSetCal();



    }

    public void getSetCal() {
        progressbar.setVisibility(View.VISIBLE);
            IMAGE_URL = "http://static.iexamcenter.com/calendarweather/" + language_code + "/" + publicationStr + "/" + selectedYear + "/" + (selectedMonth + 1) + ".webp";


        Log.e("IMAGE_URL", "IMAGE_URL:" + IMAGE_URL);

        if (Connectivity.isConnected(mContext)) {
            String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=3.0, user-scalable=yes\"/></head><body  style=\"  vertical-align: middle;width:100%;height:100%;padding:0px;margin:0px;background-color:#ffffff\"><IMG style='color:#4f6e8b;font-weight:bold;line-height: 150px;text-align: center;' width='100%' height='100%' alt='Calendar not set yet.' src='" + IMAGE_URL + "'></body></html>";
            mWebView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        } else {
            String html = "<html><head><meta name=\"viewport\" content=\"width=device-width,height=device-height, initial-scale=1.0, maximum-scale=3.0, user-scalable=yes\"/></head><body  style=\"  vertical-align: middle;width:100%;height:100%;padding:0px;margin:0px;background-color:#ffffff\"><div style='background-color: #ffffff;width: 100%;    height: auto;    bottom: 0px;    top: 0px;    left: 0;    position: absolute;'>    <div style='position: absolute; width: 300px;  height: 50px;    top: 50%;    left: 50%;    margin-top: -25px;    margin-left: -150px;'>" + msg + "</div></div></body></html>";
            mWebView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        }
        progressbar.setVisibility(View.GONE);

    }

}
