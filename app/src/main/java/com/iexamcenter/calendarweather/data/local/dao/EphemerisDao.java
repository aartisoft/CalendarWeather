package com.iexamcenter.calendarweather.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;

import java.util.List;

@Dao // Required annotation for Dao to be recognized by Room
public interface EphemerisDao {

    @Query("SELECT * FROM COREDATA WHERE timestamp > :start AND timestamp < :end")
    LiveData<List<EphemerisEntity>> getAll(long start, long end);



    @Query("SELECT * FROM COREDATA WHERE timestamp > :start AND timestamp < :end")
    List<EphemerisEntity> getAll2(long start, long end);

    @Query("SELECT * FROM COREDATA WHERE day LIKE :day AND month LIKE :month AND year LIKE :year")
    LiveData<EphemerisEntity> getPlanetInfo(int day, int month, int year);

    @Query("SELECT * FROM COREDATA WHERE ((day LIKE :day1 AND month LIKE :month1 AND year LIKE :year1) || (day LIKE :day2 AND month LIKE :month2 AND year LIKE :year2))")
    LiveData<List<EphemerisEntity>> getPlanetInfo(int year1, int month1, int day1,int year2, int month2, int day2);


    @Query("SELECT * FROM COREDATA WHERE year LIKE :year OR  year LIKE :year+1  ORDER BY timestamp asc")
    LiveData<List<EphemerisEntity>> setYearlyPlanetInfo(int year);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<EphemerisEntity> obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EphemerisEntity obj);

    @Query("DELETE  FROM COREDATA WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM COREDATA WHERE ((timestamp > :start1 AND timestamp < :end1) || (timestamp > :start2 AND timestamp < :end2) )")
    LiveData<List<EphemerisEntity>> getAll(long start1, long end1, long start2, long end2);
}