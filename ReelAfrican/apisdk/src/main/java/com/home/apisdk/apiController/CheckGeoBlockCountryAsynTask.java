package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CheckGeoBlockInputModel;
import com.home.apisdk.apiModel.CheckGeoBlockOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class CheckGeoBlockCountryAsynTask extends AsyncTask<CheckGeoBlockInputModel, Void, Void> {

    CheckGeoBlockInputModel checkGeoBlockInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;
    private String countryCode;

    public interface CheckGeoBlockForCountry {
        void onCheckGeoBlockCountryPreExecuteStarted();
        void onCheckGeoBlockCountryPostExecuteCompleted(CheckGeoBlockOutputModel checkGeoBlockOutputModel, int status, String message);
    }

        private CheckGeoBlockForCountry listener;
        private Context context;
       CheckGeoBlockOutputModel checkGeoBlockOutputModel=new CheckGeoBlockOutputModel();

        public CheckGeoBlockCountryAsynTask(CheckGeoBlockInputModel checkGeoBlockInputModel,CheckGeoBlockForCountry listener, Context context) {
            this.listener = listener;
            this.context=context;

            this.checkGeoBlockInputModel = checkGeoBlockInputModel;
            PACKAGE_NAME=context.getPackageName();
            Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
            Log.v("SUBHA","getFeatureContentAsynTask");

        }

        @Override
        protected Void doInBackground(CheckGeoBlockInputModel... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = APIUrlConstant.getCheckGeoBlockUrl();
                HttpPost httppost = new HttpPost(url);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", this.checkGeoBlockInputModel.getAuthToken());
                httppost.addHeader("ip", this.checkGeoBlockInputModel.getIp());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA", "RES" + responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    status = 0;
                    countryCode = "";
                    message = "";

                } catch (IOException e) {
                    status = 0;
                    countryCode = "";
                    message = "";
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    Object json = new JSONTokener(responseStr).nextValue();
                    if (json instanceof JSONObject){
                        String statusStr = ((JSONObject) json).getString("code");
                        status = Integer.parseInt(statusStr);
                        if (status == 200){
                            countryCode = ((JSONObject) json).getString("country");
                            checkGeoBlockOutputModel.setCountrycode(countryCode);
                        }

                    }
                }




            } catch (Exception e) {
                status = 0;
                message = "";
                countryCode = "";
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onCheckGeoBlockCountryPreExecuteStarted();
            responseStr = "0";
            status = 0;
           /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onGetContentListPostExecuteCompleted(featureContentOutputModel,status,totalItems,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onGetContentListPostExecuteCompleted(featureContentOutputModel,status,totalItems,message);
            }*/


        }



        @Override
        protected void onPostExecute(Void result) {
            listener.onCheckGeoBlockCountryPostExecuteCompleted(checkGeoBlockOutputModel,status,message);

        }

    //}
}
