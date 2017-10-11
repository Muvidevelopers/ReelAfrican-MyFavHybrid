
package com.release.reelAfrican.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.FavoriteActivity;
import com.release.reelAfrican.model.GridItem;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;

public class FavoriteAdapter extends ArrayAdapter<GridItem> {
    private int layoutResourceId;
    boolean close = false;
    private closelistener listener;
    private ArrayList<GridItem> data = new ArrayList<GridItem>();
    private Activity mActivity;
    String movieUniqueId = "", IsEpisodeStr = "",loggedInStr;
    SharedPreferences pref;
    String sucessMsg;
    Integer index = 0;



    public  interface closelistener{
        void onCloseItemClick(int pos);
    }
    public FavoriteAdapter(Activity mActivity, int layoutResourceId, ArrayList<GridItem> data,closelistener listener) {
        super(mActivity, layoutResourceId, data);
        this.mActivity = mActivity;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.listener = listener;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        Log.v("SUBHA","position value  ===== "+data.size());

        if (row == null) {
            LayoutInflater inflater = (mActivity).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.movieTitle);
            holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);
            holder.closeAlbumArt = (ImageView) row.findViewById(R.id.close_album_art);
            holder.noContent = (TextView) row.findViewById(R.id.nocontent);







            Typeface castDescriptionTypeface = Typeface.createFromAsset(mActivity.getAssets(),mActivity.getResources().getString(R.string.regular_fonts));
            holder.title.setTypeface(castDescriptionTypeface);
           /* int height = holder.videoImageview.getDrawable().getIntrinsicHeight();
            int width = holder.videoImageview.getDrawable().getIntrinsicWidth();

            holder.videoImageview.getLayoutParams().height = height;
            holder.videoImageview.getLayoutParams().width = width;*/

            if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));

            }
            else if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            else if ((mActivity.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            else {
                holder.videoImageview.setImageBitmap(decodeSampledBitmapFromResource(mActivity.getResources(), R.id.movieImageView,holder.videoImageview.getDrawable().getIntrinsicWidth(),holder.videoImageview.getDrawable().getIntrinsicHeight()));


            }
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        final GridItem item = data.get(position);
        holder.title.setText(item.getTitle());
        String imageId = item.getImage();
        Log.v("Nihar_feb",""+imageId);
        movieUniqueId = item.getMovieUniqueId();
        IsEpisodeStr = item.getIsEpisode();
        pref = mActivity.getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        Log.v("SUBHA","position ===== "+ data.get(position));

//        Glide.with(mActivity)
//                .load("https://d1yjifjuhwl7lc.cloudfront.net/public/system/posters/67000/standard/ChitKwintShiThee_1491303251.jpg")
//                .into(holder.videoImageview);

        holder.closeAlbumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close=true;
                index = position;
               if (data.get(position).isClicked()){

                   Log.v("SUBHA","movieUniqueId  ========"+item.getMovieUniqueId());
                   listener.onCloseItemClick(index);

                /*   AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                   asynFavoriteDelete.execute();*/
                  // mActivity.removeFavorite(item);

               }

            }
        });




        if (data.get(position).isSelected()){
            holder.closeAlbumArt.setVisibility(View.VISIBLE);
//            feb_bt.setImageResource(R.drawable.favorite);
        }else {
            holder.closeAlbumArt.setVisibility(View.GONE);
//            feb_bt.setImageResource(R.drawable.favorite_unselected);

        }

        if(imageId.matches("") || imageId.matches(Util.getTextofLanguage(mActivity, Util.NO_DATA, Util.DEFAULT_NO_DATA))){
            holder.videoImageview.setImageResource(R.drawable.logo);


        }else {
            Picasso.with(mActivity)
                    .load(imageId)
                    .into(holder.videoImageview);


       }



        return row;
    }


    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;


        @Override
        protected Void doInBackground(String... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                httppost.addHeader("content_type", IsEpisodeStr);
                httppost.addHeader("user_id", loggedInStr);

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    Log.v("SUBHA","response === "+ responseStr);


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
;
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

//            showToast();
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            Log.v("SUBHA","REMOVED" + index);
            data.remove(index);


            notifyDataSetChanged();
            Intent Sintent = new Intent("ITEM_STATUS");
            Sintent.putExtra("movie_uniq_id", movieUniqueId);
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(Sintent);


        }
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(mActivity);
            pDialog.show();


        }
    }
    static class ViewHolder {
        public TextView title,noContent;
        public ImageView videoImageview,closeAlbumArt;


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
}