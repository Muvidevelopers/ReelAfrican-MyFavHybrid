package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.MyLibraryInputModel;
import com.home.apisdk.apiModel.MyLibraryOutputModel;

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
 * Created by User on 12-12-2016.
 */
public class MyLibraryAsynTask extends AsyncTask<MyLibraryInputModel, Void, Void> {

    MyLibraryInputModel myLibraryInputModel;
    String responseStr;
    int status;
    int totalItems;
    String message, PACKAGE_NAME;

    public interface MyLibrary {
        void onMyLibraryPreExecuteStarted();

        void onMyLibraryPostExecuteCompleted(ArrayList<MyLibraryOutputModel> myLibraryOutputModelArray, int status, int totalItems, String message);
    }


    private MyLibrary listener;
    private Context context;
    ArrayList<MyLibraryOutputModel> myLibraryOutputModel = new ArrayList<MyLibraryOutputModel>();

    public MyLibraryAsynTask(MyLibraryInputModel myLibraryInputModel, MyLibrary listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.myLibraryInputModel = myLibraryInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(MyLibraryInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getMylibraryUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.myLibraryInputModel.getAuthToken());
            httppost.addHeader("user_id", this.myLibraryInputModel.getUser_id());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                totalItems = 0;
                message = "";
                Log.v("SUBHA", "ConnectTimeoutException" + e.toString());

            } catch (IOException e) {
                status = 0;
                totalItems = 0;
                message = "";
                Log.v("SUBHA", "IOException" + e.toString());
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");
            }


            if (status == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("mylibrary");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        MyLibraryOutputModel content = new MyLibraryOutputModel();

                        if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                            content.setGenre(jsonChildNode.getString("genre"));

                        }
                        if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                            content.setName(jsonChildNode.getString("name"));
                        }
                        if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                            content.setPosterUrl(jsonChildNode.getString("poster_url"));

                        }
                        if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                            content.setPermalink(jsonChildNode.getString("permalink"));
                        }
                        if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                            content.setContentTypesId(jsonChildNode.getString("content_types_id"));

                        }


                        if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                            content.setIsConverted(Integer.parseInt(jsonChildNode.getString("is_converted")));

                        }
                        if ((jsonChildNode.has("season_id")) && jsonChildNode.getString("season_id").trim() != null && !jsonChildNode.getString("season_id").trim().isEmpty() && !jsonChildNode.getString("season_id").trim().equals("null") && !jsonChildNode.getString("season_id").trim().matches("")) {
                            content.setSeason_id(Integer.parseInt(jsonChildNode.getString("season_id")));

                        }
                        if ((jsonChildNode.has("isFreeContent")) && jsonChildNode.getString("isFreeContent").trim() != null && !jsonChildNode.getString("isFreeContent").trim().isEmpty() && !jsonChildNode.getString("isFreeContent").trim().equals("null") && !jsonChildNode.getString("isFreeContent").trim().matches("")) {
                            content.setIsfreeContent(Integer.parseInt(jsonChildNode.getString("isFreeContent")));

                        }
                        if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
                            content.setMuvi_uniq_id(jsonChildNode.getString("muvi_uniq_id"));

                        }
                        if ((jsonChildNode.has("movie_stream_uniq_id")) && jsonChildNode.getString("movie_stream_uniq_id").trim() != null && !jsonChildNode.getString("movie_stream_uniq_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_uniq_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_uniq_id").trim().matches("")) {
                            content.setMovie_stream_uniq_id(jsonChildNode.getString("movie_stream_uniq_id"));

                        }
                        if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                            content.setContentTypesId(jsonChildNode.getString("is_episode"));

                        }
                        myLibraryOutputModel.add(content);
                    } catch (Exception e) {
                        status = 0;
                        totalItems = 0;
                        message = "";
                    }
                }
            }

        } catch (Exception e) {
            status = 0;
            totalItems = 0;
            message = "";
            Log.v("SUBHA", "Exception" + e.toString());
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onMyLibraryPreExecuteStarted();
        responseStr = "0";
        status = 0;
           /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onGetContentListPostExecuteCompleted(contentListOutput,status,totalItems,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onGetContentListPostExecuteCompleted(contentListOutput,status,totalItems,message);
            }*/

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onMyLibraryPostExecuteCompleted(myLibraryOutputModel, status, totalItems, message);

    }

    //}
}
