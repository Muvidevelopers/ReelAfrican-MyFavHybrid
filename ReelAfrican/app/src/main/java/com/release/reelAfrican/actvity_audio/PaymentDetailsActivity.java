package com.release.reelAfrican.actvity_audio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PaymentDetailsActivity extends AppCompatActivity {

    RadioButton creditcardRadioButton, voucherRadioButton;
    LinearLayout creditcardLinearlayout, voucherLinearLayout;
    EditText creditcardnumber, editcardholdername, EditExpirey, EditCVV;
    TextView cardchargeprice, itemprice;
    Button watchnowbutton;
    Spinner savedcardSpinner;
    ImageView cardBackGroundImage;
    ArrayList<String> spinnerarray = new ArrayList<String>();
    CheckBox saveCardCheckbox;
    String cardnumber, cardholdername, cardexpirey, cardcvv;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    ArrayAdapter<Integer> cardExpiryYearSpinnerAdapter;
    ArrayAdapter<Integer> cardExpiryMonthSpinnerAdapter;

    SharedPreferences loginPref, countryPref;

    String expiryMonthStr,statusStr,tokenStr,responseText,profileIdStr,cardLastFourDigitStr,cardTypeStr,userIdStr,emailstr,planid,displayname;
    String expiryYearStr,currencyId,content_types_id,permalink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        creditcardRadioButton = (RadioButton) findViewById(R.id.creditcardRadioButton);
        voucherRadioButton = (RadioButton) findViewById(R.id.voucherRadioButton);

        creditcardLinearlayout = (LinearLayout) findViewById(R.id.creditcardLinearlayout);
        voucherLinearLayout = (LinearLayout) findViewById(R.id.voucherLinearLayout);

        creditcardnumber = (EditText) findViewById(R.id.creditcardnumber);
        editcardholdername = (EditText) findViewById(R.id.editcardholdername);
        EditExpirey = (EditText) findViewById(R.id.EditExpirey);
        EditCVV = (EditText) findViewById(R.id.EditCVV);

        cardchargeprice = (TextView) findViewById(R.id.cardchargeprice);
        itemprice = (TextView) findViewById(R.id.itemprice);

        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);

        watchnowbutton = (Button) findViewById(R.id.watchnowbutton);

        cardBackGroundImage=(ImageView)findViewById(R.id.cardBackGroundImage);

        creditcardRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                creditcardLinearlayout.setVisibility(View.VISIBLE);
                voucherLinearLayout.setVisibility(View.GONE);
                cardBackGroundImage.setVisibility(View.VISIBLE);
                saveCardCheckbox.setVisibility(View.VISIBLE);
            }
        });

        voucherRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherLinearLayout.setVisibility(View.VISIBLE);
                creditcardLinearlayout.setVisibility(View.GONE);
                cardBackGroundImage.setVisibility(View.GONE);
                saveCardCheckbox.setVisibility(View.GONE);

            }
        });

        loginPref = getSharedPreferences(Util.LOGIN_PREF, 0);


        ///credicard number edittext formatting
        creditcardnumber.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!isInputCorrect(editable, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    editable.replace(0, editable.length(), buildCorrecntString(getDigitArray(editable, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }

            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // chech that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }


        });

        String pricestr = getIntent().getStringExtra("price");
        cardchargeprice.setText(pricestr);
        Log.v("pratikp", "priicee=" + pricestr);
        itemprice.setText(pricestr);

        ////////////getting permalink and content_types_id
        content_types_id=getIntent().getStringExtra("content_types_id");
        permalink=getIntent().getStringExtra("permalink");



        watchnowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(PaymentDetailsActivity.this, PurchaseHistoryActivity.class);
                startActivity(intent);*/

                cardnumber = creditcardnumber.getText().toString().trim();
                cardholdername = editcardholdername.getText().toString().trim();
                cardexpirey = EditExpirey.getText().toString().trim();
                cardcvv = EditCVV.getText().toString().trim();

              // expiryMonthStr=cardexpirey.trim().substring(2);
                expiryYearStr=cardexpirey.trim().substring(cardexpirey.lastIndexOf("/")+1);

                String segs[]=cardexpirey.split("/");

                try {
                    expiryMonthStr = segs[0];
                    expiryYearStr = segs[1];
                    Log.v("exp","month=="+segs[0]);
                    Log.v("exp","yr=="+segs[1]);
                }catch (Exception e){
                    e.printStackTrace();

                }


                Log.v("exp","lengh=="+cardexpirey.length());

                if(cardexpirey.length()==0 || cardexpirey.matches("")){
                    Toast.makeText(PaymentDetailsActivity.this, "fill the empty field(s)", Toast.LENGTH_SHORT).show();
                }
                else {

                    AsynPaymentInfoDetails asyncReg = new AsynPaymentInfoDetails();
                    asyncReg.executeOnExecutor(threadPoolExecutor);
                }
            }
        });

        savedcardSpinner = (Spinner) findViewById(R.id.savedcardSpinner);
        spinnerarray.add("card1");
        spinnerarray.add("card2");
        spinnerarray.add("card3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerarray);
        savedcardSpinner.setAdapter(arrayAdapter);

        EditExpirey.addTextChangedListener(new TextWatcher() {

            private static final String INITIAL_MONTH_ADD_ON = "0";
            private static final String DEFAULT_MONTH = "01";
            private static final String SPACE = "/";
            private int mLength;


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mLength=EditExpirey.getText().length();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


               /* if (before == 1 && count == 2 && s.charAt(s.length()-1) != '/') {
                    EditExpirey.setText(EditExpirey.getText() + "/");
                }
                if (EditExpirey.getText().toString().toCharArray().length < 3) {
                 //  EditExpirey.setText(EditExpirey.getText().toString().replace("/", ""));
                }*/


            }

            @Override
            public void afterTextChanged(Editable s) {

               /* if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                        editable.insert(editable.length() - 1, String.valueOf("/"));
                    }
                }*/


                int currentLength = EditExpirey.getText().length();
                boolean ignoreBecauseIsDeleting = false;
                if (mLength > currentLength) {
                    ignoreBecauseIsDeleting = true;
                }

                if (s.length() == 1 && !isNumberChar(String.valueOf(s.charAt(0)))) {
                    EditExpirey.setText(DEFAULT_MONTH + SPACE);
                } else if (s.length() == 1 && !isCharValidMonth(s.charAt(0))) {
                    EditExpirey.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s.charAt(0)) + SPACE);
                } else if (s.length() == 2 && String.valueOf(s.charAt(s.length() - 1)).equals(SPACE)) {
                    EditExpirey.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s));
                } else if (!ignoreBecauseIsDeleting &&
                        (s.length() == 2 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE))) {
                    EditExpirey.setText(EditExpirey.getText().toString() + SPACE);
                } else if (s.length() == 3 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE) && !ignoreBecauseIsDeleting) {
                    s.insert(2, SPACE);
                    EditExpirey.setText(String.valueOf(s));
                } else if (s.length() > 3 && !isCharValidMonth(s.charAt(0))) {
                    EditExpirey.setText(INITIAL_MONTH_ADD_ON + s);
                }

                if (!ignoreBecauseIsDeleting) {
                    EditExpirey.setSelection(EditExpirey.getText().toString().length());
                }


            }
            private boolean isCharValidMonth(char charFromString) {
                int month = 0;
                if (Character.isDigit(charFromString)) {
                    month = Integer.parseInt(String.valueOf(charFromString));
                }
                int expiryMonthStr=month;
                return month <= 1;

            }

            private boolean isNumberChar(String string) {
                return string.matches(".*\\d.*");
            }


        });


        if (saveCardCheckbox.isChecked()) {


        }


    }

    private class AsynPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        ProgressBarHandler progressBarHandler;
        int status;
        String responseStr;
        String responseMessageStr;
//        String emailIdStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);




        @Override
        protected Void doInBackground(Void... voids) {

             userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            Log.v("pratikp","used ID*="+userIdStr);
           // String emailIdSubStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            String urlRouteList = Util.rootUrl().trim() + Util.authenticatedCardValidationUrl.trim();

            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("nameOnCard", cardholdername);
                httppost.addHeader("expiryMonth", String.valueOf(expiryMonthStr).trim());
                httppost.addHeader("expiryYear", String.valueOf(expiryYearStr).trim());
                httppost.addHeader("cardNumber", cardnumber);
                httppost.addHeader("cvv", cardcvv);
          //      httppost.addHeader("email", emailIdStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());
               /* if (isAPV == 1) {
                    httppost.addHeader("is_advance", "1");
                }*/
              //  httppost.addHeader("email", emailIdSubStr.trim());
            //    httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                //httppost.addHeader("movie_id","5a07372fd347136975e3dd4c9897cf23");
             //   httppost.addHeader("user_id", userIdStr.trim());

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("pratikp","res=="+responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressBarHandler.isShowing())
                                progressBarHandler.hide();
                            status = 0;
                            Util.showToast(PaymentDetailsActivity.this, Util.getTextofLanguage(PaymentDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));

                            //   Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("isSuccess"));

                    if (status == 1) {
                        JSONObject mainJson = null;

                        if (myJson.has("card")) {
                            mainJson = myJson.getJSONObject("card");
                            if (mainJson.has("status") && mainJson.getString("status").trim() != null && !mainJson.getString("status").trim().isEmpty() && !mainJson.getString("status").trim().equals("null") && !mainJson.getString("status").trim().matches("")) {
                                statusStr = mainJson.getString("status");
                            } else {
                                statusStr = "";

                            }
                            if (mainJson.has("token") && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
                                tokenStr = mainJson.getString("token");
                            } else {
                                tokenStr = "";

                            }

                            if (mainJson.has("response_text") && mainJson.getString("response_text").trim() != null && !mainJson.getString("response_text").trim().isEmpty() && !mainJson.getString("response_text").trim().equals("null") && !mainJson.getString("response_text").trim().matches("")) {
                                responseText = mainJson.getString("response_text");
                            } else {
                                responseText = "";

                            }

                            if (mainJson.has("profile_id") && mainJson.getString("profile_id").trim() != null && !mainJson.getString("profile_id").trim().isEmpty() && !mainJson.getString("profile_id").trim().equals("null") && !mainJson.getString("profile_id").trim().matches("")) {
                                profileIdStr = mainJson.getString("profile_id");
                            } else {
                                profileIdStr = "";

                            }

                            if (mainJson.has("card_last_fourdigit") && mainJson.getString("card_last_fourdigit").trim() != null && !mainJson.getString("card_last_fourdigit").trim().isEmpty() && !mainJson.getString("card_last_fourdigit").trim().equals("null") && !mainJson.getString("card_last_fourdigit").trim().matches("")) {
                                cardLastFourDigitStr = mainJson.getString("card_last_fourdigit");
                            } else {
                                cardLastFourDigitStr = "";

                            }

                            if (mainJson.has("card_type") && mainJson.getString("card_type").trim() != null && !mainJson.getString("card_type").trim().isEmpty() && !mainJson.getString("card_type").trim().equals("null") && !mainJson.getString("card_type").trim().matches("")) {
                                cardTypeStr = mainJson.getString("card_type");
                            } else {
                                cardTypeStr = "";

                            }

                        }
                    }

                    if (status == 0) {
                        if (myJson.has("Message")) {
                            responseMessageStr = myJson.optString("Message");
                        }
                        if (((responseMessageStr.equalsIgnoreCase("null")) || (responseMessageStr.length() <= 0))) {
                            responseMessageStr = "No Details found";

                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result){
            if (status == 0) {
                try {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentDetailsActivity.this);
                dlgAlert.setMessage(responseMessageStr);
                dlgAlert.setTitle("Failure");
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();
            }

            else if (status == 1){
                boolean isNetwork = Util.checkNetwork(PaymentDetailsActivity.this);
                if (isNetwork == false) {
                    try {
                        if (progressBarHandler.isShowing())
                            progressBarHandler.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentDetailsActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {


                 planid=getIntent().getStringExtra("selected_plan_id");
                    currencyId=getIntent().getStringExtra("currencyId");

                    emailstr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
                    displayname = loginPref.getString("display_namePref", null);
                    Log.v("pratikp","email str="+emailstr);
                    Log.v("pratikp","name str="+displayname);

                   /* Intent intent=new Intent(PaymentDetailsActivity.this, PurchaseHistoryActivity.class);
                    startActivity(intent);*/

                    ////////register user payment////////////
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                      AsynSubscriptionRegDetails asyncSubsrInfo = new AsynSubscriptionRegDetails();
                    asyncSubsrInfo.executeOnExecutor(threadPoolExecutor);
                }

            }
        }

        @Override
        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(PaymentDetailsActivity.this);
            progressBarHandler.show();
        }

        private class AsynSubscriptionRegDetails extends AsyncTask<Void, Void, Void> {

            ProgressBarHandler progressBarHandler = new ProgressBarHandler(PaymentDetailsActivity.this);;
            int status;
            String responseStr;
            @Override
            protected Void doInBackground(Void... voids) {

                String urlRouteList = Util.rootUrl().trim() + Util.subscriptionUrl.trim();

                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(urlRouteList);
                    httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                    httppost.addHeader("authToken", Util.authTokenStr.trim());
                    Log.v("SUBHA", "=========== 1");
                    httppost.addHeader("nameOnCard", cardholdername);
                    httppost.addHeader("expiryMonth", String.valueOf(expiryMonthStr).trim());
                    httppost.addHeader("expiryYear", String.valueOf(expiryYearStr).trim());
                    httppost.addHeader("cardNumber", cardnumber);
                    httppost.addHeader("cvv", cardcvv);
                    //  httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                    httppost.addHeader("user_id", userIdStr.trim());
                    Log.v("pratikp","user_id added="+userIdStr.trim());

                    httppost.addHeader("email", emailstr.trim());
                    Log.v("pratikp","email added="+emailstr.trim());

                    httppost.addHeader("card_type", cardTypeStr.trim());

                    httppost.addHeader("card_last_fourdigit", cardLastFourDigitStr.trim());
                    Log.v("pratikp","card_last_fourdigit="+cardLastFourDigitStr.trim());

                    httppost.addHeader("profile_id", profileIdStr.trim());
                    Log.v("pratikp","profile_id="+profileIdStr.trim());

                    httppost.addHeader("token", tokenStr.trim());
                    Log.v("pratikp","token="+tokenStr.trim());

                    httppost.addHeader("season_id", "0");
                    httppost.addHeader("episode_id", "0");

                    httppost.addHeader("currency_id", currencyId.trim());
                    Log.v("pratikp","currency_id="+currencyId.trim());

                    httppost.addHeader("plan_id", getIntent().getStringExtra("selected_plan_id").toString().trim());
                    Log.v("pratikp","plan_id="+getIntent().getStringExtra("selected_plan_id").toString().trim());

                    httppost.addHeader("name", loginPref.getString("display_namePref", ""));
                    Log.v("pratikp","name="+loginPref.getString("display_namePref", ""));
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        responseStr = EntityUtils.toString(response.getEntity());
                        Log.v("pratikp", "response of payment = " + responseStr);

                    } catch (org.apache.http.conn.ConnectTimeoutException e) {
                        Log.v("SUBHA", "error2=" + e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                status = 0;
                                if (progressBarHandler.isShowing())
                                    progressBarHandler.hide();
                                Util.showToast(PaymentDetailsActivity.this, Util.getTextofLanguage(PaymentDetailsActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                                // Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                            }

                        });

                    } catch (IOException e) {

                        status = 0;
                        if (progressBarHandler.isShowing())
                            progressBarHandler.hide();
                        e.printStackTrace();
                        Log.v("SUBHA", "error1=" + e.toString());
                    }

                    if (responseStr != null) {
                        JSONObject myJson = new JSONObject(responseStr);
                        status = Integer.parseInt(myJson.optString("code"));
                    }
                } catch (Exception e) {

                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                    status = 0;
                    Log.v("SUBHA", "error=" + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                if (status == 0) {
                    try {
                        if (progressBarHandler.isShowing())
                            progressBarHandler.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentDetailsActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.FAILURE, Util.DEFAULT_FAILURE));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentDetailsActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();
                }

                else if (status > 0) {

                    if (status == 200) {
                        if (progressBarHandler.isShowing()) {
                            progressBarHandler.hide();
                            Log.v("pratikp","status 200 showing..");
                        }
                        Util.showToast(PaymentDetailsActivity.this, Util.getTextofLanguage(PaymentDetailsActivity.this, Util.SUBSCRIPTION_COMPLETED, Util.DEFAULT_SUBSCRIPTION_COMPLETED));

                        if (Util.checkNetwork(PaymentDetailsActivity.this) == true) {
                            /////////////////PLAY SONG/////////////////
                           // Toast.makeText(PaymentDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
                            Log.v("pratikp","trueeeee..");

                            Fragment fragment=new MultiPartFragment();

                            Bundle arguments = new Bundle();
                            arguments.putString( "PERMALINK" ,permalink);
                            arguments.putString( "CONTENT_TYPE" ,content_types_id);

                            Log.v("pratikp","perma.."+getIntent().getStringExtra("permalink"));
                            Log.v("pratikp","content id.."+getIntent().getStringExtra("content_types_id"));

                            fragment.setArguments(arguments);


                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.paymentdetailsFramelayout, fragment);
                            fragmentTransaction.commit();
                            /*Intent intent = new Intent(PaymentDetailsActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();*/

                        }
                        else {
                            Intent intent = new Intent(PaymentDetailsActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                            Util.showToast(PaymentDetailsActivity.this, Util.getTextofLanguage(PaymentDetailsActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));

                            //  Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();


                        }

                    }
                }
            }

            @Override
            protected void onPreExecute() {

                progressBarHandler.show();

            }
        }



    }
}
