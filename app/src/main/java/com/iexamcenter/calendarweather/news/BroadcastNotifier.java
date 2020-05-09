package com.iexamcenter.calendarweather.news;

/**
 * Created by sasikanta on 2/6/2017.
 */

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadcastNotifier {
    private LocalBroadcastManager mBroadcaster;

    /**
     * Creates a BroadcastNotifier containing an instance of LocalBroadcastManager.
     * LocalBroadcastManager is more efficient than BroadcastManager; because it only
     * broadcasts to components within the app, it doesn't have to do parceling and so forth.
     *
     * @param context a Context from which to get the LocalBroadcastManager
     */
    public BroadcastNotifier(Context context) {
        // Gets an instance of the support library local broadcastmanager
        mBroadcaster = LocalBroadcastManager.getInstance(context);
    }


}