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
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;
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

    private static final int PAGES = 0;
    private static final int PAGE = 1;

    private static final int MOVIES = 2;
    private static final int MOVIE = 3;

    static {
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Page.TABLE_NAME, PAGES);
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Page.TABLE_NAME + "/#", PAGE);
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Movie.TABLE_NAME, MOVIES);
        mMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Movie.TABLE_NAME + "/#",
                MOVIE);
    }

    private MovieDatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        String AND = " AND ";
        if (TextUtils.isEmpty(selection)) {
            selection = MovieContract.LANGUAGE_CODE + " = ? ";
        } else {
            selection = selection + AND + MovieContract.LANGUAGE_CODE + " = ? ";
        }

        if (selectionArgs != null && selectionArgs.length > 0) {
            List<String> strings = Arrays.asList(selection);
            strings.add(Locale.getDefault().getLanguage());
            selectionArgs = (String[]) strings.toArray();
        } else {
            selectionArgs = new String[]{Locale.getDefault().getLanguage()};
        }

        Cursor result;
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String tableName;
        switch (mMatcher.match(uri)) {
            case PAGES:
                tableName = MovieContract.Page.TABLE_NAME;
                break;

            case PAGE: {
                tableName = MovieContract.Page.TABLE_NAME;
                long pageType = MovieContract.Page.getPageType(uri);
                selection = selection + AND + MovieContract.Page.PAGE_TYPE + " = " + pageType + " ";
                break;
            }

            case MOVIES:
                tableName = MovieContract.Movie.TABLE_NAME;
                break;

            case MOVIE:
                tableName = MovieContract.Movie.TABLE_NAME;
                long movieId = MovieContract.Movie.getMovieId(uri);
                selection = selection + AND + MovieContract.Movie.MOVIE_ID + " = " + movieId +
                        " ";
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        result = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type;
        switch (mMatcher.match(uri)) {
            case PAGES:
                type = MovieContract.Page.CONTENT_TYPE;
                break;
            case PAGE:
                type = MovieContract.Page.CONTENT_ITEM_TYPE;
                break;
            case MOVIES:
                type = MovieContract.Movie.CONTENT_TYPE;
                break;
            case MOVIE:
                type = MovieContract.Movie.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long _id;
        Uri insertedUri;
        switch (mMatcher.match(uri)) {
            case MOVIES:
                _id = db.insert(MovieContract.Movie.TABLE_NAME, null, values);
                if (_id > 0) {
                    insertedUri = MovieContract.Movie.buildIDUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case PAGES:
                _id = db.insert(MovieContract.Page.TABLE_NAME, null, values);
                if (_id > 0) {
                    insertedUri = MovieContract.Page.buildPageUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
