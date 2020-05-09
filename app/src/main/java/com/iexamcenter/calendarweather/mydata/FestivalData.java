package com.iexamcenter.calendarweather.mydata;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iexamcenter.calendarweather.panchang.CoreDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class FestivalData implements Comparable {

    private int slno;
    private String eFest, rFest;
    private int eDay, eMonth, eYear, lMonth, lPaksha, lTithi, weekDay, sMonth, sDay, order, specialCode;
    static ArrayList<FestivalData> fd;

    @Override
    public int compareTo(@NonNull Object o) {

        int compareOrder = ((FestivalData) o).getOrder();
        /* For Ascending order*/
        return this.order - compareOrder;

    }

    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    Predicate<FestivalData> validPersonPredicate = new Predicate<FestivalData>() {
        public boolean apply(FestivalData person) {
            return person.geteDay() == 1 && person.geteMonth() == 1;
        }
    };

    FestivalData(int slno, int eDay, int eMonth, int eYear, int lMonth, int lPaksha, int lTithi, int weekDay, int sMonth, int sDay, int nakshatra, int specialCode, int order, String eFest, String rFest) {

        this.slno = slno;
        this.eDay = eDay;
        this.eMonth = eMonth;
        this.eYear = eYear;
        this.lMonth = lMonth;
        this.lPaksha = lPaksha;
        this.lTithi = lTithi;
        this.weekDay = weekDay;
        this.sMonth = sMonth;
        this.sDay = sDay;
        this.eFest = eFest;
        this.rFest = rFest;
        this.order = order;
        this.specialCode = specialCode;
    }

    public int getOrder() {
        return order;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String geteFest() {
        return eFest;
    }

    public void seteFest(String eFest) {
        this.eFest = eFest;
    }

    public String getrFest() {
        return rFest;
    }

    public void setrFest(String rFest) {
        this.rFest = rFest;
    }

    public int geteDay() {
        return eDay;
    }

    public void seteDay(int eDay) {
        this.eDay = eDay;
    }

    public int geteMonth() {
        return eMonth;
    }

    public void seteMonth(int eMonth) {
        this.eMonth = eMonth;
    }

    public int getlMonth() {
        return lMonth;
    }

    public void setlMonth(int lMonth) {
        this.lMonth = lMonth;
    }

    public int getlPaksha() {
        return lPaksha;
    }

    public void setlPaksha(int lPaksha) {
        this.lPaksha = lPaksha;
    }

    public int getlTithi() {
        return lTithi;
    }

    public void setlTithi(int lTithi) {
        this.lTithi = lTithi;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getsMonth() {
        return sMonth;
    }

    public void setsMonth(int sMonth) {
        this.sMonth = sMonth;
    }

    public int getsDay() {
        return sDay;
    }

    public void setsDay(int sDay) {
        this.sDay = sDay;
    }

    public int geteYear() {
        return eYear;
    }

    public int getSpecialCode() {
        return specialCode;
    }

    public static ArrayList<FestivalData> setFestivalData(String lang) {
        // int slno, int eDay, int eMonth, int lMonth, int lPaksha, int lTithi, int weekDay, int sMonth, int sDay, int order,String eFest, String rFest)
        fd = new ArrayList<>();

        switch (lang) {
            case "or":
                fd.addAll(FestivalDataOR.setFestivalData());
                break;
            case "hi":
                fd.addAll(FestivalDataHI.setFestivalData());
                break;
            case "bn":
                fd.addAll(FestivalDataBn.setFestivalData());
                break;
            case "te":
                fd.addAll(FestivalDataTE.setFestivalData());
                break;
            case "ta":
                fd.addAll(FestivalDataTA.setFestivalData());
                break;
            case "kn":
                fd.addAll(FestivalDataKN.setFestivalData());
                break;
            case "ml":
                fd.addAll(FestivalDataML.setFestivalData());
                break;
            case "pa":
                fd.addAll(FestivalDataPA.setFestivalData());
                break;
            case "gu":
                fd.addAll(FestivalDataGU.setFestivalData());
                break;
            case "en":
                fd.addAll(FestivalDataEN.setFestivalData());
                break;
            default:
                fd.addAll(FestivalDataEN.setFestivalData());
                break;

        }


        return fd;
    }
    static int panchakCnt=0;
    public static ArrayList<FestivalData> getFestival(boolean masant, Boolean currNightCover, Boolean nextNightCover, Boolean tithiMala, int dayOfWeek, int dayIndex, int monthIndex, int yearIndex, int solarDayIndex, int solarMonthIndex, int lunarMonthIndex, int pakshaIndex, int currTithiIndex, int nextTithiIndex, int nextToNextTithiIndex, int currMoonSignIndex, int nextMoonSignIndex, boolean nextTithiB4rNoon) {


        int lmonth = lunarMonthIndex;

        Predicate<FestivalData> validFestivalPredicate = new Predicate<FestivalData>() {
            public boolean apply(FestivalData festival) {

                double minTithiDuration=3.0;

                boolean cond1 = (festival.geteDay() == dayIndex && festival.geteMonth() == monthIndex && festival.geteYear() == yearIndex);
                boolean cond2 = (festival.geteDay() == dayIndex && festival.geteMonth() == monthIndex && festival.geteYear() == 0);
                boolean cond10 = false, cond11 = false, cond12 = false, cond13 = false, cond14 = false, cond15 = false, cond16 = false, cond17 = false;
                cond17 = (masant && festival.specialCode == 104);
                boolean cond9 = false;


                if (!tithiMala) {



                    /*if (festival.specialCode != 100 && nextTithiIndex==0) {
                        cond10 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }else */if (festival.specialCode != 100 && !currNightCover  && festival.specialCode != 108) {
                        cond10 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    } else if (festival.specialCode != 100 && festival.specialCode != 103  && festival.specialCode != 108) {
                        cond10 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }
                    if (festival.specialCode == 103) {
                        cond10 = (festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex);

                    }

                    if (festival.specialCode == 100 && currNightCover) {
                        cond12 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }
                    if (festival.specialCode == 100 && nextNightCover) {
                        cond14 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                    }

                    if(festival.specialCode == 108 &&  nextTithiB4rNoon){
                        cond15 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex ) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                    }



                   /*  if (festival.specialCode == 106 && currNightCover) {
                        cond12 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == 0 && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    } else if (festival.specialCode == 106 && nextNightCover) {
                        cond14 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == 0 && festival.getlTithi() == nextTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                    }  if (festival.specialCode == 107) {
                        cond12 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == 0 && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }*/


                } else {
                    if (festival.specialCode == 103) {
                        cond14 = (festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex);

                    } else if (festival.specialCode != 100 && !currNightCover) {
                        cond10 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    } else if (festival.specialCode != 100) {
                        cond10 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }


                    if (festival.specialCode == 100 && currNightCover) {
                        cond12 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                    }
                    if (festival.specialCode == 100 && nextNightCover) {
                        cond14 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                    }
                    if(festival.specialCode == 108 &&  nextTithiB4rNoon){
                        cond15 = ((festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex ) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                    }

                }





                /*else if (festival.specialCode == 106 && currNightCover) {
                    cond12 = ((festival.getlMonth() == 0 && festival.getlPaksha() == 0 && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                } else if (festival.specialCode == 106 && nextNightCover) {
                    cond14 = ((festival.getlMonth() == 0 && festival.getlPaksha() == 0 && festival.getlTithi() == nextTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex) && festival.weekDay == 0);

                }*/


              //  if (festival.specialCode == 107) {
                    //   cond12 = ((festival.getlMonth() == 0 && festival.getlPaksha() == 0 && festival.getlTithi() == currTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex) && festival.weekDay == 0);

                //}


                cond16 = (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == currTithiIndex && festival.getWeekDay() == dayOfWeek);

                if (currTithiIndex > 0 && nextTithiIndex > 0 && nextToNextTithiIndex > 0) {

                    cond9 = (festival.getlMonth() == lmonth && festival.getlPaksha() == pakshaIndex && (festival.getlTithi() == nextTithiIndex) || (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && (festival.getlTithi() == nextTithiIndex)));

                    cond16 = (festival.getlMonth() == 0 && festival.getlPaksha() == pakshaIndex && festival.getlTithi() == nextTithiIndex && festival.getWeekDay() == dayOfWeek);

                }

                boolean cond7 = (festival.getlMonth() == lmonth && festival.getlPaksha() == 0 && festival.getlTithi() == 0 && festival.getWeekDay() == dayOfWeek);

                boolean cond4 = (festival.getsMonth() == solarMonthIndex && festival.getsDay() == solarDayIndex );

                boolean cond5 = (currTithiIndex > 0 && nextTithiIndex > 0 && nextToNextTithiIndex > 0 && festival.getSpecialCode() == 102);
                boolean rajaCond = (solarMonthIndex == 2  && masant && festival.getSpecialCode()==109);
                // boolean cond8 = (currNakshetraIndex == 23 && festival.getSpecialCode() == 101);
                // boolean cond18 = (currNakshetraIndex == 27 && nextNakshetraIndex == 1 && festival.getSpecialCode() == 1001);


                boolean cond8 = (currMoonSignIndex == 9 && nextMoonSignIndex == 10 && festival.getSpecialCode() == 101);

                boolean cond18 = (currMoonSignIndex == 11 && nextMoonSignIndex == 12 && festival.getSpecialCode() == 1001);

                boolean cond19 = ((!cond8 && !cond18) &&  (currMoonSignIndex >=10 && !(currMoonSignIndex == 11 && nextMoonSignIndex == 12)  && currMoonSignIndex < 12)   && festival.getSpecialCode() == 10001);
                boolean cond21 =false;
                if(cond8 && lmonth==7){
                    panchakCnt=0;
                }
                if((cond19 || cond18) && lmonth==7){
                    panchakCnt++;

                }

                cond21=(panchakCnt==5 && lmonth==7 &&  festival.getSpecialCode() == 100001) ;

                if(cond21){
                    panchakCnt=0;

                   // Log.e("panchakCnt",dayIndex+":"+monthIndex+":"+yearIndex+":panchakCnt:::"+panchakCnt+":"+festival.getlMonth()+":"+cond21);



                }


                return (rajaCond || cond1 || cond2 || cond4 || cond5 || cond7 || cond8 || cond9 || cond10  || cond12  || cond14 || cond15 || cond16 || cond18 || cond17 || cond19 || cond21);
            }
        };


        ArrayList<FestivalData> result = (ArrayList<FestivalData>) filter(fd, validFestivalPredicate);
        Log.e("nextTithiB4rNoon","NnnnextTithiB4rNoon:"+dayIndex+"/"+monthIndex+"/"+yearIndex+":"+":"+nextTithiB4rNoon);

        Collections.sort(result);
        return result;
    }


    public static ArrayList<String> calculateFestival(CoreDataHelper myPanchangObj, String currLang, Context ctx) {

        // PanchangUtility panchangUtility = new PanchangUtility();
        int lunarMonth;
        if (currLang.contains("or") || currLang.contains("hi") || currLang.contains("en")) {
            lunarMonth = myPanchangObj.getLunarMonthPurnimantIndex() + 1;


        } else if (currLang.contains("te") || currLang.contains("kn") || currLang.contains("mr")) {
            lunarMonth = myPanchangObj.getLunarMonthAmantIndex() + 1;

        } else {
            lunarMonth = myPanchangObj.getAdjustSolarMonth();
        }
        CoreDataHelper.Panchanga tithiObj = myPanchangObj.getTithi();
        CoreDataHelper.Panchanga nakshetraObj = myPanchangObj.getNakshetra();
       // int currNakshetraIndex = nakshetraObj.currVal;
       // int nextNakshetraIndex = nakshetraObj.nextVal;
        int currTithiIndex = tithiObj.currVal;
        int nextTithiIndex = myPanchangObj.getTithi().nextVal;
        int nextToNextTithiIndex = myPanchangObj.getTithi().nextToNextVal;

     //   double currTithiDuration = (tithiObj.currValEndTime.getTimeInMillis() - myPanchangObj.getSunRiseCal().getTimeInMillis())/(1000*60*60.0);
        long noonTime = myPanchangObj.getSunRiseCal().getTimeInMillis() + ((myPanchangObj.getSunSetCal().getTimeInMillis() - myPanchangObj.getSunRiseCal().getTimeInMillis()) / 2);
        boolean nextTithiB4rNoon = tithiObj.currValEndTime.getTimeInMillis() < noonTime;

        int pakshaIndex = myPanchangObj.getPaksha() + 1;
        int dayofWeek = myPanchangObj.getDayOfWeek();
        int day = myPanchangObj.getmDay();
        int month = myPanchangObj.getmMonth() + 1;
        int year = myPanchangObj.getmYear();
        int solarDay = myPanchangObj.getSolarDayVal();
        int solarMonth = myPanchangObj.getAdjustSolarMonth();

        CoreDataHelper.Panchanga moonsignObj = myPanchangObj.getMoonSign();

        int currMoonSignIndex = moonsignObj.currVal;
        Calendar currMoonSignEndDate = moonsignObj.currValEndTime;

        int nextMoonSignIndex = moonsignObj.nextVal;
        Calendar nextMoonSignEndDate = moonsignObj.le_nextValEndTime;

        boolean currNightCover = false, tithiMala = false, nextNightCover = false, masant = false;




        if (((myPanchangObj.getSunRiseCal().getTimeInMillis() - 24 * 60 * 60 * 1000) > tithiObj.currValStartTime.getTimeInMillis()) && (tithiObj.currValEndTime.getTimeInMillis() < myPanchangObj.getSunSetCal().getTimeInMillis())) {
            tithiMala = true;
        }
        if (tithiObj.currValStartTime.getTimeInMillis() < myPanchangObj.getSunRiseCal().getTimeInMillis() && tithiObj.currValEndTime.getTimeInMillis() > (myPanchangObj.getSunRiseCal().getTimeInMillis() + 16 * 60 * 60 * 1000)) {
            currNightCover = true;
        }


        if (tithiObj.currValEndTime.getTimeInMillis() < myPanchangObj.getSunSetCal().getTimeInMillis() && tithiObj.le_nextValEndTime != null && tithiObj.le_nextValEndTime.getTimeInMillis() > (myPanchangObj.getSunSetCal().getTimeInMillis() + 16 * 60 * 60 * 1000)) {
            nextNightCover = true;
        }
        long sunsignEnd = myPanchangObj.getSunSign().currValEndTime.getTimeInMillis();
        if (sunsignEnd > myPanchangObj.getMidNight().getTimeInMillis() && (sunsignEnd < myPanchangObj.getMidNight().getTimeInMillis() + (24 * 60 * 60 * 1000l))) {

            masant = true;

        }
        Log.e("nextTithiB4rNoon","nnnextTithiB4rNoon:"+day+"/"+month+"/"+year+":"+":"+nextTithiB4rNoon);

        ArrayList<FestivalData> myFestivallist = FestivalData.getFestival(masant, currNightCover, nextNightCover, tithiMala, dayofWeek, day, month, year, solarDay, solarMonth, lunarMonth, pakshaIndex, currTithiIndex, nextTithiIndex, nextToNextTithiIndex, currMoonSignIndex, nextMoonSignIndex,nextTithiB4rNoon);

        String festStr = "", efestStr = "",specialCode="0";
        ArrayList<String> result = new ArrayList<String>();

        if (myFestivallist.size() > 0) {
            for (FestivalData fd : myFestivallist) {
                if(fd.getSpecialCode()==101 || fd.getSpecialCode()==1001 || fd.getSpecialCode()==10001  ){

                    specialCode = "" + fd.getSpecialCode() + "@@" + fd.getrFest() + "@@" + fd.geteFest();


                }
                    festStr = festStr + "" + fd.getrFest() + ", ";
                    efestStr = efestStr + "" + fd.geteFest() + ", ";


            }
        }
        festStr = festStr.replaceAll(", $", "");
        efestStr = efestStr.replaceAll(", $", "");
        result.add(festStr);
        result.add(efestStr);
        result.add(specialCode);


        return result;

    }


}
