package com.iexamcenter.calendarweather.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.response.ForecastResponse;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;


public class ForecastDialog extends DialogFragment /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {
    private static final int URL_LOADER = 0;
    private AlertDialog.Builder builder;

    ArrayList<ForecastResponse.WeatherList> al;
    ForecastAdapter mAdapter;

    private int type;
    String lat, lng;
    static String timeZoneStr;
     private MainActivity mContext;

    private String JSONFILEFORCAST = "ONTHISDAYWEATHERFORCAST.txt";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());
    }


    public static ForecastDialog newInstance(String mTimeZoneStr) {
        ForecastDialog f = new ForecastDialog();
        timeZoneStr = mTimeZoneStr;
        return f;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        View root = inflater.inflate(R.layout.fragment_forcast, null);

        //int colr = Utility.getInstance(mContext).getPageThemeColors()[Constant.PAGE_WEATHER];

       // root.setBackgroundResource(colr);

        Bundle b = getArguments();
        type = b.getInt("CONVERTER");
        lat = b.getString("LAT");
        lng = b.getString("LNG");
        //   tvName = (TextView) root.findViewById(R.id.name);
        // tvUrl = (TextView) root.findViewById(R.id.url);
        RecyclerView listview = root.findViewById(R.id.list);
        listview.setLayoutManager(new LinearLayoutManager(mContext));
        listview.setHasFixedSize(true);
        al = new ArrayList<>();

        root.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastDialog.this.dismiss();
            }
        });

        Gson gson = new Gson();
        ForecastResponse res2 = gson.fromJson(Utility.getInstance(mContext).readFromFile( mContext,  JSONFILEFORCAST), ForecastResponse.class);
        if(res2!=null) {
            al.addAll(res2.getList());

            // ArrayList<ForecastResponse.WeatherList> list = data.getList();
         //   mAdapter.notifyDataSetChanged();

        }
        mAdapter = new ForecastAdapter(mContext, al, type, lat, lng, timeZoneStr);
        listview.setAdapter(mAdapter);

     //   getLoaderManager().restartLoader(0, null, ForecastDialog.this);


/*        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getFragmentManager().beginTransaction().remove(ForecastDialog.this).commit();

            }
        });
        */
        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }
/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new ForecastCursorLoader(mContext, args);

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            do {
                String date = cursor.getString(cursor.getColumnIndex(SqliteHelper.ForecastEntry.KEY_FORECAST_DATE));
                String valJson = cursor.getString(cursor.getColumnIndex(SqliteHelper.ForecastEntry.KEY_FORECAST_JSON));
                String type = cursor.getString(cursor.getColumnIndex(SqliteHelper.ForecastEntry.KEY_FORECAST_TYPE));

                if (type.contains("2")) {
                    ForecastResponse data = (new Gson()).fromJson(valJson, ForecastResponse.class);
                    if (data == null && data.getCity() == null)
                        return;
                    data.getCity().getName();
                    data.getCity().getId();
                    al.addAll(data.getList());

                    // ArrayList<ForecastResponse.WeatherList> list = data.getList();
                    mAdapter.notifyDataSetChanged();
                }


            } while (cursor.moveToNext());

        }

        cursor.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    */
}

