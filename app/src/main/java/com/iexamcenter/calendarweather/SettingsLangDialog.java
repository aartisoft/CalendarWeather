package com.iexamcenter.calendarweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iexamcenter.calendarweather.utility.PrefManager;


public class SettingsLangDialog extends DialogFragment implements View.OnClickListener {
    private static final int URL_LOADER = 0;
    private AlertDialog.Builder builder;
    private TextView tvName, tvUrl;
    private MySettingsActivity mContext;
    PrefManager mPref;
    int type;
    Resources res;

    public static SettingsLangDialog newInstance() {
        SettingsLangDialog f = new SettingsLangDialog();

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        type = b.getInt("type");

        mContext = (MySettingsActivity) getActivity();
        mPref = PrefManager.getInstance(getActivity());
        mPref.load();
        res = mContext.getResources();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View root = inflater.inflate(R.layout.inflate_settings, null);
        TextView cancel = root.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsLangDialog.this.dismiss();
            }
        });

        RadioButton lang_english, lang_bengali, lang_gujarati, lang_hindi, lang_kannada, lang_malayalam, lang_marathi, lang_odia, lang_punjabi, lang_tamil, lang_telugu;
        lang_bengali = root.findViewById(R.id.lang_bengali);
        lang_gujarati = root.findViewById(R.id.lang_gujarati);
        lang_hindi = root.findViewById(R.id.lang_hindi);
        lang_kannada = root.findViewById(R.id.lang_kannada);
        lang_malayalam = root.findViewById(R.id.lang_malayalam);
        lang_marathi = root.findViewById(R.id.lang_marathi);
        lang_odia = root.findViewById(R.id.lang_odia);
        lang_punjabi = root.findViewById(R.id.lang_punjabi);
        lang_tamil = root.findViewById(R.id.lang_tamil);
        lang_telugu = root.findViewById(R.id.lang_telugu);
        lang_english = root.findViewById(R.id.lang_english);

/*
        Typeface face1 = Typeface.createFromAsset(res.getAssets(), "shruti.ttf");
        lang_gujarati.setTypeface(face1);
        Typeface face2 = Typeface.createFromAsset(res.getAssets(), "kalinga.ttf");
        lang_odia.setTypeface(face2);
        Typeface face3 = Typeface.createFromAsset(res.getAssets(), "raavi.ttf");
        lang_punjabi.setTypeface(face3);*/

        lang_bengali.setOnClickListener(this);
        lang_gujarati.setOnClickListener(this);
        lang_hindi.setOnClickListener(this);
        lang_kannada.setOnClickListener(this);
        lang_malayalam.setOnClickListener(this);
        lang_marathi.setOnClickListener(this);
        lang_odia.setOnClickListener(this);
        lang_punjabi.setOnClickListener(this);
        lang_tamil.setOnClickListener(this);
        lang_telugu.setOnClickListener(this);
        lang_english.setOnClickListener(this);
        String myLang = mPref.getMyLanguage();
        if (myLang.contains(res.getString(R.string.code_bengali))) {
            lang_bengali.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_gujarati))) {
            lang_gujarati.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_hindi))) {
            lang_hindi.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_kannada))) {
            lang_kannada.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_malayalam))) {
            lang_malayalam.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_marathi))) {
            lang_marathi.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_odia))) {
            lang_odia.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_punjabi))) {
            lang_punjabi.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_tamil))) {
            lang_tamil.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_telugu))) {
            lang_telugu.setChecked(true);
        } else if (myLang.contains(res.getString(R.string.code_english))) {
            lang_english.setChecked(true);
        }

        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lang_bengali:
                mPref.setMyLanguage(res.getString(R.string.code_bengali));
                break;
            case R.id.lang_gujarati:
                mPref.setMyLanguage(res.getString(R.string.code_gujarati));
                break;
            case R.id.lang_hindi:
                mPref.setMyLanguage(res.getString(R.string.code_hindi));
                break;
            case R.id.lang_kannada:
                mPref.setMyLanguage(res.getString(R.string.code_kannada));
                break;
            case R.id.lang_malayalam:
                mPref.setMyLanguage(res.getString(R.string.code_malayalam));
                break;
            case R.id.lang_marathi:
                mPref.setMyLanguage(res.getString(R.string.code_marathi));
                break;
            case R.id.lang_odia:
                mPref.setMyLanguage(res.getString(R.string.code_odia));
                break;
            case R.id.lang_punjabi:
                mPref.setMyLanguage(res.getString(R.string.code_punjabi));
                break;
            case R.id.lang_tamil:
                mPref.setMyLanguage(res.getString(R.string.code_tamil));
                break;
            case R.id.lang_telugu:
                mPref.setMyLanguage(res.getString(R.string.code_telugu));
                break;
            case R.id.lang_english:
                mPref.setMyLanguage(res.getString(R.string.code_english));
                break;

        }
        mPref.load();
        if (type == R.id.langContainer)
            mContext.refreshLang();

        mContext.getSupportFragmentManager().beginTransaction().remove(SettingsLangDialog.this).commit();

    }
}

