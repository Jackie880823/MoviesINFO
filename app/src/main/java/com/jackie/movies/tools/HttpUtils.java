/*
 *
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
 */

package com.jackie.movies.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
        public static final int HANDLER_MESSAGE_WHAT_CANCELED = 0x3e9;
        public static final int HANDLER_MESSAGE_WHAT_SUCCESS = 0x3ea;
        public static final int HANDLER_MESSAGE_WHAT_FAILURE = 0x3eb;
        public static final String RESPONSE_STR = "response_str";

        private Context mContext;
        private HttpCallBack mCallBack;

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_MESSAGE_WHAT_CANCELED:
                        mCallBack.onCanceled();
                        break;
                    case HANDLER_MESSAGE_WHAT_SUCCESS:
                        mCallBack.onSuccess(msg.getData().getString(RESPONSE_STR));
                        break;
                    case HANDLER_MESSAGE_WHAT_FAILURE:
                        mCallBack.onFailure((IOException) msg.obj);
                        break;
                }
            }
        };

        public CallListener(Context context, HttpCallBack callBack) {
            this.mContext = context;
            mCallBack = callBack;
            mCallBack.onConnect();
        }

        @Override
        public void onFailure(final Call call, final IOException e) {
            Log.e(TAG, "onFailure: [ message = " + e.getLocalizedMessage() + " ]", e);
            Message message = new Message();

            if (call.isCanceled()) {
                message.what = HANDLER_MESSAGE_WHAT_CANCELED;
            } else {
                message.what = HANDLER_MESSAGE_WHAT_FAILURE;
                message.obj = e;
                mCallBack.onFailure(e);
            }
            handler.sendMessage(message);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" +
                    response + "]");
            if (response.isRedirect()) {
                call.enqueue(this);
                return;
            }

            Message message = new Message();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                final String string = body.string();
                Log.d(TAG, "onResponse: body " + string);
                message.what = HANDLER_MESSAGE_WHAT_SUCCESS;
                message.getData().putString(RESPONSE_STR, string);
            } else {
                message.what = HANDLER_MESSAGE_WHAT_CANCELED;
            }
            handler.sendMessage(message);
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
