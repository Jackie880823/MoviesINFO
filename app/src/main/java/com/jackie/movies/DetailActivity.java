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

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.tools.ImageLoadUtil;

public class DetailActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "DetailActivity";
    private TextView tvDescription;
    private MovieDetail detail;
    private ImageView imgPoster;

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
        imgPoster = getViewById(R.id.img_poster);
        ViewCompat.setTransitionName(imgPoster, Constants.TRANSIT_PIC);

        String url = Constants.MEDIUM_IMAGE + detail.getPoster_path();
        ImageLoadUtil.loadImage(this, url, imgPoster);

        FloatingActionButton fab = getViewById(R.id.fab);
        fab.setOnClickListener(this);
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
        tvDescription.setText(detail.getOverview());

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
