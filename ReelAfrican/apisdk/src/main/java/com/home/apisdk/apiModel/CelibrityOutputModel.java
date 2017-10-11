package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class   CelibrityOutputModel {

    String name;
    String cast_type;
    String celebrity_image;


    public String getCelebrity_image() {
        return celebrity_image;
    }

    public void setCelebrity_image(String celebrity_image) {
        this.celebrity_image = celebrity_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCast_type() {
        return cast_type;
    }

    public void setCast_type(String cast_type) {
        this.cast_type = cast_type;
    }




}
