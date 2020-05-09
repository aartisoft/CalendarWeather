package com.iexamcenter.calendarweather.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sasikanta on 5/1/2016.
 */
@Keep
public class FeedbackResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    public FeedbackResponse() {

    }


    @SerializedName("title")
    private String title;


}