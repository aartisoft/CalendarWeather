package com.iexamcenter.calendarweather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.dao.EphemerisDao;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.mydata.FestivalData;
import com.iexamcenter.calendarweather.panchang.CoreDataHelper;
import com.iexamcenter.calendarweather.panchang.PanchangTask;
import com.iexamcenter.calendarweather.panchang.PanchangUtilityLighter;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyAlaramWorker extends Worker {

    private Context mContext;
    Resources res;
    String[]  tithi_arr;
    PrefManager mPref;
    int currMonthInt, currYearInt, currDayInt, weekDayInt;
    String time_next, etime_next, time_to;
    HashMap<String, CoreDataHelper> myPanchangHashMap;
    HashMap<String, CoreDataHelper> mPanchangHashMap;
    String currLang;


    public MyAlaramWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext=context;
        System.out.println("PeriodicWorkRequestStart:min1:");

    }


    @Override
    public Result doWork() {


        mPref = PrefManager.getInstance(mContext);
        mPref.load();

        currLang = mPref.getMyLanguage();

        updateRes();


        res = mContext.getResources();

        onCreateLoader();

        int hrmin,hr,min;
        if ((hrmin = mPref.getSettingReporting1()) > 0) {
            hr = hrmin / 60;
            min = (hrmin - (hrmin / 60) * 60);
            Calendar cal = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.HOUR_OF_DAY, hr);
            cal1.set(Calendar.MINUTE, min);

            long diff = cal1.getTimeInMillis() - cal.getTimeInMillis();
            long  duration = (24 * 60) + (diff / (1000 * 60));

             String TAG="MY_APP_ALARM";
            OneTimeWorkRequest mywork =
                    new OneTimeWorkRequest.Builder(MyAlaramWorker.class)
                            .addTag(TAG)
                            .setInitialDelay(duration, TimeUnit.MINUTES)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                            .build();

            WorkManager.getInstance(mContext).enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, mywork);


        }


        return Result.success();


    }

    private void updateRes() {
        String language = mPref.getMyLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = mContext.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }
    public void onCreateLoader() {
        new DownloadFilesTask().execute();

    }
    private class DownloadFilesTask extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... views) {
            FestivalData.setFestivalData(mPref.getMyLanguage());

            mPanchangHashMap = new HashMap<>();
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            cal.set(Calendar.YEAR, 100 + cal.get(Calendar.YEAR));
            long number = cal.getTimeInMillis();

            long start = number - 1 * 32 * 24 * 60 * 60 * 1000L;
            long end = number + 1 * 32 * 24 * 60 * 60 * 1000L;


            EphemerisDao ephemerisDao = AppDatabase.getInstance(mContext).ephemerisDao();


            List<EphemerisEntity> ephemerisList = ephemerisDao.getAll2(start, end);

            PanchangTask ptObj = new PanchangTask();

            myPanchangHashMap = ptObj.panchangMapping(ephemerisList, mPref.getMyLanguage(), mPref.getLatitude(), mPref.getLongitude(), mContext);

            return null;
        }

        @Override
        protected void onPostExecute(Void views) {
            super.onPostExecute(views);
            try {
                Calendar cal = Calendar.getInstance();
                currYearInt = cal.get(Calendar.YEAR);
                currMonthInt = cal.get(Calendar.MONTH);
                currDayInt = cal.get(Calendar.DAY_OF_MONTH);
                weekDayInt = cal.get(Calendar.DAY_OF_WEEK);

                res = mContext.getResources();
                time_next = res.getString(R.string.l_time_next);
                etime_next = res.getString(R.string.e_time_next);
                time_to = res.getString(R.string.l_time_to);


                if (!myPanchangHashMap.isEmpty()) {
                    mPanchangHashMap.clear();
                    mPanchangHashMap.putAll(myPanchangHashMap);
                    String key = currYearInt + "-" + currMonthInt + "-" + currDayInt;
                    CoreDataHelper dataVal = mPanchangHashMap.get(key);
                    PanchangUtilityLighter pu = new PanchangUtilityLighter(0,mContext, mPref.getMyLanguage(), mPref.getClockFormat(), mPref.getLatitude(), mPref.getLongitude());
                    PanchangUtilityLighter.MyPanchang myPanchangObj = pu.getMyPunchang(dataVal);


                    ArrayList<String> myFestivallist = FestivalData.calculateFestival(dataVal, currLang,mContext);


                    sendLocalNotification2(myPanchangObj, myFestivallist);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void sendLocalNotification2(PanchangUtilityLighter.MyPanchang today, ArrayList<String> myFestivallist) {


        String paksha = today.le_paksha;

        PanchangUtilityLighter.MySubPanchang[] tithiArr = today.le_tithi;
        PanchangUtilityLighter.MySubPanchang[] etithiArr = today.le_tithi;


        StringBuilder tithiStr = getPanchangaValue(tithiArr);
        StringBuilder etithiStr = getPanchangaValue(etithiArr);

        int month = today.le_monthIndex;
        String monthStr = res.getStringArray(R.array.l_arr_month)[month];
        String body = "";
        if (mPref.getMyLanguage().contains("en")) {
            if (myFestivallist.get(0).isEmpty())
                body = etithiStr + "," + today.le_paksha + "," + today.le_ritu;
            else
                body = myFestivallist.get(1) + "," + today.le_paksha + "," + today.le_ritu;

        } else {
            if (myFestivallist.get(0).isEmpty())
                body = tithiStr + "," + paksha;
            else
                body = myFestivallist.get(0) + "," + paksha;

        }


        boolean isNotification = false;

        if (mPref.getNotified("NOTIF_" + res.getString(R.string.e_dina)) == 1 || mPref.getNotified("NOTIF_" + res.getString(R.string.e_dina)) == -1) {
            // sendLocalNotification(res.getString(R.string.eng_o_sankranti));
            isNotification = true;
        }


        if ((mPref.getNotified("NOTIF_" + res.getString(R.string.e_sankranti)) == 1 /*|| mPref.getNotified("NOTIF_" + res.getString(R.string.eng_o_sankranti)) == -1 */)) {
            // sendLocalNotification(res.getString(R.string.eng_o_sankranti));
            isNotification = true;
        }
        tithi_arr = res.getStringArray(R.array.e_arr_tithi);

        for (int i = 0; i < tithi_arr.length; i++) {
            if ((mPref.getNotified("NOTIF_" + tithi_arr[today.le_currTithiIndex]) == 1) && (etithiStr.toString().toLowerCase().contains(tithi_arr[i].toLowerCase()))) {
                isNotification = true;
                break;
            }

        }

        if (isNotification && today != null) {

            String title = Utility.getInstance(mContext).getDayNo(today.le_day) + "-" + monthStr;

            Intent notificationIntent1 = new Intent(mContext, MainActivity.class);
            notificationIntent1.putExtra("weather", 1);
            notificationIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            PendingIntent intent1 = PendingIntent.getActivity(mContext, 55, notificationIntent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            Intent notificationIntent2 = new Intent(mContext,
                    MainActivity.class);
            notificationIntent2.putExtra("horoscope", 2);
            //  notificationIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent intent2 = PendingIntent.getActivity(mContext, 66, notificationIntent2,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            Intent notificationIntent3 = new Intent(mContext,
                    MainActivity.class);
            notificationIntent3.putExtra("birthday", 3);
            notificationIntent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent intent3 = PendingIntent.getActivity(mContext, 77, notificationIntent3,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();


            Bitmap bmp;

            Resources res = mContext.getResources();
            NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext, getChannelId());


            Notification notificationBuilder;

            if (mPref.getMyLanguage().contains("en")) {
                //  title = today.dayNo + "-" + monthStr + "(" + today.eHinduMonth + "-" + today.dayNoLocal + ")";
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                title = df.format(c.getTime());

                inboxStyle.setBigContentTitle(title);

                if (!myFestivallist.get(1).isEmpty())
                    inboxStyle.addLine(myFestivallist.get(1));
                inboxStyle.addLine(etithiStr);
                inboxStyle.addLine(today.le_paksha);
                inboxStyle.addLine(today.le_ritu);

            } else {
                inboxStyle.setBigContentTitle(title);
                if (!myFestivallist.get(0).isEmpty())
                    inboxStyle.addLine(myFestivallist.get(0));
                inboxStyle.addLine(tithiStr);
                inboxStyle.addLine(today.le_paksha);
                inboxStyle.addLine(today.le_ritu);

            }

            notificationBuilder = notification.setContentIntent(intent1)
                    .setSmallIcon(getNotificationIcon())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setContentTitle(title).setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)




                    .setStyle(inboxStyle)
                    .setChannelId(getChannelId())
                    .build();
            //  }
            notificationBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationBuilder.defaults |= Notification.DEFAULT_SOUND;

            notificationBuilder.defaults |= Notification.DEFAULT_VIBRATE;
            final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            final int noteId = 2017;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifChannel(notificationManager);

            }
            notificationManager.notify(noteId, notificationBuilder);

        }



    }

    public String getChannelId() {
        return "calendar_channel_id";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifChannel(NotificationManager mNotificationManager) {
        // The id of the channel.
        String id = getChannelId();
        CharSequence name = "Today Information";
        String description = "Calendar App Today Information";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);

        mNotificationManager.createNotificationChannel(mChannel);
    }

    private StringBuilder getePanchangaValue(PanchangUtilityLighter.MySubPanchang[] arr) {

        StringBuilder arrStr = new StringBuilder();
        try {
            if (arr[0].time.isEmpty()) {
                arrStr.append(arr[0].name);
            } else {
                for (PanchangUtilityLighter.MySubPanchang obj : arr) {
                    String name = obj.name;
                    String time = obj.time;
                    if (time != "")
                        arrStr.append(name).append(" upto ").append(time).append(",");
                    else if (!name.isEmpty())
                        arrStr.append(etime_next + " " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrStr;
    }

    private StringBuilder getPanchangaValue(PanchangUtilityLighter.MySubPanchang[] arr) {
        if (mPref.getMyLanguage().contains("or")) {
            StringBuilder arrStr = new StringBuilder();
            try {
                if (arr[0].time.isEmpty()) {
                    arrStr.append(arr[0].name);
                } else {
                    for (PanchangUtilityLighter.MySubPanchang obj : arr) {
                        String name = obj.name;
                        String time = obj.time;
                        if (!time.isEmpty())
                            arrStr.append(" " + name + " ").append(time + " ").append(time_to).append(",");
                        else if (!name.isEmpty())
                            arrStr.append(time_next + " " + name);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrStr;
        } else {
            return getePanchangaValue(arr);
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_noti_small : R.mipmap.ic_launcher;
    }
}
