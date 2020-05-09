package com.iexamcenter.calendarweather.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;

import java.util.List;

@Dao // Required annotation for Dao to be recognized by Room
public interface NewsDao {

    @Query("SELECT * FROM NEWS WHERE NEWS_CHANNEL like :channel AND NEWS_LANG like :lang order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getLocalNews(String channel, String lang);

    @Query("SELECT * FROM NEWS WHERE NEWS_CHANNEL like :channel AND NEWS_LANG in (:lang,'en') order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getLocalEnNews(String channel, String lang);


    @Query("SELECT * FROM NEWS WHERE NEWS_CAT like :cat AND NEWS_LANG in (:lang,'hi','en') order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getCatWiseLocalHiEnNews(String cat, String lang);

    @Query("SELECT * FROM NEWS WHERE NEWS_CAT like :cat AND NEWS_TYPE like :type AND NEWS_LANG in (:lang,'en') order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getCatWiseLocalEnNews(String cat, String type, String lang);

    @Query("SELECT * FROM NEWS WHERE NEWS_CAT like :cat AND NEWS_TYPE LIKE :type AND NEWS_LANG in (:lang,'hi','en') order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getCatTypeLocalHiEnNews(String cat, String type, String lang);


    @Query("SELECT * FROM NEWS WHERE  1 LIMIT 100")
    LiveData<List<NewsEntity>> getCatWiseLocalEnNews();

    @Query("SELECT * FROM NEWS WHERE NEWS_CAT like :cat AND NEWS_LANG in (:lang) order by NEWS_TYPE DESC, NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getCatWiseLocalNews(String cat, String lang);


    @Query("SELECT * FROM NEWS WHERE row_id = :mRowId")
    LiveData<List<NewsEntity>> getNewsById(int mRowId);

    @Query("SELECT * FROM NEWS WHERE NEWS_TITLE like :title AND NEWS_LANG in (:lang,'en') order by NEWS_PUB_TIMESTAMP DESC  LIMIT 500")
    LiveData<List<NewsEntity>> getTitleWiseLocalNews(String title, String lang);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<NewsEntity> obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity obj);

    @Query("DELETE  FROM NEWS WHERE  NEWS_PUB_TIMESTAMP < :timestamp")
    void deleteOlder(long timestamp);


}