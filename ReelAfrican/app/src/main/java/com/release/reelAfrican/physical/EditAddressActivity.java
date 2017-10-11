package com.release.reelAfrican.physical;

import android.content.Intent;
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


public class EditAddressActivity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    EditText et_name,et_add,et_city,et_country,et_zip,et_ph;
    String id,NAME,ADDRESS,PHONE,CITY,COUNTRY,ZIP;
    AsynUpdateaddress asynUpdateaddress;
    String first_name,address_details,city_details,country_details,zip_details,phone_details;
    private ProgressBarHandler videoPDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Edit Address");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });



        id=getIntent().getStringExtra("ID");
        NAME=getIntent().getStringExtra("NAME");
        ADDRESS=getIntent().getStringExtra("ADDRESS");
        PHONE=getIntent().getStringExtra("PHONE");
        CITY=getIntent().getStringExtra("CITY");
        COUNTRY=getIntent().getStringExtra("COUNTRY");
        ZIP=getIntent().getStringExtra("ZIP");

        et_name = (EditText) findViewById(R.id.nme);
        et_add = (EditText) findViewById(R.id.address);
        et_city = (EditText) findViewById(R.id.city);
        et_country = (EditText) findViewById(R.id.country);
        et_zip = (EditText) findViewById(R.id.zip);
        et_ph = (EditText) findViewById(R.id.ph);

        et_name.setText(NAME);
        et_add.setText(ADDRESS);
        et_city.setText(CITY);
        et_country.setText(COUNTRY);
        et_zip.setText(ZIP);
        et_ph.setText(PHONE);

        Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));
        et_name.setTypeface(font);
        et_add.setTypeface(font);
        et_city.setTypeface(font);
        et_country.setTypeface(font);
        et_zip.setTypeface(font);
        et_ph.setTypeface(font);
    }


    public void Update(View view){


        first_name=et_name.getText().toString();
        address_details=et_add.getText().toString();
        city_details=et_city.getText().toString();
        country_details=et_country.getText().toString();
        zip_details=et_zip.getText().toString();
        phone_details=et_ph.getText().toString();

        if (first_name.matches("")) {


            Toast.makeText(EditAddressActivity.this,"Name should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (address_details.matches("")) {

            Toast.makeText(EditAddressActivity.this,"Address should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (city_details.matches("")) {

            Toast.makeText(EditAddressActivity.this,"City should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (country_details.matches("")) {

            Toast.makeText(EditAddressActivity.this,"Country should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (zip_details.matches("")) {

            Toast.makeText(EditAddressActivity.this,"Zip should not blanck", Toast.LENGTH_LONG).show();

        }

        else if (phone_details.matches("")) {

            Toast.makeText(EditAddressActivity.this,"Phone number should not blanck", Toast.LENGTH_LONG).show();

        }else {


            asynUpdateaddress = new AsynUpdateaddress();
            asynUpdateaddress.execute();

        }


    }


    private class AsynUpdateaddress extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.editpgaddress.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("addressid", id);
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

                Toast.makeText(getApplicationContext(),"Address Updated successfully", Toast.LENGTH_SHORT).show();
                finish();
                final Intent searchIntent = new Intent(EditAddressActivity.this, DeliveryAddressActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);

            } else {


            }

        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(EditAddressActivity.this);
            videoPDialog.show();

        }

    }
}
