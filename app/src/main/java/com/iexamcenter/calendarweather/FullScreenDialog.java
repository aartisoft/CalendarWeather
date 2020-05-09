package com.iexamcenter.calendarweather;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.iexamcenter.calendarweather.utility.MyTheme;
import com.iexamcenter.calendarweather.utility.PrefManager;


public class FullScreenDialog extends DialogFragment {
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());

    }

    public static FullScreenDialog newInstance(MainActivity ctx) {
        FullScreenDialog f = new FullScreenDialog();
        mContext = ctx;
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyTheme.onActivityCreateSetTheme(mContext);
    }

    WebView webView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.frag_fullwebview, null);

        mPref = PrefManager.getInstance(mContext);
        String IMAGE_URL = getArguments().getString("url");


        lang = mPref.getMyLanguage();
        webView = rootView.findViewById(R.id.webView);
        res = mContext.getResources();
        ImageView close = rootView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.this.dismiss();
            }
        });


        String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.5, maximum-scale=3.0, user-scalable=yes\"/></head><body  style=\"  vertical-align: middle;width:100%;height:100%;padding:0px;margin:0px;background-color:#ffffff\"><IMG style='color:#4f6e8b;font-weight:bold;line-height: 150px;text-align: center;' width='100%' height='100%' alt='Calendar not set yet.' src='" + IMAGE_URL + "'></body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);


        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

