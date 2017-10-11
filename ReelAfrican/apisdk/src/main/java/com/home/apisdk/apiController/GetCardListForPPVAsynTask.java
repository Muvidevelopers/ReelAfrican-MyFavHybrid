package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.GetCardListForPPVInputModel;
import com.home.apisdk.apiModel.GetCardListForPPVOutputModel;

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
public class GetCardListForPPVAsynTask extends AsyncTask<GetCardListForPPVInputModel, Void, Void> {

    GetCardListForPPVInputModel getCardListForPPVInputModel;
    String responseStr;
    int status;
    int totalItems;
    String message,PACKAGE_NAME;

    public interface GetCardListForPPV {
        void onGetCardListForPPVPreExecuteStarted();
        void onGetCardListForPPVPostExecuteCompleted(ArrayList<GetCardListForPPVOutputModel> getCardListForPPVOutputModelArray, int status, int totalItems, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

        private GetCardListForPPV listener;
        private Context context;
        ArrayList<GetCardListForPPVOutputModel> getCardListForPPVOutputModel=new ArrayList<GetCardListForPPVOutputModel>();

        public GetCardListForPPVAsynTask(GetCardListForPPVInputModel getCardListForPPVInputModel,GetCardListForPPV listener, Context context) {
            this.listener = listener;
            this.context=context;


            this.getCardListForPPVInputModel = getCardListForPPVInputModel;
            PACKAGE_NAME=context.getPackageName();
            Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
            Log.v("SUBHA","GetContentListAsynTask");

        }

        @Override
        protected Void doInBackground(GetCardListForPPVInputModel... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getGetCardListForPpvUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", this.getCardListForPPVInputModel.getAuthToken());
                httppost.addHeader("user_id", this.getCardListForPPVInputModel.getUser_id());


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

                    message = myJson.optString("status");
                }


                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("cards");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                GetCardListForPPVOutputModel content = new GetCardListForPPVOutputModel();

                                if ((jsonChildNode.has("card_last_fourdigit")) && jsonChildNode.getString("card_last_fourdigit").trim() != null && !jsonChildNode.getString("card_last_fourdigit").trim().isEmpty() && !jsonChildNode.getString("card_last_fourdigit").trim().equals("null") && !jsonChildNode.getString("card_last_fourdigit").trim().matches("")) {
                                    content.setCard_last_fourdigit(jsonChildNode.getString("card_last_fourdigit"));

                                }
                                if ((jsonChildNode.has("card_id")) && jsonChildNode.getString("card_id").trim() != null && !jsonChildNode.getString("card_id").trim().isEmpty() && !jsonChildNode.getString("card_id").trim().equals("null") && !jsonChildNode.getString("card_id").trim().matches("")) {
                                    content.setCard_id(jsonChildNode.getString("card_id"));
                                }

                                getCardListForPPVOutputModel.add(content);
                            } catch (Exception e) {
                                status = 0;

                                message = "";
                            }
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
            listener.onGetCardListForPPVPreExecuteStarted();
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
            listener.onGetCardListForPPVPostExecuteCompleted(getCardListForPPVOutputModel,status,totalItems,message);

        }

    //}
}
