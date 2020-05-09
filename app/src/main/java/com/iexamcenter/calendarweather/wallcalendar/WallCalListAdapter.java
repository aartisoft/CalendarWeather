package com.iexamcenter.calendarweather.wallcalendar;

import android.content.res.Resources;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.iexamcenter.calendarweather.FullScreenDialog;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.quote.WebviewWhatTodayDialog;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.MovableFloatingActionButton;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.Calendar;

public class WallCalListAdapter extends RecyclerView.Adapter<WallCalListAdapter.ItemViewHolder> {

    Resources res;
    private static MainActivity activity;

    PrefManager mPref;

    String publicationStr, IMAGE_URL, language_code, msg;
    int selectedYear, pagerPos;
    private static final FrameLayout.LayoutParams ZOOM_PARAMS =
            new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);

    public WallCalListAdapter(MainActivity context, int pagerPos) {
        res = context.getResources();
        activity = context;
        this.pagerPos = pagerPos;
        msg = "Seems calendar not set yet!<br/>Please check connection &amp; try once.";

        mPref = PrefManager.getInstance(activity);
        mPref.load();
        language_code = mPref.getMyLanguage();

        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_wallcal_list;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        setCalendar(holder, position);


    }

    @Override
    public int getItemCount() {
        if (mPref.getMyLanguage().contains("or") && !mPref.getPublication().contains("govtcal")) {
            return 12;
        }
        return 1;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        WebView webview;
        FrameLayout progressCntr;
        MovableFloatingActionButton fab;


        public ItemViewHolder(View rootView) {
            super(rootView);

            webview = rootView.findViewById(R.id.webview);
            progressCntr = rootView.findViewById(R.id.progressCntr);
            fab = rootView.findViewById(R.id.fab);

        }


    }


    private void setCalendar(ItemViewHolder holder, int position) {
        holder.fab.setOnClickListener(v -> {
            Bundle args = new Bundle();

            args.putString("url",(String) v.getTag());



            FragmentTransaction ft0 = activity.getSupportFragmentManager().beginTransaction();
            Fragment prev0 = activity.getSupportFragmentManager().findFragmentByTag("webview1");
            if (prev0 != null) {
                ft0.remove(prev0);
            }
            final DialogFragment webViewDialog = FullScreenDialog.newInstance(activity);
            webViewDialog.setCancelable(true);
            webViewDialog.setArguments(args);
            webViewDialog.show(ft0, "webview1");
        });
        if (!mPref.getMyLanguage().contains("or")) {
            getSetCal(holder, position);
        } else if (mPref.getMyLanguage().contains("or") && mPref.getPublication().contains("kohinoor")) {
            publicationStr = "kohinoor";
            getSetCal(holder, position);
        } else if (mPref.getMyLanguage().contains("or") && mPref.getPublication().contains("biraja")) {
            publicationStr = "biraja";
            getSetCal(holder, position);
        } else if (mPref.getMyLanguage().contains("or") && mPref.getPublication().contains("bhagyadipa")) {
            publicationStr = "bhagyadipa";
            getSetCal(holder, position);
        } else if (mPref.getMyLanguage().contains("or") && mPref.getPublication().contains("radharamana")) {
            publicationStr = "radharamana";
            getSetCal(holder, position);
        } else if (mPref.getMyLanguage().contains("or") && mPref.getPublication().contains("govtcal")) {
            publicationStr = "govtcal";
            getSetCal(holder, position);
        } else {
            publicationStr = "kohinoor";
            getSetCal(holder, position);
        }


    }

    public void getSetCal(ItemViewHolder holder, int position) {
        holder.progressCntr.setVisibility(View.VISIBLE);
        if (pagerPos == 1)
            position = 1;
        //https://www.calendar-365.com/2020-calendar.html
        //https://www.mapsofindia.com/events/2019-holidays-calendar.html [dont delete] ref
        if (mPref.getMyLanguage().contains("or")) {
            IMAGE_URL = "http://static.iexamcenter.com/calendarweather/" + language_code + "/" + publicationStr + "/" + selectedYear + "/" + (position + 1) + ".webp";

        } else {
            IMAGE_URL = "http://static.iexamcenter.com/calendarweather/" + language_code + "/" + selectedYear + "/" + (position + 1) + ".webp";

        }

        holder.fab.setTag(IMAGE_URL);
        holder.webview.getSettings().setSupportZoom(false);
        holder.webview.getSettings().setBuiltInZoomControls(false);


        if (Connectivity.isConnected(activity)) {
            String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=3.0, user-scalable=yes\"/></head><body  style=\"  vertical-align: middle;width:100%;height:100%;padding:0px;margin:0px;background-color:#ffffff\"><IMG style='color:#4f6e8b;font-weight:bold;line-height: 150px;text-align: center;' width='100%' height='100%' alt='Calendar not set yet.' src='" + IMAGE_URL + "'></body></html>";
            holder.webview.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        } else {
            String html = "<html><head><meta name=\"viewport\" content=\"width=device-width,height=device-height, initial-scale=1.0, maximum-scale=3.0, user-scalable=yes\"/></head><body  style=\"  vertical-align: middle;width:100%;height:100%;padding:0px;margin:0px;background-color:#ffffff\"><div style='background-color: #ffffff;width: 100%;    height: auto;    bottom: 0px;    top: 0px;    left: 0;    position: absolute;'>    <div style='position: absolute; width: 300px;  height: 50px;    top: 50%;    left: 50%;    margin-top: -25px;    margin-left: -150px;'>" + msg + "</div></div></body></html>";
            holder.webview.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        }
        holder.progressCntr.setVisibility(View.GONE);

    }

}
