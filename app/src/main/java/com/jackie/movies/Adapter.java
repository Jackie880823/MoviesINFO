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
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.tools.ImageLoadUtil;
import com.jackie.movies.ui.DetailActivity;

import java.util.List;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class Adapter extends BaseRecyclerAdapter<MovieDetail> {


    public Adapter(Context context, List<MovieDetail> data) {
        super(context, data);
    }

    @Override
    public ViewHolder<MovieDetail> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    protected static class Holder extends ViewHolder<MovieDetail> {
        private static final String TAG = "Holder";

        private ImageView imgMovie;

        public Holder(View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
            imgMovie.setOnClickListener(this);
        }

        @Override
        public void bindEntity(MovieDetail detail) {
            super.bindEntity(detail);
            String url = Constants.MEDIUM_IMAGE + detail.getPoster_path();
            ImageLoadUtil.loadPosterImage(itemView.getContext(), url, imgMovie);
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
                    break;
            }
        }

        private void startDetailActivity(View view) {
            Activity context = (Activity) view.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Constants.EXTRA_MOVIE, entity);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(context, view, Constants.TRANSIT_PIC);
            ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
//            context.startActivity(intent);
        }
    }
}
