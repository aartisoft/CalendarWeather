package com.iexamcenter.calendarweather.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */
@Keep
public class FamousPeopleResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    public FamousPeopleResponse() {

    }

    @SerializedName("QUOTE")
    private String quote;

    public String getQuote() {
        return quote;
    }
}