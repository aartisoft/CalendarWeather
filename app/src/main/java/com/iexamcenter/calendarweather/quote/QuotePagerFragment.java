package com.iexamcenter.calendarweather.quote;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class QuotePagerFragment extends Fragment {
    public static String ARG_POSITION;
    MyQuoteRecyclerViewAdapter adapter1,adapter2,adapter3;
    ViewGroup rootView;
    int num;
    int pageType = 1;
    MainActivity mContext;
    String[] allAuth, myCat;
    RecyclerView recyclerView;
    List<String> qAuthor, qCategory, qIndianAuth, qRestAuthor;

    public static QuotePagerFragment newInstance() {

        return new QuotePagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quote_list, container, false);
        setRetainInstance(false);


        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;


        return rootView;
    }

    private  class DownloadQuoteTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... urls) {
            myCat = mContext.getResources().getStringArray(R.array.cat);
            allAuth = mContext.getResources().getStringArray(R.array.auth);

            qCategory.clear();
            qRestAuthor.clear();
            qIndianAuth.clear();
            qAuthor.clear();

          //  qIndianAuth = new ArrayList<>();
          //  qRestAuthor = new ArrayList<>();
            qCategory.addAll( Arrays.asList(myCat));
            qAuthor.addAll(Arrays.asList(allAuth));
            for (String item : qAuthor) {
                String[] tmp = item.split(Pattern.quote("|"));
                if (Integer.parseInt(tmp[1]) == 1) {
                    qIndianAuth.add(item);
                } else {
                    qRestAuthor.add(item);
                }
            }
            return 1L;
        }

        protected void onPostExecute(Long result) {
          //  MyQuoteRecyclerViewAdapter adapter1,adapter2,adapter3;

            switch (num) {
                case 0:
                    adapter1.notifyDataSetChanged();
                  //  recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                  //  recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qCategory, mContext, 0));
                    break;
                case 2:
                    adapter2.notifyDataSetChanged();
                   // recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                     // recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qRestAuthor, mContext, 1));
                    break;
                case 1:
                    adapter3.notifyDataSetChanged();
                  //  recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                  //  recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qIndianAuth, mContext, 1));
                    break;
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* Bundle arg = getArguments();

        myCat = mContext.getResources().getStringArray(R.array.cat);
        allAuth = mContext.getResources().getStringArray(R.array.auth);
        //  qIndianAuth = Arrays.asList(myAuth);
        qIndianAuth=new ArrayList<>();
        qRestAuthor=new ArrayList<>();
        qCategory = Arrays.asList(myCat);
        qAuthor = Arrays.asList(allAuth);
        Iterator<String> it = qAuthor.iterator();
        while (it.hasNext()){
            String item = it.next();
            String[] tmp=item.split(Pattern.quote("|"));
            if(Integer.parseInt(tmp[1])==1){
                qIndianAuth.add(item);
            }else{
                qRestAuthor.add(item);
            }
        }*/
        recyclerView = view.findViewById(R.id.list1);

        qCategory=new ArrayList<>();
        qRestAuthor=new ArrayList<>();
        qIndianAuth=new ArrayList<>();
        qAuthor=new ArrayList<>();
        switch (num) {
            case 0:
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter1= new MyQuoteRecyclerViewAdapter(qCategory, mContext, 0);
                recyclerView.setAdapter(adapter1);
                break;
            case 2:
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter2= new MyQuoteRecyclerViewAdapter(qRestAuthor, mContext, 1);
                recyclerView.setAdapter(adapter2);
                break;
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter3= new MyQuoteRecyclerViewAdapter(qIndianAuth, mContext, 1);
                recyclerView.setAdapter(adapter3);
                break;
        }
        new DownloadQuoteTask().execute();


    }

}
