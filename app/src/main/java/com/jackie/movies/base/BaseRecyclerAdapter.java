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

package com.jackie.movies.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 16/11/25.
 *
 * @author Jackie
 * @version 1.0
 */

public abstract class BaseRecyclerAdapter<E> extends RecyclerView
        .Adapter<ViewHolder<E>> {

    protected List<E> mData;

    protected Context mContext;

    public BaseRecyclerAdapter(Context context, List<E> data) {
        mContext = context;
        mData = data;
    }

    public void setData(Collection<E> data) {
        if (data == null || data.isEmpty()) {
            // 清空数据
            mData = null;
        } else if (mData != null) {
            mData.clear();
            mData = new ArrayList<>(data);
        } else {
            mData = new ArrayList<>(data);
        }
        notifyDataSetChanged();

    }

    public void addData(Collection<E> data) {
        if (data == null || data.isEmpty()) {
            // 添加空数据直接返回
            return;
        }

        if (mData == null) {
            mData = new ArrayList<>(data);
            notifyDataSetChanged();
        } else {
            int positionStart = getItemCount();
            mData.addAll(data);
            notifyItemRangeInserted(positionStart, data.size());
        }
    }

    public void addItem(E item) {
        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void onBindViewHolder(ViewHolder<E> holder, int position) {
        holder.bindEntity(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
