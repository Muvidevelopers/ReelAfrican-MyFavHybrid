package com.release.reelAfrican.actvity_audio;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PurchaseHistoryDetailsActivity extends AppCompatActivity {

    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ProgressDialog progressDialog;
    ProgressBarHandler pDialog;

    Toolbar mActionBarToolbar;

    String id, user_id,ppvstatus;
    String TransactionDate, OredrId, Amount, Invoice, TransactionStatus, PlanName, Currency_symbol,currency_code;
    String Download_Url,filename;
    boolean deletion_success = false;
    static File mediaStorageDir;
    int progress_bar_type = 0;
    int progressStatus = 0;
    AlertDialog msgAlert;

    LinearLayout transactionHistoryDetailRowLayout;
    Button transactionDownloadButton;
    CardView puchase_historyDetailsCardview;
    TextView showPricesimbolTextView,showPriceTextView,successTextView,invoicenumber,ordernoTextView,planNameTextView,transactionDateTextView,activeTextView,activeIconTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history_details);

        transactionHistoryDetailRowLayout=(LinearLayout)findViewById(R.id.transactionHistoryDetailRowLayout);

        showPricesimbolTextView=(TextView)findViewById(R.id.showPricesimbolTextView);
        showPriceTextView=(TextView)findViewById(R.id.showPriceTextView);
        successTextView=(TextView)findViewById(R.id.successTextView);
        invoicenumber=(TextView)findViewById(R.id.invoicenumber);
        planNameTextView=(TextView)findViewById(R.id.planNameTextView);
        transactionDateTextView=(TextView)findViewById(R.id.transactionDateTextView);
        activeTextView=(TextView)findViewById(R.id.activeTextView);
        activeIconTextView=(TextView)findViewById(R.id.activeIconTextView);
        ordernoTextView=(TextView)findViewById(R.id.ordernoTextView);

        transactionDownloadButton=(Button)findViewById(R.id.transactionDownloadButton);
        transactionDownloadButton.setVisibility(View.GONE);

        puchase_historyDetailsCardview=(CardView)findViewById(R.id.puchase_historyDetailsCardview);
        puchase_historyDetailsCardview.setVisibility(View.GONE);

        id = getIntent().getStringExtra("id");
        user_id = getIntent().getStringExtra("user_id");
        ppvstatus = getIntent().getStringExtra("ppvstatus");

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //////////////
        /*transactionHistoryDetailRowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PurchaseHistoryDetailsActivity.this,SubsriptionPlanList.class);
                startActivity(intent);
            }
        });*/
/////////////////////////////////////////


        //////////////Transaction Details/////////////////////////////////
        AsynGetTransactionDetails asynGetTransactionDetails = new AsynGetTransactionDetails();
        asynGetTransactionDetails.executeOnExecutor(threadPoolExecutor);


        transactionDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(PurchaseHistoryDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PurchaseHistoryDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(PurchaseHistoryDetailsActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                111);
                    } else {
                        ActivityCompat.requestPermissions(PurchaseHistoryDetailsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                111);
                    }
                }else {
                    //Call whatever you want

                    if (Util.checkNetwork(PurchaseHistoryDetailsActivity.this)) {


                        /////////////PDF download API call
                        DownloadDocumentDetails downloadDocumentDetails = new DownloadDocumentDetails();
                        downloadDocumentDetails.executeOnExecutor(threadPoolExecutor);

                        Toast.makeText(PurchaseHistoryDetailsActivity.this, "downloading..", Toast.LENGTH_SHORT).show();

                    } else {
                        Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

                    }
                }
            }
        });



    }


    ///////////AsynGetTransactionDetails////////////////////
    private class AsynGetTransactionDetails extends AsyncTask<Void, Void, Void> {

        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.PurchaseHistoryDetails.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("user_id", user_id);
                httppost.addHeader("id", id);
                httppost.addHeader("lang_code", Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("SUBHA", "responseStr transcation Details=" + responseStr);
                } catch (Exception e) {

                }

                JSONObject myJson = null;
                JSONObject myJson1 = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    myJson1 = myJson.getJSONObject("section");

//
                }
                if(status>0){

                    if(status==200){

                        OredrId=myJson1.optString("order_number");
                        if (OredrId.equals("") || OredrId == null || OredrId.equals("null"))
                            OredrId = "";

                        Invoice = myJson1.optString("invoice_id");
                        if (Invoice.equals("") || Invoice == null || Invoice.equals("null"))
                            Invoice = "";

                        TransactionDate = myJson1.optString("transaction_date");
                        if (TransactionDate.equals("") || TransactionDate == null || TransactionDate.equals("null"))
                            TransactionDate = "";

                        if(!TransactionDate.equals(""))
                        {
                            String date=TransactionDate.trim();
                            SimpleDateFormat spf=new SimpleDateFormat("MMMM dd,yyyy hh:mm:ss aaa");
                            Date newDate=spf.parse(date);
                            spf= new SimpleDateFormat("MMMM dd,yyyy");
                            date = spf.format(newDate);
                            Log.v("SUBHA","Transaction Date = "+date);
                        }


                        TransactionStatus = myJson1.optString("transaction_status");
                        if (TransactionStatus.equals("") || TransactionStatus == null || TransactionStatus.equals("null"))
                            TransactionStatus = "";

                        PlanName = myJson1.optString("plan_name");
                        if (PlanName.equals("") || PlanName == null || PlanName.equals("null"))
                            PlanName = "";

                        Currency_symbol = myJson1.optString("currency_symbol");
                        if (Currency_symbol.equals("") || Currency_symbol == null || Currency_symbol.equals("null"))
                            Currency_symbol = "";

                        Log.v("SUBHA", "currency_symbol = " + Currency_symbol);

                        currency_code = myJson1.optString("currency_code");
                        if (currency_code.equals("") || currency_code == null || currency_code.equals("null"))
                            currency_code = "";

                        Log.v("SUBHA", "currency_code = " + currency_code);


                        Amount = myJson1.optString("amount");
                        if (Amount.equals("") || Amount == null || Amount.equals("null"))
                            Amount = "";


                    }
                }

            }catch (final JSONException e1) {
                responseStr = "0";
            } catch (Exception e) {
                responseStr = "0";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(PurchaseHistoryDetailsActivity.this);
            pDialog.show();
        }

        protected void onPostExecute(Void result){
            if(pDialog.isShowing() && pDialog!=null)
                pDialog.hide();

            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))){

                ////////////No data condition
                Toast.makeText(PurchaseHistoryDetailsActivity.this, "No data", Toast.LENGTH_SHORT).show();
                if(pDialog.isShowing() && pDialog!=null)
                    pDialog.hide();
            }

            else{
                puchase_historyDetailsCardview.setVisibility(View.VISIBLE);
                transactionDownloadButton.setVisibility(View.VISIBLE);
                showPricesimbolTextView.setText(Currency_symbol);
                showPriceTextView.setText(Amount);
                successTextView.setText(TransactionStatus.substring(0,1).toUpperCase()+TransactionStatus.substring(1));
                planNameTextView.setText(PlanName);
                invoicenumber.setText(Invoice);
                transactionDateTextView.setText(TransactionDate);
                ordernoTextView.setText(OredrId);


                Log.v("pratikp","ppv s="+ppvstatus);
                if(ppvstatus.contains("Active") || ppvstatus.contains("active")){
                    activeTextView.setTextColor(Color.parseColor("#197b30"));
                    activeIconTextView.setBackgroundResource(R.drawable.bg_circle_green);

                }
                else if(ppvstatus.contains("N/A")){
                    activeTextView.setText("Expired");
                    activeIconTextView.setBackgroundResource(R.drawable.bg_circle_red);
                }

            }


        }
    }


    ///////PDF download Asyntask
    private class DownloadDocumentDetails extends AsyncTask<Void, Void, Void> {

        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.GetInvoicePDF.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("user_id", user_id);
                httppost.addHeader("id", id);
                httppost.addHeader("device_type", "app");
                httppost.addHeader("lang_code", Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("pratikp", "responseStr getpdf Details=" + responseStr);
                } catch (Exception e) {

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                if (status > 0) {
                    if (status == 200) {


                        Download_Url = myJson.optString("section");
                        if (Download_Url.equals("") || Download_Url == null || Download_Url.equals("null")) {

                            Download_Url = "";
                        }
                    } else {
                        responseStr = "0";
                    }
                } else {
                    responseStr = "0";

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                responseStr = "0";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressBarHandler(PurchaseHistoryDetailsActivity.this);
            pDialog.show();
        }

        protected void onPostExecute(Void result){

            if(pDialog.isShowing() && pDialog!=null)
                pDialog.hide();

            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))) {
                /////////No data condition
                Toast.makeText(PurchaseHistoryDetailsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
            else {

                if (!Download_Url.equals("")) {

                    registerReceiver(InternetStatus, new IntentFilter("android.net.wifi.STATE_CHANGE"));
                    new DownloadFileFromURL().execute(Util.Dwonload_pdf_rootUrl+Download_Url);
                }
                else
                    Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_PDF,Util.DEFAULT_NO_PDF));

            }
        }
    }


    ////////////DownloadFileFromURL API
    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)

        @Override
        protected String doInBackground(String... f_url) {

            int count;

            try {
                URL url = new URL(f_url[0]);
                String str = f_url[0];
                filename = str.substring(str.lastIndexOf("/") + 1);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("pratikp", "failed to create directory");
                    }
                }

                ///////output stream
                OutputStream output = new FileOutputStream(mediaStorageDir + "/" + filename);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // Log.v("SUBHA", "Lrngth" + data.length);
                    // writing data to file
                    output.write(data, 0, count);
                }


                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));

            if ((Integer.parseInt(progress[0])) == 100) {

                showDialog(Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this,Util.DOWNLOAD_COMPLETED,Util.DEFAULT_DOWNLOAD_COMPLETED), 1);

                unregisterReceiver(InternetStatus);
                progressDialog.setProgress(0);
                progressStatus = 0;
                dismissDialog(progress_bar_type);

                //Calling Api To Delete Pdf file from the Server.


            }

        }
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            if(progressDialog.isShowing() && progressDialog!=null)
                progressDialog.dismiss();

            if(pDialog.isShowing() && pDialog!=null)
                pDialog.hide();

            Log.v("pratikp","Download Completed");

        }


    }


    public void showDialog(String msg, final int deletevalue) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PurchaseHistoryDetailsActivity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                        if (deletevalue == 1) {
                            Deletepdf deletepdf = new Deletepdf();
                            deletepdf.executeOnExecutor(threadPoolExecutor);
                            Log.v("pratikp","deletevalue=1");
                        }
                        else{
                            showPdf();
                        }
                    }
                });
        dlgAlert.create();
        msgAlert = dlgAlert.show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0: // we set this to 0
                progressDialog = new ProgressDialog(PurchaseHistoryDetailsActivity.this);
                progressDialog.setMessage("Downloading file. Please wait...");
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                pDialog.show();
                progressStatus = 0;
                return progressDialog;
            default:
                return null;
        }
    }

    private BroadcastReceiver InternetStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            //  Toast.makeText(getApplicationContext(),""+Util.checkNetwork(TransactionDetailsActivity.this),Toast.LENGTH_SHORT).show();
            if (!Util.checkNetwork(PurchaseHistoryDetailsActivity.this)) {
                if (progressDialog.isShowing() && pDialog != null) {

                    showDialog(Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);
                    unregisterReceiver(InternetStatus);
                    progressDialog.setProgress(0);
                    progressStatus = 0;
                    dismissDialog(progress_bar_type);
                }
            }
        }
    };

    public void showPdf(){

        File file = new File(mediaStorageDir+ "/" + filename);
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }

    private class Deletepdf extends AsyncTask<Void, Void, Void> {
        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.DeleteInvoicePath.trim());//hv to cahnge
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("filepath", Download_Url);
                httppost.addHeader("lang_code", Util.getTextofLanguage(PurchaseHistoryDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("SUBHA", "responseStr Delete Invoice Path=" + responseStr);
                } catch (Exception e) {

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                if (status > 0) {
                    if (status == 200) {

                        deletion_success = true;

                    } else {
                        deletion_success = false;
                    }
                } else {
                    deletion_success = false;

                }

            }catch (final JSONException e1) {
                deletion_success = false;
            } catch (Exception e) {
                deletion_success = false;
            }
            return null;
        }

        protected void onPostExecute(Void result){

            try {
                if (pDialog.isShowing())
                    pDialog.hide();
            } catch (IllegalArgumentException ex) {

                deletion_success = false;
            }

            if (responseStr == null)
                deletion_success = false;

            if (deletion_success) {
                // Do whatever u want to do
                showPdf();
                finish();
            }
        }


        @Override
        protected void onPreExecute(){

            pDialog = new ProgressBarHandler(PurchaseHistoryDetailsActivity.this);
            pDialog.show();
        }
    }

}
