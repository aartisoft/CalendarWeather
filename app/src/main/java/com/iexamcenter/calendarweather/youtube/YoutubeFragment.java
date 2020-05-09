package com.iexamcenter.calendarweather.youtube;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class YoutubeFragment extends Fragment implements
        YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.PlaybackEventListener {
    public String videoId;
    ArrayList<YoutubeList> hl;
    int videoCnt = 0;
    int currentItem = 0;
    YouTubePlayer mYoutubePlayer;
    View rootView;
    MainActivity mContext;

    public static class YoutubeList {
        public String vKey, lTitle, eTitle;
    }

    public static Fragment newInstance() {
        return new YoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = (MainActivity) getActivity();
        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);

        DateFormat dateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
        mContext.toolbar.setTitle("Prayers and Mantras");
        mContext.toolbar.setSubtitle(today);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.showHideBottomNavigationView(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_youtube, container, false);
        try {
            Bundle b = getArguments();
            String VIDEO_ID = "";
            if (b != null)
                VIDEO_ID = b.getString("VIDEO_ID");
            //int clor = Utility.getInstance(mContext).getPageColors()[currPage];

            PrefManager mPref = PrefManager.getInstance(mContext);
            mPref.load();
            RelativeLayout mainCntr = rootView.findViewById(R.id.mainCntr);
            // mainCntr.setBackgroundColor(clor);

            // FloatingActionButton actionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
            // actionButton.setVisibility(View.VISIBLE);

            String[] l_festival_arr = getResources().getStringArray(R.array.youtube);
            hl = new ArrayList<>();
            int selectedId = -1;
            for (int i = 0; i < l_festival_arr.length; i++) {
                JSONObject festReader = null;
                try {
                    festReader = new JSONObject(l_festival_arr[i]);

                    String key = festReader.getString("key");
                    String titleL = festReader.getString("titleL");
                    String titleE = festReader.getString("titleE");
                    String lang = festReader.getString("lang");
                    YoutubeList obj = new YoutubeList();
                    obj.vKey = key;
                    obj.lTitle = titleL;
                    obj.eTitle = titleE;
                    if (lang.contains("all") || lang.contains(mPref.getMyLanguage()))
                        hl.add(obj);
                    if (key.contains(VIDEO_ID)) {
                        selectedId = i;
                        Log.e("VIDEO_ID", key + "::" + selectedId + "::VIDEO_ID:::");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            Random rand = new Random();
            int randId = rand.nextInt(hl.size());
            if (randId == 0)
                randId = 1;
            currentItem = 0;
            videoCnt = hl.size();
            RecyclerView listview = rootView.findViewById(R.id.video);
            YoutubeListAdapter mAdapter = new YoutubeListAdapter(mContext, hl);
            listview.setLayoutManager(new GridLayoutManager(mContext, 3));
            listview.setHasFixedSize(true);
            listview.setAdapter(mAdapter);

            YouTubePlayerSupportFragment frag =
                    YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.youtube_fragment, frag).commit();

            byte[] mykeyencode = Base64.encode("123456789A123456789IzaSyBqmEkATb123456789KBAZpwx9iHL5e0Qbo-zRH1StI123456789".getBytes(), Base64.DEFAULT);
            byte[] mykeydecode = Base64.decode(mykeyencode, Base64.DEFAULT);
            String mykeydecodestr = new String(mykeydecode);
            // Log.e("mykeyencode", "mykeyencoderryou::" + mykeydecodestr);

            frag.initialize(mykeydecodestr.replace("123456789", ""), this);
            if (selectedId != -1)
                randId = selectedId;
          //  Log.e("VIDEO_ID", selectedId + "::" + randId + "::VIDEO_ID:::" + hl.get(randId).vKey + "::" + hl.get(randId).lTitle);
            playVideo(hl.get(randId).vKey, hl.get(randId).lTitle, hl.get(randId).eTitle, true);

        } catch (Exception e) {
            String errorFeedBack = "YOUTUBEACVT-" + e.getMessage();
            //  CalendarWeatherApp.getInstance().loadData(errorFeedBack, Constant.FEEDBACK_API);
        }

        return rootView;
    }

    public void playVideo(String videoId, String lTitle, String eTitle, boolean autoplay) {

        this.videoId = videoId;
        if (mYoutubePlayer != null) {
            mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

            if (autoplay) {
                mYoutubePlayer.loadVideo(videoId);
            } else {
                mYoutubePlayer.cueVideo(videoId);
            }


        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youTubePlayer.cueVideo(videoId);
            //youTubePlayer.loadVideo(videoId);
            //youTubePlayer.pause();


            mYoutubePlayer = youTubePlayer;
            //mYoutubePlayer.setPlaybackEventListener(this);
            mYoutubePlayer.setPlayerStateChangeListener(this);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {
        if (currentItem < videoCnt) {

            playVideo(hl.get(currentItem).vKey, hl.get(currentItem).lTitle, hl.get(currentItem).eTitle, true);
            currentItem++;

        }
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

}
