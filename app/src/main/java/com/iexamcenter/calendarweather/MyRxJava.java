package com.iexamcenter.calendarweather;


import com.iexamcenter.calendarweather.news.ListFragment;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sasikanta on 11/10/2017.
 */

public class MyRxJava {
    private static MyRxJava obj;

    public static MyRxJava newInstance() {
        if (obj == null)
            obj = new MyRxJava();
        return obj;
    }



    public void rssFetchInBackground(final ListFragment listFragment) {

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                listFragment.doRssInBg();
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        listFragment.postExecute();

                    }
                });
    }





}
