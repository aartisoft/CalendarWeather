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


public class SettingsClockTypeDialog extends DialogFragment implements View.OnClickListener {
    private static final int URL_LOADER = 0;
    private AlertDialog.Builder builder;
    private TextView tvName, tvUrl;
    private MySettingsActivity mContext;
    PrefManager mPref;
    int type;
    Resources res;

    public static SettingsClockTypeDialog newInstance() {
        SettingsClockTypeDialog f = new SettingsClockTypeDialog();

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
        View root = inflater.inflate(R.layout.inflate_settings_clock_type, null);

        TextView cancel = root.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsClockTypeDialog.this.dismiss();
            }
        });
        RadioButton clockType1, clockType2,clockType3,calendarType4;

        clockType1 = root.findViewById(R.id.calType1);
        clockType2 = root.findViewById(R.id.calType2);
        clockType3 = root.findViewById(R.id.calType3);
       // calendarType4 = (RadioButton) root.findViewById(R.id.calType4);

        clockType1.setOnClickListener(this);
        clockType2.setOnClickListener(this);
        clockType3.setOnClickListener(this);
      //  calendarType4.setOnClickListener(this);
        int myHomePage = mPref.getClockFormat();
        switch (myHomePage) {
            case 0:
                clockType1.setChecked(true);
                break;
            case 1:
                clockType2.setChecked(true);
                break;
            case 2:
                clockType3.setChecked(true);
                break;
         //   default:
           //     calendarType4.setChecked(true);
             //   break;

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
            case R.id.calType1:
                mPref.setClockFormat(0);
                break;
            case R.id.calType2:
                mPref.setClockFormat(1);
                break;

            case R.id.calType3:
                mPref.setClockFormat(2);
                break;



        }
        mPref.load();
       // if (type == R.id.langContainer)
            mContext.refreshClockType();

        mContext.getSupportFragmentManager().beginTransaction().remove(SettingsClockTypeDialog.this).commit();

    }
}

