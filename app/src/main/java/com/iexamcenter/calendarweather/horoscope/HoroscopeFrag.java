package com.iexamcenter.calendarweather.horoscope;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.saxrssreader.RssFeed;
import com.iexamcenter.calendarweather.saxrssreader.RssItem;
import com.iexamcenter.calendarweather.saxrssreader.RssReader;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HoroscopeFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    ArrayList<horoscopeItem> horoscopeAl = new ArrayList<>();
    TextView msg;

    PrefManager mPref;
    String lang;

    HoroscopeListAdapter mMomentListAdapter;
    RashiListAdapter mRashiListAdapter;
    RecyclerView momentListView, rashiList;
    private String JSONFILE = "ONTHISDAYHOROSCOPE.txt";
    private MainActivity mContext;

    public static HoroscopeFrag newInstance() {

        return new HoroscopeFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {

            Resources res = mContext.getResources();
            String[] rasi_kundali_arr = res.getStringArray(R.array.e_arr_rasi_kundali);

            String[] en_rasi_kundali_arr = res.getStringArray(R.array.en_rasi_kundali_arr);

            String[] lrasi_kundali_arr = res.getStringArray(R.array.l_arr_rasi_kundali);

            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            //  txt1 = (TextView) rootView.findViewById(R.id.txt1);
            //   credit = (TextView) rootView.findViewById(R.id.credit);
            msg = rootView.findViewById(R.id.pick_my_city);
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mContext.refreshWhatToday();
                }
            });
            horoscopeAl = new ArrayList<>();
            momentListView = rootView.findViewById(R.id.rashiphalaList);
            rashiList = rootView.findViewById(R.id.rashiList);
            mRashiListAdapter = new RashiListAdapter(mContext, momentListView);
            // LinearLayoutManager glm = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
            GridLayoutManager glm = new GridLayoutManager(getActivity(), 6);

            rashiList.setLayoutManager(glm);
            rashiList.setHasFixedSize(false);
            rashiList.setAdapter(mRashiListAdapter);


            mMomentListAdapter = new HoroscopeListAdapter(mContext, horoscopeAl, rasi_kundali_arr, en_rasi_kundali_arr, lrasi_kundali_arr);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            momentListView.setLayoutManager(llm);
            momentListView.setHasFixedSize(false);
            momentListView.setAdapter(mMomentListAdapter);
            horoscopeAl.clear();
            if (!Connectivity.isConnected(mContext)) {
                Utility.getInstance(mContext).newToast("Please check internet connection.");
                // getLoaderManager().restartLoader(URL_LOADER, null, HoroscopeFrag.this);
                loadHoroscope();

            } else {
                new DownloadFilesTask().execute();
            }
            // loadHoroscope();
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Connectivity.isConnected(mContext))
                        new DownloadFilesTask().execute();
                    else
                        Utility.getInstance(mContext).newToast("Please check internet connection.");

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void loadHoroscope() {
        horoscopeAl.clear();
        String horoscopeData = Utility.getInstance(mContext).readFromFile(mContext, JSONFILE);
        if (!horoscopeData.isEmpty()) {
            String[] horoscopeItemArr = horoscopeData.split("@@@");
            for (String horoscopeStr : horoscopeItemArr) {

                String[] horoscopeArr = horoscopeStr.split("__iec__");
                horoscopeItem horoscopeItem = new horoscopeItem();
                horoscopeItem.date = horoscopeArr[1];
                horoscopeItem.title = horoscopeArr[0];
                horoscopeItem.desc = horoscopeArr[2];

                horoscopeAl.add(horoscopeItem);
            }
            mMomentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.frag_horoscope, null);
        setHasOptionsMenu(true);
        return rootView;

    }


    private class DownloadFilesTask extends AsyncTask<Void, Void, Long> {
        protected Long doInBackground(Void... urls) {
            CalendarWeatherApp.updateAppResource(getResources(), mContext);

            URL url = null;
            RssFeed feed = new RssFeed();
            try {
                Log.e("HOROSCOPE_RSS", "HOROSCOPE_RSS:" + Constant.HOROSCOPE_RSS);
                url = new URL(Constant.HOROSCOPE_RSS);
                feed = RssReader.read(url);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (feed != null) {
                ArrayList<RssItem> rssItems = feed.getRssItems();
                if (rssItems.size() > 0) {
                    String horoscopeData = "";
                    for (RssItem rssItem : rssItems) {
                        String desc = rssItem.getDescription();
                        String title = rssItem.getTitle();
                        desc = Jsoup.parse(desc).text();
                        desc = desc.replace("AstroSage.com,", "");
                        desc = desc.replace("astrosage.com,", "");
                        desc = desc.replace("www.AstroSage.com,", "");
                        desc = desc.replace("www.astrosage.com,", "");
                        String date = rssItem.getPubDate().toString();
                        horoscopeData = horoscopeData + title + "__iec__" + date + "__iec__" + desc + "@@@";
                    }
                    if (!horoscopeData.isEmpty())
                        Utility.getInstance(mContext).writeToFile(horoscopeData, mContext, JSONFILE);


                }
            }
            return 1L;
        }


        protected void onPostExecute(Long feed) {
            try {
                CalendarWeatherApp.updateAppResource(getResources(), mContext);
                loadHoroscope();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    public static class horoscopeItem {
        public String title, desc, date;
        public boolean isCurr = false;
    }
}

