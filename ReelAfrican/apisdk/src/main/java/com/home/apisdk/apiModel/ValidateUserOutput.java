package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class ValidateUserOutput {
    String message;
    String isMemberSubscribed;

    public String getValiduser_str() {
        return validuser_str;
    }

    public void setValiduser_str(String validuser_str) {
        this.validuser_str = validuser_str;
    }

    String validuser_str;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsMemberSubscribed() {
        return isMemberSubscribed;
    }

    public void setIsMemberSubscribed(String isMemberSubscribed) {
        this.isMemberSubscribed = isMemberSubscribed;
    }
}
