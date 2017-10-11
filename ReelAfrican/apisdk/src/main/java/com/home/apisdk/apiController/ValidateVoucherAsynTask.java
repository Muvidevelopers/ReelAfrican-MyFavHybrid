package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.ValidateVoucherInputModel;
import com.home.apisdk.apiModel.ValidateVoucherOutputModel;

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
public class ValidateVoucherAsynTask extends AsyncTask<ValidateVoucherInputModel, Void, Void> {

    ValidateVoucherInputModel validateVoucherInputModel;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface ValidateVoucher {
        void onValidateVoucherPreExecuteStarted();

        void onValidateVoucherPostExecuteCompleted(ValidateVoucherOutputModel validateVoucherOutputModel, int status, String message);
    }

    private ValidateVoucher listener;
    private Context context;
    ValidateVoucherOutputModel validateVoucherOutputModel = new ValidateVoucherOutputModel();

    public ValidateVoucherAsynTask(ValidateVoucherInputModel validateVoucherInputModel, ValidateVoucher listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.validateVoucherInputModel = validateVoucherInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "validate voucher");

    }

    @Override
    protected Void doInBackground(ValidateVoucherInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getValidateVoucherUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.validateVoucherInputModel.getAuthToken());
            httppost.addHeader("movie_id", this.validateVoucherInputModel.getMovie_id());
            httppost.addHeader("stream_id", this.validateVoucherInputModel.getStream_id());
            httppost.addHeader("purchase_type", this.validateVoucherInputModel.getPurchase_type());
            httppost.addHeader("season", this.validateVoucherInputModel.getSeason());
            httppost.addHeader("voucher_code", this.validateVoucherInputModel.getVoucher_code());
            httppost.addHeader("user_id", this.validateVoucherInputModel.getUser_id());


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
                if ((myJson.has("msg")) && myJson.getString("msg").trim() != null && !myJson.getString("msg").trim().isEmpty() && !myJson.getString("msg").trim().equals("null") && !myJson.getString("msg").trim().matches("")) {
                    validateVoucherOutputModel.setMsg(myJson.getString("msg"));
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
        listener.onValidateVoucherPreExecuteStarted();
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
        listener.onValidateVoucherPostExecuteCompleted(validateVoucherOutputModel, status, message);

    }

    //}
}
