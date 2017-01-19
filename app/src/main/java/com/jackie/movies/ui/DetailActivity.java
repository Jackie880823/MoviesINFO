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

package com.jackie.movies.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.Constants;
import com.jackie.movies.R;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.entities.Videos;
import com.jackie.movies.tools.HttpUtils;
import com.jackie.movies.tools.ImageLoadUtil;

import java.io.IOException;
import java.util.Locale;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DetailActivity";
    /**
     * 电影详情
     */
    private MovieDetail detail;
    /**
     * 简介
     */
    private TextView tvDescription;
    /**
     * 海报
     */
    private ImageView imgPoster;
    /**
     * 背影图片
     */
    private ImageView imgBackdrop;
    /**
     * 平均分
     */
    private TextView tvVoteAverage;
    /**
     * 标题名称
     */
    private TextView tvTitleName;
    /**
     * 上映日期
     */
    private TextView tvReleaseDate;
    /**
     * 热闹指数
     */
    private TextView tvPopularity;
    /**
     * 投票数量
     */
    private TextView tvVoteCount;

    private RecyclerView recVideos;

    private RecyclerView recReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();

        String videosUri = String.format(Constants.GET_VIDEOS, detail.getId());

        Uri.Builder builder = Uri.parse(videosUri).buildUpon();
        builder.appendQueryParameter(Constants.API_KEY_PARAM, getString(R.string.api_key_v3_auth));
        builder.appendQueryParameter(Constants.LANGUAGE_PARAM, Locale.getDefault().getLanguage());
        videosUri = builder.build().toString();
        HttpUtils.get(this, videosUri, new HttpUtils.HttpCallBack() {
            @Override
            public void onConnect() {
                Log.d(TAG, "onConnect: ");
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "onCanceled: ");
            }

            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
                Gson gson = new GsonBuilder().create();
                Videos videos = gson.fromJson(response, Videos.class);
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }

    private void initView() {
        tvDescription = getViewById(R.id.tv_description);
        tvVoteAverage = getViewById(R.id.tv_vote_average);
        imgBackdrop = getViewById(R.id.img_backdrop);
        imgPoster = getViewById(R.id.img_poster);
        tvTitleName = getViewById(R.id.tv_title_name);
        tvReleaseDate = getViewById(R.id.tv_release_date);
        tvPopularity = getViewById(R.id.tv_popularity);
        tvVoteCount = getViewById(R.id.tv_vote_count);
        recVideos = getViewById(R.id.rec_videos);
        recReviews = getViewById(R.id.rec_reviews);
        ViewCompat.setTransitionName(imgPoster, Constants.TRANSIT_PIC);
    }

    private void initData() {
        detail = getIntent().getParcelableExtra(Constants.EXTRA_MOVIE);
        if (detail == null) {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called with: detail [ " + detail + " ]");
        tvDescription.setText(detail.getOverview());
        tvVoteAverage.setText(String.valueOf(detail.getVote_average()));

        tvTitleName.setText(detail.getTitle());
        tvReleaseDate.setText(detail.getRelease_date());
        tvPopularity.setText(String.valueOf(detail.getPopularity()));
        tvVoteCount.setText(String.valueOf(detail.getVote_count()));

        String backdropUrl = Constants.MEDIUM_IMAGE + detail.getBackdrop_path();
        String posterUrl = Constants.MEDIUM_IMAGE + detail.getPoster_path();
        ImageLoadUtil.loadBackDropImage(this, backdropUrl, imgBackdrop);
        ImageLoadUtil.loadPosterImage(this, posterUrl, imgPoster);

        FloatingActionButton fab = getViewById(R.id.fab);
        fab.setOnClickListener(this);
        if (actionBar == null) {
            return;
        }

        actionBar.setTitle(detail.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                break;
            default:
                break;
        }
    }
}
