package com.iexamcenter.calendarweather.observance;

import androidx.fragment.app.Fragment;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.os.Looper.getMainLooper;

public class ObservanceFragment extends Fragment {

    PrefManager mPref;
    String[] l_festival_arr;
    String lang;
    Resources res;
    RecyclerView holidayListView;
    ArrayList<Holiday> hl;
    ObservanceListAdapter mHolidayListAdapter;


    static private MainActivity mContext;
    public static Fragment newInstance() {
        return new ObservanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
      //  mContext.showHideBottomNavigationView(false);
       // mContext.enableBackButtonViews(true);

        DateFormat dateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
      //  mContext.toolbar.setTitle("Observance");
      //  mContext.toolbar.setSubtitle(today);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // mContext.showHideBottomNavigationView(true);
    }
    private class DownloadObservanceTask extends AsyncTask<String, Integer, Integer> {
        protected Integer doInBackground(String... urls) {

            l_festival_arr = res.getStringArray(R.array.observances_mapping_arr);
            hl.clear();

            Calendar cal = Calendar.getInstance();
            int currMonth = cal.get(Calendar.MONTH);
            String[] e_month_arr = res.getStringArray(R.array.e_arr_month);
            String currMon = e_month_arr[currMonth];

            int pos = 0;
            for (int i = 0; i < l_festival_arr.length; i++) {
                JSONObject festReader = null;
                try {
                    festReader = new JSONObject(l_festival_arr[i]);

                    String hday = festReader.getString("desc");
                    String hdate = festReader.getString("date");
                    String holiday = festReader.getString("name");


                    if (Utility.getInstance(mContext).isTimeAhead(hdate) && pos == 0) {
                        pos = i;
                    }
                    Holiday obj = new Holiday();
                    obj.hday = hday;
                    obj.hdate = hdate;
                    obj.holiday = holiday;
                    obj.wiki ="https://en.wikipedia.org/wiki/"+ festReader.getString("wiki");

                    hl.add(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return pos;
        }

        protected void onPostExecute (Integer pos){

            /*mHolidayListAdapter = new FestivalListAdapter(mContext, hl);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holidayListView.setLayoutManager(llm);
            holidayListView.setHasFixedSize(true);
            holidayListView.setAdapter(mHolidayListAdapter);
            holidayListView.scrollToPosition(pos);*/
            mHolidayListAdapter.notifyDataSetChanged();


            Handler mainHandler = new Handler(getMainLooper());
            Runnable runnable = () ->  holidayListView.scrollToPosition(pos);
            mainHandler.postDelayed(runnable, 200);


        }
    }
    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
           // Bundle arg = getArguments();

            res = mContext.getResources();

           /* l_festival_arr = res.getStringArray(R.array.observances_mapping_arr);
            hl = new ArrayList<>();

            Calendar cal = Calendar.getInstance();
            int currMonth = cal.get(Calendar.MONTH);
            String[] e_month_arr = res.getStringArray(R.array.e_month_arr);
            String currMon = e_month_arr[currMonth];

            int pos = 0;
            for (int i = 0; i < l_festival_arr.length; i++) {
                JSONObject festReader = null;
                try {
                    festReader = new JSONObject(l_festival_arr[i]);

                    String hday = festReader.getString("desc");
                    String hdate = festReader.getString("date");
                    String holiday = festReader.getString("name");


                    if (Utility.getInstance(mContext).isTimeAhead(hdate) && pos == 0) {
                        pos = i;
                    }
                    Holiday obj = new Holiday();
                    obj.hday = hday;
                    obj.hdate = hdate;
                    obj.holiday = holiday;
                    obj.wiki = festReader.getString("wiki");

                    hl.add(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            holidayListView = (RecyclerView) rootView.findViewById(R.id.holidayList);
            mHolidayListAdapter = new FestivalListAdapter(mContext, hl);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holidayListView.setLayoutManager(llm);
            holidayListView.setHasFixedSize(true);
            holidayListView.setAdapter(mHolidayListAdapter);
            holidayListView.scrollToPosition(pos);
            */


            holidayListView = rootView.findViewById(R.id.holidayList);
            hl=new ArrayList<>();
            mHolidayListAdapter = new ObservanceListAdapter(mContext, hl);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holidayListView.setLayoutManager(llm);
            holidayListView.setHasFixedSize(true);
            holidayListView.setAdapter(mHolidayListAdapter);
          //  holidayListView.scrollToPosition(pos);
            new DownloadObservanceTask().execute();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_observance, null);
        return rootView;

    }


    public static class Holiday {
        public String hday, hdate, holiday, wiki;
    }

}

