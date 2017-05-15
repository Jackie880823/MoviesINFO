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

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class Constants {
    private static final String SERVICE_API = App.getInstance().getString(R.string.api_service_url);
    private static final String IMAGE_BASE_URI = App.getInstance().getString(R.string
            .image_base_url);

    /**
     * 高分电影
     */
    public static final String MOVIE_TOP_RATED = SERVICE_API + "movie/top_rated?";

    /**
     * 热门电影
     */
    public static final String MOVIE_POPULAR = SERVICE_API + "movie/popular?";

    /**
     *
     */
    public static final String GET_DETAILS = SERVICE_API + "movie/%s?";

    public static final String GET_VIDEOS = SERVICE_API + "movie/%s/videos?";

    public static final String GET_REVIEWS = SERVICE_API + "movie/%s/reviews?";

    public static final String LANGUAGE_PARAM = "language";

    public static final String API_KEY_PARAM = "api_key";

    public static final String PAGE_PARAM = "page";

    public static final String SMALL_IMAGE = IMAGE_BASE_URI + "w342";

    public static final String MEDIUM_IMAGE = IMAGE_BASE_URI + "w500";

    public static final String ORIGINAL_IMAGE = IMAGE_BASE_URI + "original";

    public static final String EXTRA_MOVIE = "extra_movie";


    public static final String TRANSIT_PIC = "picture";

    public static final String LINK_TO_YOUTUBE = "https://www.youtube.com/watch?v=%s";
    public static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
}
