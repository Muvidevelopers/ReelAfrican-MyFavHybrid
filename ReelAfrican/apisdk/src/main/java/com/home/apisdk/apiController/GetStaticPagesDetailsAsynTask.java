package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.GetStaticPageDetailsModelOutput;
import com.home.apisdk.apiModel.GetStaticPagesDeatilsModelInput;

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

public class GetStaticPagesDetailsAsynTask extends AsyncTask<GetStaticPagesDeatilsModelInput, Void, Void> {

    public GetStaticPagesDeatilsModelInput getStaticPagesDeatilsModelInput;
    String PACKAGE_NAME, message, responseStr, status;
    int code;
    GetStaticPageDetailsModelOutput getStaticPageDetailsModelOutput;

    public interface GetStaticPageDetails {
        void onGetStaticPageDetailsPreExecuteStarted();

        void onGetStaticPageDetailsPostExecuteCompleted(GetStaticPageDetailsModelOutput getStaticPageDetailsModelOutput, int code, String message, String status);
    }

    private GetStaticPageDetails listener;
    private Context context;

    public GetStaticPagesDetailsAsynTask(GetStaticPagesDeatilsModelInput getStaticPagesDeatilsModelInput, GetStaticPageDetails listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.getStaticPagesDeatilsModelInput = getStaticPagesDeatilsModelInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "GetUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(GetStaticPagesDeatilsModelInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetstaticpagesUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.getStaticPagesDeatilsModelInput.getAuthToken());
            httppost.addHeader("permalink", this.getStaticPagesDeatilsModelInput.getPermalink());


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

            if (code == 200) {

                Log.v("SUBHA", "code = " + status);
                JSONObject mainJson = myJson.getJSONObject("page_details");
                getStaticPageDetailsModelOutput = new GetStaticPageDetailsModelOutput();
                if ((mainJson.has("content")) && mainJson.getString("content").trim() != null && !mainJson.getString("content").trim().isEmpty() && !mainJson.getString("content").trim().equals("null") && !mainJson.getString("content").trim().matches("")) {
                    Log.v("SUBHA", "mainJson.has(\"content\") = " + mainJson.getString("content"));

                    getStaticPageDetailsModelOutput.setContent(mainJson.getString("content"));

                } else {
                    getStaticPageDetailsModelOutput.setContent("");

                }
                if ((mainJson.has("title")) && mainJson.getString("title").trim() != null && !mainJson.getString("title").trim().isEmpty() && !mainJson.getString("title").trim().equals("null") && !mainJson.getString("title").trim().matches("")) {
                    getStaticPageDetailsModelOutput.setTitle(mainJson.getString("title"));
                } else {
                    getStaticPageDetailsModelOutput.setTitle("");

                }


            }

            Log.v("SUBHA", "content = " + getStaticPageDetailsModelOutput.getContent());
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
        listener.onGetStaticPageDetailsPreExecuteStarted();
        code = 0;
       /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetStaticPageDetailsPostExecuteCompleted(getStaticPageDetailsModelOutput,code,message,status);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetStaticPageDetailsPostExecuteCompleted(getStaticPageDetailsModelOutput,code,message,status);
        }*/
        listener.onGetStaticPageDetailsPostExecuteCompleted(getStaticPageDetailsModelOutput, code, message, status);

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGetStaticPageDetailsPostExecuteCompleted(getStaticPageDetailsModelOutput, code, message, status);
    }
}
