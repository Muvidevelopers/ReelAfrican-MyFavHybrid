package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/28/2016.
 */
public class ContentDetailsInput {
String permalink;
    String authtoken;
    String user_id;
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public ContentDetailsInput() {

    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getAuthToken() {
        return authtoken;
    }

    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }
}
