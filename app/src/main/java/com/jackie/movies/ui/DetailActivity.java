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

package com.jackie.movies.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.movies.Constants;
import com.jackie.movies.R;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.tools.ImageLoadUtil;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DetailActivity";
    private TextView tvDescription;
    private MovieDetail detail;
    private ImageView imgPoster;
    private ImageView imgBackdrop;
    private TextView tvVoteAverage;
    private TextView tvTitleName;
    private TextView tvReleaseDate;
    private TextView tvPopularity;
    private TextView tvVoteCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();
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
        ViewCompat.setTransitionName(imgPoster, Constants.TRANSIT_PIC);
    }

    private void initData() {
        detail = (MovieDetail) getIntent().getSerializableExtra(Constants.EXTRA_MOVIE);
        if (detail == null) {
            finish();
            return;
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
        ImageLoadUtil.loadImage(this, backdropUrl, imgBackdrop);
        ImageLoadUtil.loadImage(this, posterUrl, imgPoster);

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
