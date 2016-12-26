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

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created 16/12/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieDatabaseProviderTest extends AndroidTestCase {

    public void testGetType() {
        ContentResolver contentResolver = mContext.getContentResolver();

        Uri uri = MovieContract.Page.CONTENT_URI;
        String type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Page.CONTENT_TYPE);

        uri = MovieContract.Page.buildPageUri((1 << 2) + 1);
        type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Page.CONTENT_ITEM_TYPE);

                uri = MovieContract.Movie.CONTENT_URI;
        type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Movie.CONTENT_TYPE);

        uri = MovieContract.Movie.CONTENT_URI.buildUpon().appendPath(MovieContract.Movie
                .PATH_POPULAR).appendPath(String.valueOf(1)).build();
        type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Movie.CONTENT_TYPE);

        uri = MovieContract.Movie.CONTENT_URI.buildUpon().appendPath(MovieContract.Movie
                .PATH_TOP_RATED).appendPath(String.valueOf(1)).build();
        type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Movie.CONTENT_TYPE);

                uri = MovieContract.Movie.buildMovieUri(127380);
        type = contentResolver.getType(uri);
        assertEquals("get the type is: " + type, type, MovieContract.Movie.CONTENT_ITEM_TYPE);


    }

    public void testQuery() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MovieContract.Movie.buildMovieUri(127380);
        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);
        assertEquals("query failure", cursor.getCount(), 1);

        uri = MovieContract.Page.buildPageUri(0b101);
        cursor = contentResolver.query(uri, null, null, null, null, null);
        assertEquals("query failure", cursor.getCount(), 1);

        uri = MovieContract.Movie.CONTENT_URI.buildUpon().appendPath(MovieContract.Movie
                .PATH_POPULAR).appendPath(String.valueOf(1)).build();
        cursor = contentResolver.query(uri, null, null, null, null, null);
        assertEquals("query failure", cursor.getCount(), 20);

        uri = MovieContract.Movie.CONTENT_URI.buildUpon().appendPath(MovieContract.Movie
                .PATH_TOP_RATED).appendPath(String.valueOf(1)).build();
        cursor = contentResolver.query(uri, null, null, null, null, null);
        assertEquals("query failure", cursor.getCount(), 0);

    }

    public void testDelete() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MovieContract.Movie.buildMovieUri(127380);
        int delete = contentResolver.delete(uri, null, null);
        assertEquals("delete failure", delete, 1);

        uri = MovieContract.Movie.CONTENT_URI;
        delete = contentResolver.delete(uri, null, null);
        assertEquals("delete failure", delete, 19);

//        uri = MovieContract.Page.buildPageUri(5);
//        delete = contentResolver.delete(uri, null, null);
//        assertEquals("delete failure", delete, 1);

        uri = MovieContract.Page.CONTENT_URI;
        delete = contentResolver.delete(uri, null, null);
        assertEquals("delete failure", delete, 1);
    }
}
