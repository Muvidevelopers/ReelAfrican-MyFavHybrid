package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;


import com.home.apisdk.APIUrlConstant;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MUVI on 1/20/2017.
 */

public class GetIpAddressAsynTask extends AsyncTask<Void,Void ,Void > {

    String PACKAGE_NAME,ipAddressStr,responseStr;
    int statusCode;
    String message;
    public interface IpAddress{
        void onIPAddressPreExecuteStarted();
        void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr);
    }

    private IpAddress listener;
    private Context context;

    public GetIpAddressAsynTask(IpAddress listener,Context context) {
        this.listener = listener;
        this.context=context;

        PACKAGE_NAME=context.getPackageName();

    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            // Execute HTTP Post Request
            try {
                URL url = new URL(APIUrlConstant.getIpAddressUrl());
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                InputStream ins = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    responseStr = inputLine;
                }

                in.close();


            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                ipAddressStr = "";
                statusCode = 0;
                message = "Failure";
            } catch (UnsupportedEncodingException e) {

                ipAddressStr = "";
                statusCode = 0;
                message = "Failure";


            } catch (IOException e) {
                ipAddressStr = "";
                statusCode = 0;
                message = "Failure";

            }
            if (responseStr != null) {
                Object json = new JSONTokener(responseStr).nextValue();
                if (json instanceof JSONObject) {
                    statusCode = 200;
                    message = "Success";
                    ipAddressStr = ((JSONObject) json).getString("ip");

                }

            }

        } catch (Exception e) {
            ipAddressStr = "";
            statusCode = 0;
            message = "Failure";



        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onIPAddressPreExecuteStarted();
        statusCode= 0;
       /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onContactUsPostExecuteCompleted(contactUsOutputModel,code,message,status);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onContactUsPostExecuteCompleted(contactUsOutputModel,code,message,status);
        }*/
        //listener.onIPAddressPostExecuteCompleted(message,statusCode,ipAddressStr);

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onIPAddressPostExecuteCompleted(message, statusCode,ipAddressStr);
    }
}
