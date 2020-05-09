package com.iexamcenter.calendarweather.panchang;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.AuspData;
import com.iexamcenter.calendarweather.mydata.AuspWork;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class AuspWorkListAdapter extends RecyclerView.Adapter<AuspWorkListAdapter.ItemViewHolder> {
    Context mContext;
    HashMap<String, CoreDataHelper> mItems;
    Resources res;

    PrefManager mPref;
    boolean isFirstTime = true;
    int mMonthIndex, year, month, dayofmonth, mPagePos;
    Resources resource;
    int daysInMonth = 0;
    String grahabhoga, arambha, sesha, pkey_sunset, pkey_sunrise, dayHora, nightHora, mLang, pkey_time_to, pkey_time_next, epkey_nakshatra_byakti, pkey_nakshatra_byakti;
    private String[] hora_arr, enakshatra_arr;
    Resources mRes;
    ArrayList<AuspData> auspData;

    public AuspWorkListAdapter(Context context, HashMap<String, CoreDataHelper> myPanchangHashMap, HashMap<String, ArrayList<String>> monthFestivalmap, int monthIndex, int myear, int mday, int pagePos) {

        auspData = AuspWork.setAuspData();
        mContext = context;
        mPagePos = pagePos;
        mRes = mContext.getResources();

        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        mLang = mPref.getMyLanguage();
        if (!CalendarWeatherApp.isPanchangEng) {
            //enakshatra_arr = mContext.getResources().getStringArray(R.array.en_rasi_kundali_arr);
            hora_arr = mContext.getResources().getStringArray(R.array.l_arr_hora);
            //tarabalam_arr = mContext.getResources().getStringArray(R.array.l_tarabalam_arr);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);

            dayHora = mContext.getResources().getString(R.string.l_dayhora);
            nightHora = mContext.getResources().getString(R.string.l_nighthora);
            pkey_sunrise = mContext.getResources().getString(R.string.l_sunrise);
            pkey_sunset = mContext.getResources().getString(R.string.l_sunset);
            grahabhoga = mContext.getResources().getString(R.string.l_grahabhoga);
            arambha = mContext.getResources().getString(R.string.l_arambha);
            sesha = mContext.getResources().getString(R.string.l_sesha);
        } else {
            Configuration conf = mRes.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = Locale.ENGLISH; // whatever you want here
            mRes.updateConfiguration(conf, null); // second arg null means don't change

            hora_arr = mContext.getResources().getStringArray(R.array.l_arr_hora);
            //tarabalam_arr = mContext.getResources().getStringArray(R.array.l_tarabalam_arr);
            pkey_time_to = mContext.getResources().getString(R.string.l_time_to);
            pkey_time_next = mContext.getResources().getString(R.string.l_time_next);
            epkey_nakshatra_byakti = mContext.getResources().getString(R.string.e_ghatarashi);
            pkey_nakshatra_byakti = mContext.getResources().getString(R.string.l_ghatarashi);

            dayHora = mContext.getResources().getString(R.string.l_dayhora);
            nightHora = mContext.getResources().getString(R.string.l_nighthora);
            pkey_sunrise = mContext.getResources().getString(R.string.l_sunrise);
            pkey_sunset = mContext.getResources().getString(R.string.l_sunset);

            grahabhoga = mContext.getResources().getString(R.string.l_grahabhoga);
            arambha = mContext.getResources().getString(R.string.l_arambha);
            sesha = mContext.getResources().getString(R.string.l_sesha);
// restore original locale
            conf.locale = savedLocale;
            mRes.updateConfiguration(conf, null);

        }


        mMonthIndex = monthIndex;
        year = myear;
        month = monthIndex;
        dayofmonth = mday;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayofmonth);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        Calendar mycal = new GregorianCalendar(year, month, dayofmonth);

        daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28


        mItems = myPanchangHashMap;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.inflate_tarabalamlist;


        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(AuspWorkListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);


    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        try {
            setPanchnang(position, holder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return daysInMonth;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView dateTxt;
        LinearLayout tarabalamCntr;

        public ItemViewHolder(View itemView) {
            super(itemView);

            dateTxt = itemView.findViewById(R.id.dateTxt);

            tarabalamCntr = itemView.findViewById(R.id.tarabalamCntr);
        }


    }

    private void setPanchnang(int position, ItemViewHolder holder) {

        try {
            String key = year + "" + mMonthIndex + "" + (position + 1);
            CoreDataHelper myCoreData = mItems.get(key);
            //  CoreDataHelper.Panchanga nakshetraObj = myCoreData.getMoonSign();
            Calendar sunriseCal = myCoreData.getSunRise();
            Calendar sunsetCal = myCoreData.getSunSetCal();


            Calendar cal = Calendar.getInstance();
            cal.set(year, mMonthIndex, (position + 1));
          //  int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE, dd-MMMM yyyy", Locale.ENGLISH);
            String formatted = format1.format(cal.getTime());
          //  String sunRise = getFormattedDate(sunriseCal);
           // String sunSet = getFormattedDate(sunsetCal);
         //   String riseset = formatted + "\n(" + pkey_sunrise + " : " + sunRise + " - " + pkey_sunset + " : " + sunSet + ")";


            holder.dateTxt.setText(formatted);
            LayoutInflater inflater = LayoutInflater.from(mContext); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.tarabalamCntr.removeAllViews();


            int souraMasa = myCoreData.getAdjustSolarMonth();
            int chandraMasa = myCoreData.getLunarMonthPurnimantIndex();
            int pakshya = myCoreData.getPaksha()+1;
            int bara = myCoreData.getDayOfWeek();
            int tithi = myCoreData.getTithi().currVal<15?myCoreData.getTithi().currVal:(myCoreData.getTithi().currVal-15);
            int nakshetra = myCoreData.getNakshetra().currVal;
            int joga = myCoreData.getJoga().currVal;
            int karana = myCoreData.getKarana().val1;
            int karana1=karana;

            if(karana>8 && karana<58){
                karana=( karana==9 || karana==16 || karana==23 || karana==30 || karana==37 || karana==44 || karana==51)?2:karana;
                karana=( karana==10 || karana==17 || karana==24 || karana==31 || karana==38 || karana==45 || karana==52)?3:karana;
                karana=( karana==11 || karana==18 || karana==25 || karana==32 || karana==39 || karana==46 || karana==53)?4:karana;
                karana=( karana==12 || karana==19 || karana==26 || karana==33 || karana==40 || karana==47 || karana==54)?5:karana;
                karana=( karana==13 || karana==20 || karana==27 || karana==34 || karana==41 || karana==48 || karana==55)?6:karana;
                karana=( karana==14 || karana==21 || karana==28 || karana==35 || karana==42 || karana==49 || karana==56)?7:karana;
                karana=( karana==15 || karana==22 || karana==29 || karana==36 || karana==43 || karana==50 || karana==57)?8:karana;
            }
            boolean isVivah=false;

            for (int i = 0; i < auspData.size(); i++) {
                AuspData dataObj = auspData.get(i);
             //   switch (dataObj.getSlno()) {
                 //   case 1:
                        if (dataObj.getSouraMasa().contains("|" + souraMasa + "|")/*
                                && dataObj.getChandraMasa().contains("|" + chandraMasa + "|")*/
                                && dataObj.getPakshya().contains("|" + pakshya + "|")
                                && dataObj.getBara().contains("|" + bara + "|")
                                && dataObj.getTithi().contains("|" + tithi + "|")
                                && dataObj.getNakshetra().contains("|" + nakshetra + "|")
                                && dataObj.getJoga().contains("|" + joga + "|")
                                && dataObj.getKarana().contains("|" + karana + "|")

                        ) {
                            isVivah = true;
                        }
                   //     break;
                   // case 2:
                     //   break;
              //  }
Log.e("formatted","formatted::"+formatted+":isVivah:"+isVivah+",souraMasa:"+souraMasa+",bara:"+bara+",nakshetra:"+nakshetra+",tithi:"+tithi+",:karana:"+karana+",karana1:"+karana1+",joga:"+joga);
                if(isVivah){
                    holder.dateTxt.setTextColor(Color.GREEN);
                }else{
                    holder.dateTxt.setTextColor(Color.RED);
                }
                holder.dateTxt.setText(":Vivah:"+isVivah+":date:"+formatted);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getFormattedDate(Calendar cal) {

        if (mLang.contains("or") && !CalendarWeatherApp.isPanchangEng) {
            int calDayNo = cal.get(Calendar.DAY_OF_MONTH);
            int calHour = cal.get(Calendar.HOUR_OF_DAY);
            int calMin = cal.get(Calendar.MINUTE);
            int calMonth = cal.get(Calendar.MONTH);
            String calDayNoStr = Utility.getInstance(mContext).getDayNo("" + calDayNo);
            String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
            String prefixTime = "";
            if ((calHour >= 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
                prefixTime = mRes.getString(R.string.l_time_night);
            }
            if (calHour >= 4 && calHour < 9) {
                prefixTime = mRes.getString(R.string.l_time_prattha);
            } else if (calHour >= 9 && calHour < 16) {
                prefixTime = mRes.getString(R.string.l_time_diba);
            } else if (calHour >= 16 && calHour < 19) {
                prefixTime = mRes.getString(R.string.l_time_sandhya);
            }
            if (calHour > 12) {
                calHour = calHour - 12;
            }
            String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

            String ldate;


            ldate = prefixTime + " " + calHourNoStr + "" + mRes.getString(R.string.l_time_hour) + " " + calMinStr + mRes.getString(R.string.l_time_min);


            return ldate;
        } else {
            return geteFormattedDate(cal);
        }
    }

    private String geteFormattedDate(Calendar cal) {

        try {

            Date date = cal.getTime();
            DateFormat dateFormat;

            String dt;
            dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            dt = dateFormat.format(date);

            return dt;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
