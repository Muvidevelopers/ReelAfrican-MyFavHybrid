package com.release.reelAfrican.physical;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Payment extends ActionBarActivity {
    CardModel[] cardSavedArray;
    String existing_card_id = "";
    String isCheckedToSavetheCard = "1";
    SharedPreferences countryPref,pref;
    Double chargedPrice = 0.00;
    Toolbar mActionBarToolbar;
    boolean isCouponCodeAdded = false;
    String validCouponCode;
    AsynmuvicartPaymentInfoDetails asynmuvicartPaymentInfoDetails;
    private ProgressBarHandler videoPDialog;
    Spinner cardExpiryYearSpinner;
    Spinner cardExpiryMonthSpinner;
    Spinner creditCardSaveSpinner;
    private RelativeLayout creditCardLayout,RL;
    private RelativeLayout withoutCreditCardLayout;
    private LinearLayout cardExpiryDetailsLayout;
    private CheckBox saveCardCheckbox;
    private TextView withoutCreditCardChargedPriceTextView;
    private Button nextButton;
    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText securityCodeEditText;
    private Button scanButton;
    private Button payNowButton,payNowButton1;
    private Button applyButton;
    private EditText couponCodeEditText;
    private TextView selectShowRadioButton;
    private TextView chargedPriceTextView;
    private TextView TOTALPRICE,cardchargedprice;
    String cardLastFourDigitStr;
    String tokenStr;
    String cardTypeStr;
    String responseText;
    String statusStr;
    String currencyCountryCodeStr;
    String currencyIdStr;
    String currencySymbolStr,shippingcost;
    String videoPreview;
    String profileIdStr;
    int expiryMonthStr = 0;
    int expiryYearStr = 0;
    String couponCodeStr;
    ArrayAdapter<Integer> cardExpiryYearSpinnerAdapter;
    ArrayAdapter<Integer> cardExpiryMonthSpinnerAdapter;
    CardSpinnerAdapter creditCardSaveSpinnerAdapter;
    List<Integer> yearArray = new ArrayList<Integer>(21);
    List<Integer> monthsIdArray = new ArrayList<Integer>(12);
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    String emailstr,user_id;
    TextView creditCardDetailsTitleTextView;
    ArrayList<CardModel> savedCards;
    String cardid;
    Double afterdiscount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        videoPreview = Util.getTextofLanguage(Payment.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        creditCardDetailsTitleTextView = (TextView) findViewById(R.id.creditCardDetailsTitleTextView);
        //Set toolbar
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Payment.this);
                onBackPressed();
            }
        });

        countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0);
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);


        }else {
            emailstr = "";
            user_id= "";

        }

        if (getIntent().getStringExtra("currencyId") != null) {
            currencyIdStr = getIntent().getStringExtra("currencyId");
        } else {
            currencyIdStr = "";
        }

        if (getIntent().getStringExtra("currencyCountryCode") != null) {
            currencyCountryCodeStr = getIntent().getStringExtra("currencyCountryCode");
        } else {
            currencyCountryCodeStr = "";
        }

        if (getIntent().getStringExtra("csymbol") != null) {
            currencySymbolStr = getIntent().getStringExtra("csymbol");
        } else {
            currencySymbolStr = "";
        }

        if (getIntent().getStringExtra("shippingcost") != null) {

            shippingcost = getIntent().getStringExtra("shippingcost");

        } else {

            shippingcost = "";
        }

        TOTALPRICE = (TextView) findViewById(R.id.TOTAL_PRICE);
        cardchargedprice = (TextView) findViewById(R.id.chargeprice_detailtxt);
        nameOnCardEditText = (EditText) findViewById(R.id.nameOnCardEditText);
        cardNumberEditText = (EditText) findViewById(R.id.cardNumberEditText);
        securityCodeEditText = (EditText) findViewById(R.id.securityCodeEditText);
        couponCodeEditText = (EditText) findViewById(R.id.couponCodeEditText);
        couponCodeEditText.addTextChangedListener(filterTextWatcher);

        Typeface typeface6 = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.light_fonts));
        nameOnCardEditText.setTypeface(typeface6);
        cardNumberEditText.setTypeface(typeface6);
        securityCodeEditText.setTypeface(typeface6);
        couponCodeEditText.setTypeface(typeface6);

        nameOnCardEditText.setHint(Util.getTextofLanguage(Payment.this, Util.CREDIT_CARD_NAME_HINT, Util.DEFAULT_CREDIT_CARD_NAME_HINT));
        cardNumberEditText.setHint(Util.getTextofLanguage(Payment.this, Util.CREDIT_CARD_NUMBER_HINT, Util.DEFAULT_CREDIT_CARD_NUMBER_HINT));
        securityCodeEditText.setHint(Util.getTextofLanguage(Payment.this, Util.CREDIT_CARD_CVV_HINT, Util.DEFAULT_CREDIT_CARD_CVV_HINT));
        couponCodeEditText.setHint(Util.getTextofLanguage(Payment.this, Util.COUPON_CODE_HINT, Util.DEFAULT_COUPON_CODE_HINT));


        chargedPriceTextView = (TextView) findViewById(R.id.chargeDetailsTextView);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
        cardExpiryDetailsLayout = (LinearLayout) findViewById(R.id.cardExpiryDetailsLayout);
        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);
        withoutCreditCardLayout = (RelativeLayout) findViewById(R.id.withoutPaymentLayout);
        withoutCreditCardLayout.setVisibility(View.GONE);
        withoutCreditCardChargedPriceTextView = (TextView) findViewById(R.id.withoutPaymentChargeDetailsTextView);
        nextButton = (Button) findViewById(R.id.nextButton);

        cardExpiryMonthSpinner = (Spinner) findViewById(R.id.cardExpiryMonthEditText);
        cardExpiryYearSpinner = (Spinner) findViewById(R.id.cardExpiryYearEditText);
        creditCardSaveSpinner = (Spinner) findViewById(R.id.creditCardSaveEditText);


        payNowButton = (Button) findViewById(R.id.payNowButton);
        payNowButton1 = (Button) findViewById(R.id.payNowButton1);
        applyButton = (Button) findViewById(R.id.addCouponButton);


        Typeface typeface2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        creditCardDetailsTitleTextView.setTypeface(typeface2);
        creditCardDetailsTitleTextView.setText(Util.getTextofLanguage(Payment.this, Util.CREDIT_CARD_DETAILS, Util.DEFAULT_CREDIT_CARD_DETAILS));

        Typeface typeface3 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
        chargedPriceTextView.setTypeface(typeface3);

        Typeface typeface4 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        payNowButton.setTypeface(typeface4);
        payNowButton.setText(Util.getTextofLanguage(Payment.this, Util.BUTTON_PAY_NOW, Util.DEFAULT_BUTTON_PAY_NOW));

        TOTALPRICE.setText(currencySymbolStr+Util.grandtotal);
        cardchargedprice.setText(currencySymbolStr+Util.grandtotal);
        selectShowRadioButton = (TextView) findViewById(R.id.showNameWithPrice);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 21; i++) {
            yearArray.add(year + i);

        }
        for (int i = 1; i < 13; i++) {
            monthsIdArray.add(i);


        }


        cardExpiryMonthSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, monthsIdArray);
        cardExpiryMonthSpinner.setAdapter(cardExpiryMonthSpinnerAdapter);


        int mn = c.get(Calendar.MONTH);
        if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
            // true
            int mnIndex = monthsIdArray.indexOf(mn + 1);

            cardExpiryMonthSpinner.setSelection(mnIndex);
            expiryMonthStr = monthsIdArray.get(mnIndex);
        } else {
            cardExpiryMonthSpinner.setSelection(0);
            expiryMonthStr = monthsIdArray.get(0);

        }

       /* cardExpiryMonthSpinner.setSelection(0);
        expiryMonthStr = monthsIdArray.get(0);*/

        cardExpiryYearSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, yearArray);
        cardExpiryYearSpinner.setAdapter(cardExpiryYearSpinnerAdapter);
        cardExpiryYearSpinner.setSelection(0);
        expiryYearStr = yearArray.get(0);

        Typeface typeface7 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        saveCardCheckbox.setTypeface(typeface7);
        saveCardCheckbox.setText(" " + Util.getTextofLanguage(Payment.this,Util.SAVE_THIS_CARD,Util.DEFAULT_SAVE_THIS_CARD));

        saveCardCheckbox.setChecked(true);

        creditCardSaveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCardSaveSpinner.setSelection(position);

                cardid=savedCards.get(position).cardId;

                //Toast.makeText(getApplicationContext(),cardid,Toast.LENGTH_LONG).show();

                if (position == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);



                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                creditCardSaveSpinner.setSelection(0);

                if (creditCardSaveSpinner.getSelectedItemPosition() == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);

                    //withoutCreditCardLayout.setVisibility(View.VISIBLE);
                    //creditCardLayout.setVisibility(View.GONE);
                    //chargedPriceTextView.setText(Util.getTextofLanguage(Payment.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " +currencySymbolStr+chargedPrice);
                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);
                }

            }
        });



        saveCardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (isChecked == true) {
                                                                isCheckedToSavetheCard = "1";
                                                            } else {
                                                                isCheckedToSavetheCard = "0";

                                                            }

                                                        }
                                                    }
        );


        cardExpiryMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryMonthSpinner.setSelection(position);
                expiryMonthStr = monthsIdArray.get(position);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();
                int mn = c.get(Calendar.MONTH);
                if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
                    // true
                    int mnIndex = monthsIdArray.indexOf(mn + 1);

                    cardExpiryMonthSpinner.setSelection(mnIndex);
                    expiryMonthStr = monthsIdArray.get(mnIndex);
                } else {
                    cardExpiryMonthSpinner.setSelection(0);
                    expiryMonthStr = monthsIdArray.get(0);

                }
            }
        });
        cardExpiryYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryYearSpinner.setSelection(position);
                expiryYearStr = yearArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                cardExpiryYearSpinner.setSelection(0);
                expiryYearStr = yearArray.get(0);

            }
        });

        chargedPriceTextView.setText(Util.getTextofLanguage(Payment.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" : ");


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                couponCodeStr = couponCodeEditText.getText().toString().trim();

                InputMethodManager imm = (InputMethodManager) getSystemService(Payment.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(couponCodeEditText.getWindowToken(), 0);
                String couponCodeStr = couponCodeEditText.getText().toString().trim();

                if (couponCodeStr.matches("")) {
                    Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this, Util.COUPON_CODE_HINT,Util.DEFAULT_COUPON_CODE_HINT));

                    // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CODE_HINT,Util.DEFAULT_COUPON_CODE_HINT), Toast.LENGTH_LONG).show();

                } else {
                    boolean isNetwork = Util.checkNetwork(Payment.this);
                    if (isNetwork == false) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Payment.this);
                        dlgAlert.setMessage(Util.getTextofLanguage(Payment.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                        dlgAlert.setTitle(Util.getTextofLanguage(Payment.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {
                        Log.v("SUBHA","coupon details");

                        AsynCouponInfoDetails asyncReg = new AsynCouponInfoDetails();
                        asyncReg.executeOnExecutor(threadPoolExecutor);
                    }

                }
            }
        });



        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (creditCardSaveSpinner != null && cardSavedArray != null && cardSavedArray.length > 0 && creditCardSaveSpinner.getSelectedItemPosition() > 0) {

                    boolean isNetwork = Util.checkNetwork(Payment.this);
                    if (isNetwork == false) {
                        Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                        // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    } else {
                        if (creditCardSaveSpinner != null && cardSavedArray != null && cardSavedArray.length > 0 && creditCardSaveSpinner.getSelectedItemPosition() > 0) {
                            existing_card_id = cardSavedArray[creditCardSaveSpinner.getSelectedItemPosition()].getCardId();

                        } else {
                            existing_card_id = "";
                        }

                        AsynExistingCardPaymentInfoDetails asynExistingCardPaymentInfoDetails = new AsynExistingCardPaymentInfoDetails();
                        asynExistingCardPaymentInfoDetails.executeOnExecutor(threadPoolExecutor);
                    }

                } else {
                    String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
                    String cardNumberStr = cardNumberEditText.getText().toString().trim();
                    String securityCodeStr = securityCodeEditText.getText().toString().trim();


                    if (nameOnCardStr.matches("")) {
                        Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.CREDIT_CARD_NAME_HINT,Util.DEFAULT_CREDIT_CARD_NAME_HINT));

                        //  Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NAME_HINT,Util.DEFAULT_CREDIT_CARD_NAME_HINT), Toast.LENGTH_LONG).show();

                    } else if (cardNumberStr.matches("")) {
                        Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.CREDIT_CARD_NUMBER_HINT,Util.DEFAULT_CREDIT_CARD_NUMBER_HINT));

                        //  Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NUMBER_HINT,Util.DEFAULT_CREDIT_CARD_NUMBER_HINT), Toast.LENGTH_LONG).show();

                    } else if (securityCodeStr.matches("")) {
                        Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.CVV_ALERT,Util.DEFAULT_CVV_ALERT));

                        //    Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CVV_ALERT,Util.DEFAULT_CVV_ALERT), Toast.LENGTH_LONG).show();


                    } else if (expiryMonthStr <= 0) {
//                        Toast.makeText(PPvPaymentInfoActivity.this, "Please enter expiry month", Toast.LENGTH_LONG).show();

                    } else if (expiryYearStr <= 0) {
//                        Toast.makeText(PPvPaymentInfoActivity.this, "Please enter expiry year", Toast.LENGTH_LONG).show();

                    } else {
                        boolean isNetwork = Util.checkNetwork(Payment.this);
                        if (isNetwork == false) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Payment.this);
                            dlgAlert.setMessage(Util.getTextofLanguage(Payment.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                            dlgAlert.setTitle(Util.getTextofLanguage(Payment.this, Util.SORRY, Util.DEFAULT_SORRY));
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            dlgAlert.create().show();

                        } else {

                            AsynPaymentInfoDetails asyncReg = new AsynPaymentInfoDetails();
                            asyncReg.executeOnExecutor(threadPoolExecutor);


                        }

                    }
                }

            }
        });




        payNowButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                asynmuvicartPaymentInfoDetails = new AsynmuvicartPaymentInfoDetails();
                asynmuvicartPaymentInfoDetails.executeOnExecutor(threadPoolExecutor);


            }
        });





        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetwork = Util.checkNetwork(Payment.this);
                if (isNetwork == false) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Payment.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(Payment.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(Payment.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(Payment.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {


                }
            }

        });


        AsynLoadCardList asynLoadCardList = new AsynLoadCardList();
        asynLoadCardList.executeOnExecutor(threadPoolExecutor);

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private class AsynCouponInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog1;
        String responseStr;
        String statusStr;


        int status;

        @Override
        protected Void doInBackground(Void... params) {


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.couponCodeValidationUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("couponCode", couponCodeStr);
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("currencyId", Util.currencyid.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("SUBHA","response == "+ responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                } catch (IOException e) {

                    e.printStackTrace();
                }


                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    statusStr = myJson.optString("msg");
                    isCouponCodeAdded = false;
                    validCouponCode = "";

                }

                if (status >= 0) {
                    if (status == 432) {


                        if ((myJson.has("discount_type")) && myJson.getString("discount_type").trim() != null && !myJson.getString("discount_type").trim().isEmpty() && !myJson.getString("discount_type").trim().equals("null") && !myJson.getString("discount_type").trim().matches("")) {
                            String discountTypeStr = myJson.getString("discount_type").trim();

                            if ((myJson.has("discount")) && myJson.getString("discount").trim() != null && !myJson.getString("discount").trim().isEmpty() && !myJson.getString("discount").trim().equals("null") && !myJson.getString("discount").trim().matches("")) {

                                String discount=myJson.getString("discount").trim();

                                if (discountTypeStr.equalsIgnoreCase("%")) {

                                    chargedPrice = ((Double.parseDouble(Util.grandtotal) * Double.parseDouble(discount)) / 100);
                                    afterdiscount=Double.parseDouble(Util.grandtotal)-chargedPrice;
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    afterdiscount = Double.valueOf(decimalFormat.format(afterdiscount));
                                    if (afterdiscount <= 0.0) {
                                        afterdiscount = 0.0;
                                    }

                                } else {

                                    chargedPrice = Double.parseDouble(discount);
                                    afterdiscount=Double.parseDouble(Util.grandtotal)-chargedPrice;
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    afterdiscount = Double.valueOf(decimalFormat.format(afterdiscount));
                                    if (afterdiscount < 0.0) {
                                        afterdiscount = 0.0;
                                    }

                                }
                            }
                        }

                    } else {
                        responseStr = "0";
                        isCouponCodeAdded = false;
                        validCouponCode = "";

                    }
                } else {
                    responseStr = "0";
                    isCouponCodeAdded = false;
                    validCouponCode = "";
                    // couponCodeEditText.setText("");


                }
            } catch (JSONException e1) {

                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
                e1.printStackTrace();
            } catch (Exception e) {

                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {
            try {
                if (pDialog1 != null && pDialog1.isShowing()) {
                    pDialog1.hide();
                    pDialog1 = null;
                }
            } catch (IllegalArgumentException ex) {
                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
            }


            //Double afterdiscount=Double.parseDouble(Util.totalprice)-chargedPrice;



            if(status == 432){

                TOTALPRICE.setText("$"+String.format("%.2f", afterdiscount));
                cardchargedprice.setText("$"+String.format("%.2f", afterdiscount));
                if(afterdiscount==0.0){

                    creditCardLayout.setVisibility(View.GONE);
                    payNowButton1.setVisibility(View.VISIBLE);

                }

                Toast.makeText(getApplicationContext(),"Coupon applied successfully",Toast.LENGTH_LONG).show();

            }else {

                Toast.makeText(getApplicationContext(),"Invalid Coupon",Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            pDialog1 = new ProgressBarHandler(Payment.this);
            pDialog1.show();
        }


    }





    private class AsynPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler progressBarHandler;
        int status;
        String responseStr = "";
        String responseMessageStr;
        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
        String cardNumberStr = cardNumberEditText.getText().toString().trim();
        String securityCodeStr = securityCodeEditText.getText().toString().trim();

        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.authenticatedCardValidationUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("nameOnCard", nameOnCardStr);
                httppost.addHeader("expiryMonth", String.valueOf(expiryMonthStr).trim());
                httppost.addHeader("expiryYear", String.valueOf(expiryYearStr).trim());
                httppost.addHeader("cardNumber", cardNumberStr);
                httppost.addHeader("cvv", securityCodeStr);
                httppost.addHeader("email", emailstr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());



                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            status = 0;
                            Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //Toast.makeText(Payment.this, Util.getTextofLanguage(Payment.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {

                    status = 0;
                    e.printStackTrace();


                }

                Log.v("SUBHA", "response of card validation 1 = " + responseStr);
                JSONObject myJson = null;



                if (responseStr.trim() != null && !responseStr.trim().isEmpty() && !responseStr.trim().equals("null") && !responseStr.trim().matches("")) {
                    Log.v("SUBHA","status 1");
                    Log.v("SUBHA", "response of card validation  2= " + responseStr);
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("isSuccess"));
                }else{

                    Log.v("SUBHA","status 0");
                    status = 0;
                }
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

                    responseMessageStr = Util.getTextofLanguage(Payment.this,Util.DETAILS_NOT_FOUND_ALERT,Util.DEFAULT_DETAILS_NOT_FOUND_ALERT);
                }

            } catch (Exception e) {


                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {


            if (progressBarHandler.isShowing())
                progressBarHandler.hide();



            if(responseStr!=null){

                if(status==1){


                    asynmuvicartPaymentInfoDetails = new AsynmuvicartPaymentInfoDetails();
                    asynmuvicartPaymentInfoDetails.executeOnExecutor(threadPoolExecutor);

                }else {

                    Toast.makeText(getApplicationContext(),"Please enter valid card details",Toast.LENGTH_LONG).show();

                }


            }


        }

        @Override
        protected void onPreExecute() {
            progressBarHandler = new ProgressBarHandler(Payment.this);
            progressBarHandler.show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    private class AsynmuvicartPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler progressBarHandler;
        int code;
        String responseStr = "";



        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim()+Util.muvicartpayment.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id);
                httppost.addHeader("profile_id", profileIdStr);
                httppost.addHeader("amount", Util.totalprice);
                httppost.addHeader("is_save_this_card", "1");
                httppost.addHeader("currency_id", Util.currencyid);
                httppost.addHeader("cart_item", Util.cartitem);
                httppost.addHeader("ship", Util.shipping);
                httppost.addHeader("coupon_code", couponCodeStr);
                httppost.addHeader("card", Util.carddetails);



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());



                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {


                    e.printStackTrace();


                }



                JSONObject myJson = null;



                if (responseStr.trim() != null && !responseStr.trim().isEmpty() && !responseStr.trim().equals("null") && !responseStr.trim().matches("")) {

                    myJson = new JSONObject(responseStr);
                    code = myJson.getInt("code");
                }else{


                }


            } catch (Exception e) {






            }

            return null;
        }


        protected void onPostExecute(Void result) {


            if (progressBarHandler.isShowing())
                progressBarHandler.hide();



            if(responseStr!=null){

                if (code==200){

                    Toast.makeText(getApplicationContext(),"Payment suuceessful",Toast.LENGTH_LONG).show();
//                    asynemptycart = new Asynemptycart();
//                    asynemptycart.executeOnExecutor(threadPoolExecutor);
                    final Intent searchIntent = new Intent(Payment.this, ProductListingActivity.class);
                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(searchIntent);
                }



            }


        }

        @Override
        protected void onPreExecute() {

            Util.carddetails=convertJson();
            progressBarHandler = new ProgressBarHandler(Payment.this);
            progressBarHandler.show();

        }


    }



    private class AsynExistingCardPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler progressBarHandler;
        int code;
        String responseStr = "";



        @Override
        protected Void doInBackground(Void... params) {
            String urlRouteList = Util.rootUrl().trim()+Util.muvicartpayment.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id);
                httppost.addHeader("amount", Util.totalprice);
                httppost.addHeader("currency_id", Util.currencyid);
                httppost.addHeader("cart_item", Util.cartitem);
                httppost.addHeader("ship", Util.shipping);
                httppost.addHeader("coupon_code", couponCodeStr);
                httppost.addHeader("existing_card_id", cardid.trim());



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());



                } catch (org.apache.http.conn.ConnectTimeoutException e) {


                } catch (IOException e) {


                    e.printStackTrace();


                }



                JSONObject myJson = null;



                if (responseStr.trim() != null && !responseStr.trim().isEmpty() && !responseStr.trim().equals("null") && !responseStr.trim().matches("")) {

                    myJson = new JSONObject(responseStr);
                    code = myJson.getInt("code");
                }else{


                }


            } catch (Exception e) {






            }

            return null;
        }


        protected void onPostExecute(Void result) {


            if (progressBarHandler.isShowing())
                progressBarHandler.hide();



            if(responseStr!=null){

                if (code==200){

                    Toast.makeText(getApplicationContext(),"Payment suuceessful",Toast.LENGTH_LONG).show();
//                    asynemptycart = new Asynemptycart();
//                    asynemptycart.executeOnExecutor(threadPoolExecutor);
                    final Intent searchIntent = new Intent(Payment.this, ProductListingActivity.class);
                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(searchIntent);
                }



            }


        }

        @Override
        protected void onPreExecute() {

            Util.carddetails=convertJson();
            progressBarHandler = new ProgressBarHandler(Payment.this);
            progressBarHandler.show();

        }


    }




    private String convertJson() {


        JSONObject jsonObject = new JSONObject();

            JSONObject pgJson = new JSONObject();


            try {

                pgJson.put("profile_id", profileIdStr);
                pgJson.put("status", statusStr);
                pgJson.put("card_type", cardTypeStr);
                pgJson.put("card_last_fourdigit", cardLastFourDigitStr);
                pgJson.put("token", tokenStr);
                pgJson.put("response_text", "");

                jsonObject.put("card", pgJson);

            } catch (Exception e) {

                e.printStackTrace();
            }


        Log.v("json format:------",jsonObject.toString());
        return jsonObject.toString();
    }


    //Load Films Videos
    private class AsynLoadCardList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;



        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.getCardDetailsUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (cardSavedArray == null || cardSavedArray.length <= 0) {
                                creditCardSaveSpinner.setVisibility(View.GONE);
                            }

                            Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                            //    Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (cardSavedArray == null || cardSavedArray.length <= 0) {
                                creditCardSaveSpinner.setVisibility(View.GONE);
                            }
                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("cards");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            String cardIdStr = "";
                            String cardNumberStr = "";

                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("card_id")) && jsonChildNode.getString("card_id").trim() != null && !jsonChildNode.getString("card_id").trim().isEmpty() && !jsonChildNode.getString("card_id").trim().equals("null") && !jsonChildNode.getString("card_id").trim().matches("") && !jsonChildNode.getString("card_id").trim().equalsIgnoreCase("")) {

                                    cardIdStr = jsonChildNode.getString("card_id");

                                }
                                if ((jsonChildNode.has("card_last_fourdigit")) && jsonChildNode.getString("card_last_fourdigit").trim() != null && !jsonChildNode.getString("card_last_fourdigit").trim().isEmpty() && !jsonChildNode.getString("card_last_fourdigit").trim().equals("null") && !jsonChildNode.getString("card_last_fourdigit").trim().matches("") && !jsonChildNode.getString("card_id").trim().equalsIgnoreCase("")) {

                                    cardNumberStr = jsonChildNode.getString("card_last_fourdigit");

                                }
                                if (cardIdStr != null && !cardIdStr.matches("") && !cardIdStr.equalsIgnoreCase("")) {

                                    savedCards.add(new CardModel(cardIdStr, cardNumberStr));
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        creditCardSaveSpinner.setVisibility(View.GONE);

                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                creditCardSaveSpinner.setVisibility(View.GONE);

                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        creditCardSaveSpinner.setVisibility(View.GONE);

                    }
                });
            }
            return null;

        }

        protected void onPostExecute(Void result) {
            try {
                if (videoPDialog.isShowing())
                    videoPDialog.hide();
            } catch (IllegalArgumentException ex) {

                creditCardSaveSpinner.setVisibility(View.GONE);
            }
            if (responseStr == null)
                responseStr = "0";
            if ((responseStr.trim().equals("0"))) {
                try {
                    if (videoPDialog.isShowing())
                        videoPDialog.hide();
                } catch (IllegalArgumentException ex) {

                    creditCardSaveSpinner.setVisibility(View.GONE);

                }
                creditCardSaveSpinner.setVisibility(View.GONE);

            } else {
                if (savedCards.size() <= 0) {
                    try {
                        if (videoPDialog.isShowing())
                            videoPDialog.hide();
                    } catch (IllegalArgumentException ex) {

                        creditCardSaveSpinner.setVisibility(View.GONE);

                    }
                    creditCardSaveSpinner.setVisibility(View.GONE);

                } else {
                    savedCards.add(0, new CardModel("0",Util.getTextofLanguage(Payment.this,Util.USE_NEW_CARD,Util.DEFAULT_USE_NEW_CARD)));
                    cardSavedArray = savedCards.toArray(new CardModel[savedCards.size()]);
                    creditCardSaveSpinnerAdapter = new CardSpinnerAdapter(Payment.this, cardSavedArray);
                    //cardExpiryYearSpinnerAdapter = new CardSpinnerAdapter<Integer>(this, R.layout.spinner_new, yearArray);

                    creditCardSaveSpinner.setAdapter(creditCardSaveSpinnerAdapter);
                    creditCardSaveSpinner.setSelection(0);
                }
            }
        }

        @Override
        protected void onPreExecute() {

            savedCards = new ArrayList<>();
           /* videoPDialog = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            videoPDialog.setCancelable(false);
            videoPDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            videoPDialog.setIndeterminate(false);
            videoPDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            videoPDialog.show();*/

            videoPDialog = new ProgressBarHandler(Payment.this);
            videoPDialog.show();


        }


    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS

                if (s.length() <= 0) {


                    cardchargedprice.setText(currencySymbolStr+Util.grandtotal);
                    TOTALPRICE.setText(currencySymbolStr+Util.grandtotal);
                    creditCardLayout.setVisibility(View.VISIBLE);
                    payNowButton1.setVisibility(View.GONE);
                    isCouponCodeAdded = false;
                    validCouponCode = "";
                    nameOnCardEditText.setText("");
                    cardNumberEditText.setText("");
                    securityCodeEditText.setText("");
                    cardExpiryMonthSpinner.setSelection(0);
                    cardExpiryYearSpinner.setSelection(0);
                    expiryMonthStr = monthsIdArray.get(0);
                    expiryYearStr = yearArray.get(0);

                    //Util.showToast(Payment.this, Util.getTextofLanguage(Payment.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED));
                    Toast.makeText(Payment.this,Util.getTextofLanguage(Payment.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED), Toast.LENGTH_LONG).show();
                }




                    // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED), Toast.LENGTH_LONG).show();




        }


        @Override
        public void afterTextChanged(Editable s) {

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }


    };

}
