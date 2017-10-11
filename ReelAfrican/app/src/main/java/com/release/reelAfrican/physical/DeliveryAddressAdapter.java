package com.release.reelAfrican.physical;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muvi on 1/16/2017.
 */
public class DeliveryAddressAdapter extends BaseAdapter {
    DeliveryAddressActivity activity;
    ArrayList<DeliveryAddressModel> deliveryAddressModels;
    SharedPreferences pref;
    String addressid;
    private ProgressBarHandler videoPDialog;
    AsynDeleteaddress asynDeleteaddress;
    int pos;


    public DeliveryAddressAdapter(Context activity, int simple_dropdown_item_1line, ArrayList<DeliveryAddressModel> address) {
        this.activity = (DeliveryAddressActivity) activity;
        this.deliveryAddressModels = address;

    }

    @Override
    public int getCount() {
        return deliveryAddressModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    int selected = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;

        LayoutInflater inflater = LayoutInflater.from(activity);
        v = inflater.inflate(R.layout.deliveryaddress_row, null);

        Typeface font = Typeface.createFromAsset(activity.getAssets(),activity.getResources().getString(R.string.muvicart_font));

        TextView title = (TextView) v.findViewById(R.id.item_name);
        title.setTypeface(font);
        TextView delivery_address = (TextView) v.findViewById(R.id.addresss);
        delivery_address.setTypeface(font);
        TextView ph = (TextView) v.findViewById(R.id.phnumber);
        ph.setTypeface(font);
        final RadioButton radioButton=(RadioButton)v.findViewById(R.id.radio);
        final LinearLayout ll = (LinearLayout) v.findViewById(R.id.linear);

        final Button deliveryaddress = (Button) v.findViewById(R.id.deliveryaddress);
        deliveryaddress.setTypeface(font);
        final Button edit = (Button) v.findViewById(R.id.edit);
        edit.setTypeface(font);
        final Button delet = (Button) v.findViewById(R.id.delet);
        delet.setTypeface(font);

        title.setText(deliveryAddressModels.get(position).getName());

        delivery_address.setText(deliveryAddressModels.get(position).getAddress());

        ph.setText(""+deliveryAddressModels.get(position).getPhno());

        if(position == 0)
        {
            radioButton.setChecked(true);
        }
        else
            radioButton.setChecked(false);




        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selected = position;
                notifyDataSetChanged();
            }
        });

        if(position == selected){
            radioButton.setChecked(true);
            ll.setVisibility(View.VISIBLE);
        }

        else
            radioButton.setChecked(false);


        deliveryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.shipping=convertJson(deliveryAddressModels.get(position));

                final Intent shipping = new Intent(activity, ShippingActivity.class);
                shipping.putExtra("NAME",deliveryAddressModels.get(position).getName());
                shipping.putExtra("ADDRESS",deliveryAddressModels.get(position).getAddress());
                shipping.putExtra("PHONE",deliveryAddressModels.get(position).getPhno());
                shipping.putExtra("CITY",deliveryAddressModels.get(position).getCity());
                shipping.putExtra("COUNTRY",deliveryAddressModels.get(position).getCountry());
                shipping.putExtra("ZIP",deliveryAddressModels.get(position).getZip());
                activity.startActivity(shipping);
                activity.finish();

            }
        });




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent edit = new Intent(activity, EditAddressActivity.class);
                edit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                edit.putExtra("ID",deliveryAddressModels.get(position).getID());
                edit.putExtra("NAME",deliveryAddressModels.get(position).getName());
                edit.putExtra("ADDRESS",deliveryAddressModels.get(position).getAddress());
                edit.putExtra("PHONE",deliveryAddressModels.get(position).getPhno());
                edit.putExtra("CITY",deliveryAddressModels.get(position).getCity());
                edit.putExtra("COUNTRY",deliveryAddressModels.get(position).getCountry());
                edit.putExtra("ZIP",deliveryAddressModels.get(position).getZip());
                activity.startActivity(edit);
                //activity.finish();


            }
        });


        delet.setOnClickListener(new View.OnClickListener() {
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
                        addressid=deliveryAddressModels.get(pos).getID();
                        asynDeleteaddress = new AsynDeleteaddress();
                        asynDeleteaddress.execute();
                        dialog.dismiss();

                    }
                });

                dialog.show();


            }
        });



        return v;
    }






    private class AsynDeleteaddress extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;


        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.deletepgaddress.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("addressid", addressid);


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

                Toast.makeText(activity,"Address Deleted successfully", Toast.LENGTH_SHORT).show();
                deliveryAddressModels.remove(pos);
                notifyDataSetChanged();
                activity.visible();


            } else {


            }

        }

        @Override
        protected void onPreExecute() {

            videoPDialog = new ProgressBarHandler(activity);
            videoPDialog.show();

        }



    }

    private String convertJson(DeliveryAddressModel addressModel){


        JSONObject jsonObject=new JSONObject();

        try {

            jsonObject.put("first_name",addressModel.getName());
            jsonObject.put("email",addressModel.getMail());
            jsonObject.put("address",addressModel.getAddress());
            jsonObject.put("city",addressModel.getCity());
            jsonObject.put("country",addressModel.getCountry());
            jsonObject.put("phone_number",addressModel.getPhno());


        }catch (Exception e){

            e.printStackTrace();
        }

        Log.v("json format:------",jsonObject.toString());
        return jsonObject.toString();

    }


}
