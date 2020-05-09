package com.iexamcenter.calendarweather.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "NEWS", indices = {@Index(value = {"NEWS_LINK", "NEWS_CAT"}, unique = true)})

public class NewsEntity {

    @PrimaryKey(autoGenerate = true)
    public int row_id;
    @ColumnInfo(name = "NEWS_TITLE")
    public String newsTitle;
    @ColumnInfo(name = "NEWS_CAT")
    public String newsCat;
    @ColumnInfo(name = "NEWS_TYPE")
    public String newsType;
    @ColumnInfo(name = "NEWS_CHANNEL")
    public String newsChannel;
    @ColumnInfo(name = "NEWS_PUB_TIMESTAMP")
    public long newsTimeStamp;
    @ColumnInfo(name = "NEWS_PUB_DATE")
    public String newsPubData;
    @ColumnInfo(name = "NEWS_IMAGE")
    public String newsImage;
    @ColumnInfo(name = "NEWS_STATUS")
    public String newsStatus;
    @ColumnInfo(name = "NEWS_LANG")
    public String newsLang;
    @ColumnInfo(name = "NEWS_LINK")
    public String newsLink;
    @ColumnInfo(name = "NEWS_VIDEO_ID")
    public String newsVideoId;
    @ColumnInfo(name = "IS_NEWS_IS_NEW")
    public String newsIsNew;


}
