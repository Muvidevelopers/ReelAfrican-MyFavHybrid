package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.AuthUserPaymentInfoInputModel;
import com.home.apisdk.apiModel.AuthUserPaymentInfoOutputModel;

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

public class AuthUserPaymentInfoAsyntask extends AsyncTask<AuthUserPaymentInfoInputModel,Void ,Void > {

    public AuthUserPaymentInfoInputModel authUserPaymentInfoInputModel;
    String PACKAGE_NAME,message,responseStr;
    int code;

    public interface AuthUserPaymentInfo{
        void onAuthUserPaymentInfoPreExecuteStarted();
        void onAuthUserPaymentInfoPostExecuteCompleted(AuthUserPaymentInfoOutputModel authUserPaymentInfoOutputModel, int status);
    }

    private AuthUserPaymentInfo listener;
    private Context context;
    AuthUserPaymentInfoOutputModel authUserPaymentInfoOutputModel = new AuthUserPaymentInfoOutputModel();

    public AuthUserPaymentInfoAsyntask(AuthUserPaymentInfoInputModel authUserPaymentInfoInputModel,AuthUserPaymentInfo listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.authUserPaymentInfoInputModel = authUserPaymentInfoInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","register user payment");
    }

    @Override
    protected Void doInBackground(AuthUserPaymentInfoInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAuthUserPaymentInfoUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.authUserPaymentInfoInputModel.getAuthToken());
            httppost.addHeader("nameOnCard", this.authUserPaymentInfoInputModel.getName_on_card());
            httppost.addHeader("expiryMonth", this.authUserPaymentInfoInputModel.getExpiryMonth());
            httppost.addHeader("expiryYear", this.authUserPaymentInfoInputModel.getExpiryYear());
            httppost.addHeader("cardNumber", this.authUserPaymentInfoInputModel.getCardNumber());
            httppost.addHeader("cvv", this.authUserPaymentInfoInputModel.getCvv());
            httppost.addHeader("email", this.authUserPaymentInfoInputModel.getEmail());



            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";


            } catch (IOException e) {
                code = 0;
                message = "";

            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("isSuccess"));

            }

            if(code == 1){

                JSONObject mainJson = null;

                if (myJson.has("card")) {
                    mainJson = myJson.getJSONObject("card");
                    if (mainJson.has("status") && mainJson.getString("status").trim() != null && !mainJson.getString("status").trim().isEmpty() && !mainJson.getString("status").trim().equals("null") && !mainJson.getString("status").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setStatus(mainJson.getString("status"));
                    }

                    if (mainJson.has("token") && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setToken(mainJson.getString("token"));
                    }

                    if (mainJson.has("response_text") && mainJson.getString("response_text").trim() != null && !mainJson.getString("response_text").trim().isEmpty() && !mainJson.getString("response_text").trim().equals("null") && !mainJson.getString("response_text").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setResponse_text( mainJson.getString("response_text"));
                    }

                    if (mainJson.has("profile_id") && mainJson.getString("profile_id").trim() != null && !mainJson.getString("profile_id").trim().isEmpty() && !mainJson.getString("profile_id").trim().equals("null") && !mainJson.getString("profile_id").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setProfile_id(mainJson.getString("profile_id"));
                    }
                    if (mainJson.has("card_last_fourdigit") && mainJson.getString("card_last_fourdigit").trim() != null && !mainJson.getString("card_last_fourdigit").trim().isEmpty() && !mainJson.getString("card_last_fourdigit").trim().equals("null") && !mainJson.getString("card_last_fourdigit").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setCard_last_fourdigit( mainJson.getString("card_last_fourdigit"));
                    }

                    if (mainJson.has("card_type") && mainJson.getString("card_type").trim() != null && !mainJson.getString("card_type").trim().isEmpty() && !mainJson.getString("card_type").trim().equals("null") && !mainJson.getString("card_type").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setCard_type(mainJson.getString("card_type"));
                    }
                }

            }
            /*if (code == 0) {
                if (myJson.has("Message")) {
                     String responseMessageStr = myJson.optString("Message");
                }
                if (((responseMessageStr.equalsIgnoreCase("null")) || (responseMessageStr.length() <= 0))) {
                    responseMessageStr = Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE);

                }
            }*/


        } catch (Exception e) {
            code = 0;
            message = "";

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAuthUserPaymentInfoPreExecuteStarted();
        code= 0;
     /*   if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
        }*/

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onAuthUserPaymentInfoPostExecuteCompleted(authUserPaymentInfoOutputModel,code);
    }
}
