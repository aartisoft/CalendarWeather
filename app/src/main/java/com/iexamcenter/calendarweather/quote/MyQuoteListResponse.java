package com.iexamcenter.calendarweather.quote;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 10/30/2017.
 */


public class MyQuoteListResponse implements Serializable {
    private static final long serialVersionUID = 187L;
    public ArrayList<MyQuoteList> mylist;
    public MyQuoteListResponse(ArrayList<MyQuoteList> mylist) {
        this.mylist = mylist;
    }

    public static class MyQuoteList implements Serializable {
        private static final long serialVersionUID = 192L;
       public String quote;
        public String author;
        public String category;

    }
}