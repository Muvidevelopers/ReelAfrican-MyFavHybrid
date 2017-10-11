package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.VoucherSubscriptionInputModel;
import com.home.apisdk.apiModel.VoucherSubscriptionOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 1/20/2017.
 */

public class VoucherSubscriptionAsyntask extends AsyncTask<VoucherSubscriptionInputModel, Void, Void> {

    public VoucherSubscriptionInputModel voucherSubscriptionInputModel;
    String PACKAGE_NAME, message, responseStr;
    int code;

    public interface VoucherSubscription {
        void onVoucherSubscriptionPreExecuteStarted();

        void onVoucherSubscriptionPostExecuteCompleted(VoucherSubscriptionOutputModel voucherSubscriptionOutputModel, int status);
    }

    private VoucherSubscription listener;
    private Context context;
    VoucherSubscriptionOutputModel voucherSubscriptionOutputModel = new VoucherSubscriptionOutputModel();

    public VoucherSubscriptionAsyntask(VoucherSubscriptionInputModel voucherSubscriptionInputModel, VoucherSubscription listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.voucherSubscriptionInputModel = voucherSubscriptionInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "register user payment");

    }

    @Override
    protected Void doInBackground(VoucherSubscriptionInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getVoucherSubscriptionUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.voucherSubscriptionInputModel.getAuthToken());
            httppost.addHeader("user_id", this.voucherSubscriptionInputModel.getUser_id());
            httppost.addHeader("movie_id", this.voucherSubscriptionInputModel.getMovie_id());
            httppost.addHeader("stream_id", this.voucherSubscriptionInputModel.getStream_id());
            httppost.addHeader("purchase_type", this.voucherSubscriptionInputModel.getPurchase_type());
            httppost.addHeader("voucher_code", this.voucherSubscriptionInputModel.getVoucher_code());
            httppost.addHeader("is_preorder", this.voucherSubscriptionInputModel.getIs_preorder());
            httppost.addHeader("season", this.voucherSubscriptionInputModel.getSeason());


            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";


            } catch (IOException e) {
                code = 0;
                message = "";

            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));

            }

            if ((myJson.has("msg")) && myJson.getString("msg").trim() != null && !myJson.getString("msg").trim().isEmpty() && !myJson.getString("msg").trim().equals("null") && !myJson.getString("msg").trim().matches("")) {
                voucherSubscriptionOutputModel.setMsg(myJson.getString("msg"));
            }

        } catch (Exception e) {
            code = 0;
            message = "";

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onVoucherSubscriptionPreExecuteStarted();
        code = 0;
     /*   if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
        }*/

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onVoucherSubscriptionPostExecuteCompleted(voucherSubscriptionOutputModel, code);
    }
}
