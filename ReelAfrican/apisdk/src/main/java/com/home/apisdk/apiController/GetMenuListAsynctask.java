package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.MenuListInput;
import com.home.apisdk.apiModel.MenuListOutput;

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

public class GetMenuListAsynctask extends AsyncTask<MenuListInput,Void ,Void > {

    public MenuListInput menuListInput;
    String PACKAGE_NAME,message,responseStr;
    int code;

    public interface GetMenuList{
        void onGetMenuListPreExecuteStarted();
        void onGetMenuListPostExecuteCompleted(ArrayList<MenuListOutput> menuListOutput, int status, String message);
    }

    private GetMenuList listener;
    private Context context;
    ArrayList<MenuListOutput> menuListOutput = new ArrayList<MenuListOutput>();

    public GetMenuListAsynctask(MenuListInput menuListInput,GetMenuList listener, Context context) {
        this.listener = listener;
        this.context=context;


        this.menuListInput = menuListInput;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","GetMenuListAsynctask");

    }

    @Override
    protected Void doInBackground(MenuListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getMenuListUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.menuListInput.getAuthToken());

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
                message = myJson.optString("msg");
            }

                if (code == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("menu");

                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            MenuListOutput content = new MenuListOutput();

                            if ((jsonChildNode.has("link_type")) && jsonChildNode.getString("link_type").trim() != null && !jsonChildNode.getString("link_type").trim().isEmpty() && !jsonChildNode.getString("link_type").trim().equals("null") && !jsonChildNode.getString("link_type").trim().matches("")) {
                                content.setLink_type(jsonChildNode.getString("link_type"));

                            }
                            if ((jsonChildNode.has("display_name")) && jsonChildNode.getString("display_name").trim() != null && !jsonChildNode.getString("display_name").trim().isEmpty() && !jsonChildNode.getString("display_name").trim().equals("null") && !jsonChildNode.getString("display_name").trim().matches("")) {
                                content.setDisplay_name(jsonChildNode.getString("display_name"));
                            }
                            if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("null").trim().matches("")) {
                                content.setPermalink(jsonChildNode.getString("permalink"));
                            }
                            menuListOutput.add(content);
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
        listener.onGetMenuListPreExecuteStarted();
        code= 0;
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetMenuListPostExecuteCompleted(menuListOutput,code,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetMenuListPostExecuteCompleted(menuListOutput,code,message);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGetMenuListPostExecuteCompleted(menuListOutput,code,message);
    }
}
