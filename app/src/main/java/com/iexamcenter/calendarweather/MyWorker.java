package com.iexamcenter.calendarweather;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class MyWorker extends Worker {

    private Context mContext;
    private String jsonStr = "";
    ArrayList<String> retroJupiter;
    ArrayList<String> retroMars;
    ArrayList<String> retroMercury;
    ArrayList<String> retroNeptune;
    ArrayList<String> retroPluto;
    ArrayList<String> retroSaturn;
    ArrayList<String> retroUranus;
    ArrayList<String> retroVenus;
    //  String[] zodiac = {"AR", "TA", "GE", "CN", "LE", "VI", "LI", "SC", "SG", "CP", "AQ", "PI"};

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext = context;

    }

    public void callRetrograde(String file) throws IOException {

        ArrayList<String> retro = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(file + ".txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                retro.add(mLine);
            }
            switch (file) {
                case "RetroJupiter":
                    retroJupiter = retro;
                    break;
                case "RetroVenus":
                    retroVenus = retro;
                    break;
                case "RetroMars":
                    retroMars = retro;
                    break;
                case "RetroMercury":
                    retroMercury = retro;
                    break;
                case "RetroNeptune":
                    retroNeptune = retro;
                    break;
                case "RetroPluto":
                    retroPluto = retro;
                    break;
                case "RetroSaturn":
                    retroSaturn = retro;
                    break;
                case "RetroUranus":
                    retroUranus = retro;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public String InRetrogradeRange(ArrayList<String> rangeList, Date checkDate) {
        try {
            String range;
            int size = rangeList.size();
            int i = 0;
            while (i < size) {
                range = rangeList.get(i);

                String[] rangeDateArr = range.split(" ");
                String minRange = rangeDateArr[0];
                String maxRange = rangeDateArr[1];
                //   String checkDateStr = yearInt + "-" + (monthInt + 1) + "-" + dayInt;
                SimpleDateFormat formatterRange = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);

                Date minRangeDate = formatterRange.parse(minRange);
                Date maxRangeDate = formatterRange.parse(maxRange);
                // Date checkDate = formatterRange.parse(checkDateStr);

                formatterRange.format(minRangeDate);


                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getDefault());
                c.setTime(minRangeDate);
                c.add(Calendar.YEAR, 100);
                minRangeDate = c.getTime();

                Calendar c1 = Calendar.getInstance();
                c1.setTime(maxRangeDate);
                c1.add(Calendar.YEAR, 100);
                maxRangeDate = c1.getTime();

                //SimpleDateFormat formatterCheck = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                //  Date checkDate = formatterCheck.parse(chk);
                SimpleDateFormat formatterRange1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


                String x = formatterRange1.format(minRangeDate);


                String y = formatterRange1.format(checkDate);


                if (minRangeDate.compareTo(checkDate) * checkDate.compareTo(maxRangeDate) > 0) {

                    return "R";
                } else if (minRangeDate.compareTo(checkDate) == 0) {


                    return "B";
                } else if (maxRangeDate.compareTo(checkDate) == 0) {

                    return "E";
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "X";
        }
        return "F";
    }

    @Override
    public Result doWork() {
//USE R-RETRO,F-FORWARD, B-RETRO BEGIN DATE, E-RETRO END DATE


        doRssUpdate();
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);


        try {

            AppDatabase.getInstance(mContext).ephemerisDao().deleteAll();
            Log.e("TIMEINMILLI", "InsertEphemeris:1:" + System.currentTimeMillis());
            InsertEphemerisData((currYear - 1), (currYear + 1));
            InsertEphemerisData((currYear + 2), 2050);
            InsertEphemerisData(1970, (currYear - 2));
            InsertEphemerisData(2051, 2100);
             InsertEphemerisData(1900, 1969);


            // handleRetroData(currYear);
        } catch (Exception e) {
            e.printStackTrace();
            //  return Result.failure();
            return Result.success();
        }
        return Result.success();


    }

    private void handleRetroData(int currYear) {
        Log.e("isFirstUse", "isFirstUse:handleRetroData");
        try {
            callRetrograde("RetroJupiter");
            callRetrograde("RetroVenus");
            callRetrograde("RetroMars");
            callRetrograde("RetroMercury");
            callRetrograde("RetroNeptune");
            callRetrograde("RetroPluto");
            callRetrograde("RetroSaturn");
            callRetrograde("RetroUranus");
            // retroEphemerisData(2042, 2100);
            retroEphemerisData(2010, 2014);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retroEphemerisData(int startYear, int endYear) throws IOException {
        if (startYear > endYear) {
            int tmp = endYear;
            endYear = startYear;
            startYear = tmp;
        }

        ArrayList<EphemerisEntity> al = new ArrayList<>();
        al.clear();
        for (int i = startYear; i <= endYear; i++) {
            String file = "planetinfo" + i + ".txt";
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = null;
            try {

                reader = new BufferedReader(
                        new InputStreamReader(mContext.getAssets().open(file)));

                String mLine;

                while ((mLine = reader.readLine()) != null) {

                    String[] content = mLine.split(" ");
                    // EphemerisEntity ephemeris = new EphemerisEntity();
                    // DATE SUN   MOON  MERCURY  VENUS  MARS JUPITER SATURN URANUS NEPTUNE PLUTO  NODE

                    String[] dateStr = content[0].split("-");
                    int yearInt = Integer.parseInt(dateStr[0]) + 100;
                    int monthInt = Integer.parseInt(dateStr[1]);
                    int dayInt = Integer.parseInt(dateStr[2]);


                    String rmercury, rvenus, rmars, rjupitor, rsaturn, ruranus, rneptune, rpluto;

                    String checkDateStr = yearInt + "-" + (monthInt + 1) + "-" + dayInt;
                    SimpleDateFormat formatterRange = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
                    Date checkDate = formatterRange.parse(checkDateStr);


                    rmercury = InRetrogradeRange(retroMercury, checkDate);
                    rvenus = InRetrogradeRange(retroVenus, checkDate);
                    rmars = InRetrogradeRange(retroMars, checkDate);
                    rjupitor = InRetrogradeRange(retroJupiter, checkDate);
                    rsaturn = InRetrogradeRange(retroSaturn, checkDate);
                    ruranus = InRetrogradeRange(retroUranus, checkDate);
                    rneptune = InRetrogradeRange(retroNeptune, checkDate);
                    rpluto = InRetrogradeRange(retroPluto, checkDate);


                    sb.append(content[0]).append(" ")
                            .append(content[1]).append(" ")
                            .append(content[2]).append(" ")
                            .append(content[3]).append(" ")
                            .append(content[4]).append(" ")
                            .append(content[5]).append(" ")
                            .append(content[6]).append(" ")
                            .append(content[7]).append(" ")
                            .append(content[8]).append(" ")
                            .append(content[9]).append(" ")
                            .append(content[10]).append(" ")
                            .append(content[11]).append(" ")
                            .append(content[12]).append(" ")
                            .append(content[13]).append(" ")
                            .append(rmercury).append(content[14]).append(" ")
                            .append(rvenus).append(content[15]).append(" ")
                            .append(rmars).append(content[16]).append(" ")
                            .append(rjupitor).append(content[17]).append(" ")
                            .append(rsaturn).append(content[18]).append(" ")
                            .append(ruranus).append(content[19]).append(" ")
                            .append(rneptune).append(content[20]).append(" ")
                            .append(rpluto).append(content[21]).append(" ")
                            .append(content[22]).append("\n");

                 //   Log.e("RRTRO", "RRTROX:" + content[0]);
                    //Log.e("RRTRO", "RRTROY:" + mLine);


                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // closes the stream and releases system resources
                if (reader != null)
                    reader.close();

            }

            writeTextFile(sb.toString(), file);
        }
        // AppDatabase.getInstance(mContext).ephemerisDao().insertAll(al);

    }

    public void writeTextFile(String data, String file) {
        try {

            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "r" + file;
            String filePath = baseDir + File.separator + fileName;
            FileWriter writer = new FileWriter(filePath);
            writer.write(data);
            writer.close();
            Log.e("RRTRO", "RRTROX:DONE:" + fileName);
        } catch (Exception e) {
            Log.e("RRTRO", "RRTROX:ERROR:");
            e.printStackTrace();
        }
    }

    private void InsertEphemerisData(int startYear, int endYear) throws IOException {

        ArrayList<EphemerisEntity> al = new ArrayList<>();
        al.clear();
        for (int i = startYear; i <= endYear; i++) {
            String file = "rplanetinfo" + i + ".txt";

            BufferedReader reader = null;
            try {

                reader = new BufferedReader(
                        new InputStreamReader(mContext.getAssets().open(file)));

                String mLine;

                while ((mLine = reader.readLine()) != null) {

                    String[] content = mLine.split(" ");
                  //  Log.e("mLine", file + ":mLinemLine:" + mLine);
                    EphemerisEntity ephemeris = new EphemerisEntity();
                    // DATE SUN   MOON  MERCURY  VENUS  MARS JUPITER SATURN URANUS NEPTUNE PLUTO  NODE

                    String[] dateStr = content[0].split("-");
                    int yearInt = Integer.parseInt(dateStr[0]) + 100;
                    int monthInt = Integer.parseInt(dateStr[1]);
                    int dayInt = Integer.parseInt(dateStr[2]);
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    //cal.setTimeZone(TimeZone.getDefault());


                    cal.set(yearInt, monthInt, dayInt, 0, 0, 0);
                    // Date dateRaw = cal.getTime();
                    ephemeris.timestamp = "" + cal.getTimeInMillis();
                    ephemeris.year = "" + (yearInt - 100);
                    ephemeris.month = "" + monthInt;
                    ephemeris.day = "" + dayInt;

                    ephemeris.sun = content[1];
                    ephemeris.moon = content[2];
                    ephemeris.mercury = content[3];
                    ephemeris.venus = content[4];
                    ephemeris.mars = content[5];
                    ephemeris.jupitor = content[6];
                    ephemeris.saturn = content[7];
                    ephemeris.uranus = content[8];
                    ephemeris.neptune = content[9];
                    ephemeris.pluto = content[10];
                    ephemeris.node = content[11];

                    ephemeris.dmsun = content[12];
                    ephemeris.dmmoon = content[13];
                    ephemeris.dmmercury = content[14];
                    ephemeris.dmvenus = content[15];
                    ephemeris.dmmars = content[16];
                    ephemeris.dmjupitor = content[17];
                    ephemeris.dmsaturn = content[18];
                    ephemeris.dmuranus = content[19];
                    ephemeris.dmneptune = content[20];
                    ephemeris.dmpluto = content[21];
                    ephemeris.dmnode = content[22];
                    al.add(ephemeris);


                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // closes the stream and releases system resources
                if (reader != null)
                    reader.close();

            }


        }
        AppDatabase.getInstance(mContext).ephemerisDao().insertAll(al);
        Log.e("TIMEINMILLI", "InsertEphemeris::::" + startYear+"::"+endYear);
    }

    public void doRssUpdate() {
        AppDatabase.getInstance(mContext).rssDao().deleteAll();
        String[] mRssArr = mContext.getResources().getStringArray(R.array.rss);


        ArrayList<RssEntity> cva = new ArrayList<>();
        int i = 0;
        for (String rssStr : mRssArr) {
            RssEntity re = new RssEntity();
            String[] rssStrArr = rssStr.split(Pattern.quote("],["));
            String rssLink = rssStrArr[4];
            String rssChannel = rssStrArr[2];
            String rssCat = rssStrArr[3];
            String rssLang = rssStrArr[0];
            String rssType = rssStrArr[1];
            re.rssLink = rssLink;
            re.rssCat = rssCat;
            re.rssChannel = rssChannel;
            re.rssType = rssType;
            re.rssLang = rssLang;
            re.rssScbscribe = "0";
            re.rssOrder = "" + i++;
            re.rssDelete = "0";
            re.rssLastModified = "0";
            re.rssPublish = "1";
            cva.add(re);
        }
        AppDatabase.getInstance(mContext).rssDao().insertAll(cva);

    }

    private void InsertEphemerisDatabck() {
        try {
            BufferedReader reader;
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("ephemerisdata.txt")));
         //   Log.e("InsertEphemerisData", "EphemerisData|1|" + System.currentTimeMillis());

            String mLine;

            ArrayList<EphemerisEntity> al = new ArrayList<>();

            al.clear();
            while ((mLine = reader.readLine()) != null) {

                String[] content = mLine.split(" ");
                EphemerisEntity ephemeris = new EphemerisEntity();
                //DATE  SID.TIME  SUN   MOON  MERCURY  VENUS  MARS JUPITER SATURN URANUS NEPTUNE PLUTO  NODE
                String[] dateStr = content[0].split("-");
                int yearInt = Integer.parseInt(dateStr[0]) + 100;
                int monthInt = Integer.parseInt(dateStr[1]);
                int dayInt = Integer.parseInt(dateStr[2]);
                Calendar cal = Calendar.getInstance();

                cal.set(yearInt, monthInt, dayInt);
                // cal.getTime()
                //  ephemeris.date = content[0];
                // ephemeris.time = content[1];
                //InRetrogradeRange(retroJupiter,content[0]);
                ephemeris.sun = content[2];
                ephemeris.moon = content[3];
                ephemeris.mercury = content[4];
                ephemeris.venus = content[5];
                ephemeris.mars = content[6];
                ephemeris.jupitor = content[7];
                ephemeris.saturn = content[8];
                ephemeris.uranus = content[9];
                ephemeris.neptune = content[10];
                ephemeris.pluto = content[11];
                ephemeris.node = content[12];

                ephemeris.timestamp = "" + cal.getTimeInMillis();
                ephemeris.year = "" + (yearInt - 100);
                ephemeris.month = "" + monthInt;
                ephemeris.day = "" + dayInt;
                ephemeris.sun = content[13];
                ephemeris.moon = content[14];
                ephemeris.dmsun = content[15];
                ephemeris.dmmoon = content[16];
                al.add(ephemeris);


                // db.insert(SqliteHelper.CoreDataEntry.TABLE_COREDATA, null, values);
                //  String sql = "INSERT INTO " + SqliteHelper.CoreDataEntry.TABLE_COREDATA + " VALUES (null," + milli + "," + year + "," + month + "," + day + ",'" + row[1] + "','" + row[2] + "','" + row[3] + "','" + row[4] + "','" + row[5] + "','" + row[6] + "','" + row[7] + "','" + row[8] + "','" + row[9] + "','" + row[10] + "','" + row[11] + "','" + row[12] + "');";


            }
          //  Log.e("InsertEphemerisData", "EphemerisData|2|" + System.currentTimeMillis());
            AppDatabase.getInstance(mContext).ephemerisDao().insertAll(al);
          //  Log.e("InsertEphemerisData", "EphemerisData|3|" + System.currentTimeMillis());

        } catch (IOException e) {
            e.printStackTrace();
        }

      //  Log.e("InsertEphemerisData", "EphemerisData||" + System.currentTimeMillis());
    }

}
