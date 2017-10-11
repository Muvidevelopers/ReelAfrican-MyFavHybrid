package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Update_UserProfile_Output {

    String name,email,nick_name,profile_image;
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setNick_name(String nick_name){this.nick_name = nick_name;}
    public String getNick_name(){return nick_name;}

    public void setProfile_image(String profile_image){this.profile_image = profile_image;}
    public String getProfile_image(){return profile_image;}

}
