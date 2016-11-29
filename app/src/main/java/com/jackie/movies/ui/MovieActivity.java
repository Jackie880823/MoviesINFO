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
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.Adapter;
import com.jackie.movies.Constants;
import com.jackie.movies.entities.MovieEntity;
import com.jackie.movies.R;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.tools.HttpUtils;

import java.io.IOException;
import java.util.Locale;

public class MovieActivity extends BaseActivity implements HttpUtils.HttpCallBack {
    private static final String TAG = "MovieActivity";
    public static final String PREF_IS_POPULAR_KEY = "pref_is_popular_key";

    private MenuItem menuForType;
    private boolean isPopular = true;
    private int currentPage = 1;
    private RecyclerView recyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = getViewById(R.id.rec_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isPopular = preferences.getBoolean(PREF_IS_POPULAR_KEY, false);
        updateMovies();
    }

    private void updateMovies() {
        Log.d(TAG, "updateMovies() called");

        String baseUrl;
        if (isPopular) {
            baseUrl = Constants.MOVIE_POPULAR;
        } else {
            baseUrl = Constants.MOVIE_TOP_RATED;
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
            menuForType.setTitle(R.string.action_popular_type);
        } else {
            menuForType.setTitle(R.string.action_top_rated_type);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuForType = menu.findItem(R.id.action_type);
        if (isPopular) {
            menuForType.setTitle(R.string.action_popular_type);
        } else {
            menuForType.setTitle(R.string.action_top_rated_type);
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
        MovieEntity entity = gson.fromJson(response, MovieEntity.class);
        if (entity != null) {
            Log.d(TAG, "onSuccess: entity {" + entity.toString() + "}");
            mAdapter = new Adapter(this, entity.getResults());
            recyclerView.setAdapter(mAdapter);
            Log.d(TAG, "run: " + mAdapter.getItemCount());
        }
    }

    @Override
    public void onFailure(IOException e) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie;
    }
}
