package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class GetVideoDetailsInput {
    String authToken;
    String content_uniq_id;
    String stream_uniq_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;
    String internetSpeed = "0";
    public String getAuthToken() {
        return authToken;
    }

    public String getInternetSpeed() {
        return internetSpeed;
    }

    public void setInternetSpeed(String internetSpeed) {
        this.internetSpeed = internetSpeed;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getContent_uniq_id() {
        return content_uniq_id;
    }

    public void setContent_uniq_id(String content_uniq_id) {
        this.content_uniq_id = content_uniq_id;
    }

    public String getStream_uniq_id() {
        return stream_uniq_id;
    }

    public void setStream_uniq_id(String stream_uniq_id) {
        this.stream_uniq_id = stream_uniq_id;
    }
}
