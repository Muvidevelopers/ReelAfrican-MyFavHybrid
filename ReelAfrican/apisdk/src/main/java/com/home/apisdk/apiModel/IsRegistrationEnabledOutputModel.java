package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class IsRegistrationEnabledOutputModel {

    int isMylibrary = 0;
    int is_login =0;
    int signup_step =0;
    int has_favourite=0;

    public String getIsRestrictDevice() {
        return isRestrictDevice;
    }

    public void setIsRestrictDevice(String isRestrictDevice) {
        this.isRestrictDevice = isRestrictDevice;
    }

    String isRestrictDevice;

    public int getIsMylibrary() {
        return isMylibrary;
    }

    public void setIsMylibrary(int isMylibrary) {
        this.isMylibrary = isMylibrary;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public int getSignup_step() {
        return signup_step;
    }

    public void setSignup_step(int signup_step) {
        this.signup_step = signup_step;
    }

    public int getHas_favourite() {
        return has_favourite;
    }

    public void setHas_favourite(int has_favourite) {
        this.has_favourite = has_favourite;
    }







}
