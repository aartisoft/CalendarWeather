package com.iexamcenter.calendarweather.news;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class NewsPublishDialog extends DialogFragment {
    private static final int URL_LOADER = 0;
    private AlertDialog.Builder builder;

   // ArrayList<ChannelVal> al;
    ArrayList<RssEntity> arrayList;
    ChannelPublishListAdapter mAdapter;

    private int type;
    String lat, lng;
    static String timeZoneStr;
    static private MainActivity mContext;
    PrefManager mPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());
    }


    public static NewsPublishDialog newInstance() {
        NewsPublishDialog f = new NewsPublishDialog();
        return f;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // FragmentTransaction ft1 = getFragmentManager().beginTransaction();
      //  Fragment f = getFragmentManager().findFragmentById(R.id.ads_dialog);
       // if (f != null) ft1.remove(f);
       // ft1.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
       // Tracker mTracker = CalendarWeatherApp.getInstance().getDefaultTracker();
       // mTracker.setScreenName(Constant.TRACKER_MAINACTIVITY_DIALOG + "_NEWSSUBSCRIBE");
       // mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    MainViewModel viewModel;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        View root = inflater.inflate(R.layout.fragment_news_subscribe, null);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mPref=PrefManager.getInstance(mContext);
        mPref.load();
        Bundle b = getArguments();
        lat = b.getString("TEMP");

        RecyclerView listview = root.findViewById(R.id.list);
        TextView txt1 = root.findViewById(R.id.txt1);
        txt1.setText("Manage your Channel");
        listview.setLayoutManager(new LinearLayoutManager(mContext));
        listview.setHasFixedSize(true);
     //   al = new ArrayList<>();
        arrayList= new ArrayList<>();
        mAdapter = new ChannelPublishListAdapter(mContext, arrayList);
        listview.setAdapter(mAdapter);
       // this.getLoaderManager().restartLoader(0, null, this);

        viewModel.getAllRss(mPref.getMyLanguage()).observe(this, new Observer<List<RssEntity>>() {
            @Override
            public void onChanged(List<RssEntity> rssEntities) {
                arrayList.clear();
                arrayList.addAll(rssEntities);
                mAdapter.notifyDataSetChanged();
            }
        });




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
                return new RssCursorLoader(mContext, args);

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int pos = 0;
            do {
                String channel = cursor.getString(cursor.getColumnIndex(SqliteHelper.RssEntry.KEY_RSS_CHANNEL));
                int subscribe = cursor.getInt(cursor.getColumnIndex(SqliteHelper.RssEntry.KEY_RSS_SUBSCRIBE));
                int row_id = cursor.getInt(cursor.getColumnIndex(SqliteHelper.RssEntry._ID));
                int publish = cursor.getInt(cursor.getColumnIndex(SqliteHelper.RssEntry.KEY_RSS_PUBLISH));
                int orderby = cursor.getInt(cursor.getColumnIndex(SqliteHelper.RssEntry.KEY_RSS_ORDER));
                int deleted = cursor.getInt(cursor.getColumnIndex(SqliteHelper.RssEntry.KEY_RSS_DELETE));

                ChannelVal obj = new ChannelVal();
                obj.channel = channel;
                obj.subscribe = subscribe;
                obj.publish = publish;
                obj.orderby = orderby;
                obj.row_id = row_id;
                if (deleted == 0) {
                    al.add(pos, obj);
                    pos++;
                }

            } while (cursor.moveToNext());

            mAdapter.notifyDataSetChanged();
        }

        cursor.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class ChannelVal {
        public int row_id, subscribe, publish, orderby;
        public String channel;
    }
*/
}