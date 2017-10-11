package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Forgotpassword_input;
import com.home.apisdk.apiModel.Forgotpassword_output;

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
 * Created by Muvi on 12/19/2016.
 */
public class ForgotpassAsynTask extends AsyncTask<Forgotpassword_input, Void, Void> {
    Forgotpassword_input forgotpassword_input;
    String responseStr;
    int status;

    String message,PACKAGE_NAME;


    public interface ForgotpassDetails {
       //String PACKAGE_NAME = getPackageName();
        void onForgotpassDetailsPreExecuteStarted();
        void onForgotpassDetailsPostExecuteCompleted(Forgotpassword_output forgotpassword_output, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private ForgotpassDetails listener;
    private Context context;
    Forgotpassword_output forgotpassword_output=new Forgotpassword_output();

    public ForgotpassAsynTask(Forgotpassword_input forgotpassword_input,ForgotpassDetails listener, Context context) {
        this.forgotpassword_input = forgotpassword_input;
        this.listener = listener;
        this.context=context;

        Log.v("SUBHA", "ForgotpassAsynTask");
        //PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
    }

    @Override
    protected Void doInBackground(Forgotpassword_input... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getForgotPasswordUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.forgotpassword_input.getAuthToken());
            httppost.addHeader("email", this.forgotpassword_input.getEmail());
            httppost.addHeader("lang_code",this.forgotpassword_input.getLang_code());

            Log.v("SUBHA", "responseStr"+this.forgotpassword_input.getAuthToken());
            Log.v("SUBHA", "responseStr"+this.forgotpassword_input.getEmail());
            Log.v("SUBHA", "responseStr"+this.forgotpassword_input.getLang_code());



            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "responseStr"+responseStr);


            } catch (org.apache.http.conn.ConnectTimeoutException e){

                status = 0;
                message = "Error";

            }catch (IOException e) {
                status = 0;
                message = "Error";
            }
            JSONObject mainJson =null;
            if(responseStr!=null) {
                mainJson = new JSONObject(responseStr);

                status = Integer.parseInt(mainJson.optString("code"));

                if ((mainJson.has("msg")) && mainJson.getString("msg").trim() != null && !mainJson.getString("msg").trim().isEmpty() && !mainJson.getString("msg").trim().equals("null") && !mainJson.getString("msg").trim().matches("")) {
                    forgotpassword_output.setMsg(mainJson.getString("msg"));
                } else {
                    forgotpassword_output.setMsg("");
                }

            }
            else{
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";            }

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
        //String pkg=context.getPackageName();
       // String s=forgotpassword_input.getPakagename();
        listener.onForgotpassDetailsPreExecuteStarted();
        status = 0;
       /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onForgotpassDetailsPostExecuteCompleted(forgotpassword_output, status, message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onForgotpassDetailsPostExecuteCompleted(forgotpassword_output, status, message);
        }*/
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onForgotpassDetailsPostExecuteCompleted(forgotpassword_output, status, message);
    }
}
