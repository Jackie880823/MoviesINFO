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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jackie.movies.base.BaseRecyclerAdapter;
import com.jackie.movies.base.ViewHolder;
import com.jackie.movies.tools.ImageLoadUtil;

import java.util.List;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class Adapter extends BaseRecyclerAdapter<ViewHolder, MovieDetail> {


    public Adapter(Context context, List<MovieDetail> data) {
        super(context, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    protected static class Holder extends ViewHolder<MovieDetail> implements View.OnClickListener{
        private static final String TAG = "Holder";

        private ImageView imgMovie;
        private MovieDetail mDetail;

        public Holder(View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
            imgMovie.setOnClickListener(this);
        }

        public void bindEntity(MovieDetail detail) {
            mDetail = detail;
            String url = Constants.MEDIUM_IMAGE + detail.getPoster_path();
            ImageLoadUtil.loadImage(itemView.getContext(), url, imgMovie);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.img_movie:
                    startDetailActivity(view);
                    break;
                default:
                    Log.d(TAG, "onClick: id [ " + id + " ] not action");
                    return;
            }
        }

        private void startDetailActivity(View view) {
            Activity context = (Activity) view.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Constants.EXTRA_MOVIE, mDetail);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(context, view, Constants.TRANSIT_PIC);
            ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
//            context.startActivity(intent);
        }
    }
}
