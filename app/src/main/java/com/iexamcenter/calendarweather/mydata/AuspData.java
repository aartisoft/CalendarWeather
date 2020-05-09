package com.iexamcenter.calendarweather.mydata;

import com.iexamcenter.calendarweather.panchang.CoreDataHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class AuspData  implements Cloneable{

     private int dayNight,slno, mangalikaType,isGoodNakshetra;
     private String title,name, souraMasa, chandraMasa, pakshya, bara, tithi, nakshetra, joga, karana;
   // private ArrayList<AuspWork> fd;
    private boolean isTodayAusp;
    private ArrayList<String> goodTimes;
    private CoreDataHelper DayInfo;
    boolean malamasa,isSaranaPanchak,isChaturMasa,closerSunVenus,closerSunJupitor,chandraRabiSaniMangala,singhaGuru,guruAditya,jupiterBakra;
   public boolean isMasa,isTithi,isNakshatra,isJoga,isKarana,isKharaBara,isBisaBara,isHutasanBara,isDagdaBara,isSankranti,isTrihaspada,isMasant;


    public ArrayList<String> getGoodTimes() {
        return goodTimes;
    }

    public void setGoodTimes(ArrayList<String> goodTimes) {
        this.goodTimes = goodTimes;
    }

    AuspData(int slno, int dayNight, int mangalikType, String name, String souraMasa, String chandraMasa, String pakshya, String bara, String tithi, String nakshetra, String joga, String karana) {
        this.slno = slno;
        this.name = name;
        this.dayNight = dayNight;
        this.isGoodNakshetra=0;
        this.title="";
        this.souraMasa = souraMasa;
        this.chandraMasa = chandraMasa;
        this.pakshya = pakshya;
        this.bara = bara;
        this.tithi = tithi;
        this.nakshetra = nakshetra;
        this.joga = joga;
        this.karana = karana;
        this.isTodayAusp=false;
        this.goodTimes=new ArrayList<>();

        this.mangalikaType =mangalikType;
    }
    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public int getDayNight() {
        return dayNight;
    }

    public void setDayNight(int dayNight) {
        this.dayNight = dayNight;
    }

    public boolean isMalamasa() {
        return malamasa;
    }

    public boolean isSaranaPanchak() {
        return isSaranaPanchak;
    }

    public boolean isChaturMasa() {
        return isChaturMasa;
    }

    public boolean isCloserSunVenus() {
        return closerSunVenus;
    }

    public boolean isCloserSunJupitor() {
        return closerSunJupitor;
    }

    public boolean isChandraRabiSaniMangala() {
        return chandraRabiSaniMangala;
    }

    public boolean isSinghaGuru() {
        return singhaGuru;
    }

    public boolean isGuruAditya() {
        return guruAditya;
    }

    public boolean isJupiterBakra() {
        return jupiterBakra;
    }

    public void setMalamasa(boolean malamasa) {
        this.malamasa = malamasa;
    }

    public void setSaranaPanchak(boolean saranaPanchak) {
        isSaranaPanchak = saranaPanchak;
    }

    public void setChaturMasa(boolean chaturMasa) {
        isChaturMasa = chaturMasa;
    }

    public void setCloserSunVenus(boolean closerSunVenus) {
        this.closerSunVenus = closerSunVenus;
    }

    public void setCloserSunJupitor(boolean closerSunJupitor) {
        this.closerSunJupitor = closerSunJupitor;
    }

    public void setChandraRabiSaniMangala(boolean chandraRabiSaniMangala) {
        this.chandraRabiSaniMangala = chandraRabiSaniMangala;
    }

    public void setSinghaGuru(boolean singhaGuru) {
        this.singhaGuru = singhaGuru;
    }

    public void setGuruAditya(boolean guruAditya) {
        this.guruAditya = guruAditya;
    }

    public void setJupiterBakra(boolean jupiterBakra) {
        this.jupiterBakra = jupiterBakra;
    }

    public int getMangalikaType() {
        return mangalikaType;
    }

    public CoreDataHelper getDayInfo() {
        return DayInfo;
    }

    public void setDayInfo(CoreDataHelper dayInfo) {
        DayInfo = dayInfo;
    }

    public boolean isTodayAusp() {
        return isTodayAusp;
    }

    public void setTodayAusp(boolean todayAusp) {
        isTodayAusp = todayAusp;
    }

    public void setGoodNakshetra(int index ) {
        isGoodNakshetra = index;
    }
    public int getGoodNakshetra( ) {
       return isGoodNakshetra;
    }
    public int getSlno() {
        return slno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSouraMasa() {
        return souraMasa;
    }

    public String getChandraMasa() {
        return chandraMasa;
    }

    public String getPakshya() {
        return pakshya;
    }

    public String getBara() {
        return bara;
    }

    public String getTithi() {
        return tithi;
    }

    public String getNakshetra() {
        return nakshetra;
    }

    public String getJoga() {
        return joga;
    }

    public String getKarana() {
        return karana;
    }
}
