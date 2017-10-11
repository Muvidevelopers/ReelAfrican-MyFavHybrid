package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Registration_input;
import com.home.apisdk.apiModel.Registration_output;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Muvi on 12/16/2016.
 */
public class RegistrationAsynTask extends AsyncTask<Registration_input, Void, Void> {
    Registration_input registration_input;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface RegistrationDetails {
        void onRegistrationDetailsPreExecuteStarted();

        void onRegistrationDetailsPostExecuteCompleted(Registration_output registration_output, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private RegistrationDetails listener;
    private Context context;
    Registration_output registration_output = new Registration_output();

    public RegistrationAsynTask(Registration_input registration_input, RegistrationDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.registration_input = registration_input;
        //PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "ResistrationAsynTask");

    }

    @Override
    protected Void doInBackground(Registration_input... params) {
        Log.v("Niihhar",this.registration_input.getEmail()+this.registration_input.getPassword()+"\\\\"+status+this.registration_input.getName());


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getRegisterUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.registration_input.getAuthToken());
            httppost.addHeader("email", this.registration_input.getEmail());
            httppost.addHeader("password", this.registration_input.getPassword());
            httppost.addHeader("name", this.registration_input.getName());

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


                if ((mainJson.has("email")) && mainJson.getString("email").trim() != null && !mainJson.getString("email").trim().isEmpty() && !mainJson.getString("email").trim().equals("null") && !mainJson.getString("email").trim().matches("")) {
                    registration_output.setEmail(mainJson.getString("email"));
                } else {
                    registration_output.setEmail("");

                }
                if ((mainJson.has("display_name")) && mainJson.getString("display_name").trim() != null && !mainJson.getString("display_name").trim().isEmpty() && !mainJson.getString("display_name").trim().equals("null") && !mainJson.getString("display_name").trim().matches("")) {
                    String hh = mainJson.getString("display_name");
                    registration_output.setDisplay_name(mainJson.getString("display_name"));


                } else {
                    registration_output.setDisplay_name("");

                }
                if ((mainJson.has("profile_image")) && mainJson.getString("profile_image").trim() != null && !mainJson.getString("profile_image").trim().isEmpty() && !mainJson.getString("profile_image").trim().equals("null") && !mainJson.getString("profile_image").trim().matches("")) {
                    registration_output.setProfile_image(mainJson.getString("profile_image"));


                } else {
                    registration_output.setProfile_image("");

                }
                if ((mainJson.has("isSubscribed")) && mainJson.getString("isSubscribed").trim() != null && !mainJson.getString("isSubscribed").trim().isEmpty() && !mainJson.getString("isSubscribed").trim().equals("null") && !mainJson.getString("isSubscribed").trim().matches("")) {
                    registration_output.setIsSubscribed(mainJson.getString("isSubscribed"));
                } else {
                    registration_output.setIsSubscribed("");

                }
                if ((mainJson.has("nick_name")) && mainJson.getString("nick_name").trim() != null && !mainJson.getString("nick_name").trim().isEmpty() && !mainJson.getString("nick_name").trim().equals("null") && !mainJson.getString("nick_name").trim().matches("")) {
                    registration_output.setNick_name(mainJson.getString("nick_name"));
                } else {
                    registration_output.setNick_name("");

                }

                if ((mainJson.has("studio_id")) && mainJson.getString("studio_id").trim() != null && !mainJson.getString("studio_id").trim().isEmpty() && !mainJson.getString("studio_id").trim().equals("null") && !mainJson.getString("studio_id").trim().matches("")) {
                    registration_output.setStudio_id(mainJson.getString("studio_id"));

                } else {
                    registration_output.setStudio_id("");

                }

                if ((mainJson.has("msg")) && mainJson.getString("msg").trim() != null && !mainJson.getString("msg").trim().isEmpty() && !mainJson.getString("msg").trim().equals("null") && !mainJson.getString("msg").trim().matches("")) {
                    registration_output.setMsg(mainJson.getString("msg"));
                } else {
                    registration_output.setMsg("");

                }
                if ((mainJson.has("login_history_id")) && mainJson.getString("login_history_id").trim() != null && !mainJson.getString("login_history_id").trim().isEmpty() && !mainJson.getString("login_history_id").trim().equals("null") && !mainJson.getString("login_history_id").trim().matches("")) {
                    registration_output.setLoginhistory_id(mainJson.getString("login_history_id"));
                } else {
                    registration_output.setLoginhistory_id("");

                }
                if ((mainJson.has("id")) && mainJson.getString("id").trim() != null && !mainJson.getString("id").trim().isEmpty() && !mainJson.getString("id").trim().equals("null") && !mainJson.getString("id").trim().matches("")) {
                    registration_output.setId(mainJson.getString("id"));
                } else {
                    registration_output.setId("");

                }

            } else {
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {
            Log.v("niihhar","JSONException"+e1.toString());

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {
            Log.v("niihhar","Exception"+e.toString());

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onRegistrationDetailsPreExecuteStarted();


      /*  if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onRegistrationDetailsPostExecuteCompleted(registration_output, status, message);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onRegistrationDetailsPostExecuteCompleted(registration_output, status, message);

        }*/
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.v("niihhar","onPostExecute"+registration_output.getMsg()+"status"+status+"message"+message);
        listener.onRegistrationDetailsPostExecuteCompleted(registration_output, status, message);
    }
}
