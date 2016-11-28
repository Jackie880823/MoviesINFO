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

import java.io.Serializable;
import java.util.List;

/**
 * Created 16/11/24.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieEntity implements Serializable{

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
    public String toString() {
        return "MovieEntity{" +
                "page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", results=" + results +
                '}';
    }
}
