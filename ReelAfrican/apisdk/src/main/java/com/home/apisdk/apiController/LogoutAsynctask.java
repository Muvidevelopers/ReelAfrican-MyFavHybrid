package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.LogoutInput;

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

public class LogoutAsynctask extends AsyncTask<LogoutInput, Void, Void> {

    public LogoutInput logoutInput;
    String PACKAGE_NAME, message, responseStr, status;
    int code;



    public interface Logout {
        void onLogoutPreExecuteStarted();

        void onLogoutPostExecuteCompleted(int code, String status, String message);
    }

    private Logout listener;
    private Context context;

    public LogoutAsynctask(LogoutInput logoutInput, Logout listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.logoutInput = logoutInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "LogoutAsynctask");

    }

    @Override
    protected Void doInBackground(LogoutInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getLogoutUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.logoutInput.getAuthToken());
            httppost.addHeader("login_history_id", this.logoutInput.getLogin_history_id());
            httppost.addHeader("lang_code", this.logoutInput.getLang_code());

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
                status = myJson.optString("status");
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
        listener.onLogoutPreExecuteStarted();
        code = 0;
        status = "";
        /*if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onLogoutPostExecuteCompleted(code,status,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onLogoutPostExecuteCompleted(code,status,message);
        }*/
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onLogoutPostExecuteCompleted(code, status, message);
    }
}
