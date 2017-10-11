package com.release.reelAfrican.physical;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Mycart_Adapter extends BaseAdapter {
    MyCartActivity activity;
    ArrayList<ProductlistingModel> cartitem;
    SharedPreferences pref;
    private ProgressBarHandler videoPDialog;
    String emailstr,user_id;
    Asynremovecartitem asynremovecartitem;
    Asynincreasecartitem asynincreasecartitem;
    Asyndecreasecartitem asyndecreasecartitem;
    int pos;
    TextView item_remove,addtowishlist,item_add,remove;
    private Dialog progress_dialog;

    public Mycart_Adapter(Context activity,ArrayList<ProductlistingModel> cartitem) {
        this.activity = (MyCartActivity) activity;
        this.cartitem = cartitem;

        pref = activity.getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            user_id= "";

        }

    }

    @Override
    public int getCount() {
        return cartitem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(activity);
        v = inflater.inflate(R.layout.cartitem_row, null);

        Typeface font = Typeface.createFromAsset(activity.getAssets(),activity.getResources().getString(R.string.muvicart_font));

        TextView title = (TextView) v.findViewById(R.id.item_name);
        title.setTypeface(font);
        TextView item_price = (TextView) v.findViewById(R.id.item_price);
        item_price.setTypeface(font);
        final TextView item_amount = (TextView) v.findViewById(R.id.iteam_amount);
        item_amount.setTypeface(font);
        item_add = (TextView) v.findViewById(R.id.add_item);
        item_add.setTypeface(font);
        item_remove = (TextView) v.findViewById(R.id.remove_item);
        item_remove.setTypeface(font);
        addtowishlist = (TextView) v.findViewById(R.id.addtowhishlist);
        addtowishlist.setTypeface(font);
        remove = (TextView) v.findViewById(R.id.remove);
        remove.setTypeface(font);

        ImageView image = (ImageView) v.findViewById(R.id.product_thumb);
        ImageView image1 = (ImageView) v.findViewById(R.id.imageView1);



        if(cartitem.get(position).getStatus().equals("3")){



            Picasso.with(activity)
                    .load(R.drawable.stockout)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(image);


        }else {


            Picasso.with(activity)
                    .load(cartitem.get(position).getPoster())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(image);

        }


        title.setText(cartitem.get(position).getTitle());
        String dd = String.valueOf(cartitem.get(position).getPrice());
        Log.v("SUBHA", dd);
        item_price.setText(cartitem.get(position).getCurrencysymbol()+dd);
        item_amount.setText(""+cartitem.get(position).getItem_Count());




        item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos=position;

                asynincreasecartitem = new Asynincreasecartitem();
                asynincreasecartitem.execute();

            }
        });


        item_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos=position;

                asyndecreasecartitem = new Asyndecreasecartitem();
                asyndecreasecartitem.execute();
            }
        });

        addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                taoast("Work under process");


            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // custom dialog
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                // Custom Android Allert Dialog Title

                dialog.setCancelable(false);
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancelButton);
                dialogButtonCancel.setText("cancel");
                Button dialogButtonOk = (Button) dialog.findViewById(R.id.yesButton);
                dialogButtonOk.setText("delete");
                // Click cancel to dismiss android custom dialog box
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                // Your android custom dialog ok action
                // Action for custom dialog ok button click
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        pos=position;
                        dialog.dismiss();
                        asynremovecartitem = new Asynremovecartitem();
                        asynremovecartitem.execute();

                    }
                });

                dialog.show();



            }
        });



        return v;
    }

    private void taoast(String msgs) {

        final Toast toast = Toast.makeText(activity, msgs, Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast_boder1);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        //v.setTextColor(getResources().getColor(R.color.muvilogocolor));
        v.setTextColor(Color.WHITE);

        toast.show();

        new CountDownTimer(1000, 500)
        {

            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.show();}

        }.start();

    }


    private class Asynremovecartitem extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", cartitem.get(pos).getProdidd());
                httppost.addHeader("flag", "delete");

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

            progress_dialog.dismiss();

            if (status == 200) {


                BadgeCount.count = BadgeCount.count - 1;
                cartitem.remove(pos);
                activity.itemcont();
                activity.visiblee();
                activity.calculateprice();
                notifyDataSetChanged();

            } else {

               Toast.makeText(activity,"Network problem",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {

            progress_dialog = Util.LoadingCircularDialog(activity);
            progress_dialog.show();
        }



    }


    private class Asynincreasecartitem extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", cartitem.get(pos).getProdidd());
                int qty=cartitem.get(pos).getItem_Count()+1;
                httppost.addHeader("quantity", String.valueOf(qty));




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


            progress_dialog.dismiss();
            if (status == 200) {

                activity.Updatecart();

            } else {


                Toast.makeText(activity,"Network problem",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {

            progress_dialog = Util.LoadingCircularDialog(activity);
            progress_dialog.show();

        }

    }



    private class Asyndecreasecartitem extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;




        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", cartitem.get(pos).getProdidd());
                int qty=cartitem.get(pos).getItem_Count()-1;
                httppost.addHeader("quantity", String.valueOf(qty));




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

            progress_dialog.dismiss();

            if (status == 200) {

                activity.Updatecart();

            } else {


                Toast.makeText(activity,"Network problem",Toast.LENGTH_LONG).show();
            }
//
        }

        @Override
        protected void onPreExecute() {

            progress_dialog = Util.LoadingCircularDialog(activity);
            progress_dialog.show();
        }

    }



}
