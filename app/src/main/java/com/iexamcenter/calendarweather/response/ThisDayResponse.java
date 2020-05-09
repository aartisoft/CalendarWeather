package com.iexamcenter.calendarweather.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */
@Keep
public class ThisDayResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    public ThisDayResponse() {

    }

    @SerializedName("TODAY")
    private TODAY todayList;

    public TODAY getTodayList() {
        return todayList;
    }

    @Keep
    public static class TODAY implements Serializable {
        private static final long serialVersionUID = 2L;

        @SerializedName("BASEURL")
        private String imageBaseUrl;

        @SerializedName("HISTORY")
        private ArrayList<HISTORY> history;

        @SerializedName("DEATHDAY")
        private ArrayList<DEATHDAY> deathDay;

        @SerializedName("BIRTHDAY")
        private ArrayList<BIRTHDAY> birthDay;

        public ArrayList<HISTORY> getHistory() {
            return history;
        }

        public ArrayList<DEATHDAY> getDeathDay() {
            return deathDay;
        }

        public ArrayList<BIRTHDAY> getBirthDay() {
            return birthDay;
        }

        public String getImageBaseUrl() {
            return imageBaseUrl;
        }
    }

    @Keep
    public static class HISTORY implements Serializable {
        private static final long serialVersionUID = 3L;

        @SerializedName("age")
        private String age;
        @SerializedName("pkey")
        private String pkey;
        @SerializedName("name")
        private String name;
        @SerializedName("fullname")
        private String fullname;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("deathday")
        private String deathday;
        @SerializedName("famousfor")
        private String famousfor;
        @SerializedName("jobtitle")
        private String jobtitle;
        @SerializedName("birthplace")
        private String birthplace;
        @SerializedName("imagelink")
        private String imagelink;
        @SerializedName("event")
        private String event;
        @SerializedName("eventlist")
        private String eventlist;
        @SerializedName("eventdate")
        private String eventdate;
        @SerializedName("wikilink")
        private String wikilink;

        public String getWikilink() {
            return " ";//wikilink;
        }

        public String getAge() {
            return age;
        }

        public String getPkey() {
            return pkey;
        }

        public String getName() {
            return name;
        }

        public String getFullname() {
            return fullname;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getDeathday() {
            return deathday;
        }

        public String getFamousfor() {
            return famousfor;
        }

        public String getJobtitle() {
            return jobtitle;
        }

        public String getBirthplace() {
            return birthplace;
        }

        public String getImagelink() {
            return imagelink;
        }

        public String getEvent() {
            return event;
        }

        public String getEventlist() {
            return eventlist;
        }

        public String getEventdate() {
            return eventdate;
        }
    }

    @Keep
    public static class DEATHDAY implements Serializable {
        private static final long serialVersionUID = 4L;

        @SerializedName("age")
        private String age;
        @SerializedName("pkey")
        private String pkey;
        @SerializedName("name")
        private String name;
        @SerializedName("fullname")
        private String fullname;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("deathday")
        private String deathday;
        @SerializedName("famousfor")
        private String famousfor;
        @SerializedName("jobtitle")
        private String jobtitle;
        @SerializedName("birthplace")
        private String birthplace;
        @SerializedName("imagelink")
        private String imagelink;
        @SerializedName("event")
        private String event;
        @SerializedName("wikilink")
        private String wikilink;

        public String getWikilink() {
            return " ";//wikilink;
        }

        public String getAge() {
            return age;
        }

        public String getPkey() {
            return pkey;
        }

        public String getName() {
            return name;
        }

        public String getFullname() {
            return fullname;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getDeathday() {
            return deathday;
        }

        public String getFamousfor() {
            return famousfor;
        }

        public String getJobtitle() {
            return jobtitle;
        }

        public String getBirthplace() {
            return birthplace;
        }

        public String getImagelink() {
            return imagelink;
        }

        public String getEvent() {
            return event;
        }
    }

    @Keep
    public static class BIRTHDAY implements Serializable {
        private static final long serialVersionUID = 5L;

        @SerializedName("age")
        private String age;
        @SerializedName("pkey")
        private String pkey;
        @SerializedName("name")
        private String name;
        @SerializedName("fullname")
        private String fullname;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("deathday")
        private String deathday;
        @SerializedName("famousfor")
        private String famousfor;
        @SerializedName("jobtitle")
        private String jobtitle;
        @SerializedName("birthplace")
        private String birthplace;
        @SerializedName("imagelink")
        private String imagelink;
        @SerializedName("event")
        private String event;
        @SerializedName("wikilink")
        private String wikilink;

        public String getWikilink() {
            return " ";//wikilink;
        }

        public String getAge() {
            return age;
        }

        public String getPkey() {
            return pkey;
        }

        public String getName() {
            return name;
        }

        public String getFullname() {
            return fullname;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getDeathday() {
            return deathday;
        }

        public String getFamousfor() {
            return famousfor;
        }

        public String getJobtitle() {
            return jobtitle;
        }

        public String getBirthplace() {
            return birthplace;
        }

        public String getImagelink() {
            return imagelink;
        }

        public String getEvent() {
            return event;
        }
    }
}