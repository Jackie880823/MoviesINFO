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

    public static final String LANGUAGE_PARAM = "language";

    public static final String API_KEY_PARAM = "api_key";

    public static final String SMAL_IMAGE = IMAGE_BASE_URI + "w185/";

    public static final String MEDIUM_IMAGE = IMAGE_BASE_URI + "w500/";

    public static final String ORIGINAL_IMAGE = IMAGE_BASE_URI + "original/";

}
