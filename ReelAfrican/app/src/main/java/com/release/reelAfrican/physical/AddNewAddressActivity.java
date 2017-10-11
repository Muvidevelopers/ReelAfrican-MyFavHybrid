package com.release.reelAfrican.physical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class AddNewAddressActivity extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    EditText et_name,et_add,et_city,et_country,et_zip,et_ph;
    AsynSaveaddress asynSaveaddress;
    private ProgressBarHandler videoPDialog;
    String first_name,address_details,city_details,country_details,zip_details,phone_details;
    SharedPreferences pref;
    String emailstr,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewaddress);


        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            user_id= "";

        }
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


        et_name = (EditText) findViewById(R.id.nme);
        et_add = (EditText) findViewById(R.id.address);
        et_city = (EditText) findViewById(R.id.city);
        et_country = (EditText) findViewById(R.id.country);
        et_zip = (EditText) findViewById(R.id.zip);
        et_ph = (EditText) findViewById(R.id.ph);



        Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
        et_name.setTypeface(font);
        et_add.setTypeface(font);
        et_city.setTypeface(font);
        et_country.setTypeface(font);
        et_zip.setTypeface(font);
        et_ph.setTypeface(font);
    }

    public void save(View view){

        first_name=et_name.getText().toString();
        address_details=et_add.getText().toString();
        city_details=et_city.getText().toString();
        country_details=et_country.getText().toString();
        zip_details=et_zip.getText().toString();
        phone_details=et_ph.getText().toString();

        if (first_name.matches("")) {


             Toast.makeText(AddNewAddressActivity.this,"Name should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (address_details.matches("")) {

            Toast.makeText(AddNewAddressActivity.this,"Address should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (city_details.matches("")) {

            Toast.makeText(AddNewAddressActivity.this,"City should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (country_details.matches("")) {

            Toast.makeText(AddNewAddressActivity.this,"Country should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (zip_details.matches("")) {

            Toast.makeText(AddNewAddressActivity.this,"Zip should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (phone_details.matches("")) {

            Toast.makeText(AddNewAddressActivity.this,"Phone number should not blanck", Toast.LENGTH_LONG).show();

        }else {


            asynSaveaddress = new AsynSaveaddress();
            asynSaveaddress.execute();

        }



    }


    private class AsynSaveaddress extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.savepgaddress.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("firstname", first_name);
                httppost.addHeader("address", address_details);
                httppost.addHeader("city", city_details);
                httppost.addHeader("country", country_details);
                httppost.addHeader("zip", zip_details);
                httppost.addHeader("phoneno", phone_details);



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


            if (status == 200) {

             Toast.makeText(getApplicationContext(),"Address saved successfully", Toast.LENGTH_SHORT).show();

                final Intent searchIntent = new Intent(AddNewAddressActivity.this, DeliveryAddressActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                finish();

            } else {


            }

        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(AddNewAddressActivity.this);
            videoPDialog.show();

        }



    }
}
