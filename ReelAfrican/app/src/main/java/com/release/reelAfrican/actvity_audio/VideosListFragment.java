package com.release.reelAfrican.actvity_audio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.adapter.VideoFilterAdapter;
import com.release.reelAfrican.model.GridItem;
import com.release.reelAfrican.model.ListItem;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */
public class VideosListFragment extends Fragment {

    public static boolean clearClicked = false;



    AsynLOADUI loadUI;
    AsynLoadVideos asynLoadVideos;

    ArrayList<String> url_maps;
    private ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    private ProgressBarHandler gDialog;

    String videoImageStrToHeight;
    int  videoHeight = 185;
    int  videoWidth = 256;
    SharedPreferences pref;
    GridItem itemToPlay;
    private ProgressBarHandler pDialog;
    private static int firstVisibleInListview;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    FragmentTransaction fragmentTransaction;
    //for no internet

    private RelativeLayout noInternetConnectionLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;

    //firsttime load
    boolean firstTime=false;



    //no data

    /*The Data to be posted*/
    int offset = 1;
    int limit = 10;
    int listSize =0;
    int itemsInServer=0;

    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    //Set Context
    Context context;

    //Adapter for GridView
    private VideoFilterAdapter customGridAdapter;

    //Model for GridView
    ArrayList<GridItem> itemData= new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl ;

    // UI
    private GridView gridView;
        Fragment fragment;
    ImageView img;
    //data to load videourl
    private String movieUniqueId;
    private String movieStreamUniqueId;
    String videoUrlStr;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;

    RelativeLayout filterView;
    public static ArrayList<String> genreArray;

    public static String filterOrderByStr = "lastupload";
    MenuItem filterMenuItem;
    int prevPosition = 5;
    String filterPermalink = "";
    int scrolledPosition=0;
    boolean scrolling;
    boolean isSearched=false;
    RecyclerView genreListData;


    RelativeLayout footerView;
    public VideosListFragment() {
        // Required empty public constructor

    }
    View header;
    public static boolean isLoading = false;


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed

        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        context = getActivity();

        Log.v("BIBHU22"," videolist  called");
        //for search for each activity
        setHasOptionsMenu(true);


        filterView = (RelativeLayout) rootView.findViewById(R.id.filterBg);
        TextView categoryTitle = (TextView) rootView.findViewById(R.id.categoryTitle);
//        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.));
//        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText(getArguments().getString("title"));
        genreListData = (RecyclerView) rootView.findViewById(R.id.demoListView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        genreListData.setLayoutManager(linearLayout);
        genreListData.setItemAnimator(new DefaultItemAnimator());
        pref = context.getSharedPreferences(Util.LOGINPREFERENCE, 0); // 0 - for private mode

        posterUrl = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);

        gridView = (GridView) rootView.findViewById(R.id.imagesGridView);
        itemData.clear();
       /* gridView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context,2);
        gridView.setLayoutManager(mLayoutManager);
        gridView.setItemAnimator(new DefaultItemAnimator());*/
        footerView = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout)rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout)rootView.findViewById(R.id.noData);
        noInternetTextView =(TextView)rootView.findViewById(R.id.noInternetTextView);
        noDataTextView =(TextView)rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(context,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(context,Util.NO_CONTENT,Util.DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

        gridView.setAdapter(customGridAdapter);

        //Detect Network Connection

        boolean isNetwork = Util.checkNetwork(context);
        if (isNetwork==false){
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }
        resetData();

        Log.v("Nihar_filter","gvututfufutfv");
        asynLoadVideos = new AsynLoadVideos();
        asynLoadVideos.executeOnExecutor(threadPoolExecutor);



        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {
                    footerView.setVisibility(View.GONE);
                    return;

                }

                if (view.getId() == gridView.getId()) {
                    final int currentFirstVisibleItem = gridView.getFirstVisiblePosition();

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

                        listSize = itemData.size();
                        if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;
                        boolean isNetwork = Util.checkNetwork(context);
                        if (isNetwork == true) {

                            if (genreArray!=null && genreArray.size() > 0){

                                AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
                            }else{
                                asynLoadVideos = new AsynLoadVideos();
                                asynLoadVideos.executeOnExecutor(threadPoolExecutor);
                            }

                            scrolling = false;

                        }

                    }

                }

            }
        });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                img = (ImageView) view.findViewById(R.id.movieImageView) ;

                GridItem item = itemData.get(position);
                itemToPlay = item;
                String posterUrl = item.getImage();
                String movieName = item.getTitle();
                String movieGenre = item.getMovieGenre();
                String moviePermalink = item.getPermalink();
                String movieTypeId = item.getVideoTypeId();
                videoUrlStr = item.getVideoUrl();
                String isEpisode = item.getIsEpisode();
                movieUniqueId = item.getMovieUniqueId();
                movieStreamUniqueId = item.getMovieStreamUniqueId();

                if (moviePermalink.matches(Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA))) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(Util.getTextofLanguage(context,Util.NO_DETAILS_AVAILABLE,Util.DEFAULT_NO_DETAILS_AVAILABLE));
                    dlgAlert.setTitle(Util.getTextofLanguage(context,Util.SORRY,Util.DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(context,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(Util.getTextofLanguage(context,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {

                    if ((movieTypeId.trim().equalsIgnoreCase("5"))) {
                        fragment = new MultiPartFragment();
                        Bundle multidetailsIntent = new Bundle();
                        multidetailsIntent.putString( "PERMALINK" ,moviePermalink);
                        multidetailsIntent.putString( "CONTENT_TYPE" ,movieTypeId);
                        fragment.setArguments(multidetailsIntent);
                        if (fragment != null) {
                            FragmentManager fragmentManager = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                fragmentTransaction.replace(R.id.main_content, fragment,"MultiPartFragment");
                                fragmentTransaction.addToBackStack("MultiPartFragment");
                                fragmentTransaction.commit();
                            }
                        });



                    } else if ((movieTypeId.trim().equalsIgnoreCase("6")) ) {
                        fragment = new MultiPartFragment();
                        Bundle multidetailsIntent = new Bundle();
                        multidetailsIntent.putString( "PERMALINK" ,moviePermalink);
                        multidetailsIntent.putString( "CONTENT_TYPE" ,movieTypeId);
                        fragment.setArguments(multidetailsIntent);
                        if (fragment != null) {
                            FragmentManager fragmentManager = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                fragmentTransaction.replace(R.id.main_content, fragment,"MultiPartFragment");
                                fragmentTransaction.addToBackStack("MultiPartFragment");
                                fragmentTransaction.commit();
                            }
                        });
                    }
                }

            }
        });


        filterView.setOnTouchListener (new View.OnTouchListener()
        {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {


                filterView.setVisibility(View.GONE);
                gridView.setEnabled(true);

                if ((filterOrderByStr!=null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray!=null && genreArray.size() > 0)) {
                    firstTime = true;


                    offset = 1;
                    scrolledPosition = 0;
                    listSize = 0;
                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        limit = 20;
                    } else {
                        limit = 15;
                    }
                    itemsInServer = 0;
                    scrolling = false;
                    if (itemData != null && itemData.size() > 0) {
                        itemData.clear();
                    }
                    boolean isNetwork = Util.checkNetwork(context);
                    isSearched = false;

                    if (isNetwork == false) {
                        noInternetConnectionLayout.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                        if (filterMenuItem != null) {

                            filterMenuItem.setVisible(false);
                        }


                    } else {
                        if (pDialog!= null && pDialog.isShowing()){
                            pDialog.hide();
                            pDialog = null;
                        }
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
                        }
                        if (asynLoadVideos!=null){
                            asynLoadVideos.cancel(true);
                        }
                        if (loadUI!=null){
                            loadUI.cancel(true);
                        }
                        AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

                    }
                }
                return false;
            }
        });



        final ArrayList<ListItem> mdata = new ArrayList<ListItem>();
        genreArray = new ArrayList<String>();
        SharedPreferences isLoginPref = context.getSharedPreferences(Util.IS_LOGIN_SHARED_PRE, 0); // 0 - for private mode

        String genreString = isLoginPref.getString(Util.GENRE_ARRAY_PREF_KEY, null);
        String genreValuesString = isLoginPref.getString(Util.GENRE_VALUES_ARRAY_PREF_KEY, null);
        final String[] genreTempArr = genreString.split(",");
        String[] genreValuesTempArr = genreValuesString.split(",");

        for (int i = 0; i < genreTempArr.length; i++) {
            mdata.add(new ListItem(genreTempArr[i], genreValuesTempArr[i]));
            if (i == 0){
                mdata.set(0, new ListItem(Util.getTextofLanguage(context,Util.FILTER_BY,Util.DEFAULT_FILTER_BY), genreValuesTempArr[i]));
            }
            if (i  == genreTempArr.length - 5) {
                mdata.set(i, new ListItem(Util.getTextofLanguage(context, Util.SORT_BY, Util.DEFAULT_SORT_BY), genreValuesTempArr[i]));
            }
            if (i  == genreTempArr.length - 4) {
                mdata.set(i, new ListItem(Util.getTextofLanguage(context, Util.SORT_LAST_UPLOADED, Util.DEFAULT_SORT_LAST_UPLOADED), genreValuesTempArr[i]));
            }
            if (i  == genreTempArr.length - 3) {
                mdata.set(i, new ListItem(Util.getTextofLanguage(context, Util.SORT_RELEASE_DATE, Util.DEFAULT_SORT_RELEASE_DATE), genreValuesTempArr[i]));
            }
            if (i  == genreTempArr.length - 2) {
                mdata.set(i, new ListItem(Util.getTextofLanguage(context, Util.SORT_ALPHA_A_Z, Util.DEFAULT_SORT_ALPHA_A_Z), genreValuesTempArr[i]));
            }
            if (i  == genreTempArr.length - 1) {
                mdata.set(i, new ListItem(Util.getTextofLanguage(context, Util.SORT_ALPHA_Z_A, Util.DEFAULT_SORT_ALPHA_Z_A), genreValuesTempArr[i]));
            }
        }





//        mdata.get(prevPosition).setSelected(true);
        genreListData.addOnItemTouchListener(new RecyclerTouchListener(context, genreListData, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (position >= 1 && position <= (genreTempArr.length -6)) {
                    if (mdata.get(position).isSelected() == true) {
                        mdata.get(position).setSelected(false);

                        for(int i=0;i<genreArray.size();i++)
                        {
                            if(genreArray.contains(mdata.get(position).getSectionType()))
                            {
                                genreArray.remove(mdata.get(position).getSectionType());
                            }
                        }


                    } else {
                        genreArray.add(mdata.get(position).getSectionType());
                        mdata.get(position).setSelected(true);
                    }
                }

                if (position >= (genreTempArr.length - 4)) {
                    mdata.get(position).setSelected(true);
                    filterOrderByStr = mdata.get(position).getSectionType();
                    if (prevPosition != position) {
                        mdata.get(prevPosition).setSelected(false);
                        prevPosition = position;

                    }

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.v("nihar_back","onKey");

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.v("nihar_back","actiondown");
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.v("nihar_back","actionback");

                        if(pDialog!=null && pDialog.isShowing())
                        {
                            pDialog.hide();
                        }

                        Intent startIntent = new Intent(getActivity(), MainActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(startIntent);
                        getActivity().finish();


                    }
                }
                return false;
            }
        });

        return rootView;
    }

    private class AsynLoadFilterVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieName = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String movieImageStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String moviePermalinkStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList= Util.rootUrl().trim()+Util.listUrl.trim();
            if (genreArray!=null && genreArray.size() > 0){
                String[] mStringArray = new String[genreArray.size()];
                mStringArray = genreArray.toArray(mStringArray);
                for (int i = 0 ; i < mStringArray.length;i++) {
                    if (mStringArray.length <= 1){
                        urlRouteList = (urlRouteList +"?genre[]="+mStringArray[i].trim()).replace(" ","%20");

                    }else {
                        if (i == 0){
                            urlRouteList = (urlRouteList +"?genre[]="+mStringArray[i].trim()).replace(" ","%20");
                        }else{
                            urlRouteList = (urlRouteList +"&genre[]="+mStringArray[i].trim()).replace(" ","%20");

                        }

                    }
                }
            }

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                httppost.addHeader("lang_code",Util.getTextofLanguage(context,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                if (filterPermalink.trim()!=null && !filterPermalink.trim().equalsIgnoreCase("") && !filterPermalink.trim().matches("")){
                    httppost.addHeader("permalink", filterPermalink.trim());

                }

                if (filterOrderByStr.trim()!=null && !filterOrderByStr.trim().equalsIgnoreCase("") && !filterOrderByStr.trim().matches("")) {
                    httppost.addHeader("orderby", filterOrderByStr.trim());

                }


                SharedPreferences countryPref = context.getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                if (countryPref != null) {
                    String countryCodeStr = countryPref.getString("countryCode", null);
                    httppost.addHeader("country", countryCodeStr);
                }else{
                    httppost.addHeader("country", "IN");

                }
                if (pref != null) {
                    String loggedLanguageStr = pref.getString("PREFS_LOGIN_LANGUAGE_KEY", null);
                    if (loggedLanguageStr == null) {
                    } else {
                        httppost.addHeader("lang_code",loggedLanguageStr);
                    }
                }
                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData!=null){
                                itemData.clear();
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);


                            }else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);


                            }

                            footerView.setVisibility(View.GONE);
                            Util.showToast(context,Util.getTextofLanguage(context,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

                            //  Toast.makeText(context,Util.getTextofLanguage(context,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            footerView.setVisibility(View.GONE);
                            gridView.setVisibility(View.GONE);

                        }
                    });
                    e.printStackTrace();
                }

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        itemData.clear();
                        for(int i=0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                    movieGenreStr = jsonChildNode.getString("genre");

                                }
                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                    movieName = jsonChildNode.getString("name");

                                }
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster_url");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));

                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));

                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));

                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV));
                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataLayout.setVisibility(View.VISIBLE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);
                                        gridView.setVisibility(View.GONE);
                                        footerView.setVisibility(View.GONE);

                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        responseStr = "0";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                                footerView.setVisibility(View.GONE);


                            }
                        });
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);

                    }
                });
            }
            return null;

        }

        protected void onPostExecute(Void result) {


            if(responseStr == null)
                responseStr = "0";
            if((responseStr.trim().equals("0"))){
                try{
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                }
                catch(IllegalArgumentException ex)
                {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);

                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);

            }else{
                if(itemData.size() <= 0){
                    try{
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
                        }
                    }
                    catch(IllegalArgumentException ex)
                    {

                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);

                    }
                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);


                }else{
                    footerView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    if (filterMenuItem!=null) {

                        filterMenuItem.setVisible(true);
                    }

                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    videoImageStrToHeight = movieImageStr;

                    if (firstTime == true){
                        Picasso.with(getActivity()).load(videoImageStrToHeight
                        ).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                /*AsynLOADUI loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);*/
                            }
                        });
                    }else {
                        loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }


                }
            }
        }

        @Override
        protected void onPreExecute() {

            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()){
                videoPDialog = MainActivity.internetSpeedDialog;
                footerView.setVisibility(View.GONE);

            }else {
                videoPDialog = new ProgressBarHandler(context);
                if (listSize == 0) {
                    // hide loader for first time

                  /*  if (videoPDialog!=null && videoPDialog.isShowing()){

                    }else {
                        videoPDialog.show();
                    }*/
                    videoPDialog.show();

                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }


    }



    // on device configuration change , the grid numbers need to be changed

    public void onResume() {
        // if (genreArray!=null && genreArray.size() > 0) {
        if ((filterOrderByStr!=null && !filterOrderByStr.equalsIgnoreCase("")) || (genreArray!=null && genreArray.size() > 0)) {
            firstTime = true;
            Log.v("SUBHAA","hgdjhdgjhbj"+clearClicked);

            offset = 1;
            scrolledPosition = 0;
            listSize = 0;
            if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                limit = 20;
            } else {
                limit = 15;
            }
            itemsInServer = 0;
            scrolling = false;
            if (itemData != null && itemData.size() > 0) {
                itemData.clear();
            }
            boolean isNetwork = Util.checkNetwork(context);
            isSearched = false;

            if (isNetwork == false) {
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                if (filterMenuItem != null) {

                    filterMenuItem.setVisible(false);
                }


            } else {
                if (pDialog!= null && pDialog.isShowing()){
                    pDialog.hide();
                    pDialog = null;
                }
                if (videoPDialog != null && videoPDialog.isShowing()) {
                    videoPDialog.hide();
                    videoPDialog = null;
                }
                if (asynLoadVideos!=null){
                    asynLoadVideos.cancel(true);
                }
                if (loadUI!=null){
                    loadUI.cancel(true);
                }
                AsynLoadFilterVideos asyncLoadVideos = new AsynLoadFilterVideos();
                asyncLoadVideos.executeOnExecutor(threadPoolExecutor);

            }
        }
        // }

        if (pDialog != null) {
            pDialog.hide();
            pDialog = null;
        }
//        Log.v("SUBHA","JFJFJCLEA"+clearClicked);
        if (clearClicked == true){
            boolean isNetwork = Util.checkNetwork(context);
            if (isNetwork==false){
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }
            resetData();
            Log.v("SUBHAA","JFJFJCLEA"+clearClicked);

            clearClicked=false;
            asynLoadVideos = new AsynLoadVideos();
            asynLoadVideos.executeOnExecutor(threadPoolExecutor);
        }



        if (url_maps != null && url_maps.size() > 0) {
            url_maps.clear();
        }
        getActivity().invalidateOptionsMenu();
        super.onResume();

        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
    //Load Films Videos
    private class AsynLoadVideos extends AsyncTask<Void, Void, Void> {
        String responseStr;
        int status;
        String movieGenreStr = "";
        String movieName = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String movieImageStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String moviePermalinkStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(context,Util.NO_DATA,Util.DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;


        @Override
        protected Void doInBackground(Void... params) {

            String urlRouteList= Util.rootUrl().trim()+Util.listUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                httppost.addHeader("authToken", Util.authTokenStr.trim());
                String strtext = getArguments().getString("item");
                httppost.addHeader("permalink",strtext.trim());
                filterPermalink = strtext.trim();
                Log.v("Nihar_filter","==================="+filterPermalink);
                httppost.addHeader("limit", String.valueOf(limit));
                httppost.addHeader("offset", String.valueOf(offset));
                //httppost.addHeader("orderby", "");
                // httppost.addHeader("deviceType", "roku");
                SharedPreferences countryPref = context.getSharedPreferences(Util.COUNTRY_PREF, 0); // 0 - for private mode
                if (countryPref != null) {
                    String countryCodeStr = countryPref.getString("countryCode", null);
                    httppost.addHeader("country", countryCodeStr);
                }else{
                    httppost.addHeader("country", "IN");

                }
                httppost.addHeader("lang_code",Util.getTextofLanguage(context,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));

                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (itemData!=null){
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                            }else {
                                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                            }

                            footerView.setVisibility(View.GONE);
                            Util.showToast(context,Util.getTextofLanguage(context,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

//                            Toast.makeText(context,Util.getTextofLanguage(context,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }

                    });

                }catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            footerView.setVisibility(View.GONE);
                            gridView.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

                Log.v("Nihar_filter","response = ="+responseStr);
                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("status"));
                    String items = myJson.optString("item_count");
                    itemsInServer = Integer.parseInt(items);
                }

                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                        int lengthJsonArr = jsonMainNode.length();
                        for(int i=0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode;
                            try {
                                jsonChildNode = jsonMainNode.getJSONObject(i);

                                if ((jsonChildNode.has("genre")) && jsonChildNode.getString("genre").trim() != null && !jsonChildNode.getString("genre").trim().isEmpty() && !jsonChildNode.getString("genre").trim().equals("null") && !jsonChildNode.getString("genre").trim().matches("")) {
                                    movieGenreStr = jsonChildNode.getString("genre");

                                }
                                if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                    movieName = jsonChildNode.getString("name");

                                }
                                if ((jsonChildNode.has("poster_url")) && jsonChildNode.getString("poster_url").trim() != null && !jsonChildNode.getString("poster_url").trim().isEmpty() && !jsonChildNode.getString("poster_url").trim().equals("null") && !jsonChildNode.getString("poster_url").trim().matches("")) {
                                    movieImageStr = jsonChildNode.getString("poster_url");
                                    //movieImageStr = movieImageStr.replace("episode", "original");

                                }
                                if ((jsonChildNode.has("permalink")) && jsonChildNode.getString("permalink").trim() != null && !jsonChildNode.getString("permalink").trim().isEmpty() && !jsonChildNode.getString("permalink").trim().equals("null") && !jsonChildNode.getString("permalink").trim().matches("")) {
                                    moviePermalinkStr = jsonChildNode.getString("permalink");

                                }
                                if ((jsonChildNode.has("content_types_id")) && jsonChildNode.getString("content_types_id").trim() != null && !jsonChildNode.getString("content_types_id").trim().isEmpty() && !jsonChildNode.getString("content_types_id").trim().equals("null") && !jsonChildNode.getString("content_types_id").trim().matches("")) {
                                    videoTypeIdStr = jsonChildNode.getString("content_types_id");

                                }
                                //videoTypeIdStr = "1";

                                if ((jsonChildNode.has("is_converted")) && jsonChildNode.getString("is_converted").trim() != null && !jsonChildNode.getString("is_converted").trim().isEmpty() && !jsonChildNode.getString("is_converted").trim().equals("null") && !jsonChildNode.getString("is_converted").trim().matches("")) {
                                    isConverted = Integer.parseInt(jsonChildNode.getString("is_converted"));

                                }
                                if ((jsonChildNode.has("is_advance")) && jsonChildNode.getString("is_advance").trim() != null && !jsonChildNode.getString("is_advance").trim().isEmpty() && !jsonChildNode.getString("is_advance").trim().equals("null") && !jsonChildNode.getString("is_advance").trim().matches("")) {
                                    isAPV = Integer.parseInt(jsonChildNode.getString("is_advance"));

                                }
                                if ((jsonChildNode.has("is_ppv")) && jsonChildNode.getString("is_ppv").trim() != null && !jsonChildNode.getString("is_ppv").trim().isEmpty() && !jsonChildNode.getString("is_ppv").trim().equals("null") && !jsonChildNode.getString("is_ppv").trim().matches("")) {
                                    isPPV = Integer.parseInt(jsonChildNode.getString("is_ppv"));

                                }
                                if ((jsonChildNode.has("is_episode")) && jsonChildNode.getString("is_episode").trim() != null && !jsonChildNode.getString("is_episode").trim().isEmpty() && !jsonChildNode.getString("is_episode").trim().equals("null") && !jsonChildNode.getString("is_episode").trim().matches("")) {
                                    isEpisodeStr = jsonChildNode.getString("is_episode");

                                }

                                itemData.add(new GridItem(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV));

                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataLayout.setVisibility(View.VISIBLE);
                                        noInternetConnectionLayout.setVisibility(View.GONE);
                                        gridView.setVisibility(View.GONE);
                                        footerView.setVisibility(View.GONE);
                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        responseStr = "0";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetConnectionLayout.setVisibility(View.GONE);
                                gridView.setVisibility(View.GONE);
                                footerView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
            catch (Exception e)
            {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            noDataLayout.setVisibility(View.VISIBLE);
                            noInternetConnectionLayout.setVisibility(View.GONE);
                            gridView.setVisibility(View.GONE);
                            footerView.setVisibility(View.GONE);}
                    });
                }

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void result) {

            if(responseStr == null)
                responseStr = "0";
            if((responseStr.trim().equals("0"))){
                try{
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                }
                catch(IllegalArgumentException ex)
                {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }
                noDataLayout.setVisibility(View.VISIBLE);
                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
            }else{
                if(itemData.size() <= 0){
                    try{
                        if (videoPDialog != null && videoPDialog.isShowing()) {
                            videoPDialog.hide();
                            videoPDialog = null;
                        }
                    }
                    catch(IllegalArgumentException ex)
                    {

                        noDataLayout.setVisibility(View.VISIBLE);
                        noInternetConnectionLayout.setVisibility(View.GONE);
                        gridView.setVisibility(View.GONE);
                        footerView.setVisibility(View.GONE);
                    }
                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    footerView.setVisibility(View.GONE);
                }else{
                    footerView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.GONE);
                    videoImageStrToHeight = movieImageStr;
                    if (firstTime == true){
                        Picasso.with(context).load(videoImageStrToHeight
                        ).error(R.drawable.no_image).into(new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                videoWidth = bitmap.getWidth();
                                videoHeight = bitmap.getHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {
                                Log.v("SUBHA","videoImageStrToHeight = "+ videoImageStrToHeight);
                                videoImageStrToHeight = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";
                                videoWidth = errorDrawable.getIntrinsicWidth();
                                videoHeight = errorDrawable.getIntrinsicHeight();
                                loadUI = new AsynLOADUI();
                                loadUI.executeOnExecutor(threadPoolExecutor);

                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {

                            }
                        });

                    }else {
                        loadUI = new AsynLOADUI();
                        loadUI.executeOnExecutor(threadPoolExecutor);
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {
            if (MainActivity.internetSpeedDialog != null && MainActivity.internetSpeedDialog.isShowing()){
                videoPDialog = MainActivity.internetSpeedDialog;
                footerView.setVisibility(View.GONE);

            }else {
                videoPDialog = new ProgressBarHandler(context);

                if (listSize == 0) {
                    // hide loader for first time

                    videoPDialog.show();
                    footerView.setVisibility(View.GONE);
                } else {
                    // show loader for first time
                    videoPDialog.hide();
                    footerView.setVisibility(View.VISIBLE);

                }
            }
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

     /*   InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
*/

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        gridView.setLayoutParams(layoutParams);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);

        if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            }

        } else if ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

            gridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);


        } else {
            if (videoWidth > videoHeight) {
                gridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
            } else {
                gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 4);
            }


        }

        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onDetach() {
        itemData.clear();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.v("nihar_called","true");
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        Log.v("BIBHU22"," videolist onCreateOptionsMenu called");
        inflater.inflate(R.menu.menu_main, menu);
      //  menu.findItem(R.id.filter).setVisible(true);
    //    menu.findItem(R.id.nav_Search).setVisible(true);

    }




    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            float density = context.getResources().getDisplayMetrics().density;
            if (firstTime == true) {
                try {
                    if (videoPDialog != null && videoPDialog.isShowing()) {
                        videoPDialog.hide();
                        videoPDialog = null;
                    }
                } catch (IllegalArgumentException ex) {

                    noDataLayout.setVisibility(View.VISIBLE);
                    noInternetConnectionLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    if (filterMenuItem != null) {

                        filterMenuItem.setVisible(false);
                    }

                    footerView.setVisibility(View.GONE);
                }

                gridView.smoothScrollToPosition(0);
                firstTime = false;
                ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
                gridView.setLayoutParams(layoutParams);
                gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gridView.setGravity(Gravity.CENTER_HORIZONTAL);

                if (getActivity()!=null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    }

                } else if (getActivity() !=null && (getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
                    }

                } else if (getActivity()!=null && (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {

                    gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2);


                } else {
                    if (videoWidth > videoHeight) {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 4);
                    } else {
                        gridView.setNumColumns(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 5);
                    }

                }

                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout_land, itemData);
                    }else{
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout, itemData);
                    }else{
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);

                    }
                   // customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);
                    gridView.setAdapter(customGridAdapter);
                }


            } else {
                // save RecyclerView state
                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = gridView.onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


                if (videoWidth > videoHeight) {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout_land, itemData);
                    }else{
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_280_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                } else {
                    if (density >= 3.5 && density <= 4.0) {
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.nexus_videos_grid_layout, itemData);
                    }else{
                        customGridAdapter = new VideoFilterAdapter(context, R.layout.videos_grid_layout, itemData);

                    }
                    gridView.setAdapter(customGridAdapter);
                }

                if (mBundleRecyclerViewState != null) {
                    gridView.onRestoreInstanceState(listState);
                }

            }
        }


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = gridView.onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    public void resetData(){
        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        firstTime=true;

        offset = 1;
        isLoading = false;
        listSize = 0;
        if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        }else {
            limit = 15;
        }
        itemsInServer = 0;

    }


  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           /* case R.id.filter:
                // Not implemented here


                noInternetConnectionLayout.setVisibility(View.GONE);
                gridView.setEnabled(true);
                startActivity(new Intent(getActivity(), FilterActivity.class));
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
                filterIntent.putExtra("genreList", genreArray);

            return false;*/

            default:
                break;
        }

        return false;
    }


}
