package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Login_input;
import com.home.apisdk.apiModel.Login_output;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.R.attr.password;

/**
 * Created by Muvi on 12/16/2016.
 */
public class LoginAsynTask extends AsyncTask<Login_input, Void, Void> {
    Login_input login_input;

    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface LoinDetails {
        void onLoginPreExecuteStarted();

        void onLoginPostExecuteCompleted(Login_output login_output, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private LoinDetails listener;
    private Context context;
    Login_output login_output = new Login_output();

    public LoginAsynTask(Login_input login_input, LoinDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.login_input = login_input;
        Log.v("SUBHA", "LoginAsynTask");
        //PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("Nihar",this.login_input.getEmail()+this.login_input.getPassword());

    }

    @Override
    protected Void doInBackground(Login_input... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getLoginUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.login_input.getAuthToken());
            httppost.addHeader("email", this.login_input.getEmail());
            httppost.addHeader("password", this.login_input.getPassword());
            httppost.addHeader("lang_code", this.login_input.getLang_code());
            httppost.addHeader("device_id", this.login_input.getDevice_id());
            httppost.addHeader("google_id", this.login_input.getGoogle_id());
            httppost.addHeader("device_type", "1");

            Log.v("SUBHA","device id"+ this.login_input.getAuthToken()+this.login_input.getEmail()+this.login_input.getPassword()+this.login_input.getLang_code()+this.login_input.getDevice_id()+this.login_input.getGoogle_id());
            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                status = 0;
                message = "Error";


            } catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject mainJson = null;
            if (responseStr != null) {
                mainJson = new JSONObject(responseStr);
                status = Integer.parseInt(mainJson.optString("code"));


                if ((mainJson.has("email")) && mainJson.optString("email").trim() != null && !mainJson.optString("email").trim().isEmpty() && !mainJson.optString("email").trim().equals("null") && !mainJson.optString("email").trim().matches("")) {
                    login_output.setEmail(mainJson.optString("email"));
                } else {
                    login_output.setEmail("");

                }
                if ((mainJson.has("display_name")) && mainJson.optString("display_name").trim() != null && !mainJson.optString("display_name").trim().isEmpty() && !mainJson.optString("display_name").trim().equals("null") && !mainJson.optString("display_name").trim().matches("")) {
                    String hh = mainJson.optString("display_name");
                    login_output.setDisplay_name(mainJson.optString("display_name"));


                } else {
                    login_output.setDisplay_name("");

                }
                if ((mainJson.has("profile_image")) && mainJson.optString("profile_image").trim() != null && !mainJson.optString("profile_image").trim().isEmpty() && !mainJson.optString("profile_image").trim().equals("null") && !mainJson.optString("profile_image").trim().matches("")) {
                    login_output.setProfile_image(mainJson.optString("profile_image"));


                } else {
                    login_output.setProfile_image("");

                }
                if ((mainJson.has("isSubscribed")) && mainJson.optString("isSubscribed").trim() != null && !mainJson.optString("isSubscribed").trim().isEmpty() && !mainJson.optString("isSubscribed").trim().equals("null") && !mainJson.optString("isSubscribed").trim().matches("")) {
                    login_output.setIsSubscribed(mainJson.optString("story"));
                } else {
                    login_output.setIsSubscribed("");

                }
                if ((mainJson.has("nick_name")) && mainJson.optString("nick_name").trim() != null && !mainJson.optString("nick_name").trim().isEmpty() && !mainJson.optString("nick_name").trim().equals("null") && !mainJson.optString("nick_name").trim().matches("")) {
                    login_output.setNick_name(mainJson.optString("nick_name"));
                } else {
                    login_output.setNick_name("");

                }

                if ((mainJson.has("studio_id")) && mainJson.optString("studio_id").trim() != null && !mainJson.optString("studio_id").trim().isEmpty() && !mainJson.optString("studio_id").trim().equals("null") && !mainJson.optString("studio_id").trim().matches("")) {
                    login_output.setStudio_id(mainJson.optString("studio_id"));

                } else {
                    login_output.setStudio_id("");

                }

                if ((mainJson.has("msg")) && mainJson.optString("msg").trim() != null && !mainJson.optString("msg").trim().isEmpty() && !mainJson.optString("msg").trim().equals("null") && !mainJson.optString("msg").trim().matches("")) {
                    login_output.setMsg(mainJson.optString("msg"));
                } else {
                    login_output.setMsg("");

                }
                if ((mainJson.has("login_history_id")) && mainJson.optString("login_history_id").trim() != null && !mainJson.optString("login_history_id").trim().isEmpty() && !mainJson.optString("login_history_id").trim().equals("null") && !mainJson.optString("login_history_id").trim().matches("")) {
                    login_output.setLogin_history_id(mainJson.optString("login_history_id"));
                } else {
                    login_output.setLogin_history_id("");

                }
                if ((mainJson.has("id")) && mainJson.optString("id").trim() != null && !mainJson.optString("id").trim().isEmpty() && !mainJson.optString("id").trim().equals("null") && !mainJson.optString("id").trim().matches("")) {
                    login_output.setId(mainJson.optString("id"));
                } else {
                    login_output.setId("");

                }

            } else {
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onLoginPreExecuteStarted();

        status = 0;
//        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
//            this.cancel(true);
//            message = "Packge Name Not Matched";
//            listener.onLoginPostExecuteCompleted(login_output, status, message);
//            return;
//        }
//        if (CommonConstants.hashKey.equals("")) {
//            this.cancel(true);
//            message = "Hash Key Is Not Available. Please Initialize The SDK";
//            listener.onLoginPostExecuteCompleted(login_output, status, message);
//        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onLoginPostExecuteCompleted(login_output, status, message);

    }

}
