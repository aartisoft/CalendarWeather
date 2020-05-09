package com.iexamcenter.calendarweather.utility;

import com.iexamcenter.calendarweather.mydata.AuspData;

import java.util.ArrayList;

/**
 * Created by Sasikanta_Sahoo on 1/6/2018.
 * TableDataObject
 */

public class TableDataObject {

    public ArrayList<AuspData> valueList;
    public AuspData[] valueList1;

    public TableDataObject(ArrayList<AuspData> valueList) {

        this.valueList = valueList;

    }
    public TableDataObject(AuspData[] valueList) {

        this.valueList1 = valueList;

    }
}
