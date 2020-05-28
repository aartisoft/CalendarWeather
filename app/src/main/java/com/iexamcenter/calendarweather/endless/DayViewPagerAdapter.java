package com.iexamcenter.calendarweather.endless;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;


/**
 * Created by sasikanta on 11/14/2017.
 * MuhurtaPagerAdapter
 */

class DayViewPagerAdapter extends FragmentPagerAdapter {
    // private String[] pageTitle = {"Regional", "English"};
    MainActivity activity;
    int selectedDay, selectedMonth, selectedYear;
    String languageFull, langCode;
    Resources mRes;

    private DayViewPagerAdapter(FragmentManager fm, FragmentActivity activity, int selectedDay, int selectedMonth, int selectedYear, String languageFull, String langCode) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mRes = activity.getResources();
        this.selectedDay = selectedDay;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        this.activity = (MainActivity) activity;
        this.languageFull = languageFull;
        this.langCode = langCode;

    }

    public static DayViewPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, int selectedDay, int selectedMonth, int selectedYear, String languageFull, String langCode) {

        return new DayViewPagerAdapter(fm, activity, selectedDay, selectedMonth, selectedYear, languageFull, langCode);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        Bundle args;
       // if (langCode.contains("or")) {
            switch (position) {
                case 0:
                    fragment = DayViewFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);


                    args.putInt(DayViewFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
                case 1:
                    fragment = TaraBalamFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(TaraBalamFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    fragment = ChandraBalamFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(ChandraBalamFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
                case 3:
                    fragment = ChoghadiyaFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(ChoghadiyaFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
                case 4:
                    fragment = HoraMuhurtaFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(HoraMuhurtaFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
            case 5:
                fragment = AuspWorkFragment.newInstance();
                args = new Bundle();
                args.putInt("DAY", selectedDay);
                args.putInt("MONTH", selectedMonth);
                args.putInt("YEAR", selectedYear);

                args.putInt(AuspWorkFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                return fragment;
            }

      /*  }else{
            switch (position) {
                case 0:
                    fragment = DayViewFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);


                    args.putInt(DayViewFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;

                case 1:
                    fragment = ChoghadiyaFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(ChoghadiyaFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    fragment = HoraMuhurtaFragment.newInstance();
                    args = new Bundle();
                    args.putInt("DAY", selectedDay);
                    args.putInt("MONTH", selectedMonth);
                    args.putInt("YEAR", selectedYear);

                    args.putInt(HoraMuhurtaFragment.ARG_POSITION, position);
                    fragment.setArguments(args);
                    return fragment;

            }
        }*/
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       // if (langCode.contains("or")) {
            if (!CalendarWeatherApp.isPanchangEng) {
                switch (position) {
                    case 0:

                        return mRes.getString(R.string.l_pager_title1);
                    case 1:
                        return mRes.getString(R.string.l_pager_title2);
                    case 2:
                        return mRes.getString(R.string.l_pager_title3);
                    case 3:
                        return mRes.getString(R.string.l_pager_title4);
                    case 4:
                        return mRes.getString(R.string.l_pager_title5);
                    case 5:
                        return mRes.getString(R.string.l_pager_title6);

                    default:
                        return "Ausp Work";
                }
            } else {
                switch (position) {
                    case 0:
                        return "Daily\nPanchang";
                    case 1:
                        return "Tara\nBalam";
                    case 2:
                        return "Chandra\nBalam";
                    case 3:
                        return "Chou\nGhadi";
                    case 4:
                        return "Hora\nMuhurta";
                    case 5:
                        return "Shubha\nKarya";
                    default:
                        return "Ausp Work";
                }
            }
       /* } else {
            if (!CalendarWeatherApp.isPanchangEng) {
                switch (position) {
                    case 0:
                        return mRes.getString(R.string.pager_title1);
                    case 1:
                        return mRes.getString(R.string.pager_title4);
                    case 2:
                        return mRes.getString(R.string.pager_title5);

                    default:
                        return "Ausp Work";
                }
            } else {
                switch (position) {
                    case 0:
                        return "Daily\nPanchang";

                    case 1:
                        return "Chou\nGhadi";
                    case 2:
                        return "Hora\nMuhurta";

                    default:
                        return "Ausp Work";
                }
            }
        }*/


    }


    @Override
    public int getCount() {
       /* if(CalendarWeatherApp.ForTesting){
            if (langCode.contains("or"))
                return 6;
            else
                return 3;
        }else {*/
           // if (langCode.contains("or") || langCode.contains("hi") || langCode.contains("mr"))
                return 5;
       // return 6;
          //  else
            //    return 3;
       // }
    }
}
