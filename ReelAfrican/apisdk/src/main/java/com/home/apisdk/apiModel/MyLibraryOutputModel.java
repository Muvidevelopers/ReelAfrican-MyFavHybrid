package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class MyLibraryOutputModel {
    String movieId,
            permalink,name,story,releaseDate,
            contentTypesId,posterUrl,genre,is_episode,muvi_uniq_id,movie_stream_uniq_id;
    int isConverted;

    int season_id;
    int isfreeContent;


    public int getIsfreeContent() {
        return isfreeContent;
    }

    public void setIsfreeContent(int isfreeContent) {
        this.isfreeContent = isfreeContent;
    }

    public String getIs_episode() {
        return is_episode;
    }

    public void setIs_episode(String is_episode) {
        this.is_episode = is_episode;
    }

    public String getMuvi_uniq_id() {
        return muvi_uniq_id;
    }

    public void setMuvi_uniq_id(String muvi_uniq_id) {
        this.muvi_uniq_id = muvi_uniq_id;
    }

    public String getMovie_stream_uniq_id() {
        return movie_stream_uniq_id;
    }

    public void setMovie_stream_uniq_id(String movie_stream_uniq_id) {
        this.movie_stream_uniq_id = movie_stream_uniq_id;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }





    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public String getContentTypesId() {
        return contentTypesId;
    }

    public void setContentTypesId(String contentTypesId) {
        this.contentTypesId = contentTypesId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getIsConverted() {
        return isConverted;
    }

    public void setIsConverted(int isConverted) {
        this.isConverted = isConverted;
    }
}
