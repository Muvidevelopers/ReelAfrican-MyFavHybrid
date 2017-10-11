package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class ContactUsInputModel {
    String authToken;
    String email;
    String name;
    String message;

    public String getLang_code() {
        return Lang_code;
    }

    public void setLang_code(String lang_code) {
        Lang_code = lang_code;
    }

    String Lang_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
