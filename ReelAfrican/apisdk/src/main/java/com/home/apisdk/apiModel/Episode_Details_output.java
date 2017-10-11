package com.home.apisdk.apiModel;

import java.io.Serializable;

/**
 * Created by Muvi on 9/29/2016.
 */
public class Episode_Details_output implements Serializable {
    String name,muvi_uniq_id,is_ppv,permalink,item_count,id,movie_stream_uniq_id,full_movie,episode_number,video_resolution,
            episode_title,series_number,episode_date,episode_story,video_url,rolltype,roll_after,video_duration,embeddedUrl,
            poster_url,movieUrlForTv,genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuvi_uniq_id() {
        return muvi_uniq_id;
    }

    public void setMuvi_uniq_id(String muvi_uniq_id) {
        this.muvi_uniq_id = muvi_uniq_id;
    }

    public String getIs_ppv() {
        return is_ppv;
    }

    public void setIs_ppv(String is_ppv) {
        this.is_ppv = is_ppv;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_stream_uniq_id() {
        return movie_stream_uniq_id;
    }

    public void setMovie_stream_uniq_id(String movie_stream_uniq_id) {
        this.movie_stream_uniq_id = movie_stream_uniq_id;
    }

    public String getFull_movie() {
        return full_movie;
    }

    public void setFull_movie(String full_movie) {
        this.full_movie = full_movie;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(String episode_number) {
        this.episode_number = episode_number;
    }

    public String getVideo_resolution() {
        return video_resolution;
    }

    public void setVideo_resolution(String video_resolution) {
        this.video_resolution = video_resolution;
    }

    public String getEpisode_title() {
        return episode_title;
    }

    public void setEpisode_title(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getEpisode_date() {
        return episode_date;
    }

    public void setEpisode_date(String episode_date) {
        this.episode_date = episode_date;
    }

    public String getEpisode_story() {
        return episode_story;
    }

    public void setEpisode_story(String episode_story) {
        this.episode_story = episode_story;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getRolltype() {
        return rolltype;
    }

    public void setRolltype(String rolltype) {
        this.rolltype = rolltype;
    }

    public String getRoll_after() {
        return roll_after;
    }

    public void setRoll_after(String roll_after) {
        this.roll_after = roll_after;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getEmbeddedUrl() {
        return embeddedUrl;
    }

    public void setEmbeddedUrl(String embeddedUrl) {
        this.embeddedUrl = embeddedUrl;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getMovieUrlForTv() {
        return movieUrlForTv;
    }

    public void setMovieUrlForTv(String movieUrlForTv) {
        this.movieUrlForTv = movieUrlForTv;
    }


}
