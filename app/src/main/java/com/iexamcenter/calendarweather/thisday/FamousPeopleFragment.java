package com.iexamcenter.calendarweather.thisday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.FamousPeopleResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;

import org.json.JSONObject;

import androidx.fragment.app.Fragment;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class FamousPeopleFragment extends Fragment {

    PrefManager mPref;

    String history, quotes, pkey;
    int type = 0;
    TextView famousPeopleTV;

    static private MainActivity mContext;
    View rootView;

    public static FamousPeopleFragment newInstance() {

        return new FamousPeopleFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mContext = (MainActivity) getActivity();

        mContext.showHideBottomNavigationView(false);

        mContext.enableBackButtonViews(true);
        // mContext.showHideToolBarView(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.showHideBottomNavigationView(true);
        mContext.showHideToolBarView(false);

    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            CalendarWeatherApp.updateAppResource(getResources(), mContext);
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            Bundle b = getArguments();
            pkey = b.getString("PKEY");

            history = b.getString("HISTORY");
            type = b.getInt("TYPE");
            createMyView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.frag_famous_people, container, false);
        return rootView;
    }

    private void createMyView() {

        famousPeopleTV = rootView.findViewById(R.id.famousPeople);

        if (type == 0) {

            String[] arr = history.split("\n\n");
            String fullStr = "";
            for (String str : arr
            ) {
                if (!str.isEmpty())
                    fullStr = fullStr + "<p><li>&nbsp;&nbsp;" + str + " </li></p>";

            }
            if (fullStr.isEmpty()) {
                famousPeopleTV.setText("No history found");
            } else
                famousPeopleTV.setText(Html.fromHtml("<p>" + fullStr + "</p>"));
        } else {

            String json1 = "{'PKEY':'" + pkey + "'}";
            loadData(json1, Constant.FAMOUS_PEOPLE_QUOTE_API);

        }


    }


    private void loadData(String profileJson, int api) {
        try {
            HttpRequestObject mReqobject = new HttpRequestObject(mContext);
            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());

            ApiUtil.getIExamCenterBaseURLClass().getFamousPeople(body).enqueue(new Callback<FamousPeopleResponse>() {


                @Override
                public void onResponse(Call<FamousPeopleResponse> call, retrofit2.Response<FamousPeopleResponse> response) {
                    if (response.isSuccessful()) {
                        FamousPeopleResponse res = response.body();

                        String[] arr = res.getQuote().split("\n\n");
                        String fullStr = "";
                        for (String str : arr
                        ) {
                            if (!str.isEmpty())
                                fullStr = fullStr + "<p><li>&nbsp;&nbsp;" + str + "</li></p>";

                        }
                        if (fullStr.isEmpty()) {
                            famousPeopleTV.setText("No quote(s) found");
                        } else
                            famousPeopleTV.setText(Html.fromHtml("<p>" + fullStr + "</p>"));


                    }
                }

                @Override
                public void onFailure(Call<FamousPeopleResponse> call, Throwable t) {
                    //showErrorMessage();
                    // Log.d("xxx", "error loading from API");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
