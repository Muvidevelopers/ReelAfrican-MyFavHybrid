package com.release.reelAfrican.actvity_audio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.adapter_audio.CardSpinnerAdapter;
import com.release.reelAfrican.model_audio.CardModel;
import com.release.reelAfrican.utils.ProgressBarHandler;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class PPvPaymentInfoActivity extends ActionBarActivity {
    CardModel[] cardSavedArray;
    ProgressDialog pDialog;
    String existing_card_id = "";
    String isCheckedToSavetheCard = "1";
    private boolean isCastConnected = false;

    SharedPreferences loginPref, countryPref;

    Toolbar mActionBarToolbar;
    boolean isCouponCodeAdded = false;
    String validCouponCode;

    final String TAG = getClass().getName();
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    Spinner cardExpiryYearSpinner;
    Spinner cardExpiryMonthSpinner;
    Spinner creditCardSaveSpinner;
    Spinner spinnerCardTextView;
    private RelativeLayout creditCardLayout;
    private RelativeLayout withoutCreditCardLayout;
    private LinearLayout cardExpiryDetailsLayout;
    private CheckBox saveCardCheckbox;

    private TextView withoutCreditCardChargedPriceTextView;
    private Button nextButton;
    //private Button paywithCreditCardButton;
    //private Button paywithPaypalButton;

    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText securityCodeEditText;
    private Button scanButton;
    private Button payNowButton;

    private Button applyButton;
    private EditText couponCodeEditText;

    private TextView selectShowRadioButton;

    private TextView chargedPriceTextView;

    String cardLastFourDigitStr;
    String tokenStr;
    String cardTypeStr;
    String responseText;
    String statusStr;

    String movieStreamUniqueIdStr;
    String muviUniqueIdStr;
    String planPriceStr;
    String videoUrlStr;
    String currencyCountryCodeStr;
    String currencyIdStr;
    String currencySymbolStr;
    String videoPreview;
    String videoName = "No Name";
    int isPPV = 0;
    int isAPV = 0;
    int isConverted = 0;

    String profileIdStr;
    int expiryMonthStr = 0;
    int planIdForPaypal = 0;
    ProgressBarHandler videoPDialog;

    int expiryYearStr = 0;
    float planPrice = 0.0f;
    float chargedPrice = 0.0f;
    float previousChargedPrice = 0.0f;
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

    TextView purchaseTextView,creditCardDetailsTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppv_payment_info);

        Log.v("SUBHA","ppvpayment Activity Season Id ="+Util.selected_season_id);
        Log.v("SUBHA","ppvpatment Activity episode Id ="+Util.selected_episode_id);

        videoPreview = Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        countryPref = getSharedPreferences(Util.COUNTRY_PREF, 0);

        //Set toolbar
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(PPvPaymentInfoActivity.this);
                onBackPressed();
            }
        });
        if (pDialog == null) {
            pDialog = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

        }


        purchaseTextView = (TextView) findViewById(R.id.purchaseTextView);




        loginPref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode


        if (getIntent().getStringExtra("muviuniqueid") != null) {
            muviUniqueIdStr = getIntent().getStringExtra("muviuniqueid");
        } else {
            muviUniqueIdStr = "";
        }


        if (getIntent().getStringExtra("episodeStreamId") != null) {
            movieStreamUniqueIdStr = getIntent().getStringExtra("episodeStreamId");
        } else {
            movieStreamUniqueIdStr = "";
        }

        if (getIntent().getStringExtra("videoPreview") != null) {
            videoPreview = getIntent().getStringExtra("videoPreview");
        }

        if (getIntent().getStringExtra("showName") != null) {
            videoName = getIntent().getStringExtra("showName");
        }
        if (getIntent().getStringExtra("planIdForPaypal") != null) {
            planIdForPaypal = Integer.parseInt(getIntent().getStringExtra("planIdForPaypal"));
        }
        if (getIntent().getIntExtra("isPPV", 0) != 0) {
            isPPV = getIntent().getIntExtra("isPPV", 0);
        }
        if (getIntent().getIntExtra("isAPV", 0) != 0) {
            isAPV = getIntent().getIntExtra("isPPV", 0);
        }
        if (getIntent().getIntExtra("isConverted", 0) != 0) {
            isConverted = getIntent().getIntExtra("isPPV", 0);
        }

        if (loginPref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", null) != null) {
            String isSubscribedStr = "0";
            isSubscribedStr = loginPref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", null);
            if (isSubscribedStr.equalsIgnoreCase("1")) {
                if (getIntent().getStringExtra("planSubscribedPrice") != null) {
                    chargedPrice = Float.parseFloat(getIntent().getStringExtra("planSubscribedPrice"));
                    previousChargedPrice = Float.parseFloat(getIntent().getStringExtra("planSubscribedPrice"));
                    planPrice = Float.parseFloat(getIntent().getStringExtra("planSubscribedPrice"));
                } else {
                    chargedPrice = 0.0f;
                    previousChargedPrice = 0.0f;
                    planPrice = 0.0f;
                }
            } else {
                if (getIntent().getStringExtra("planUnSubscribedPrice") != null) {
                    chargedPrice = Float.parseFloat(getIntent().getStringExtra("planUnSubscribedPrice"));
                    previousChargedPrice = Float.parseFloat(getIntent().getStringExtra("planUnSubscribedPrice"));
                    planPrice = Float.parseFloat(getIntent().getStringExtra("planUnSubscribedPrice"));

                } else {
                    chargedPrice = 0.0f;
                    previousChargedPrice = 0.0f;
                    planPrice = 0.0f;

                }
            }


        } else {
            chargedPrice = 0.0f;
            previousChargedPrice = 0.0f;
            planPrice = 0.0f;

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

        if (getIntent().getStringExtra("currencySymbol") != null) {
            currencySymbolStr = getIntent().getStringExtra("currencySymbol");
        } else {
            currencySymbolStr = "";
        }


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


        chargedPriceTextView = (TextView) findViewById(R.id.chargeDetailsTextView);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
        cardExpiryDetailsLayout = (LinearLayout) findViewById(R.id.cardExpiryDetailsLayout);
        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);
        withoutCreditCardLayout = (RelativeLayout) findViewById(R.id.withoutPaymentLayout);
        withoutCreditCardLayout.setVisibility(View.GONE);
        withoutCreditCardChargedPriceTextView = (TextView) findViewById(R.id.withoutPaymentChargeDetailsTextView);
        nextButton = (Button) findViewById(R.id.nextButton);
        //paywithCreditCardButton = (Button) findViewById(R.id.payWithCreditCardButton);
        //paywithPaypalButton = (Button) findViewById(R.id.payWithPaypalCardButton);

        cardExpiryMonthSpinner = (Spinner) findViewById(R.id.cardExpiryMonthEditText);
        cardExpiryYearSpinner = (Spinner) findViewById(R.id.cardExpiryYearEditText);
        creditCardSaveSpinner = (Spinner) findViewById(R.id.creditCardSaveEditText);

        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setVisibility(View.GONE);
        payNowButton = (Button) findViewById(R.id.payNowButton);
        applyButton = (Button) findViewById(R.id.addCouponButton);
        selectShowRadioButton = (TextView) findViewById(R.id.showNameWithPrice);
        creditCardDetailsTitleTextView = (TextView) findViewById(R.id.creditCardDetailsTitleTextView);

        Typeface typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        applyButton.setTypeface(typeface);
        applyButton.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_APPLY, Util.DEFAULT_BUTTON_APPLY));

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        purchaseTextView.setTypeface(typeface1);
        purchaseTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.PURCHASE, Util.DEFAULT_PURCHASE));

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        creditCardDetailsTitleTextView.setTypeface(typeface2);
        creditCardDetailsTitleTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.CREDIT_CARD_DETAILS, Util.DEFAULT_CREDIT_CARD_DETAILS));

        Typeface typeface3 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        chargedPriceTextView.setTypeface(typeface3);
        selectShowRadioButton.setTypeface(typeface3);

        Typeface typeface4 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        payNowButton.setTypeface(typeface4);
        payNowButton.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_PAY_NOW, Util.DEFAULT_BUTTON_PAY_NOW));


        Typeface typeface7 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        saveCardCheckbox.setTypeface(typeface7);
        saveCardCheckbox.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this, "  "+Util.SAVE_THIS_CARD, "  "+Util.DEFAULT_SAVE_THIS_CARD));



        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 21; i++) {
            yearArray.add(year + i);

        }
        for (int i = 1; i < 13; i++) {
            monthsIdArray.add(i);
        }


        cardExpiryMonthSpinnerAdapter = new ArrayAdapter<Integer>(this,R.layout.spinner_new, monthsIdArray);
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

      /*  creditCardSaveSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                creditCardSaveSpinner.setSelection(position);
                if(position == 0)
                {
                    creditCardLayout.setVisibility(View.VISIBLE);
                }else
                {
                    creditCardLayout.setVisibility(View.INVISIBLE);
                }

            }

        });
*/

        saveCardCheckbox.setChecked(true);

        creditCardSaveSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCardSaveSpinner.setSelection(position);
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
                    //chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " +currencySymbolStr+chargedPrice);
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


        cardExpiryMonthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

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
        cardExpiryYearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {


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
        selectShowRadioButton.setText(videoName + " : " + currencySymbolStr + planPrice);
        chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " + currencySymbolStr + chargedPrice);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameOnCardEditText.setText("");
                securityCodeEditText.setText("");
                cardNumberEditText.setText("");

               /* cardExpiryMonthSpinner.setSelection(0);
                expiryMonthStr = monthsIdArray.get(0);
                cardExpiryYearSpinner.setSelection(0);
                expiryYearStr = yearArray.get(0);*/


            }
        });


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(couponCodeEditText.getWindowToken(), 0);
                String couponCodeStr = couponCodeEditText.getText().toString().trim();

                if (couponCodeStr.matches("")) {
                    Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CODE_HINT,Util.DEFAULT_COUPON_CODE_HINT));

                   // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CODE_HINT,Util.DEFAULT_COUPON_CODE_HINT), Toast.LENGTH_LONG).show();

                } else {
                    boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                    if (isNetwork == false) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                        dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                        dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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

       /* payByPaypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsynPayByPaypalDetails asynPayByPaypalDetails = new AsynPayByPaypalDetails();
                asynPayByPaypalDetails.executeOnExecutor(threadPoolExecutor);

            }
        });*/
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (creditCardSaveSpinner != null && cardSavedArray != null && cardSavedArray.length > 0 && creditCardSaveSpinner.getSelectedItemPosition() > 0) {

                    boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                    if (isNetwork == false) {
                        Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                       // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    } else {
                        if (creditCardSaveSpinner != null && cardSavedArray != null && cardSavedArray.length > 0 && creditCardSaveSpinner.getSelectedItemPosition() > 0) {
                            existing_card_id = cardSavedArray[creditCardSaveSpinner.getSelectedItemPosition()].getCardId();

                        } else {
                            existing_card_id = "";
                        }

                        AsynWithouPaymentSubscriptionRegDetails asynWithouPaymentSubscriptionRegDetails = new AsynWithouPaymentSubscriptionRegDetails();
                        asynWithouPaymentSubscriptionRegDetails.executeOnExecutor(threadPoolExecutor);
                    }

                } else {
                    String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
                    String cardNumberStr = cardNumberEditText.getText().toString().trim();
                    String securityCodeStr = securityCodeEditText.getText().toString().trim();


                    if (nameOnCardStr.matches("")) {
                        Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NAME_HINT,Util.DEFAULT_CREDIT_CARD_NAME_HINT));

                      //  Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NAME_HINT,Util.DEFAULT_CREDIT_CARD_NAME_HINT), Toast.LENGTH_LONG).show();

                    } else if (cardNumberStr.matches("")) {
                        Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NUMBER_HINT,Util.DEFAULT_CREDIT_CARD_NUMBER_HINT));

                      //  Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CREDIT_CARD_NUMBER_HINT,Util.DEFAULT_CREDIT_CARD_NUMBER_HINT), Toast.LENGTH_LONG).show();

                    } else if (securityCodeStr.matches("")) {
                        Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CVV_ALERT,Util.DEFAULT_CVV_ALERT));

                    //    Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CVV_ALERT,Util.DEFAULT_CVV_ALERT), Toast.LENGTH_LONG).show();


                    } else if (expiryMonthStr <= 0) {
//                        Toast.makeText(PPvPaymentInfoActivity.this, "Please enter expiry month", Toast.LENGTH_LONG).show();

                    } else if (expiryYearStr <= 0) {
//                        Toast.makeText(PPvPaymentInfoActivity.this, "Please enter expiry year", Toast.LENGTH_LONG).show();

                    } else {
                        boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                        if (isNetwork == false) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                            dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                            dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
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
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                if (isNetwork == false) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {

                    AsynWithouPaymentSubscriptionRegDetails asyncSubWithoutPayment = new AsynWithouPaymentSubscriptionRegDetails();
                    asyncSubWithoutPayment.executeOnExecutor(threadPoolExecutor);
                }
            }

        });


        boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
        if (isNetwork == false) {
            creditCardSaveSpinner.setVisibility(View.GONE);
            Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

           // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

        } else {
            AsynLoadCardList asynLoadCardList = new AsynLoadCardList();
            asynLoadCardList.executeOnExecutor(threadPoolExecutor);
        }
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
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            if (isCouponCodeAdded == true) {
                if (s.length() <= 0) {
                    withoutCreditCardLayout.setVisibility(View.GONE);
                    creditCardLayout.setVisibility(View.VISIBLE);

                    chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " + currencySymbolStr + previousChargedPrice);
                    isCouponCodeAdded = false;
                    validCouponCode = "";
                    nameOnCardEditText.setText("");
                    cardNumberEditText.setText("");
                    securityCodeEditText.setText("");
                    cardExpiryMonthSpinner.setSelection(0);
                    cardExpiryYearSpinner.setSelection(0);
                    expiryMonthStr = monthsIdArray.get(0);
                    ;
                    expiryYearStr = yearArray.get(0);
                    ;

                    Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED));

                   // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED), Toast.LENGTH_LONG).show();

                }
            }

        }


        @Override
        public void afterTextChanged(Editable s) {

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }


    };


    //Load Films Videos
    private class AsynLoadCardList extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        ArrayList<CardModel> savedCards = new ArrayList<CardModel>();

        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList = Util.rootUrl().trim() + Util.getCardDetailsUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                String userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                httppost.addHeader("user_id", userIdStr.trim());

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

                            Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

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
                    savedCards.add(0, new CardModel("0",Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.USE_NEW_CARD,Util.DEFAULT_USE_NEW_CARD)));
                    cardSavedArray = savedCards.toArray(new CardModel[savedCards.size()]);
                    creditCardSaveSpinnerAdapter = new CardSpinnerAdapter(PPvPaymentInfoActivity.this, cardSavedArray);
                    //cardExpiryYearSpinnerAdapter = new CardSpinnerAdapter<Integer>(this, R.layout.spinner_new, yearArray);

                    creditCardSaveSpinner.setAdapter(creditCardSaveSpinnerAdapter);
                    creditCardSaveSpinner.setSelection(0);
                }
            }
        }

        @Override
        protected void onPreExecute() {
           /* videoPDialog = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            videoPDialog.setCancelable(false);
            videoPDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            videoPDialog.setIndeterminate(false);
            videoPDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            videoPDialog.show();*/

            videoPDialog = new ProgressBarHandler(PPvPaymentInfoActivity.this);
            videoPDialog.show();


        }


    }

    //load video urls as per resolution
    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr;
        int statusCode;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.loadVideoUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
                httppost.addHeader("internet_speed", MainActivity.internetSpeed.trim());



                // Execute HTTP Post Request
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
                            responseStr = "0";
                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                            Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    statusCode = Integer.parseInt(myJson.optString("code"));
                }

                if (statusCode >= 0) {
                    if (statusCode == 200) {
                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
                                videoUrlStr = myJson.getString("videoUrl").trim();
                            }
                            else{
                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                            }
                        }else{
                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));


                            }
                            else{
                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                            }
                        }
                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));

                        }

                    }

                }
                else {

                    responseStr = "0";
                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                }
            } catch (JSONException e1) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
                e1.printStackTrace();
            }

            catch (Exception e)
            {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
                responseStr = "0";
                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));

                e.printStackTrace();

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
                responseStr = "0";
                videoUrlStr = Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
            }
            if (responseStr == null) {
                responseStr = "0";
                videoUrlStr = Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
            }

            if ((responseStr.trim().equalsIgnoreCase("0"))) {
                videoUrlStr = Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            } else {
                if (videoUrlStr == null) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    dlgAlert.create().show();
                } else if (videoUrlStr.matches("") || videoUrlStr.equalsIgnoreCase(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    dlgAlert.create().show();
                } else {
                    if (isCastConnected == true) {
                        onBackPressed();
                    } else {

//                      Modified By Bibhu
//                        final Intent playVideoIntent = new Intent(PPvPaymentInfoActivity.this, PlayerFragment.class);

                        runOnUiThread(new Runnable() {
                            public void run() {
//                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(playVideoIntent);
//                                finish();

                            }
                        });
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(PPvPaymentInfoActivity.this);
            pDialog.show();
        }
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
        ProgressDialog pDialog1;
        String responseStr;
        String statusStr;
        String couponCodeStr = couponCodeEditText.getText().toString().trim();
        int status;

        @Override
        protected Void doInBackground(Void... params) {
            String userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.couponCodeValidationUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("couponCode", couponCodeStr);
                httppost.addHeader("user_id", userIdStr.trim());
                httppost.addHeader("currencyId", currencyIdStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("SUBHA","response == "+ responseStr);

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog1.isShowing())
                                pDialog1.dismiss();
                            chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " + currencySymbolStr + chargedPrice);
                            //selectShowRadioButton.setText("Entire Show: "+currencySymbolStr+planPrice);
                            isCouponCodeAdded = false;
                            validCouponCode = "";
                            Util.showToast(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                           // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION)+"."+Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.COUPON_CANCELLED,Util.DEFAULT_COUPON_CANCELLED), Toast.LENGTH_LONG).show();
                            couponCodeEditText.setText("");


                        }

                    });

                } catch (IOException e) {
                    if (pDialog1.isShowing())
                        pDialog1.dismiss();
                    responseStr = "0";
                    isCouponCodeAdded = false;
                    validCouponCode = "";
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

                                if (discountTypeStr.equalsIgnoreCase("%")) {

                                    chargedPrice = planPrice - planPrice * (Float.parseFloat(myJson.getString("discount")) / 100);

                                    if (chargedPrice < 0.0f) {
                                        chargedPrice = 0.0f;
                                    }
                                } else {

                                    chargedPrice = planPrice - Float.parseFloat(myJson.getString("discount").trim());

                                    if (chargedPrice < 0.0f) {
                                        chargedPrice = 0.0f;
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
                if (pDialog1.isShowing())
                    pDialog1.dismiss();
                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
                e1.printStackTrace();
            } catch (Exception e) {
                if (pDialog1.isShowing())
                    pDialog1.dismiss();
                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {
            try {
                if (pDialog1.isShowing())
                    pDialog1.dismiss();
            } catch (IllegalArgumentException ex) {
                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
            }
            if (responseStr == null) {
                responseStr = "0";
                isCouponCodeAdded = false;
                validCouponCode = "";
                //couponCodeEditText.setText("");
            }
            if ((responseStr.trim().equals("0"))) {
                chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " + currencySymbolStr + planPrice);
                //selectShowRadioButton.setText("Entire Show: " + currencySymbolStr + planPrice);
                isCouponCodeAdded = false;
                validCouponCode = "";
                couponCodeEditText.setText("");

                if (statusStr.trim() != null && !statusStr.trim().isEmpty() && !statusStr.trim().equals("null") && !statusStr.trim().matches("")) {
                    Util.showToast(PPvPaymentInfoActivity.this,statusStr);

                    //Toast.makeText(PPvPaymentInfoActivity.this, statusStr, Toast.LENGTH_LONG).show();

                } else {

                    Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.INVALID_COUPON,Util.DEFAULT_INVALID_COUPON));
                  //  Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.INVALID_COUPON,Util.DEFAULT_INVALID_COUPON), Toast.LENGTH_LONG).show();

                }


            } else {
                //selectShowRadioButton.setText("Entire Show: "+currencySymbolStr+planPrice);
                creditCardLayout.setVisibility(View.VISIBLE);

                chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " + currencySymbolStr + chargedPrice);
                isCouponCodeAdded = true;
                validCouponCode = couponCodeEditText.getText().toString().trim();
                Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.DISCOUNT_ON_COUPON,Util.DEFAULT_DISCOUNT_ON_COUPON));

             //   Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.DISCOUNT_ON_COUPON,Util.DEFAULT_DISCOUNT_ON_COUPON), Toast.LENGTH_LONG).show();
                if (chargedPrice <= 0.0f && isCouponCodeAdded == true) {
                    creditCardLayout.setVisibility(View.GONE);

                    //paywithCreditCardButton.setVisibility(View.GONE);
                    withoutCreditCardLayout.setVisibility(View.VISIBLE);
                    withoutCreditCardChargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" : " + currencySymbolStr + chargedPrice);
                }
            }


        }

        @Override
        protected void onPreExecute() {
            pDialog1 = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            pDialog1.setCancelable(false);
            pDialog1.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog1.setIndeterminate(false);
            pDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            pDialog1.show();

        }


    }

    //Verify the login
    private class AsynValidateUserDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;

        int status;
        String validUserStr;
        String userMessage;
        String responseStr;
        String loggedInIdStr;
        String isSubscribedDataStr;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences pref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode
            if (pref != null) {
                loggedInIdStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                isSubscribedDataStr = pref.getString("PREFS_LOGIN_ISSUBSCRIBED_KEY", null);
            }


            String urlRouteList = Util.rootUrl().trim() + Util.userValidationUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("user_id", loggedInIdStr.trim());
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                httppost.addHeader("purchase_type", "show");

                httppost.addHeader("season_id", "0");
                httppost.addHeader("country", countryPref.getString("countryCode", null));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            status = 0;
                            Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                           // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    status = 0;
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    if (myJson.has("code")) {
                        status = Integer.parseInt(myJson.optString("code"));
                    }
                    if (myJson.has("status")) {
                        validUserStr = myJson.optString("status");
                    }
                    if (myJson.has("msg")) {
                        userMessage = myJson.optString("msg");
                    }
                }

            } catch (Exception e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
           /* try {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                status = 0;
            }*/
            if (responseStr == null) {
                try {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                hideKeyboard(PPvPaymentInfoActivity.this);
                                onBackPressed();

                            }
                        });
                dlgAlert.create().show();
            } else if (status <= 0) {
                try {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                hideKeyboard(PPvPaymentInfoActivity.this);
                                onBackPressed();
                            }
                        });
                dlgAlert.create().show();
            }
            if (status > 0) {
                if (status == 425) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                    onBackPressed();

                                }
                            });
                    dlgAlert.create().show();
                } else if (status == 426) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                    onBackPressed();
                                }
                            });
                    dlgAlert.create().show();
                } else if (status == 428) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.CROSSED_MAXIMUM_LIMIT, Util.DEFAULT_CROSSED_MAXIMUM_LIMIT));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                    onBackPressed();
                                }
                            });
                    dlgAlert.create().show();
                } else if (status == 430) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                    onBackPressed();
                                }
                            });
                    dlgAlert.create().show();
                } else if (status == 427) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, Util.DEFAULT_CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                    onBackPressed();
                                }
                            });
                    dlgAlert.create().show();
                } else if (status == 429) {
                    if (validUserStr == null) {
                        try {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (IllegalArgumentException ex) {
                            status = 0;
                        }
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                        dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                        dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        hideKeyboard(PPvPaymentInfoActivity.this);
                                        onBackPressed();
                                    }
                                });
                        dlgAlert.create().show();
                    }
                    if (validUserStr != null) {
                        boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                        if (isNetwork == false) {
                            try {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                            } catch (IllegalArgumentException ex) {
                                status = 0;
                            }
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                            dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                            dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            hideKeyboard(PPvPaymentInfoActivity.this);
                                            onBackPressed();
                                        }
                                    });
                            dlgAlert.create().show();

                        } else {
                            if ((validUserStr.trim().equalsIgnoreCase("OK")) || (validUserStr.trim().matches("OK")) || (validUserStr.trim().equals("OK"))) {
                                AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                            } else {
                                if ((userMessage.trim().equalsIgnoreCase("Unpaid")) || (userMessage.trim().matches("Unpaid")) || (userMessage.trim().equals("Unpaid"))) {
                                    if (isNetwork == false) {
                                        try {
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                        } catch (IllegalArgumentException ex) {
                                            status = 0;
                                        }
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                                        dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                                        dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                        dlgAlert.setCancelable(false);
                                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        hideKeyboard(PPvPaymentInfoActivity.this);
                                                        onBackPressed();
                                                    }
                                                });
                                        dlgAlert.create().show();

                                    } else {
                                        try {
                                            if (pDialog.isShowing())
                                                pDialog.dismiss();
                                        } catch (IllegalArgumentException ex) {
                                            status = 0;
                                        }
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                                        dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, Util.DEFAULT_ACTIVATE_SUBSCRIPTION_WATCH_VIDEO));
                                        dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                        dlgAlert.setCancelable(false);
                                        dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        hideKeyboard(PPvPaymentInfoActivity.this);
                                                        onBackPressed();
                                                    }
                                                });
                                        dlgAlert.create().show();
                                    }
                                } else {
                                    try {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                    } catch (IllegalArgumentException ex) {
                                        status = 0;
                                    }
                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.DETAILS_NOT_FOUND_ALERT, Util.DEFAULT_DETAILS_NOT_FOUND_ALERT));
                                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                    dlgAlert.setCancelable(false);
                                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    hideKeyboard(PPvPaymentInfoActivity.this);
                                                    onBackPressed();
                                                }
                                            });
                                    dlgAlert.create().show();
                                }
                            }
                        }
                    }

                }
            }

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(PPvPaymentInfoActivity.this, R.style.CustomDialogTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            pDialog.show();

        }


    }

    private class AsynPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
        //ProgressDialog pDialog;
        int status;
        String responseStr;
        String responseMessageStr;
        String emailIdStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);

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
                httppost.addHeader("email", emailIdStr);
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            status = 0;
                            Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                         //   Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    status = 0;

                    e.printStackTrace();
                }
                JSONObject myJson = null;

                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("isSuccess"));
                }
                if (status == 1) {JSONObject mainJson = null;

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
                        responseMessageStr = Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE);

                    }
                }

            } catch (Exception e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
           /* try {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                status = 0;
            }*/
            if (responseStr == null) {
                try {
                    if (videoPDialog.isShowing())
                        videoPDialog.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_PAYMENT_VALIDATION, Util.DEFAULT_ERROR_IN_PAYMENT_VALIDATION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (status == 0) {
                try {
                    if (videoPDialog.isShowing())
                        videoPDialog.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(responseMessageStr);
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.FAILURE, Util.DEFAULT_FAILURE));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            } else if (status == 1) {
                boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                if (isNetwork == false) {
                    try {
                        if (videoPDialog.isShowing())
                            videoPDialog.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    dlgAlert.create().show();

                } else {
                    AsynSubscriptionRegDetails asyncSubsrInfo = new AsynSubscriptionRegDetails();
                    asyncSubsrInfo.executeOnExecutor(threadPoolExecutor);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            videoPDialog = new ProgressBarHandler(PPvPaymentInfoActivity.this);
            videoPDialog.show();
           /* pDialog = new ProgressDialog(PPvPaymentInfoActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }


    }

    private class AsynWithouPaymentSubscriptionRegDetails extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        int status;
        String responseStr;

        @Override
        protected Void doInBackground(Void... params) {

            String userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            String emailIdSubStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);

         /*   runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(saveCardCheckbox.isChecked()){
                        isCheckedToSavetheCard = "1";
                    }else{
                        isCheckedToSavetheCard = "0";

                    }

                }

            });*/


            String urlRouteList = Util.rootUrl().trim() + Util.addSubscriptionUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                final HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                if (isAPV == 1) {
                    httppost.addHeader("is_advance", "1");
                }

                httppost.addHeader("card_name", "");
                httppost.addHeader("exp_month", "");
                httppost.addHeader("card_number", "");
                httppost.addHeader("exp_year", "");
                httppost.addHeader("email", emailIdSubStr.trim());
                httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                //httppost.addHeader("movie_id","5a07372fd347136975e3dd4c9897cf23");
                httppost.addHeader("user_id", userIdStr.trim());
                if (isCouponCodeAdded == true) {
                    httppost.addHeader("coupon_code", validCouponCode);
                } else {
                    httppost.addHeader("coupon_code", "");
                }
                httppost.addHeader("card_type", "");
                httppost.addHeader("card_last_fourdigit", "");
                httppost.addHeader("profile_id", "");
                httppost.addHeader("token", "");
                httppost.addHeader("cvv", "");
                // httppost.addHeader("country","US");

                httppost.addHeader("country", countryPref.getString("countryCode", null));
                //*********************************//

//                httppost.addHeader("season_id", "0");
//                httppost.addHeader("episode_id", "0");
                httppost.addHeader("season_id",Util.selected_season_id);
                httppost.addHeader("episode_id",Util.selected_episode_id);


                Log.v("SUBHA","season_id====================="+Util.selected_season_id);
                Log.v("SUBHA","episode_id====================="+Util.selected_episode_id);

                httppost.addHeader("currency_id", currencyIdStr.trim());

                httppost.addHeader("is_save_this_card", isCheckedToSavetheCard.trim());
                if (existing_card_id != null && !existing_card_id.matches("") && !existing_card_id.equalsIgnoreCase("")) {
                    httppost.addHeader("existing_card_id", existing_card_id);
                } else {
                    httppost.addHeader("existing_card_id", "");
                }

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (creditCardSaveSpinner!=null && cardSavedArray!=null && cardSavedArray.length > 0 && creditCardSaveSpinner.getSelectedItemPosition() > 0){
                            String  existing_card_id = cardSavedArray[creditCardSaveSpinner.getSelectedItemPosition()].getCardId();
                            httppost.addHeader("existing_card_id", existing_card_id);

                        }

                    }

                });

*/

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            status = 0;
                            Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                          //  Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (IllegalArgumentException ex) {
                        status = 0;

                        e.printStackTrace();
                    }
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));

                }

            } catch (Exception e) {
                try {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }

            }

            return null;
        }


        protected void onPostExecute(Void result) {
            try {
                if (videoPDialog.isShowing())
                    videoPDialog.hide();
            } catch (IllegalArgumentException ex) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (responseStr == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (status == 0) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.FAILURE, Util.DEFAULT_FAILURE));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (status > 0) {

                if (status == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                            if (isNetwork == false) {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                dlgAlert.create().show();

                            } else {
                                if (isAPV == 1) {
                                    Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.PURCHASE_SUCCESS_ALERT,Util.DEFAULT_PURCHASE_SUCCESS_ALERT));

                                   // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.PURCHASE_SUCCESS_ALERT,Util.DEFAULT_PURCHASE_SUCCESS_ALERT), Toast.LENGTH_LONG).show();
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    if (isCastConnected == true) {
                                        onBackPressed();

                                    } else {
                                        AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                    }
                                }
                               /* final Intent playVideoIntent = new Intent(PPvPaymentInfoActivity.this, PlayVideoActivity.class);
                                playVideoIntent.putExtra("activity", "generic");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        playVideoIntent.putExtra("url", videoUrlStr.trim());
                                        startActivity(playVideoIntent);
                                        finish();
                                    }
                                });*/
                            }
                        }
                    });


                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    dlgAlert.create().show();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            videoPDialog = new ProgressBarHandler(PPvPaymentInfoActivity.this);
            videoPDialog.show();

        }


    }

    private class AsynSubscriptionRegDetails extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pDialog;
        int status;
        String responseStr;
        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
        String cardNumberStr = cardNumberEditText.getText().toString().trim();
        String securityCardStr = securityCodeEditText.getText().toString().trim();

        @Override
        protected Void doInBackground(Void... params) {

            String userIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            String emailIdSubStr = loginPref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(saveCardCheckbox.isChecked()){
                        isCheckedToSavetheCard = "1";
                        Toast.makeText(PPvPaymentInfoActivity.this,"Data saved",Toast.LENGTH_SHORT).show();

                    }else{
                        isCheckedToSavetheCard = "0";
                        Toast.makeText(PPvPaymentInfoActivity.this,"Data Not saved",Toast.LENGTH_SHORT).show();


                    }

                }

            });*/
            String urlRouteList = Util.rootUrl().trim() + Util.addSubscriptionUrl.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                httppost.addHeader("card_name", nameOnCardStr);
                httppost.addHeader("exp_month", String.valueOf(expiryMonthStr).trim());
                httppost.addHeader("card_number", cardNumberStr);
                httppost.addHeader("exp_year", String.valueOf(expiryYearStr).trim());
                httppost.addHeader("email", emailIdSubStr.trim());
                httppost.addHeader("movie_id", muviUniqueIdStr.trim());
                httppost.addHeader("user_id", userIdStr.trim());


                if (isCouponCodeAdded == true) {

                    httppost.addHeader("coupon_code", validCouponCode);
                } else {

                    httppost.addHeader("coupon_code", "");
                }

                httppost.addHeader("card_type", cardTypeStr.trim());
                httppost.addHeader("card_last_fourdigit", cardLastFourDigitStr.trim());
                httppost.addHeader("profile_id", profileIdStr.trim());
                httppost.addHeader("token", tokenStr.trim());
                httppost.addHeader("cvv", securityCardStr);
                // httppost.addHeader("country",currencyCountryCodeStr.trim());

                httppost.addHeader("country", countryPref.getString("countryCode", null));
                //*********************************// ((Global) getApplicationContext()).getCountryCode()
//                httppost.addHeader("season_id", "0");
//                httppost.addHeader("episode_id", "0");
                httppost.addHeader("season_id",Util.selected_season_id);
                httppost.addHeader("episode_id",Util.selected_episode_id);





                if (isAPV == 1) {
                    httppost.addHeader("is_advance", "1");
                }
                httppost.addHeader("currency_id", currencyIdStr.trim());

                httppost.addHeader("is_save_this_card", isCheckedToSavetheCard.trim());


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());



                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            status = 0;
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                           // Toast.makeText(PPvPaymentInfoActivity.this, Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {

                    status = 0;
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    e.printStackTrace();
                }
                if (responseStr != null) {
                    JSONObject myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));

                }

            } catch (Exception e) {

                if (pDialog.isShowing())
                    pDialog.dismiss();
                status = 0;

            }

            return null;
        }


        protected void onPostExecute(Void result) {
           /* try {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            } catch (IllegalArgumentException ex) {
               status = 0;
            }*/
            if (responseStr == null) {
                try {
                    if (videoPDialog.isShowing())
                        videoPDialog.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (status == 0) {
                try {
                    if (videoPDialog.isShowing())
                        videoPDialog.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                dlgAlert.create().show();
            }
            if (status > 0) {

                if (status == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            boolean isNetwork = Util.checkNetwork(PPvPaymentInfoActivity.this);
                            if (isNetwork == false) {
                                try {
                                    if (videoPDialog.isShowing())
                                        videoPDialog.hide();
                                } catch (IllegalArgumentException ex) {
                                    status = 0;
                                }
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                                dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
                                dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                                dlgAlert.setCancelable(false);
                                dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                dlgAlert.create().show();

                            } else {
                                try {
                                    if (videoPDialog.isShowing())
                                        videoPDialog.hide();
                                } catch (IllegalArgumentException ex) {
                                    status = 0;
                                }
                                if (isAPV == 1) {
                                    Util.showToast(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.PURCHASE_SUCCESS_ALERT,Util.DEFAULT_PURCHASE_SUCCESS_ALERT));

                                   // Toast.makeText(PPvPaymentInfoActivity.this,Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.PURCHASE_SUCCESS_ALERT,Util.DEFAULT_PURCHASE_SUCCESS_ALERT), Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    if (isCastConnected == true) {
                                        onBackPressed();

                                    } else {
                                        AsynLoadVideoUrls asynLoadVideoUrls = new AsynLoadVideoUrls();
                                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
                                    }
                                }
                            }

                        }
                    });


                } else {
                    try {
                        if (videoPDialog.isShowing())
                        videoPDialog.hide();
                    } catch (IllegalArgumentException ex) {
                        status = 0;
                    }
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PPvPaymentInfoActivity.this);
                    dlgAlert.setMessage(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
                    dlgAlert.setTitle(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PPvPaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    dlgAlert.create().show();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            videoPDialog = new ProgressBarHandler(PPvPaymentInfoActivity.this);
            videoPDialog.show();
           /* pDialog = new ProgressDialog(PPvPaymentInfoActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading_str));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }




}
