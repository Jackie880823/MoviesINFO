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

package com.jackie.movies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.data.MovieContract;
import com.jackie.movies.data.MovieProvider;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.entities.MovieEntity;
import com.jackie.movies.tools.HttpUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * Created 16/12/26.
 *
 * @author Jackie
 * @version 1.0
 */

public class UpdateMoviesTask extends AsyncTask<String, Integer, Void>  {
    private static final String TAG = "UpdateMoviesTask";

    private Context mContext;
    private boolean isPopular;

    public UpdateMoviesTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(TAG, "doInBackground() called with: params = [" + params + "]");
        if (params == null || params.length != 2) {
            return null;
        }

        String baseUrl;
        isPopular = Boolean.valueOf(params[0]);
        if (isPopular) {
            baseUrl = Constants.MOVIE_POPULAR;
        } else {
            baseUrl = Constants.MOVIE_TOP_RATED;
        }

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();

        builder.appendQueryParameter(Constants.LANGUAGE_PARAM, Locale.getDefault().getLanguage());
        builder.appendQueryParameter(Constants.PAGE_PARAM, params[1]);
        builder.appendQueryParameter(Constants.API_KEY_PARAM, mContext.getString(R.string
                .api_key_v3_auth));
        HttpUtils.get(mContext, builder.build().toString(), new HttpUtils.HttpCallBack() {

            @Override
            public void onConnect() {
                Log.d(TAG, "onConnect: ");
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "onCanceled: ");
            }

            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
                Gson gson = new GsonBuilder().create();
                MovieEntity entity = gson.fromJson(response, MovieEntity.class);

                if (entity == null) {
                    return;
                }

                insertToDatabase(entity);
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, "onFailure: ", e);
                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    private void insertToDatabase(MovieEntity entity) {
        Log.d(TAG, "insertToDatabase: entity {" + entity.toString() + "}");
        int currentPage = entity.getPage();
        long pageType;
        if (isPopular) {
            pageType = (currentPage << 2) + MovieProvider.TYPE_POPULAR;
        } else {
            pageType = (currentPage << 2) + MovieProvider.TYPE_TOP_RATED;
        }

        String languageCode = Locale.getDefault().getLanguage();
        ContentResolver contentResolver = mContext.getContentResolver();

        ContentValues pageValues = new ContentValues();
        pageValues.put(MovieContract.LANGUAGE_CODE, languageCode);
        pageValues.put(MovieContract.Page.PAGE_TYPE, pageType);
        pageValues.put(MovieContract.Page.TOTAL_RESULTS, entity.getTotal_results());
        pageValues.put(MovieContract.Page.TOTAL_PAGES, entity.getTotal_pages());

        contentResolver.insert(MovieContract.Page.CONTENT_URI, pageValues);

        Vector<ContentValues> valuesVector = new Vector<>();
        for (MovieDetail detail : entity.getResults()) {

            List<Integer> genre_ids = detail.getGenre_ids();

            StringBuilder builder = new StringBuilder();
            for (Integer genre_id : genre_ids) {
                if (builder.length() == 0) {
                    builder.append(genre_id);
                } else {
                    builder.append(",").append(genre_id);
                }
            }

            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.LANGUAGE_CODE, languageCode);
            movieValues.put(MovieContract.Movie.PAGE_TYPE, pageType);
            movieValues.put(MovieContract.Movie.POSTER_PATH, detail.getPoster_path());
            movieValues.put(MovieContract.Movie.ADULT, detail.isAdult());
            movieValues.put(MovieContract.Movie.OVERVIEW, detail.getOverview());
            movieValues.put(MovieContract.Movie.RELEASE_DATE, detail.getRelease_date());
            movieValues.put(MovieContract.Movie.GENRE_IDS, builder.toString());
            movieValues.put(MovieContract.Movie.MOVIE_ID, detail.getId());
            movieValues.put(MovieContract.Movie.ORIGINAL_TITLE, detail.getOriginal_title());
            movieValues.put(MovieContract.Movie.ORIGINAL_LANGUAGE, detail.getOriginal_language());

            movieValues.put(MovieContract.Movie.TITLE, detail.getTitle());
            movieValues.put(MovieContract.Movie.BACKDROP_PATH, detail.getBackdrop_path());
            movieValues.put(MovieContract.Movie.POPULARITY, detail.getPopularity());
            movieValues.put(MovieContract.Movie.VOTE_COUNT, detail.getVote_count());
            movieValues.put(MovieContract.Movie.VIDEO, detail.isVideo() );
            movieValues.put(MovieContract.Movie.VOTE_AVERAGE, detail.getVote_average());
            valuesVector.add(movieValues);
        }

        int inserted = 0;
        if (valuesVector.size() > 0) {
            ContentValues[] cvArrays = new ContentValues[valuesVector.size()];
            valuesVector.toArray(cvArrays);
            inserted = contentResolver.bulkInsert(MovieContract.Movie.CONTENT_URI,
                    cvArrays);
        }
        Log.d(TAG, "insertToDatabase: inserted movie count is " + inserted);
    }
}
