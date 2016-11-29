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

package com.jackie.movies.tools;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.jackie.movies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class ImageLoadUtil {
    private static final String TAG = "ImageLoadUtil";
    public static void loadPosterImage(Context context, String url, ImageView imageView) {
        Log.d(TAG, "loadPosterImage() called with: url = [" + url + "]");
        Picasso picasso = Picasso.with(context);
        picasso.cancelRequest(imageView);

        RequestCreator creator = picasso.load(url);
        creator.placeholder(R.drawable.image_default);
        creator.into(imageView);
    }
    public static void loadBackDropImage(Context context, String url, ImageView imageView) {
        Log.d(TAG, "loadPosterImage() called with: url = [" + url + "]");
        Picasso picasso = Picasso.with(context);
        picasso.cancelRequest(imageView);

        RequestCreator creator = picasso.load(url);
        creator.placeholder(R.drawable.image_backdrop);
        creator.into(imageView);
    }
}
