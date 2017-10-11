package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.GetVoucherPlanInputModel;
import com.home.apisdk.apiModel.GetVoucherPlanOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class GetVoucherPlanAsynTask extends AsyncTask<GetVoucherPlanInputModel, Void, Void> {

    GetVoucherPlanInputModel getVoucherPlanInputModel;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface GetVoucherPlan {
        void onGetVoucherPlanPreExecuteStarted();

        void onGetVoucherPlanPostExecuteCompleted(GetVoucherPlanOutputModel getVoucherPlanOutputModel, int status, String message);
    }

    private GetVoucherPlan listener;
    private Context context;
    GetVoucherPlanOutputModel getVoucherPlanOutputModel = new GetVoucherPlanOutputModel();

    public GetVoucherPlanAsynTask(GetVoucherPlanInputModel getVoucherPlanInputModel, GetVoucherPlan listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.getVoucherPlanInputModel = getVoucherPlanInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "get voucher plan");

    }

    @Override
    protected Void doInBackground(GetVoucherPlanInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetVoucherPlanUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.getVoucherPlanInputModel.getAuthToken());
            httppost.addHeader("movie_id", this.getVoucherPlanInputModel.getMovie_id());
            httppost.addHeader("stream_id", this.getVoucherPlanInputModel.getStream_id());
            httppost.addHeader("season", this.getVoucherPlanInputModel.getSeason());
            httppost.addHeader("user_id", this.getVoucherPlanInputModel.getUser_id());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                message = "";

            } catch (IOException e) {
                status = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");
            }


            if (status == 200) {
                if ((myJson.has("is_show")) && myJson.getString("is_show").trim() != null && !myJson.getString("is_show").trim().isEmpty() && !myJson.getString("is_show").trim().equals("null") && !myJson.getString("is_show").trim().matches("")) {
                    getVoucherPlanOutputModel.setIs_show(myJson.getString("is_show"));
                }
                if ((myJson.has("is_episode")) && myJson.getString("is_episode").trim() != null && !myJson.getString("is_episode").trim().isEmpty() && !myJson.getString("is_episode").trim().equals("null") && !myJson.getString("is_episode").trim().matches("")) {
                    getVoucherPlanOutputModel.setIs_episode(myJson.getString("is_episode"));
                }
                if ((myJson.has("is_season")) && myJson.getString("is_season").trim() != null && !myJson.getString("is_season").trim().isEmpty() && !myJson.getString("is_season").trim().equals("null") && !myJson.getString("is_season").trim().matches("")) {
                    getVoucherPlanOutputModel.setIs_season(myJson.getString("is_season"));
                }

            }

        } catch (Exception e) {
            status = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetVoucherPlanPreExecuteStarted();
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
        listener.onGetVoucherPlanPostExecuteCompleted(getVoucherPlanOutputModel, status, message);

    }

    //}
}
