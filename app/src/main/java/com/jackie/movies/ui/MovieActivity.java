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

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.Adapter;
import com.jackie.movies.Constants;
import com.jackie.movies.R;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.entities.MovieEntity;
import com.jackie.movies.tools.HttpUtils;

import java.io.IOException;
import java.util.Locale;

public class MovieActivity extends BaseActivity implements HttpUtils.HttpCallBack,
        GestureDetector.OnGestureListener {
    private static final String TAG = "MovieActivity";
    public static final String PREF_IS_POPULAR_KEY = "pref_is_popular_key";

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private MenuItem menuForType;
    private boolean isPopular = true;
    private int currentPage = 1;
    private RecyclerView recyclerView;
    private TextView tvPage;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private MovieEntity entity;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detector = new GestureDetector(this, this);

        tvPage = getViewById(R.id.tv_page_description);

        recyclerView = getViewById(R.id.rec_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float curX = 0.0f;
            private float curY = 0.0f;
            private float lastX = 0.0f;
            private float lastY = 0.0f;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isPopular = preferences.getBoolean(PREF_IS_POPULAR_KEY, false);
        updateMovies();
    }

    private void updateMovies() {
        Log.d(TAG, "updateMovies() called");
        tvPage.setVisibility(View.GONE);

        String baseUrl;
        if (isPopular) {
            baseUrl = Constants.MOVIE_POPULAR;
            setTitle(getString(R.string.title_popular_movies));
        } else {
            baseUrl = Constants.MOVIE_TOP_RATED;
            setTitle(getString(R.string.title_top_rated_movies));
        }

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();

        builder.appendQueryParameter(Constants.LANGUAGE_PARAM, Locale.getDefault().getLanguage());
        builder.appendQueryParameter(Constants.PAGE_PARAM, String.valueOf(currentPage));
        builder.appendQueryParameter(Constants.API_KEY_PARAM, getString(R.string.api_key_v3_auth));
        HttpUtils.get(this, builder.build().toString(), this);

        if (menuForType == null) {
            return;
        }

        if (isPopular) {
            menuForType.setTitle(R.string.action_top_rated_type);
        } else {
            menuForType.setTitle(R.string.action_popular_type);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuForType = menu.findItem(R.id.action_type);
        if (isPopular) {
            menuForType.setTitle(R.string.action_top_rated_type);
        } else {
            menuForType.setTitle(R.string.action_popular_type);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                updateMovies();
                break;

            case R.id.action_type:
                isPopular = !isPopular;
                currentPage = 1;
                preferences.edit().putBoolean(PREF_IS_POPULAR_KEY, isPopular).apply();
                updateMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnect() {
        if (mAdapter != null) {
            mAdapter.setData(null);
        }
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void onSuccess(String response) {
        Log.d(TAG, "onSuccess() called with: string = [" + response + "]");
        Gson gson = new GsonBuilder().create();
        entity = gson.fromJson(response, MovieEntity.class);
        if (entity != null) {
            Log.d(TAG, "onSuccess: entity {" + entity.toString() + "}");
            mAdapter = new Adapter(this, entity.getResults());
            recyclerView.setAdapter(mAdapter);
            Log.d(TAG, "run: " + mAdapter.getItemCount());
            currentPage = entity.getPage();
            String description = String.format(getString(R.string.txt_page_description),
                    currentPage, entity.getTotal_pages());
            tvPage.setText(description);
        }
    }

    @Override
    public void onFailure(IOException e) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        tvPage.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        tvPage.setVisibility(View.GONE);

        boolean result = false;
        if (motionEvent == null || motionEvent1 == null) {
            return false;
        }

        float diffY = motionEvent1.getY() - motionEvent.getY();
        float diffX = motionEvent1.getX() - motionEvent.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(v) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    Log.d(TAG, "onFling: onSwipeRight()");
                    if (entity != null && currentPage > 1) {
                        currentPage -= 1;
                        updateMovies();
                        result = true;
                    }
                } else {
                    Log.d(TAG, "onFling: onSwipeLeft()");
                    if (entity != null && currentPage < entity.getTotal_pages()) {
                        currentPage += 1;
                        updateMovies();
                        result = true;
                    }
                }
            }
        } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(v1) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY > 0) {
                Log.d(TAG, "onFling: onSwipeBottom()");
            } else {
                Log.d(TAG, "onFling: onSwipeTop()");
            }
            result = true;
        }
        return result;
    }
}
