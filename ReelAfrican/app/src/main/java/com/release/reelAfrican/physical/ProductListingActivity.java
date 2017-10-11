package com.release.reelAfrican.physical;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.LoginActivity;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.activity.ProfileActivity;
import com.release.reelAfrican.activity.RegisterActivity;
import com.release.reelAfrican.activity.SplashScreen;
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
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;


public class ProductListingActivity extends AppCompatActivity {

    AsynLoadVideos asynLoadVideos;
    ArrayList<String> url_maps;
    private ProgressBarHandler videoPDialog;
    private ProductlistingAdapter customGridAdapter;
    SharedPreferences pref;
    GridItem itemclick;
    private RelativeLayout noInternetConnectionLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;
    int itemsInServer=0;
    int itemcount;
    public static HashMap<String, String> hashMap = new HashMap<String, String>();
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    public  static ArrayList<ProductlistingModel> cart_item_list;
    Context context;
    AsynLoadsavecart asynLoadsavecart;
    //Model for GridView
    ArrayList<GridItem> itemData= new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl,movieName,movigene,movieUniqueId,movieStreamUniqueId,videoUrlStr;
    int badge;
    // UI
    private GridView gridView;
    String emailstr,userid;
    RelativeLayout footerView;
    Toolbar mActionBarToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing);
        mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Product listing");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                //finish();
//                Intent intent = new Intent(ProductListingActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);

            }
        });



        context=ProductListingActivity.this;
        pref = context.getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
        posterUrl = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        gridView = (GridView) findViewById(R.id.imagesGridView);
        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);
        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(context, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(context, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        //Detect Network Connection

        boolean isNetwork = Util.checkNetwork(context);
        if (isNetwork == true) {


            asynLoadVideos = new AsynLoadVideos();
            asynLoadVideos.executeOnExecutor(threadPoolExecutor);

        }else {

            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GridItem item = itemData.get(position);
                itemclick = item;
                posterUrl = item.getImage();
                movieName = item.getTitle();
                movigene = item.getMovieGenre();
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();
                videoUrlStr = item.getVideoUrl();
                String price = String.valueOf(item.getPrice());
                String descriptions = item.getDescrip();
                movieUniqueId = item.getMovieUniqueId();
                movieStreamUniqueId = item.getMovieStreamUniqueId();

                Intent intent=new Intent(ProductListingActivity.this,ProductDetail_activity.class);
                intent.putExtra("permalink",moviePermalink);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main1, menu);



        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            userid= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            userid= "";

        }
        MenuItem item0,item,item1,item2,item3,item4,item5,item6;


        item0= menu.findItem(R.id.action_filter1);
        item0.setVisible(false);


        SharedPreferences language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        (menu.findItem(R.id.menu_item_language)).setTitle(Util.getTextofLanguage(ProductListingActivity.this, Util.LANGUAGE_POPUP_LANGUAGE, Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
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
                   item6.setTitle(Util.getTextofLanguage(ProductListingActivity.this, Util.MY_DOWNLOAD, Util.DEFAULT_MY_DOWNLOAD));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        int i = item.getItemId();
        if (i == R.id.action_notifications) {
            final Intent searchIntent = new Intent(ProductListingActivity.this, MyCartActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
            finish();
            // Not implemented here
            return false;
        }

        if (i == R.id.action_login) {

//           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductListingActivity.this, LoginActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_register) {

//           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductListingActivity.this, RegisterActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_purchasehistory) {

            // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductListingActivity.this, PurchaseHistory_Activity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }


        if (i == R.id.menu_item_profile) {

            // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            final Intent searchIntent = new Intent(ProductListingActivity.this, ProfileActivity.class);
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(searchIntent);
//            // Not implemented here
            return false;
        }

        if (i == R.id.action_logout) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ProductListingActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(Util.getTextofLanguage(ProductListingActivity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
            dlgAlert.setTitle("");

            dlgAlert.setPositiveButton(Util.getTextofLanguage(ProductListingActivity.this, Util.YES, Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog

                    // dialog.cancel();
                    AsynLogoutDetails asynLogoutDetails = new AsynLogoutDetails();
                    asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                    dialog.dismiss();
                }
            });

            dlgAlert.setNegativeButton(Util.getTextofLanguage(ProductListingActivity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

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


            String urlRouteList = Util.rootUrlmuvicart().trim()+Util.logoutUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("login_history_id",loginHistoryIdStr);
                httppost.addHeader("lang_code",Util.getTextofLanguage(ProductListingActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e){


                }catch (IOException e) {

                    e.printStackTrace();
                }
                if(responseStr!=null){
                    JSONObject myJson = new JSONObject(responseStr);
                    responseCode = Integer.parseInt(myJson.optString("code"));
                }

            }
            catch (Exception e) {

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
                Util.showToast(ProductListingActivity.this, Util.getTextofLanguage(ProductListingActivity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if(responseStr == null){
                Util.showToast(ProductListingActivity.this, Util.getTextofLanguage(ProductListingActivity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                // Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if (responseCode == 0) {
                Util.showToast(ProductListingActivity.this, Util.getTextofLanguage(ProductListingActivity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

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
                    if ((Util.getTextofLanguage(ProductListingActivity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                            .trim()).equals("1")) {
                        final Intent startIntent = new Intent(ProductListingActivity.this, SplashScreen.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(ProductListingActivity.this,Util.getTextofLanguage(ProductListingActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }
                    else
                    {
                        final Intent startIntent = new Intent(ProductListingActivity.this, MainActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(ProductListingActivity.this,Util.getTextofLanguage(ProductListingActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }

                }
                else {
                    Util.showToast(ProductListingActivity.this, Util.getTextofLanguage(ProductListingActivity.this,Util.SIGN_OUT_ERROR,Util.DEFAULT_SIGN_OUT_ERROR));

                    //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

                }
            }

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(ProductListingActivity.this);
            pDialog.show();
        }
    }



    public void optionmenu(){

        invalidateOptionsMenu();
    }


    @Override
    public void onBackPressed()
    {

        finish();
        Intent intent = new Intent(ProductListingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        super.onBackPressed();
    }


    //Load Films Videos
    private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieName = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String movieImageStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String moviePermalinkStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;
        String price;
        String des=" ";
        String currencyid=" ";
        String prodctid=" ";
        String Stockstatus=" ";
        String currencysymbol=" ";



        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.productlst.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                //httppost.addHeader("lang_code", Util.getTextofLanguage(ProductListingActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


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
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("itemlist");

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
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster_url");
                                    //movieImageStr = movieImageStr.replace("episode", "original");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));

                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));

                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));

                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }

                                if ((jsonChildNode.has("sale_price")) && jsonChildNode.getString("sale_price").trim() != null && !jsonChildNode.getString("sale_price").trim().isEmpty() && !jsonChildNode.getString("sale_price").trim().equals("null") && !jsonChildNode.getString("sale_price").trim().matches("")) {

                                    price = jsonChildNode.getString("sale_price");
                                }

                                if ((jsonChildNode.has("description")) && jsonChildNode.getString("description").trim() != null && !jsonChildNode.getString("description").trim().isEmpty() && !jsonChildNode.getString("description").trim().equals("null") && !jsonChildNode.getString("description").trim().matches("")) {
                                    des = jsonChildNode.getString("description");

                                }

                                if ((jsonChildNode.has("currency_id")) && jsonChildNode.getString("currency_id").trim() != null && !jsonChildNode.getString("currency_id").trim().isEmpty() && !jsonChildNode.getString("currency_id").trim().equals("null") && !jsonChildNode.getString("currency_id").trim().matches("")) {
                                    currencyid = jsonChildNode.getString("currency_id");

                                }

                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {
                                    prodctid = jsonChildNode.getString("id");

                                }

                                if ((jsonChildNode.has("status")) && jsonChildNode.getString("status").trim() != null && !jsonChildNode.getString("status").trim().isEmpty() && !jsonChildNode.getString("status").trim().equals("null") && !jsonChildNode.getString("status").trim().matches("")) {
                                    Stockstatus = jsonChildNode.getString("status");

                                }

                                if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.getString("currency_symbol").trim() != null && !jsonChildNode.getString("currency_symbol").trim().isEmpty() && !jsonChildNode.getString("currency_symbol").trim().equals("null") && !jsonChildNode.getString("currency_symbol").trim().matches("")) {
                                    currencysymbol = jsonChildNode.getString("currency_symbol");

                                }

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV,price,des,currencyid,prodctid,Stockstatus,currencysymbol));


                            } catch (Exception e) {

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

            asynLoadsavecart = new AsynLoadsavecart();
            asynLoadsavecart.executeOnExecutor(threadPoolExecutor);




        }

        @Override
        protected void onPreExecute() {

                videoPDialog = new ProgressBarHandler(ProductListingActivity.this);
                videoPDialog.show();

            }



    }


    @Override
    public void onResume() {

//        asynLoadsavecart = new AsynLoadsavecart();
//        asynLoadsavecart.executeOnExecutor(threadPoolExecutor);
        super.onResume();
    }


    private class AsynLoadsavecart extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String idd="";



        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.getfromcart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", userid.trim());

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


                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {

                                    idd = jsonChildNode.getString("id");

                                }


                                hashMap.put(idd,idd);



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

            float density = context.getResources().getDisplayMetrics().density;

            if (videoPDialog != null && videoPDialog.isShowing()) {

                videoPDialog.hide();
                videoPDialog = null;
            }

            badge= itemcount;
            BadgeCount.count=badge;
            invalidateOptionsMenu();



                if(itemData.size()>0){

                    gridView.setVisibility(View.VISIBLE);
                    if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                            gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);


                    } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {

                            gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                    } else if ( (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);


                    }

                        if (density >= 3.5 && density <= 4.0) {

                            customGridAdapter = new ProductlistingAdapter(context, R.layout.productlisting_row_nexus, itemData);
                            
                        }else {

                            customGridAdapter = new ProductlistingAdapter(context, R.layout.productlisting_row, itemData);

                        }


                    gridView.setAdapter(customGridAdapter);

                }else {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);

                }



        }

        @Override
        protected void onPreExecute() {
            hashMap.clear();

            videoPDialog = new ProgressBarHandler(context);
            videoPDialog.show();

        }

    }
}
