package com.iexamcenter.calendarweather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> fullList;
    private ArrayList<String> arFullList;

    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;
    Context mContext;

    public AutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {

        super(context, resource, textViewResourceId, objects);
        mContext = context;
        fullList = (ArrayList<String>) objects;
        mOriginalValues = new ArrayList<String>(fullList);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    /*
        @Override
        public String getItem(int position) {
            String[] tmp = fullList.get(position).split(" ", 4);
            // arList.add(tmp[3]);
            return tmp[3];//fullList.get(position);
        }
    */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dropdown_menu_popup_item, parent, false);
        }
        if(position%2==0){
            view.setBackgroundColor(Color.LTGRAY);
        }else{
            view.setBackgroundColor(Color.WHITE);
        }

        TextView txt = view.findViewById(R.id.txt1);


        String[] tmp = fullList.get(position).split(" ", 4);


        view.setTag(fullList.get(position));
        txt.setText(tmp[3]);
        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<String>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<String> list = new ArrayList<String>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> values = mOriginalValues;
                int count = values.size();

                ArrayList<String> newValues = new ArrayList<String>(count);

                for (int i = 0; i < count; i++) {
                    String item = values.get(i);

                    String tmp = item.split(" ", 4)[3].split(",")[0];

                    if (tmp.toLowerCase().startsWith(prefixString.toLowerCase())) {
                        newValues.add(item);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                fullList = (ArrayList<String>) results.values;
            } else {
                fullList = new ArrayList<String>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
