package com.release.reelAfrican.physical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.Util;

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

public class DeliveryAddressActivity extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    ArrayList<DeliveryAddressModel> deliveryAddressModels=new ArrayList<DeliveryAddressModel>();
    ListView list;
    TextView nodatas;
    DeliveryAddressAdapter adapter;
    AsynLoadSavedaddress asynLoadSavedaddress;
    private ProgressBarHandler videoPDialog;
    SharedPreferences pref;
    String emailstr,user_id;
    Button addaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            user_id= "";

        }
        Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
        mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Delivery");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();

                finish();

            }
        });
        list = (ListView) findViewById(R.id.listView);
        nodatas = (TextView) findViewById(R.id.nodatas);
        addaddress = (Button) findViewById(R.id.add);
        addaddress.setTypeface(font);
        deliveryAddressModels.clear();



        asynLoadSavedaddress = new AsynLoadSavedaddress();
        asynLoadSavedaddress.execute();



    }



    public void addnewaddress(View v){


        Intent addnewaddress = new Intent(DeliveryAddressActivity.this, AddNewAddressActivity.class);
        addnewaddress.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(addnewaddress);
        finish();
    }

    public  void visible(){


        if (deliveryAddressModels.size() > 0) {

            adapter = new DeliveryAddressAdapter(DeliveryAddressActivity.this, android.R.layout.simple_dropdown_item_1line, deliveryAddressModels);
            list.setAdapter(adapter);

        } else {

            nodatas.setVisibility(View.VISIBLE);


        }



    }


    private class AsynLoadSavedaddress extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String idd="";
        String name="";
        String mail="";
        String address="";
        String city="";
        String country="";
        String zip="";
        String ph="";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.getsavedaddress.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e){

                }catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    //String items = myJson.optString("item_count");

                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("alladdress");

                        int lengthJsonArr = jsonMainNode.length();
                        for(int i=0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);


                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {
                                    idd = jsonChildNode.getString("id");

                                }

                                if ((jsonChildNode.has("first_name")) && jsonChildNode.getString("first_name").trim() != null && !jsonChildNode.getString("first_name").trim().isEmpty() && !jsonChildNode.getString("first_name").trim().equals("null") && !jsonChildNode.getString("first_name").trim().matches("")) {
                                    name = jsonChildNode.getString("first_name");

                                }

                                if ((jsonChildNode.has("email")) && jsonChildNode.getString("email").trim() != null && !jsonChildNode.getString("email").trim().isEmpty() && !jsonChildNode.getString("email").trim().equals("null") && !jsonChildNode.getString("email").trim().matches("")) {
                                    mail = jsonChildNode.getString("email");

                                }


                                if ((jsonChildNode.has("address")) && jsonChildNode.getString("address").trim() != null && !jsonChildNode.getString("address").trim().isEmpty() && !jsonChildNode.getString("address").trim().equals("null") && !jsonChildNode.getString("address").trim().matches("")) {
                                    address = jsonChildNode.getString("address");

                                }

                                if ((jsonChildNode.has("city")) && jsonChildNode.getString("city").trim() != null && !jsonChildNode.getString("city").trim().isEmpty() && !jsonChildNode.getString("city").trim().equals("null") && !jsonChildNode.getString("city").trim().matches("")) {
                                    city = jsonChildNode.getString("city");

                                }

                                if ((jsonChildNode.has("country")) && jsonChildNode.getString("country").trim() != null && !jsonChildNode.getString("country").trim().isEmpty() && !jsonChildNode.getString("country").trim().equals("null") && !jsonChildNode.getString("country").trim().matches("")) {
                                    country = jsonChildNode.getString("country");

                                }

                                if ((jsonChildNode.has("zip")) && jsonChildNode.getString("zip").trim() != null && !jsonChildNode.getString("zip").trim().isEmpty() && !jsonChildNode.getString("zip").trim().equals("null") && !jsonChildNode.getString("zip").trim().matches("")) {
                                    zip = jsonChildNode.getString("zip");

                                }

                                if ((jsonChildNode.has("phone_number")) && jsonChildNode.getString("phone_number").trim() != null && !jsonChildNode.getString("phone_number").trim().isEmpty() && !jsonChildNode.getString("phone_number").trim().equals("null") && !jsonChildNode.getString("phone_number").trim().matches("")) {
                                    ph = jsonChildNode.getString("phone_number");
                                    //movieImageStr = movieImageStr.replace("episode", "original");

                                }


                               DeliveryAddressModel addressModel=new DeliveryAddressModel();
                                addressModel.setID(idd);
                                addressModel.setName(name);
                                addressModel.setMail(mail);
                                addressModel.setAddress(address);
                                addressModel.setCity(city);
                                addressModel.setCountry(country);
                                addressModel.setZip(zip);
                                addressModel.setPhno(ph);
                                deliveryAddressModels.add(addressModel);


                            } catch (Exception e) {

                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    else{

                    }
                }
            }
            catch (Exception e)
            {


                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            if (videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
                videoPDialog = null;
            }


            if (deliveryAddressModels.size() > 0) {
//
//
            nodatas.setVisibility(View.GONE);
            adapter = new DeliveryAddressAdapter(DeliveryAddressActivity.this, android.R.layout.simple_dropdown_item_1line, deliveryAddressModels);
            list.setAdapter(adapter);
        } else {

            nodatas.setVisibility(View.VISIBLE);
        }
//
        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(DeliveryAddressActivity.this);
            videoPDialog.show();

        }


    }

}
