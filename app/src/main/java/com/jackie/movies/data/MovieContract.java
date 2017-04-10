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
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

/**
 * Created 16/12/22.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.jackie.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PAGE = "Page";
    public static final String PATH_MOVIE = "Movie";


    public static final String LANGUAGE_CODE = "language_code";

    static long getLongForUri(Uri uri){
        List<String> pathSegments = uri.getPathSegments();
        return Long.parseLong(pathSegments.get(pathSegments.size() - 1));
    }

    public static final class Page implements BaseColumns {
        public static final String TABLE_NAME = PATH_PAGE;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
                .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "." + TABLE_NAME;
        /**
         *
         */
        public static final String PAGE_TYPE = "page_type";
        public static final String TOTAL_RESULTS = "total_results";
        public static final String TOTAL_PAGES = "total_pages";

        static Uri buildPageUri(long pageType) {
            return ContentUris.withAppendedId(CONTENT_URI, pageType);
        }

        static long getPageType(Uri uri) {
            return getLongForUri(uri);
        }
    }

    public static final class Movie implements BaseColumns {
        public static final String TABLE_NAME = PATH_MOVIE;
        public static final String PATH_POPULAR = "popular";
        public static final String PATH_TOP_RATED = "top_rated";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
                .build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "." + TABLE_NAME;


        public static final String PAGE_TYPE = "page_type";
        public static final String POSTER_PATH = "poster_path";
        public static final String ADULT = "adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String GENRE_IDS = "genre_ids";
        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String TITLE = "title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String FAVOUR = "favour";

        /**
         * 确定movieId的电影
         * @param movieId
         * @return
         */
        public static Uri buildMovieUri(long movieId) {
           return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }

        /**
         * @param uri
         * @return
         */
        static long getMovieId(Uri uri) {
            return getLongForUri(uri);
        }
    }
}
