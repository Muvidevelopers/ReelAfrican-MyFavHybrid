package com.release.reelAfrican.physical;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.LoginActivity;
import com.release.reelAfrican.activity.MainActivity;
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
import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    Context context;
    ListView list;
    RelativeLayout nodata;
    Mycart_Adapter adapter;
    SharedPreferences pref;
    LinearLayout linearLayout1;
    int itemcount;
    ArrayList<ProductlistingModel> cart_item_list;
    Toolbar mActionBarToolbar;
    TextView total;
    TextView totalprice;
    Button buynow;
    LinearLayout linearLayout;
    String emailstr,user_id;
    AsynLoadsavecart asynLoadsavecart;
    private ProgressBarHandler videoPDialog;
    String cont;
    String currencysymbol="";
    private Dialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycart_layout);

        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            //Toast.makeText(getApplicationContext(),user_id,Toast.LENGTH_LONG).show();

        }else {
            emailstr = "";
            user_id= "";

        }

        if(getIntent().getStringExtra("main")!=null && getIntent().getStringExtra("main")!=""){

            cont=getIntent().getStringExtra("main");
        }else {

            cont="";
        }



        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("My Cart"+" ("+ BadgeCount.count+")");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });

        list = (ListView) findViewById(R.id.listView);

        linearLayout = (LinearLayout) findViewById(R.id.ll);
        linearLayout1 = (LinearLayout) findViewById(R.id.ll_sublayout);
        nodata = (RelativeLayout) findViewById(R.id.noData);
        total = (TextView) findViewById(R.id.total);
        totalprice = (TextView) findViewById(R.id.totalPRICE);
        buynow = (Button) findViewById(R.id.buynow);

        asynLoadsavecart = new AsynLoadsavecart();
        asynLoadsavecart.execute();



    }




    public void checkout(View view){


        if(emailstr!=null){



            Util.cartitem=convertJson(cart_item_list);

            Intent deliveryaddress = new Intent(MyCartActivity.this, DeliveryAddressActivity.class);
            deliveryaddress.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(deliveryaddress);


        }else {

            Intent Login = new Intent(MyCartActivity.this, LoginActivity.class);
            Login.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(Login);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(cont.equals("main")){

            Intent productlisting = new Intent(MyCartActivity.this, MainActivity.class);
            productlisting.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(productlisting);
            finish();


        }else {

            Intent productlisting = new Intent(MyCartActivity.this, ProductListingActivity.class);
            productlisting.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(productlisting);
            finish();

        }


    }

    public  void calculateprice(){

        double tott = 0;

        for(int i=0;i<cart_item_list.size();i++){

            ProductlistingModel productlistingModel =cart_item_list.get(i);
            tott= tott+ Double.parseDouble(productlistingModel.getPrice());
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            tott = Double.valueOf(decimalFormat.format(tott));

        }

        total.setText(currencysymbol+String.format("%.2f", tott));
        Util.totalprice= String.valueOf(String.format("%.2f", tott));

    }


    public  void Updatecart(){

        asynLoadsavecart = new AsynLoadsavecart();
        asynLoadsavecart.execute();

    }


    public  void visiblee(){


        if (cart_item_list.size() > 0) {

            adapter = new Mycart_Adapter(MyCartActivity.this, cart_item_list);
            list.setAdapter(adapter);
            Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
            linearLayout.setVisibility(View.VISIBLE);
            totalprice.setTypeface(font);
            buynow.setTypeface(font);

        } else {

            nodata.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }



    }


    public  void itemcont(){


        mActionBarToolbar.setTitle("My Cart"+" ("+ BadgeCount.count+")");

    }


    private class AsynLoadsavecart extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String idd="";
        String name="";
        String price="";
        String poster="";
        String currencyid="";
        int quanty;
        String sku="";
        String uniqid="";
        String permalink="";
        String custom_fields="";
        String is_free_offer="";
        String size="";
        String personalization_id="";
        String personalization_image="";
        String productstatus="";


        String cartid="";

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.getfromcart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("san",responseStr.toString());


                } catch (org.apache.http.conn.ConnectTimeoutException e){

                }catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    itemcount = myJson.getInt("items");

                }

                if (status > 0) {
                    if (status == 200) {

                        JSONObject cartitems = myJson.getJSONObject("cartitems");

                        Util.cartid=cartitems.getString("id");



                        JSONArray cart_item = cartitems.getJSONArray("cart_item");


                        int lengthJsonArr = cart_item.length();
                        for(int i=0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = cart_item.getJSONObject(i);



                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {

                                    name = jsonChildNode.getString("name");
                                }


                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {
                                    idd = jsonChildNode.getString("id");

                                }

                                if ((jsonChildNode.has("quantity")) && jsonChildNode.getString("quantity").trim() != null && !jsonChildNode.getString("quantity").trim().isEmpty() && !jsonChildNode.getString("quantity").trim().equals("null") && !jsonChildNode.getString("quantity").trim().matches("")) {

                                    quanty = jsonChildNode.getInt("quantity");

                                }

                                if ((jsonChildNode.has("price")) && jsonChildNode.getString("price").trim() != null && !jsonChildNode.getString("price").trim().isEmpty() && !jsonChildNode.getString("price").trim().equals("null") && !jsonChildNode.getString("price").trim().matches("")) {

                                    price = jsonChildNode.getString("price");

                                }

                                if ((jsonChildNode.has("currency_id")) && jsonChildNode.getString("currency_id").trim() != null && !jsonChildNode.getString("currency_id").trim().isEmpty() && !jsonChildNode.getString("currency_id").trim().equals("null") && !jsonChildNode.getString("currency_id").trim().matches("")) {

                                    currencyid = jsonChildNode.getString("currency_id");
                                    Util.currencyid=currencyid;

                                }

                                if ((jsonChildNode.has("sku")) && jsonChildNode.getString("sku").trim() != null && !jsonChildNode.getString("sku").trim().isEmpty() && !jsonChildNode.getString("sku").trim().equals("null") && !jsonChildNode.getString("sku").trim().matches("")) {

                                    sku = jsonChildNode.getString("sku");

                                }

                                if ((jsonChildNode.has("uniqid")) && jsonChildNode.getString("uniqid").trim() != null && !jsonChildNode.getString("uniqid").trim().isEmpty() && !jsonChildNode.getString("uniqid").trim().equals("null") && !jsonChildNode.getString("uniqid").trim().matches("")) {

                                    uniqid = jsonChildNode.getString("uniqid");

                                }

                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {

                                    permalink = jsonChildNode.getString("permalink");

                                }

                                if ((jsonChildNode.has("custom_fields")) && jsonChildNode.getString("custom_fields").trim() != null && !jsonChildNode.getString("custom_fields").trim().isEmpty() && !jsonChildNode.getString("custom_fields").trim().equals("null") && !jsonChildNode.getString("custom_fields").trim().matches("")) {

                                    custom_fields = jsonChildNode.getString("permalink");

                                }

                                if ((jsonChildNode.has("is_free_offer")) && jsonChildNode.getString("is_free_offer").trim() != null && !jsonChildNode.getString("is_free_offer").trim().isEmpty() && !jsonChildNode.getString("is_free_offer").trim().equals("null") && !jsonChildNode.getString("is_free_offer").trim().matches("")) {

                                    is_free_offer = jsonChildNode.getString("is_free_offer");

                                }

                                if ((jsonChildNode.has("size")) && jsonChildNode.getString("size").trim() != null && !jsonChildNode.getString("size").trim().isEmpty() && !jsonChildNode.getString("size").trim().equals("null") && !jsonChildNode.getString("size").trim().matches("")) {

                                    size = jsonChildNode.getString("size");

                                }

                                if ((jsonChildNode.has("personalization_id")) && jsonChildNode.getString("personalization_id").trim() != null && !jsonChildNode.getString("personalization_id").trim().isEmpty() && !jsonChildNode.getString("personalization_id").trim().equals("null") && !jsonChildNode.getString("personalization_id").trim().matches("")) {

                                    personalization_id = jsonChildNode.getString("personalization_id");

                                }

                                if ((jsonChildNode.has("personalization_image")) && jsonChildNode.getString("personalization_image").trim() != null && !jsonChildNode.getString("personalization_image").trim().isEmpty() && !jsonChildNode.getString("personalization_image").trim().equals("null") && !jsonChildNode.getString("personalization_image").trim().matches("")) {

                                    personalization_image = jsonChildNode.getString("personalization_image");

                                }

                                if ((jsonChildNode.has("status")) && jsonChildNode.getString("status").trim() != null && !jsonChildNode.getString("status").trim().isEmpty() && !jsonChildNode.getString("status").trim().equals("null") && !jsonChildNode.getString("status").trim().matches("")) {

                                    productstatus = jsonChildNode.getString("status");

                                }

                                if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.getString("currency_symbol").trim() != null && !jsonChildNode.getString("currency_symbol").trim().isEmpty() && !jsonChildNode.getString("currency_symbol").trim().equals("null") && !jsonChildNode.getString("currency_symbol").trim().matches("")) {

                                    currencysymbol = jsonChildNode.getString("currency_symbol");

                                }


                                if ((jsonChildNode.has("poster_original")) && jsonChildNode.getString("poster_original").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster_original").trim().equals("null") && !jsonChildNode.getString("poster_original").trim().matches("")) {
                                    poster = jsonChildNode.getString("poster_original");

                                }


                                ProductlistingModel productlistingModel=new ProductlistingModel();
                                productlistingModel.setProdidd(idd);
                                productlistingModel.setTitle(name);
                                productlistingModel.setPrice(price);
                                productlistingModel.setPoster(poster);
                                productlistingModel.setCurrencyid(currencyid);
                                productlistingModel.setItem_Count(quanty);
                                productlistingModel.setSku(sku);
                                productlistingModel.setUniqid(uniqid);
                                productlistingModel.setPermalink(permalink);
                                productlistingModel.setCustom_fields(custom_fields);
                                productlistingModel.setIs_free_offer(is_free_offer);
                                productlistingModel.setSize(size);
                                productlistingModel.setPersonalization_id(personalization_id);
                                productlistingModel.setPersonalization_image(personalization_image);
                                productlistingModel.setStatus(productstatus);
                                productlistingModel.setCurrencysymbol(currencysymbol);


                                cart_item_list.add(productlistingModel);



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


            progress_dialog.dismiss();

            if (cart_item_list.size() > 0) {


                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);

                adapter = new Mycart_Adapter(MyCartActivity.this, cart_item_list);
                list.setAdapter(adapter);
                calculateprice();



            } else {
                nodata.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }

        }

        @Override
        protected void onPreExecute() {
            cart_item_list=new ArrayList<>();;
            progress_dialog = Util.LoadingCircularDialog(MyCartActivity.this);
            progress_dialog.show();
        }

    }


    private String convertJson(ArrayList<ProductlistingModel> cart_item_list) {


        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < cart_item_list.size(); i++) {

            ProductlistingModel productlistingModel =cart_item_list.get(i);


            JSONObject pgJson = new JSONObject();

            try {

                pgJson.put("name", productlistingModel.getTitle());
                pgJson.put("id", productlistingModel.getProdidd());
                pgJson.put("quantity", productlistingModel.getItem_Count());
                pgJson.put("price", productlistingModel.getPrice());
                pgJson.put("currency_id", productlistingModel.getCurrencyid());
                pgJson.put("sku", productlistingModel.getSku());
                pgJson.put("uniqid", productlistingModel.getUniqid());
                pgJson.put("permalink", productlistingModel.getPermalink());
                pgJson.put("custom_fields", productlistingModel.getCustom_fields());
                pgJson.put("is_free_offer", productlistingModel.getIs_free_offer());
                pgJson.put("size", productlistingModel.getSize());
                pgJson.put("personalization_id", productlistingModel.getPersonalization_id());
                pgJson.put("personalization_image", productlistingModel.getPersonalization_image());



                jsonObject.put("pg_"+productlistingModel.getProdidd(), pgJson);

            } catch (Exception e) {

                e.printStackTrace();
            }



        }


        Log.v("json format:------",jsonObject.toString());
        return jsonObject.toString();
    }

}
