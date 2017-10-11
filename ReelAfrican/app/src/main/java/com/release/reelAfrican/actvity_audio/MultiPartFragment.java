package com.release.reelAfrican.actvity_audio;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.GetContentDetailsAsynTask;
import com.home.apisdk.apiController.GetEpisodeDeatailsAsynTask;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.*;
import com.release.reelAfrican.activity.HomeFragment;
import com.release.reelAfrican.adapter_audio.MultiDataAdaptor;
import com.release.reelAfrican.adapter_audio.SingleDataAdaptor;
import com.release.reelAfrican.service.MusicService;
import com.release.reelAfrican.utils.Constants;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.R.attr.name;
import static android.content.Context.MODE_PRIVATE;
import static com.release.reelAfrican.R.layout.minicontroller;
import static com.release.reelAfrican.utils.Util.mediaPlayer;
import static com.release.reelAfrican.utils.Util.pause_controller;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiPartFragment extends Fragment implements GetContentDetailsAsynTask.GetContentDetails ,GetValidateUserAsynTask.GetValidateUser,GetEpisodeDeatailsAsynTask.GetEpisodeDetails{
    ///////////////

    private Paint p = new Paint();
    ContentDetailsOutput contentDetailsOutput;
    Episode_Details_output episode_details_output_model;
    ArrayList<Episode_Details_output> episode_details_output;
    ArrayList<Episode_Details_output> episode_details_output_arr=new ArrayList<Episode_Details_output>();
    private View view;
    String Poster;
    ProgressBarHandler progressHandler;
    SingleDataAdaptor adapter;
    MultiDataAdaptor multiAdapter;
    String desired_string,content_types_id,user_id;
    ImageView banner_image,favourite;
    String banner,artist_multi;
    SQLiteDatabase DB;
    int Position;
    SharedPreferences pref;
    TextView SongCount;
    TextView albumName_multipart;
    RelativeLayout controller;
    ////////////////////
    RecyclerView my_recycler_view;
    SharedPreferences prefs;
    String userId;
    Toolbar toolbar;
    String album_name, song_url, song_imageUrl, song_name,genere,artist_name;
    ProgressBarHandler pDialog;
     String movieId;
    private String movieUniqueIdmulti;
    private int isFavorite;
    private String movieUniqueId;
    private String isEpisode;
    private String isEpisodeMulti;
    private String loggedInStr;

    private String poster;
    private String MutiArtist;
    ImageView miniControl_play,albumArt_player,player_play_ic,open_bottomSheet;

    String song_status = null;
    RelativeLayout minicontroller;
    TextView curent_duration, total_duration, song_p_name, Artist_p_name;

    SeekBar seekBar, seekbar_botomSht;
    String bannerimage,posterimage;

    ArrayList<ContentDetailsOutput> contentDetailsOutputArrayList = new ArrayList<ContentDetailsOutput>();



    public MultiPartFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver DELETE_ACTION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String movieUniqId = intent.getStringExtra("movie_uniq_id").trim();

            if(content_types_id.equals("5")) {

                if (movieUniqId.equals(movieUniqueId.trim())) {
                    isFavorite = 0;
                    favourite.setImageResource(R.drawable.favorite_unselected);
                }
            }else{
                if (movieUniqId.equals(movieUniqueIdmulti.trim())) {
                    isFavorite = 0;
                    favourite.setImageResource(R.drawable.favorite_unselected);
                }
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_multi_part, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(DELETE_ACTION, new IntentFilter("ITEM_STATUS"));


        String imgurl_up= "https://sampledesign.muvi.com/mobileaudio/testimage1.png";
        ImageView banner_overlay = (ImageView) v.findViewById(R.id.overlay);
        favourite = (ImageView) v.findViewById(R.id.favourite_multi);
        Picasso.with(getActivity())
                .load(imgurl_up)
                .error(R.drawable.no_image)
                .into(banner_overlay);
        String imgurl_bottom= "https://sampledesign.muvi.com/mobileaudio/testimage2.png";
        ImageView banner_overlay_down = (ImageView) v.findViewById(R.id.overlay2);


        /***favorite *****/

        favourite.setVisibility(View.GONE);


        favourite.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {


                Log.v("SUBHA","is Favourite == "+isFavorite);

                    if (isFavorite==1){
                        Log.v("goofy","Item deleted");
                        AsynFavoriteDelete asynFavoriteDelete=new AsynFavoriteDelete();
                        asynFavoriteDelete.execute();

                    }else{
                        AsynFavoriteAdd asynFavoriteAdd =new AsynFavoriteAdd();
                        asynFavoriteAdd.execute();

                    }



            }
        });
        /***favorite *****/


        Picasso.with(getActivity())
                .load(imgurl_bottom)
                .error(R.drawable.no_image)
                .into(banner_overlay_down);
        DB = getActivity().openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);



                /*
        @Bundle arguments capture from Adaptor(permalink)
         @#GetContaint details used for unique data .
         */
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        desired_string = arguments.getString("PERMALINK");
        content_types_id = arguments.getString("CONTENT_TYPE");
        user_id = arguments.getString("USER_ID");
        Util.content_types_id=content_types_id;
        Log.v("Niharuuu", "" + desired_string);
        Log.v("Niharuuu", "" + content_types_id);
        pDialog = new ProgressBarHandler(getActivity());
        pDialog.show();
        if (content_types_id.equals("6")){
            if (Util.checkNetwork(getActivity()) == true) {
                AsyncgetMultiDetails asyncgetMultiDetails= new AsyncgetMultiDetails();
                asyncgetMultiDetails.execute();
            } else {
                Util.showToast(getActivity(), Util.getTextofLanguage(getActivity(), Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }

            Episode_Details_input episode_details_input = new Episode_Details_input();
            episode_details_input.setAuthtoken(Util.authTokenStr);
            episode_details_input.setPermalink(desired_string);
            episode_details_input.setOffset("0");
            episode_details_input.setLimit("10");
            GetEpisodeDeatailsAsynTask getEpisodeDeatailsAsynTask = new GetEpisodeDeatailsAsynTask(episode_details_input,this,getActivity());
            getEpisodeDeatailsAsynTask.execute();
        }
        else{
            if (pref != null)
                user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
            contentDetailsInput.setAuthToken(Util.authTokenStr);
            contentDetailsInput.setPermalink(desired_string);
            contentDetailsInput.setUser_id(user_id);
            GetContentDetailsAsynTask getContentDetailsAsynTask = new GetContentDetailsAsynTask(contentDetailsInput, this, getActivity());
            getContentDetailsAsynTask.execute();
        }

        progressHandler = new ProgressBarHandler(getActivity());
        my_recycler_view = (RecyclerView) v.findViewById(R.id.list_recyclerView);
      //  my_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        my_recycler_view.setHasFixedSize(true);
        banner_image = (ImageView) v.findViewById(R.id.banner_image);
        albumName_multipart = (TextView) v.findViewById(R.id.albumName_multipart);
        SongCount = (TextView) v.findViewById(R.id.SongCount);
        Typeface albumName_multipart_tf = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.regular_fonts));
        albumName_multipart.setTypeface(albumName_multipart_tf);
        Typeface SongCount_tf = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.light_fonts));
        SongCount.setTypeface(SongCount_tf);

        albumArt_player = (ImageView) v.findViewById(R.id.miniControl_play);
        albumArt_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pintent = new Intent("SERVICE_ACTION_NEXT");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(Pintent);
            }
        });
        player_play_ic = (ImageView) v.findViewById(R.id.player_play_ic);
        minicontroller = (RelativeLayout) v.findViewById(R.id.bottomSheetLayout);

        curent_duration = (TextView) v.findViewById(R.id.curent_duration);
        total_duration = (TextView) v.findViewById(R.id.total_duration);
        song_p_name = (TextView) v.findViewById(R.id.song_p_name);
        Artist_p_name = (TextView) v.findViewById(R.id.song_p_Genre);

        seekbar_botomSht = (SeekBar) v.findViewById(R.id.miniController_seekbar);

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.minicontroller, null);
        controller=(RelativeLayout) v.findViewById(R.id.minicontroller_layout_relative);
        //controller.setVisibility(View.VISIBLE);
       if (mediaPlayer.isPlaying()){
           controller.setVisibility(View.VISIBLE);
       }else{
           controller.setVisibility(View.GONE);
       }
        open_bottomSheet=(ImageView)v.findViewById(R.id.open_bottomSheet);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(CLOSE_NOTIFiCATION, new IntentFilter("CLOSE_NOTI"));

        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AudioPlayerActivity.class);
                if(content_types_id.equals("5")) {
                    intent.putExtra("bannerimage", bannerimage);
                    intent.putExtra("posterimage", posterimage);
                    intent.putExtra("content_types_id", content_types_id);
                    intent.putExtra("Content", contentDetailsOutputArrayList);
                    Log.v("pratik", String.valueOf(contentDetailsOutputArrayList.size()));
                }if(content_types_id.equals("6")){
                    intent.putExtra("bannerimage", banner);
                    intent.putExtra("posterimage", poster);
                    intent.putExtra("content_types_id", content_types_id);
                    intent.putExtra("Content_multipart", episode_details_output);
                    Log.v("pratik", String.valueOf(episode_details_output.size()));
                }
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(SongStatusReciver, new IntentFilter("SONG_STATUS"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(notificationStatus, new IntentFilter("SONG_STATUS_NOTI"));



        prefs = getActivity().getSharedPreferences(Util.LOGIN_PREF, 0);
         userId = prefs.getString("PREFS_LOGGEDIN_ID_KEY", null);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(SONG_POSITION, new IntentFilter("SONG_POSITION"));


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {


                        Log.v("SUBHA","onbackpressed ===== called");
                        getActivity().finish();


                      /*  final Intent startIntent = new Intent(getActivity(), MainActivity.class);

                        getActivity().runOnUiThread(new Runnable() {

                            public void run() {
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(startIntent);
                                getActivity().finish();

                            }
                        });*/
                    }
                }
                return false;
            }
        });


        return v;
    }




    private BroadcastReceiver CLOSE_NOTIFiCATION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responce = intent.getStringExtra("closeNotification");
            LocalBroadcastManager.getInstance(context).unregisterReceiver(SongStatusReciver);
            //Toast.makeText(context, "MultiFrgment noti", Toast.LENGTH_SHORT).show();
            if (responce.equals("close")){


                controller.setVisibility(View.GONE);

            }

        }
    };

    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }



    }
    private BroadcastReceiver notificationStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            controller.setVisibility(View.GONE);

        }

    };
    private BroadcastReceiver SONG_POSITION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String Song_status=intent.getStringExtra("songStatusNumber");
//            Log.v("nihar_Status_multipart",Song_status);


        }

    };

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
         bannerimage = contentDetailsOutput.getBanner();
         posterimage = contentDetailsOutput.getPoster();
        Util.bannerimage=bannerimage;
        Util.posterimage=posterimage;

        if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                .trim()).equals("1")) {
            favourite.setVisibility(View.VISIBLE);
        }

//        Toast.makeText(getActivity(),posterimage,Toast.LENGTH_LONG).show();

        String containt_name = contentDetailsOutput.getName();
        String url = contentDetailsOutput.getMovieUrl();
        isFavorite=contentDetailsOutput.getIsFavorite();
        Log.v("Nihar_url",""+url);
        Log.v("Niharuuu", "postexe.." + content_types_id);
//try {
    movieUniqueId=contentDetailsOutput.getMuviUniqId();
    isEpisode=contentDetailsOutput.getIsEpiosde();
    String perma=contentDetailsOutput.getPermalink();

        Log.v("SUBHA","isFavorite == "+isFavorite);

        /***favorite *****/
        String loggedInStr = prefs.getString("PREFS_LOGGEDIN_KEY", null);
        if(loggedInStr != null && isFavorite== 0 && Util.favorite_clicked == true){

            Util.favorite_clicked = false;

            AsynFavoriteAdd  asynFavoriteAdd = new AsynFavoriteAdd();
            asynFavoriteAdd.execute();
        }
        else if (loggedInStr != null && isFavorite==1){

            favourite.setImageResource(R.drawable.favorite_red);
        }
        /***favorite *****/

    this.contentDetailsOutput = contentDetailsOutput;

    String artist_name=contentDetailsOutput.getArtist();
    albumName_multipart.setText(containt_name);

try {


    if (bannerimage != null) {
        Picasso.with(getActivity())
                .load(bannerimage)
                .error(R.drawable.no_image)
                .into(banner_image);
        Log.v("NIahr_bnner", "+banner" + banner);
    } else {
        Picasso.with(getActivity())
                .load(posterimage)
                .error(R.drawable.no_image)
                .into(banner_image);
        Log.v("NIahr_bnner", "+poster" + poster);
    }
}catch (Exception e){
    Log.v("nihar_exception",""+e.toString());
}
finally {
    Picasso.with(getActivity())
            .load(posterimage)
            .error(R.drawable.no_image)
            .into(banner_image);
    Log.v("NIahr_bnner", "+poster" + poster);
}


    SongCount.setText("1"+" "+"Track");

    contentDetailsOutputArrayList.add(contentDetailsOutput);
        Util.contentDetailsOutputArrayList = contentDetailsOutputArrayList;
    Intent CONTENT_OUTPUT = new Intent("CONTENT_OUTPUT1");
    CONTENT_OUTPUT.putExtra("Content", contentDetailsOutputArrayList);
    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CONTENT_OUTPUT);
    adapter = new SingleDataAdaptor(getActivity(), contentDetailsOutput, MultiPartFragment.this);
    my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    my_recycler_view.setAdapter(adapter);

}




    public void PlaySongs(ContentDetailsOutput item, boolean isClicked) {
        if (isClicked){
            ValidateUserInput validateUserInput= new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userId);
            Log.v("niharId",""+item.getMuviUniqId());
            Log.v("niharId","user id=="+userId);
            validateUserInput.setMuviUniqueId(item.getMuviUniqId());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");

            GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput,this,getActivity());
            getValidateUserAsynTask.execute();

            song_p_name.setText(Util.SongName);
            Artist_p_name.setText(Util.ArtistName);

            contentDetailsOutput = item;
            album_name = item.getName();
            song_url = item.getMovieUrl();
            genere=item.getGenre();
            song_imageUrl = item.getPoster();
            song_name = item.getName();
            song_p_name.setText(song_name);
            artist_name = item.getArtist();
            Artist_p_name.setText(artist_name);
            Player_State(1);
        } else {

        }
        Log.v("nihar_nihar", "" + item.getName());



    }
    public void PlaySongsmulti(Episode_Details_output item, boolean isClicked, int adapterPosition) {
        if (isClicked){
            ValidateUserInput validateUserInput= new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userId);
            validateUserInput.setMuviUniqueId(item.getMuvi_uniq_id());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            song_p_name.setText(Util.SongName);
            Artist_p_name.setText(Util.ArtistName);
            GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput,this,getActivity());
            getValidateUserAsynTask.execute();
            episode_details_output_model = item;
            album_name = item.getName();
             song_url = item.getVideo_url();
            song_imageUrl = item.getPoster_url();
            genere=item.getGenre();
            song_name = item.getEpisode_title();
            artist_name=MutiArtist;
            //episode_details_output_arr.add(item);
            Util.episode_details_output = episode_details_output;
            Intent CONTENT_OUTPUT = new Intent("CONTENT_OUTPUT2");
            CONTENT_OUTPUT.putExtra("Content_multipart", episode_details_output);
            CONTENT_OUTPUT.putExtra("position_item", adapterPosition);
            CONTENT_OUTPUT.putExtra("artist", artist_name);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(CONTENT_OUTPUT);
        } else {

        }
//        Log.v("nihar_multi", "" + song_url_multipart);



    }
    public void Player_State(int funId) {

        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", album_name);
        Log.v("nihar3", "" + album_name);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(playerData);


       /* String DEL_QRY = "DELETE FROM " + Util.USER_TABLE_NAME +"";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.USER_TABLE_NAME + "(ALBUM_ART_PATH,ALBUM_SONG_NAME) VALUES ( '" + song_imageUrl + "','" + song_name + "')";
        DB.execSQL(INS_QRY);*/
    try {
    Intent j = new Intent(getContext(), MusicService.class);
    j.putExtra("ALBUM", song_url);
    j.putExtra("PERMALINK", desired_string);
    j.putExtra("POSITION", Position);
    j.putExtra("ALBUM_ART", song_imageUrl);
    j.putExtra("ALBUM_NAME", name);
    j.putExtra("Artist", artist_name);
    j.putExtra("ALBUM_SONG_NAME", song_name);
    j.putExtra("STATE", funId);
    j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
    getContext().startService(j);
    }catch (Exception e ){

    }


    }


    private BroadcastReceiver SongStatusReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            // Extract data included in the Intent
            song_status = (intent.getStringExtra("songStatus")).trim();
            if (song_status.equals("play")) {
                albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
                //player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
                controller.setVisibility(View.VISIBLE);
            }
            if (song_status.equals("pause")) {
                pause_controller = "show";
                albumArt_player.setImageResource(R.drawable.play_icon);
                //player_play_ic.setImageResource(R.drawable.player_play_ic);
                controller.setVisibility(View.VISIBLE);
            }
            if (song_status.contains("close")){

                controller.setVisibility(View.GONE);
            }

            if (song_status.contains("@@@@@")) {
                final String data[] = song_status.split("@@@@@");
                seekbar_botomSht.setMax(Integer.parseInt(data[1]));
                seekbar_botomSht.setProgress(Integer.parseInt(data[0]));
            }


        }
    };

    public static String timeC(long dur) {
        Log.v("Nihar", " duration=" + dur);
        String strDate = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(dur) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur)));
        Log.v("Nihar", "audio duration=" + strDate);
        return strDate;
    }


    @Override
    public void onResume() {
        if (pause_controller.equals("show")&& pause_controller !=null){
            controller.setVisibility(View.VISIBLE);
        }else{
            if (mediaPlayer.isPlaying()){
                controller.setVisibility(View.VISIBLE);
            }
            else
            {
                controller.setVisibility(View.GONE);
            }

        }

        song_p_name.setText(Util.SongName);
        Artist_p_name.setText(Util.ArtistName);
        if (mediaPlayer.isPlaying()){
            albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
        }else{
            albumArt_player.setImageResource(R.drawable.play_icon);
        }

        super.onResume();
    }

    @Override
    public void onGetValidateUserPreExecuteStarted() {
        pDialog.show();
    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
        Log.v("nihar_payment","==========================="+message+"    "+validateUserOutput.getValiduser_str());
        Log.v("pratikl","status--"+status);
        if (status == 427){

            Toast.makeText(getActivity(), "Sorry , This content is  not available in your country . ", Toast.LENGTH_SHORT).show();
        }
//        else if (status == 425 || status == 426){
//
//            Intent intent=new Intent(getActivity(),SubscriptionActivity.class);
//            intent.putExtra("movieId",movieId);
//            Bundle arguments=getArguments();
//            intent.putExtra("permalink",arguments.getString("PERMALINK"));
//            intent.putExtra("content_types_id",arguments.getString("CONTENT_TYPE"));
//            startActivity(intent);
//        }
        else if (status == 429){

            Player_State(1);
        }
        else if (status == 430)
        {
//            message.equals("Unpaid") &&
//            Log.v("nihar_payment",""+validateUserOutput.getIsMemberSubscribed());
//            Toast.makeText(getActivity(), validateUserOutput.getIsMemberSubscribed() , Toast.LENGTH_SHORT).show();
           if ( validateUserOutput.getIsMemberSubscribed().equals("0")){

               /////////////////Payment
               /* Intent intent=new Intent(getActivity(),SubscriptionActivity.class);
                intent.putExtra("movieId",movieId);
                Bundle arguments=getArguments();
                intent.putExtra("permalink",arguments.getString("PERMALINK"));
                intent.putExtra("content_types_id",arguments.getString("CONTENT_TYPE"));
                startActivity(intent);*/

               Player_State(1);

            }else {
                Player_State(1);
            }

//            Player_State(1);
        }else {
            Player_State(1);
        }
    }

    @Override
    public void onGetEpisodeDetailsPreExecuteStarted() {

    }

    @Override
    public void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output, int i, int status, String message) {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }




        
        SongCount.setText(episode_details_output.size()+" "+"Tracks");
            this.episode_details_output = episode_details_output;

        String containt_name = episode_details_output.get(0).getName();
        albumName_multipart.setText(containt_name);
        if (banner != null && ! banner.equals("")){
            Picasso.with(getActivity())
                    .load(banner)
                    .error(R.drawable.no_image)
                    .into(banner_image);
            Log.v("NIahr_bnner","+banner"+banner);
        }else{
            Picasso.with(getActivity())
                    .load(poster)
                    .error(R.drawable.no_image)
                    .into(banner_image);
            Log.v("NIahr_bnner","+poster"+poster);
        }

        Log.v("Nihar_artist","fragment"+artist_multi);
        multiAdapter = new MultiDataAdaptor(getActivity(), episode_details_output, MultiPartFragment.this,MutiArtist);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(multiAdapter);
        multiAdapter.notifyDataSetChanged();

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
       *//* menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.nav_Search).setVisible(true);*//*

    }*/

    public class AsyncgetMultiDetails extends AsyncTask<Void,Void,Void> {

        ContentDetailsInput contentDetailsInput;
        String responseStr,message;
        int status;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.detailsUrl.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);
                httppost.addHeader("permalink",desired_string );

                if (pref != null)
                    user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);


                if (user_id != null) {
                    httppost.addHeader("user_id", user_id);
                }else{
                    httppost.addHeader("user_id", "");
                }


                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                    status = 0;



                } catch (IOException e) {
                    status = 0;
                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("msg");
                }

                if (status > 0) {

                    if (status == 200) {
                        JSONObject mainJson = myJson.getJSONObject("movie");
                        poster = mainJson.getString("poster");
                        Util.poster=poster;
                        movieUniqueIdmulti = mainJson.getString("muvi_uniq_id");
                        try {
                            isFavorite = Integer.parseInt(mainJson.getString("is_favorite"));
                            poster = mainJson.getString("poster");

                            if (mainJson.has("cast_detail")) {
                                JSONArray castArray = mainJson.getJSONArray("cast_detail");
                                Log.v("SUBHA", "cast_detail" + castArray.length());


                                StringBuilder sb = new StringBuilder();
                                if (castArray.length() > 0) {
                                    for (int i = 0; i < castArray.length(); i++) {
                                        JSONObject jsonChildNode = castArray.getJSONObject(i);
                                        if (jsonChildNode.has("celeb_name")){
                                            sb.append(jsonChildNode.getString("celeb_name").toString());
                                            if (i != castArray.length() - 1) {
                                                sb.append(",");
                                            }

                                        }

                                    }
                                     MutiArtist = sb.toString();
//                                    contentDetailsOutput.setArtist(sb.toString());
                                    Log.v("SUBHA","SB"+sb.toString());

                                }else{

                                }
                            } else {


                            }

                        }catch (Exception e){
                            Log.v("nihare","isfavourite===errror=="+e.toString());
                        }
                        Log.v("Nihar_multi","poster=================================================="+poster);
                        Log.v("Nihar_multi","movieUniqueIdmulti" + movieUniqueIdmulti);



                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
                            Log.v("Nihar_multi","============================EnterName");

                            if(banner!=null && banner!=""){

                                banner =  mainJson.getString("banner");
                                Util.banner=banner;

                            }else {

                                banner = "";
                            }

//                            contentDetailsOutput.setName(mainJson.getString("name"));
                        } else {
                            contentDetailsOutput.setName("");

                        }
                        if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                            Log.v("Nihar_multi","============================EnterName");
                            Log.v("Nihar_multi","============================Enter"+mainJson.getString("name"));
                            contentDetailsOutput.setName(mainJson.getString("name"));



                        } else {
                            contentDetailsOutput.setName("");

                        }

                        if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                            contentDetailsOutput.setGenre(mainJson.getString("genre"));


                        } else {
                            contentDetailsOutput.setGenre("");

                        }
                        if ((mainJson.has("censor_rating")) && mainJson.getString("censor_rating").trim() != null && !mainJson.getString("censor_rating").trim().isEmpty() && !mainJson.getString("censor_rating").trim().equals("null") && !mainJson.getString("censor_rating").trim().matches("")) {
                            contentDetailsOutput.setCensorRating(mainJson.getString("censor_rating"));


                        } else {
                            contentDetailsOutput.setCensorRating("");

                        }
                        if ((mainJson.has("story")) && mainJson.getString("story").trim() != null && !mainJson.getString("story").trim().isEmpty() && !mainJson.getString("story").trim().equals("null") && !mainJson.getString("story").trim().matches("")) {
                            contentDetailsOutput.setStory(mainJson.getString("story"));
                        } else {
                            contentDetailsOutput.setStory("");

                        }
                        if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                            contentDetailsOutput.setTrailerUrl(mainJson.getString("trailerUrl"));
                        } else {
                            contentDetailsOutput.setTrailerUrl("");

                        }

                        if ((mainJson.has("movie_stream_uniq_id")) && mainJson.getString("movie_stream_uniq_id").trim() != null && !mainJson.getString("movie_stream_uniq_id").trim().isEmpty() && !mainJson.getString("movie_stream_uniq_id").trim().equals("null") && !mainJson.getString("movie_stream_uniq_id").trim().matches("")) {
                            contentDetailsOutput.setMovieStreamUniqId(mainJson.getString("movie_stream_uniq_id"));
                            movieUniqueIdmulti = mainJson.getString("movie_stream_uniq_id");


                        } else {
                            contentDetailsOutput.setMovieStreamUniqId("");

                        }

                        try {
                            movieUniqueIdmulti = mainJson.getString("muvi_uniq_id");
                        }catch (Exception e){

                        }

                        if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {

                            contentDetailsOutput.setMuviUniqId(mainJson.getString("muvi_uniq_id"));




                        } else {
                            contentDetailsOutput.setMuviUniqId("");

                        }

                        if ((mainJson.has("is_episode")) && mainJson.getString("is_episode").trim() != null && !mainJson.getString("is_episode").trim().isEmpty() && !mainJson.getString("is_episode").trim().equals("null") && !mainJson.getString("is_episode").trim().matches("")) {
                            contentDetailsOutput.setIsEpiosde(mainJson.getString("is_episode"));
                            isEpisodeMulti = mainJson.getString("is_episode");
                        } else {
                            contentDetailsOutput.setIsEpiosde("");

                        }
                        if ((mainJson.has("is_favorite")) && mainJson.getString("is_favorite").trim() != null && !mainJson.getString("is_favorite").trim().isEmpty() && !mainJson.getString("is_favorite").trim().equals("null") && !mainJson.getString("is_favorite").trim().matches("")) {
                            isFavorite = Integer.parseInt(mainJson.getString("is_favorite"));

                        }

                        if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {
                            contentDetailsOutput.setMovieUrl(mainJson.getString("movieUrl"));

                        } else {
                            contentDetailsOutput.setMovieUrl("");

                        }

//                        if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
//                            banner =  mainJson.getString("banner");
//                            Log.v("Nihar_multi","============================Enter"+mainJson.getString("banner"));
//
//                        } else {
//                            contentDetailsOutput.setBanner("");
//
//                        }

                        Log.v("android_poster","=======================banner"+"called"+mainJson.getString("banner"));

                       /* if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                            Poster = mainJson.getString("poster");
                        } else {
                            contentDetailsOutput.setPoster("");

                        }
                        Log.v("android_poster","=======================poster"+"called"+Poster);*/

                        if ((mainJson.has("isFreeContent")) && mainJson.getString("isFreeContent").trim() != null && !mainJson.getString("isFreeContent").trim().isEmpty() && !mainJson.getString("isFreeContent").trim().equals("null") && !mainJson.getString("isFreeContent").trim().matches("")) {
                            contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));
                        } else {
                            contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));

                        }
                        if ((mainJson.has("release_date")) && mainJson.getString("release_date").trim() != null && !mainJson.getString("release_date").trim().isEmpty() && !mainJson.getString("release_date").trim().equals("null") && !mainJson.getString("release_date").trim().matches("")) {
                            contentDetailsOutput.setReleaseDate(mainJson.getString("release_date"));
                        } else {
                            contentDetailsOutput.setReleaseDate(mainJson.getString("isFreeContent"));

                        }
                        if ((mainJson.has("is_ppv")) && mainJson.getString("is_ppv").trim() != null && !mainJson.getString("is_ppv").trim().isEmpty() && !mainJson.getString("is_ppv").trim().equals("null") && !mainJson.getString("is_ppv").trim().matches("")) {
                            contentDetailsOutput.setIsPpv(Integer.parseInt(mainJson.getString("is_ppv")));
                        } else {
                            contentDetailsOutput.setIsPpv(0);

                        }
                        if ((mainJson.has("is_converted")) && mainJson.getString("is_converted").trim() != null && !mainJson.getString("is_converted").trim().isEmpty() && !mainJson.getString("is_converted").trim().equals("null") && !mainJson.getString("is_converted").trim().matches("")) {
                            contentDetailsOutput.setIsConverted(Integer.parseInt(mainJson.getString("is_converted")));
                        } else {
                            contentDetailsOutput.setIsConverted(0);

                        }
                        if ((mainJson.has("is_advance")) && mainJson.getString("is_advance").trim() != null && !mainJson.getString("is_advance").trim().isEmpty() && !mainJson.getString("is_advance").trim().equals("null") && !mainJson.getString("is_advance").trim().matches("")) {
                            contentDetailsOutput.setIsApv(Integer.parseInt(mainJson.getString("is_advance")));
                        } else {
                            contentDetailsOutput.setIsApv(0);

                        }

                    }
                } else {

                    responseStr = "0";
                    status = 0;
                    message = "Error";
                }
            } catch (final JSONException e1) {

                responseStr = "0";
                status = 0;
                message = "Error";

            } catch (Exception e) {

                responseStr = "0";
                status = 0;
                message = "Error";
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.v("Nihar_favorite",""+isFavorite);

            if ((Util.getTextofLanguage(getActivity(), Util.HAS_FAVORITE, Util.DEFAULT_HAS_FAVORITE)
                    .trim()).equals("1")) {
                favourite.setVisibility(View.VISIBLE);
            }
            /***favorite *****/
            String loggedInStr = prefs.getString("PREFS_LOGGEDIN_KEY", null);
            if(loggedInStr != null && isFavorite== 0 && Util.favorite_clicked == true){

                Util.favorite_clicked = false;

                AsynFavoriteAdd  asynFavoriteAdd = new AsynFavoriteAdd();
                asynFavoriteAdd.execute();
            }
            else if (loggedInStr != null && isFavorite==1){

                favourite.setImageResource(R.drawable.favorite_red);
            }
            /***favorite *****/
        }
    }
    private class AsynFavoriteAdd extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;
        String contMessage;
        String responseStr;
         String sucessMsg;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());

                if(content_types_id.equals("5")){
                    httppost.addHeader("movie_uniq_id", movieUniqueId);
                    Log.v("pratikf","muidji when 5=="+movieUniqueId);
                }else {
                    httppost.addHeader("movie_uniq_id", movieUniqueIdmulti);
                    Log.v("pratikf","else  muidji when 5==");
                }
                if (pref != null)
                    user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                httppost.addHeader("user_id", user_id);
                Log.v("pratikf","uiddd== === "+user_id);

                if(content_types_id.equals("5")){
                    httppost.addHeader("content_type", isEpisode);
                    Log.v("pratikf","isEpisode == === "+isEpisode);
                }
                else{
                    httppost.addHeader("content_type", isEpisodeMulti);
                    Log.v("pratikf","isEpisodeMulti == === "+isEpisodeMulti);

                }

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                    Log.v("pratikf","add to fav response="+responseStr);
                    Toast.makeText(getActivity().getApplicationContext(), ""+responseStr, Toast.LENGTH_SHORT).show();

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            }catch (Exception e){

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

            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }
            Toast.makeText(getActivity(), sucessMsg, Toast.LENGTH_SHORT).show();
            isFavorite=1;
            favourite.setImageResource(R.drawable.favorite_red);


        }
    }    ////////Asyn deleteFav
    private class AsynFavoriteDelete extends AsyncTask<String, Void, Void> {

        String contName;
        JSONObject myJson = null;
        int status;
        ProgressBarHandler pDialog;

        String contMessage;
        String responseStr;
        private String sucessMsg;

        @Override
        protected Void doInBackground(String... strings) {
            String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
                if(content_types_id.equals("5")){
                    httppost.addHeader("movie_uniq_id", movieUniqueId);
                }else {
                    httppost.addHeader("movie_uniq_id", movieUniqueIdmulti);
                }
                if(content_types_id.equals("5")){
                    httppost.addHeader("content_type", isEpisode);
                    Log.v("pratikf","isEpisode == === "+isEpisode);
                }
                else{
                    httppost.addHeader("content_type", isEpisodeMulti);
                    Log.v("pratikf","isEpisodeMulti == === "+isEpisodeMulti);

                }
                if (pref != null)
                    user_id= pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
                httppost.addHeader("user_id", user_id);
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

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



            favourite.setImageResource(R.drawable.favorite_unselected);

            isFavorite = 0;
            if(pDialog.isShowing()&& pDialog!=null)
            {
                pDialog.hide();
            }

            Toast.makeText(getActivity(), ""+sucessMsg, Toast.LENGTH_SHORT).show();
            //  adapter.removeItem(position);





        }
        @Override
        protected void onPreExecute() {

            pDialog=new ProgressBarHandler(getActivity());
            pDialog.show();

        }
    }

}
