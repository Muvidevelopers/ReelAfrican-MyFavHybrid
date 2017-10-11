package com.release.reelAfrican.physical;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muvi on 1/16/2017.
 */
public class OrderDetailsAdapter extends BaseAdapter {
    OderDetailsActivity activity;
    ArrayList<OrderDetailsModel> ordertitem;
    SharedPreferences pref;
    String emailstr,user_id;


    public OrderDetailsAdapter(Context activity,ArrayList<OrderDetailsModel> ordertitem) {
        this.activity = (OderDetailsActivity) activity;
        this.ordertitem = ordertitem;

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
        return ordertitem.size();
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
        v = inflater.inflate(R.layout.order_details_row, null);

        //Order Details
        Typeface font = Typeface.createFromAsset(activity.getAssets(),activity.getResources().getString(R.string.muvicart_font));
        TextView Orderid = (TextView) v.findViewById(R.id.oder_id);
        Orderid.setTypeface(font);
        TextView Orderidtag = (TextView) v.findViewById(R.id.oderidtag);
        Orderidtag.setTypeface(font);
        TextView Qty = (TextView) v.findViewById(R.id.qty);
        Qty.setTypeface(font);
        TextView Price = (TextView) v.findViewById(R.id.item_price);
        Price.setTypeface(font);
        TextView Pname = (TextView) v.findViewById(R.id.item_name);
        Pname.setTypeface(font);
        ImageView image = (ImageView) v.findViewById(R.id.product_thumb);

        Picasso.with(activity)
                .load(ordertitem.get(position).getPoster())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(image);


        Orderid.setText(ordertitem.get(position).getOderid());
        Qty.setText(""+ordertitem.get(position).getQty());
        Price.setText(ordertitem.get(position).getCurrencysymbol()+"."+ordertitem.get(position).getPrice());
        Pname.setText(ordertitem.get(position).getPName());

        return v;
    }



}
