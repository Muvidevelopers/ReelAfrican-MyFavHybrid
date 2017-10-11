package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class FeatureContentOutputModel {
    String genre;
    String name;
    String poster_url;
    String permalink;
    String content_types_id;
    String is_episode;

    int is_converted;
    int is_ppv;
    int is_advance;

    public int getIs_advance() {
        return is_advance;
    }

    public void setIs_advance(int is_advance) {
        this.is_advance = is_advance;
    }

    public int getIs_converted() {
        return is_converted;
    }

    public void setIs_converted(int is_converted) {
        this.is_converted = is_converted;
    }

    public int getIs_ppv() {
        return is_ppv;
    }

    public void setIs_ppv(int is_ppv) {
        this.is_ppv = is_ppv;
    }





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

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getContent_types_id() {
        return content_types_id;
    }

    public void setContent_types_id(String content_types_id) {
        this.content_types_id = content_types_id;
    }



    public String getIs_episode() {
        return is_episode;
    }

    public void setIs_episode(String is_episode) {
        this.is_episode = is_episode;
    }




}
