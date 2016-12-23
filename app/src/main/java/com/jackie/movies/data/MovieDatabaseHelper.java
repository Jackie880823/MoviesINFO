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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created 16/12/20.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "movie.db";

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PAGE = "CREATE TABLE "
                + MovieContract.Page.TABLE_NAME + "("
                + MovieContract.Page._ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.LANGUAGE_CODE       + " TEXT NOT NULL, "
                + MovieContract.Page.PAGE_TYPE      + " INTEGER NOT NULL, "
                + MovieContract.Page.TOTAL_RESULTS  + " INTEGER NOT NULL, "
                + MovieContract.Page.TOTAL_PAGES    + " INTEGER NOT NULL, "
                + "UNIQUE ("
                + MovieContract.LANGUAGE_CODE       + ", "
                + MovieContract.Page.PAGE_TYPE
                + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_MOVIE = "CREATE TABLE "
                + MovieContract.Movie.TABLE_NAME        + "("
                + MovieContract.Movie._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.LANGUAGE_CODE           + " TEXT NOT NULL, "
                + MovieContract.Movie.PAGE_ID           + " INTEGER NOT NULL, "
                + MovieContract.Movie.MOVIE_ID          + " INTEGER NOT NULL, "
                + MovieContract.Movie.POSTER_PATH       + " TEXT NOT NULL, "
                + MovieContract.Movie.ADULT             + " NUMERIC NOT NULL, "
                + MovieContract.Movie.OVERVIEW          + " TEXT, "
                + MovieContract.Movie.RELEASE_DATE      + " TEXT NOT NULL, "
                + MovieContract.Movie.GENRE_IDS         + " TEXT, "
                + MovieContract.Movie.ORIGINAL_TITLE    + " TEXT NOT NULL, "
                + MovieContract.Movie.ORIGINAL_LANGUAGE + " TEXT NOT NULL, "
                + MovieContract.Movie.TITLE             + " TEXT NOT NULL, "
                + MovieContract.Movie.BACKDROP_PATH     + " TEXT NOT NULL, "
                + MovieContract.Movie.POPULARITY        + " REAL, "
                + MovieContract.Movie.VOTE_COUNT        + " INTEGER, "
                + MovieContract.Movie.VIDEO             + " NUMERIC NOT NULL, "
                + MovieContract.Movie.VOTE_AVERAGE      + " REAL, "
                + " FOREIGN KEY (" + MovieContract.Movie.PAGE_ID + ") REFERENCES "
                + MovieContract.Page.TABLE_NAME + " (" + MovieContract.Page._ID + "), "
                + "UNIQUE ("
                + MovieContract.LANGUAGE_CODE + ", "
                + MovieContract.Movie.MOVIE_ID
                + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_PAGE);
        db.execSQL(SQL_CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.Movie.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.Page.TABLE_NAME);
        onCreate(db);
    }
}
