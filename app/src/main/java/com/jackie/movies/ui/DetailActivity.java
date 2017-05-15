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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.jackie.movies.base.BaseRecyclerAdapter;
import com.jackie.movies.data.MovieContract;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.entities.Reviews;
import com.jackie.movies.entities.Trailer;
import com.jackie.movies.entities.Videos;
import com.jackie.movies.tools.HttpUtils;
import com.jackie.movies.tools.ImageLoadUtil;

import java.io.IOException;
import java.util.Locale;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DetailActivity";

    private static final int WHAT_IS_LOAD_TRAILER = 100;

    private static final int WHAT_IS_LOAD_REVIEWS = 200;
    public static final String EXTRA_ENTITY = "extra_entity";

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
    private FloatingActionButton fabFavour;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isFinishing() || isDestroyed()) {
                return;
            }

            switch (msg.what) {
                case WHAT_IS_LOAD_TRAILER:
                    Videos videos = msg.getData().getParcelable(EXTRA_ENTITY);
                    if (videos != null) {
                        BaseRecyclerAdapter<Trailer> adapter = new VideoAdapter(DetailActivity
                                .this, videos.getResults());
                        recVideos.setAdapter(adapter);
                    } else {
                        recVideos.setAdapter(null);
                    }
                    break;

                case WHAT_IS_LOAD_REVIEWS:
                    Reviews reviews = msg.getData().getParcelable(EXTRA_ENTITY);
                    if (reviews != null) {
                        BaseRecyclerAdapter adapter = new ReviewAdapter(DetailActivity.this,
                                reviews.getResults());
                        recReviews.setAdapter(adapter);
                    } else {
                        recReviews.setAdapter(null);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();


        String videosUrl = String.format(Constants.GET_VIDEOS, detail.getId());
        loadList(videosUrl, WHAT_IS_LOAD_TRAILER, Videos.class);

        String reviewsUrl = String.format(Constants.GET_REVIEWS, detail.getId());
        loadList(reviewsUrl, WHAT_IS_LOAD_REVIEWS, Reviews.class);
    }

    private void loadList(String url, final int what, final Class<? extends Parcelable> classOfT) {

        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter(Constants.API_KEY_PARAM, getString(R.string.api_key_v3_auth));
        builder.appendQueryParameter(Constants.LANGUAGE_PARAM, Locale.getDefault().getLanguage());
        url = builder.build().toString();
        HttpUtils.get(this, url, new HttpUtils.HttpCallBack() {
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

                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_ENTITY, gson.fromJson(response, classOfT));

                Message message = new Message();
                message.what = what;
                message.setData(bundle);
                handler.sendMessage(message);
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

        fabFavour = getViewById(R.id.fab_favour);
        fabFavour.setOnClickListener(this);
        checkFavour(detail.isFavour());
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
            case R.id.fab_favour:
                updateMark();
                break;
            default:
                break;
        }
    }

    private void updateMark() {
        ContentValues values = new ContentValues();
        boolean favour = detail.isFavour();
        values.put(MovieContract.Movie.FAVOUR, !favour);
        Uri uri = MovieContract.Movie.buildMovieUri(detail.getId());
        ContentResolver contentResolver = getContentResolver();
        int update = contentResolver.update(uri, values, null, null);

        if (update == 1) {
            detail.setFavour(!favour);
            checkFavour(!favour);
        }
    }

    private void checkFavour(boolean favour) {
        if (favour) {
            fabFavour.setImageResource(R.drawable.ic_bookmark);
        } else {
            fabFavour.setImageResource(R.drawable.ic_bookmark_border);
        }
    }


}
