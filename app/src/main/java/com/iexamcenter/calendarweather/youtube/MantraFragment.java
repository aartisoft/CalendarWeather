package com.iexamcenter.calendarweather.youtube;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class MantraFragment extends Fragment {
    public static String ARG_POSITION;

    ViewGroup rootView;
    RecyclerView recyclerView;
    int num;
    int pageType = 1;
    MainActivity activity;
    ArrayList<YoutubeList> hl;
    PrefManager mPref;
    MantraListAdapter mAdapter;

    public static MantraFragment newInstance() {

        return new MantraFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_omg_mantra, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        setUp(rootView);
        return rootView;
    }

    public void refreshPage() {

        new DownloadFilesTask().execute("", "", "");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refreshPage();
                Log.e("REFRESH", "REFRESH:PRAYER");
                return false;
        }
        return false;
    }


    protected void setUp(View view) {
        new DownloadFilesTask().execute("", "", "");
        hl = new ArrayList<>();


        RecyclerView listview = view.findViewById(R.id.video);
        mAdapter = new MantraListAdapter(activity, hl);
        listview.setLayoutManager(new GridLayoutManager(activity, 3));
        listview.setHasFixedSize(true);
        listview.setAdapter(mAdapter);

    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, ArrayList<YoutubeList>> {
        protected ArrayList<YoutubeList> doInBackground(String... urls) {
            Log.e("xx","xxx:x:"+System.currentTimeMillis());
            String[] l_ENG_arr = getResources().getStringArray(R.array.youtube);
            ArrayList<YoutubeList> hll = new ArrayList<>();
            for (int i = 0; i < l_ENG_arr.length; i++) {
                JSONObject festReader = null;
                try {
                    festReader = new JSONObject(l_ENG_arr[i]);

                    String key = festReader.getString("key");
                    String titleL = festReader.getString("titleL");
                    String titleE = festReader.getString("titleE");
                    String lang = festReader.getString("lang");
                    YoutubeList obj = new YoutubeList();
                    obj.vKey = key;
                    obj.lTitle = titleL;
                    obj.eTitle = titleE;
                    if (lang.contains("all") || lang.contains(mPref.getMyLanguage()))
                        hll.add(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.e("xx","xxx:x:"+System.currentTimeMillis());
            return hll;
        }

        protected void onPostExecute(ArrayList<YoutubeList> result) {
            try {
                if (hl != null) {
                    hl.clear();
                    hl.addAll(result);
                }
                mAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class YoutubeList {
        public String vKey, lTitle, eTitle;
    }
}









