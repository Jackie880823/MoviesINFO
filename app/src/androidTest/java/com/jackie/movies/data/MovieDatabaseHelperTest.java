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

package com.jackie.movies.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.R;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.entities.MovieEntity;

import java.util.List;
import java.util.Locale;

/**
 * Created 16/12/22.
 *
 * @author Jackie
 * @version 1.0
 */
public class MovieDatabaseHelperTest extends AndroidTestCase {
    private static final String TAG = "MovieDatabaseHelperTest";

    private void deleteDatabase() {
        mContext.deleteDatabase(MovieDatabaseHelper.DATABASE_NAME);
    }

    public void testCreateDatabase() {
        deleteDatabase();
        int ss = 3 << 2;
        assertTrue("the value is " + ss, ss == 0b1100);
        SQLiteDatabase sql = new MovieDatabaseHelper(mContext).getWritableDatabase();
        assertEquals(true, sql.isOpen());
    }

    public void testInsertValues() {
        Gson gson = new GsonBuilder().create();
        String response = mContext.getString(R.string.json);
        Log.d(TAG, "testInsertValues: response = " + response);
        MovieEntity entity = gson.fromJson(response, MovieEntity.class);
        assertTrue("the json is null", entity != null);
        deleteDatabase();
        SQLiteDatabase db = new MovieDatabaseHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        String languageCode = Locale.getDefault().getLanguage();

        int page = entity.getPage();
        ContentValues pageValues = new ContentValues();
        pageValues.put(MovieContract.LANGUAGE_CODE, languageCode);
        pageValues.put(MovieContract.Page.PAGE_TYPE, (page << 2) + 1);
        pageValues.put(MovieContract.Page.TOTAL_RESULTS, entity.getTotal_results());
        pageValues.put(MovieContract.Page.TOTAL_PAGES, entity.getTotal_pages());
        long pageId = db.insert(MovieContract.Page.TABLE_NAME, null, pageValues);
        assertTrue("not insert page to database", pageId > -1);

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
            movieValues.put(MovieContract.LANGUAGE_CODE,            languageCode)  ;
            movieValues.put(MovieContract.Movie.PAGE_ID,            pageId);
            movieValues.put(MovieContract.Movie.POSTER_PATH,        detail.getPoster_path());
            movieValues.put(MovieContract.Movie.ADULT,              detail.isAdult());
            movieValues.put(MovieContract.Movie.OVERVIEW,           detail.getOverview());
            movieValues.put(MovieContract.Movie.RELEASE_DATE,       detail.getRelease_date());
            movieValues.put(MovieContract.Movie.GENRE_IDS,          builder.toString());
            movieValues.put(MovieContract.Movie.MOVIE_ID,           detail.getId());
            movieValues.put(MovieContract.Movie.ORIGINAL_TITLE,     detail.getOriginal_title());
            movieValues.put(MovieContract.Movie.ORIGINAL_LANGUAGE,  detail.getOriginal_language());
            movieValues.put(MovieContract.Movie.TITLE,              detail.getTitle());
            movieValues.put(MovieContract.Movie.BACKDROP_PATH,      detail.getBackdrop_path());
            movieValues.put(MovieContract.Movie.POPULARITY,         detail.getPopularity());
            movieValues.put(MovieContract.Movie.VOTE_COUNT,         detail.getVote_count());
            movieValues.put(MovieContract.Movie.VIDEO,              detail.isVideo());
            movieValues.put(MovieContract.Movie.VOTE_AVERAGE,       detail.getVote_average());
            db.insert(MovieContract.Movie.TABLE_NAME, null, movieValues);
        }
    }
}