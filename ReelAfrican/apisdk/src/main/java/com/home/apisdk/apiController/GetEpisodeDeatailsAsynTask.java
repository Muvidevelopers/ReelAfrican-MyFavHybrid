package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;

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
 * Created by Muvi on 12/14/2016.
 */
public class GetEpisodeDeatailsAsynTask extends AsyncTask<Episode_Details_input, Void, Void> {
    Episode_Details_input episode_details_input;
    String responseStr;
    int status,is_ppv,item_count;
    String message,permalink,PACKAGE_NAME,name;


    public interface GetEpisodeDetails {
        void onGetEpisodeDetailsPreExecuteStarted();
        void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output, int i, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetEpisodeDetails listener;
    private Context context;
    ArrayList<Episode_Details_output> episode_details_output=new ArrayList<Episode_Details_output>();

    public GetEpisodeDeatailsAsynTask(Episode_Details_input episode_details_input,GetEpisodeDetails listener, Context context) {
        this.listener=listener;
        this.context=context;
        this.episode_details_input = episode_details_input;
        //PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(Episode_Details_input... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetEpisodeDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.episode_details_input.getAuthtoken());
            httppost.addHeader("permalink", this.episode_details_input.getPermalink());
            httppost.addHeader("limit",this.episode_details_input.getLimit());
            httppost.addHeader("offset",this.episode_details_input.getOffset());

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
                message = myJson.optString("msg");
                is_ppv= Integer.parseInt(myJson.optString("is_ppv"));
                permalink=myJson.getString("permalink");
                item_count= Integer.parseInt(myJson.optString("item_count"));
                name= myJson.optString("name");

            }

            if (status > 0) {

                if (status == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("episode");
                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            Episode_Details_output content = new Episode_Details_output();

                            if ((jsonChildNode.has("episode_title")) && jsonChildNode.getString("episode_title").trim() != null && !jsonChildNode.getString("episode_title").trim().isEmpty() && !jsonChildNode.getString("episode_title").trim().equals("null") && !jsonChildNode.getString("episode_title").trim().matches("")) {
                               // String episode_title=jsonChildNode.getString("episode_title");
                                content.setEpisode_title(jsonChildNode.getString("episode_title"));
                            }
                            if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                content.setPoster_url(jsonChildNode.getString("poster_url"));

                            }  if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                content.setPoster_url(jsonChildNode.getString("poster_url"));

                            }
                            if ((jsonChildNode.has("video_url")) && jsonChildNode.getString("video_url").trim() != null && !jsonChildNode.getString("video_url").trim().isEmpty() && !jsonChildNode.getString("video_url").trim().equals("null") && !jsonChildNode.getString("video_url").trim().matches("")) {
                                content.setVideo_url(jsonChildNode.getString("video_url"));

                            }
                            if ((jsonChildNode.has("episode_story")) && jsonChildNode.getString("episode_story").trim() != null && !jsonChildNode.getString("episode_story").trim().isEmpty() && !jsonChildNode.getString("episode_story").trim().equals("null") && !jsonChildNode.getString("episode_story").trim().matches("")) {
                                content.setPermalink(jsonChildNode.getString("episode_story"));
                            }
                           if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.getString("embeddedUrl").trim() != null && !jsonChildNode.getString("embeddedUrl").trim().isEmpty() && !jsonChildNode.getString("embeddedUrl").trim().equals("null") && !jsonChildNode.getString("embeddedUrl").trim().matches("")) {
                                content.setEmbeddedUrl(jsonChildNode.getString("embeddedUrl"));

                            }

                            //videoTypeIdStr = "1";

                            if ((jsonChildNode.has("video_duration")) && jsonChildNode.getString("video_duration").trim() != null && !jsonChildNode.getString("video_duration").trim().isEmpty() && !jsonChildNode.getString("video_duration").trim().equals("null") && !jsonChildNode.getString("video_duration").trim().matches("")) {
                                content.setVideo_duration(jsonChildNode.getString("video_duration"));

                            }

                            content.setName(name);
                           /*  if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                content.setIsAPV(Integer.parseInt(jsonChildNode.getString("is_advance")));

                            }
                            if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                content.setIsPPV(Integer.parseInt(jsonChildNode.getString("is_ppv")));

                            }
                            if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                content.setContentTypesId(jsonChildNode.getString("is_episode"));

                            }*/
                           episode_details_output.add(content);
                        } catch (Exception e) {
                            status = 0;
                          // totalItems = 0;
                            message = "";
                        }
                    }
                } else {
                    responseStr = "0";
                    status = 0;
                   // totalItems = 0;
                    message = "";
                }
            }
        } catch (Exception e) {
            status = 0;
           // totalItems = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetEpisodeDetailsPreExecuteStarted();
        status = 0;
//        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
//        {
//            this.cancel(true);
//            message = "Packge Name Not Matched";
//            listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);
//            return;
//        }
//        if(CommonConstants.hashKey.equals(""))
//        {
//            this.cancel(true);
//            message = "Hash Key Is Not Available. Please Initialize The SDK";
//            listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);
//        }

    }



    @Override
    protected void onPostExecute(Void result) {

        try{
    listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);
            }catch (Exception e){}

    }

    //}
}
