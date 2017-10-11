package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class CheckGeoBlockInputModel {

    String authToken;
    String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
