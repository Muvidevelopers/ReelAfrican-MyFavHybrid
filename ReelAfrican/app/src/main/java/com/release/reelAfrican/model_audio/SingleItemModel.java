package com.release.reelAfrican.model_audio;

/**
 * Created by Muvi on 6/5/2017.
 */

public class SingleItemModel {
    private String name;
    private String mSongName;
    private boolean isClicked;

    public String getmSongName() {
        return mSongName;
    }

    public void setmSongName(String mSongName) {
        this.mSongName = mSongName;
    }

    private String mUrl;
    public boolean isClicked() {
        return isClicked;
    }
    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }


    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    private String mImage;
    private String description;


    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url) {
        this.name = name;
        mUrl = url;
    }
    public SingleItemModel(String name, String songName, String image, String url){
        mImage=image;
        this.name=name;
        mSongName=songName;
        mUrl=url;
    }


    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
