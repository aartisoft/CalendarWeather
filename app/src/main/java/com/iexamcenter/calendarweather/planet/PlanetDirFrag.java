package com.iexamcenter.calendarweather.planet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class PlanetDirFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    int selYear, selMonth, selDate, selHour, selMin;
    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView txt;
    private MainActivity mContext;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    RecyclerView planetinfo;
    PlanetDirListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    String pkey_ghara;
    ArrayList<ArrayList<String>> addAllRetro;//=new ArrayList<>();

    public static PlanetDirFrag newInstance() {

        return new PlanetDirFrag();
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

            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            lrashiList = mContext.getResources().getStringArray(R.array.l_arr_rasi_kundali);
            rashiList = mContext.getResources().getStringArray(R.array.e_arr_rasi_kundali);
            erashiList = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);

            eplanetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            lplanetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            pkey_ghara = mContext.getResources().getString(R.string.l_ghara);
            Calendar cal = Calendar.getInstance();
            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);

            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            //retroAll = new ArrayList<>();
            addAllRetro = new ArrayList<>();
            planetinfo = rootView.findViewById(R.id.planetinfo);
            progressBar = rootView.findViewById(R.id.progressbar);
            mPlanetInfoAdapter = new PlanetDirListAdapter(mContext, addAllRetro);
            LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            planetinfo.setLayoutManager(riseSetll);
            planetinfo.setHasFixedSize(false);
            planetinfo.setAdapter(mPlanetInfoAdapter);
            NumberPicker np = rootView.findViewById(R.id.np);
            np.setMinValue(1900);
            np.setMaxValue(2100);
            np.setValue(selYear);
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    selYear = newVal;
                    handleRetroData();
                }
            });

            handleRetroData();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void handleRetroData() {
        try {
            // retroAll.clear();
            resetAll();
            callRetrograde("RetroMercury", 3);
            callRetrograde("RetroVenus", 4);
            callRetrograde("RetroMars", 5);
            callRetrograde("RetroJupiter", 6);
            callRetrograde("RetroSaturn", 7);
            callRetrograde("RetroUranus", 8);
            callRetrograde("RetroNeptune", 9);
            callRetrograde("RetroPluto", 10);

            addAllRetro.clear();

            addAllRetro.add(alMercury);
            addAllRetro.add(alVenus);
            addAllRetro.add(alMars);
            addAllRetro.add(alJupiter);
            addAllRetro.add(alSaturn);
            addAllRetro.add(alUranus);
            addAllRetro.add(alNeptune);
            addAllRetro.add(alPluto);

            mPlanetInfoAdapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ArrayList<String> retroAll;

    public void callRetrograde(String file, int type) throws IOException {


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(file + ".txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] rangeDateArr = mLine.split(" ");
                String minRange = rangeDateArr[0];

                if (minRange.contains("" + selYear)) {

                    //  retroAll.add(mLine+" "+type);
                    setArrayList(type, mLine + " " + type);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private ArrayList<String> alMoon = new ArrayList<>();
    private ArrayList<String> alMercury = new ArrayList<>();
    private ArrayList<String> alVenus = new ArrayList<>();
    private ArrayList<String> alMars = new ArrayList<>();
    private ArrayList<String> alJupiter = new ArrayList<>();
    private ArrayList<String> alSaturn = new ArrayList<>();
    private ArrayList<String> alUranus = new ArrayList<>();
    private ArrayList<String> alNeptune = new ArrayList<>();
    private ArrayList<String> alPluto = new ArrayList<>();

    private void resetAll() {

        addAllRetro.clear();
        alMercury.clear();
        alVenus.clear();
        alMars.clear();
        alJupiter.clear();
        alSaturn.clear();
        alUranus.clear();
        alNeptune.clear();
        alPluto.clear();


    }

    private void setArrayList(int type, String obj) {
        switch (type) {
            case 2:
                alMoon.add(obj);
                break;
            case 3:
                alMercury.add(obj);
                break;
            case 4:
                alVenus.add(obj);
                break;
            case 5:
                alMars.add(obj);
                break;
            case 6:
                alJupiter.add(obj);
                break;
            case 7:
                alSaturn.add(obj);
                break;
            case 8:
                alUranus.add(obj);
                break;
            case 9:
                alNeptune.add(obj);
                break;
            case 10:
                alPluto.add(obj);
                break;


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.frag_planet_dir, null);
        setHasOptionsMenu(true);


        return rootView;

    }


}

