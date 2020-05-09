package com.iexamcenter.calendarweather.planet;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlanetTransFrag extends Fragment {

    public static final String ARG_POSITION = "POSITION";
    int selYear, selMonth, selDate, selHour, selMin;
    TextView msg;
    MainViewModel viewModel;
    PrefManager mPref;
    String lang;
    TextView grahatxt;
    private MainActivity mContext;
    //ArrayList<ArrayList<String>> planetDataList;
    String[] rashiList, lrashiList, erashiList, eplanetList, lplanetList;
    RecyclerView planetinfo;
    PlanetTransListAdapter mPlanetInfoAdapter;
    ProgressBar progressBar;
    String pkey_ghara;
    ArrayList<EphemerisEntity> al;
    boolean isRecord = false;
    ArrayList<ArrayList<PlanetTransData>> addAllTrans;

    public static PlanetTransFrag newInstance() {

        return new PlanetTransFrag();
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

            pkey_ghara = mContext.getResources().getString(R.string.l_ghara);
            Calendar cal = Calendar.getInstance();
            selDate = cal.get(Calendar.DAY_OF_MONTH);
            selMonth = cal.get(Calendar.MONTH);
            selYear = cal.get(Calendar.YEAR);
            selHour = cal.get(Calendar.HOUR_OF_DAY);
            selMin = cal.get(Calendar.MINUTE);
            grahatxt= rootView.findViewById(R.id.graha);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            addAllTrans = new ArrayList<>();
            planetinfo = rootView.findViewById(R.id.planetinfo);
            progressBar = rootView.findViewById(R.id.progressbar);
            rootView.findViewById(R.id.planetCntr).setVisibility(View.VISIBLE);
            mPlanetInfoAdapter = new PlanetTransListAdapter(mContext, addAllTrans);
            LinearLayoutManager riseSetll = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            planetinfo.setLayoutManager(riseSetll);
            planetinfo.setHasFixedSize(false);
            planetinfo.setAdapter(mPlanetInfoAdapter);

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
                if (i != 1 && i < 10) {
                    planetArr[j++] = planetList[i];
                }
            }
            grahatxt.setText(""+graha.toUpperCase());
            NumberPicker planetPicker = rootView.findViewById(R.id.np2);
            planetPicker.setMaxValue(8);
            planetPicker.setMinValue(0);
            planetPicker.setWrapSelectorWheel(true);
            planetPicker.setDisplayedValues(planetArr);
            planetPicker.setValue(0);


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
            np.setWrapSelectorWheel(true);

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
            //  calculateTransition(obj);
            new doBgTask(obj).execute();
        });

    }

    public void calculateTransition(List<EphemerisEntity> obj) {
        resetAll();
        if (obj != null && obj.size() > 0) {

            int size = obj.size();
            for (int i = 0; i < size; i++) {
                String day = obj.get(i).day;
                String month = obj.get(i).month;
                String year = obj.get(i).year;
                if (i == 0)
                    continue;


                EphemerisEntity row = obj.get(i);
                EphemerisEntity rowPrev = obj.get(i - 1);
                planetTransition(obj, i, 1, row.sun, rowPrev.sun, rowPrev.dmsun);
                planetTransition(obj, i, 3, row.mercury, rowPrev.mercury, rowPrev.dmmercury);
                planetTransition(obj, i, 4, row.venus, rowPrev.venus, rowPrev.dmvenus);
                planetTransition(obj, i, 5, row.mars, rowPrev.mars, rowPrev.dmmars);
                planetTransition(obj, i, 6, row.jupitor, rowPrev.jupitor, rowPrev.dmjupitor);
                planetTransition(obj, i, 7, row.saturn, rowPrev.saturn, rowPrev.dmsaturn);
                planetTransition(obj, i, 8, row.uranus, rowPrev.uranus, rowPrev.dmuranus);
                planetTransition(obj, i, 9, row.neptune, rowPrev.neptune, rowPrev.dmneptune);
                planetTransition(obj, i, 10, row.pluto, rowPrev.pluto, rowPrev.dmpluto);

                if (Integer.parseInt(year) == (selYear + 1)) {
                    break;
                }
            }

            addAllTrans.add(alSun);
            addAllTrans.add(alMercury);
            addAllTrans.add(alVenus);
            addAllTrans.add(alMars);
            addAllTrans.add(alJupiter);
            addAllTrans.add(alSaturn);
            addAllTrans.add(alUranus);
            addAllTrans.add(alNeptune);
            addAllTrans.add(alPluto);
            Log.e("strstr", "addAllTrans:" + addAllTrans.size());
            Log.e("strstr", "addAllTrans:alPluto" + alPluto.size());
            Log.e("strstr", "addAllTrans:alNeptune" + alNeptune.size());
            Log.e("strstr", "addAllTrans:alUranus" + alUranus.size());
            Log.e("strstr", "addAllTrans:alSaturn" + alSaturn.size());
            Log.e("strstr", "addAllTrans:alJupiter" + alJupiter.size());
            Log.e("strstr", "addAllTrans:alMars" + alMars.size());
            // addAllTrans.add(alSun);


            // setPlanetInfo(obj);
        }
    }


    private void planetTransition(List<EphemerisEntity> obj, int i, int type, String planet, String planetPrev, String prevDailymotion) {

        //  EphemerisEntity row = obj.get(i);
        EphemerisEntity rowPrev = obj.get(i - 1);

        int deg = Integer.parseInt(planet.split("_")[0]);
        int min = Integer.parseInt(planet.split("_")[1]);
        double minToSec = ((min * 60.0) / 3600.0);
        double degPlanet = deg + minToSec;
        int index = (int) (degPlanet / 30);

        int deg1 = Integer.parseInt(planetPrev.split("_")[0]);
        int min1 = Integer.parseInt(planetPrev.split("_")[1]);
        double minToSec1 = ((min1 * 60.0) / 3600.0);
        double degPlanetPrev = deg1 + minToSec1;
        int indexPrev = (int) (degPlanetPrev / 30);
        double remDeg;
        double dm = Double.parseDouble(prevDailymotion.substring(1));

        if (!prevDailymotion.substring(0, 1).contains("F")) {
            dm = 360 - Double.parseDouble(prevDailymotion.substring(1));
        }
        if (type < 3) {
            dm = Double.parseDouble(prevDailymotion);
        }
        PlanetTransData planetTransObj = new PlanetTransData();
        if ((index - indexPrev) == 1) {
            remDeg = ((index * 30) - degPlanetPrev);
            // double dm = degPlanet - degPlanetPrev;

            double timereq = (24 / dm) * remDeg;
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.set(Integer.parseInt(rowPrev.year), Integer.parseInt(rowPrev.month), Integer.parseInt(rowPrev.day), 5, 30, 0);
            long timeInMilli = cal.getTimeInMillis() + (long) (timereq * 60 * 60 * 1000.0);

            cal.setTimeInMillis(timeInMilli);
           /* int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int date = cal.get(Calendar.DAY_OF_MONTH);
            int hrOfday = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);*/

            planetTransObj.prevRashi = indexPrev;
            planetTransObj.nextRashi = index;
            planetTransObj.date = cal;
            planetTransObj.type = type;
            setArrayList(type, planetTransObj);
        } else if ((index - indexPrev) == -1) {
            remDeg = (degPlanetPrev - (indexPrev * 30));
            double timereq = (24 / dm) * remDeg;

            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(rowPrev.year), Integer.parseInt(rowPrev.month), Integer.parseInt(rowPrev.day), 5, 30, 0);
            long timeInMilli = cal.getTimeInMillis() + (long) (timereq * 60 * 60 * 1000.0);
            cal.setTimeInMillis(timeInMilli);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int date = cal.get(Calendar.DAY_OF_MONTH);
            int hrOfday = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
           // Log.e("TransDeg", ":TIMEREQ:Y:" + timereq + "::index:" + index + "::planet" + rowPrev.mercury + "::CurPlanet:" + planet + ":DATE:" + year + "-" + month + "-" + date + " " + hrOfday + ":" + minute);

            planetTransObj.prevRashi = indexPrev;
            planetTransObj.nextRashi = index;
            planetTransObj.date = cal;
            planetTransObj.type = type;
            setArrayList(type, planetTransObj);


        } else if ((index - indexPrev) == -11) {
            // bckward
            remDeg = (360 - degPlanetPrev);// - (indexPrev * 30));
            double timereq = (24 / dm) * remDeg;

            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(rowPrev.year), Integer.parseInt(rowPrev.month), Integer.parseInt(rowPrev.day), 5, 30, 0);
            long timeInMilli = cal.getTimeInMillis() + (long) (timereq * 60 * 60 * 1000.0);
            cal.setTimeInMillis(timeInMilli);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int date = cal.get(Calendar.DAY_OF_MONTH);
            int hrOfday = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
           // Log.e("TransDeg", ":TIMEREQ:ZZ:" + timereq + "::index:" + index + "::planet" + rowPrev.mercury + "::CurPlanet:" + planet + ":DATE:" + year + "-" + month + "-" + date + " " + hrOfday + ":" + minute);

            planetTransObj.prevRashi = indexPrev;
            planetTransObj.nextRashi = index;
            planetTransObj.date = cal;
            planetTransObj.type = type;
            setArrayList(type, planetTransObj);

        } else if ((index - indexPrev) > 1) {
           // Log.e("TransDeg", ":TIMEREQ:A:AAAA:" + indexPrev + ":" + index + "::planet" + rowPrev.mercury + "::DM:" + rowPrev.dmmercury + ":DATE:" + rowPrev.year + "-" + rowPrev.month + "-" + rowPrev.day);
            planetTransObj.prevRashi = 1;
            planetTransObj.nextRashi = 1;
            planetTransObj.date = Calendar.getInstance();
            planetTransObj.type = 1;
            setArrayList(type, planetTransObj);
        }


    }


    private ArrayList<PlanetTransData> alSun = new ArrayList<>();
    private ArrayList<PlanetTransData> alMercury = new ArrayList<>();
    private ArrayList<PlanetTransData> alVenus = new ArrayList<>();
    private ArrayList<PlanetTransData> alMars = new ArrayList<>();
    private ArrayList<PlanetTransData> alJupiter = new ArrayList<>();
    private ArrayList<PlanetTransData> alSaturn = new ArrayList<>();
    private ArrayList<PlanetTransData> alUranus = new ArrayList<>();
    private ArrayList<PlanetTransData> alNeptune = new ArrayList<>();
    private ArrayList<PlanetTransData> alPluto = new ArrayList<>();

    private void setArrayList(int type, PlanetTransData obj) {
        switch (type) {
            case 1:
                alSun.add(obj);
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


    private void resetAll() {
        addAllTrans.clear();

        alSun.clear();
        alMercury.clear();
        alVenus.clear();
        alMars.clear();
        alJupiter.clear();
        alSaturn.clear();
        alUranus.clear();
        alNeptune.clear();
        alPluto.clear();

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


    public static class PlanetTransData {
        public int prevRashi, nextRashi, type;
        public Calendar date;


    }

    private class doBgTask extends AsyncTask<Void, Integer, Long> {
        List<EphemerisEntity> obj;

        private doBgTask(List<EphemerisEntity> obj1) {
            obj = obj1;
        }

        @Override
        protected Long doInBackground(Void... voids) {

            calculateTransition(obj);
            return 1L;
        }

        protected void onPostExecute(Long result) {
            if (mPlanetInfoAdapter != null)
                mPlanetInfoAdapter.notifyDataSetChanged();
        }

    }

}

