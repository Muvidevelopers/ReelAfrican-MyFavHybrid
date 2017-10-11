package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.VideoLogsInputModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Muvi on 12/16/2016.
 */
public class GetVideoLogsAsynTask extends AsyncTask<VideoLogsInputModel, Void, Void> {
    VideoLogsInputModel videoLogsInputModel;

    String responseStr;
    int status;
    String message, PACKAGE_NAME;
    String videoLogId = "";

    public interface GetVideoLogs {
        void onGetVideoLogsPreExecuteStarted();

        void onGetVideoLogsPostExecuteCompleted(int status, String message, String videoLogId);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetVideoLogs listener;
    private Context context;

    public GetVideoLogsAsynTask(VideoLogsInputModel videoLogsInputModel, GetVideoLogs listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.videoLogsInputModel = videoLogsInputModel;
        Log.v("SUBHA", "LoginAsynTask");
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);

    }

    @Override
    protected Void doInBackground(VideoLogsInputModel... params) {


        try {

            // Execute HTTP Post Request
            try {
                URL url = new URL(APIUrlConstant.getVideoLogsUrl());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("authToken", this.videoLogsInputModel.getAuthToken())
                        .appendQueryParameter("user_id", this.videoLogsInputModel.getUserId())
                        .appendQueryParameter("ip_address", this.videoLogsInputModel.getIpAddress())
                        .appendQueryParameter("movie_id", this.videoLogsInputModel.getMuviUniqueId())
                        .appendQueryParameter("episode_id", this.videoLogsInputModel.getEpisodeStreamUniqueId())
                        .appendQueryParameter("played_length", this.videoLogsInputModel.getPlayedLength())
                        .appendQueryParameter("watch_status", this.videoLogsInputModel.getWatchStatus())
                        .appendQueryParameter("device_type", this.videoLogsInputModel.getDeviceType())
                        .appendQueryParameter("log_id", this.videoLogsInputModel.getVideoLogId());
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


                if ((mainJson.has("log_id")) && mainJson.getString("log_id").trim() != null && !mainJson.getString("log_id").trim().isEmpty() && !mainJson.getString("log_id").trim().equals("null") && !mainJson.getString("log_id").trim().matches("")) {
                    ;
                    videoLogId = mainJson.getString("log_id");
                }

            } else {
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetVideoLogsPreExecuteStarted();

        status = 0;
      /*  if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetVideoLogsPostExecuteCompleted(status, message,videoLogId);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetVideoLogsPostExecuteCompleted(status, message, videoLogId);
        }*/

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetVideoLogsPostExecuteCompleted(status, message, videoLogId);

    }

}
