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

public abstract class BaseRecyclerAdapter<VH extends ViewHolder, E> extends RecyclerView
        .Adapter<VH> {

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
    public void onBindViewHolder(VH holder, int position) {
        holder.bindEntity(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
