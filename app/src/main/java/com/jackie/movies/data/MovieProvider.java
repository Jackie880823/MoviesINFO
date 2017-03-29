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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jackie.movies.data.MovieContract.Movie;
import com.jackie.movies.data.MovieContract.Page;

import java.util.Locale;

/**
 * Created 16/12/23.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieProvider extends ContentProvider {
    private static final String TAG = "MovieProvider";

    // Creates a UriMatcher object.
    private static final UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * 匹配页的多项
     */
    private static final int PAGES = 0;
    /**
     * 匹配指定页
     */
    private static final int PAGE = 1;
    /**
     * 匹配多项电影数据
     */
    private static final int MOVIES = 2;
    /**
     * 指定电影数据
     */
    private static final int MOVIE = 3;
    /**
     * 热闹电影
     */
    private static final int POPULAR = 4;
    /**
     * 高分电影
     */
    private static final int TOP_RATED = 5;

    /**
     * 拼接{@link Page#PAGE_TYPE}二进制前两位的的值
     */
    public static final int TYPE_POPULAR = 0b0001;
    public static final int TYPE_TOP_RATED = 0b0010;

    static {
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Page.TABLE_NAME, PAGES);
        // 具体的页，后接 page_type
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Page.TABLE_NAME + "/#", PAGE);
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Movie.TABLE_NAME, MOVIES);
        // 具体的电影数据，后面接 movie_id
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Movie.TABLE_NAME + "/#",
                MOVIE);
        // 热门电影，后接page
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Movie.TABLE_NAME + "/" +
                Movie.PATH_POPULAR + "/#", POPULAR);
        // 高分电影，后接page
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, Movie.TABLE_NAME + "/" +
                Movie.PATH_TOP_RATED + "/#", TOP_RATED);
    }

    private MovieDatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    /**
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[]
            selectionArgs, String sortOrder) {
        String AND = " AND ";
        String equal = "=";
        String language = Locale.getDefault().getLanguage();
        if (TextUtils.isEmpty(selection)) {
            selection = MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        } else {
            selection = selection + AND + MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        }

        Cursor result;
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String tableName;
        switch (mMatcher.match(uri)) {
            case PAGES:
                tableName = Page.TABLE_NAME;
                break;

            case PAGE: {
                tableName = Page.TABLE_NAME;
                long pageType = Page.getPageType(uri);
                selection = selection + AND + Page.PAGE_TYPE + equal + pageType + " ";
                break;
            }

            case MOVIES:
                tableName = Movie.TABLE_NAME;
                break;

            case MOVIE:
                tableName = Movie.TABLE_NAME;
                long movieId = Movie.getMovieId(uri);
                selection = selection + AND + Movie.MOVIE_ID + equal + movieId + " ";
                break;

            case POPULAR: {
                tableName = null;
                int page = (int) MovieContract.getLongForUri(uri);
                long pageType = (page << 2) + TYPE_POPULAR;

                Log.d(TAG, "query: pageType is " + pageType);
                selection = getInnerSql(AND, equal, language, pageType, " order by " + Movie
                        .POPULARITY);
                break;
            }

            case TOP_RATED: {
                tableName = null;
                int page = (int) MovieContract.getLongForUri(uri);
                long pageType = (page << 2) + TYPE_TOP_RATED;
                Log.d(TAG, "query: pageType is " + pageType);
                selection = getInnerSql(AND, equal, language, pageType, " order by " + Movie
                        .VOTE_AVERAGE);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (TextUtils.isEmpty(tableName)) {
            result = db.rawQuery(selection, null);
        } else {
            result = db.query(tableName, projection, selection, selectionArgs, null, null,
                    sortOrder);

        }
        StringBuilder stringBuilder = new StringBuilder("result's column is [ ");
        for (String s : result.getColumnNames()) {
            stringBuilder.append(s).append(",");
        }
        stringBuilder.append("]");
        Log.d(TAG, "query: result " + stringBuilder.toString());
        return result;
    }

    @NonNull
    private String getInnerSql(String AND, String equal, String language, long pageType, String
            orderBy) {
        String selection;
        selection = "SELECT * FROM " + Movie.TABLE_NAME + " INNER JOIN " + Page
                .TABLE_NAME + " ON " + Movie.TABLE_NAME + "." + Movie.PAGE_TYPE + equal +
                Page.TABLE_NAME + "." + Page.PAGE_TYPE + AND + Movie.TABLE_NAME + "." +
                MovieContract.LANGUAGE_CODE + equal + Page.TABLE_NAME + "." +
                MovieContract.LANGUAGE_CODE + " WHERE " + Movie.TABLE_NAME + "." + Movie
                .PAGE_TYPE + equal + pageType + AND + Movie.TABLE_NAME + "." +
                MovieContract.LANGUAGE_CODE + " = '" + language + "'" + orderBy;
        return selection;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type;
        switch (mMatcher.match(uri)) {
            case PAGES:
                type = Page.CONTENT_TYPE;
                break;
            case PAGE:
                type = Page.CONTENT_ITEM_TYPE;
                break;
            case MOVIES:
            case POPULAR:
            case TOP_RATED:
                type = Movie.CONTENT_TYPE;
                break;
            case MOVIE:
                type = Movie.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (!values.containsKey(MovieContract.LANGUAGE_CODE)) {
            values.put(MovieContract.LANGUAGE_CODE, Locale.getDefault().getLanguage());
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Uri insertedUri;
        String tableName;
        switch (mMatcher.match(uri)) {
            case MOVIES:
                Long movieId = values.getAsLong(Movie.MOVIE_ID);
                insertedUri = Movie.buildMovieUri(movieId);
                tableName = Movie.TABLE_NAME;
                break;
            case PAGES:
                Long pageType = values.getAsLong(Page.PAGE_TYPE);
                insertedUri = Page.buildPageUri(pageType);
                tableName = Page.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursor = query(insertedUri, null, null, null, null);
        if (cursor != null) {
            cursor.close();
            update(uri, values, null, null);
        } else {
            db.insert(tableName, null, values);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String AND = " AND ";
        String language = Locale.getDefault().getLanguage();
        if (TextUtils.isEmpty(selection)) {
            selection = MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        } else {
            selection = selection + AND + MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String tableName;
        switch (mMatcher.match(uri)) {
            case PAGES:
                tableName = Page.TABLE_NAME;
                break;
            case MOVIES:
                tableName = Movie.TABLE_NAME;
                break;

            case PAGE:
                tableName = Page.TABLE_NAME;
                long pageType = Page.getPageType(uri);
                selection = selection + AND + Page.PAGE_TYPE + "=" + pageType;
                break;

            case MOVIE:
                tableName = Movie.TABLE_NAME;
                long movieId = Movie.getMovieId(uri);
                selection = selection + AND + Movie.MOVIE_ID + "=" + movieId;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        return db.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {
        String AND = " AND ";
        String language = Locale.getDefault().getLanguage();
        if (TextUtils.isEmpty(selection)) {
            selection = MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        } else {
            selection = selection + AND + MovieContract.LANGUAGE_CODE + " = '" + language + "'";
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String tableName;
        switch (mMatcher.match(uri)) {
            case PAGES:
                tableName = Page.TABLE_NAME;
                break;
            case MOVIES:
                tableName = Movie.TABLE_NAME;
                break;

            case PAGE:
                tableName = Page.TABLE_NAME;
                long pageType = Page.getPageType(uri);
                selection = selection + AND + Page.PAGE_TYPE + "=" + pageType;
                break;

            case MOVIE:
                tableName = Movie.TABLE_NAME;
                long movieId = Movie.getMovieId(uri);
                selection = selection + AND + Movie.MOVIE_ID + "=" + movieId;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        return db.update(tableName, values, selection, selectionArgs);
    }
}
