package com.release.reelAfrican.physical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


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

public class ShippingActivity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    TextView shipaddress,shippingcost,carttotal,totalamt;
    String NAME,ADDRESS,PHONE,CITY,COUNTRY,ZIP;
    Spinner shippingspinner;
    private ProgressBarHandler videoPDialog;
    SharedPreferences pref;
    String emailstr,user_id;
    String spinneritemselect;
    String shipcost,currencysymbol,minimum_order_free_shipping;
    LinearLayout shiplayout,cartpricelayout,shippingcostlayout;
    TextView CARTPRICETAG,SHIPTAG,TOTALTAG;
    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Shipping Address");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

//                int price= (int) (Double.parseDouble(Util.grandtotal)-Double.parseDouble(shipcost));
//                Util.grandtotal=""+price;

            }
        });


        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);


        }else {
            emailstr = "";
            user_id= "";
        }



        if (getIntent().getStringExtra("NAME")!=null){

            NAME=getIntent().getStringExtra("NAME");
        }else {

            NAME="";
        }

        if (getIntent().getStringExtra("ADDRESS")!=null){

            ADDRESS=getIntent().getStringExtra("ADDRESS");

        }else {

            ADDRESS="";
        }

        if (getIntent().getStringExtra("PHONE")!=null){

            PHONE=getIntent().getStringExtra("PHONE");

        }else {

            PHONE="";
        }

        if (getIntent().getStringExtra("CITY")!=null){

            CITY=getIntent().getStringExtra("CITY");

        }else {

            CITY="";
        }

        if (getIntent().getStringExtra("COUNTRY")!=null){

            COUNTRY=getIntent().getStringExtra("COUNTRY");

        }else {

            COUNTRY="";
        }

        Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
        ZIP=getIntent().getStringExtra("ZIP");

        cartpricelayout= (LinearLayout) findViewById(R.id.cartprice);
        shippingcostlayout= (LinearLayout) findViewById(R.id.shippingcost);
        shiplayout= (LinearLayout) findViewById(R.id.shiplayout);

        CARTPRICETAG= (TextView) findViewById(R.id.cartitempricetag);
        SHIPTAG= (TextView) findViewById(R.id.shippingcosttag);
        TOTALTAG= (TextView) findViewById(R.id.totaltag);
        checkout= (Button) findViewById(R.id.checkout);

        shippingcost= (TextView) findViewById(R.id.shiptotal);
        totalamt=(TextView) findViewById(R.id.total);
        carttotal=(TextView) findViewById(R.id.carttotal);
        shipaddress= (TextView) findViewById(R.id.shipaddress);
        shippingspinner = (Spinner) findViewById(R.id.shippingspinner);
        shipaddress.setText(NAME+"\n"+ADDRESS+"\n"+PHONE+"\n"+CITY+"\n"+COUNTRY);
        shipaddress.setTypeface(font);

        AsynLoadshippingmethod asynLoadshippingmethod = new AsynLoadshippingmethod();
        asynLoadshippingmethod.execute();

        shippingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spinneritemselect=String.valueOf(shippingspinner.getSelectedItem().toString());

//                Toast.makeText(ShippingActivity.this,
//                        spinneritemselect,
//                           Toast.LENGTH_SHORT).show();


                AsynLoadshippingmethodCharges asynLoadshippingmethodCharges = new AsynLoadshippingmethodCharges();
                asynLoadshippingmethodCharges.execute();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub



            }
        });





    }


    //Load shipping method
    private class AsynLoadshippingmethod extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String methods;

        ArrayList<String> shippingmethod = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrlmuvicart().trim() + Util.shippingmethod.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("methods");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;


                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("method")) && jsonChildNode.getString("method").trim() != null && !jsonChildNode.getString("method").trim().isEmpty() && !jsonChildNode.getString("method").trim().equals("null") && !jsonChildNode.getString("method").trim().matches("") && !jsonChildNode.getString("method").trim().equalsIgnoreCase("")) {

                                    methods = jsonChildNode.getString("method");

                                }

                                shippingmethod.add(methods);

                            } catch (Exception e) {

                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {
            try {
                if (videoPDialog.isShowing())
                    videoPDialog.hide();
            } catch (IllegalArgumentException ex) {

                shippingspinner.setVisibility(View.GONE);

            }
            shippingspinner.setVisibility(View.VISIBLE);
            //Creating the ArrayAdapter instance having the bank name list
            ArrayAdapter shipmethodspinner = new ArrayAdapter(ShippingActivity.this,android.R.layout.simple_dropdown_item_1line,shippingmethod);
            //shippingspinner.setPrompt("Choose Shipping Method");
            shipmethodspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            shippingspinner.setAdapter(shipmethodspinner);


        }

        @Override
        protected void onPreExecute() {
           /* videoPDialog = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            videoPDialog.setCancelable(false);
            videoPDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            videoPDialog.setIndeterminate(false);
            videoPDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            videoPDialog.show();*/

            videoPDialog = new ProgressBarHandler(ShippingActivity.this);
            videoPDialog.show();


        }


    }


    //Load shipping method
    private class AsynLoadshippingmethodCharges extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;




        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrlmuvicart().trim() + Util.shippingmethod.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("shipping_method", spinneritemselect);
                httppost.addHeader("country", COUNTRY.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {


                        try {

                            JSONObject jsonMainNode = myJson.getJSONObject("shipping_details");
                            shipcost=jsonMainNode.getString("shipcost");
                            currencysymbol=jsonMainNode.getString("ship_symbol");

                            if ((jsonMainNode.has("minimum_order_free_shipping")) && jsonMainNode.getString("minimum_order_free_shipping").trim() != null && !jsonMainNode.getString("minimum_order_free_shipping").trim().isEmpty() && !jsonMainNode.getString("minimum_order_free_shipping").trim().equals("null") && !jsonMainNode.getString("minimum_order_free_shipping").trim().matches("")) {

                                minimum_order_free_shipping = jsonMainNode.getString("minimum_order_free_shipping");

                            }else {

                                minimum_order_free_shipping="";

                            }

                           // minimum_order_free_shipping=jsonMainNode.getString("minimum_order_free_shipping");

                        }catch (Exception e){


                        }

                        }

                    } else {



                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

                if (videoPDialog.isShowing())
                    videoPDialog.hide();

            Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));

            cartpricelayout.setVisibility(View.VISIBLE);
            shippingcostlayout.setVisibility(View.VISIBLE);
            shiplayout.setVisibility(View.VISIBLE);
            CARTPRICETAG.setTypeface(font);
            SHIPTAG.setTypeface(font);
            TOTALTAG.setTypeface(font);
            checkout.setTypeface(font);


            if(minimum_order_free_shipping!=null && minimum_order_free_shipping!=""){


                     int minimumshipping= (int) (Double.parseDouble(minimum_order_free_shipping));
                     double tot=(Double.parseDouble(Util.totalprice));

                if(tot>=minimumshipping){


        shippingcost.setText(currencysymbol+"0.00");
        shippingcost.setTypeface(font);
        carttotal.setText(currencysymbol+ Util.totalprice);
        carttotal.setTypeface(font);
        Util.grandtotal=""+Util.totalprice;
        totalamt.setText(currencysymbol+Util.grandtotal);


    }else {

        shippingcost.setText(currencysymbol+"."+shipcost);
        shippingcost.setTypeface(font);
        carttotal.setText(currencysymbol+"."+Util.totalprice);
        carttotal.setTypeface(font);
        double price= Double.parseDouble(Util.totalprice)+Double.parseDouble(shipcost);
        Util.grandtotal=String.format("%.2f", price);
        totalamt.setText(currencysymbol+"."+Util.grandtotal);
          }

}else {

            shippingcost.setText(currencysymbol+shipcost);
            shippingcost.setTypeface(font);
            carttotal.setText(currencysymbol+Util.totalprice);
            carttotal.setTypeface(font);
            double price= (Double.parseDouble(Util.totalprice)+Double.parseDouble(shipcost));
            Util.grandtotal=String.format("%.2f", price);
            totalamt.setText(currencysymbol+Util.grandtotal);
}




        }

        @Override
        protected void onPreExecute() {


            videoPDialog = new ProgressBarHandler(ShippingActivity.this);
            videoPDialog.show();


        }


    }

    public  void checkout(View view){

        Intent payment = new Intent(ShippingActivity.this, Payment.class);
        payment.putExtra("shippingcost",shipcost);
        payment.putExtra("csymbol",currencysymbol);
        startActivity(payment);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        int price= (int) (Double.parseDouble(Util.grandtotal)-Double.parseDouble(shipcost));
//        Util.grandtotal=""+price;
    }
}
