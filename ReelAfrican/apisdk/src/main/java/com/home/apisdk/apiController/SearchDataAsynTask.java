package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Search_Data_input;
import com.home.apisdk.apiModel.Search_Data_otput;

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
public class SearchDataAsynTask extends AsyncTask<Search_Data_input, Void, Void> {
    Search_Data_input search_data_input;
    String responseStr;
    int status;
    int totalItems;
    String message, PACKAGE_NAME;


    public interface SearchData {
        void onSearchDataPreexecute();

        void onSearchDataPostExecuteCompleted(ArrayList<Search_Data_otput> contentListOutputArray, int status, int totalItems, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private SearchData listener;
    private Context context;
    ArrayList<Search_Data_otput> search_data_otputs = new ArrayList<Search_Data_otput>();

    public SearchDataAsynTask(Search_Data_input search_data_input, SearchData listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.search_data_input = search_data_input;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
    }
   /* public SearchDataAsynTask(Search_Data_input search_data_input,SearchData listener) {
        this.listener = listener;

        this.search_data_input = search_data_input;
        Log.v("SUBHA", "GetContentListAsynTask");

    }*/

    @Override
    protected Void doInBackground(Search_Data_input... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getSearchDataUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.search_data_input.getAuthToken());
            httppost.addHeader("limit", this.search_data_input.getLimit());
            httppost.addHeader("offset", this.search_data_input.getOffset());
            httppost.addHeader("q", this.search_data_input.getQ());


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
                status = Integer.parseInt(myJson.optString("code"));
                totalItems = Integer.parseInt(myJson.optString("item_count"));
                message = myJson.optString("msg");
            }

            if (status > 0) {
                if (status == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("search");

                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            Search_Data_otput content = new Search_Data_otput();

                            if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                content.setGenre(jsonChildNode.getString("genre"));

                            }
                            if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                content.setName(jsonChildNode.getString("name"));
                            }
                            if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                content.setPoster_url(jsonChildNode.getString("poster_url"));

                            }
                            if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                content.setPermalink(jsonChildNode.getString("permalink"));
                            }
                            if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                content.setContent_types_id(jsonChildNode.getString("content_types_id"));

                            }
                            //videoTypeIdStr = "1";

                            if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                content.setIs_converted(Integer.parseInt(jsonChildNode.getString("is_converted")));

                            }
                            if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                content.setIs_advance(Integer.parseInt(jsonChildNode.getString("is_advance")));

                            }
                            if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                content.setIs_ppv(Integer.parseInt(jsonChildNode.getString("is_ppv")));

                            }
                            if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                content.setIs_episode(jsonChildNode.getString("is_episode"));

                            }
                            search_data_otputs.add(content);
                        } catch (Exception e) {
                            status = 0;
                            totalItems = 0;
                            message = "";
                        }
                    }
                } else {
                    responseStr = "0";
                    status = 0;
                    totalItems = 0;
                    message = "";
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
        listener.onSearchDataPreexecute();

        status = 0;
        totalItems = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onSearchDataPostExecuteCompleted(search_data_otputs, status, totalItems, message);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onSearchDataPostExecuteCompleted(search_data_otputs, status, totalItems, message);
        }
    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onSearchDataPostExecuteCompleted(search_data_otputs, status, totalItems, message);

    }

    //}
}

