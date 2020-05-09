package com.iexamcenter.calendarweather.sunmoon;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class SunmoonFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    ArrayList<Utility.SunDetails> riseSetBucket1;
    ArrayList<Utility.MoonDetails> riseSetBucket2;

    RiseSetListAdapter mRiseSetAdapter;
    Boolean isMore = false;
    int pos = 0;
    static private MainActivity mContext;
    PrefManager mPref;

    //private BroadcastReceiver mNewsDownloadReceiver;
   // IntentFilter mNewsDownloadFilter;
    View rootView;
    ProgressBar progressBar;
    int monthPos, dayPos;

    RecyclerView risesetinfo;

    public static SunmoonFragment newInstance() {

        return new SunmoonFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = (MainActivity) getActivity();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

          //  mContext.unregisterReceiver(mNewsDownloadReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            riseSetBucket1.clear();
            riseSetBucket2.clear();
            //  CalendarWeatherApp.updateAppResource(getResources(), mContext);
            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.MONTH, monthPos);


            riseSetBucket1.addAll(Utility.getInstance(mContext).getSunDetails(cal1));
            riseSetBucket2.addAll(Utility.getInstance(mContext).getMoonDetails(cal1));
            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
            if (mRiseSetAdapter != null)
                mRiseSetAdapter.notifyDataSetChanged();
            risesetinfo.scrollToPosition(pos);
            progressBar.setVisibility(View.GONE);
        }
    }

    public Boolean isMore() {
        return isMore;
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mPref = PrefManager.getInstance(mContext);

        risesetinfo = rootView.findViewById(R.id.risesetinfo);
        progressBar = rootView.findViewById(R.id.progressbar);


        mContext.getSupportActionBar().setTitle("Sun Moon Details");
         mContext.getSupportActionBar().setSubtitle("");
        Bundle args = getArguments();

        monthPos = SunMoonMainFragment.currMonthPos;
        dayPos = SunMoonMainFragment.dayPosition;
        isMore = args.getInt(ARG_POSITION) != 0;


        riseSetBucket1 = new ArrayList<>();
        riseSetBucket2 = new ArrayList<>();


        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.MONTH, monthPos);
        if (dayPos == 0)
            pos = cal1.get(Calendar.DAY_OF_MONTH) - 1;
        else
            pos = dayPos - 1;





        mRiseSetAdapter = new RiseSetListAdapter(mContext,this, cal1);
        LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        risesetinfo.setLayoutManager(riseSetll);
        risesetinfo.setHasFixedSize(false);
        risesetinfo.setAdapter(mRiseSetAdapter);
        progressBar.setVisibility(View.GONE);
        risesetinfo.scrollToPosition(pos);
       // createMyView();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_sunmoon, container, false);

        return rootView;
    }


}
