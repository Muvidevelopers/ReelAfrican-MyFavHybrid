package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_Video_Details_Output {

    String videoResolution,videoUrl,emed_url;
    public void setVideoResolution(String videoResolution){
        this.videoResolution = videoResolution;
    }
    public String getVideoResolution(){
        return videoResolution;
    }

    public void setVideoUrl(String videoUrl){this.videoUrl = videoUrl;}
    public String getVideoUrl(){return videoUrl;}

    public void setEmed_url(String emed_url){this.emed_url = emed_url;}
    public String getEmed_url(){return emed_url;}
}
