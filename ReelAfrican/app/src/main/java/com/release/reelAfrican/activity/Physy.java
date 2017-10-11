package com.release.reelAfrican.activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter.VideoFilterAdapter;
import com.release.reelAfrican.model.GridItem;
import com.release.reelAfrican.physical.ProductDetail_activity;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Physy extends Fragment {


    int  videoHeight = 185;
    int  videoWidth = 256;
    Bundle bundle;
    private ArrayList<GridItem> mGridData;
    private ArrayList<String> url;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    Context context;
    private VideoFilterAdapter mGridAdapter;
    AsynLoadVideos asynLoadVideos;
    String videoUrlStr;
    TextView nocontent;
    int itemsInServer = 0;



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.physical, container, false);
        mGridView = (GridView) layout.findViewById(R.id.imagesGridView);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        nocontent = (TextView) layout.findViewById(R.id.nocontent);
        mGridData = new ArrayList<>();
        url = new ArrayList<>();
        context = getActivity();

        bundle = getArguments();

        asynLoadVideos = new AsynLoadVideos();
        asynLoadVideos.execute();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GridItem item = mGridData.get(position);
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();
                videoUrlStr = item.getVideoUrl();


                final Intent detailsIntent = new Intent(context, ProductDetail_activity.class);
                detailsIntent.putExtra("permalink", moviePermalink);
                detailsIntent.putExtra("search", "search");
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(detailsIntent);

            }
        });




//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Fetching The File....");
//        progressDialog.show();

        return layout;

    }





    public class AsynLoadVideos extends AsyncTask<String, Void, Void> {

        ProgressBarHandler pDialog;
        String responseStr;
        int status;
        String videoGenreStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoName = "";
        String videoImageStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoPermalinkStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoTypeStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoUrlStr = Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        String movieUniqueIdStr = "";
        String movieStreamUniqueIdStr = "";
        int isConverted = 0;
        int isAPV = 0;
        int isPPV = 0;
        String movieThirdPartyUrl = "";
        int count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... params) {

            //singleItem = new ArrayList<SingleItemModel>();
            String urlRouteList = Util.rootUrl().trim() + Util.hybridsearch.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("q", Util.searchstring.trim());
                httppost.addHeader("type", "3");

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                    //Log.v("sanjay:----",responseStr.toString());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseStr = "0";

                        }

                    });

                } catch (IOException e) {
                    responseStr = "0";

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    itemsInServer = Integer.parseInt(myJson.optString("item_count"));
                }


                if (status > 0) {
                    if (status == 200) {
                        JSONArray jsonMainNode = myJson.getJSONArray("search");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                if ((jsonChildNode.has("thirdparty_url")) && jsonChildNode.getString("thirdparty_url").trim() != null && !jsonChildNode.getString("thirdparty_url").trim().isEmpty() && !jsonChildNode.getString("thirdparty_url").trim().equals("null") && !jsonChildNode.getString("thirdparty_url").trim().matches("")) {
                                    movieThirdPartyUrl = jsonChildNode.getString("thirdparty_url");

                                }
                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                    videoGenreStr = jsonChildNode.getString("genre");

                                }
                                if ((jsonChildNode.has("episode_title")) && jsonChildNode.getString("episode_title").trim() != null && !jsonChildNode.getString("episode_title").trim().isEmpty() && !jsonChildNode.getString("episode_title").trim().equals("null") && !jsonChildNode.getString("episode_title").trim().matches("")) {
                                    videoName = jsonChildNode.getString("episode_title");

                                } else {
                                    if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                        videoName = jsonChildNode.getString("name");

                                    }
                                }
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    videoImageStr = jsonChildNode.getString("poster_url");

                                    Log.v("sanjay:----123",videoImageStr.toString());
                                    //videoImageStr = videoImageStr.replace("episode", "original");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    videoPermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("display_name")) && jsonChildNode.getString("display_name").trim() != null && !jsonChildNode.getString("display_name").trim().isEmpty() && !jsonChildNode.getString("display_name").trim().equals("null") && !jsonChildNode.getString("display_name").trim().matches("")) {
                                    videoTypeStr = jsonChildNode.getString("display_name");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.getString("embeddedUrl").trim() != null && !jsonChildNode.getString("embeddedUrl").trim().isEmpty() && !jsonChildNode.getString("embeddedUrl").trim().equals("null") && !jsonChildNode.getString("embeddedUrl").trim().matches("")) {
                                    videoUrlStr = jsonChildNode.getString("embeddedUrl");

                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }
                                if ((jsonChildNode.has("muvi_uniq_id")) && jsonChildNode.getString("muvi_uniq_id").trim() != null && !jsonChildNode.getString("muvi_uniq_id").trim().isEmpty() && !jsonChildNode.getString("muvi_uniq_id").trim().equals("null") && !jsonChildNode.getString("muvi_uniq_id").trim().matches("")) {
                                    movieUniqueIdStr = jsonChildNode.getString("muvi_uniq_id");

                                }
                                if ((jsonChildNode.has("movie_stream_uniq_id")) && jsonChildNode.getString("movie_stream_uniq_id").trim() != null && !jsonChildNode.getString("movie_stream_uniq_id").trim().isEmpty() && !jsonChildNode.getString("movie_stream_uniq_id").trim().equals("null") && !jsonChildNode.getString("movie_stream_uniq_id").trim().matches("")) {
                                    movieStreamUniqueIdStr = jsonChildNode.getString("movie_stream_uniq_id");

                                }
                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));

                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));

                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));

                                }

                                mGridData.add(new GridItem(videoImageStr, videoName, "", videoTypeIdStr, videoGenreStr, "", videoPermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV));

                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
            } catch (Exception e) {
                responseStr = "0";

            }
            return null;

        }

        protected void onPostExecute(Void result) {


            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);

            if(itemsInServer==0){

                nocontent.setVisibility(View.VISIBLE);

            }




            if (responseStr!=null) {


                if (getActivity()!=null && videoImageStr!=null) {

                    final float density = getResources().getDisplayMetrics().density;

                    Picasso.with(getActivity()).load(videoImageStr
                    ).into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            videoWidth = bitmap.getWidth();
                            videoHeight = bitmap.getHeight();

                            if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                                if (videoWidth > videoHeight) {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                                } else {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                                }

                            } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                                if (videoWidth > videoHeight) {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                                } else {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                                }

                            } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                                mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                            } else {
                                if (videoWidth > videoHeight) {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                                } else {
                                    mGridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                                }

                            }
                            if (videoWidth > videoHeight) {
                                if (density >= 3.5 && density <= 4.0) {
                                    mGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout_land, mGridData);
                                }else{
                                    mGridAdapter = new VideoFilterAdapter(context, R.layout.videos_280_grid_layout, mGridData);

                                }
                                mGridView.setAdapter(mGridAdapter);
                                mProgressBar.setVisibility(View.GONE);
                            }else {


                                if (density >= 3.5 && density <= 4.0) {
                                    mGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout, mGridData);
                                }else{
                                    mGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, mGridData);

                                }
                                mGridView.setAdapter(mGridAdapter);
                                mProgressBar.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onBitmapFailed(final Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {

                        }
                    });


                }



            } else {


                //Toast.makeText(getActivity(),Util.getTextofLanguage(getActivity(),Util.SORRY,Util.DEFAULT_SORRY)+ " "+Util.getTextofLanguage(getActivity(),Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();



            }
        }

    }


}