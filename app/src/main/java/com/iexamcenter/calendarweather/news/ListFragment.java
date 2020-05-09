package com.iexamcenter.calendarweather.news;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.MyRxJava;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.AppDatabase;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.ArrayList;
import java.util.List;

//     LoaderManager.LoaderCallbacks<Cursor>
public class ListFragment extends Fragment implements
        PopupMenu.OnMenuItemClickListener {
    public static final String ARG_POSITION = "ARG_POSITION";
    MainActivity mContext;
    String mKey = "Top News";
    int mType = 1;

    FloatingActionButton mypopup;
   // private static final int LOADER_NEWS = 2000;
    private NewsListAdapter mAdapter;
    private RecyclerView listview;
    PrefManager mPref;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
   // private BroadcastReceiver mRssDataUpdateReceiver;
  //  IntentFilter mRssDataUpdateFilter;

   // private BroadcastReceiver mNewsDownloadReceiver;
   // IntentFilter mNewsDownloadFilter;
    List<String> channelList, hindiChannelList, englishChannelList, categoriesList, titleList, autoCompList;
    TextView searchBy, no_result;
    ArrayList<NewsEntity> mNewsList;
    ArrayAdapter<String> catDataAdapter;
    private AutoCompleteTextView actv;

    public static Fragment getInstance() {
        return new ListFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSearchByText();
        actv.clearListSelection();
        actv.setText("");
        mKey = "Top News";
        mType = 0;

        actv.setAdapter(null);
        autoCompList.clear();
        autoCompList.addAll(channelList);
        catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
        catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
        actv.setAdapter(catDataAdapter);
        catDataAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void getNews(String key, int type,String selLang) {


        mKey = key;
        mType = type;
        if (mType > 1 && actv.getText().toString().isEmpty()) {
            mKey = "";
        }
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);

        bundle.putString("KEY", mKey);
        loadNews(mKey, type, 0, selLang);

        // getLoaderManager().restartLoader(LOADER_NEWS, bundle, ListFragment.this);
        if (Connectivity.isConnected(mContext) && type != 3) {
            no_result.setText(mContext.getResources().getString(R.string.no_result));
            String UPDATE_KEY = mPref.getMyLanguage() + "--" + mKey + "--" + mType;
            long lastUpdateTime = mPref.getNewsUpdateTime(UPDATE_KEY);
            long currTime = System.currentTimeMillis();
            if (lastUpdateTime != -1L && ((currTime - lastUpdateTime) > Constant.MIN_NEWS_PULL_TIME)) {

                progressBar.setVisibility(View.VISIBLE);
                new DownloadTask(mContext, mKey, mType);
            } else if (lastUpdateTime == -1L) {

                progressBar.setVisibility(View.VISIBLE);
                new DownloadTask(mContext, mKey, mType);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            no_result.setText(mContext.getResources().getString(R.string.refresh));
            Utility.getInstance(mContext).newToast("Please check internet.");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void setAutoCompData(MenuItem item) {
        searchBy.setText(item.getTitle() + ":");
        actv.clearListSelection();
        actv.setText("");
        mKey = "";
        actv.setAdapter(null);
        autoCompList.clear();
        autoCompList.addAll(channelList);
        catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
        catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
        actv.setAdapter(catDataAdapter);
        catDataAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.news_publish:
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                Fragment prev1 = getFragmentManager().findFragmentByTag("NEWSPUBLISH");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                final DialogFragment filterDialog1 = NewsPublishDialog.newInstance();

                Bundle args1 = new Bundle();
                args1.putString("TEMP", "txt");

                filterDialog1.setArguments(args1);
                filterDialog1.setCancelable(true);
                filterDialog1.show(ft1, "NEWSPUBLISH");

                break;

            case R.id.news_cricket:
                mType = 1;
                // setAutoCompData(item);
                getNews("Cricket", mType,"");
                break;

            case R.id.news_movie:
                mType = 1;
                // setAutoCompData(item);
                getNews("Entertainment", mType,"");
                break;

            case R.id.news_music:
                mType = 1;
                // setAutoCompData(item);
                getNews("Music", mType,"");
                break;

            case R.id.search_by_bengali_news:
                mType = 102;
                setAutoCompData(item);
                getNews("", mType,"bn");
                break;
            case R.id.search_by_gujarati_news:
                mType = 103;
                setAutoCompData(item);
                getNews("", mType,"gu");
                break;
            case R.id.search_by_kannada_news:
                mType = 104;
                setAutoCompData(item);
                getNews("", mType,"kn");
                break;
            case R.id.search_by_malayalam_news:
                mType = 105;
                setAutoCompData(item);
                getNews("", mType,"ml");
                break;
            case R.id.search_by_marathi_news:
                mType = 106;
                setAutoCompData(item);
                getNews("", mType,"mr");
                break;
            case R.id.search_by_odia_news:
                mType = 107;

                setAutoCompData(item);
                getNews("", mType,"or");
                break;
            case R.id.search_by_punjabi_news:
                mType = 108;
                setAutoCompData(item);
                getNews("", mType,"pa");
                break;
            case R.id.search_by_tamil_news:
                mType = 109;
                setAutoCompData(item);
                getNews("", mType,"ta");
                break;
            case R.id.search_by_telugu_news:
                mType = 110;
                setAutoCompData(item);
                getNews("", mType,"te");
                break;
            case R.id.search_by_hindi_news:
                mType = 101;
                setAutoCompData(item);
                getNews("", mType,"hi");
                break;
            case R.id.search_by_english_news:
                mType = 100;

                setAutoCompData(item);
                getNews("", mType,"en");
                break;


            case R.id.search_by_category:
                searchBy.setText("Category:");
                mType = 1;
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(categoriesList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);
                catDataAdapter.notifyDataSetChanged();

                break;
            case R.id.search_by_bengali_channel:
                mType = 102;

                setAutoCompData(item);
                break;
            case R.id.search_by_gujarati_channel:
                mType = 103;
                setAutoCompData(item);
                break;
            case R.id.search_by_kannada_channel:
                mType = 104;
                setAutoCompData(item);
                break;
            case R.id.search_by_malayalam_channel:
                mType = 105;
                setAutoCompData(item);
                break;
            case R.id.search_by_marathi_channel:
                mType = 106;
                setAutoCompData(item);
                break;
            case R.id.search_by_odia_channel:
                mType = 107;
                setAutoCompData(item);
                break;
            case R.id.search_by_punjabi_channel:
                mType = 108;
                setAutoCompData(item);
                break;
            case R.id.search_by_tamil_channel:
                mType = 109;
                setAutoCompData(item);
                break;
            case R.id.search_by_telugu_channel:
                mType = 110;
                setAutoCompData(item);
                break;
            case R.id.search_by_hindi_channel:
                searchBy.setText(item.getTitle() + ":");
                mType = 101;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(hindiChannelList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();


                break;
            case R.id.search_by_english_channel:
                searchBy.setText(item.getTitle() + ":");
                mType = 100;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(englishChannelList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();


                break;
            case R.id.search_by_title:
                searchBy.setText("Title:");
                mType = 3;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(titleList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    MainViewModel viewModel;

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            setHasOptionsMenu(true);
            viewModel = new ViewModelProvider(this).get(MainViewModel.class);


            mPref = PrefManager.getInstance(mContext);
            mPref.load();

            searchBy = rootView.findViewById(R.id.search_by);
            no_result = rootView.findViewById(R.id.no_result);
            mypopup = rootView.findViewById(R.id.mypopup);
            progressBar = rootView.findViewById(R.id.progressBar);
            setSearchByText();
            actv = rootView.findViewById(R.id.search);


            actv.setOnTouchListener((paramView, paramMotionEvent) -> {
                actv.showDropDown();
                actv.requestFocus();

                return false;
            });

            autoCompList = new ArrayList<String>();
            titleList = new ArrayList<String>();
            channelList = new ArrayList<String>();
            hindiChannelList = new ArrayList<String>();
            englishChannelList = new ArrayList<String>();
            categoriesList = new ArrayList<String>();


            catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
            catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
            actv.setAdapter(catDataAdapter);
            actv.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mKey = "" + v.getText();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                    }
                    setTypeVal();
                    getNews(mKey, mType,"");

                    return true;
                }
                return false;
            });
            actv.setOnItemClickListener((parent, view, position, id) -> {
                mKey = (String) parent.getItemAtPosition(position);
                setTypeVal();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                }
                getNews(mKey, mType,"");


            });
            swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);

            swipeRefreshLayout.setOnRefreshListener(
                    () -> getNews(mKey, mType,"")
            );
            mypopup.setOnClickListener(v -> showPopup(v, 2));

            searchBy.setOnClickListener(v -> showPopup(v, 1));

            listview = rootView.findViewById(R.id.recyclerview);
            mNewsList = new ArrayList<>();
            mAdapter = new NewsListAdapter(mContext, mNewsList);

            listview.setLayoutManager(new LinearLayoutManager(mContext));
            listview.setHasFixedSize(true);


            listview.setAdapter(mAdapter);

            no_result.setOnClickListener(v -> getNews("Top News", 0,""));

            getNews("Top News", 0,"");
          /*  mNewsDownloadFilter = new IntentFilter("NEWS_DOWNLOAD_RECEIVER");

            mNewsDownloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String key = intent.getStringExtra("KEY");
                        if (mKey.equalsIgnoreCase(key)) {
                            new Handler().postDelayed(() -> {

                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("KEY", mKey);
                                    bundle.putInt("TYPE", mType);
                                    loadNews(mKey,mType,0,"");
                                   // getLoaderManager().restartLoader(LOADER_NEWS, bundle, (LoaderManager.LoaderCallbacks<Cursor>) ListFragment.this);

                                    swipeRefreshLayout.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }, 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };*/

/*
            mRssDataUpdateFilter = new IntentFilter("RSS_DATA_UPDATE");
            mRssDataUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {


                        String key = intent.getStringExtra("KEY");
                        Utility.getInstance(mContext).newToast(key);
                        doExecuteBg();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };*/


            //   mContext.registerReceiver(mRssDataUpdateReceiver, mRssDataUpdateFilter);
            //  mContext.registerReceiver(mNewsDownloadReceiver, mNewsDownloadFilter);

            doExecuteBg();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            //  mContext.unregisterReceiver(mNewsDownloadReceiver);
            // mContext.unregisterReceiver(mRssDataUpdateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNews(String key, int type, int rowId, String lang) {
        if(lang.isEmpty())
            lang=mPref.getMyLanguage();

        if(viewModel.getNewsList(key, type, rowId, lang).hasObservers()){
            viewModel.getNewsList(key, type, rowId, lang).removeObservers(this);
        }
        viewModel.getNewsList(key, type, rowId, lang).observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> obj) {
                mNewsList.clear();
                if (obj != null) {
                    mNewsList.addAll(obj);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("loadNews", "loadNews::" + obj.size());
                }
            }
        });


    }

    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        switch (id) {
            case LOADER_NEWS:


                NewsCursorLoader c = new NewsCursorLoader(mContext, args);

                return c;


        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if (cursor.getCount() == 0) {
            no_result.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            no_result.setVisibility(View.GONE);
        }

        mAdapter.changeCursor(cursor);


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
        loader.reset();


    }
*/
    public void doRssInBg() {
        hindiChannelList.clear();
        englishChannelList.clear();
        channelList.clear();
        String lang = mPref.getMyLanguage();

        List<RssEntity> rssList = AppDatabase.getInstance(mContext).rssDao().getChannelRegHiEn(lang);

      /*  Uri url = SqliteHelper.CONTENT_URI_RSS;
        final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.RssEntry.TABLE_RSS);

        String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " IN (?,?,?)";
        String[] selectionArgs = {lang, "hi", "en"};

        String projection[] = new String[]{SqliteHelper.RssEntry.KEY_RSS_CHANNEL, SqliteHelper.RssEntry.KEY_RSS_LANG};
        String sort = SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " ASC";
        Cursor cursor = mContext.getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);*/
        channelList.clear();
        int size=rssList.size();
        if (size>0) {

            for(int i=0;i<size;i++) {
                RssEntity obj = rssList.get(i);
                String language =obj.rssLang;// cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_LANG));

                String channel = obj.rssChannel;//cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CHANNEL));

                if (language.contains("hi"))
                    hindiChannelList.add(channel);
                else if (language.contains("en"))
                    englishChannelList.add(channel);
                else
                    channelList.add(channel);
            } //while (cursor.moveToNext());

        }

       /*cursor.close();
        String projection1[] = new String[]{"distinct " + SqliteHelper.RssEntry.KEY_RSS_CAT};
        sort = SqliteHelper.RssEntry.KEY_RSS_CAT + " ASC";
        cursor = mContext.getContentResolver().query(contenturi, projection1, selection, selectionArgs, sort);

        */
        categoriesList.clear();
        List<String> rssListStr = AppDatabase.getInstance(mContext).rssDao().getCatRegHiEn(lang);

         size=rssListStr.size();
        if (size>0) {

            for (int i = 0; i < size; i++) {
                String category  = rssListStr.get(i);
               // String category = obj.rssCat;//cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CAT));
                categoriesList.add(category);
            }
        }
       /* if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CAT));
                categoriesList.add(category);
            } while (cursor.moveToNext());

        }
        cursor.close();*/
    }

    public void postExecute() {
        refreshChannelVal();
    }

    public void doExecuteBg() {

        MyRxJava.newInstance().rssFetchInBackground(ListFragment.this);
    }


    public void showPopup(View v, int type) {
        PopupMenu popup = new PopupMenu(mContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        Menu m = popup.getMenu();

        if (type == 1) {
            inflater.inflate(R.menu.news, m);
            if (mPref.getMyLanguage().contains("bn")) {
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("gu")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("hi")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("kn")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("ml")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("mr")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("or")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("pa")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("ta")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("te")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
            } else if (mPref.getMyLanguage().contains("en")) {

                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_hindi_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            }

        } else {
            inflater.inflate(R.menu.news_filter, m);
            if (mPref.getMyLanguage().contains("bn")) {
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("gu")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("hi")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("kn")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("ml")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("mr")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("or")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("pa")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("ta")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("te")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
            } else if (mPref.getMyLanguage().contains("en")) {

                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_hindi_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            }

        }

        popup.show();
    }

    public void setSearchByText() {
        if (mPref.getMyLanguage().contains("bn"))
            searchBy.setText("Bangla Channel:");
        else if (mPref.getMyLanguage().contains("gu"))
            searchBy.setText("Gujarati Channel:");
        else if (mPref.getMyLanguage().contains("hi"))
            searchBy.setText("Hindi Channel:");
        else if (mPref.getMyLanguage().contains("kn"))
            searchBy.setText("Kannada Channel:");
        else if (mPref.getMyLanguage().contains("ml"))
            searchBy.setText("Malayalam Channel:");
        else if (mPref.getMyLanguage().contains("mr"))
            searchBy.setText("Marathi Channel:");
        else if (mPref.getMyLanguage().contains("or"))
            searchBy.setText("Odia Channel:");
        else if (mPref.getMyLanguage().contains("pa"))
            searchBy.setText("Punjabi Channel:");
        else if (mPref.getMyLanguage().contains("ta"))
            searchBy.setText("Tamil Channel:");
        else if (mPref.getMyLanguage().contains("te"))
            searchBy.setText("Telugu Channel:");
        else if (mPref.getMyLanguage().contains("en"))
            searchBy.setText("English Channel:");

    }

    public void setTypeVal() {
        String searchByTxt = searchBy.getText().toString();
        if (searchByTxt.contains("Bangla Channel:")) {
            mType = 102;

        }
        else if (searchByTxt.contains("Gujarati Channel:"))
            mType = 103;
        else if (searchByTxt.contains("Hindi Channel:"))
            mType = 101;
        else if (searchByTxt.contains("Kannada Channel:"))
            mType = 104;
        else if (searchByTxt.contains("Malayalam Channel:"))
            mType = 105;
        else if (searchByTxt.contains("Marathi Channel:"))
            mType = 106;
        else if (searchByTxt.contains("Odia Channel:"))
            mType = 107;
        else if (searchByTxt.contains("Punjabi Channel:"))
            mType = 108;
        else if (searchByTxt.contains("Tamil Channel:"))
            mType = 109;
        else if (searchByTxt.contains("Telugu Channel:"))
            mType = 110;
        else if (searchByTxt.contains("English Channel:"))
            mType = 100;

    }

    public void refreshChannelVal() {
        String searchByTxt = searchBy.getText().toString();
        autoCompList.clear();
        if (searchByTxt.contains("Bangla Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Gujarati Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Hindi Channel:")) {

            autoCompList.addAll(hindiChannelList);
        } else if (searchByTxt.contains("Kannada Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Malayalam Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Marathi Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Odia Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Punjabi Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Tamil Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Telugu Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("English Channel:")) {

            autoCompList.addAll(englishChannelList);
        } else {
            autoCompList.addAll(channelList);
        }
        catDataAdapter.notifyDataSetChanged();

    }
}
