package com.iexamcenter.calendarweather.news;

/**
 * Created by sasikanta on 2/6/2017.
 */


import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * RSSPullParser reads an RSS feed from the Picasa featured pictures site. It uses
 * several packages from the widely-known XMLPull API.
 */
public class RSSPullParser extends DefaultHandler {
    // Global constants
    // An attribute value indicating that the element contains media content
    private static final String CONTENT = "media:content";

    // An attribute value indicating that the element contains a thumbnail
    private static final String THUMBNAIL = "media:thumbnail";

    // An attribute value indicating that the element contains an item
    private static final String ITEM = "item";
    // Sets the initial size of the vector that stores data.
    private static final int VECTOR_INITIAL_SIZE = 500;
    // Storage for a single ContentValues for image data
    private static NewsEntity mImage;
    private static String[] mRss;
    // A vector that will contain all of the images
    private Vector<NewsEntity> mItemData;

    /**
     * A getter that returns the image data Vector
     *
     * @return A Vector containing all of the image data retrieved by the parser
     */
    public Vector<NewsEntity> getItemData() {
        return mItemData;
    }

    /**
     * This method parses XML in an input stream and stores parts of the data in memory
     *
     * @param inputStream      a stream of data containing XML elements, usually a RSS feed
     * @param progressNotifier a helper class for sending status and logs
     * @throws XmlPullParserException defined by XMLPullParser; thrown if the thread is cancelled.
     * @throws IOException            thrown if an IO error occurs during parsing
     */
    public void parseXml(String[] rssLink, InputStream inputStream)
            throws XmlPullParserException, IOException {
        // Instantiates a parser factory
        mRss = rssLink;
        XmlPullParserFactory localXmlPullParserFactory = XmlPullParserFactory
                .newInstance();
        // Turns off namespace handling for the XML input
        localXmlPullParserFactory.setNamespaceAware(false);
        // Instantiates a new pull parser
        XmlPullParser localXmlPullParser = localXmlPullParserFactory
                .newPullParser();
        // Sets the parser's input stream
        localXmlPullParser.setInput(inputStream, null);
        // Gets the first event in the input sream
        int eventType = localXmlPullParser.getEventType();
        // Sets the number of images read to 1
        int imageCount = 1;
        // Returns if the current event (state) is not START_DOCUMENT
        if (eventType != XmlPullParser.START_DOCUMENT) {
            throw new XmlPullParserException("Invalid RSS");
        }
        // Creates a new store for image URL data
        mItemData = new Vector<NewsEntity>(VECTOR_INITIAL_SIZE);
        // Loops indefinitely. The exit occurs if there are no more URLs to process
        boolean insideItem = false;
        int dataCnt = 0;
        String title, pubDate, link, image;
        title = pubDate = link = image = "";
        while (true) {

            // Gets the next event in the input stream
            int nextEvent = localXmlPullParser.next();
            // If the current thread is interrupted, throws an exception and returns
            if (Thread.currentThread().isInterrupted()) {
                throw new XmlPullParserException("Cancelled");
                // At the end of the feed, exits the loop
            } else if (nextEvent == XmlPullParser.END_DOCUMENT) {
                break;
                // At the beginning of the feed, skips the event and continues
            } else if (nextEvent == XmlPullParser.START_DOCUMENT) {
                continue;
                // At the start of a tag, gets the tag's name
            } else if (nextEvent == XmlPullParser.START_TAG) {
                String eventName = localXmlPullParser.getName();
                /*
                 * If this is the start of an individual item, logs it and creates a new
                 * ContentValues
                 */
                System.out.println("eventName::" + eventName);

                if (eventName.equalsIgnoreCase(ITEM)) {
                    mImage = new NewsEntity();
                    insideItem = true;
                    dataCnt = 0;

                    // If this isn't an item, then checks for other options
                } else if (eventName.equalsIgnoreCase("title") && insideItem) {
                    title = localXmlPullParser.nextText();
                    ++dataCnt;


                    // If this isn't an item, then checks for other options
                } else if (eventName.equalsIgnoreCase("pubDate") && insideItem) {
                    ++dataCnt;
                    pubDate = localXmlPullParser.nextText();
                    // If this isn't an item, then checks for other options
                } else if (eventName.equalsIgnoreCase("link") && insideItem) {
                    ++dataCnt;
                    link = localXmlPullParser.nextText();


                    // If this isn't an item, then checks for other options
                } else if (eventName.equalsIgnoreCase("image") && insideItem) {
                    ++dataCnt;
                    image = localXmlPullParser.nextText();

                    // If this isn't an item, then checks for other options
                } else if (insideItem) {
                    // Defines keys to store the column names
                    String imageUrlKey;
                    String imageNameKey;

                    // Defines a place to store the filename of a URL,
                    String fileName;
                    // If it's CONTENT
                   /* if (eventName.equalsIgnoreCase(CONTENT)) {
                        // Stores the image URL and image name column names as keys
                        imageUrlKey = "xxx";//DataProviderContract.IMAGE_URL_COLUMN;
                        imageNameKey ="xxxx";// DataProviderContract.IMAGE_PICTURENAME_COLUMN;
                        // If it's a THUMBNAIL
                    } else*/
                    if (eventName.equalsIgnoreCase(THUMBNAIL)) {
                        ++dataCnt;
                        image = localXmlPullParser.getAttributeValue(null, "url");


                    } else {
                        continue;
                    }
                    // It's not an ITEM. Gets the URL attribute from the event
                    //  String urlValue = localXmlPullParser.getAttributeValue(null, "url");
                    // If the value is null, exits
                    //if (urlValue == null)
                    //  break;
                    // Puts the URL and the key into the ContentValues
                    //  mImage.put(imageUrlKey, urlValue);

                    // Gets the filename of the URL and puts it into the ContentValues
                    // fileName = Uri.parse(urlValue).getLastPathSegment();
                    // mImage.put(imageNameKey, fileName);
                }
            }
            /*
             * If it's not an ITEM, and it is an END_TAG, and the current event is an ITEM, and
             * there is data in the current ContentValues
             */
            else if ((nextEvent == XmlPullParser.END_TAG)
                    && (localXmlPullParser.getName().equalsIgnoreCase(ITEM))
                    && (mImage != null)) {
                // Adds the current ContentValues to the ContentValues storage
                if (insideItem && dataCnt >= 3) {


                    mImage.newsTitle=title.trim();
                    mImage.newsCat=mRss[1].trim();
                    mImage.newsType=mRss[3].trim();
                    mImage.newsChannel=mRss[0].trim();
                    mImage.newsPubData= pubDate.trim();
                    mImage.newsTimeStamp=(System.currentTimeMillis() / 1000);
                    mImage.newsLink=link.trim();
                    mImage.newsImage=image.trim();
                    mImage.newsLang=mRss[4].trim();
                   /* mImage.newsVideoId=video_id.trim();


                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_TITLE, title.trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_CAT, mRss[1].trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_TYPE, mRss[3].trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL, mRss[0].trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE, pubDate.trim());

                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP, "" + System.currentTimeMillis() / 1000);

                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_LINK, link.trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_LANG, mRss[4].trim());
                    mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_IMAGE, image.trim());

                    */
                    title = pubDate = link = image = "";
                    if (imageCount > 10)
                        break;
                    mItemData.add(mImage);
                }
                // Logs progress
                //DataProviderContract.IMAGE_URL_COLUMN
                //    progressNotifier.notifyProgress("Parsed Image[" + imageCount + "]:"
                //          + mImage.getAsString("xxxxxx"));
                // Clears out the current ContentValues
                mImage = null;
                // Increments the count of the number of images stored.
                imageCount++;

            }
        }
    }

    public String getStory(String newsLink, String referrer, String storySelector, String unWantedSel) {

        try {
            String userAgent = "Mozilla";
            Connection.Response response1 = Jsoup.connect(newsLink)
                    .userAgent(userAgent)
                    .timeout(10000)
                    .referrer(referrer).ignoreContentType(true)
                    .execute();
            if (response1.statusCode() != 200)
                return "";
            Document doc1 = response1.parse();

            if (storySelector != "") {


                if (unWantedSel != null && unWantedSel.length() > 0) {
                    String[] unWantedArr = unWantedSel.split(",");
                    for (int removeIndex = 0; removeIndex < unWantedArr.length; removeIndex++) {
                        String sel = unWantedArr[removeIndex];
                        for (Element element : doc1.select(sel)) {
                            element.remove();
                        }
                    }

                }
                Elements rows1 = doc1.select(storySelector);
                return rows1.html();

                // return fullStory = Jsoup.parse(fullStory).text();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public long getTimeStamp(String str_date) {

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
            date = sdf.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate.getTime() / 1000;
        } catch (Exception e0) {


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                date = sdf.parse(str_date);
                java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                return timeStampDate.getTime() / 1000;
            } catch (Exception e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm z", Locale.US);
                    date = sdf.parse(str_date);
                    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                    return timeStampDate.getTime() / 1000;
                } catch (Exception e1) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss z", Locale.US);
                        date = sdf.parse(str_date);
                        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                        return timeStampDate.getTime() / 1000;
                    } catch (Exception e2) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm z", Locale.US);
                            date = sdf.parse(str_date);
                            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                            return timeStampDate.getTime() / 1000;
                        } catch (Exception e3) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                                date = sdf.parse(str_date);
                                java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                                return timeStampDate.getTime() / 1000;
                            } catch (ParseException e4) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.US);
                                    date = sdf.parse(str_date);
                                    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                                    return timeStampDate.getTime() / 1000;
                                } catch (ParseException e5) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.US);
                                        date = sdf.parse(str_date);
                                        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                                        return timeStampDate.getTime() / 1000;
                                    } catch (ParseException e6) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.US);
                                            date = sdf.parse(str_date);
                                            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
                                            return timeStampDate.getTime() / 1000;
                                        } catch (ParseException e7) {
                                            e7.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return 0l;
    }
}
