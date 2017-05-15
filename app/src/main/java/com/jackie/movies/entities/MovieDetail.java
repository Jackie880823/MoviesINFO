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

package com.jackie.movies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created 17/5/15.
 *
 * @author Jackie
 * @version 1.0
 */

public class MovieDetail implements Parcelable {
    /**
     * adult : false
     * backdrop_path : /fp6X6yhgcxzxCpmM0EVC6V9B8XB.jpg
     * belongs_to_collection : null
     * budget : 30000000
     * genres : [{"id":35,"name":"Comedy"},{"id":18,"name":"Drama"},{"id":10402,"name":"Music"},
     * {"id":10749,"name":"Romance"}]
     * homepage : http://www.lalaland.movie/
     * id : 313369
     * imdb_id : tt3783958
     * original_language : en
     * original_title : La La Land
     * overview : Mia, an aspiring actress, serves lattes to movie stars in between auditions and
     * Sebastian, a jazz musician, scrapes by playing cocktail party gigs in dingy bars, but as
     * success mounts they are faced with decisions that begin to fray the fragile fabric of
     * their love affair, and the dreams they worked so hard to maintain in each other threaten
     * to rip them apart.
     * popularity : 18.512791
     * poster_path : /ylXCdC106IKiarftHkcacasaAcb.jpg
     * production_companies : [{"name":"Marc Platt Productions","id":2527},{"name":"Gilbert
     * Films","id":10161},{"name":"Black Label Media","id":33681},{"name":"Impostor Pictures",
     * "id":53247}]
     * production_countries : [{"iso_3166_1":"US","name":"United States of America"}]
     * release_date : 2016-11-29
     * revenue : 438956154
     * runtime : 128
     * spoken_languages : [{"iso_639_1":"en","name":"English"}]
     * status : Released
     * tagline : Here's to the fools who dream.
     * title : La La Land
     * video : false
     * vote_average : 7.8
     * vote_count : 3007
     */

    private boolean adult;
    private String backdrop_path;
    private Object belongs_to_collection;
    private int budget;
    private String homepage;
    private int id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private String release_date;
    private int revenue;
    private int runtime;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;
    private List<Genres> genres;
    private List<ProductionCompanies> production_companies;
    private List<ProductionCountries> production_countries;
    private List<SpokenLanguages> spoken_languages;

    public boolean isAdult() { return adult;}

    public void setAdult(boolean adult) { this.adult = adult;}

    public String getBackdrop_path() { return backdrop_path;}

    public void setBackdrop_path(String backdrop_path) { this.backdrop_path = backdrop_path;}

    public Object getBelongs_to_collection() { return belongs_to_collection;}

    public void setBelongs_to_collection(Object belongs_to_collection) { this.belongs_to_collection = belongs_to_collection;}

    public int getBudget() { return budget;}

    public void setBudget(int budget) { this.budget = budget;}

    public String getHomepage() { return homepage;}

    public void setHomepage(String homepage) { this.homepage = homepage;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getImdb_id() { return imdb_id;}

    public void setImdb_id(String imdb_id) { this.imdb_id = imdb_id;}

    public String getOriginal_language() { return original_language;}

    public void setOriginal_language(String original_language) { this.original_language =
            original_language;}

    public String getOriginal_title() { return original_title;}

    public void setOriginal_title(String original_title) { this.original_title = original_title;}

    public String getOverview() { return overview;}

    public void setOverview(String overview) { this.overview = overview;}

    public double getPopularity() { return popularity;}

    public void setPopularity(double popularity) { this.popularity = popularity;}

    public String getPoster_path() { return poster_path;}

    public void setPoster_path(String poster_path) { this.poster_path = poster_path;}

    public String getRelease_date() { return release_date;}

    public void setRelease_date(String release_date) { this.release_date = release_date;}

    public int getRevenue() { return revenue;}

    public void setRevenue(int revenue) { this.revenue = revenue;}

    public int getRuntime() { return runtime;}

    public void setRuntime(int runtime) { this.runtime = runtime;}

    public String getStatus() { return status;}

    public void setStatus(String status) { this.status = status;}

    public String getTagline() { return tagline;}

    public void setTagline(String tagline) { this.tagline = tagline;}

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title;}

    public boolean isVideo() { return video;}

    public void setVideo(boolean video) { this.video = video;}

    public double getVote_average() { return vote_average;}

    public void setVote_average(double vote_average) { this.vote_average = vote_average;}

    public int getVote_count() { return vote_count;}

    public void setVote_count(int vote_count) { this.vote_count = vote_count;}

    public List<Genres> getGenres() { return genres;}

    public void setGenres(List<Genres> genres) { this.genres = genres;}

    public List<ProductionCompanies> getProduction_companies() { return production_companies;}

    public void setProduction_companies(List<ProductionCompanies> production_companies) { this
            .production_companies = production_companies;}

    public List<ProductionCountries> getProduction_countries() { return production_countries;}

    public void setProduction_countries(List<ProductionCountries> production_countries) { this
            .production_countries = production_countries;}

    public List<SpokenLanguages> getSpoken_languages() { return spoken_languages;}

    public void setSpoken_languages(List<SpokenLanguages> spoken_languages) { this
            .spoken_languages = spoken_languages;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDetail)) return false;

        MovieDetail that = (MovieDetail) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return "MovieDetail" + new Gson().toJson(this);
    }

    public static class Genres implements Parcelable {
        /**
         * id : 35
         * name : Comedy
         */

        private int id;
        private String name;

        public int getId() { return id;}

        public void setId(int id) { this.id = id;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public Genres() {}

        protected Genres(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<Genres> CREATOR = new Parcelable.Creator<Genres>() {
            @Override
            public Genres createFromParcel(Parcel source) {return new Genres(source);}

            @Override
            public Genres[] newArray(int size) {return new Genres[size];}
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Genres)) return false;

            Genres genres = (Genres) o;

            return getId() == genres.getId();

        }

        @Override
        public int hashCode() {
            return getId();
        }

        @Override
        public String toString() {
            return "Genres" + new Gson().toJson(this);
        }
    }

    public static class ProductionCompanies implements Parcelable {
        /**
         * name : Marc Platt Productions
         * id : 2527
         */

        private String name;
        private int id;

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        public int getId() { return id;}

        public void setId(int id) { this.id = id;}

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeInt(this.id);
        }

        public ProductionCompanies() {}

        protected ProductionCompanies(Parcel in) {
            this.name = in.readString();
            this.id = in.readInt();
        }

        public static final Parcelable.Creator<ProductionCompanies> CREATOR = new Parcelable
                .Creator<ProductionCompanies>() {
            @Override
            public ProductionCompanies createFromParcel(Parcel source) {return new ProductionCompanies(source);}

            @Override
            public ProductionCompanies[] newArray(int size) {return new ProductionCompanies[size];}
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProductionCompanies)) return false;

            ProductionCompanies that = (ProductionCompanies) o;

            return getId() == that.getId();

        }

        @Override
        public int hashCode() {
            return getId();
        }

        @Override
        public String toString() {
            return "ProductionCompanies" + new Gson().toJson(this);
        }
    }

    public static class ProductionCountries implements Parcelable {
        /**
         * iso_3166_1 : US
         * name : United States of America
         */

        private String iso_3166_1;
        private String name;

        public String getIso_3166_1() { return iso_3166_1;}

        public void setIso_3166_1(String iso_3166_1) { this.iso_3166_1 = iso_3166_1;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.iso_3166_1);
            dest.writeString(this.name);
        }

        public ProductionCountries() {}

        protected ProductionCountries(Parcel in) {
            this.iso_3166_1 = in.readString();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<ProductionCountries> CREATOR = new Parcelable
                .Creator<ProductionCountries>() {
            @Override
            public ProductionCountries createFromParcel(Parcel source) {return new ProductionCountries(source);}

            @Override
            public ProductionCountries[] newArray(int size) {return new ProductionCountries[size];}
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ProductionCountries)) return false;

            ProductionCountries that = (ProductionCountries) o;

            return getIso_3166_1().equals(that.getIso_3166_1());

        }

        @Override
        public int hashCode() {
            return getIso_3166_1().hashCode();
        }

        @Override
        public String toString() {
            return "ProductionCountries" + new Gson().toJson(this);
        }
    }

    public static class SpokenLanguages implements Parcelable {
        /**
         * iso_639_1 : en
         * name : English
         */

        private String iso_639_1;
        private String name;

        public String getIso_639_1() { return iso_639_1;}

        public void setIso_639_1(String iso_639_1) { this.iso_639_1 = iso_639_1;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.iso_639_1);
            dest.writeString(this.name);
        }

        public SpokenLanguages() {}

        protected SpokenLanguages(Parcel in) {
            this.iso_639_1 = in.readString();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<SpokenLanguages> CREATOR = new Parcelable
                .Creator<SpokenLanguages>() {
            @Override
            public SpokenLanguages createFromParcel(Parcel source) {return new SpokenLanguages(source);}

            @Override
            public SpokenLanguages[] newArray(int size) {return new SpokenLanguages[size];}
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpokenLanguages)) return false;

            SpokenLanguages that = (SpokenLanguages) o;

            return getIso_639_1().equals(that.getIso_639_1());

        }

        @Override
        public int hashCode() {
            return getIso_639_1().hashCode();
        }

        @Override
        public String toString() {
            return "SpokenLanguages" + new Gson().toJson(this);
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdrop_path);
        //dest.writeParcelable(this.belongs_to_collection, flags);
        dest.writeInt(this.budget);
        dest.writeString(this.homepage);
        dest.writeInt(this.id);
        dest.writeString(this.imdb_id);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.vote_average);
        dest.writeInt(this.vote_count);
        dest.writeTypedList(this.genres);
        dest.writeTypedList(this.production_companies);
        dest.writeTypedList(this.production_countries);
        dest.writeTypedList(this.spoken_languages);
    }

    public MovieDetail() {}

    protected MovieDetail(Parcel in) {
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        //this.belongs_to_collection = in.readParcelable(Object.class.getClassLoader());
        this.budget = in.readInt();
        this.homepage = in.readString();
        this.id = in.readInt();
        this.imdb_id = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
        this.vote_count = in.readInt();
        this.genres = in.createTypedArrayList(Genres.CREATOR);
        this.production_companies = in.createTypedArrayList(ProductionCompanies.CREATOR);
        this.production_countries = in.createTypedArrayList(ProductionCountries.CREATOR);
        this.spoken_languages = in.createTypedArrayList(SpokenLanguages.CREATOR);
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {return new MovieDetail(source);}

        @Override
        public MovieDetail[] newArray(int size) {return new MovieDetail[size];}
    };
}
