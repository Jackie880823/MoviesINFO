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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jackie.movies.Constants;
import com.jackie.movies.R;
import com.jackie.movies.base.BaseRecyclerAdapter;
import com.jackie.movies.base.ViewHolder;
import com.jackie.movies.entities.Trailer;

import java.util.List;
import java.util.Locale;

/**
 * Created 17/5/15.
 *
 * @author Jackie
 * @version 1.0
 */

public class VideoAdapter extends BaseRecyclerAdapter<Trailer> {
    public VideoAdapter(Context context, List<Trailer> data) {
        super(context, data);
    }

    @Override
    public ViewHolder<Trailer> onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(mContext);
        return new TrailerHolder(imageView);
    }

    private static class TrailerHolder extends ViewHolder<Trailer> implements View.OnClickListener{
        private ImageView imageView;
        public TrailerHolder(ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
            this.imageView.setImageResource(R.drawable.image_default);
            this.imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id = entity.getId();
            String youtubeLink = String.format(Locale.US, Constants.LINK_TO_YOUTUBE, id);
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));

            PackageManager packageManager = imageView.getContext().getPackageManager();
            for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(youtubeIntent, 0)) {
                if (resolveInfo.activityInfo.packageName.equals(Constants.YOUTUBE_PACKAGE_NAME)) {
                    youtubeIntent.setPackage(Constants.YOUTUBE_PACKAGE_NAME);
                    break;
                }
            }
            itemView.getContext().startActivity(youtubeIntent);
        }
    }
}

