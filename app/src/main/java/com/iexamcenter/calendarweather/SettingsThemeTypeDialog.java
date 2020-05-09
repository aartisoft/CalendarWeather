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


public class SettingsThemeTypeDialog extends DialogFragment implements View.OnClickListener {
    private static final int URL_LOADER = 0;
    private AlertDialog.Builder builder;
    private TextView tvName, tvUrl;
    private MySettingsActivity mContext;
    PrefManager mPref;
    int type;
    Resources res;

    public static SettingsThemeTypeDialog newInstance() {
        SettingsThemeTypeDialog f = new SettingsThemeTypeDialog();

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
        View root = inflater.inflate(R.layout.inflate_settings_theme_type, null);

        TextView cancel = root.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsThemeTypeDialog.this.dismiss();
            }
        });
        RadioButton theme1, theme2,theme3;

        theme1 = root.findViewById(R.id.theme1);
        theme2 = root.findViewById(R.id.theme2);
        theme3 = root.findViewById(R.id.theme3);

        theme1.setOnClickListener(this);
        theme2.setOnClickListener(this);
        theme3.setOnClickListener(this);
        int myHomePage = mPref.getThemeType();
        switch (myHomePage) {
            case 0:
                theme1.setChecked(true);
                break;
            case 1:
                //theme1.setChecked(true);
                theme2.setChecked(true);
                break;
            case 2:
                theme3.setChecked(true);
                break;


            default:
                theme1.setChecked(true);
                break;

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
            case R.id.theme1:
                mPref.setThemeType(0);
                break;
            case R.id.theme2:
                mPref.setThemeType(1);
                break;

            case R.id.theme3:
                mPref.setThemeType(2);
                break;
        }
        mPref.load();
       // if (type == R.id.langContainer)
            mContext.refreshThemeType();

        mContext.getSupportFragmentManager().beginTransaction().remove(SettingsThemeTypeDialog.this).commit();

    }
}

