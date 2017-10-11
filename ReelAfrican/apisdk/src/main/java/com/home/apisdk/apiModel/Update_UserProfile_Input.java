package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Update_UserProfile_Input {

    String authToken,user_id,name,password;
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setAuthToken(String authToken){this.authToken = authToken;}
    public String getAuthToken(){return authToken;}

    public void setUser_id(String user_id){this.user_id = user_id;}
    public String getUser_id(){return user_id;}

    public void setPassword(String password){this.password = password;}
    public String getPassword(){return password;}

}
