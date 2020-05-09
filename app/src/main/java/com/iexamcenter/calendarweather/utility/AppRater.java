package com.iexamcenter.calendarweather.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.FeedbackResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.os.Looper.getMainLooper;

public class AppRater {
    private final static String APP_TITLE = "Panchanga Darpana";
    private static String PACKAGE_NAME = "com.iexamcenter.calendarweather";
    private static int DAYS_UNTIL_PROMPT = 5;
    private static int LAUNCHES_UNTIL_PROMPT = 15;
    private static long EXTRA_DAYS;
    private static long EXTRA_LAUCHES;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Activity mContext;
    private static Dialog dialog;

    public static void app_launched(Activity activity1, boolean isForce) {
        if (!isForce)
            return;
        mContext = activity1;
        PACKAGE_NAME = mContext.getPackageName();

        prefs = mContext.getSharedPreferences("apprater", Context.MODE_PRIVATE);
        editor = prefs.edit();

        EXTRA_DAYS = prefs.getLong("extra_days", 0);
        EXTRA_LAUCHES = prefs.getLong("extra_launches", 0);
        editor = prefs.edit();

        EXTRA_DAYS = prefs.getLong("extra_days", 0);
        EXTRA_LAUCHES = prefs.getLong("extra_launches", 0);

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        editor.commit();

        showRateDialog();
    }

    public static void app_launched(Activity activity1) {
        Log.e("app_launched", "app_launched:1");
        mContext = activity1;
         PACKAGE_NAME = mContext.getPackageName();

        prefs = mContext.getSharedPreferences("apprater", Context.MODE_PRIVATE);
        // if (prefs.getBoolean("dontshowagain", false))
        //   return;

        editor = prefs.edit();

        EXTRA_DAYS = prefs.getLong("extra_days", 0);
        EXTRA_LAUCHES = prefs.getLong("extra_launches", 0);

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        // showRateDialog();
        // Wait at least n days before opening
        Log.e("app_launched", "app_launched:2");
        if (launch_count >= (LAUNCHES_UNTIL_PROMPT + EXTRA_LAUCHES)) {
            Log.e("app_launched", launch_count + "app_launcheddd:" + EXTRA_LAUCHES + "::" + EXTRA_DAYS + "::" + (LAUNCHES_UNTIL_PROMPT + EXTRA_LAUCHES));

            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) + EXTRA_DAYS) {

                Handler mainHandler = new Handler(getMainLooper());
                Runnable runnable = () ->  showRateDialog();
                mainHandler.postDelayed(runnable, 5000);


            }
        }

        editor.commit();
    }

    public static void showRateDialog() {
        //    final Dialog dialog = new Dialog(mContext);
        dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_apprater, null);
        TextView notnow = rootView.findViewById(R.id.notnow);
        final TextView rate = rootView.findViewById(R.id.rate);
        final LinearLayout reviewcntr = rootView.findViewById(R.id.reviewcntr);
        final LinearLayout playstoreCntr = rootView.findViewById(R.id.playstoreCntr);
        final RatingBar star = rootView.findViewById(R.id.star);
        final LinearLayout myratecontanr = rootView.findViewById(R.id.myratecontanr);
        final EditText review = rootView.findViewById(R.id.review);
        rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar", mContext);
                if (rate.getText().toString().contains("PLAY STORE")) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
                    delayDays(365);
                    delayLaunches(1000);
                    dialog.dismiss();
                } else if (star.getRating() > 4f && reviewcntr.getVisibility() == View.GONE) {
                    star.setIsIndicator(true);
                    playstoreCntr.setVisibility(View.VISIBLE);
                    //  myratecontanr.setVisibility(View.INVISIBLE);
                    rate.setText("PLAY STORE");
                    delayDays(180);
                    delayLaunches(360);
                } else if (star.getRating() > 0f && reviewcntr.getVisibility() == View.GONE) {
                    reviewcntr.setVisibility(View.VISIBLE);
                    delayDays(180);
                    delayLaunches(360);

                } else if (star.getRating() > 0f && reviewcntr.getVisibility() == View.VISIBLE) {
                    Toast.makeText(mContext, "Thank You!", Toast.LENGTH_SHORT).show();
                    String feedback = "Star:" + star.getRating() + "Rev:" + review.getText();
                    feedback = feedback.replaceAll("'", " ");
                    feedback = feedback.replaceAll("\"", " ");
                    String json1 = "{'FEEDBACK':'" + feedback + "'}";
                    loadData(json1, Constant.FEEDBACK_API);
                    delayDays(180);
                    delayLaunches(360);
                    dialog.dismiss();

                } else {
                    Toast.makeText(mContext, "Please select star.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        notnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  Configs.sendHitEvents(Configs.APP_RATER, Configs.CATEGORIA_ANALYTICS, "Clique", "Avaliar Mais Tarde", mContext);
                delayDays(180);
                delayLaunches(360);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        //  dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(rootView);
        dialog.show();
    }

    private static void delayLaunches(int numberOfLaunches) {
        long extra_launches = prefs.getLong("extra_launches", 0) + numberOfLaunches;
        editor.putLong("extra_launches", extra_launches);
        editor.commit();
    }

    private static void delayDays(int numberOfDays) {
        Long extra_days = prefs.getLong("extra_days", 0) + (numberOfDays * 1000 * 60 * 60 * 24);
        editor.putLong("extra_days", extra_days);
        editor.commit();
    }

    private static void loadData(String profileJson, int api) {
        try {


            HttpRequestObject mReqobject = new HttpRequestObject(mContext);

            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());
            ApiUtil.getIExamCenterBaseURLClass().sendFeedBack(body).enqueue(new Callback<FeedbackResponse>() {
                @Override
                public void onResponse
                        (Call<FeedbackResponse> call, retrofit2.Response<FeedbackResponse> response) {

                }

                @Override
                public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                    t.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}