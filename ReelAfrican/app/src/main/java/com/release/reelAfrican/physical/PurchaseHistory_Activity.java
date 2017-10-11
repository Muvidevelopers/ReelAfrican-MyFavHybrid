package com.release.reelAfrican.physical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.TransactionDetailsActivity;
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

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PurchaseHistory_Activity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    RecyclerView recyclerView;
    ArrayList<PurchaseHmodel> purchaseData = new ArrayList<PurchaseHmodel>();
    RelativeLayout noInternet;
    RelativeLayout noData;
    LinearLayout primary_layout;
    Button tryAgainButton;
    boolean isNetwork;

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    String Invoice,Id,PutrcahseDate,TranactionStatus,Ppvstatus,Amount,Oderid,Currencysymbol,Currencycode,Contenttype;
    private String Currency_symbol = "";
    private String currency_code  = "";

    TextView purchaseHistoryTitleTextView,no_internet_text,noDataTextView;
    SharedPreferences pref;
    PurchaseHmodel purchaseHistoryModel;
    ArrayList<String> Id_Purchase_History;

    String emailstr,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_history);

        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            userid= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            userid= "";
        }

        noInternet = (RelativeLayout)findViewById(R.id.noInternet);
        noData = (RelativeLayout)findViewById(R.id.noData);
        primary_layout = (LinearLayout)findViewById(R.id.primary_layout);
        tryAgainButton = (Button)  findViewById(R.id.tryAgainButton);
        no_internet_text = (TextView)  findViewById(R.id.no_internet_text);
        noDataTextView = (TextView)  findViewById(R.id.noDataTextView);
        recyclerView = (RecyclerView) findViewById(R.id.purchase_history_recyclerview);
        purchaseHistoryTitleTextView = (TextView)findViewById(R.id.purchaseHistoryTitleTextView);

        no_internet_text.setText(Util.getTextofLanguage(PurchaseHistory_Activity.this, Util.NO_INTERNET_NO_DATA,Util.DEFAULT_NO_INTERNET_NO_DATA));
        tryAgainButton.setText(Util.getTextofLanguage(PurchaseHistory_Activity.this,Util.TRY_AGAIN,Util.DEFAULT_TRY_AGAIN));



        Typeface typeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        purchaseHistoryTitleTextView.setTypeface(typeface);
        purchaseHistoryTitleTextView.setText(Util.getTextofLanguage(PurchaseHistory_Activity.this,Util.PURCHASE_HISTORY,Util.DEFAULT_PURCHASE_HISTORY
        ));


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.checkNetwork(PurchaseHistory_Activity.this))
                    GetPurchaseHistoryDetails();
                else
                {
                    noInternet.setVisibility(View.VISIBLE);
                    primary_layout.setVisibility(View.GONE);
                }
            }
        });

        GetPurchaseHistoryDetails();


     /*   for(int i = 0 ;i<20 ;i++)
        {
            PurchaseHistoryModel purchaseHistoryModel = new PurchaseHistoryModel
                    ("Invoie Data","Order "+i,"12-10-20","Success","$299","Active");
            purchaseData.add(purchaseHistoryModel);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        PurchaseAdapter purchaseHistoryAdapter = new PurchaseAdapter(PurchaseHistoryActivity.this,purchaseData);
        recyclerView.setAdapter(purchaseHistoryAdapter);*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemclick(PurchaseHistory_Activity.this, new RecyclerItemclick.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click



                        if(purchaseData.get(position).getContenttype().equals("physical")){

                            Util.ORDERID=purchaseData.get(position).getOrderId();
                            Util.invoiceid=purchaseData.get(position).getId();

                            final Intent detailsIntent = new Intent(PurchaseHistory_Activity.this, OderDetailsActivity.class);
                            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(detailsIntent);


                        }else {

                            final Intent detailsIntent = new Intent(PurchaseHistory_Activity.this, TransactionDetailsActivity.class);

                            detailsIntent.putExtra("id",purchaseData.get(position).getId());
                            detailsIntent.putExtra("user_id",userid);
                            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(detailsIntent);

                        }


                    }
                })
        );

    }




    public void GetPurchaseHistoryDetails()
    {
        noInternet.setVisibility(View.GONE);
        primary_layout.setVisibility(View.VISIBLE);

        AsynGetPurchaseDetails asynGetPurchaseDetail = new AsynGetPurchaseDetails();
        asynGetPurchaseDetail.executeOnExecutor(threadPoolExecutor);
    }

    //Asyntask for getDetails of the csat and crew members.

    private class AsynGetPurchaseDetails extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;
        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.PurchaseHistory.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id",userid.trim());

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (Exception e){

                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                if (status > 0) {
                    if (status == 200) {
                        Id_Purchase_History = new ArrayList<>();
                        JSONArray jsonArray = myJson.getJSONArray("section");
                        for(int i=0 ;i<jsonArray.length();i++)
                        {

                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonArray.getJSONObject(i);



                                if ((jsonChildNode.has("invoice_id")) && jsonChildNode.getString("invoice_id").trim() != null && !jsonChildNode.getString("invoice_id").trim().isEmpty() && !jsonChildNode.getString("invoice_id").trim().equals("null") && !jsonChildNode.getString("invoice_id").trim().matches("")) {
                                    Invoice = jsonChildNode.getString("invoice_id");

                                }
                                if ((jsonChildNode.has("transaction_date")) && jsonChildNode.getString("transaction_date").trim() != null && !jsonChildNode.getString("transaction_date").trim().isEmpty() && !jsonChildNode.getString("transaction_date").trim().equals("null") && !jsonChildNode.getString("transaction_date").trim().matches("")) {
                                    PutrcahseDate = jsonChildNode.getString("transaction_date");

                                }
                                if ((jsonChildNode.has("amount")) && jsonChildNode.getString("amount").trim() != null && !jsonChildNode.getString("amount").trim().isEmpty() && !jsonChildNode.getString("amount").trim().equals("null") && !jsonChildNode.getString("amount").trim().matches("")) {
                                    Amount = jsonChildNode.getString("amount");
                                    //movieImageStr = movieImageStr.replace("episode", "original");

                                }
                                if ((jsonChildNode.has("transaction_status")) && jsonChildNode.getString("transaction_status").trim() != null && !jsonChildNode.getString("transaction_status").trim().isEmpty() && !jsonChildNode.getString("transaction_status").trim().equals("null") && !jsonChildNode.getString("transaction_status").trim().matches("")) {
                                    TranactionStatus = jsonChildNode.getString("transaction_status");

                                }
                                if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.getString("currency_symbol").trim() != null && !jsonChildNode.getString("currency_symbol").trim().isEmpty() && !jsonChildNode.getString("currency_symbol").trim().equals("null") && !jsonChildNode.getString("currency_symbol").trim().matches("")) {
                                    Currencysymbol = jsonChildNode.getString("currency_symbol");

                                }

                                if ((jsonChildNode.has("currency_code")) && jsonChildNode.getString("currency_code").trim() != null && !jsonChildNode.getString("currency_code").trim().isEmpty() && !jsonChildNode.getString("currency_code").trim().equals("null") && !jsonChildNode.getString("currency_code").trim().matches("")) {
                                    Currencycode = jsonChildNode.getString("currency_code");

                                }
                                if ((jsonChildNode.has("id")) && jsonChildNode.getString("id").trim() != null && !jsonChildNode.getString("id").trim().isEmpty() && !jsonChildNode.getString("id").trim().equals("null") && !jsonChildNode.getString("id").trim().matches("")) {
                                    Id = jsonChildNode.getString("id");

                                }
                                if ((jsonChildNode.has("order_id")) && jsonChildNode.getString("order_id").trim() != null && !jsonChildNode.getString("order_id").trim().isEmpty() && !jsonChildNode.getString("order_id").trim().equals("null") && !jsonChildNode.getString("order_id").trim().matches("")) {
                                    Oderid = jsonChildNode.getString("order_id");

                                }
                                if ((jsonChildNode.has("Content_type")) && jsonChildNode.getString("Content_type").trim() != null && !jsonChildNode.getString("Content_type").trim().isEmpty() && !jsonChildNode.getString("Content_type").trim().equals("null") && !jsonChildNode.getString("Content_type").trim().matches("")) {
                                    Contenttype = jsonChildNode.getString("Content_type");

                                }

                                purchaseHistoryModel = new PurchaseHmodel(Invoice,PutrcahseDate,Amount,TranactionStatus,Currencysymbol,Currencycode,Id,Oderid,Contenttype);
                                purchaseData.add(purchaseHistoryModel);



                            } catch (Exception e) {

                                e.printStackTrace();
                            }




                        }



                    }else{  responseStr = "0";}
                }
                else{
                    responseStr = "0";

                }
            } catch (final JSONException e1) {
                responseStr = "0";
            }
            catch (Exception e)
            {
                responseStr = "0";
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try{
                if(pDialog.isShowing())
                    pDialog.hide();
            }
            catch(IllegalArgumentException ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        primary_layout.setVisibility(View.GONE);
                        noInternet.setVisibility(View.VISIBLE);

                    }

                });
                responseStr = "0";
            }
            if(responseStr == null)
                responseStr = "0";

            if((responseStr.trim().equals("0"))){
                primary_layout.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
            }else{
                if(purchaseData.size()>0){

                    // Set the recycler adapter here.
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    PurchaseAdapter purchaseHistoryAdapter = new PurchaseAdapter(PurchaseHistory_Activity.this,purchaseData);
                    recyclerView.setAdapter(purchaseHistoryAdapter);

                }
                else{

                    primary_layout.setVisibility(View.GONE);

                    noData.setVisibility(View.VISIBLE);
                    noDataTextView.setText(Util.getTextofLanguage(PurchaseHistory_Activity.this,Util.NO,Util.DEFAULT_NO) + "  "+ Util.getTextofLanguage(PurchaseHistory_Activity.this,Util.PURCHASE_HISTORY,Util.DEFAULT_PURCHASE_HISTORY) );

                }


            }
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(PurchaseHistory_Activity.this);
            pDialog.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}
