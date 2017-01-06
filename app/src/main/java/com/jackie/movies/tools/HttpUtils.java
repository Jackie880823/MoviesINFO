/*
 *             $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 *             $                                                   $
 *             $                       _oo0oo_                     $
 *             $                      o8888888o                    $
 *             $                      88" . "88                    $
 *             $                      (| -_- |)                    $
 *             $                      0\  =  /0                    $
 *             $                    ___/`-_-'\___                  $
 *             $                  .' \\|     |$ '.                 $
 *             $                 / \\|||  :  |||$ \                $
 *             $                / _||||| -:- |||||- \              $
 *             $               |   | \\\  -  $/ |   |              $
 *             $               | \_|  ''\- -/''  |_/ |             $
 *             $               \  .-\__  '-'  ___/-. /             $
 *             $             ___'. .'  /-_._-\  `. .'___           $
 *             $          ."" '<  `.___\_<|>_/___.' >' "".         $
 *             $         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       $
 *             $         \  \ `_.   \_ __\ /__ _/   .-` /  /       $
 *             $     =====`-.____`.___ \_____/___.-`___.-'=====    $
 *             $                       `=-_-='                     $
 *             $     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   $
 *             $                                                   $
 *             $          Buddha Bless         Never Bug           $
 *             $                                                   $
 *             $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 *
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jackie.movies.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static final OkHttpClient client = new OkHttpClient();

    @NonNull
    private static Request.Builder initBuilder(String url) {
        Log.d(TAG, "initBuilder() called with: url = [" + url + "]");

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        return builder;
    }

    public static void get(Context context, String url, HttpCallBack callBack) {
        Log.d(TAG, "get() called with: url = [" + url + "]");

        Request.Builder builder = initBuilder(url);
        final Request request = builder.get().build();
        client.newCall(request).enqueue(new CallListener(context, callBack));
    }

    private static class CallListener implements Callback {
        private static final String TAG = "CallListener";

        private Context mContext;
        private HttpCallBack mCallBack;

        public CallListener(Context context, HttpCallBack callBack) {
            this.mContext = context;
            mCallBack = callBack;
            mCallBack.onConnect();
        }

        @Override
        public void onFailure(final Call call, final IOException e) {
            Log.e(TAG, "onFailure: [ message = " + e.getLocalizedMessage() + " ]", e);

            if (call.isCanceled()) {
                mCallBack.onCanceled();
            } else {
                mCallBack.onFailure(e);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" +
                    response + "]");
            if (response.isRedirect()) {
                call.enqueue(this);
                return;
            }

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String string = body.string();
                mCallBack.onSuccess(string);
            } else {
                mCallBack.onCanceled();
            }
            response.close();
        }
    }

    public interface HttpCallBack {
        void onConnect();

        void onCanceled();

        void onSuccess(String response);

        void onFailure(IOException e);
    }
}
