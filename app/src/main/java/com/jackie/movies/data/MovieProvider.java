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

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
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
                tableName = Movie.TABLE_NAME;
                int page = (int) MovieContract.getLongForUri(uri);
                long pageType = (page << 2) + TYPE_POPULAR;
                Log.d(TAG, "query: pageType is " + pageType);
                String selectPage = Page.PAGE_TYPE + equal + pageType;
                long pageId = getPageId(selectPage, null);
                Log.d(TAG, "query: pageId is " + pageId);
                selection = selection + AND + Movie.PAGE_ID + equal + pageId + " ";
                break;
            }

            case TOP_RATED: {
                tableName = Movie.TABLE_NAME;
                int page = (int) MovieContract.getLongForUri(uri);
                long pageType = (page << 2) + TYPE_TOP_RATED;
                Log.d(TAG, "query: pageType is " + pageType);
                String selectPage = Page.PAGE_TYPE + equal + pageType;
                long pageId = getPageId(selectPage, null);
                Log.d(TAG, "query: pageId is " + pageId);
                selection = selection + AND + Movie.PAGE_ID + equal + pageId + " ";
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        result = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return result;
    }

    private long getPageId(String select, String[] selectArg) {
        Cursor cursor = query(Page.CONTENT_URI, new
                String[]{Page._ID}, select,
                selectArg, null);
        long pageId = -1;
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            pageId = cursor.getLong(cursor.getColumnIndex(Page._ID));
            cursor.close();
        }
        return pageId;
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
        switch (mMatcher.match(uri)) {
            case MOVIES:
                db.insert(Movie.TABLE_NAME, null, values);
                Long movieId = values.getAsLong(Movie.MOVIE_ID);
                insertedUri = Movie.buildMovieUri(movieId);
                break;
            case PAGES:
                db.insert(Page.TABLE_NAME, null, values);
                Long pageType = values.getAsLong(Page.PAGE_TYPE);
                insertedUri = Page.buildPageUri(pageType);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
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
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
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
