package com.release.reelAfrican.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.DemoActivity;
import com.release.reelAfrican.adapter.FavoriteAdapter;
import com.release.reelAfrican.adapter.VideoFilterAdapter;
import com.release.reelAfrican.model.GridItem;
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
import org.json.JSONException;
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
public class FavoriteAod extends Fragment {

    String isEpisodeStr = "";
    int videoHeight = 185;
    int videoWidth = 256;
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
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;
    int itemcount = 0;
    boolean firstTime = false;
    boolean a = false;
    GridItem data_send;
    String sucessMsg;
    String loggedInStr;
    SharedPreferences pref;
    int index;
    private FavoriteAdapter customGridAdapter;
    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    // String videoUrlStr;
    String videoResolution = "BEST";

    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    int scrolledPosition = 0;
    boolean scrolling;

    //search
    String searchTextStr;
    boolean isSearched = false;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.vod, container, false);
        mGridView = (GridView) layout.findViewById(R.id.imagesGridView);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        nocontent = (TextView) layout.findViewById(R.id.nocontent);
        mGridData = new ArrayList<>();
        url = new ArrayList<>();
        context = getActivity();
        pref = context.getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        bundle = getArguments();

        asynLoadVideos = new AsynLoadVideos();
        asynLoadVideos.execute();

        mGridView.setVisibility(View.VISIBLE);


        mGridView.setAdapter(customGridAdapter);
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                a = true;
                index = i;
                if (customGridAdapter.getItem(i).isSelected() == false) {

                    customGridAdapter.getItem(i).setSelected(true);
                    customGridAdapter.getItem(i).setClicked(true);

                    data_send = mGridData.get(i);

                    String url = data_send.getImage();


                } else {
                    customGridAdapter.getItem(i).setSelected(false);

                }
                customGridAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridItem item = mGridData.get(position);
//                itemToPlay = item;
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();


                Log.v("bibhu", "moviePermalink =" + moviePermalink);
                String movieTypeId = item.getVideoTypeId();
                if (a) {
                    a = false;
                    return;
                } else {

                    if (moviePermalink.matches(Util.getTextofLanguage(getActivity(), Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        dlgAlert.setMessage(Util.getTextofLanguage(getActivity(), Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                        dlgAlert.setTitle(Util.getTextofLanguage(getActivity(), Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(getActivity(), Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {

                        if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                            final Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                            movieDetailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                            movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    movieDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(movieDetailsIntent);
                                }
                            });


                        } else if ((movieTypeId.trim().equalsIgnoreCase("3"))) {
                            final Intent detailsIntent = new Intent(getActivity(), ShowWithEpisodesActivity.class);
                            detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(detailsIntent);
                                }
                            });
                        } else if (movieTypeId.trim().equalsIgnoreCase("5") || movieTypeId.trim().equalsIgnoreCase("6")) {

                            Log.v("SUBHA", "audio data called" + movieTypeId);

                            final Intent detailsIntent = new Intent(getActivity(), DemoActivity.class);
                            detailsIntent.putExtra("PERMALINK", moviePermalink);
                            detailsIntent.putExtra("CONTENT_TYPE", movieTypeId);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(detailsIntent);
                                }
                            });


                        }

                    }
                }


            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (mGridView.getLastVisiblePosition() >= itemsInServer - 1) {
                    return;

                }

                if (view.getId() == mGridView.getId()) {
                    final int currentFirstVisibleItem = mGridView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mIsScrollingUp = false;

                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;

                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolling = false;

                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    scrolling = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (scrolling == true && mIsScrollingUp == false) {

                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                        listSize = mGridData.size();
                        if (mGridView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        boolean isNetwork = Util.checkNetwork(context);
                        if (isNetwork == true) {

                            // default data
                            asynLoadVideos = new AsynLoadVideos();
                            asynLoadVideos.execute();


                            scrolling = false;

                        }

                    }

                }

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridItem item = mGridData.get(position);
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();
                videoUrlStr = item.getVideoUrl();
                // if searched

                // for tv shows navigate to episodes
                if ((movieTypeId.equalsIgnoreCase("3"))) {
                    if (moviePermalink.matches(Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(Util.getTextofLanguage(context, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                        dlgAlert.setTitle(Util.getTextofLanguage(context, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();
                    } else {

                        final Intent detailsIntent = new Intent(context, ShowWithEpisodesActivity.class);
                        detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(detailsIntent);
                            }
                        });
                    }


                } else if (movieTypeId.trim().equalsIgnoreCase("5") || movieTypeId.trim().equalsIgnoreCase("6")) {


                    final Intent detailsIntent = new Intent(context, DemoActivity.class);
                    detailsIntent.putExtra("PERMALINK", moviePermalink);
                    detailsIntent.putExtra("CONTENT_TYPE", movieTypeId);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(detailsIntent);
                        }
                    });

                }


                // for single clips and movies
                else if ((movieTypeId.trim().equalsIgnoreCase("1")) || (movieTypeId.trim().equalsIgnoreCase("2")) || (movieTypeId.trim().equalsIgnoreCase("4"))) {
                    final Intent detailsIntent = new Intent(context, MovieDetailsActivity.class);

                    if (moviePermalink.matches(Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(Util.getTextofLanguage(context, Util.NO_DETAILS_AVAILABLE, Util.DEFAULT_NO_DETAILS_AVAILABLE));
                        dlgAlert.setTitle(Util.getTextofLanguage(context, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(context, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();
                    } else {
                        detailsIntent.putExtra(Util.PERMALINK_INTENT_KEY, moviePermalink);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(detailsIntent);
                            }
                        });
                    }
                }

            }
        });

        return layout;

    }


    public void removeFavorite(GridItem gridItem) {
        movieUniqueId = gridItem.getMovieUniqueId();

        AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
        asynFavoriteDelete.execute();
    }

    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {


        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;

//    @Override
//    protected void onPreExecute() {
//        pDialog = new ProgressBarHandler(getActivity().getBaseContext());
//        pDialog.show();
//        Log.v("NIhar","onpreExecution");
//    }

        @Override
        protected Void doInBackground(String... params) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("movie_uniq_id", movieUniqueId);
                Log.v("Fav_act", "movieUniqueId  ========" + movieUniqueId);
                httppost.addHeader("content_type", isEpisodeStr);
                httppost.addHeader("user_id", loggedInStr);

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                        status = 0;
//
//                    }
//
//                });
                }
            } catch (IOException e) {
//            if (pDialog != null && pDialog.isShowing()) {
//                pDialog.hide();
//                pDialog = null;
//            }
//            status = 0;
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

            showToast();
            if (pDialog.isShowing() && pDialog != null) {
                pDialog.hide();
            }
            Log.v("ANU", "REMOVED");
            mGridData.remove(index);
            mGridView.invalidateViews();
            customGridAdapter.notifyDataSetChanged();
            mGridView.setAdapter(customGridAdapter);


            Intent Sintent = new Intent("ITEM_STATUS");
            Sintent.putExtra("movie_uniq_id", movieUniqueId);

            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(Sintent);
            if (mGridData.size() <= 0) {


                Log.v("SUBHA","item count == "+ mGridData.size());

                nocontent.setVisibility(View.VISIBLE);
            }


        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();


        }
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
            String urlRouteList = Util.rootUrl().trim() + Util.hybridFavorite.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("limit", String.valueOf(limit));
//                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("user_id", loggedInStr);
                httppost.addHeader("type", "2");

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (mGridData != null) {
                                mGridView.setVisibility(View.VISIBLE);
                            } else {

                                mGridView.setVisibility(View.GONE);
                            }

                            Toast.makeText(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mGridView.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    itemcount = Integer.parseInt(myJson.optString("item_count"));

                   /* String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);*/
                }
                Log.v("Nihar", "length " + status);
                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);
                                Log.v("nihar_c", "enter");
                                movieUniqueId = jsonChildNode.getString("movie_uniq_id");

//                               if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
//                                    movieGenreStr = jsonChildNode.getString("genre");
//
//                                }
                                if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                    videoName = jsonChildNode.getString("title");
                                    Log.v("mainActivity", videoName);
                                }
                                if ((jsonChildNode.has("poster")) && jsonChildNode.getString("poster").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster").trim().equals("null") && !jsonChildNode.getString("poster").trim().matches("")) {
                                    videoImageStr = jsonChildNode.getString("poster");
                                    //movieImageStr = movieImageStr.replace("episode", "original");
                                    Log.v("mainActivity", videoImageStr);

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    videoPermalinkStr = jsonChildNode.getString("permalink");
//                                    moviePermalinkStr = "chit-kwint-she-the";
                                    Log.v("bibhu", "Api permalink===" + videoPermalinkStr);

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }

                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }
                                //videoTypeIdStr = "1";
//

                                mGridData.add(new GridItem(videoImageStr, videoName, "", videoTypeIdStr, "", "", videoPermalinkStr, isEpisodeStr, movieUniqueId, "", 0, 0, 0));


                            } catch (Exception e) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {



                                        mGridView.setVisibility(View.GONE);
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
                                Log.v("nihar_c", "else_enter");

                                mGridView.setVisibility(View.GONE);

                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mGridView.setVisibility(View.GONE);
                    }
                });
            }
            return null;

        }

        protected void onPostExecute(Void result) {


            super.onPostExecute(result);


            if (itemcount <= 0) {

                nocontent.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            } else {
                nocontent.setVisibility(View.GONE);
                if (responseStr != null) {
                    mProgressBar.setVisibility(View.GONE);

                    if (getActivity() != null && videoImageStr != null) {

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
                                        customGridAdapter = new FavoriteAdapter(getActivity(), R.layout.nexus_videos_grid_layout_land, mGridData, new FavoriteAdapter.closelistener() {
                                            @Override
                                            public void onCloseItemClick(int pos) {

                                                AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                                                asynFavoriteDelete.execute();

                                            }
                                        });
                                    } else {
                                        customGridAdapter = new FavoriteAdapter(getActivity(), R.layout.videos_280_grid_layout, mGridData, new FavoriteAdapter.closelistener() {
                                            @Override
                                            public void onCloseItemClick(int pos) {

                                                AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                                                asynFavoriteDelete.execute();

                                            }
                                        });

                                    }
                                    mGridView.setAdapter(customGridAdapter);
                                } else {
                                    if (density >= 3.5 && density <= 4.0) {
                                        customGridAdapter = new FavoriteAdapter(getActivity(), R.layout.nexus_videos_grid_layout, mGridData, new FavoriteAdapter.closelistener() {
                                            @Override
                                            public void onCloseItemClick(int pos) {

                                                AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                                                asynFavoriteDelete.execute();
                                            }
                                        });
                                    } else {
                                        customGridAdapter = new FavoriteAdapter(getActivity(), R.layout.videos_grid_layout, mGridData, new FavoriteAdapter.closelistener() {
                                            @Override
                                            public void onCloseItemClick(int pos) {

                                                AsynFavoriteDelete asynFavoriteDelete = new AsynFavoriteDelete();
                                                asynFavoriteDelete.execute();


                                            }
                                        });

                                    }
                                    mGridView.setAdapter(customGridAdapter);
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


                }
            }
        }

    }

    public void showToast() {

        Context context = getActivity();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater(getArguments());

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        TextView customToastMsg = (TextView) toastRoot.findViewById(R.id.toastMsg);
        customToastMsg.setText(sucessMsg);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

}