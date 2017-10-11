package com.release.reelAfrican.actvity_audio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.FavoriteActivity;
import com.release.reelAfrican.activity.LoginActivity;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.activity.MyDownloads;
import com.release.reelAfrican.activity.ProfileActivity;
import com.release.reelAfrican.activity.RegisterActivity;
import com.release.reelAfrican.activity.SearchActivity;
import com.release.reelAfrican.activity.SplashScreen;
import com.release.reelAfrican.physical.BadgeCount;
import com.release.reelAfrican.physical.MyCartActivity;
import com.release.reelAfrican.physical.ProgressBarHandler;
import com.release.reelAfrican.physical.PurchaseHistory_Activity;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.release.reelAfrican.activity.MainActivity.isNavigated;

public class DemoActivity extends AppCompatActivity {


    FragmentTransaction fragmentTransaction;
    FrameLayout frameLayout;
    Toolbar mActionBarToolbar;
    SharedPreferences pref;
    String emailstr,userid;
    int isLogin = 0;
    SharedPreferences isLoginPref;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
   String AOD,HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);



        if (getIntent().getStringExtra("AOD")!=null && getIntent().getStringExtra("AOD")!=""){

            AOD=getIntent().getStringExtra("AOD");

        }else {

            AOD="";
        }


        if (getIntent().getStringExtra("HOME")!=null && getIntent().getStringExtra("HOME")!=""){

            HOME=getIntent().getStringExtra("HOME");

        }else {

            HOME="";
        }


        mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Audio");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AOD!=null && AOD!=""){

                    finish();

                }else if (HOME!=null && HOME!=""){

                    finish();

                }

                else {

                    onBackPressed();

                }



            }
        });




        isLoginPref = getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode

        isLogin = isLoginPref.getInt(Util.IS_LOGIN_PREF_KEY, 0);

        frameLayout= (FrameLayout) findViewById(R.id.frameLayout);
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref != null)
            userid= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        final Fragment fragment = new MultiPartFragment();
        Bundle multidetailsIntent = new Bundle();
        multidetailsIntent.putString( "PERMALINK" ,getIntent().getStringExtra("PERMALINK"));
        multidetailsIntent.putString( "CONTENT_TYPE" ,getIntent().getStringExtra("CONTENT_TYPE"));
        multidetailsIntent.putString( "USER_ID" ,userid);


        Log.v("SUBHA","UserID === "+ userid);

        fragment.setArguments(multidetailsIntent);

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
        }
                         /*  fragmentTransaction.replace(R.id.video_frame, fragment,"MultiPartFragment");
                                fragmentTransaction.addToBackStack("MultiPartFragment");
                                fragmentTransaction.commit();*/
        runOnUiThread(new Runnable() {
            public void run() {


                fragmentTransaction.add(R.id.frameLayout, fragment, "MultiPartFragment");
                fragmentTransaction.addToBackStack("MultiPartFragment");
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);


        /************chromecast***********/
//        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
//                R.id.media_route_menu_item);
        //showIntroductoryOverlay();

        /************chromecast***********/


        MenuItem item,item1,item2,item3,item4,item5,item6,item7,item8;
        item= menu.findItem(R.id.action_filter);
        item.setVisible(false);

        item7 = menu.findItem(R.id.action_search);
        item7.setVisible(true);
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            userid= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            userid= "";

        }

        SharedPreferences language_list_pref = getSharedPreferences(Util.LANGUAGE_LIST_PREF, 0);
        (menu.findItem(R.id.menu_item_language)).setTitle(Util.getTextofLanguage(DemoActivity.this, Util.LANGUAGE_POPUP_LANGUAGE, Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
        if(language_list_pref.getString("total_language","0").equals("1"))
            (menu.findItem(R.id.menu_item_language)).setVisible(false);

        if(emailstr!=null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));
            item4.setVisible(false);
            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            item5.setVisible(false);
          /*  item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(MainActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));
            item1.setVisible(true);

            item2 = menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(true);

            item3 = menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(true);

            item6 = menu.findItem(R.id.action_mydownload);
            item6.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.MY_DOWNLOAD, Util.DEFAULT_MY_DOWNLOAD));
            item6.setVisible(true);
            item7 = menu.findItem(R.id.menu_item_favorite);
            item7.setTitle(Util.getTextofLanguage(this, Util.MY_FAVOURITE, Util.DEFAULT_MY_FAVOURITE));
            if ((Util.getTextofLanguage(DemoActivity.this, Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                item7.setVisible(true);
            }else{
                item7.setVisible(false);

            }

            item8 = menu.findItem(R.id.action_notifications);
            item8.setVisible(false);
            LayerDrawable icon = (LayerDrawable) item8.getIcon();

            // Update LayerDrawable's BadgeDrawable
            BadgeCount.setBadgeCount(this, icon, BadgeCount.count);

        }else if(emailstr==null){
            item4= menu.findItem(R.id.action_login);
            item4.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.LANGUAGE_POPUP_LOGIN, Util.DEFAULT_LANGUAGE_POPUP_LOGIN));


            item5= menu.findItem(R.id.action_register);
            item5.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.BTN_REGISTER, Util.DEFAULT_BTN_REGISTER));
            if(isLogin == 1)
            {
                item4.setVisible(true);
                item5.setVisible(true);

            }else{
                item4.setVisible(false);
                item5.setVisible(false);

            }
            item6 = menu.findItem(R.id.action_mydownload);
            item6.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.MY_DOWNLOAD, Util.DEFAULT_MY_DOWNLOAD));
            item6.setVisible(false);
           /* item6= menu.findItem(R.id.menu_item_language);
            item6.setTitle(Util.getTextofLanguage(MainActivity.this,Util.LANGUAGE_POPUP_LANGUAGE,Util.DEFAULT_LANGUAGE_POPUP_LANGUAGE));
            item6.setVisible(true);*/
            item1 = menu.findItem(R.id.menu_item_profile);
            item1.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.PROFILE, Util.DEFAULT_PROFILE));
            item1.setVisible(false);
            item2= menu.findItem(R.id.action_purchage);
            item2.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.PURCHASE_HISTORY, Util.DEFAULT_PURCHASE_HISTORY));
            item2.setVisible(false);
            item3= menu.findItem(R.id.action_logout);
            item3.setTitle(Util.getTextofLanguage(DemoActivity.this, Util.LOGOUT, Util.DEFAULT_LOGOUT));
            item3.setVisible(false);
            item7 = menu.findItem(R.id.menu_item_favorite);
            item7.setTitle(Util.getTextofLanguage(this, Util.MY_FAVOURITE, Util.DEFAULT_MY_FAVOURITE));
            item7.setVisible(false);

            item8 = menu.findItem(R.id.action_notifications);
            item8.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(DemoActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                isNavigated = 1;
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;


            case R.id.action_notifications:

                // Not implemented here
                final Intent cart = new Intent(DemoActivity.this, MyCartActivity.class);
                cart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                cart.putExtra("main","main");
                startActivity(cart);
                finish();

                return false;

            case R.id.action_login:

                Intent loginIntent = new Intent(DemoActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(DemoActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;

            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(DemoActivity.this, ProfileActivity.class);
//                profileIntent.putExtra("EMAIL",email);
//                profileIntent.putExtra("LOGID",id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(DemoActivity.this, PurchaseHistory_Activity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
                Log.v("ANUU","going to favorite activity");
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.action_mydownload:

                Intent mydownload = new Intent(DemoActivity.this, MyDownloads.class);
                startActivity(mydownload);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DemoActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(Util.getTextofLanguage(DemoActivity.this, Util.SIGN_OUT_WARNING, Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(Util.getTextofLanguage(DemoActivity.this, Util.YES, Util.DEFAULT_YES) ,new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        AsynLogoutDetails asynLogoutDetails=new AsynLogoutDetails();
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);



                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(Util.getTextofLanguage(DemoActivity.this, Util.NO, Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
                dlgAlert.setCancelable(false);
           /* dlgAlert.setNegativeButton(getResources().getString(R.string.no_str),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no_str),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });*/
                dlgAlert.create().show();

               /* Intent lanuageIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);*/
                // Not implemented here
                return false;
            default:
                break;
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


            String urlRouteList = Util.rootUrlmuvicart().trim()+ Util.logoutUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("login_history_id",loginHistoryIdStr);
                httppost.addHeader("lang_code", Util.getTextofLanguage(DemoActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


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
                Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR));

                //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if(responseStr == null){
                Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR));

                // Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

            }
            if (responseCode == 0) {
                Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR));

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
                    if ((Util.getTextofLanguage(DemoActivity.this, Util.IS_ONE_STEP_REGISTRATION, Util.DEFAULT_IS_ONE_STEP_REGISTRATION)
                            .trim()).equals("1")) {
                        final Intent startIntent = new Intent(DemoActivity.this, SplashScreen.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }
                    else
                    {
                        final Intent startIntent = new Intent(DemoActivity.this, MainActivity.class);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.LOGOUT_SUCCESS, Util.DEFAULT_LOGOUT_SUCCESS));
                                finish();

                            }
                        });
                    }

                }
                else {
                    Util.showToast(DemoActivity.this, Util.getTextofLanguage(DemoActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR));

                    //Toast.makeText(MovieDetailsActivity.this,Util.getTextofLanguage(MovieDetailsActivity.this, Util.SIGN_OUT_ERROR, Util.DEFAULT_SIGN_OUT_ERROR),Toast.LENGTH_LONG).show();

                }
            }

        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(DemoActivity.this);
            pDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Log.v("SUBHA","HOME == "+ HOME);

       /* if (HOME != null && HOME != "") {

            finish();

        } else {

            Log.v("SUBHA","HOME == "+ HOME);
            finish();
            Intent intent = new Intent(DemoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }*/
    }
}