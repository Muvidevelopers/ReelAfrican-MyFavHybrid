package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class FeatureContentInputModel {
    String authToken;
    String section_id;
    int request_code;

    public int getRequest_code() {
        return request_code;
    }

    public void setRequest_code(int request_code) {
        this.request_code = request_code;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



}
