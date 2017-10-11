package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class TransactionInputModel {

    String authToken;
    String user_id;
    String id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
