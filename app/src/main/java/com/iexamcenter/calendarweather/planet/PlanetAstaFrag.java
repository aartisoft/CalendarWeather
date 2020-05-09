package com.iexamcenter.calendarweather.planet;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlanetAstaFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    int selYear, selMonth, selDate, selHour, selMin;

    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    private MainActivity mContext;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    RecyclerView planetinfo;
    PlanetAstaListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    String pkey_ghara;
    ArrayList<ArrayList<EphemerisEntity>> addAllAsta;


    public static PlanetAstaFrag newInstance() {

        return new PlanetAstaFrag();
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
            TextView grahatxt= rootView.findViewById(R.id.graha);
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


            astaAll = new ArrayList<>();
            addAllAsta=new ArrayList<>();
            planetinfo = rootView.findViewById(R.id.planetinfo);
            progressBar = rootView.findViewById(R.id.progressbar);
            mPlanetInfoAdapter = new PlanetAstaListAdapter(mContext, addAllAsta);
            LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            planetinfo.setLayoutManager(riseSetll);
            planetinfo.setHasFixedSize(false);
            planetinfo.setAdapter(mPlanetInfoAdapter);

          //  String[] displayValues1 = new String[]{"MOON", "MERCURY", "VENUS", "MARS", "JUPITER", "SATURN", "URANAS", "NEPTUNE", "PLUTO"};



            String graha;
            String[] planetList,planetArr = new String[]{"SUN", "MERCURY", "VENUS", "MARS", "JUPITER", "SATURN", "URANAS", "NEPTUNE", "PLUTO"};
            if (!CalendarWeatherApp.isPanchangEng) {
                graha=mContext.getResources().getString(R.string.l_planet_graha);

                planetList = mContext.getResources().getStringArray(R.array.l_arr_planet);
            }else {
                graha=mContext.getResources().getString(R.string.e_planet_graha);
                planetList = mContext.getResources().getStringArray(R.array.e_arr_planet);
            }
            for (int i = 0, j = 0; i < planetList.length; i++) {
                if (i != 0 && i < 10) {
                    planetArr[j++] = planetList[i];
                }
            }
            grahatxt.setText(""+graha.toUpperCase());



            LinearLayout planetCntr = rootView.findViewById(R.id.planetCntr);

            NumberPicker planetPicker = rootView.findViewById(R.id.np2);

            planetPicker.setMaxValue(8);
            planetPicker.setMinValue(0);
            planetPicker.setWrapSelectorWheel(true);
            planetPicker.setDisplayedValues(planetArr);
            planetPicker.setValue(0);
            planetCntr.setVisibility(View.VISIBLE);



            planetPicker.setOnScrollListener((numberPicker, scrollState) -> {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    int planetIndex = planetPicker.getValue();
                    planetinfo.scrollToPosition(planetIndex);

                }
            });




            NumberPicker np = rootView.findViewById(R.id.np);
            np.setMinValue(1900);
            np.setMaxValue(2100);
            np.setValue(selYear);
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    //Display the newly selected number from picker
                    Log.e("Selected:", "SelectedSelected:" + newVal);
                    selYear = newVal;
                    observerPlanetInfo();
                }
            });

            observerPlanetInfo();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void observerPlanetInfo() {
        viewModel.getYearlyPlanetInfo(selYear).removeObservers(this);
        viewModel.getYearlyPlanetInfo(selYear).observe(this, obj -> {
            Log.e("startDate", "startDate:11:" +selYear+ obj.size());

            new doBgTask(obj).execute();
        });

    }
    private class doBgTask extends AsyncTask<Void, Integer, Long> {
        List<EphemerisEntity> obj;

        private doBgTask(List<EphemerisEntity> obj1) {
            obj = obj1;
        }

        @Override
        protected Long doInBackground(Void... voids) {

            calculateAsta(obj);
            return 1L;
        }

        protected void onPostExecute(Long result) {
            if(mPlanetInfoAdapter!=null)
                mPlanetInfoAdapter.notifyDataSetChanged();
        }

    }
    public void calculateAsta(List<EphemerisEntity> obj) {
        resetAll();
        if (obj != null && obj.size() > 0) {

            int size = obj.size();
            for (int i = 0; i < size; i++) {
                if (i == 0)
                    continue;
             //   String day = obj.get(i).day;
              //  String month = obj.get(i).month;
               // String year = obj.get(i).year;
                EphemerisEntity row = obj.get(i);

                isPlanetAsta(obj, i, 2, row.moon, row.dmmoon);
                isPlanetAsta(obj, i, 3, row.mercury, row.dmmercury);
                isPlanetAsta(obj, i, 4, row.venus, row.dmvenus);
                isPlanetAsta(obj, i, 5, row.mars, row.dmmars);
                isPlanetAsta(obj, i, 6, row.jupitor, row.dmjupitor);
                isPlanetAsta(obj, i, 7, row.saturn, row.dmsaturn);
                isPlanetAsta(obj, i, 8, row.uranus, row.dmuranus);
                isPlanetAsta(obj, i, 9, row.neptune, row.dmneptune);
                isPlanetAsta(obj, i, 10, row.pluto, row.dmpluto);

            }


          /*  addAstaAll(alMercury, 3);
            addAstaAll(alVenus, 4);
            addAstaAll(alMars, 5);
            addAstaAll(alJupiter, 6);
            addAstaAll(alSaturn, 7);
            addAstaAll(alMoon, 2);*/

            addAllAsta.clear();
            addAllAsta.add(alMoon);
            addAllAsta.add(alMercury);
            addAllAsta.add(alVenus);
            addAllAsta.add(alMars);
            addAllAsta.add(alJupiter);
            addAllAsta.add(alSaturn);
            addAllAsta.add(alUranus);
            addAllAsta.add(alNeptune);
            addAllAsta.add(alPluto);




            // setPlanetInfo(obj);
        }
    }



    private boolean isPlanetAsta(List<EphemerisEntity> obj, int i, int type, String planet, String dailymotion) {

        EphemerisEntity row = obj.get(i);
        String sun = row.sun;
        // planet=getPlanetDeg(row,type);, String dailymotion
        int degSun = Integer.parseInt(sun.split("_")[0]);
        int minSun = Integer.parseInt(sun.split("_")[1]);
        double minToSecSun = ((minSun * 60.0) / 3600.0);
        double degPlanetSun = degSun + minToSecSun;

        int deg = Integer.parseInt(planet.split("_")[0]);
        int min = Integer.parseInt(planet.split("_")[1]);
        double minToSec = ((min * 60.0) / 3600.0);
        double degPlanet = deg + minToSec;


        String dir = "F";
        if (type != 2)
            dir = dailymotion.substring(0, 1);
        // String dm = dailymotion.substring(1);

        boolean isPlanetAsta = Math.abs(degPlanet - degPlanetSun) <= getAngle(dir, type);
        if (isPlanetAsta && !getAdded(type)) {

            // isRecord = true;
            setAdded(type, true);
            // String data = row.day + "-" + row.month + "-" + row.year + ":" + row.sun + ":::" + planet + ":X::" + dailymotion;
            //  Log.e("JUPITORVAKRA", "datadata:" + data);
            // al.add(row);
            setArrayList(type, row);
        }
        if (!isPlanetAsta && getAdded(type)) {
            //String data = rowPrev.day + "-" +rowPrev.month + "-" + rowPrev.year + ":" + rowPrev.sun + ":Y::" + rowPrev.mercury+":::"+rowPrev.dmmercury;
            // Log.e("JUPITORVAKRA", "datadata:" + data);
            //al.add(obj.get(i - 1));
            setArrayList(type, obj.get(i - 1));
            // isRecord = false;
            setAdded(type, false);
        }
        return false;
    }

    private Boolean isMoonAdded = false;
    private Boolean isMercuryAdded = false;
    private Boolean isVenusAdded = false;
    private Boolean isMarsAdded = false;
    private Boolean isJupiterAdded = false;
    private Boolean isSaturnAdded = false;
    private Boolean isUranusAdded = false;
    private Boolean isNeptuneAdded = false;
    private Boolean isPlutoAdded = false;

    private ArrayList<EphemerisEntity> alMoon = new ArrayList<>();
    private ArrayList<EphemerisEntity> alMercury = new ArrayList<>();
    private ArrayList<EphemerisEntity> alVenus = new ArrayList<>();
    private ArrayList<EphemerisEntity> alMars = new ArrayList<>();
    private ArrayList<EphemerisEntity> alJupiter = new ArrayList<>();
    private ArrayList<EphemerisEntity> alSaturn = new ArrayList<>();
    private ArrayList<EphemerisEntity> alUranus = new ArrayList<>();
    private ArrayList<EphemerisEntity> alNeptune = new ArrayList<>();
    private ArrayList<EphemerisEntity> alPluto = new ArrayList<>();

    private void setArrayList(int type, EphemerisEntity obj) {
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

    private Boolean getAdded(int type) {
        switch (type) {
            case 2:
                return isMoonAdded;
            case 3:
                return isMercuryAdded;
            case 4:
                return isVenusAdded;
            case 5:
                return isMarsAdded;
            case 6:
                return isJupiterAdded;
            case 7:
                return isSaturnAdded;
            case 8:
                return isUranusAdded;
            case 9:
                return isNeptuneAdded;
            case 10:
                return isPlutoAdded;
            default:
                return false;


        }
    }

    private void setAdded(int type, boolean bool) {
        switch (type) {
            case 2:
                isMoonAdded = bool;
                break;
            case 3:
                isMercuryAdded = bool;
                break;
            case 4:
                isVenusAdded = bool;
                break;
            case 5:
                isMarsAdded = bool;
                break;
            case 6:
                isJupiterAdded = bool;
                break;
            case 7:
                isSaturnAdded = bool;
                break;
            case 8:
                isUranusAdded = bool;
                break;
            case 9:
                isNeptuneAdded = bool;
                break;
            case 10:
                isPlutoAdded = bool;
                break;


        }
    }

    private int getAngle(String dir, int type) {
        switch (type) {
            case 2:
                return 12;
            case 3:
                return (dir.contains("F")) ? 14 : 12;
            case 4:
                return (dir.contains("F")) ? 10 : 8;
            case 5:
                return 17;
            case 6:
                return 11;
            case 7:
                return 15;
            case 8:
                return (dir.contains("F")) ? 8 : 10;
            case 9:
                return (dir.contains("F")) ? 8 : 10;
            case 10:
                return (dir.contains("F")) ? 8 : 10;


        }

        return 0;

    }

    private void resetAll() {
     //   al = new ArrayList<>();
       // astaAll.clear();
        addAllAsta.clear();
        alMoon.clear();
        alMercury.clear();
        alVenus.clear();
        alMars.clear();
        alJupiter.clear();
        alSaturn.clear();
        alUranus.clear();
        alNeptune.clear();
        alPluto.clear();
        isMoonAdded = false;
        isMercuryAdded = false;
        isVenusAdded = false;
        isMarsAdded = false;
        isJupiterAdded = false;
        isSaturnAdded = false;
        isUranusAdded = false;
        isNeptuneAdded = false;
        isPlutoAdded = false;


    }

    ArrayList<String> astaAll;


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

