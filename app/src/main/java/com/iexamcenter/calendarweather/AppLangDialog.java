package com.iexamcenter.calendarweather;


import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.utility.Helper;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import static android.os.Looper.getMainLooper;


public class AppLangDialog extends DialogFragment {
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;
    MainViewModel viewModel;
    LinearLayout textCntr, langCntr;
    MaterialButton cont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());

    }

    public static AppLangDialog newInstance(MainActivity ctx) {
        AppLangDialog f = new AppLangDialog();
        mContext = ctx;
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mContext == null)
            return null;
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.app_languages, null);
        langCntr = rootView.findViewById(R.id.langCntr);
        textCntr = rootView.findViewById(R.id.textCntr);
        cont = rootView.findViewById(R.id.proceed);
        res = mContext.getResources();
        mPref = PrefManager.getInstance(mContext);
        mPref.load();
        String lang_odia = res.getString(R.string.lang_odia);
        String lang_kannada = res.getString(R.string.lang_kannada);
        String lang_gujarati = res.getString(R.string.lang_gujarati);
        String lang_tamil = res.getString(R.string.lang_tamil);
        String lang_telugu = res.getString(R.string.lang_telugu);
        String lang_punjabi = res.getString(R.string.lang_punjabi);
        String lang_bengali = res.getString(R.string.lang_bengali);
        String lang_malayalam = res.getString(R.string.lang_malayalam);
        String lang_marathi = res.getString(R.string.lang_marathi);
        String lang_hindi = res.getString(R.string.lang_hindi);

        String code_odia = res.getString(R.string.code_odia);
        String code_kannada = res.getString(R.string.code_kannada);
        String code_gujarati = res.getString(R.string.code_gujarati);
        String code_tamil = res.getString(R.string.code_tamil);
        String code_telugu = res.getString(R.string.code_telugu);
        String code_punjabi = res.getString(R.string.code_punjabi);
        String code_bengali = res.getString(R.string.code_bengali);
        String code_malayalam = res.getString(R.string.code_malayalam);
        String code_marathi = res.getString(R.string.code_marathi);
        String code_hindi = res.getString(R.string.code_hindi);

        MaterialButton btn_odia = rootView.findViewById(R.id.lang1);
        MaterialButton btn_kannada = rootView.findViewById(R.id.lang2);
        MaterialButton btn_gujarati = rootView.findViewById(R.id.lang3);
        MaterialButton btn_tamil = rootView.findViewById(R.id.lang4);
        MaterialButton btn_telugu = rootView.findViewById(R.id.lang5);
        MaterialButton btn_punjabi = rootView.findViewById(R.id.lang6);
        MaterialButton btn_bengali = rootView.findViewById(R.id.lang7);
        MaterialButton btn_malayalam = rootView.findViewById(R.id.lang8);
        MaterialButton btn_marathi = rootView.findViewById(R.id.lang9);
        MaterialButton btn_hindi = rootView.findViewById(R.id.lang10);
        MaterialButton btn_en = rootView.findViewById(R.id.lang11);

        btn_odia.setTag(code_odia);
        btn_kannada.setTag(code_kannada);
        btn_gujarati.setTag(code_gujarati);
        btn_tamil.setTag(code_tamil);
        btn_telugu.setTag(code_telugu);
        btn_punjabi.setTag(code_punjabi);
        btn_bengali.setTag(code_bengali);
        btn_malayalam.setTag(code_malayalam);
        btn_marathi.setTag(code_marathi);
        btn_hindi.setTag(code_hindi);
        btn_en.setTag("en");
        btn_odia.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_kannada.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_gujarati.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_tamil.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_telugu.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_punjabi.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_bengali.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_malayalam.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_marathi.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_hindi.setOnClickListener(v -> setLanguage((String) v.getTag()));
        btn_en.setOnClickListener(v -> setLanguage((String) v.getTag()));
        //EE6411
        String str_odia = "<strong>" + lang_odia + "</storng><br/>" + "<font color='#444444'>Odia</font>";
        String str_kannada = "<strong>" + lang_kannada + "</storng><br/>" + "<font color='#444444'>Kannada</font>";
        String str_gujarati = "<strong>" + lang_gujarati + "</storng><br/>" + "<font color='#444444'>Gujarati</font>";
        String str_tamil = "<strong>" + lang_tamil + "</storng><br/>" + "<font color='#444444'>Tamil</font>";
        String str_telugu = "<strong>" + lang_telugu + "</storng><br/>" + "<font color='#444444'>Telugu</font>";
        String str_punjabi = "<strong>" + lang_punjabi + "</storng><br/>" + "<font color='#444444'>Punjabi</font>";
        String str_bengali = "<strong>" + lang_bengali + "</storng><br/>" + "<font color='#444444'>Bangla</font>";
        String str_malayalam = "<strong>" + lang_malayalam + "</storng><br/>" + "<font color='#444444'>Malayalam</font>";
        String str_marathi = "<strong>" + lang_marathi + "</storng><br/>" + "<font color='#444444'>Marathi</font>";
        String str_hindi = "<strong>" + lang_hindi + "</storng><br/>" + "<font color='#444444'>Hindi</font>";
        // String strOdia = "<strong>" + lang_odia + "</storng><br/>" + "<font color='#ff00ff'>Odia</font>";


        btn_odia.setText(Html.fromHtml(str_odia));
        btn_kannada.setText(Html.fromHtml(str_kannada));
        btn_gujarati.setText(Html.fromHtml(str_gujarati));
        btn_tamil.setText(Html.fromHtml(str_tamil));
        btn_telugu.setText(Html.fromHtml(str_telugu));
        btn_punjabi.setText(Html.fromHtml(str_punjabi));
        btn_bengali.setText(Html.fromHtml(str_bengali));
        btn_malayalam.setText(Html.fromHtml(str_malayalam));
        btn_marathi.setText(Html.fromHtml(str_marathi));
        btn_hindi.setText(Html.fromHtml(str_hindi));


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);

        Handler mainHandler = new Handler(getMainLooper());
        Runnable runnable = () -> setUpHome();
        mainHandler.postDelayed(runnable, 200);
        return dialog;

    }

    private void setLanguage(String lang) {
        mPref.setMyLanguage(lang);
        mPref.load();
        setDefaultLatLng();
        cont.setText("One time setup. Loading Calendar..");
        cont.setEnabled(false);
        langCntr.setVisibility(View.GONE);
        textCntr.setVisibility(View.VISIBLE);
         viewModel.setCurrLang(lang);
        Handler mainHandler = new Handler(getMainLooper());
        Runnable runnable = () -> {
            cont.setText("Continue");
            cont.setEnabled(true);
        };
        mainHandler.postDelayed(runnable, 2000);

    }


    public void setUpHome() {
        FragmentManager fm = mContext.getSupportFragmentManager();
        Fragment rootFragment = fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (rootFragment == null)
            rootFragment = RootFragment.newInstance();
        ft.replace(R.id.constraintLayout, rootFragment, AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.addToBackStack(AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.commit();
    }

    public void setDefaultLatLng() {

        switch (mPref.getMyLanguage()) {
            case "or":
                mPref.setLatitude("19.8876");
                mPref.setLongitude("86.0945");
                mPref.setAreaAdmin("Konark Sun Temple, Puri, Odisha");
                break;
            case "bn":
                mPref.setLatitude("22.572645");
                mPref.setLongitude("88.363892");
                mPref.setAreaAdmin("Kolkata, West Bengal");
                break;
            case "hi":
                mPref.setLatitude("28.644800");
                mPref.setLongitude("77.216721");
                mPref.setAreaAdmin("New Delhi, India");
                break;
            case "pa":
                mPref.setLatitude("30.741482");
                mPref.setLongitude("76.768066");
                mPref.setAreaAdmin("Chandigarh, Punjab");
                break;
            case "gu":
                mPref.setLatitude("23.237560");
                mPref.setLongitude("72.647781");
                mPref.setAreaAdmin("Gandhinagar, Gujarat");
                break;
            case "mr":
                mPref.setLatitude("18.516726");
                mPref.setLongitude("73.856255");
                mPref.setAreaAdmin("Pune, Maharashtra");
                break;
            case "kn":
                mPref.setLatitude("12.972442");
                mPref.setLongitude("77.580643");
                mPref.setAreaAdmin("Bengaluru, Karnataka");
                break;
            case "ml":
                mPref.setLatitude("8.524139");
                mPref.setLongitude("76.936638");
                mPref.setAreaAdmin("Thiruvananthapuram, Kerala");
                break;
            case "ta":
                mPref.setLatitude("13.067439");
                mPref.setLongitude("80.237617");
                mPref.setAreaAdmin("Chennai, Tamil Nadu");
                break;
            case "te":
                mPref.setLatitude("17.387140");
                mPref.setLongitude("78.491684");
                mPref.setAreaAdmin("Hyderabad, Telangana");
                break;
            default:
                mPref.setLatitude("28.6129");
                mPref.setLongitude("77.2295");
                mPref.setAreaAdmin("India Gate");

        }
        mPref.load();
    }


}

