package com.iexamcenter.calendarweather.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.AppConstants;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * QuoteMainFragment
 */

public class YogaMainFragment extends Fragment {
    public static final String ARG_POSITION = "ARG_POSITION";
    MainActivity activity;
    Resources res;
    RecyclerView recyclerView;
    private RewardedAd rewardedAd;
    int page;
    MaterialButton ads;
    PremiumListAdapter mAdapter;
    String[] le_arr_special_ausp_day1, le_arr_special_ausp_day2;

    public static YogaMainFragment newInstance() {

        return new YogaMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_calculator, container, false);
        Bundle arg = getArguments();
        page = arg.getInt(ARG_POSITION);

        res = activity != null ? activity.getResources() : null;
        // if (!CalendarWeatherApp.isPanchangEng) {
        le_arr_special_ausp_day1 = res.getStringArray(R.array.l_arr_special_ausp_day);
        // } else {
        le_arr_special_ausp_day2 = res.getStringArray(R.array.e_arr_special_ausp_day);
        // }

        setRetainInstance(true);
        setUp(rootView);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getPageSubpage().observe(getViewLifecycleOwner(), pageSubpage -> {

            String[] page = pageSubpage.split("_");
            Log.e("pagepage", ":::page:::::::" + page[0] + "--" + page[1]);
            if (Integer.parseInt(page[1]) >= 13 && CalendarWeatherApp.isRewardedPremiumGrp2) {
                int pg=Integer.parseInt(page[1])-13;
                openPage(pg );
            }else{
                Toast.makeText(activity,"Please unlock, before use",Toast.LENGTH_SHORT).show();
            }

        });
        return rootView;
    }

    public void openPage(int position) {
        String[] mLe_arr_special_calculator1, mLe_arr_special_calculator2;
        mLe_arr_special_calculator1 = le_arr_special_ausp_day1;
        mLe_arr_special_calculator2 = le_arr_special_ausp_day2;
        String title1 = mLe_arr_special_calculator1[position];
        String title2 = mLe_arr_special_calculator2[position];

        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment frag;
        FragmentTransaction ft = fm.beginTransaction();
        Bundle b = new Bundle();

        frag = PanchangaYogaFrag.newInstance();
        b.putString("PAGE_TITLE_ENG", title2);
        b.putString("PAGE_TITLE_LOCAL", title1);
        b.putInt(PanchangaYogaFrag.ARG_POSITION, position);
        frag.setArguments(b);
        ft.replace(R.id.frameContainer, frag, AppConstants.FRAG_ANNIVERSARY_TAG);
        ft.addToBackStack(AppConstants.FRAG_ANNIVERSARY_TAG);
        ft.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void loadAds() {

        String reward_ad_unit_id_2 = activity.getResources().getString(R.string.reward_ad_unit_id_2);
        rewardedAd = new RewardedAd(activity,
                reward_ad_unit_id_2);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.e("RewardedAdClosed", "onRewardedAdLoaded-Sorry, Ads Closed. Try again l");
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Log.e("RewardedAdClosed", errorCode + "onRewardedAdFailedToLoad-Sorry, Ads Closed. Try again l");

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Connectivity.isConnected(activity)) {
                    Toast.makeText(activity, "Please check internet connection", Toast.LENGTH_LONG).show();
                    return;
                }
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = activity;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                            Log.e("RewardedAdClosed", "onRewardedAdOpened-Sorry, Ads Closed. Try again l");

                        }

                        @Override
                        public void onRewardedAdClosed() {
                            loadAds();
                            Log.e("RewardedAdClosed", "onRewardedAdClosed-Sorry, Ads Closed. Try again l");
                            //  ads.setText("Sorry, Ads Closed. Try again later.");
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            CalendarWeatherApp.isRewardedPremiumGrp2 = true;
                            mAdapter.notifyDataSetChanged();
                            Log.e("RewardedAdClosed", "onUserEarnedReward-Sorry, Ads Closed. Try again l");
                            ads.setText("Thank You, Now you can able to access below features");
                            ads.setEnabled(false);
                            ads.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            Log.e("RewardedAdClosed", "onRewardedAdFailedToShow-Sorry, Ads Closed. Try again l");
                            ads.setText("Sorry, Failed to load ads, Try again later.");
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });
    }

    protected void setUp(View rootView) {
        ads = rootView.findViewById(R.id.ads);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> myDataset = new ArrayList<>();
        List<String> pageList = Arrays.asList(le_arr_special_ausp_day1);
        myDataset.addAll(pageList);

       /* myDataset.add("Amrit Siddhi Yoga");
        myDataset.add("Sarvartha Siddhi Yoga");
        myDataset.add("Ravi Pushya Yoga");
        myDataset.add("Guru Pushya Yoga");
        myDataset.add("Dwipushkar Yoga");
        myDataset.add("Tripushkar Yoga");
        myDataset.add("Abhijit Nakshetra");*/
        ads.setText("Unlock below items for just one Google Ads");
        //ads.setText("Tap here to watch one Google Ads to unlock below items");
        mAdapter = new PremiumListAdapter(activity, myDataset, ads, le_arr_special_ausp_day1, le_arr_special_ausp_day2);
        recyclerView.setAdapter(mAdapter);
        if (!CalendarWeatherApp.isRewardedPremiumGrp2) {
            ads.setVisibility(View.VISIBLE);
            loadAds();
        } else {
            ads.setVisibility(View.GONE);
        }


    }

    public static class data {
        public String page;
        public int type;
    }
}