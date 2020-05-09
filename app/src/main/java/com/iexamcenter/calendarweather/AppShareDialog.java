package com.iexamcenter.calendarweather;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.Calendar;
import java.util.List;


public class AppShareDialog extends DialogFragment {
    TextView txt1, txt2;
    static MainActivity mContext;

    Resources res;
    View rootView;
    ShareDialog shareDialog;

    public static AppShareDialog newInstance(MainActivity ctx) {
        AppShareDialog f = new AppShareDialog();
        mContext = ctx;
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        shareDialog = new ShareDialog(this);

        LayoutInflater inflater = mContext.getLayoutInflater();
        rootView = inflater.inflate(R.layout.share, null);
        Button close = rootView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppShareDialog.this.dismiss();
            }
        });
        LinearLayout fbShare = rootView.findViewById(R.id.fbShare);
        LinearLayout whatsAppShare = rootView.findViewById(R.id.whatsAppShare);
        LinearLayout twitterShare = rootView.findViewById(R.id.twitterShare);
        LinearLayout emailShare = rootView.findViewById(R.id.emailShare);
        LinearLayout moreShare = rootView.findViewById(R.id.moreShare);
        fbShare.setOnClickListener(v -> handleShare("facebook"));
        whatsAppShare.setOnClickListener(v -> handleShare("whatsapp"));
        twitterShare.setOnClickListener(v -> handleShare("twitter"));
        emailShare.setOnClickListener(v -> handleShare("email"));
        moreShare.setOnClickListener(v -> handleShare("more"));

        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    public void handleShare(String type) {
        String appmail;
        Intent shareIntent;
        boolean installed;
        PackageManager pm;
        List<ResolveInfo> activityList;
        switch (type) {
            case "facebook":


                if (ShareDialog.canShow(ShareLinkContent.class) && Connectivity.isConnected(mContext)) {

                    String myLang = Utility.getInstance(mContext).getLanguageFull();
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    String shrYear;
                    if (year <= 2019)
                        shrYear = "2019";
                    else
                        shrYear = "";

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(shrYear + " " + myLang + " " + getResources().getString(R.string.share_title))
                            .setQuote(getResources().getString(R.string.share_desc))
                            .setImageUrl(Uri.parse("http://static.iexamcenter.com/calendarweather/app_share_web.png"))
                            .setContentUrl(Uri.parse("http://iexamcenter.com/" + myLang.toLowerCase() + "/calendar/weather.html"))
                            .build();

                    shareDialog.show(linkContent);
                } else {
                    Utility.getInstance(mContext).newToast(getResources().getString(R.string.no_internet));

                }
                break;
            case "whatsapp":
                appmail = "com.whatsapp";
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.setPackage(appmail);
                installed = appInstalledOrNot(appmail);
                pm = mContext.getPackageManager();


                activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if (appmail.equals(app.activityInfo.packageName) && Connectivity.isConnected(mContext)) {
                        share(app, appmail);
                        break;
                    } else if (installed && appmail.equals(app.activityInfo.packageName) && !Connectivity.isConnected(mContext)) {
                        Utility.getInstance(mContext).newToast(getResources().getString(R.string.no_internet));
                        break;
                    }
                }
                if (!installed) {
                    Utility.getInstance(mContext).newToast(getResources().getString(R.string.app_not_found));

                }
                break;
            case "email":
                appmail = "com.google.android.gm";
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.setPackage(appmail);
                installed = appInstalledOrNot(appmail);
                pm = mContext.getPackageManager();
                activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if (appmail.equals(app.activityInfo.packageName) && Connectivity.isConnected(mContext)) {
                        share(app, appmail);
                        break;
                    } else if (installed && appmail.equals(app.activityInfo.packageName) && !Connectivity.isConnected(mContext)) {
                        Utility.getInstance(mContext).newToast(getResources().getString(R.string.no_internet));
                        break;
                    }
                }
                if (!installed) {
                    Utility.getInstance(mContext).newToast(getResources().getString(R.string.app_not_found));

                }
                break;
            case "twitter":
                String apptwt = "com.twitter.android";
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.setPackage(apptwt);
                pm = mContext.getPackageManager();
                installed = appInstalledOrNot(apptwt);
                activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if (installed && apptwt.equals(app.activityInfo.packageName) && Connectivity.isConnected(mContext)) {
                        share(app, apptwt);
                        break;
                    } else if (installed && apptwt.equals(app.activityInfo.packageName) && !Connectivity.isConnected(mContext)) {
                        Utility.getInstance(mContext).newToast(getResources().getString(R.string.no_internet));
                        break;
                    }
                }
                if (!installed) {
                    Utility.getInstance(mContext).newToast(getResources().getString(R.string.app_not_found));

                }

                break;
            default:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + mContext.getApplicationContext().getPackageName());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Link"));
                break;

        }


    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public void share(ResolveInfo appInfo, String pkg) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);


        String myLang = Utility.getInstance(mContext).getLanguageFull();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String shrYear = "";
        if (year <= 2019)
            shrYear = "2019";
        else
            shrYear = "";
        String title = shrYear + " " + myLang + " " + getResources().getString(R.string.share_title);
        String desc = getResources().getString(R.string.share_desc);
        // String link = "http://iexamcenter.com/" + myLang.toLowerCase() + "/calendar/weather.html";
        String link = "https://play.google.com/store/apps/details?id=com.iexamcenter.calendarweather";


        if (appInfo != null) {
            if (appInfo.activityInfo.packageName.contains("com.google.android.gm")) {
                sendIntent.setType("text/html");
                sendIntent.setPackage(pkg);
                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        Html.fromHtml(new StringBuilder()
                                .append("<a href ='" + link + "'>" + link + "</a>")
                                .append("<small><p>" + desc + "</p></small>")
                                .toString()));

                startActivity(sendIntent);
            } else if (appInfo.activityInfo.packageName.contains("com.twitter.android")) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setPackage(pkg);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            } else {
                sendIntent.setPackage(pkg);
                sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + link);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        }

    }

}

