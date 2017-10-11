package com.release.reelAfrican.physical;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.LoginActivity;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.activity.ProfileActivity;
import com.release.reelAfrican.activity.RegisterActivity;
import com.release.reelAfrican.activity.SplashScreen;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.release.reelAfrican.physical.ProductListingActivity.hashMap;

public class ProductDetail_activity extends AppCompatActivity {

    ImageView image;
    TextView mtitle,price_test,des,nodata,des1,pricesymbol;
    String permalink,FROM,SRCH;
    int badge;
    Button addtcart,ordernow;
    Toolbar mActionBarToolbar;
    AsynLoadProductDetails asynLoadProductDetails;
    private ProgressBarHandler videoPDialog;
    String Currencyid = "";
    String productid = "";
    String stockstatus = "";
    String movieGenreStr = "";
    String movieName = "";
    String productbanner = "";
    String moviePermalinkStr = "";
    String videoTypeIdStr = "";
    String currencysymbol = "";
    double product_price=0;
    String product_des=" ";
    SharedPreferences pref;
    String emailstr,user_id;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    LinearLayout ll1,ll2,ll3;
    int keepAliveTime = 10;
    int itemcount;
    AsynLoadsavecart asynLoadsavecart;
    Asynaddtocart asynaddtocart;
    Asynodernow asynodernow;
    Button stockout;
    private Dialog progress_dialog;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        if (getIntent().getStringExtra("permalink")!=null && getIntent().getStringExtra("permalink")!=""){

            permalink=getIntent().getStringExtra("permalink");

        }else {

            permalink="";

        }


        if (getIntent().getStringExtra("from")!=null && getIntent().getStringExtra("from")!=""){

            FROM=getIntent().getStringExtra("from");

        }else {

            FROM="";
        }


        if (getIntent().getStringExtra("search")!=null && getIntent().getStringExtra("search")!=""){

            SRCH=getIntent().getStringExtra("search");

        }else {

            SRCH="";
        }


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Product Details");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(FROM!=null && FROM!=""){

                    finish();

                }else if(SRCH!=null && SRCH!=""){


                    finish();

                }
                else {

                    onBackPressed();

                }



            }
        });

        progress_dialog = Util.LoadingCircularDialog(ProductDetail_activity.this);






        ll2= (LinearLayout) findViewById(R.id.addtocart_ll);
        ll3= (LinearLayout) findViewById(R.id.total_ll1);
        image= (ImageView) findViewById(R.id.imageView);
        mtitle= (TextView) findViewById(R.id.movietitle);
        pricesymbol= (TextView) findViewById(R.id.PRICEsymbol);
        price_test= (TextView) findViewById(R.id.PRICE);
        nodata= (TextView) findViewById(R.id.no_data);
        des= (TextView) findViewById(R.id.DES);
        des1= (TextView) findViewById(R.id.DES1);
        addtcart= (Button) findViewById(R.id.addtocart);
        ordernow= (Button) findViewById(R.id.ordernow);



        stockout= (Button) findViewById(R.id.stockout);
        asynLoadProductDetails = new AsynLoadProductDetails();
        asynLoadProductDetails.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(FROM!=null && FROM!=""){

            finish();

        }else if(SRCH!=null && SRCH!=""){


            finish();

        }


        else {

            finish();
            Intent productlisting = new Intent(ProductDetail_activity.this, ProductListingActivity.class);
            productlisting.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(productlisting);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main1, menu);


        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {

            emailstr = "";
            user_id= "";

        }
        MenuItem item0,item,item1,item2,item3,item4,item5,item6;


        item0= menu.findItem(R.id.action_filter1);
        item0.setVisible(false);


        SharedPreferences language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        (menu.findItem(R.id.menu_item_language)).setTitle(Util.getTextofLanguage(ProductDetail_activity.this, Util.LANGUAGE_POPUP_LANGUAGE, Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
        if(language_list_pref.getString("total_language","0").equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);



        if(emailstr!=null){


            item1= menu.findItem(R.id.action_login);
            item1.setVisible(false);

            item2= menu.findItem(R.id.action_register);
            item2.setVisible(false);

            item4= menu.findItem(R.id.menu_item_profile);
            item4.setVisible(true);


            item3= menu.findItem(R.id.action_purchasehistory);
            item3.setVisible(true);

            item5= menu.findItem(R.id.action_logout);
            item5.setVisible(true);

            item6 = menu.findItem(R.id.action_mydownload);
            item6.setTitle(Util.getTextofLanguage(ProductDetail_activity.this, Util.MY_DOWNLOAD, Util.DEFAULT_MY_DOWNLOAD));
            item6.setVisible(true);


        }else {

            item1= menu.findItem(R.id.action_login);
            item1.setVisible(true);

            item2= menu.findItem(R.id.action_register);
            item2.setVisible(true);

            item3= menu.findItem(R.id.action_purchasehistory);
            item3.setVisible(false);

            item4= menu.findItem(R.id.menu_item_profile);
            item4.setVisible(false);

            item5= menu.findItem(R.id.action_logout);
            item5.setVisible(false);

        }



        item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        BadgeCount.setBadgeCount(this, icon, BadgeCount.count);

        return true;
    }




    private class AsynLoadProductDetails extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.productdeatil.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("permalink", permalink);


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("sanjay:-------",responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e){


                }catch (IOException e) {

                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");

                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("details");

                        int lengthJsonArr = jsonMainNode.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {

                                    movieGenreStr = jsonChildNode.getString("genre");

                                }
                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {

                                    movieName = jsonChildNode.getString("name");

                                }
                                if ((jsonChildNode.has("poster_original")) && jsonChildNode.getString("poster_original").trim() != null && !jsonChildNode.getString("poster_original").trim().isEmpty() && !jsonChildNode.getString("poster_original").trim().equals("null") && !jsonChildNode.getString("poster_original").trim().matches("")) {

                                    productbanner = jsonChildNode.getString("poster_original");
                                    //movieImageStr = movieImageStr.replace("episode", "original");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {

                                    moviePermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {

                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }

                                if ((jsonChildNode.has("sale_price")) && jsonChildNode.getString("sale_price").trim() != null && !jsonChildNode.getString("sale_price").trim().isEmpty() && !jsonChildNode.getString("sale_price").trim().equals("null") && !jsonChildNode.getString("sale_price").trim().matches("")) {

                                    product_price = Double.parseDouble(jsonChildNode.getString("sale_price"));

                                }

                                if ((jsonChildNode.has("description")) && jsonChildNode.getString("description").trim() != null && !jsonChildNode.getString("description").trim().isEmpty() && !jsonChildNode.getString("description").trim().equals("null") && !jsonChildNode.getString("description").trim().matches("")) {

                                    product_des = jsonChildNode.getString("description");

                                }


                                if ((jsonChildNode.has("currency_id")) && jsonChildNode.getString("currency_id").trim() != null && !jsonChildNode.getString("currency_id").trim().isEmpty() && !jsonChildNode.getString("currency_id").trim().equals("null") && !jsonChildNode.getString("currency_id").trim().matches("")) {

                                    Currencyid = jsonChildNode.getString("currency_id");

                                }

                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {

                                    productid = jsonChildNode.getString("id");

                                }

                                if ((jsonChildNode.has("status")) && jsonChildNode.getString("status").trim() != null && !jsonChildNode.getString("status").trim().isEmpty() && !jsonChildNode.getString("status").trim().equals("null") && !jsonChildNode.getString("status").trim().matches("")) {

                                    stockstatus = jsonChildNode.getString("status");

                                }

                                if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.getString("currency_symbol").trim() != null && !jsonChildNode.getString("currency_symbol").trim().isEmpty() && !jsonChildNode.getString("currency_symbol").trim().equals("null") && !jsonChildNode.getString("currency_symbol").trim().matches("")) {

                                    currencysymbol = jsonChildNode.getString("currency_symbol");

                                }



                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        responseStr = "0";

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

            if(responseStr!=null){



                Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
                asynLoadsavecart = new AsynLoadsavecart();
                asynLoadsavecart.executeOnExecutor(threadPoolExecutor);



                if (stockstatus.equals("3")) {

                    stockout.setVisibility(View.VISIBLE);
                    stockout.setTypeface(font);
                }

                else {


                    ll2.setVisibility(View.VISIBLE);

                }





                ll3.setVisibility(View.VISIBLE);
                mtitle.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                des1.setVisibility(View.VISIBLE);
                des.setVisibility(View.VISIBLE);
                price_test.setVisibility(View.VISIBLE);
                pricesymbol.setVisibility(View.VISIBLE);
                mtitle.setText(movieName);
                mtitle.setTypeface(font);
                pricesymbol.setText(currencysymbol);
                price_test.setText(""+product_price);
                price_test.setTypeface(font);
                des.setText(product_des);
                des.setTypeface(font);
                addtcart.setTypeface(font);
                ordernow.setTypeface(font);



                if(hashMap.size()>0) {

                    if (productid.equalsIgnoreCase(hashMap.get(productid))) {

                        addtcart.setText("Go to Cart");
                        //addtcart.setBackgroundDrawable(getResources().getDrawable(R.drawable.gotocart));
                        addtcart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_36dp, 0);


                    }
                }







                if(productbanner.length()!=0){

                    Picasso.with(ProductDetail_activity.this)
                            .load(productbanner)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .fit()
                            .into(image);

                }else {

                    Picasso.with(ProductDetail_activity.this)
                            .load("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png")
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .fit()
                            .into(image);
                }


            }else {

                nodata.setVisibility(View.VISIBLE);

            }



        }

        @Override
        protected void onPreExecute() {

           progress_dialog.show();
        }



    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_notifications) {
            final Intent searchIntent = new Intent(ProductDetail_activity.this, MyCartActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
            // Not implemented here
            return false;
        }
        if (i == R.id.action_login) {

//           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductDetail_activity.this, LoginActivity.class);
            searchIntent.putExtra("pid",productid);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_register) {

//           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductDetail_activity.this, RegisterActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_purchasehistory) {

            // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductDetail_activity.this, PurchaseHistory_Activity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.menu_item_profile) {

            // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductDetail_activity.this, ProfileActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_logout) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProductDetail_activity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(Util.getTextofLanguage(ProductDetail_activity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
            dlgAlert.setTitle("");

            dlgAlert.setPositiveButton(Util.getTextofLanguage(ProductDetail_activity.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    // dialog.cancel();
                    AsynLogoutDetails asynLogoutDetails = new AsynLogoutDetails();
                    asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                    dialog.dismiss();
                }
            });

            dlgAlert.setNegativeButton(Util.getTextofLanguage(ProductDetail_activity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
            dlgAlert.setCancelable(false);

            dlgAlert.create().show();

        }

        return false;
    }




    private class AsynLogoutDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        int responseCode;
        String loginHistoryIdStr = pref.getString("PREFS_LOGIN_HISTORYID_KEY", null);
        String responseStr;

        @Override
        protected Void doInBackground(Void... params) {


            String urlRouteList =Util.rootUrlmuvicart().trim()+Util.logoutUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("login_history_id",loginHistoryIdStr);
                httppost.addHeader("lang_code",Util.getTextofLanguage(ProductDetail_activity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.hide();
                                pDialog = null;
                            }
                            responseCode = 0;
                            Util.showToast(ProductDetail_activity.this, Util.getTextofLanguage(ProductDetail_activity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            // Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION),Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    responseCode = 0;
                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    responseCode = Integer.parseInt(myJson.optString("code"));
                }

            }
            catch (Exception e) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseCode = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;

                }
            } catch (IllegalArgumentException ex) {
                Util.showToast(ProductDetail_activity.this, Util.getTextofLanguage(ProductDetail_activity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if(responseStr == null){
                Util.showToast(ProductDetail_activity.this, Util.getTextofLanguage(ProductDetail_activity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                // Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if (responseCode == 0) {
                Util.showToast(ProductDetail_activity.this, Util.getTextofLanguage(ProductDetail_activity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                // Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if (responseCode > 0) {
                if (responseCode == 200) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
                    if (loginPref!=null) {
                        SharedPreferences.Editor countryEditor = loginPref.edit();
                        countryEditor.clear();
                        countryEditor.commit();
                    }
                 /*   SharedPreferences countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                    if (countryPref!=null) {
                        SharedPreferences.Editor countryEditor = countryPref.edit();
                        countryEditor.clear();
                        countryEditor.commit();
                    }*/
                    if ((Util.getTextofLanguage(ProductDetail_activity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                            .trim()).equals("1")) {
                        final Intent startIntent = new Intent(ProductDetail_activity.this, SplashScreen.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(ProductDetail_activity.this,Util.getTextofLanguage(ProductDetail_activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }
                    else
                    {
                        final Intent startIntent = new Intent(ProductDetail_activity.this, MainActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(ProductDetail_activity.this,Util.getTextofLanguage(ProductDetail_activity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }

                }
                else {
                    Util.showToast(ProductDetail_activity.this, Util.getTextofLanguage(ProductDetail_activity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                    //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

                }
            }

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(ProductDetail_activity.this);
            pDialog.show();
        }
    }



    @Override
    protected void onResume() {


        invalidateOptionsMenu();
        super.onResume();


    }



    public void stockout(View view){

//             Toast.makeText(getApplicationContext(),"Out of Stocks",Toast.LENGTH_LONG).show();

    }


    public void cart(View view){


        if (user_id!=null) {


            if (hashMap.size() > 0) {

                if (productid.equalsIgnoreCase(hashMap.get(productid))) {

                    Intent intent = new Intent(ProductDetail_activity.this, MyCartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } else {


                    asynaddtocart = new Asynaddtocart();
                    asynaddtocart.execute();
                }
            } else {

                asynaddtocart = new Asynaddtocart();
                asynaddtocart.execute();

            }
        }else {


            final Intent intent = new Intent(ProductDetail_activity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }



    }

    public void ordernow(View view){


        if (user_id!=null) {


            if (hashMap.size() > 0) {

                if (productid.equalsIgnoreCase(hashMap.get(productid))) {

                    Intent intent = new Intent(ProductDetail_activity.this, MyCartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } else {

                    asynodernow = new Asynodernow();
                    asynodernow.execute();

                }
            } else {

                asynodernow = new Asynodernow();
                asynodernow.execute();

            }

        }else {


            Intent intent = new Intent(ProductDetail_activity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

    }




    private class Asynaddtocart extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", productid);
                httppost.addHeader("quantity", "1");

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

                }

            }
            catch (Exception e)
            {


                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {


//            if (videoPDialog != null && videoPDialog.isShowing()) {
//                videoPDialog.hide();
//                videoPDialog = null;
//            }
            progress_dialog.dismiss();

            if (status == 200) {


                if(stockstatus.equals("3")){


                    Toast.makeText(getApplicationContext(),"Out of Stock", Toast.LENGTH_LONG).show();
                    finish();

                }else {

                    BadgeCount.count = BadgeCount.count + 1;
                    invalidateOptionsMenu();
                    hashMap.put(productid,productid);
                    addtcart.setText("Go to Cart");
                    //addtcart.setBackgroundDrawable(getResources().getDrawable(R.drawable.gotocart));
                    addtcart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_36dp, 0);

                }


            } else {

                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {

//            videoPDialog = new ProgressBarHandler(ProductDetail_activity.this);
//            videoPDialog.show();

            progress_dialog.show();

        }

    }


    private class Asynodernow extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", productid);
                httppost.addHeader("quantity", "1");

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


            if (status == 200) {



                    BadgeCount.count = BadgeCount.count + 1;

                    Intent mycart = new Intent(ProductDetail_activity.this, MyCartActivity.class);
                    mycart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mycart);
                    //finish();



            } else {

                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

            }

        }

        @Override
        protected void onPreExecute() {

            progress_dialog.show();

        }

    }


    private class AsynLoadsavecart extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;



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
                    Log.v("response",responseStr.toString());


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

            }
            catch (Exception e)
            {


                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            progress_dialog.dismiss();


                badge= itemcount;
                BadgeCount.count=badge;
                invalidateOptionsMenu();



        }

        @Override
        protected void onPreExecute() {

            progress_dialog.show();
        }

    }


}
