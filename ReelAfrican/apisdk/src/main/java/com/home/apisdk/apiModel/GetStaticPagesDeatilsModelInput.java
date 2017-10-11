package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class GetStaticPagesDeatilsModelInput {

    String authToken;
    String permalink;


    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }


    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
