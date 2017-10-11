package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_UserProfile_Output {

    String id,display_name,email,studio_id,profile_image,isSubscribed;
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setDisplay_name(String display_name){this.display_name = display_name;}
    public String getDisplay_name(){return display_name;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setStudio_id(String studio_id){this.studio_id = studio_id;}
    public String getStudio_id(){return studio_id;}

    public void setProfile_image(String profile_image){this.profile_image = profile_image;}
    public String getProfile_image(){return profile_image;}

    public void setIsSubscribed(String isSubscribed){this.isSubscribed = isSubscribed;}
    public String getIsSubscribed(){return isSubscribed;}

}
