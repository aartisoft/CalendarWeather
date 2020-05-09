package com.iexamcenter.calendarweather.utility;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.AuspData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangUtility;

import java.text.DecimalFormat;

public class BottomDialogFragment extends BottomSheetDialogFragment {
    AuspData obj;
    PrefManager mPref;
    MainActivity mContext;
    Resources resource;
    int type;
    String le_sal, le_ritu, le_masa, le_paksha, le_bara, headerStr, le_samvat, le_dina, le_time_from, le_time_to, le_time_next, le_tithi, le_day, le_nakshetra, le_solar_rasi, le_lunar_rashi, le_joga, le_karana;
    String eheaderStr, le_shakaddha, le_solar_month, le_solar_day, le_plunar_month, le_plunar_day, le_alunar_month, le_alunar_day, le_fortnight;
    String[] le_arr_ausp_work, le_arr_paksha;
    String[] le_arr_tithi, le_arr_masa, resPaksha, le_arr_nakshatra, le_arr_bara, le_arr_joga, le_arr_karana;
    String le_time_hour, le_time_min, le_time_tharu;
    String le_time_night,le_time_prattha,le_time_diba,le_time_sandhya;


    int mType;

    public void getAppResourse() {
        mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
        if (mType == 0) {
            le_time_night=resource.getString(R.string.l_time_night);
            le_time_prattha=resource.getString(R.string.l_time_prattha);
            le_time_diba=resource.getString(R.string.l_time_diba);
            le_time_sandhya=resource.getString(R.string.l_time_sandhya);
            le_time_tharu = resource.getString(R.string.l_time_tharu);
            le_time_hour = resource.getString(R.string.l_time_hour);
            le_time_min = resource.getString(R.string.l_time_min);
            le_paksha = resource.getString(R.string.l_paksha);
            le_arr_paksha = resource.getStringArray(R.array.l_arr_paksha);
            le_arr_ausp_work = resource.getStringArray(R.array.l_arr_ausp_work);
            le_masa = resource.getString(R.string.l_masa);
            le_bara = resource.getString(R.string.l_bara);
            le_tithi = resource.getString(R.string.l_tithi);
            le_day = resource.getString(R.string.l_day);
            le_nakshetra = resource.getString(R.string.l_nakshetra);
            le_solar_rasi = resource.getString(R.string.l_solar_rasi);
            le_lunar_rashi = resource.getString(R.string.l_lunar_rashi);
            le_joga = resource.getString(R.string.l_joga);
            le_karana = resource.getString(R.string.l_karana);
            le_arr_tithi = resource.getStringArray(R.array.l_arr_tithi);
            le_arr_masa = resource.getStringArray(R.array.l_arr_masa);
            le_arr_nakshatra = resource.getStringArray(R.array.l_arr_nakshatra);
            le_arr_bara = resource.getStringArray(R.array.l_arr_bara);
            le_arr_joga = resource.getStringArray(R.array.l_arr_joga);
            le_arr_karana = resource.getStringArray(R.array.l_arr_karana);
            le_dina = resource.getString(R.string.l_dina);
            le_samvat = resource.getString(R.string.l_samvat);
            le_shakaddha = resource.getString(R.string.l_shakaddha);
            le_solar_month = resource.getString(R.string.l_solar_month);
            le_solar_day = resource.getString(R.string.l_solar_day);
            le_plunar_month = resource.getString(R.string.l_plunar_month);
            le_plunar_day = resource.getString(R.string.l_plunar_day);
            le_alunar_month = resource.getString(R.string.l_alunar_month);
            le_alunar_day = resource.getString(R.string.l_alunar_day);
            le_fortnight = resource.getString(R.string.l_fortnight);
            le_time_from = resource.getString(R.string.l_time_from);
            le_time_to = resource.getString(R.string.l_time_to);
            le_time_next = resource.getString(R.string.l_time_next);
            le_sal = resource.getString(R.string.l_sal);
            le_ritu = resource.getString(R.string.l_ritu);

        } else {
            le_time_night=resource.getString(R.string.e_time_night);
            le_time_prattha=resource.getString(R.string.e_time_prattha);
            le_time_diba=resource.getString(R.string.e_time_diba);
            le_time_sandhya=resource.getString(R.string.e_time_sandhya);
            le_time_tharu = resource.getString(R.string.e_time_tharu);
            le_time_hour = resource.getString(R.string.e_time_hour);
            le_time_min = resource.getString(R.string.e_time_min);
            le_paksha = resource.getString(R.string.e_paksha);
            le_arr_paksha = resource.getStringArray(R.array.e_arr_paksha);
            le_arr_ausp_work = resource.getStringArray(R.array.e_arr_ausp_work);
            le_masa = resource.getString(R.string.e_masa);
            le_bara = resource.getString(R.string.e_bara);
            le_tithi = resource.getString(R.string.e_tithi);
            le_day = resource.getString(R.string.e_day);
            le_nakshetra = resource.getString(R.string.e_nakshetra);
            le_solar_rasi = resource.getString(R.string.e_solar_rasi);
            le_lunar_rashi = resource.getString(R.string.e_lunar_rashi);
            le_joga = resource.getString(R.string.e_joga);
            le_karana = resource.getString(R.string.e_karana);
            le_arr_tithi = resource.getStringArray(R.array.e_arr_tithi);
            le_arr_masa = resource.getStringArray(R.array.e_arr_masa);
            le_arr_nakshatra = resource.getStringArray(R.array.e_arr_nakshatra);
            le_arr_bara = resource.getStringArray(R.array.e_arr_bara);
            le_arr_joga = resource.getStringArray(R.array.e_arr_joga);
            le_arr_karana = resource.getStringArray(R.array.e_arr_karana);
            le_dina = resource.getString(R.string.e_dina);
            le_samvat = resource.getString(R.string.e_samvat);
            le_shakaddha = resource.getString(R.string.e_shakaddha);
            le_solar_month = resource.getString(R.string.e_solar_month);
            le_solar_day = resource.getString(R.string.e_solar_day);
            le_plunar_month = resource.getString(R.string.e_plunar_month);
            le_plunar_day = resource.getString(R.string.e_plunar_day);
            le_alunar_month = resource.getString(R.string.e_alunar_month);
            le_alunar_day = resource.getString(R.string.e_alunar_day);
            le_fortnight = resource.getString(R.string.e_fortnight);
            le_time_from = resource.getString(R.string.e_time_from);
            le_time_to = resource.getString(R.string.e_time_to);
            le_time_next = resource.getString(R.string.e_time_next);
            le_sal = resource.getString(R.string.e_sal);
            le_ritu = resource.getString(R.string.e_ritu);
        }
    }

    public static BottomDialogFragment getInstance(AuspData obj, int type) {
        return new BottomDialogFragment(obj, type);
    }

    public BottomDialogFragment(AuspData obj, int type) {
        this.obj = obj;
        this.type = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.custom_bottom_sheet, container, false);
        mContext = (MainActivity) getActivity();
        mPref = PrefManager.getInstance(getActivity());
        mPref.load();
        resource = mContext.getResources();
        getAppResourse();


        if (type == 1)
            doCreateView(view);
        else
            doCreateViewHeader(view);
        BottomDialogFragment.this.getDialog().setCanceledOnTouchOutside(true);
        BottomDialogFragment.this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return view;
    }

    public String getStr(String str, String[] resArr, int isTithi) {
        String[] str1Arr = str.split("\\|");
        // str1Arr = new HashSet<String>(Arrays.asList(str1Arr)).toArray(new String[0]);

        // Arrays.sort(str1Arr);
        String str1 = "";
        String str2 = "";

        if (isTithi == 1) {
            for (int i = 0; i < str1Arr.length; i++) {
                String val = str1Arr[i];
                if (!val.trim().isEmpty()) {
                    int index = Integer.parseInt(val) - 1;
                    if (index <= 14) {
                        str1 = str1 + resArr[index] + ", ";
                    } else {
                        str2 = str2 + resArr[index] + ", ";
                    }
                }


            }
            if (!str1.isEmpty()) {
                str1 = le_arr_paksha[0] + " " + le_paksha + ":" + str1;
                str1 = str1.trim().replaceAll(",$", "");
            }
            if (!str2.isEmpty()) {
                str2 = le_arr_paksha[1] + " " + le_paksha + ":" + str2;
                str2 = str2.trim().replaceAll(",$", "");

            }
            return (str1 + "\n" + str2).trim();

        } else {
            for (int i = 0; i < str1Arr.length; i++) {
                String val = str1Arr[i];
                if (!val.trim().isEmpty()) {
                    str1 = str1 + resArr[Integer.parseInt(val) - 1] + ", ";
                }
            }
            return str1.trim().replaceAll(",$", "");
        }


    }

    private void doCreateViewHeader(View view) {

        String tithiStr = getStr(obj.getTithi(), le_arr_tithi, 1);
        String chandraMasaStr = getStr(obj.getChandraMasa(), le_arr_masa, 0);
        // String pakshaStr = getStr(obj.getPakshya(), resPaksha, 0);
        String nakshatraStr = getStr(obj.getNakshetra(), le_arr_nakshatra, 0);
        String baraStr = getStr(obj.getBara(), le_arr_bara, 0);
        String jogaStr = getStr(obj.getJoga(), le_arr_joga, 0);
        String karanaStr = getStr(obj.getKarana(), le_arr_karana, 0);

        TextView txt1 = view.findViewById(R.id.txt1);
        TextView txt2 = view.findViewById(R.id.txt2);
        TextView txt3 = view.findViewById(R.id.txt3);
        TextView txt4 = view.findViewById(R.id.txt4);
        TextView txt5 = view.findViewById(R.id.txt5);
        TextView txt6 = view.findViewById(R.id.txt6);
        TextView txt7 = view.findViewById(R.id.txt7);
        TextView txt8 = view.findViewById(R.id.txt8);
        TextView txt9 = view.findViewById(R.id.txt9);
       /* txt1.setText(obj.getName());
        txt2.setText(tithi + ":" + tithiStr);
        txt3.setText(masa + ":" + chandraMasaStr);
        txt4.setText(paksha + ":" + pakshaStr);
        txt5.setText(nakshetra + ":" + nakshatraStr);
        txt6.setText(bara + ":" + baraStr);
        txt7.setText(joga + ":" + jogaStr);
        txt8.setText(karana + ":" + karanaStr);
        */

        txt6.setText(le_arr_ausp_work[obj.getSlno() - 1]);
        txt6.setTypeface(Typeface.DEFAULT_BOLD);
        txt1.setText(le_masa + ":" + chandraMasaStr);
        //txt2.setText(paksha + ":" + pakshaStr);
        txt2.setVisibility(View.GONE);
        txt3.setText(le_bara + ":" + baraStr);

        txt4.setText(tithiStr);
        txt5.setText(le_nakshetra + ":" + nakshatraStr);
        // txt6.setText(bara + ":" + baraStr);
        txt7.setText(le_joga + ":" + jogaStr);
        txt8.setText(le_karana + ":" + karanaStr);

        txt8.setVisibility(View.VISIBLE);
        if (obj.getMangalikaType() == 1) {
            txt9.setVisibility(View.VISIBLE);
            txt9.setText("Some more rules applicable like guruasta,shukrasta, gurbadity,singhguru,khara masa etc.. ");
        }


        // Toast.makeText(mContext, obj.getName() + ":type:" + type, Toast.LENGTH_LONG).show();
    }

    private void doCreateView(View view) {
        PanchangUtility panchangUtility = new PanchangUtility();


        TextView txt1 = view.findViewById(R.id.txt1);
        TextView txt2 = view.findViewById(R.id.txt2);
        TextView txt3 = view.findViewById(R.id.txt3);
        TextView txt4 = view.findViewById(R.id.txt4);
        TextView txt5 = view.findViewById(R.id.txt5);
        TextView txt6 = view.findViewById(R.id.txt6);
        TextView txt7 = view.findViewById(R.id.txt7);


        String str = "BadMasa:" + (!obj.isMasa ? "YES" : "NO");
        str += ",ChaturMasa:" + (obj.isChaturMasa() ? "YES" : "NO");
        str += ",Malamasa:" + (obj.isMalamasa() ? "YES" : "NO");
        str += ",SaranaPanchak:" + (obj.isSaranaPanchak() ? "YES" : "NO");
        str += ",Shukrasta:" + (obj.isCloserSunVenus() ? "YES" : "NO");
        str += ",Guruasta:" + (obj.isCloserSunJupitor() ? "YES" : "NO");
        str += ",ChandraRabiSaniMangla:" + (obj.isChandraRabiSaniMangala() ? "YES" : "NO");
        str += ",SinghaGuru:" + (obj.isSinghaGuru() ? "YES" : "NO");
        str += ",GuruAditya:" + (obj.isGuruAditya() ? "YES" : "NO");
        str += ",Atichar:" + (obj.isJupiterBakra() ? "YES" : "NO");
        str += ",KharaBara:" + (obj.isKharaBara ? "YES" : "NO");
        str += ",DagdaBara:" + (obj.isDagdaBara ? "YES" : "NO");
        str += ",BisaBara:" + (obj.isBisaBara ? "YES" : "NO");
        str += ",Hutasan:" + (obj.isHutasanBara ? "YES" : "NO");
        str += ",BadTithi:" + (!obj.isTithi ? "YES" : "NO");
        str += ",BadNakshetra:" + (!obj.isNakshatra ? "YES" : "NO");
        str += ",BadJoga:" + (!obj.isJoga ? "YES" : "NO");
        str += ",BadKarana:" + (!obj.isKarana ? "YES" : "NO");
        str += ",Sankranthi:" + (obj.isSankranti ? "YES" : "NO");
        str += ",Masanta:" + (obj.isMasant ? "YES" : "NO");
        str += ",Trihasparsa:" + (obj.isTrihaspada ? "YES" : "NO");


        txt7.setText(str);
        CoreDataHelper myCoreData = obj.getDayInfo();


        PanchangUtility.MyPanchang myPanchangObj = panchangUtility.getMyPunchang(myCoreData, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude(), mContext, myCoreData.getmYear(), myCoreData.getmMonth(), myCoreData.getmDay());


        PanchangUtility.MySubPanchang[] tithiArr = myPanchangObj.le_tithi;
        PanchangUtility.MySubPanchang[] jogaArr = myPanchangObj.le_joga;
        PanchangUtility.MySubPanchang[] nakshetraArr = myPanchangObj.le_nakshetra;
        PanchangUtility.MySubPanchang[] karanaArr = myPanchangObj.le_karana;


        StringBuilder tithiStr = getPanchangaValue(tithiArr);
        //  StringBuilder etithiStr = getePanchangaValue(etithiArr);

        StringBuilder nakshetraStr = getPanchangaValue(nakshetraArr);
        // StringBuilder enakshetraStr = getePanchangaValue(enakshetraArr);
        StringBuilder jogaStr = getPanchangaValue(jogaArr);
        // StringBuilder ejogaStr = getePanchangaValue(ejogaArr);
        StringBuilder karanaStr = getPanchangaValue(karanaArr);
        // StringBuilder ekaranaStr = getePanchangaValue(ekaranaArr);

        String tithiTxt = le_tithi + " : " + tithiStr;
        // String etithiTxt = le_tithi + " : " + etithiStr;

        String nakshetraTxt = le_nakshetra + " : " + nakshetraStr;
        // String enakshetraTxt = le_nakshetra + " : " + enakshetraStr;
        String jogaTxt = le_joga + " : " + jogaStr;
        // String ejogaTxt = le_joga + " : " + ejogaStr;

        String karanaTxt = le_karana + " : " + karanaStr;
        // String ekaranaTxt = le_karana + " : " + ekaranaStr;

        String lDay = Utility.getInstance(mContext).getDayNo(myPanchangObj.le_day);
        String lYear = Utility.getInstance(mContext).getDayNo(myPanchangObj.le_year);

        eheaderStr = headerStr = lDay + " " + myPanchangObj.le_month + " " + lYear + ", " + myPanchangObj.le_solarMonth + " " + myPanchangObj.le_solarDay + le_dina + ", " + myPanchangObj.le_lunarMonthPurimant + " " + myPanchangObj.le_lunarDayPurimant + le_dina + ", " + myPanchangObj.le_bara + ", " + myPanchangObj.le_paksha + le_paksha + ", " + myPanchangObj.le_ritu + le_ritu + ", " + myPanchangObj.le_ayana + ", " + myPanchangObj.le_sakaddha + " " + le_shakaddha + ", " + myPanchangObj.le_sanSal + " " + le_sal + ", " + myPanchangObj.le_samvata + " " + le_samvat;


        String goodTime = "", minTime = "", maxTime = "";
        if (obj.getGoodTimes().size() > 0) {

            goodTime = obj.getGoodTimes().get(0);
            double minVal = Double.parseDouble(goodTime.split(":")[0]);
            double maxVal = Double.parseDouble(goodTime.split(":")[1]);
            if (!mPref.getMyLanguage().contains("or") || CalendarWeatherApp.isPanchangEng) {
                minTime = getFormattedTimeEn(minVal);
                maxTime = getFormattedTimeEn(maxVal);
                goodTime = "[" + minTime + "<--->" + maxTime + "]";
            } else {
                minTime = getFormattedTime(minVal);
                maxTime = getFormattedTime(maxVal);
                goodTime = "[" + minTime + " " + le_time_tharu + " " + maxTime + " " + le_time_to + "]";
            }

        }


        txt1.setText(headerStr);

        txt6.setText(obj.getTitle() + " " + goodTime);
        txt2.setText(tithiTxt);

        txt3.setText(nakshetraTxt);
        txt4.setText(jogaTxt);
        txt5.setText(karanaTxt);

       /* if (!CalendarWeatherApp.isPanchangEng) {
            txt1.setText(headerStr);

            txt6.setText(obj.getTitle() + " " + goodTime);
            txt2.setText(tithiTxt);

            txt3.setText(nakshetraTxt);
            txt4.setText(jogaTxt);
            txt5.setText(karanaTxt);


        } else {
            txt1.setText(eheaderStr);

            txt6.setText(obj.getTitle() + " " + goodTime);
            txt2.setText(etithiTxt);

            txt3.setText(enakshetraTxt);
            txt4.setText(ejogaTxt);
            txt5.setText(ekaranaTxt);
        }*/

        if (obj.isTodayAusp()) {
            txt6.setTextColor(Color.parseColor("#FFAB00"));
        }

        txt6.setTypeface(Typeface.DEFAULT_BOLD);

    }

    private StringBuilder getPanchangaValue(PanchangUtility.MySubPanchang[] arr) {

        if (mPref.getMyLanguage().contains("or") && mType == 0) {
            StringBuilder arrStr = new StringBuilder();
            try {
                if (arr[0].time.isEmpty()) {
                    arrStr.append(arr[0].name);
                } else {
                    for (PanchangUtility.MySubPanchang obj : arr) {
                        String name = obj.name;
                        String time = obj.time;
                        if (!time.isEmpty())
                            arrStr.append(" " + name + " ").append(time + " ").append(le_time_to).append(",");
                        else if (!name.isEmpty())
                            arrStr.append(le_time_next + " " + name);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String arrStr1 = arrStr.toString().replaceAll(",$", "");
            return new StringBuilder(arrStr1);
        } else {
            return getePanchangaValue(arr);
        }
    }

    public String getFormattedTimeEn(double time) {
        DecimalFormat df2 = new DecimalFormat("00.00");
        boolean isNextDay = false, isPM = false;
        if (time >= 24.0) {
            time = time - 24.0;
            isNextDay = true;
        }
        if (time >= 12.0) {
            isPM = true;
            time = time - 12.0;
        }


        return "" + df2.format(time) + (isPM ? "pm" : "am") + (isNextDay ? "(+)" : "");

    }

    public String getFormattedTime(double time) {



        boolean isNextDay = false, isPM = false;
        if (time >= 24.0) {
            time = time - 24.0;
            isNextDay = true;
        }
        int calHour = (int) time;
        int calMin = (int) ((time - calHour) * 100);
        String prefixTime = "";
        if ((calHour >= 0 && calHour < 4) || (calHour >= 19 && calHour <= 23)) {
            prefixTime = le_time_night;
        }

        if (calHour >= 4 && calHour < 9) {
            prefixTime = le_time_prattha;
        } else if (calHour >= 9 && calHour < 16) {
            prefixTime =le_time_diba;
        } else if (calHour >= 16 && calHour < 19) {
            prefixTime = le_time_sandhya;
        }
        if (calHour >= 12.0) {
            isPM = true;
            //  time=time-12.0;
        }

        String calMinStr = Utility.getInstance(mContext).getDayNo("" + calMin);
        String calHourNoStr = Utility.getInstance(mContext).getDayNo("" + calHour);

        String ldate = prefixTime + " " + calHourNoStr + "" + le_time_hour + " " + calMinStr + le_time_min;


        return ldate + (isNextDay ? "(+)" : "");//""+df2.format(time)+(isPM?"pm":"am")+(isNextDay?"(+)":"");

    }

    private StringBuilder getePanchangaValue(PanchangUtility.MySubPanchang[] arr) {

        StringBuilder arrStr = new StringBuilder();
        try {
            if (arr[0].time.isEmpty()) {
                arrStr.append(arr[0].name);
            } else {
                for (PanchangUtility.MySubPanchang obj : arr) {
                    String name = obj.name;
                    String time = obj.time;
                    if (time != "")
                        arrStr.append(name).append(" upto ").append(time).append(",");
                    else if (!name.isEmpty())
                        arrStr.append(le_time_next + " " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String arrStr1 = arrStr.toString().replaceAll(",$", "");
        return new StringBuilder(arrStr1);
    }
}
