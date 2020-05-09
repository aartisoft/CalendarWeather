package com.iexamcenter.calendarweather.quote;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.MyTheme;
import com.iexamcenter.calendarweather.utility.PrefManager;


public class WebviewWhatTodayDialog extends DialogFragment {
    TextView txt1, txt2;
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());
    }

    public static WebviewWhatTodayDialog newInstance(MainActivity ctx) {
        WebviewWhatTodayDialog f = new WebviewWhatTodayDialog();
        mContext = ctx;
        return f;
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
        View rootView = inflater.inflate(R.layout.frag_webview_whattoday, null);

        mPref = PrefManager.getInstance(mContext);
        String NEWS_LINK = getArguments().getString("url");


        lang = mPref.getMyLanguage();
        webView = rootView.findViewById(R.id.webView);
        res = mContext.getResources();
        ImageView close = rootView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewWhatTodayDialog.this.dismiss();
            }
        });
        //  WebSettings webSettings = webView.getSettings();
        // webSettings.setJavaScriptEnabled(false);

        // webView.loadUrl(NEWS_LINK);
      /*  webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });*/
        webView.loadUrl(NEWS_LINK);
       // Log.e("URLLINK::","NEWS_LINK:"+NEWS_LINK);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

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

