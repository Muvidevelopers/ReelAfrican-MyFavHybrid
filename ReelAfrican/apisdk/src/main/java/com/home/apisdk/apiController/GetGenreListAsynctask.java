package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.GenreListInput;
import com.home.apisdk.apiModel.GenreListOutput;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class GetGenreListAsynctask extends AsyncTask<GenreListInput,Void ,Void > {

    public GenreListInput genreListInput;
    String PACKAGE_NAME,status,responseStr;
    int code;

    public interface GenreList{
        void onGetGenreListPreExecuteStarted();
        void onGetGenreListPostExecuteCompleted(ArrayList<GenreListOutput> genreListOutput, int code, String status);
    }

    private GenreList listener;
    private Context context;
    ArrayList<GenreListOutput> genreListOutput = new ArrayList<GenreListOutput>();

    public GetGenreListAsynctask(GenreListInput genreListInput, GenreList listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.genreListInput = genreListInput;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","GetGenreListAsynctask");

    }

    @Override
    protected Void doInBackground(GenreListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGenreListUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.genreListInput.getAuthToken());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                status = "";

            } catch (IOException e) {
                code = 0;
                status = "";
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                status = myJson.optString("status");
            }

                if (code == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("genre_list");

                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        try {
                            GenreListOutput content = new GenreListOutput();
                            content.setGenre_name(jsonMainNode.get(i).toString());

                            Log.v("SUBHA", "setGenre_name====== " +jsonMainNode.get(i).toString());

                            genreListOutput.add(content);
                        } catch (Exception e) {
                            code = 0;
                            status = "";
                        }
                    }
                }
        } catch (Exception e) {
            code = 0;
            status = "";
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetGenreListPreExecuteStarted();
        code= 0;
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            status = "Package Name Not Matched";
            listener.onGetGenreListPostExecuteCompleted(genreListOutput,code,status);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            status = "Please Initialize The SDK";
            listener.onGetGenreListPostExecuteCompleted(genreListOutput,code,status);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGetGenreListPostExecuteCompleted(genreListOutput,code,status);
    }
}
