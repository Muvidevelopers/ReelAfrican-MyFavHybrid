package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class PurchaseHistoryInputModel {

    String authToken;
    String user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
