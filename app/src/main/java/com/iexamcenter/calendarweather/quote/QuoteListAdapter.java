package com.iexamcenter.calendarweather.quote;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.Iterator;

public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ItemViewHolder> implements Filterable {
    Context mContext;

    int mType;
    private int lastPosition = -1;
    PrefManager mPref;
    ArrayList<MyQuoteListResponse.MyQuoteList> mylist;

    public QuoteListAdapter(Context context, ArrayList<MyQuoteListResponse.MyQuoteList> mylist) {
        this.mylist = mylist;
        mContext = context;
        mPref = PrefManager.getInstance(mContext);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_quotelist;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);


        ItemViewHolder vh = new ItemViewHolder(view, new QuoteListAdapter.ItemViewHolder.IMyViewHolderInterface() {

            @Override
            public void copy(View v) {
                String tag = (String) v.getTag();
                ClipboardManager service = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cd = ClipData.newPlainText("Copy", tag);
                service.setPrimaryClip(cd);
                Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void share(View v) {
                String tag = (String) v.getTag();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, tag);
                sendIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(sendIntent, "Share Quote"));

            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        // String val = mItems.get(position);
        MyQuoteListResponse.MyQuoteList val = mylist.get(position);
        String quote = "";


            quote =val.quote;// Utility.getInstance(mContext).decryptMsg(val.quote);

        holder.title1.setText(quote);
        holder.copy.setTag(quote);
        holder.share.setTag(quote);
        lastPosition = Utility.getInstance(mContext).setAnimation(holder.container, position, lastPosition);


        int px = Utility.getInstance(mContext).dpToPx(0);
        //  int px82= (int)Utility.getInstance(mContext).dpToPx(20);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position == (mylist.size() - 1)) {
            int px1 = Utility.getInstance(mContext).dpToPx(80);
            buttonLayoutParams.setMargins(px, px, px, px1);
            holder.container.setLayoutParams(buttonLayoutParams);
        } else {
            buttonLayoutParams.setMargins(px, px, px, px);
            holder.container.setLayoutParams(buttonLayoutParams);
        }


    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    @Override
    public void onViewDetachedFromWindow(QuoteListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    @Override
    public Filter getFilter() {
        PlanetFilter planetFilter = null;
        if (planetFilter == null)
            planetFilter = new PlanetFilter();
        return planetFilter;
    }

    private class PlanetFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = mylist;
                results.count = mylist.size();
            } else {
                ArrayList<MyQuoteListResponse.MyQuoteList> myNewlist = new ArrayList<>();
                Iterator it = mylist.iterator();
                while (it.hasNext()) {
                    MyQuoteListResponse.MyQuoteList obj = (MyQuoteListResponse.MyQuoteList) it.next();
                    if (obj.author.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        myNewlist.add(obj);
                    }
                    else if (obj.category.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        myNewlist.add(obj);
                    }
                }
                results.values = myNewlist;
                results.count = myNewlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                QuoteListAdapter.this.notifyDataSetChanged();//notifyDataSetInvalidated();
            else {
                mylist.clear();
                mylist = (ArrayList<MyQuoteListResponse.MyQuoteList>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title1;
        public ImageView copy, share;
        public LinearLayout container;
        IMyViewHolderInterface mListener;

        public ItemViewHolder(View itemView, IMyViewHolderInterface listener) {
            super(itemView);
            mListener = listener;
            title1 = itemView.findViewById(R.id.title1);
            copy = itemView.findViewById(R.id.copy);
            share = itemView.findViewById(R.id.share);
            container = itemView.findViewById(R.id.container);
            copy.setOnClickListener(this);
            share.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.copy:
                    mListener.copy(v);
                    break;
                case R.id.share:
                    mListener.share(v);
                    break;
            }
        }

        public interface IMyViewHolderInterface {
            void copy(View v);


            void share(View v);
        }
    }
}
