package com.iexamcenter.calendarweather.thisday;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.quote.WebviewWhatTodayDialog;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;

public class ThisDayAdapter extends RecyclerView.Adapter<ThisDayAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<ThisDayFragment.horoscopeItem> mItems;
    private int lastPosition = -1;
    int mPage=0;

    public ThisDayAdapter(MainActivity context, ArrayList<ThisDayFragment.horoscopeItem> pglist, int page) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;

        mPage=page;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_whattodaylist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ThisDayFragment.horoscopeItem obj = mItems.get(position);


        holder.title2.setText(obj.title);
        int drawable = R.drawable.ic_menu_event;
        holder.title1.setText(position + 1 + " : " + obj.date);

        holder.more.setTag(obj.wiki);
        holder.more.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && obj.wiki!=null && !obj.wiki.isEmpty()) {
            holder.more.setVisibility(View.VISIBLE);
        }

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = (String) view.getTag();


                if (!Connectivity.isConnected(mContext)) {
                    Utility.getInstance(mContext).newToast("Please switch on internet connection.");
                    return;
                }

                Bundle args = new Bundle();

                args.putString("url", url);
                args.putInt("page", mPage);


                FragmentTransaction ft0 = mContext.getSupportFragmentManager().beginTransaction();
                Fragment prev0 = mContext.getSupportFragmentManager().findFragmentByTag("webview1");
                if (prev0 != null) {
                    ft0.remove(prev0);
                }
                final DialogFragment webViewDialog = WebviewWhatTodayDialog.newInstance(mContext);
                webViewDialog.setCancelable(true);
                webViewDialog.setArguments(args);
                webViewDialog.show(ft0, "webview1");


            }
        });
        if (obj.type.contains("event"))
            drawable = R.drawable.ic_menu_event;
        else if (obj.type.contains("birth"))
            drawable = R.drawable.ic_menu_birthdays;
        else if (obj.type.contains("death"))
            drawable = R.drawable.ic_menu_deaths;
        else if (obj.type.contains("wed"))
            drawable = R.drawable.ic_menu_weeding;
        holder.title1.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);

     }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewDetachedFromWindow(ThisDayAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2, more;

        public LinearLayout container;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);

            title1 = itemView.findViewById(R.id.title);
            title2 = itemView.findViewById(R.id.desc);
            more = itemView.findViewById(R.id.more);

        }


    }
}
