package com.iexamcenter.calendarweather.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "COREDATA", indices = {@Index(value = {"timestamp"}, unique = true)})

public class EphemerisEntity {

    @PrimaryKey(autoGenerate = true)
    public int row_id;
    public String timestamp;
    public String year;
    public String month;
    public String day;
    public String sun;
    public String moon;
    public String mercury;
    public String venus;
    public String mars;
    public String jupitor;
    public String saturn;
    public String uranus;
    public String neptune;
    public String pluto;
    public String node;
    public String dmsun;
    public String dmmoon;
    public String dmmercury;
    public String dmvenus;
    public String dmmars;
    public String dmjupitor;
    public String dmsaturn;
    public String dmuranus;
    public String dmneptune;
    public String dmpluto;
    public String dmnode;
    /*
    public int rmercury;
    public int rvenus;
    public int rmars;
    public int rjupitor;
    public int rsaturn;
    public int ruranus;
    public int rneptune;
    public int rpluto;

     */
}
