package com.release.reelAfrican.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.AudioPlayerActivity;
import com.release.reelAfrican.adapter.RecyclerViewDataAdapter;
import com.release.reelAfrican.model.GetMenuItem;
import com.release.reelAfrican.model.SectionDataModel;
import com.release.reelAfrican.model.SingleItemModel;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
import static com.release.reelAfrican.utils.Util.mediaPlayer;
import static com.release.reelAfrican.utils.Util.pause_controller;

/**
 * Created by Muvi on 11/24/2016.
 */
public class HomeFragment extends Fragment {

//    int banner[] = {R.drawable.slider,R.drawable.slider1,R.drawable.slider2,R.drawable.slider3,R.drawable.slider4,R.drawable.slider5};
//    int bannerL[] = {R.drawable.slider,R.drawable.slider1,R.drawable.slider2,R.drawable.slider3,R.drawable.slider4,R.drawable.slider5};


    /***************chromecast*********************
    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }



    private VideoView mVideoView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mStartText;
    private TextView mEndText;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private ProgressBar mLoading;
    private View mControllers;
    private View mContainer;
    private ImageView mCoverArt;
    private Timer mSeekbarTimer;
    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final Handler mHandler = new Handler();
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;
    private boolean mControllersVisible;
    private int mDuration;
    private TextView mAuthorView;
    private ImageButton mPlayCircle;


    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            //invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(CastSession session) {
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionEnding(CastSession session) {
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
        }
    }





    MediaInfo mediaInfo;
    **************chromecast*********************

*/

    int  videoHeight = 185;
    int  videoWidth = 256;
    TextView  song_p_name, Artist_p_name;

    AsynLoadVideos asynLoadVideos;
    AsynLOADUI loadui;
    View rootView;
    int item_CountOfSections = 0;
    boolean isFirstTime = false;
    int counter = 0;
    ArrayList<GetMenuItem> menuList;
    ArrayList<String> url_maps;

    private ProgressBarHandler mProgressBarHandler = null;

    //private ProgressDialog videoPDialog = null;
    private RelativeLayout noInternetLayout;
    RelativeLayout noDataLayout;
    TextView noDataTextView;
    TextView noInternetTextView;


    RecyclerView my_recycler_view;
    Context context;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;


    ArrayList<SectionDataModel> allSampleData;
    ArrayList<SingleItemModel> singleItem;

    //AsynLoadImageUrls as = null;
    AsynLoadMenuItems asynLoadMenuItems = null;
   /* int banner[] = {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
    int bannerL[] = {R.drawable.banner1_l,R.drawable.banner2_l,R.drawable.banner3_l};*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    private String imageUrlStr;
    private int listSize = 0;
    RecyclerView recycler_view_list;
    private boolean firstTime = false;
    RecyclerViewDataAdapter adapter;
    LinearLayoutManager mLayoutManager;
    RelativeLayout footerView;
    int bannerLoaded = 0;
    RelativeLayout sliderRelativeLayout;
    private SliderLayout mDemoSlider;
    String videoImageStrToHeight;
    int ui_completed = 0;
    int loading_completed = 0;
    String song_status = null;
    RelativeLayout minicontroller_layout_relative;
    ImageView albumArt_player,open_bottomSheet;
    SeekBar  seekbar_botomSht;
    SharedPreferences pref;
    String emailstr,user_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rootView = v;
        context = getActivity();
        setHasOptionsMenu(true);
        Util.image_orentiation.clear();



        minicontroller_layout_relative =(RelativeLayout) v.findViewById(R.id.minicontroller_layout_relative);
        seekbar_botomSht = (SeekBar) v.findViewById(R.id.miniController_seekbar);
        song_p_name = (TextView) v.findViewById(R.id.song_p_name);
        Artist_p_name = (TextView) v.findViewById(R.id.song_p_Genre);
        Log.v("BIBHU2","device_id already created ="+ Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID));
        Log.v("BIBHU2","google_id already created ="+Util.getTextofLanguage(getActivity(),Util.GOOGLE_FCM_TOKEN,Util.DEFAULT_GOOGLE_FCM_TOKEN));

        open_bottomSheet = (ImageView) v.findViewById(R.id.open_bottomSheet);
        open_bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AudioPlayerActivity.class);
                if(Util.content_types_id.equals("5")) {
                    intent.putExtra("bannerimage", Util.bannerimage);
                    intent.putExtra("posterimage", Util.posterimage);
                    intent.putExtra("content_types_id", Util.content_types_id);
                    intent.putExtra("Content", Util.contentDetailsOutputArrayList);
                }if(Util.content_types_id.equals("6")){
                    intent.putExtra("bannerimage", Util.banner);
                    intent.putExtra("posterimage", Util.poster);
                    intent.putExtra("content_types_id", Util.content_types_id);
                    intent.putExtra("Content_multipart", Util.episode_details_output);

                }
                startActivity(intent);
            }
        });
        albumArt_player = (ImageView) v.findViewById(R.id.miniControl_play);
        albumArt_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pintent = new Intent("SERVICE_ACTION_NEXT");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(Pintent);
            }
        });


        minicontroller_layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            @Nullable
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AudioPlayerActivity.class);
                if(Util.content_types_id.equals("5")) {
                    intent.putExtra("bannerimage", Util.bannerimage);
                    intent.putExtra("posterimage", Util.posterimage);
                    intent.putExtra("content_types_id", Util.content_types_id);
                    intent.putExtra("Content", Util.contentDetailsOutputArrayList);
                }if(Util.content_types_id.equals("6")){
                    intent.putExtra("bannerimage", Util.banner);
                    intent.putExtra("posterimage", Util.poster);
                    intent.putExtra("content_types_id", Util.content_types_id);
                    intent.putExtra("Content_multipart", Util.episode_details_output);

                }
                startActivity(intent);
            }
        });



        allSampleData = new ArrayList<SectionDataModel>();
        // createDummyData();
        footerView = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        sliderRelativeLayout = (RelativeLayout) v.findViewById(R.id.sliderRelativeLayout);
        mDemoSlider = (SliderLayout) v.findViewById(R.id.sliderLayout);

        sliderRelativeLayout.setVisibility(View.GONE);
        noInternetLayout = (RelativeLayout)rootView.findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout)rootView.findViewById(R.id.noData);
        noInternetTextView =(TextView)rootView.findViewById(R.id.noInternetTextView);
        noDataTextView =(TextView)rootView.findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(context, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(context, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));
        albumArt_player = (ImageView) v.findViewById(R.id.miniControl_play);
        footerView.setVisibility(View.GONE);

        my_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        boolean isNetwork = Util.checkNetwork(getActivity());
        if (isNetwork == true) {
            // default data
            menuList = new ArrayList<GetMenuItem>();

            url_maps = new ArrayList<String>();

            asynLoadMenuItems = new AsynLoadMenuItems();
            asynLoadMenuItems.executeOnExecutor(threadPoolExecutor);


        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(SongStatusReciverhome, new IntentFilter("SONG_STATUS"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(notificationStatus, new IntentFilter("SONG_STATUS_NOTI"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(CLOSE_NOTIFiCATION, new IntentFilter("CLOSE_NOTI"));
        return v;


    }

    private BroadcastReceiver CLOSE_NOTIFiCATION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responce = intent.getStringExtra("closeNotification");
            LocalBroadcastManager.getInstance(context).unregisterReceiver(SongStatusReciverhome);

            if (responce.equals("close")){


                minicontroller_layout_relative.setVisibility(View.GONE);

            }

        }
    };

    private BroadcastReceiver notificationStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            minicontroller_layout_relative.setVisibility(View.GONE);

        }

    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        MenuItem item,item1;
        item= menu.findItem(R.id.action_filter);
        item.setVisible(false);

        pref = getActivity().getSharedPreferences(Util.LOGIN_PREF, 0);
        if (pref!=null){
            emailstr= pref.getString("PREFS_LOGIN_EMAIL_ID_KEY", null);
            user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);

        }else {
            emailstr = "";
            user_id= "";

        }

        if(emailstr!=null){

            item1= menu.findItem(R.id.action_notifications);
            item1.setVisible(true);
        }


        super.onCreateOptionsMenu(menu, inflater);

    }


    private void StartAsyncTaskInParallel(AsynLoadMenuItems task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    private class AsynLoadVideos extends AsyncTask<String, Void, Void> {

        String responseStr;
        int status;
        String movieGenreStr = "";
        String moviePermalinkStr = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String videoTypeIdStr = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String isEpisodeStr = "";
        int isAPV = 0;
        int isPPV = 0;
        int isConverted = 0;
        String movieName = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String movieImageStr = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String producttitle = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String productImageStr = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String productprice = Util.getTextofLanguage(context, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        String prm,permalink;


        @Override
        protected Void doInBackground(String... params) {

            singleItem = new ArrayList<SingleItemModel>();
            String urlRouteList = Util.rootUrl().trim() + Util.getContent.trim();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("section_id",String.valueOf(params[0]));
                httppost.addHeader("lang_code", Util.getTextofLanguage(context, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

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
                }


                if (status > 0) {
                    if (status == 200) {

                        JSONArray jsonMainNode = myJson.getJSONArray("section");

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

                                if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {

                                    producttitle = jsonChildNode.getString("title");

                                }

                                if ((jsonChildNode.has("poster")) && jsonChildNode.getString("poster").trim() != null && !jsonChildNode.getString("poster").trim().isEmpty() && !jsonChildNode.getString("poster").trim().equals("null") && !jsonChildNode.getString("poster").trim().matches("")) {

                                    productImageStr = jsonChildNode.getString("poster");

                                }

                                if ((jsonChildNode.has("price")) && jsonChildNode.getString("price").trim() != null && !jsonChildNode.getString("price").trim().isEmpty() && !jsonChildNode.getString("price").trim().equals("null") && !jsonChildNode.getString("price").trim().matches("")) {

                                    productprice = jsonChildNode.getString("price");

                                }

                                singleItem.add(new SingleItemModel(movieImageStr, movieName, "", videoTypeIdStr, movieGenreStr, "", moviePermalinkStr,isEpisodeStr,"","",isConverted,isPPV,isAPV,producttitle,productImageStr,productprice));
                            } catch (Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        responseStr = "0";

                                    }
                                });
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        responseStr = "0";

                    }
                }
            } catch (Exception e) {
                responseStr = "0";

            }
            return null;

        }

        protected void onPostExecute(Void result) {
            if (mProgressBarHandler != null) {
                mProgressBarHandler.hide();
                mProgressBarHandler = null;
            }

            if (responseStr == null) {
                responseStr = "0";
            }


            allSampleData.add(new SectionDataModel(menuList.get(counter).getName(),menuList.get(counter).getSectionType(), menuList.get(counter).getSectionId(), singleItem));

            boolean isNetwork = Util.checkNetwork(context);
            if (isNetwork == true) {

                if (getActivity()!=null) {

                    new RetrieveFeedTask().execute(movieImageStr);


                }


            }else{
                noInternetLayout.setVisibility(View.VISIBLE);
            }





           /* videoImageStrToHeight = movieImageStr;
            AsynLOADPicasso asynLOADPicasso= new AsynLOADPicasso();
            asynLOADPicasso.executeOnExecutor(threadPoolExecutor);
            loadui = new AsynLOADUI();
            loadui.executeOnExecutor(threadPoolExecutor);*/

            return;

            // }
        }

        @Override
        protected void onPreExecute() {

            if (firstTime == false){


                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarHandler = new ProgressBarHandler(context);
                            mProgressBarHandler.show();
                        }
                    });

                }

            }else {
               /* if (counter >= 0 && counter >= menuList.size()-1) {
                    Log.v("SUBHA","COUNTER");
                    loading_completed = true;
                }*/
            }
        }

    }
    private class AsynLOADPicasso extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Picasso.with(context).load(videoImageStrToHeight).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    videoWidth = bitmap.getWidth();
                    videoHeight = bitmap.getHeight();
                    loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    loadui = new AsynLOADUI();
                    loadui.executeOnExecutor(threadPoolExecutor);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            return null;
        }

        protected void onPostExecute(Void result) {

        }
        @Override
        protected void onPreExecute() {

        }

    }

    private class AsynLOADUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        protected void onPostExecute(Void result) {
            //ui_completed = ui_completed + 1;

          /*  if (videoWidth > videoHeight) {

                Util.image_orentiation.add(0);

            }else{
                Util.image_orentiation.add(1);

            }*/

            if (videoWidth > videoHeight) {

                Util.ori=0;

            }else{
                Util.ori=1;

            }

            Log.v("SUBHA", "HHH"+videoWidth+videoHeight);
            Log.v("SUBHA", "vertical"+MainActivity.vertical);

            if (getView() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            if (firstTime == false) {

                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }
                firstTime = true;

                if (adapter != null){

                    adapter.notifyDataSetChanged();
                }
                else { // it works first time
                    adapter = new RecyclerViewDataAdapter(context, allSampleData, url_maps,firstTime, MainActivity.vertical);
                    //   adapter = new AdapterClass(context,list);
                    my_recycler_view.setAdapter(adapter);
                }

            }else{
                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }


                if (counter >= 0 && counter >= menuList.size()-1) {
                    footerView.setVisibility(View.GONE);
                }

                mBundleRecyclerViewState = new Bundle();
                Parcelable listState = my_recycler_view.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
                if (mBundleRecyclerViewState != null) {
                    my_recycler_view.getLayoutManager().onRestoreInstanceState(listState);
                }
            }


            if (counter >= 0 && counter < menuList.size() -1 ){
                counter = counter+1;
                boolean isNetwork = Util.checkNetwork(context);
                if (isNetwork == true) {
                    if (getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                footerView.setVisibility(View.VISIBLE);

                            }
                        });
                    }


                    // default data
                    asynLoadVideos = new AsynLoadVideos();
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor, menuList.get(counter).getSectionId());

                }else{
                    noInternetLayout.setVisibility(View.VISIBLE);
                }
            }



        }
        @Override
        protected void onPreExecute() {

        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save RecyclerView state
        if (my_recycler_view != null)
        {
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mLayoutManager.onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

        }
        super.onSaveInstanceState(outState);

       /* if (my_recycler_view!=null && my_recycler_view.getLayoutManager().onSaveInstanceState()!=nu) {
            Parcelable listState = my_recycler_view.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }*/
    }


    private class AsynLoadMenuItems extends AsyncTask<Void, Void, Void> implements  BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{


        String responseStr;
        int statusCode;
        String title;
        String studio_id;
        String language_id;
        String section_id;
        String section_type;
        JSONArray sectionJson;

        private ProgressBarHandler progressBarHandler = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.getFeaturedContent.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                httppost.addHeader("lang_code", Util.getTextofLanguage(context, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE));

              /*  httppost.addHeader("limit", "1");
                httppost.addHeader("offset", String.valueOf(counter));*/

                // Execute HTTP Post Request
                try {

                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    if (singleItem != null && singleItem.size() > 0) {
                        singleItem.clear();
                    }

                    if (allSampleData != null && allSampleData.size() > 0) {
                        allSampleData.clear();
                    }




                } catch (org.apache.http.conn.ConnectTimeoutException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressBarHandler != null) {
                                progressBarHandler.hide();
                                progressBarHandler = null;
                            }

                            responseStr = "0";
                            allSampleData = null;


                        }

                    });

                } catch (IOException e) {
                    if (progressBarHandler != null) {
                        progressBarHandler.hide();
                        progressBarHandler = null;
                    }

                    responseStr = "0";
                    allSampleData = null;

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    Log.v("ANU","responseStr========="+responseStr);
                    if (Integer.parseInt(myJson.optString("code")) == 200) {
                        if (myJson.has("BannerSectionList")) {
                            JSONArray bannerJson = null;
                            Log.v("ANU","responseStr1111========="+responseStr);
                            try {
                                bannerJson = myJson.getJSONArray("BannerSectionList");
                                int lengthBannerImagesArray = bannerJson.length();

                                if (lengthBannerImagesArray > 0) {
                                    for (int i = 0; i < lengthBannerImagesArray; i++) {
                                        url_maps.add(bannerJson.getJSONObject(i).getString("image_path").trim());
                                        Log.v("ANU","image_path========="+bannerJson.getJSONObject(i).getString("image_path").trim());


                                    }
                                } else {
                                    url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");

                                }
                            } catch (JSONException e2) {
                                url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");
                                e2.printStackTrace();
                            }
                        } else {
                            url_maps.add("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");

                        }
                        if (myJson.has("SectionName")) {
                            sectionJson = myJson.getJSONArray("SectionName");
                            int lengthJsonArr = sectionJson.length();
                            for (int i = 0; i < lengthJsonArr; i++) {
                                JSONObject jsonChildNode;
                                try {
                                    jsonChildNode = sectionJson.getJSONObject(i);

                                    if ((jsonChildNode.has("studio_id")) && jsonChildNode.getString("studio_id").trim() != null && !jsonChildNode.getString("studio_id").trim().isEmpty() && !jsonChildNode.getString("studio_id").trim().equals("null") && !jsonChildNode.getString("studio_id").trim().matches("")) {
                                        studio_id = jsonChildNode.getString("studio_id");

                                    }
                                    if ((jsonChildNode.has("language_id")) && jsonChildNode.getString("language_id").trim() != null && !jsonChildNode.getString("language_id").trim().isEmpty() && !jsonChildNode.getString("language_id").trim().equals("null") && !jsonChildNode.getString("language_id").trim().matches("")) {
                                        language_id = jsonChildNode.getString("language_id");

                                    }
                                    if ((jsonChildNode.has("title")) && jsonChildNode.getString("title").trim() != null && !jsonChildNode.getString("title").trim().isEmpty() && !jsonChildNode.getString("title").trim().equals("null") && !jsonChildNode.getString("title").trim().matches("")) {
                                        title = jsonChildNode.getString("title");

                                    }
                                    if ((jsonChildNode.has("section_id")) && jsonChildNode.getString("section_id").trim() != null && !jsonChildNode.getString("section_id").trim().isEmpty() && !jsonChildNode.getString("section_id").trim().equals("null") && !jsonChildNode.getString("section_id").trim().matches("")) {
                                        section_id = jsonChildNode.getString("section_id");
                                        Log.v("ANU","section_id========="+section_id);

                                    }

                                    if ((jsonChildNode.has("section_type")) && jsonChildNode.getString("section_type").trim() != null && !jsonChildNode.getString("section_type").trim().isEmpty() && !jsonChildNode.getString("section_type").trim().equals("null") && !jsonChildNode.getString("section_type").trim().matches("")) {
                                        section_type = jsonChildNode.getString("section_type");
                                        Log.v("ANU","section_type========="+section_type);

                                    }

                                    menuList.add(new GetMenuItem(title, section_id, studio_id, language_id,section_type));
                                } catch (Exception e) {
                                    responseStr = "0";

                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            responseStr = "0";


                        }
                    }
                }
            }catch (Exception e) {


                responseStr = "0";
                allSampleData = null;
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {


            if (progressBarHandler != null) {
                progressBarHandler.hide();
                progressBarHandler = null;
            }

            if (responseStr == null) {
                if (progressBarHandler != null) {
                    progressBarHandler.hide();
                    progressBarHandler = null;
                }
                responseStr = "0";
            }
            else if((responseStr.trim().equals("0"))){


                if(firstTime == false) {
                    firstTime = true;

                    if (((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    } else {
                        for (int j = 0; j < url_maps.size(); j++) {
                            DefaultSliderView textSliderView = new DefaultSliderView(context);
                            textSliderView
                                    .description("")
                                    .image(url_maps.get(j))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", "");

                            mDemoSlider.addSlider(textSliderView);
                        }
                    }
                }
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(10000);
                mDemoSlider.addOnPageChangeListener(this);
                sliderRelativeLayout.setVisibility(View.VISIBLE);
            }

                else {
                if (mProgressBarHandler != null) {
                    mProgressBarHandler.hide();
                    mProgressBarHandler = null;
                }
                boolean isNetwork = Util.checkNetwork(context);


                if (isNetwork == true) {

                    my_recycler_view.setLayoutManager(mLayoutManager);
                    adapter = new RecyclerViewDataAdapter(context, allSampleData, url_maps, firstTime, MainActivity.vertical);
                    my_recycler_view.setAdapter(adapter);
                    my_recycler_view.setVisibility(View.VISIBLE);


                    asynLoadVideos = new AsynLoadVideos();
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor, menuList.get(counter).getSectionId());
                    // default data
                    /*asynLoadVideos = new AsynLoadVideos();
                    asynLoadVideos.executeOnExecutor(threadPoolExecutor,menuList.get(counter).getSectionId());*/

                } else {
                    noInternetLayout.setVisibility(View.VISIBLE);
                }
            }
            return;
        }

        @Override
        protected void onPreExecute() {

            progressBarHandler = new ProgressBarHandler(getActivity());
            progressBarHandler.show();

        }

        @Override
        public void onSliderClick(BaseSliderView slider) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void myOnKeyDown(){
        //do whatever you want here
        if (asynLoadMenuItems != null){
            asynLoadMenuItems.cancel(true);
        }

        if (asynLoadVideos != null){
            asynLoadVideos.cancel(true);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        song_p_name.setText(Util.SongName);
        Artist_p_name.setText(Util.ArtistName);
        if (pause_controller.equals("show")&& pause_controller !=null){
            minicontroller_layout_relative.setVisibility(View.VISIBLE);
        }else{
            minicontroller_layout_relative.setVisibility(View.GONE);
        }
        if (mediaPlayer.isPlaying()) {
            minicontroller_layout_relative.setVisibility(View.VISIBLE);
            albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            if (Util.close.trim().equals("multi")){

                minicontroller_layout_relative.setVisibility(View.GONE);
                Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();

            }else{

            }


        }

        if (mProgressBarHandler != null) {
            mProgressBarHandler.hide();
            mProgressBarHandler = null;
        }

        getActivity().invalidateOptionsMenu();

    }
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.media_route_menu_item:
            // Not implemented here
            return false;
        default:
            break;
    }

    return false;
}
    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressBarHandler phandler;

        protected Void doInBackground(String... urls) {
            try {


                URL url = new URL(urls[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                videoHeight = bmp.getHeight();
                videoWidth = bmp.getWidth();


                Log.v("SUBHA", "videoHeight=============="+videoHeight);
                Log.v("SUBHA", "videoWidth=============="+videoWidth);

                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed

            if(phandler!=null && phandler.isShowing())
            {
                phandler.hide();
            }

            Log.v("SUBHA", "HHH");
            loadui = new AsynLOADUI();
            loadui.executeOnExecutor(threadPoolExecutor);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phandler = new ProgressBarHandler(getActivity());
            phandler.show();

        }
    }
    private BroadcastReceiver SongStatusReciverhome = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            // Extract data included in the Intent
            song_status = (intent.getStringExtra("songStatus")).trim();
            if (song_status.equals("play")) {
                albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
                //player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
                minicontroller_layout_relative.setVisibility(View.VISIBLE);
            }
            if (song_status.equals("pause")) {
                albumArt_player.setImageResource(R.drawable.play_icon);
                //player_play_ic.setImageResource(R.drawable.player_play_ic);
                minicontroller_layout_relative.setVisibility(View.VISIBLE);
            }
            if (song_status.contains("close")){

                minicontroller_layout_relative.setVisibility(View.GONE);
            }
            if (song_status.contains("@@@@@")) {
                final String data[] = song_status.split("@@@@@");
                seekbar_botomSht.setMax(Integer.parseInt(data[1]));
                seekbar_botomSht.setProgress(Integer.parseInt(data[0]));
            }

        }
    };
}





