package com.iexamcenter.calendarweather.thisday;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.ThisDayResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class ThisDayFragment extends Fragment {
    ArrayList<horoscopeItem> horoscopeAl = new ArrayList<>();

    PrefManager mPref;
    String lang;
    ThisDayResponse.TODAY resList;
    BirthDayAdapter mBirthDayAdapter;
    DeathDayAdapter mDeathDayAdapter;
    HistoryAdapter mHistoryAdapter;
    RecyclerView famousBday, famousDday, history;

    ArrayList<ThisDayResponse.BIRTHDAY> birthDayItemList;
    ArrayList<ThisDayResponse.DEATHDAY> deathDayItemList;
    ArrayList<ThisDayResponse.HISTORY> historyItemList;

    TextView bDayMore, dDayMore, historyMore;
    // MovableFloatingActionButton refreshMsg;
    private MainActivity mContext;
    private String JSONFILE = "ONTHISDAY.txt";
    Calendar cal;

    public static ThisDayFragment newInstance() {

        return new ThisDayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    public void showMore(String type) {

        FragmentManager fm = mContext.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_PROFILE_DETAILS);
        if (frag == null)
            frag = OnThisDayFragment.newInstance();


        Bundle b = new Bundle();
        b.putString("TYPE", type);
        frag.setArguments(b);
        ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_PROFILE_DETAILS);
        ft.addToBackStack(AppConstants.FRAG_PROFILE_DETAILS);
        ft.commit();

    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mContext = (MainActivity) getActivity();
            setHasOptionsMenu(true);
            Bundle arg = getArguments();
            birthDayItemList = new ArrayList<>();
            deathDayItemList = new ArrayList<>();
            historyItemList = new ArrayList<>();
            int URL_LOADER = arg.getInt("position");
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            lang = mPref.getMyLanguage();
            bDayMore = rootView.findViewById(R.id.bDayMore);
            dDayMore = rootView.findViewById(R.id.dDayMore);
            historyMore = rootView.findViewById(R.id.historyMore);
            bDayMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMore("birthdays");
                }
            });
            dDayMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMore("deaths");
                }
            });
            historyMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMore("events");
                }
            });
            famousBday = rootView.findViewById(R.id.famousBday);
            famousDday = rootView.findViewById(R.id.famousDday);
            history = rootView.findViewById(R.id.history);
            // refreshMsg = rootView.findViewById(R.id.refreshMsg);
            famousDday.setNestedScrollingEnabled(false);

            famousBday.setNestedScrollingEnabled(false);
            history.setNestedScrollingEnabled(false);
            mBirthDayAdapter = new BirthDayAdapter(mContext, birthDayItemList, URL_LOADER);
            LinearLayoutManager llm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            famousBday.setLayoutManager(llm);
            famousBday.setHasFixedSize(false);
            famousBday.setAdapter(mBirthDayAdapter);
            mDeathDayAdapter = new DeathDayAdapter(mContext, deathDayItemList, URL_LOADER);
            LinearLayoutManager llm1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            famousDday.setLayoutManager(llm1);
            famousDday.setHasFixedSize(false);
            famousDday.setAdapter(mDeathDayAdapter);


            mHistoryAdapter = new HistoryAdapter(mContext, historyItemList, URL_LOADER);
            LinearLayoutManager llm2 = new LinearLayoutManager(mContext);
            history.setLayoutManager(llm2);
            history.setHasFixedSize(false);
            history.setAdapter(mHistoryAdapter);


            refreshWhatToday();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_thisday, null);


        setRetainInstance(true);

        return rootView;
    }

    public static class horoscopeItem {
        public String title, year, date, type, wiki;
    }


    public void refreshWhatToday() {

        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


        if (!mPref.getThisday().contains(dayOfMonth + "-" + month + "-" + year) && Connectivity.isConnected(mContext)) {
            Log.e("getThisday", "getThisday1");
            String json1 = "{'YEAR':'" + year + "','MONTH':'" + month + "','DAYOFMONTH':'" + dayOfMonth + "','LANG':'" + lang + "'}";
            loadData(json1, Constant.WHATTODAY_API);

        } else if (!mPref.getThisday().contains(dayOfMonth + "-" + month + "-" + year) && !Connectivity.isConnected(mContext)) {
            Log.e("getThisday", "getThisday2");
            Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_LONG).show();

        } else {
            Log.e("getThisday", "getThisday3");
            Gson gson = new Gson();
            ThisDayResponse res = gson.fromJson(Utility.getInstance(mContext).readFromFile(mContext, JSONFILE), ThisDayResponse.class);
            resList = res.getTodayList();

            handleUI();


        }
    }

    public void handleUI() {

        birthDayItemList.clear();
        deathDayItemList.clear();
        historyItemList.clear();
        birthDayItemList.addAll(resList.getBirthDay());
        deathDayItemList.addAll(resList.getDeathDay());
        historyItemList.addAll(resList.getHistory());


        mBirthDayAdapter.notifyDataSetChanged();
        mDeathDayAdapter.notifyDataSetChanged();
        mHistoryAdapter.notifyDataSetChanged();


    }
    private void loadData(String profileJson, int api) {
        try {
            HttpRequestObject mReqobject = new HttpRequestObject(mContext);
            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

            ApiUtil.getIExamCenterBaseURLClass().getToday(body).enqueue(new Callback<ThisDayResponse>() {


                @Override
                public void onResponse(Call<ThisDayResponse> call, retrofit2.Response<ThisDayResponse> response) {
                    if (response.isSuccessful()) {
                        ThisDayResponse res = response.body();
                        System.out.println("ThisDayResponse..:" + jsonHeader);
                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(res);
                        System.out.println("ThisDayResponse..:" + jsonInString);
                        Utility.getInstance(mContext).writeToFile(jsonInString, mContext, JSONFILE);
                        resList = res.getTodayList();
                        handleUI();
                        mPref.setThisDayImageBaseUrl(resList.getImageBaseUrl());
                        cal = Calendar.getInstance();
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        mPref.setThisday(dayOfMonth + "-" + month + "-" + year);
                        mPref.load();
                    }
                }

                @Override
                public void onFailure(Call<ThisDayResponse> call, Throwable t) {
                    //showErrorMessage();
                    Log.d("xxx", "error loading from API");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadData1(String profileJson, int api) {
        try {
            HttpRequestObject mReqobject = new HttpRequestObject(mContext);
            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

            ApiUtil.getIExamCenterBaseURLClass().getToday(body).enqueue(new Callback<ThisDayResponse>() {


                @Override
                public void onResponse(Call<ThisDayResponse> call, retrofit2.Response<ThisDayResponse> response) {
                    if (response.isSuccessful()) {
                        ThisDayResponse res = response.body();
                        System.out.println("ThisDayResponse..:" + jsonHeader);
                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(res);
                        System.out.println("ThisDayResponse..:" + jsonInString);
                        Utility.getInstance(mContext).writeToFile(jsonInString, mContext, JSONFILE);
                        resList = res.getTodayList();
                        handleUI();
                        mPref.setThisDayImageBaseUrl(resList.getImageBaseUrl());
                        cal = Calendar.getInstance();
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        mPref.setThisday(dayOfMonth + "-" + month + "-" + year);
                        mPref.load();
                    }
                }

                @Override
                public void onFailure(Call<ThisDayResponse> call, Throwable t) {
                    //showErrorMessage();
                    Log.d("xxx", "error loading from API");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
