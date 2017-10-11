package com.release.reelAfrican.physical;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.LoginActivity;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OderDetailsActivity extends AppCompatActivity {


    Context context;
    ListView list;
    RelativeLayout nodata;
    OrderDetailsAdapter adapter;
    SharedPreferences pref;
    LinearLayout linearLayout1;
    ArrayList<OrderDetailsModel> order_item_list;
    Toolbar mActionBarToolbar;
    TextView total;
    String emailstr,userid;
    TextView ship_oderid,shipname,shipaddress,shipcity,shipcountry,shipphoneno;
    AsynLoadOrderDetails asynLoadOrderDetails;
    private ProgressBarHandler videoPDialog;
    Button transactionDownloadButton;
    String Download_Url;
    ProgressDialog pDialog;
    String filename;
    int progress_bar_type = 0;
    int progressStatus = 0;
    static File mediaStorageDir;
    AlertDialog msgAlert;
    boolean deletion_success = false;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ProgressDialog mProgressDialog;
    int downloadedSize = 0, totalsize;
    float per = 0;
    Uri path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_details);

        order_item_list=new ArrayList<>();
        pref = getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){

            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            userid= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {

            emailstr = "";
            userid= "";

        }

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("Order History");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        list = (ListView) findViewById(R.id.listView);
        linearLayout1 = (LinearLayout) findViewById(R.id.ll_sublayout);
        nodata = (RelativeLayout) findViewById(R.id.noData);
        total = (TextView) findViewById(R.id.total);

        ship_oderid = (TextView) findViewById(R.id.ship_oderid);
        shipname = (TextView) findViewById(R.id.ship_name);
        shipaddress = (TextView) findViewById(R.id.ship_address);
        shipcity = (TextView) findViewById(R.id.ship_city);
        shipcountry = (TextView) findViewById(R.id.ship_country);
        shipphoneno = (TextView) findViewById(R.id.ship_phoneno);


        asynLoadOrderDetails = new AsynLoadOrderDetails();
        asynLoadOrderDetails.execute();

        list.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        transactionDownloadButton = (Button) findViewById(R.id.transactionDownloadButton);
        Typeface typeface7 = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        transactionDownloadButton.setTypeface(typeface7);
        transactionDownloadButton.setText(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_BUTTON_TITLE,Util.DEFAULT_DOWNLOAD_BUTTON_TITLE));


        transactionDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (ContextCompat.checkSelfPermission(OderDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(OderDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(OderDetailsActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                111);
                    } else {
                        ActivityCompat.requestPermissions(OderDetailsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                111);
                    }
                } else {
                    //Call whatever you want
                    if (Util.checkNetwork(OderDetailsActivity.this)) {


                        DownloadDocumentDetails downloadDocumentDetails = new DownloadDocumentDetails();
                        downloadDocumentDetails.executeOnExecutor(threadPoolExecutor);



                        // Toast.makeText(getApplicationContext(),Util.getTextofLanguage(TransactionDetailsActivity.this,Util.NO_PDF,Util.DEFAULT_NO_PDF), Toast.LENGTH_LONG).show();
                    } else {
                        Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

//                        Toast.makeText(getApplicationContext(),Util.getTextofLanguage(TransactionDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }




            }
        });


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    //Asyntask to get Transaction Details.

    private class DownloadDocumentDetails extends AsyncTask<Void, Void, Void> {

        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.GetInvoicePDF.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("user_id", userid.trim());
                httppost.addHeader("id", Util.invoiceid.trim());
                httppost.addHeader("device_type", "app");
                httppost.addHeader("lang_code",Util.getTextofLanguage(OderDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA", "responseStr getpdf Details=" + responseStr);
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
            } catch (final JSONException e1) {
                responseStr = "0";
            } catch (Exception e) {
                responseStr = "0";
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try {
                if (videoPDialog.isShowing())
                    videoPDialog.hide();
            } catch (IllegalArgumentException ex) {

                responseStr = "0";
            }
            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))) {
//                primary_layout.setVisibility(View.GONE);
//                noInternet.setVisibility(View.VISIBLE);
            } else {

                if (!Download_Url.equals(""))
                    DownloadTransactionDetails();
                else
                    Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_PDF,Util.DEFAULT_NO_PDF));

            }
        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(OderDetailsActivity.this);
            videoPDialog.show();

            /*progressDialog = new ProgressDialog(TransactionDetailsActivity.this,R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog.setIndeterminate(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            progressDialog.show()*/
            ;
        }
    }

    public void DownloadTransactionDetails() {

        //registerReceiver(InternetStatus, new IntentFilter("android.net.wifi.STATE_CHANGE"));
//        new DownloadFileFromURL().execute(Util.Dwonload_pdf_rootUrl+Download_Url);
//
//        Log.v("SUBHA","Url="+Util.Dwonload_pdf_rootUrl+Download_Url);



        runOnUiThread(new Runnable() {
            public void run() {
                mProgressDialog = new ProgressDialog(OderDetailsActivity.this);
                // Set your progress dialog Message
                mProgressDialog.setMessage("Downloading, Please Wait!");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();
            }
        });

        downloadAndOpenPDF();


    }



    private BroadcastReceiver InternetStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            //  Toast.makeText(getApplicationContext(),""+Util.checkNetwork(TransactionDetailsActivity.this),Toast.LENGTH_SHORT).show();
            if (!Util.checkNetwork(OderDetailsActivity.this)) {
                if (pDialog.isShowing() && pDialog != null) {

                    showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);
                    unregisterReceiver(InternetStatus);
                    pDialog.setProgress(0);
                    progressStatus = 0;
                    dismissDialog(progress_bar_type);
                }
            }
        }
    };


    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {



                path = Uri.fromFile(downloadFile(Util.Dwonload_pdf_rootUrl+Download_Url));
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(path, "application/pdf");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    //finish();
//                } catch (ActivityNotFoundException e) {
////                    tv_loading
////                            .setError("PDF Reader application is not installed in your device");
//                }
            }
        }).start();

    }


    File downloadFile(String dwnload_file_path){
        File file = null;
        try {


            URL url = new URL(dwnload_file_path);
            Download_Url = dwnload_file_path.substring(dwnload_file_path.lastIndexOf("/") + 1);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, Download_Url);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            //setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
//                setText("Total PDF File size  : "
//                        + (totalsize / 1024)
//                        + " KB\n\nDownloading PDF " + (int) per
//                        + "% complete");
                mProgressDialog.setProgress((int) per);
            }
            // close the output stream when complete //
            fileOutput.close();
            //setText("Download Complete. Open PDF Application installed in the device.");
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog.dismiss(); // if you want close it..

                    showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_COMPLETED,Util.DEFAULT_DOWNLOAD_COMPLETED), 1);
                }
            });
        } catch (final MalformedURLException e) {
//            setTextError("Some error occured. Press back and try again.",
//                    Color.RED);
            showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);

        } catch (final IOException e) {
//            setTextError("Some error occured. Press back and try again.",
//                    Color.RED);
            showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);

        } catch (final Exception e) {

            showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);
//            setTextError(
//                    "Failed to download image. Please check your internet connection.",
//                    Color.RED);
        }
        return file;
    }


    public void showDialog(String msg, final int deletevalue) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(OderDetailsActivity.this,R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(OderDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(Util.getTextofLanguage(OderDetailsActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (deletevalue == 1) {
                            Deletepdf deletepdf = new Deletepdf();
                            deletepdf.executeOnExecutor(threadPoolExecutor);
                        }
                    }
                });
        dlgAlert.create();
        msgAlert = dlgAlert.show();
    }


    //Asyntask to get Transaction Details.

    private class Deletepdf extends AsyncTask<Void, Void, Void> {

        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.DeleteInvoicePath.trim());//hv to cahnge
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("filepath", Download_Url);
                httppost.addHeader("lang_code",Util.getTextofLanguage(OderDetailsActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                // Execute HTTP Post Request
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
            } catch (final JSONException e1) {
                deletion_success = false;
            } catch (Exception e) {
                deletion_success = false;
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            try {
                if (videoPDialog.isShowing())
                    videoPDialog.hide();
            } catch (IllegalArgumentException ex) {

                deletion_success = false;
            }
            if (responseStr == null)
                deletion_success = false;

            if (deletion_success) {
                // Do whatever u want to do
                finish();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                } catch (ActivityNotFoundException e) {
//                    tv_loading
//                            .setError("PDF Reader application is not installed in your device");
                }
            }
        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(OderDetailsActivity.this);
            videoPDialog.show();

        }
    }




    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
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
                        Log.d("App", "failed to create directory");
                    }
                }
                // Output stream


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
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));

            if ((Integer.parseInt(progress[0])) == 100) {

                showDialog(Util.getTextofLanguage(OderDetailsActivity.this,Util.DOWNLOAD_COMPLETED,Util.DEFAULT_DOWNLOAD_COMPLETED), 1);

                unregisterReceiver(InternetStatus);
                pDialog.setProgress(0);
                progressStatus = 0;
                dismissDialog(progress_bar_type);

                //Calling Api To Delete Pdf file from the Server.


            }

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            Log.v("SUBHA","Download Completed");

        }
    }


    private class AsynLoadOrderDetails extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String oderid="";
        String pname="";
        String price="";
        int quanty;

        String shipoderid="";
        String ship_name="";
        String address="";
        String city="";
        String country="";
        String phoneno="";
        String poster="";
        String currencysymbol="";


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+Util.oderdetails.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", userid.trim());
                httppost.addHeader("orderid", Util.ORDERID.trim());


                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    //Log.v("san",responseStr.toString());


                } catch (org.apache.http.conn.ConnectTimeoutException e){

                }catch (IOException e) {

                    e.printStackTrace();
                }


            }
            catch (Exception e)
            {


                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            JSONObject myJson =null;


            if (videoPDialog != null && videoPDialog.isShowing()) {
                videoPDialog.hide();
                videoPDialog = null;
            }

            if(responseStr!=null){
                try {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (status == 200) {


                JSONArray oderdetails = null;
                try {
                    oderdetails = myJson.getJSONArray("order_details");
                    int lengthJsonArr = oderdetails.length();

                for(int i=0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {


                        jsonChildNode = oderdetails.getJSONObject(i);



                        if ((jsonChildNode.has("order_id")) && jsonChildNode.getString("order_id").trim() != null && !jsonChildNode.getString("order_id").trim().isEmpty() && !jsonChildNode.getString("order_id").trim().equals("null") && !jsonChildNode.getString("order_id").trim().matches("")) {

                            oderid = jsonChildNode.getString("order_id");
                        }


                        if ((jsonChildNode.has("quantity")) && jsonChildNode.getString("quantity").trim() != null && !jsonChildNode.getString("quantity").trim().isEmpty() && !jsonChildNode.getString("quantity").trim().equals("null") && !jsonChildNode.getString("quantity").trim().matches("")) {

                            quanty = jsonChildNode.getInt("quantity");

                        }

                        if ((jsonChildNode.has("price")) && jsonChildNode.getString("price").trim() != null && !jsonChildNode.getString("price").trim().isEmpty() && !jsonChildNode.getString("price").trim().equals("null") && !jsonChildNode.getString("price").trim().matches("")) {

                            price = jsonChildNode.getString("price");

                        }

                        if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {

                            pname = jsonChildNode.getString("name");

                        }

                        if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.getString("currency_symbol").trim() != null && !jsonChildNode.getString("currency_symbol").trim().isEmpty() && !jsonChildNode.getString("currency_symbol").trim().equals("null") && !jsonChildNode.getString("currency_symbol").trim().matches("")) {

                            currencysymbol = jsonChildNode.getString("currency_symbol");

                        }


                        if ((jsonChildNode.has("poster")) && jsonChildNode.getString("poster").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster").trim().equals("null") && !jsonChildNode.getString("poster").trim().matches("")) {
                            poster = jsonChildNode.getString("poster");

                        }


                        OrderDetailsModel orderDetailsModel=new OrderDetailsModel();
                        orderDetailsModel.setOderid(oderid);
                        orderDetailsModel.setQty(quanty);
                        orderDetailsModel.setPrice(price);
                        orderDetailsModel.setPName(pname);
                        orderDetailsModel.setCurrencysymbol(currencysymbol);
                        orderDetailsModel.setPoster(poster);

                        order_item_list.add(orderDetailsModel);



                    } catch (Exception e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                    JSONObject shipdetails = myJson.getJSONObject("shipping");


                    if ((shipdetails.has("order_id")) && shipdetails.getString("order_id").trim() != null && !shipdetails.getString("order_id").trim().isEmpty() && !shipdetails.getString("order_id").trim().equals("null") && !shipdetails.getString("order_id").trim().matches("")) {

                        shipoderid = shipdetails.getString("order_id");
                    }


                    if ((shipdetails.has("first_name")) && shipdetails.getString("first_name").trim() != null && !shipdetails.getString("first_name").trim().isEmpty() && !shipdetails.getString("first_name").trim().equals("null") && !shipdetails.getString("first_name").trim().matches("")) {

                        ship_name = shipdetails.getString("first_name");
                    }

                    if ((shipdetails.has("address")) && shipdetails.getString("address").trim() != null && !shipdetails.getString("address").trim().isEmpty() && !shipdetails.getString("address").trim().equals("null") && !shipdetails.getString("address").trim().matches("")) {

                        address = shipdetails.getString("address");
                    }

                    if ((shipdetails.has("city")) && shipdetails.getString("city").trim() != null && !shipdetails.getString("city").trim().isEmpty() && !shipdetails.getString("city").trim().equals("null") && !shipdetails.getString("city").trim().matches("")) {

                        city = shipdetails.getString("city");
                    }

                    if ((shipdetails.has("country")) && shipdetails.getString("country").trim() != null && !shipdetails.getString("country").trim().isEmpty() && !shipdetails.getString("country").trim().equals("null") && !shipdetails.getString("country").trim().matches("")) {

                        country = shipdetails.getString("country");
                    }

                    if ((shipdetails.has("phone_number")) && shipdetails.getString("phone_number").trim() != null && !shipdetails.getString("phone_number").trim().isEmpty() && !shipdetails.getString("phone_number").trim().equals("null") && !shipdetails.getString("phone_number").trim().matches("")) {

                        phoneno = shipdetails.getString("phone_number");
                    }


                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{

            }


            if (order_item_list.size() > 0) {

                Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.muvicart_font));

                linearLayout1.setVisibility(View.VISIBLE);
                adapter = new OrderDetailsAdapter(OderDetailsActivity.this, order_item_list);
                list.setAdapter(adapter);
                ship_oderid.setText(shipoderid);
                ship_oderid.setTypeface(font);
                shipname.setText(ship_name);
                shipname.setTypeface(font);
                shipaddress.setText(address);
                shipaddress.setTypeface(font);
                shipcity.setText(city);
                shipcity.setTypeface(font);
                shipcountry.setText(country);
                shipcountry.setTypeface(font);
                shipphoneno.setText(phoneno);
                shipphoneno.setTypeface(font);
                transactionDownloadButton.setVisibility(View.VISIBLE);

                setListViewHeightBasedOnChildren(list);


            } else {
                nodata.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(OderDetailsActivity.this);
            videoPDialog.show();

        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                progressStatus = 0;
                return pDialog;
            default:
                return null;
        }
    }


}
