package com.iexamcenter.calendarweather.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iexamcenter.calendarweather.data.local.entity.RssEntity;

import java.util.List;

@Dao // Required annotation for Dao to be recognized by Room
public interface RssDao {
    @Query("SELECT * FROM RSS WHERE RSS_PUBLISH=1  AND RSS_LANG  in(:mLang,'hi','en') ORDER BY RSS_CHANNEL ASC")
    List<RssEntity> getChannelRegHiEn(String mLang);

    @Query("SELECT DISTINCT RSS_CAT FROM RSS WHERE RSS_PUBLISH=1  AND RSS_LANG  in(:mLang,'hi','en') ORDER BY RSS_CAT ASC")
    List<String> getCatRegHiEn(String mLang);

    @Query("SELECT * FROM RSS WHERE RSS_PUBLISH=1 AND RSS_CHANNEL LIKE :channel AND RSS_LANG  in(:mLang)")
    List<RssEntity> getAllRssByChannel(String channel,String mLang);

    @Query("SELECT * FROM RSS WHERE RSS_PUBLISH=1 AND RSS_CAT LIKE :cat AND RSS_LANG  in(:mLang,'en')")
    List<RssEntity> getAllRssByCategory(String cat,String mLang);

    @Query("SELECT * FROM RSS WHERE RSS_LANG  in(:mLang,'hi','en')")
    LiveData<List<RssEntity>> getAllRss(String mLang);

    @Query("SELECT * FROM RSS WHERE RSS_LANG  in('en')")
    LiveData<List<RssEntity>> getRssEn();

    @Query("SELECT * FROM RSS WHERE row_id=:row_id")
    RssEntity getRssById(int row_id);

    @Query("UPDATE   RSS set RSS_PUBLISH=:pub WHERE row_id=:row_id")
    void updateById(int row_id, String pub);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RssEntity> obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RssEntity obj);

    @Query("DELETE FROM RSS")
     void deleteAll();


}