package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 08-May-17.
 */

public class SubscriptionPlanInputModel {
     String authToken ;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    String lang;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
