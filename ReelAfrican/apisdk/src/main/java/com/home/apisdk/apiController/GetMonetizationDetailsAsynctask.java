package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.GetMonetizationDetailsInputModel;
import com.home.apisdk.apiModel.GetMonetizationDetailsOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class GetMonetizationDetailsAsynctask extends AsyncTask<GetMonetizationDetailsInputModel, Void, Void> {

    GetMonetizationDetailsInputModel getMonetizationDetailsInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface GetMonetizationDetails {
        void onGetMonetizationDetailsPreExecuteStarted();
        void onGetMonetizationDetailsPostExecuteCompleted(GetMonetizationDetailsOutputModel getMonetizationDetailsOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetMonetizationDetails listener;
    private Context context;
    GetMonetizationDetailsOutputModel getMonetizationDetailsOutputModel=new GetMonetizationDetailsOutputModel();

    public GetMonetizationDetailsAsynctask(GetMonetizationDetailsInputModel getMonetizationDetailsInputModel,GetMonetizationDetails listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.getMonetizationDetailsInputModel = getMonetizationDetailsInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","transaction" + responseStr);


    }

    @Override
    protected Void doInBackground(GetMonetizationDetailsInputModel... params) {

        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetMonetizationDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.getMonetizationDetailsInputModel.getAuthToken());
            httppost.addHeader("user_id",this.getMonetizationDetailsInputModel.getUser_id());
            httppost.addHeader("movie_id",this.getMonetizationDetailsInputModel.getMovie_id());
            httppost.addHeader("purchase_type",this.getMonetizationDetailsInputModel.getPurchase_type());
            httppost.addHeader("stream_id",this.getMonetizationDetailsInputModel.getStream_id());

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

            Log.v("SUBHA","response = "+ responseStr);
            JSONObject myJson =null;
            if(responseStr!=null){
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }

            if (status > 0) {

                if (status == 200) {

                    JSONObject mainJson = myJson.getJSONObject("monetization_plans");
                    if ((mainJson.has("voucher")) && mainJson.getString("voucher").trim() != null && !mainJson.getString("voucher").trim().isEmpty() && !mainJson.getString("voucher").trim().equals("null") && !mainJson.getString("voucher").trim().matches("")) {
                        getMonetizationDetailsOutputModel.setVoucher(mainJson.getString("voucher"));
                    }else{
                        getMonetizationDetailsOutputModel.setVoucher("");

                    }



                }
            }

            else{

                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";            }

        catch (Exception e)
        {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetMonetizationDetailsPreExecuteStarted();

        status = 0;
        /*if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onTransactionPostExecuteCompleted(transactionOutputModel,status,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onTransactionPostExecuteCompleted(transactionOutputModel,status,message);
        }*/


    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onGetMonetizationDetailsPostExecuteCompleted(getMonetizationDetailsOutputModel,status,message);

    }

    //}
}
