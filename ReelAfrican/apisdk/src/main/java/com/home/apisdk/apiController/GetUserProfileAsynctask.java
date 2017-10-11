package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Get_UserProfile_Input;
import com.home.apisdk.apiModel.Get_UserProfile_Output;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 1/20/2017.
 */

public class GetUserProfileAsynctask extends AsyncTask<Get_UserProfile_Input, Void, Void> {

    public Get_UserProfile_Input get_userProfile_input;
    String PACKAGE_NAME, message, responseStr, status;
    int code;
    Get_UserProfile_Output get_userProfile_output;

    public interface Get_UserProfile {
        void onGet_UserProfilePreExecuteStarted();

        void onGet_UserProfilePostExecuteCompleted(Get_UserProfile_Output get_userProfile_output, int code, String message, String status);
    }

    private Get_UserProfile listener;
    private Context context;

    public GetUserProfileAsynctask(Get_UserProfile_Input get_userProfile_input, Get_UserProfile listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.get_userProfile_input = get_userProfile_input;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "GetUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(Get_UserProfile_Input... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetProfileDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.get_userProfile_input.getAuthToken());
            httppost.addHeader("email", this.get_userProfile_input.getEmail());
            httppost.addHeader("user_id", this.get_userProfile_input.getUser_id());
            httppost.addHeader("lang_code", this.get_userProfile_input.getLang_code());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";
                status = "";

            } catch (IOException e) {
                code = 0;
                message = "";
                status = "";
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }

            if (code == 200) {


                try {


                    get_userProfile_output = new Get_UserProfile_Output();
                    get_userProfile_output.setId(myJson.optString("id"));
                    get_userProfile_output.setEmail(myJson.optString("email"));
                    get_userProfile_output.setDisplay_name(myJson.optString("display_name"));
                    get_userProfile_output.setStudio_id(myJson.optString("studio_id"));
                    get_userProfile_output.setProfile_image(myJson.optString("profile_image"));
                    get_userProfile_output.setIsSubscribed(myJson.optString("isSubscribed"));

                } catch (Exception e) {
                    code = 0;
                    message = "";
                    status = "";
                }

            }
        } catch (Exception e) {
            code = 0;
            message = "";
            status = "";
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGet_UserProfilePreExecuteStarted();
        code = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGet_UserProfilePostExecuteCompleted(get_userProfile_output, code, message, status);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGet_UserProfilePostExecuteCompleted(get_userProfile_output, code, message, status);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGet_UserProfilePostExecuteCompleted(get_userProfile_output, code, message, status);
    }
}
