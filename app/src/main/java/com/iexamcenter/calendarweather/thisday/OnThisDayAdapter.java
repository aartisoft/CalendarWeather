package com.iexamcenter.calendarweather.thisday;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<OnThisDayFragment.OnThisDayItems> mItems;

    int mPage=0;

    public OnThisDayAdapter(MainActivity context, ArrayList<OnThisDayFragment.OnThisDayItems> pglist, int page) {
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




        ItemViewHolder vh = new ItemViewHolder(view, new OnThisDayAdapter.ItemViewHolder.IMyViewHolderInterface() {

            @Override
            public void more(View v) {
                String url = (String) v.getTag();


                if (!Connectivity.isConnected(mContext)) {
                    Utility.getInstance(mContext).newToast("Please switch on internet connection.");
                    return;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
                /*
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

*/

            }


        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final OnThisDayFragment.OnThisDayItems obj = mItems.get(position);


        holder.title2.setText(obj.title);
        int drawable = R.drawable.ic_menu_event;
        holder.title1.setText(position + 1 + " : " + obj.date);

        holder.more.setTag(obj.wiki);
        holder.more.setVisibility(View.GONE);
        if (obj.wiki!=null && !obj.wiki.isEmpty()) {
            holder.more.setVisibility(View.VISIBLE);
        }


        if (obj.type.contains("event"))
            drawable = R.drawable.ic_otd_event;
        else if (obj.type.contains("birth"))
            drawable = R.drawable.ic_otd_bday;
        else if (obj.type.contains("death"))
            drawable = R.drawable.ic_otd_death;
        else if (obj.type.contains("wed"))
            drawable = R.drawable.ic_otd_wedding;
        holder.titleIcon.setImageResource(drawable);

     }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewDetachedFromWindow(OnThisDayAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public TextView title1, title2, more;
        public ImageView titleIcon;

        public LinearLayout container;
        IMyViewHolderInterface mListener;
        public ItemViewHolder(View itemView , IMyViewHolderInterface listener) {
            super(itemView);
            mListener = listener;
            container = itemView.findViewById(R.id.container);

            title1 = itemView.findViewById(R.id.title);
            title2 = itemView.findViewById(R.id.desc);
            more = itemView.findViewById(R.id.more);
            more.setOnClickListener(this);
            titleIcon = itemView.findViewById(R.id.titleIcon);

        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.more:
                    mListener.more(v);
                    break;

            }
        }

        public interface IMyViewHolderInterface {
            void more(View v);

        }

    }
}
