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

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.tools.HttpUtils;

import java.io.IOException;
import java.util.Locale;

public class MovieActivity extends BaseActivity implements HttpUtils.HttpCallBack {
    private static final String TAG = "MovieActivity";

    private boolean isPopular = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = getViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        RecyclerView rec = getViewById(R.id.rec_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rec.setLayoutManager(layoutManager);

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
        builder.appendQueryParameter(Constants.API_KEY_PARAM, getString(R.string.api_key_v3_auth));
        HttpUtils.get(builder.build().toString(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnect() {

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
        }
    }

    @Override
    public void onFailure(IOException e) {

    }
}
