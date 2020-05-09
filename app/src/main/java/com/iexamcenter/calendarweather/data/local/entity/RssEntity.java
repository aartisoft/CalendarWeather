package com.iexamcenter.calendarweather.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "RSS", indices = {@Index(value = {"row_id"}, unique = true)})

public class RssEntity {

    @PrimaryKey(autoGenerate = true)
    public int row_id;
    @ColumnInfo(name = "RSS_LINK")
    public String rssLink;
    @ColumnInfo(name = "RSS_CHANNEL")
    public String rssChannel;
    @ColumnInfo(name = "RSS_CAT")
    public String rssCat;
    @ColumnInfo(name = "RSS_TYPE")
    public String rssType;
    @ColumnInfo(name = "RSS_LANG")
    public String rssLang;
    @ColumnInfo(name = "RSS_PUBLISH")
    public String rssPublish;
    @ColumnInfo(name = "RSS_SUBSCRIBE")
    public String rssScbscribe;
    @ColumnInfo(name = "RSS_ORDER")
    public String rssOrder;
    @ColumnInfo(name = "RSS_DELETE")
    public String rssDelete;
    @ColumnInfo(name = "RSS_LAST_MODIFIED")
    public String rssLastModified;



}
