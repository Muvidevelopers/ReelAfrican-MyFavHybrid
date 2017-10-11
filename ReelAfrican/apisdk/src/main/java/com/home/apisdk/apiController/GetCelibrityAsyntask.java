package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CelibrityInputModel;
import com.home.apisdk.apiModel.CelibrityOutputModel;

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

public class GetCelibrityAsyntask extends AsyncTask<CelibrityInputModel,Void ,Void > {

    public CelibrityInputModel celibrityInputModel;
    String PACKAGE_NAME,message,responseStr,msg;
    int code;

    public interface GetCelibrity{
        void onGetCelibrityPreExecuteStarted();
        void onGetCelibrityPostExecuteCompleted(ArrayList<CelibrityOutputModel> celibrityOutputModel, int status,String msg);
    }

    private GetCelibrity listener;
    private Context context;
    ArrayList<CelibrityOutputModel> celibrityOutputModel = new ArrayList<CelibrityOutputModel>();

    public GetCelibrityAsyntask(CelibrityInputModel celibrityInputModel,GetCelibrity listener, Context context) {
        this.listener = listener;
        this.context=context;


        this.celibrityInputModel = celibrityInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","getPlanListAsynctask");

    }

    @Override
    protected Void doInBackground(CelibrityInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetCelibrityUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.celibrityInputModel.getAuthToken());
            httppost.addHeader("movie_id", this.celibrityInputModel.getMovie_id());
            httppost.addHeader("lang_code",this.celibrityInputModel.getLang_code());

            Log.v("SUBHA","lang_code = "+ this.celibrityInputModel.getLang_code());
            Log.v("SUBHA","authToken = "+ this.celibrityInputModel.getAuthToken());
            Log.v("SUBHA","movie id = "+ this.celibrityInputModel.getMovie_id());
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
                msg=myJson.optString("msg");

            }

                if (code == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("celibrity");

                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            CelibrityOutputModel content = new CelibrityOutputModel();
                            String celebrityName = jsonChildNode.optString("name");
                            String celebrityImage = jsonChildNode.optString("celebrity_image");
                            String celebrityCastType = jsonChildNode.optString("cast_type");

                            celebrityCastType = celebrityCastType.replaceAll("\\[", "");
                            celebrityCastType = celebrityCastType.replaceAll("\\]","");
                            celebrityCastType = celebrityCastType.replaceAll(","," , ");
                            celebrityCastType = celebrityCastType.replaceAll("\"", "");


                            if(celebrityImage.equals("") || celebrityImage==null)
                            {
                                celebrityImage = "";
                            }
                            else
                            {
                                if(!celebrityImage.contains("http"))
                                {
                                    celebrityImage ="";
                                }
                            }

                            content.setName(celebrityName);
                            content.setCast_type(celebrityCastType);
                            content.setCelebrity_image(celebrityImage);

                            celibrityOutputModel.add(content);
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
        listener.onGetCelibrityPreExecuteStarted();
        code= 0;
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
        listener.onGetCelibrityPostExecuteCompleted(celibrityOutputModel,code,msg);
    }
}
