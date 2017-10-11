package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by User on 12-12-2016.
 */
public class GetLanguageListAsynTask extends AsyncTask<LanguageListInputModel, Void, Void> {

    LanguageListInputModel languageListInputModel;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;
    String defaultLanguage = "en";

    public interface GetLanguageList {
        void onGetLanguageListPreExecuteStarted();

        void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetLanguageList listener;
    private Context context;
    ArrayList<LanguageListOutputModel> languageListOutputArray = new ArrayList<LanguageListOutputModel>();

    public GetLanguageListAsynTask(LanguageListInputModel languageListInputModel, GetLanguageList listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.languageListInputModel = languageListInputModel;
        PACKAGE_NAME = context.getPackageName();

    }

    @Override
    protected Void doInBackground(LanguageListInputModel... params) {
        Log.v("SUBHA", "this.languageListInputModel.getAuthToken()" + this.languageListInputModel.getAuthToken());

        try {

            try {
                URL url = new URL(APIUrlConstant.getGetLanguageListUrl());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("authToken", this.languageListInputModel.getAuthToken());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                InputStream ins = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    responseStr = inputLine;
                    Log.v("SUBHA", "responseStr" + responseStr);

                }
                in.close();

            }
            // Execute HTTP Post Request
            catch (org.apache.http.conn.ConnectTimeoutException e) {
                Log.v("SUBHA", "org.apache.http.conn.ConnectTimeoutException e" + e.toString());

                status = 0;
                message = "";

            } catch (IOException e) {
                Log.v("SUBHA", "IOException" + e.toString());

                status = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                if (myJson.has("msg")) {
                    message = myJson.optString("msg");
                }
                if (myJson.has("default_lang")) {
                    defaultLanguage = myJson.optString("default_lang");
                }
            }


            if (status > 0 && status == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("lang_list");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        LanguageListOutputModel languageListOutputModel = new LanguageListOutputModel();

                        Log.v("SUBHA", "LAN_CODE" + jsonChildNode.getString("code"));
                        if ((jsonChildNode.has("code")) && jsonChildNode.getString("code").trim() != null && !jsonChildNode.getString("code").trim().isEmpty() && !jsonChildNode.getString("code").trim().equals("null") && !jsonChildNode.getString("code").trim().matches("")) {
                            languageListOutputModel.setLanguageCode(jsonChildNode.getString("code"));

                        }
                        if ((jsonChildNode.has("language")) && jsonChildNode.getString("language").trim() != null && !jsonChildNode.getString("language").trim().isEmpty() && !jsonChildNode.getString("language").trim().equals("null") && !jsonChildNode.getString("language").trim().matches("")) {
                            languageListOutputModel.setLanguageName(jsonChildNode.getString("language"));
                        }

                        languageListOutputArray.add(languageListOutputModel);
                    } catch (Exception e) {
                        status = 0;
                        message = "";
                    }
                }
            }

        } catch (Exception e) {
            Log.v("SUBHA", "Exception" + e.toString());

            status = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetLanguageListPreExecuteStarted();
           /* status= 0;
            responseStr = "0";*/
           /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onGetLanguageListPostExecuteCompleted(languageListOutputArray, status, message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onGetLanguageListPostExecuteCompleted(languageListOutputArray, status, message);
            }*/
    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetLanguageListPostExecuteCompleted(languageListOutputArray, status, message, defaultLanguage);

    }

    //}
}
