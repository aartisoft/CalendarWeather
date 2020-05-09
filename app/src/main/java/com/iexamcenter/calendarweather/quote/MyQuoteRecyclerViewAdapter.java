package com.iexamcenter.calendarweather.quote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MyQuoteRecyclerViewAdapter extends RecyclerView.Adapter<MyQuoteRecyclerViewAdapter.ItemViewHolder> {

    private  List<String> mValues;
    private  Context mContext;
    private  int mType;

    ArrayList<MyQuoteListResponse.MyQuoteList> mylist;

    public void myQuoteList() {
        mylist = new ArrayList<>();
        String[] arrStr = mContext.getResources().getStringArray(R.array.myQuoteArr);
        mylist.clear();
        // String patternRegex = "(?i)<br */?>";
        //  String[] arrStr = quote.split("<br/>");
        //  System.out.println("currentTimeMillis:" + System.currentTimeMillis());
        for (String str : arrStr) {
            String[] arrVal = str.split("@@");
            MyQuoteListResponse.MyQuoteList mql = new MyQuoteListResponse.MyQuoteList();
            mql.quote = arrVal[0];
            mql.author = arrVal[2];
            mql.category = arrVal[1];
            mylist.add(mql);

        }


    }

    public MyQuoteRecyclerViewAdapter(List<String> items, Context listener, int type) {
        mValues = items;
        mContext = listener;
        mType = type;
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                myQuoteList();
                subscriber.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        MyQuoteRecyclerViewAdapter.this.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(mType!=0){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_quote, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_quote_cat, parent, false);
        }

        MyQuoteRecyclerViewAdapter.ItemViewHolder vh;

        vh = new ItemViewHolder(view, new MyQuoteRecyclerViewAdapter.ItemViewHolder.IMyViewHolderClicks() {
            @Override
            public void mWiki(View v) {
                if (!Connectivity.isConnected(mContext)) {
                    Toast.makeText(mContext, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tag = (String) v.getTag();
                String[] tmp = tag.split(Pattern.quote("|"));
                String url = "https://en.wikipedia.org/wiki/" + tmp[3];
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
                /*

                String[] tmp = tag.split(Pattern.quote("|"));
                Bundle args = new Bundle();
                String url = "https://en.wikipedia.org/wiki/" + tmp[3];
                args.putString("url", url);
                args.putInt("page", 0);
                FragmentTransaction ft0 = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                Fragment prev0 = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("webview1");
                if (prev0 != null) {
                    ft0.remove(prev0);
                }
                final DialogFragment webViewDialog = WebviewWhatTodayDialog.newInstance((MainActivity) mContext);
                webViewDialog.setCancelable(true);
                webViewDialog.setArguments(args);
                webViewDialog.show(ft0, "webview1");
                */
                return;
            }

            @Override
            public void mQuote1(View v) {
                ViewGroup vg = (ViewGroup) v.getParent();
                vg.findViewById(R.id.content).performClick();
            }

            @Override
            public void mQuote(View v) {

                String tag = (String) v.getTag();
                if (mylist.size() > 0) {
                    FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                    Fragment prev = ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("QUOTE_INFO");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    final DialogFragment filterDialog = QuoteListDialog.newInstance(tag, mylist);
                    Bundle args = new Bundle();

                    filterDialog.setArguments(args);

                    filterDialog.show(ft, "QUOTE_INFO");
                }
                return;
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        String key = mValues.get(position);
        holder.mLink.setVisibility(View.GONE);
        if (mType == 1) {
            holder.mLink.setTag(key);
            String[] tmp = key.split(Pattern.quote("|"));
            key = tmp[2];
            holder.mLink.setVisibility(View.VISIBLE);
            holder.mContentView.setText(key);
            holder.mContentView.setTag(key);
            Glide.with(mContext).load(tmp[4]).into(holder.mImage);
            holder.mImage.setVisibility(View.VISIBLE);
            holder.mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            holder.mContentView.setText(key);
            holder.mContentView.setTag(key);
            holder.mLink.setVisibility(View.GONE);
            holder.mImage.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private   View mView;
        private  ImageView mLink;
        private  TextView mContentView;
        private  ImageView mImage;
        private  IMyViewHolderClicks mClickListener;

        public ItemViewHolder(View view, IMyViewHolderClicks listener) {
            super(view);
            mClickListener = listener;
            mView = view;
            mLink = view.findViewById(R.id.link);
            mContentView = view.findViewById(R.id.content);
            mContentView.setOnClickListener(this);
            mImage = view.findViewById(R.id.img);
            mImage.setOnClickListener(this);
            mLink.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.content:
                    mClickListener.mQuote(v);
                    break;
                case R.id.img:
                    mClickListener.mQuote1(v);
                    break;
                case R.id.link:
                    mClickListener.mWiki(v);
                    break;
            }

        }

        public interface IMyViewHolderClicks {
            void mQuote(View v);

            void mQuote1(View v);

            void mWiki(View v);


        }
    }


}
