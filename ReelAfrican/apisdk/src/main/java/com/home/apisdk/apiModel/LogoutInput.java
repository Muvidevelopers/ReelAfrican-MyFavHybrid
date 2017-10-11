package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class LogoutInput {

String authToken;
    String login_history_id;

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    String lang_code;


    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getLogin_history_id() {
        return login_history_id;
    }

    public void setLogin_history_id(String login_history_id) {
        this.login_history_id = login_history_id;
    }
}
