package com.iexamcenter.calendarweather.thisday;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class ProfileDeatilsFragment extends Fragment {

    PrefManager mPref;

    String pkey,name, fullName, bDay, bPlace, dDay, jobTitle, famousFor, history, imageLink, wikiLink;
    private String oldTitle="";
    private String oldSubTitle="";

    static private MainActivity mContext;
    View rootView;

    public static ProfileDeatilsFragment newInstance() {

        return new ProfileDeatilsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();

        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        mContext.showHideToolBarView(true);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.toolbar.setTitle(oldTitle);
        mContext.toolbar.setSubtitle(oldSubTitle);
        mContext.showHideBottomNavigationView(true);
        mContext.showHideToolBarView(false);

    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            CalendarWeatherApp.updateAppResource(getResources(), mContext);
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            Bundle b = getArguments();
            pkey = b.getString("PKEY");
            name = b.getString("NAME");
            fullName = b.getString("FULLNAME");
            bDay = b.getString("BORN");
            bPlace = b.getString("BORNPLACE");
            dDay = b.getString("DEATH");
            jobTitle = b.getString("JOBTITLE");
            famousFor = b.getString("FAMOUSFOR");
            history = b.getString("HISTORY");
            imageLink = b.getString("IMAGELINK");
            wikiLink = b.getString("WIKI");

            oldTitle=mContext.toolbar.getTitle().toString();
            oldSubTitle=mContext.toolbar.getSubtitle().toString();


            mContext.toolbar.setTitle(name);
            mContext.toolbar.setSubtitle("Famous People");
            createMyView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.frag_profile_details, container, false);
        return rootView;
    }

    private void createMyView() {
        TextView nameTV, fullnameTV, bdayTV, bplaceTV, ddayTV, jobtitleTV, famousforTV, historyTitleTV;
        nameTV = rootView.findViewById(R.id.name);
        fullnameTV = rootView.findViewById(R.id.fullname);
        bdayTV = rootView.findViewById(R.id.bday);
        bplaceTV = rootView.findViewById(R.id.bplace);
        ddayTV = rootView.findViewById(R.id.dday);
        jobtitleTV = rootView.findViewById(R.id.jobtitle);
        famousforTV = rootView.findViewById(R.id.famousfor);
       // historyTitleTV = rootView.findViewById(R.id.historyTitle);
        ImageView profileImage = rootView.findViewById(R.id.profileImage);

        nameTV.setText(name);
        if (!fullName.isEmpty())
            fullnameTV.setText(Html.fromHtml("<b>Full Name: </b> " + fullName));
        else
            fullnameTV.setVisibility(View.GONE);
        if (!bDay.isEmpty()) {

            if(dDay.trim().isEmpty()) {
                bdayTV.setText(Html.fromHtml("<b>Born:</b> "+bDay+"("+getAge(bDay) + " years old)"));
            }else {
                bdayTV.setText(Html.fromHtml("<b>Born:</b> "+ bDay));
            }
        }
        else
            bdayTV.setVisibility(View.GONE);
        if (!bPlace.isEmpty())
            bplaceTV.setText(Html.fromHtml("<b>Birthplace:</b> "+bPlace));
        else
            bplaceTV.setVisibility(View.GONE);
        if (!dDay.isEmpty()) {
            ddayTV.setText(Html.fromHtml("<b>Died:</b> "+dDay+"(aged "+getDiedAge(bDay,dDay)+")"));

        }
        else
            ddayTV.setVisibility(View.GONE);
        if (!jobTitle.isEmpty())
            jobtitleTV.setText(Html.fromHtml("<b>Profession:</b> "+jobTitle));
        else
            jobtitleTV.setVisibility(View.GONE);

        if (!famousFor.isEmpty())
            famousforTV.setText(Html.fromHtml("<br/><b>Why Famous:</b><br/>"+famousFor));
        else
            famousforTV.setVisibility(View.GONE);

      /*  if (!history.isEmpty())
            historyTitleTV.setText(Html.fromHtml("<br/><b>Historical Events in the Life of "+name +":</b><br/>"+history.replace("\n\n","<br/><br/>")));
        else
            historyTitleTV.setVisibility(View.GONE);
            */


        String IMAGE_URL = mPref.getThisDayImageBaseUrl() + imageLink;

        if (!IMAGE_URL.isEmpty())
            Glide.with(mContext).load(IMAGE_URL).into(profileImage);




        ViewPager tabViewPager = rootView.findViewById(R.id.tabViewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(FamousPeoplePagerAdapter.newInstance(getChildFragmentManager(), mContext,history,pkey));




    }
    private String getAge(String bDay){
        // String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(bDay);
        } catch (ParseException e) {

            e.printStackTrace();
            return "";
        }
        long timeInMillis = date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis (timeInMillis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private String getDiedAge(String bDay,String dDay){
if(!bDay.isEmpty())

        return ""+(Integer.parseInt(getAge(bDay))-Integer.parseInt(getAge(dDay)));
else
    return "-";
    }



}
