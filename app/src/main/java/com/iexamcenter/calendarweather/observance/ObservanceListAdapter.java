package com.iexamcenter.calendarweather.observance;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;

public class ObservanceListAdapter extends RecyclerView.Adapter<ObservanceListAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<ObservanceFragment.Holiday> mItems;
    private int lastPosition = -1;

    public ObservanceListAdapter(MainActivity context, ArrayList<ObservanceFragment.Holiday> pglist) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_observancelist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);


        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }
public void more(View v){
    String url = (String) v.getTag();
   // Log.e("URLLINK::","NEWS_LINK::url:"+url);
    if (!Connectivity.isConnected(mContext)) {
        Utility.getInstance(mContext).newToast("Please switch on internet connection.");
        return;
    }

    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    mContext.startActivity(browserIntent);


    /*Bundle args = new Bundle();
    args.putString("url", url);
    FragmentTransaction ft0 = mContext.getSupportFragmentManager().beginTransaction();
    Fragment prev0 = mContext.getSupportFragmentManager().findFragmentByTag("webview");
    if (prev0 != null) {
        ft0.remove(prev0);
    }
    final DialogFragment webViewDialog = WebviewWhatTodayDialog.newInstance(mContext);
    webViewDialog.setCancelable(true);
    webViewDialog.setArguments(args);
    webViewDialog.show(ft0, "webview");
    */

}
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ObservanceFragment.Holiday obj = mItems.get(position);


        String dateStr = Utility.getInstance(mContext).getDateFromString(obj.hdate);
        holder.title1.setText(dateStr + "(" + obj.holiday + ")");
        holder.title2.setText(obj.hday);
        holder.more.setTag(obj.wiki);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                more(view);
            }
        });
       // holder.title3.setVisibility(View.VISIBLE);

       // Log.e("URLLINK::","NEWS_LINK:::"+obj.wiki);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2, more;


        public LinearLayout container;
        public ItemViewHolder(View itemView ) {
            super(itemView);
            container =  itemView.findViewById(R.id.container);
            title1 =  itemView.findViewById(R.id.date);
            title2 = itemView.findViewById(R.id.title);
            more = itemView.findViewById(R.id.more);
           // more.setOnClickListener(this);

        }


    }
}
