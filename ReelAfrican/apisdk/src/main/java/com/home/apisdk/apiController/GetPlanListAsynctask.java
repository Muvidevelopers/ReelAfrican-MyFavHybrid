package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.SubscriptionPlanInputModel;
import com.home.apisdk.apiModel.SubscriptionPlanOutputModel;

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
 * Created by MUVI on 1/20/2017.
 */

public class GetPlanListAsynctask extends AsyncTask<SubscriptionPlanInputModel, Void, Void> {

    public SubscriptionPlanInputModel planListInput;
    String PACKAGE_NAME, message, responseStr;
    int code;

    public interface GetStudioPlanLists {
        void onGetPlanListPreExecuteStarted();

        void onGetPlanListPostExecuteCompleted(ArrayList<SubscriptionPlanOutputModel> planListOutput, int status);
    }

    private GetStudioPlanLists listener;
    private Context context;
    ArrayList<SubscriptionPlanOutputModel> planListOutput = new ArrayList<SubscriptionPlanOutputModel>();

    public GetPlanListAsynctask(SubscriptionPlanInputModel planListInput, GetStudioPlanLists listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.planListInput = planListInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "getPlanListAsynctask");
        Log.v("SUBHA", "authToken = " + this.planListInput.getAuthToken());
    }

    @Override
    protected Void doInBackground(SubscriptionPlanInputModel... params) {

        Log.v("SUBHA", "doInbkg....");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getSubscriptionPlanLists());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.planListInput.getAuthToken());
            httppost.addHeader("lang_code", this.planListInput.getLang());

            Log.v("SUBHA", "authToken = " + this.planListInput.getAuthToken());
            // Execute HTTP Post Request
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
                message = myJson.optString("status");

            }

            if (code == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("plans");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        SubscriptionPlanOutputModel content = new SubscriptionPlanOutputModel();

                        if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {
                            content.setId(jsonChildNode.getString("id"));

                        }
                        if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                            content.setName(jsonChildNode.getString("name"));
                        }
                        if ((jsonChildNode.has("recurrence")) && jsonChildNode.getString("recurrence").trim() != null && !jsonChildNode.getString("recurrence").trim().isEmpty() && !jsonChildNode.getString("recurrence").trim().equals("null") && !jsonChildNode.getString("recurrence").trim().matches("")) {
                            content.setRecurrence(jsonChildNode.getString("recurrence"));
                        }
                        if ((jsonChildNode.has("frequency")) && jsonChildNode.getString("frequency").trim() != null && !jsonChildNode.getString("frequency").trim().isEmpty() && !jsonChildNode.getString("frequency").trim().equals("null") && !jsonChildNode.getString("frequency").trim().matches("")) {
                            content.setFrequency(jsonChildNode.getString("frequency"));
                        }
                        if ((jsonChildNode.has("studio_id")) && jsonChildNode.getString("studio_id").trim() != null && !jsonChildNode.getString("studio_id").trim().isEmpty() && !jsonChildNode.getString("studio_id").trim().equals("null") && !jsonChildNode.getString("studio_id").trim().matches("")) {
                            content.setStudio_id(jsonChildNode.getString("studio_id"));
                        }
                        if ((jsonChildNode.has("status")) && jsonChildNode.getString("status").trim() != null && !jsonChildNode.getString("status").trim().isEmpty() && !jsonChildNode.getString("status").trim().equals("null") && !jsonChildNode.getString("status").trim().matches("")) {
                            content.setStatus(jsonChildNode.getString("status"));
                        }
                        if ((jsonChildNode.has("language_id")) && jsonChildNode.getString("language_id").trim() != null && !jsonChildNode.getString("language_id").trim().isEmpty() && !jsonChildNode.getString("language_id").trim().equals("null") && !jsonChildNode.getString("language_id").trim().matches("")) {
                            content.setLanguage_id(jsonChildNode.getString("language_id"));
                        }
                        if ((jsonChildNode.has("price")) && jsonChildNode.getString("price").trim() != null && !jsonChildNode.getString("price").trim().isEmpty() && !jsonChildNode.getString("price").trim().equals("null") && !jsonChildNode.getString("price").trim().matches("")) {
                            content.setPrice(jsonChildNode.getString("price"));
                        }
                        if ((jsonChildNode.has("trial_period")) && jsonChildNode.getString("trial_period").trim() != null && !jsonChildNode.getString("trial_period").trim().isEmpty() && !jsonChildNode.getString("trial_period").trim().equals("null") && !jsonChildNode.getString("trial_period").trim().matches("")) {
                            content.setTrial_period(jsonChildNode.getString("trial_period"));
                        }
                        if ((jsonChildNode.has("trial_recurrence")) && jsonChildNode.getString("trial_recurrence").trim() != null && !jsonChildNode.getString("trial_recurrence").trim().isEmpty() && !jsonChildNode.getString("trial_recurrence").trim().equals("null") && !jsonChildNode.getString("trial_recurrence").trim().matches("")) {
                            content.setTrial_recurrence(jsonChildNode.getString("trial_recurrence"));
                        }


                        if (jsonChildNode.has("currency")) {
                            JSONObject currencyJson = jsonChildNode.getJSONObject("currency");
                            CurrencyModel currencyModel = new CurrencyModel();
                            if (currencyJson.has("id") && currencyJson.getString("id").trim() != null && !currencyJson.getString("id").trim().isEmpty() && !currencyJson.getString("id").trim().equals("null") && !currencyJson.getString("id").trim().matches("")) {
                                currencyModel.setCurrencyId(currencyJson.getString("id"));
                            } else {
                                currencyModel.setCurrencyId("");

                            }
                            if (currencyJson.has("country_code") && currencyJson.getString("country_code").trim() != null && !currencyJson.getString("country_code").trim().isEmpty() && !currencyJson.getString("country_code").trim().equals("null") && !currencyJson.getString("country_code").trim().matches("")) {
                                currencyModel.setCurrencyCode(currencyJson.getString("country_code"));
                            } else {
                                currencyModel.setCurrencyCode("");
                            }
                            if (currencyJson.has("symbol") && currencyJson.getString("symbol").trim() != null && !currencyJson.getString("symbol").trim().isEmpty() && !currencyJson.getString("symbol").trim().equals("null") && !currencyJson.getString("symbol").trim().matches("")) {
                                currencyModel.setCurrencySymbol(currencyJson.getString("symbol"));
                            } else {
                                currencyModel.setCurrencySymbol("");
                            }

                            content.setCurrencyDetails(currencyModel);
                        }


                        planListOutput.add(content);
                    } catch (Exception e) {
                        code = 0;
                        message = "";
                    }
                }
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
        listener.onGetPlanListPreExecuteStarted();
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
        listener.onGetPlanListPostExecuteCompleted(planListOutput, code);
    }
}
