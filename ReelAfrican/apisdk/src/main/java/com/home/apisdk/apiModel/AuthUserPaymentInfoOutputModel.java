package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class AuthUserPaymentInfoOutputModel {

    String card_type;
    String card_last_fourdigit;
    String profile_id;
    String response_text;
    String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_last_fourdigit() {
        return card_last_fourdigit;
    }

    public void setCard_last_fourdigit(String card_last_fourdigit) {
        this.card_last_fourdigit = card_last_fourdigit;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getResponse_text() {
        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String status;





}
