package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class DeleteInvoicePdfInputModel {
    String authToken;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    String filepath;




}
