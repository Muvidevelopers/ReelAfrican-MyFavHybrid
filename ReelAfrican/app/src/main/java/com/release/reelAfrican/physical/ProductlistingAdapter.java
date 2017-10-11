
package com.release.reelAfrican.physical;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.LoginActivity;
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

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static com.release.reelAfrican.physical.ProductListingActivity.hashMap;


public class ProductlistingAdapter extends ArrayAdapter<GridItem> {
    ProductListingActivity context;
    private int layoutResourceId;
    private ProgressBarHandler videoPDialog;
    private ArrayList<GridItem> data = new ArrayList<GridItem>();
    SharedPreferences pref;
    String emailstr,user_id;
    Asynaddtocart asynaddtocart;
    int pos;
    ProgressDialog progressDialog;
    private Dialog progress_dialog;

    public ProductlistingAdapter(Context context, int layoutResourceId,
                                 ArrayList<GridItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = (ProductListingActivity) context;
        this.data = data;

        pref = context.getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            user_id= "";

        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            Typeface title = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.muvicart_font));

            holder.title = (TextView) row.findViewById(R.id.movieTitle);
            holder.title.setTypeface(title);
            holder.price = (TextView) row.findViewById(R.id.price);
            holder.price.setTypeface(title);
            holder.addtcrt = (TextView) row.findViewById(R.id.addtocartttt);
            holder.addtcrt.setTypeface(title);
            holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);


            if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {


                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));

            }
            else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {

                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            else if ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            else {
                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }



        final GridItem item = data.get(position);


        holder.title.setText(item.getTitle());
        holder.price.setText(item.getCurrencysymbol()+item.getPrice());

        String imageId = item.getImage();


        if(imageId.matches("") || imageId.matches(Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA))){

            holder.videoImageview.setImageResource(R.drawable.logo);

        }else {

            Picasso.with(context)
                    .load(item.getImage()).error(R.drawable.no_image).placeholder(R.drawable.no_image)
                    .into(holder.videoImageview);
        }



        if(data.get(position).getStockstatus().equals("3")){

            holder.addtcrt.setText("Out of Stock");
            holder.addtcrt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.outofstock));
            holder.addtcrt.setTextColor(Color.parseColor("#65000000"));

        }else {

            holder.addtcrt.setText("Add to Cart");
            // holder.addtcrt.setBackgroundColor(Color.parseColor("#8b0000"));
            holder.addtcrt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.addtocart));
            holder.addtcrt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }



       if(hashMap.size()>0) {

           if (data.get(position).getProductid().equalsIgnoreCase(hashMap.get(data.get(position).getProductid()))) {


               if(data.get(position).getStockstatus().equals("3")){

                   holder.addtcrt.setText("Out of Stock");
                   holder.addtcrt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.outofstock));
                   holder.addtcrt.setTextColor(Color.parseColor("#65000000"));

               }else {

                   holder.addtcrt.setText("Go to Cart");
                   //holder.addtcrt.setBackgroundColor(Color.GRAY);
                   holder.addtcrt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gotocart));
                   holder.addtcrt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward_24dp, 0);


               }

           }
       }






        holder.addtcrt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    pref = context.getSharedPreferences(Util.LOGIN_PREF, 0);
                    if (pref!=null){
                        emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
                        user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

                    }else {
                        emailstr = "";
                        user_id= "";

                    }


                    pos=position;
                    int j=0;
                    if (user_id!=null) {


                        if(hashMap.size()>0) {

                            if (data.get(position).getProductid().equalsIgnoreCase(hashMap.get(data.get(position).getProductid()))) {



                                    final Intent intent = new Intent(context, MyCartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(intent);


                            } else {


                                if(data.get(pos).getStockstatus().equals("3")){

//                                    Toast.makeText(context,"Out of stocks",Toast.LENGTH_LONG).show();

                                }else {

                                    asynaddtocart = new Asynaddtocart();
                                    asynaddtocart.execute();

                                }

                            }
                        }else {

                            if(data.get(pos).getStockstatus().equals("3")){

                                Toast.makeText(context,"Out of stocks",Toast.LENGTH_LONG).show();

                            }else {

                                asynaddtocart = new Asynaddtocart();
                                asynaddtocart.execute();

                            }


                        }


                        }else {


                        final Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }


                    }
            });



        return row;
    }



    static class ViewHolder {

        public TextView title,price,addtcrt;
        public ImageView videoImageview;

    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options opt =new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res, resId, opt);
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight);
        opt.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res, resId, opt);
    }
    public static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight){
        final int height = opt.outHeight;
        final int width = opt.outWidth;
        int sampleSize=1;
        if (height > reqHeight || width > reqWidth){
            final int halfWidth = width/2;
            final int halfHeight = height/2;
            while ((halfHeight/sampleSize) > reqHeight && (halfWidth/sampleSize) > reqWidth){
                sampleSize *=2;
            }

        }
        return sampleSize;
    }




    private class Asynaddtocart extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        Dialog dialog;
        Dialog mOverlayDialog;
        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrlmuvicart().trim()+ Util.addtocart.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("user_id", user_id.trim());
                httppost.addHeader("productid", data.get(pos).getProductid());
                httppost.addHeader("quantity", "1");

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


            progress_dialog.dismiss();

            if (status == 200) {


                    BadgeCount.count = BadgeCount.count + 1;
                    context.optionmenu();
                    Toast.makeText(context,"Product added successfully", Toast.LENGTH_LONG).show();
                    hashMap.put(data.get(pos).getProductid(),data.get(pos).getProductid());
                    notifyDataSetChanged();
                }
            else {


                Toast.makeText(context,"Network problem",Toast.LENGTH_LONG).show();
            }



        }

        @Override
        protected void onPreExecute() {

            progress_dialog = Util.LoadingCircularDialog(context);
            progress_dialog.show();

        }

    }
}