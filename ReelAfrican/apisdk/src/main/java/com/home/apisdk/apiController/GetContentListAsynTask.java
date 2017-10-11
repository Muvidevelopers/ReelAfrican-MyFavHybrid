package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.ContentListInput;
import com.home.apisdk.apiModel.ContentListOutput;

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
public class GetContentListAsynTask extends AsyncTask<ContentListInput, Void, Void> {

    ContentListInput contentListInput;
    String responseStr;
    int status;
    int totalItems;
    String message, PACKAGE_NAME;

    public interface GetContentList {
        void onGetContentListPreExecuteStarted();

        void onGetContentListPostExecuteCompleted(ArrayList<ContentListOutput> contentListOutputArray, int status, int totalItems, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetContentList listener;
    private Context context;
    ArrayList<ContentListOutput> contentListOutput = new ArrayList<ContentListOutput>();

    public GetContentListAsynTask(ContentListInput contentListInput, GetContentList listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.contentListInput = contentListInput;
//        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(ContentListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetContentListUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.contentListInput.getAuthToken());
            httppost.addHeader("permalink", this.contentListInput.getPermalink());
            httppost.addHeader("limit", this.contentListInput.getLimit());
            httppost.addHeader("offset", this.contentListInput.getOffset());
            httppost.addHeader("orderby", this.contentListInput.getOrderby());
            httppost.addHeader("country", this.contentListInput.getCountry());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                totalItems = 0;
                message = "";

            } catch (IOException e) {
                status = 0;
                totalItems = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("status"));
                totalItems = Integer.parseInt(myJson.optString("item_count"));
                message = myJson.optString("msg");
            }


            if (status == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        ContentListOutput content = new ContentListOutput();

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
                        //videoTypeIdStr = "1";

                        if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                            content.setIsConverted(Integer.parseInt(jsonChildNode.getString("is_converted")));

                        }
                        if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                            content.setIsAPV(Integer.parseInt(jsonChildNode.getString("is_advance")));

                        }
                        if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                            content.setIsPPV(Integer.parseInt(jsonChildNode.getString("is_ppv")));

                        }
                        if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                            content.setContentTypesId(jsonChildNode.getString("is_episode"));

                        }
                        contentListOutput.add(content);
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
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetContentListPreExecuteStarted();
        responseStr = "0";
        status = 0;
//        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
//            this.cancel(true);
//            message = "Packge Name Not Matched";
//            listener.onGetContentListPostExecuteCompleted(contentListOutput, status, totalItems, message);
//            return;
//        }
//        if (CommonConstants.hashKey.equals("")) {
//            this.cancel(true);
//            message = "Hash Key Is Not Available. Please Initialize The SDK";
//            listener.onGetContentListPostExecuteCompleted(contentListOutput, status, totalItems, message);
//        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetContentListPostExecuteCompleted(contentListOutput, status, totalItems, message);

    }

    //}
}
