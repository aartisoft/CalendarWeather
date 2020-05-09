package com.iexamcenter.calendarweather.youtube;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.MainViewModel;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.news.NewsListAdapter;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;


public class RssVideoFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    String shareTitle, shareLink;
    public String videoId;
    public static boolean active = false;
    YouTubePlayer mYoutubePlayer;
    private NewsListAdapter mAdapter;
    ArrayList<NewsEntity> mNewsList;
   // private static final int LOADER_NEWS = 1;
    MainActivity mContext;

    Resources res;
    View rootView;

    public static Fragment newInstance() {
        return new RssVideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.showHideBottomNavigationView(true);
    }
    MainViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_youtube, container, false);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        res = getResources();
        Bundle b = getArguments();
        String title = b.getString("TITLE");
        String video_id = b.getString("VIDEO_ID");


        PrefManager mPref = PrefManager.getInstance(mContext);
        mPref.load();

        RecyclerView listview = rootView.findViewById(R.id.video);
        mNewsList=new ArrayList<>();
        mAdapter = new NewsListAdapter(mContext, mNewsList);

        listview.setLayoutManager(new LinearLayoutManager(mContext));
        listview.setHasFixedSize(true);


        listview.setAdapter(mAdapter);


        Bundle bundle = new Bundle();
        bundle.putString("KEY", "Top News");
        bundle.putInt("TYPE", 5);


        byte[] mykeyencode = Base64.encode("123456789AIzaSyBqmEkATb123456789KBAZpwx9iHL5e0Qbo-zRH1StI123456789".getBytes(), Base64.DEFAULT);
        byte[] mykeydecode = Base64.decode(mykeyencode, Base64.DEFAULT);
        String mykeydecodestr=new String(mykeydecode);

       // getLoaderManager().restartLoader(LOADER_NEWS, bundle, this);

        viewModel.getNewsList("Top News",5,0,mPref.getMyLanguage()).observe(getViewLifecycleOwner(), new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> obj) {
                mNewsList.clear();
                if (obj != null) {
                    mNewsList.addAll(obj);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        YouTubePlayerSupportFragment frag = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment, frag).commit();

        frag.initialize(mykeydecodestr.replace("123456789",""), this);
        playVideo(video_id, title, title, true);

        return rootView;
    }

    public void playVideo(String videoId, String lTitle, String eTitle, boolean autoplay) {
        shareTitle = lTitle;
        shareLink = videoId;

        this.videoId = videoId;
        if (mYoutubePlayer != null) {
            mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);


            if (autoplay) {
                mYoutubePlayer.loadVideo(videoId);
            } else {
                mYoutubePlayer.cueVideo(videoId);
            }


        }
    }

/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_NEWS:
                NewsCursorLoader c = new NewsCursorLoader(mContext, args);

                return c;

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_NEWS:
                mAdapter.changeCursor(cursor);
                break;

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_NEWS:
                mAdapter.changeCursor(null);
                break;


        }
    }
*/

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.cueVideo(videoId);


            mYoutubePlayer = youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
