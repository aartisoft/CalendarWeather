package com.iexamcenter.calendarweather.thisday;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.OnThisDayResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class OnThisDayFragment extends Fragment {
    ArrayList<OnThisDayItems> horoscopeAl = new ArrayList<>();

    PrefManager mPref;
    String lang;
    ArrayList<OnThisDayResponse.WhatToDayList> resList;
    private int type = 1;
    private String eventType = "events";
    OnThisDayAdapter mMomentListAdapter;
    RecyclerView momentListView;

    private MainActivity mContext;
    Calendar cal;
    RelativeLayout progressCntr;
    ProgressBar progressBar;
    TextView progressMsg;
    private String JSONFILE = "ONTHISDAYMORE.txt";
    private String oldTitle = "";
    private String oldSubTitle = "";

    public static OnThisDayFragment newInstance() {

        return new OnThisDayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();

        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        mContext.showHideToolBarView(true);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mContext.showHideBottomNavigationView(true);
        mContext.showHideToolBarView(false);
        mContext.toolbar.setTitle(oldTitle);
        mContext.toolbar.setSubtitle(oldSubTitle);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mContext = (MainActivity) getActivity();
            setHasOptionsMenu(true);
            Bundle arg = getArguments();
            eventType = arg.getString("TYPE");
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            oldTitle = mContext.toolbar.getTitle().toString();
            oldSubTitle = mContext.toolbar.getSubtitle().toString();


            if (eventType.contains("birthdays")) {

                mContext.toolbar.setTitle("Birthdays");
                mContext.toolbar.setSubtitle("Famous People");
            } else if (eventType.contains("deaths")) {
                mContext.toolbar.setTitle("Deaths");
                mContext.toolbar.setSubtitle("Famous People");
            } else if (eventType.contains("events")) {
                mContext.toolbar.setTitle("History");
                mContext.toolbar.setSubtitle("On This Day");
            }


            lang = mPref.getMyLanguage();
            horoscopeAl = new ArrayList<>();
            momentListView = rootView.findViewById(R.id.rashiphalaList);

            progressCntr = rootView.findViewById(R.id.progressCntr);
            progressBar = rootView.findViewById(R.id.progressBar);
            progressMsg = rootView.findViewById(R.id.progressMsg);

            mMomentListAdapter = new OnThisDayAdapter(mContext, horoscopeAl, type);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            momentListView.setLayoutManager(llm);
            momentListView.setHasFixedSize(false);
            momentListView.setAdapter(mMomentListAdapter);


            cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            // CalendarWeatherApp.log("CalendarWeatherApp::::"+dayOfMonth + "-" + month + "-" + year);

            if (!mPref.getWhattoday().contains(dayOfMonth + "-" + month + "-" + year)) {

                String json1 = "{'YEAR':'" + year + "','MONTH':'" + month + "','DAYOFMONTH':'" + dayOfMonth + "','LANG':'" + lang + "'}";
                loadData(json1, Constant.WHATTODAY_API);


            } else {
                Gson gson = new Gson();
                OnThisDayResponse res = gson.fromJson(Utility.getInstance(mContext).readFromFile(mContext, JSONFILE), OnThisDayResponse.class);
                resList = res.getList();

                handleUI();

            }


        } catch (Exception e) {
            String errorFeedBack = "BIRTHDAYFRAG-" + e.getMessage();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_whattodayitem, null);

        //  refreshWhatToday();
        setRetainInstance(true);

        return rootView;
    }

    public void handleUI() {

        horoscopeAl.clear();
        String horoscopeDt = "";

        for (int i = 0; i < resList.size(); i++) {
            OnThisDayResponse.WhatToDayList obj = resList.get(i);

            if (obj.getType().contains(eventType)) {
                OnThisDayItems horoscopeItem = new OnThisDayItems();
                horoscopeItem.date = obj.getMonth() + "-" + obj.getDay() + ", " + obj.getYear();
                horoscopeItem.title = obj.getTitle();
                horoscopeItem.year = obj.getYear();
                horoscopeItem.type = obj.getType();


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    horoscopeItem.wiki = obj.getWiki();
                } else {
                    horoscopeItem.wiki = " ";
                }


                horoscopeAl.add(horoscopeItem);
            }


        }


        mMomentListAdapter.notifyDataSetChanged();
        progressCntr.setVisibility(View.GONE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public static class OnThisDayItems {
        public String title, year, date, type, wiki;
    }


    private void loadData(String profileJson, int api) {
        try {


            HttpRequestObject mReqobject = new HttpRequestObject(mContext);

            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

            ApiUtil.getIExamCenterBaseURLClass().getThisDayMore(body).enqueue(new Callback<OnThisDayResponse>() {


                @Override
                public void onResponse
                        (Call<OnThisDayResponse> call, retrofit2.Response<OnThisDayResponse> response) {
                    if (response.isSuccessful()) {
                        OnThisDayResponse res = response.body();

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(res);
                        Utility.getInstance(mContext).writeToFile(jsonInString, mContext, JSONFILE);

                        resList = res.getList();
                        handleUI();

                        cal = Calendar.getInstance();
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        mPref.setWhattoday(dayOfMonth + "-" + month + "-" + year);
                        mPref.load();

                        progressCntr.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<OnThisDayResponse> call, Throwable t) {
                    //showErrorMessage();
                    Log.d("xxx", "error loading from API");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
