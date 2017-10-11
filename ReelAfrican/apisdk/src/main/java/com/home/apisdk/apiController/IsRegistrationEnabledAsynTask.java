package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class IsRegistrationEnabledAsynTask extends AsyncTask<IsRegistrationEnabledInputModel, Void, Void> {

    IsRegistrationEnabledInputModel isRegistrationEnabledInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface IsRegistrationenabled {
        void onIsRegistrationenabledPreExecuteStarted();
        void onIsRegistrationenabledPostExecuteCompleted(IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private IsRegistrationenabled listener;
    private Context context;
    IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel=new IsRegistrationEnabledOutputModel();

    public IsRegistrationEnabledAsynTask(IsRegistrationEnabledInputModel isRegistrationEnabledInputModel,IsRegistrationenabled listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.isRegistrationEnabledInputModel = isRegistrationEnabledInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","GetContentListAsynTask");


    }

    @Override
    protected Void doInBackground(IsRegistrationEnabledInputModel... params) {

        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getIsRegistrationenabledUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.isRegistrationEnabledInputModel.getAuthToken());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e){

                status = 0;
                message = "Error";



            }catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject myJson =null;
            if(responseStr!=null){
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");
            }



            if (status == 200) {

                if ((myJson.has("is_login")) && myJson.optString("is_login").trim() != null && !myJson.optString("is_login").trim().isEmpty() && !myJson.optString("is_login").trim().equals("null") && !myJson.optString("is_login").trim().matches("")) {
                    isRegistrationEnabledOutputModel.setIs_login(Integer.parseInt(myJson.optString("is_login")));
                }
                if ((myJson.has("isMylibrary")) && myJson.optString("isMylibrary").trim() != null && !myJson.optString("isMylibrary").trim().isEmpty() && !myJson.optString("isMylibrary").trim().equals("null") && !myJson.optString("isMylibrary").trim().matches("")) {
                    isRegistrationEnabledOutputModel.setIsMylibrary(Integer.parseInt(myJson.optString("isMylibrary")));
                }
                if ((myJson.has("signup_step")) && myJson.optString("signup_step").trim() != null && !myJson.optString("signup_step").trim().isEmpty() && !myJson.optString("signup_step").trim().equals("null") && !myJson.optString("signup_step").trim().matches("")) {
                    isRegistrationEnabledOutputModel.setSignup_step(Integer.parseInt(myJson.optString("signup_step")));
                }
                if ((myJson.has("has_favourite")) && myJson.optString("has_favourite").trim() != null && !myJson.optString("has_favourite").trim().isEmpty() && !myJson.optString("has_favourite").trim().equals("null") && !myJson.optString("has_favourite").trim().matches("")) {
                    isRegistrationEnabledOutputModel.setHas_favourite(Integer.parseInt(myJson.optString("has_favourite")));
                }

                if ((myJson.has("isRestrictDevice")) && myJson.optString("isRestrictDevice").trim() != null && !myJson.optString("isRestrictDevice").trim().isEmpty()
                        && !myJson.optString("isRestrictDevice").trim().equals("null") && !myJson.optString("isRestrictDevice").trim().matches("")) {
                    isRegistrationEnabledOutputModel.setIsRestrictDevice(myJson.optString("isRestrictDevice"));
                }


            }

            else{

                responseStr = "0";
                status = 0;
                message = "Error";
            }
        }
        catch (Exception e)
        {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onIsRegistrationenabledPreExecuteStarted();

        status = 0;
            /*if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);
            }*/

        listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);

    }

    //}
}
