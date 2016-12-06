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

package com.jackie.movies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieEntity implements Parcelable {

    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieDetail> results;

    public int getPage() { return page;}

    public void setPage(int page) { this.page = page;}

    public int getTotal_results() { return total_results;}

    public void setTotal_results(int total_results) { this.total_results = total_results;}

    public int getTotal_pages() { return total_pages;}

    public void setTotal_pages(int total_pages) { this.total_pages = total_pages;}

    public List<MovieDetail> getResults() { return results;}

    public void setResults(List<MovieDetail> results) { this.results = results;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieEntity)) return false;

        MovieEntity that = (MovieEntity) o;

        if (getPage() != that.getPage()) return false;
        if (getTotal_results() != that.getTotal_results()) return false;
        return getTotal_pages() == that.getTotal_pages();

    }

    @Override
    public int hashCode() {
        int result = getPage();
        result = 31 * result + getTotal_results();
        result = 31 * result + getTotal_pages();
        return result;
    }

    @Override
    public String toString() {
        return "MovieEntity{" +
                "page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", results=" + results +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.total_results);
        dest.writeInt(this.total_pages);
        dest.writeTypedList(this.results);
    }

    public MovieEntity() {}

    protected MovieEntity(Parcel in) {
        this.page = in.readInt();
        this.total_results = in.readInt();
        this.total_pages = in.readInt();
        this.results = in.createTypedArrayList(MovieDetail.CREATOR);
    }

    public static final Parcelable.Creator<MovieEntity> CREATOR = new Parcelable
            .Creator<MovieEntity>() {
        @Override
        public MovieEntity createFromParcel(Parcel source) {return new MovieEntity(source);}

        @Override
        public MovieEntity[] newArray(int size) {return new MovieEntity[size];}
    };
}
