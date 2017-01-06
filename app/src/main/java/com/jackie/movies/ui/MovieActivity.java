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

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jackie.movies.Adapter;
import com.jackie.movies.R;
import com.jackie.movies.UpdateMoviesTask;
import com.jackie.movies.base.BaseActivity;
import com.jackie.movies.data.MovieContract.Movie;
import com.jackie.movies.data.MovieContract.Page;
import com.jackie.movies.entities.MovieDetail;
import com.jackie.movies.entities.MovieEntity;

import java.util.LinkedList;
import java.util.List;

public class MovieActivity extends BaseActivity implements GestureDetector.OnGestureListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MovieActivity";
    public static final String PREF_IS_POPULAR_KEY = "pref_is_popular_key";

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private static final int MOVIE_LOADER_ID = 0x3e8;
    private static final int MOVIE_FAVORITE_ID = 0x3e9;

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
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = getSupportLoaderManager();

        detector = new GestureDetector(this, this);

        tvPage = getViewById(R.id.tv_page_description);

        recyclerView = getViewById(R.id.rec_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isPopular = preferences.getBoolean(PREF_IS_POPULAR_KEY, false);
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        loaderManager.initLoader(MOVIE_FAVORITE_ID, null, this);
        updateMovies();
    }

    private void updateMovies() {
        Log.d(TAG, "updateMovies() called");

        getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        tvPage.setVisibility(View.GONE);

        String[] param = new String[]{String.valueOf(isPopular), String.valueOf(currentPage)};
        UpdateMoviesTask task = new UpdateMoviesTask(this);
        task.execute(param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuForType = menu.findItem(R.id.action_type);
        setMenuTitle(isPopular);
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

                setMenuTitle(isPopular);

                currentPage = 1;
                preferences.edit().putBoolean(PREF_IS_POPULAR_KEY, isPopular).apply();
                loaderManager.destroyLoader(MOVIE_LOADER_ID);
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
                break;

            case R.id.action_favorite:
                loaderManager.destroyLoader(MOVIE_FAVORITE_ID);
                loaderManager.restartLoader(MOVIE_FAVORITE_ID, null, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenuTitle(boolean isPopular) {
        if (isPopular) {
            menuForType.setTitle(R.string.action_top_rated_type);
            setTitle(R.string.title_popular_movies);
        } else {
            setTitle(R.string.title_top_rated_movies);
            menuForType.setTitle(R.string.action_popular_type);
        }
    }

    public void onSuccess(MovieEntity entity) {
        Log.d(TAG, "onSuccess() called with: string = [" + entity + "]");
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

    /**
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() called with: id = [" + id + "], args = [" + args + "]");

        Uri uri;
        String select;
        switch (id) {
            case MOVIE_FAVORITE_ID:
                uri = Movie.CONTENT_URI;
                select = Movie.FAVOUR + " = 1";
                break;
            case MOVIE_LOADER_ID:
                String path = isPopular ? Movie.PATH_POPULAR : Movie.PATH_TOP_RATED;
                uri = Movie.CONTENT_URI.buildUpon().appendPath(path).appendPath(String.valueOf
                        (currentPage)).build();
                select = null;
                break;

            default:
                return null;
        }
        return new CursorLoader(this, uri, null, select, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 0) {
            return;
        }

        entity = new MovieEntity();
        List<MovieDetail> details = new LinkedList<>();
        final int columnTotalResults = data.getColumnIndex(Page.TOTAL_RESULTS);
        final int columnPosterPath = data.getColumnIndex(Movie.POSTER_PATH);
        final int columnAdult = data.getColumnIndex(Movie.ADULT);
        final int columnOverview = data.getColumnIndex(Movie.OVERVIEW);
        final int columnReleaseDate = data.getColumnIndex(Movie.RELEASE_DATE);
        final int columnId = data.getColumnIndex(Movie.MOVIE_ID);
        final int columnOriginalTitle = data.getColumnIndex(Movie.ORIGINAL_TITLE);
        final int columnOriginalLanguage = data.getColumnIndex(Movie.ORIGINAL_LANGUAGE);
        final int columnTitle = data.getColumnIndex(Movie.TITLE);
        final int columnBackdropPath = data.getColumnIndex(Movie.BACKDROP_PATH);
        final int columnPopularity = data.getColumnIndex(Movie.POPULARITY);
        final int columnVoteCount = data.getColumnIndex(Movie.VOTE_COUNT);
        final int columnVideo = data.getColumnIndex(Movie.VIDEO);
        final int columnVoteAverage = data.getColumnIndex(Movie.VOTE_AVERAGE);
        final int columnGenreIds = data.getColumnIndex(Movie.GENRE_IDS);


        boolean hasPage = columnTotalResults > -1;
        Log.d(TAG, "onLoadFinished: hasPage is " + hasPage);
        while (data.moveToNext()) {
            if (hasPage && entity.getTotal_pages() == -1) {
                int columnPageType = data.getColumnIndex(Page.PAGE_TYPE);
                int columnTotalPages = data.getColumnIndex(Page.TOTAL_PAGES);

                entity.setPage(data.getInt(columnPageType) >> 2);
                entity.setTotal_pages(data.getInt(columnTotalPages));
                entity.setTotal_results(data.getInt(columnTotalResults));
            }
            MovieDetail detail = new MovieDetail();
            //        String poster_path;
            detail.setPoster_path(data.getString(columnPosterPath));
            //        boolean adult;
            detail.setAdult(data.getInt(columnAdult) == 1);
            //        String overview;
            detail.setOverview(data.getString(columnOverview));
            //        String release_date;
            detail.setRelease_date(data.getString(columnReleaseDate));
            //        long id;
            detail.setId(data.getLong(columnId));
            //        String original_title;
            detail.setOriginal_title(data.getString(columnOriginalTitle));
            //        String original_language;
            detail.setOriginal_language(data.getString(columnOriginalLanguage));
            //        String title;
            detail.setTitle(data.getString(columnTitle));
            //        String backdrop_path;
            detail.setBackdrop_path(data.getString(columnBackdropPath));
            //        double popularity;
            detail.setPopularity(data.getDouble(columnPopularity));
            //        int vote_count;
            detail.setVote_count(data.getInt(columnVoteCount));
            //        boolean video;
            detail.setVideo(data.getInt(columnVideo) == 1);
            //        double vote_average;
            detail.setVote_average(data.getDouble(columnVoteAverage));
            //        List<Integer> genre_ids;
            String[] split = data.getString(columnGenreIds).split(",");
            List<Integer> ids = new LinkedList<>();
            for (String s : split) {
                if (TextUtils.isEmpty(s)) {
                    continue;
                }
                ids.add(Integer.valueOf(s));
            }
            detail.setGenre_ids(ids);
            details.add(detail);
        }

        if (details.isEmpty()) {
            return;
        }

        entity.setResults(details);
        onSuccess(entity);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        if (mAdapter != null) {
//            mAdapter.setData(null);
//        }
    }
}
