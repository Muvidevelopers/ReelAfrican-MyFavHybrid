package com.release.reelAfrican.actvity_audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.home.apisdk.apiController.GetValidateUserAsynTask;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.home.apisdk.apiModel.ValidateUserInput;
import com.home.apisdk.apiModel.ValidateUserOutput;
import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.*;

import com.release.reelAfrican.adapter_audio.PlayerDataAdaptor;
import com.release.reelAfrican.adapter_audio.PlayerDataMultipartAdaptor;
import com.release.reelAfrican.service.MusicService;
import com.release.reelAfrican.utils.Constants;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.R.attr.name;
import static com.release.reelAfrican.utils.Util.mediaPlayer;

public class AudioPlayerActivity extends AppCompatActivity implements GetValidateUserAsynTask.GetValidateUser{

    ImageView player_image_background, player_image_main, player_play_ic, player_prev_ic, player_next_ic;
    ProgressBar musicProgress;
    private RecyclerView playerRecycle;
    RelativeLayout duration_layout;
    SeekBar seekBar, seekbar_botomSht;
    TextView curent_duration, total_duration, song_p_name;

    boolean isRunning = false;
    Timer durationTimer;
    int adaptorPosition;

    PlayerDataAdaptor adapter;
    PlayerDataMultipartAdaptor multipartAdaptor;

    String Song_name, Artist;
    String title, AlbumArtUrl;
    String song_status = null;
    String userid, emailid;

    SharedPreferences pref;

    ContentDetailsOutput contentDetailsOutput;

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(SongStatusReciver);
//        LocalBroadcastManager.getInstance(this).registerReceiver(PLAYER_DETAILS, new IntentFilter("PLAYER_DETAILS"));
        LocalBroadcastManager.getInstance(this).unregisterReceiver(CONTENT_OUTPUT1);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(CONTENT_OUTPUT2);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(SONG_STATUS_NEXT);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(SONG_STATUS_PREVIOUS);
    }

    String album_name, song_url, song_imageUrl, song_name;

    private int currentAdapterPosition;
    ProgressBarHandler pDialog;
    Context context;
    String banner;
    Toolbar mActionBarToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_icon_close));
        mActionBarToolbar.setTitle("");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        player_image_background = (ImageView) findViewById(R.id.player_image_background);
        ImageView player_overlay_up = (ImageView) findViewById(R.id.player_overlay_up);
        musicProgress = (ProgressBar) findViewById(R.id.progressBar);
        seekBar = (SeekBar) findViewById(R.id.Progress_music_sliderpanel);
        player_image_main = (ImageView) findViewById(R.id.player_image_main);

        playerRecycle = (RecyclerView) findViewById(R.id.playerlist_recycle);
        playerRecycle.addItemDecoration(new SimpleDividerItemDecoration(AudioPlayerActivity.this));
        playerRecycle.setHasFixedSize(true);

        curent_duration = (TextView) findViewById(R.id.curent_duration);
        total_duration = (TextView) findViewById(R.id.total_duration);

        player_play_ic = (ImageView) findViewById(R.id.player_play_ic);
        player_next_ic = (ImageView) findViewById(R.id.player_next_ic);
        player_prev_ic = (ImageView) findViewById(R.id.player_prev_ic);
        song_p_name = (TextView) findViewById(R.id.song_p_name);

        duration_layout = (RelativeLayout) findViewById(R.id.duration_layout);

        LocalBroadcastManager.getInstance(this).registerReceiver(SongStatusReciver, new IntentFilter("SONG_STATUS"));
//        LocalBroadcastManager.getInstance(this).registerReceiver(PLAYER_DETAILS, new IntentFilter("PLAYER_DETAILS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(CONTENT_OUTPUT1, new IntentFilter("CONTENT_OUTPUT1"));
        LocalBroadcastManager.getInstance(this).registerReceiver(CONTENT_OUTPUT2, new IntentFilter("CONTENT_OUTPUT2"));
        LocalBroadcastManager.getInstance(this).registerReceiver(SONG_STATUS_NEXT, new IntentFilter("SONG_STATUS_NEXT"));
        LocalBroadcastManager.getInstance(this).registerReceiver(SONG_STATUS_PREVIOUS, new IntentFilter("SONG_STATUS_PREVIOUS"));

        String imgurl_up = "https://sampledesign.muvi.com/mobileaudio/testimage1.png";
        ImageView image_overlay = (ImageView) findViewById(R.id.image_overlay);
        Picasso.with(this)
                .load(imgurl_up)
                .error(R.drawable.no_image)
                .into(player_overlay_up);
        String imgurl_bottom = "https://sampledesign.muvi.com/mobileaudio/testimage2.png";
        Picasso.with(this)
                .load(imgurl_bottom)
                .error(R.drawable.no_image)
                .into(image_overlay);


        seekBar.setPadding(0, 0, 0, 0);
       /* seekbar_botomSht = (SeekBar) findViewById(R.id.miniController_seekbar);
        seekbar_botomSht.setPadding(0, 0, 0, 0);*/


        player_play_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pintent = new Intent("SERVICE_ACTION_NEXT");
                LocalBroadcastManager.getInstance(AudioPlayerActivity.this).sendBroadcast(Pintent);
                StartTimer();
            }
        });

        player_next_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("SUBHA", "player_next_ic STATUS");

                next();

            }
        });

        player_prev_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               previous();
            }
        });

        pref = getSharedPreferences(Util.LOGIN_PREF, 0);

        userid = pref.getString("PREFS_LOGGEDIN_ID_KEY", null);
        Log.v("pratikl","user id=="+userid);

         if(getIntent().getStringExtra("bannerimage")!=null && getIntent().getStringExtra("bannerimage")!=""){

             banner=getIntent().getStringExtra("bannerimage");

             }else {

             banner="";

             }

        String posterimage=getIntent().getStringExtra("posterimage");
        String content_types_id=getIntent().getStringExtra("content_types_id");
        Log.v("pratikl","banner in audiop="+banner);
        Log.v("pratikl","posterimage in audiop="+posterimage);

        Glide.with(getApplicationContext()).load(posterimage).asBitmap().centerCrop().into(new BitmapImageViewTarget(player_image_main) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                player_image_main.setImageDrawable(circularBitmapDrawable);
            }
        });
        Picasso.with(getApplicationContext())
                .load(posterimage)
                .into(player_image_background);

        if(content_types_id.equals("5")){

            ArrayList<ContentDetailsOutput> filelist = (ArrayList<ContentDetailsOutput>) getIntent().getSerializableExtra("Content");
            ContentDetailsOutput contentDetailsOutput = new ContentDetailsOutput();
            contentDetailsOutput = filelist.get(0);
            String Song_name = contentDetailsOutput.getName();
            adapter = new PlayerDataAdaptor(context, filelist, AudioPlayerActivity.this);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            player_next_ic.setVisibility(View.GONE);
            player_prev_ic.setVisibility(View.GONE);




        }else if(content_types_id.equals("6")){
            ArrayList<Episode_Details_output> filelist2 = (ArrayList<Episode_Details_output>) getIntent().getSerializableExtra("Content_multipart");
            adaptorPosition = getIntent().getIntExtra("position_item", 0);
            String Artist_multi = getIntent().getStringExtra("artist");
            Episode_Details_output episode_details_output = new Episode_Details_output();
            Song_name = episode_details_output.getName();

            player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
            multipartAdaptor = new PlayerDataMultipartAdaptor(context, filelist2, AudioPlayerActivity.this, adaptorPosition, Artist_multi);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(multipartAdaptor);
        }


    }

    /*private BroadcastReceiver PLAYER_DETAILS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("pratikl","in player detail br");
            Song_name = intent.getStringExtra("SongName");
            Artist = intent.getStringExtra("Artist");
            Log.v("Nihar_artist", "onReceive Artist" + Artist);
            AlbumArtUrl = intent.getStringExtra("songImageUrl");
            Log.v("pratikl", "album url" + AlbumArtUrl);
            Glide.with(getApplicationContext()).load(AlbumArtUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(player_image_main) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    player_image_main.setImageDrawable(circularBitmapDrawable);
                }
            });
            Picasso.with(getApplicationContext())
                    .load(AlbumArtUrl)
                    .into(player_image_background);
            song_p_name.setText(Song_name);
            Artist_p_name.setText(Artist);
            Typeface Artist_p_name_tf = Typeface.createFromAsset(getAssets(), getString(R.string.light_fonts));
            Artist_p_name.setTypeface(Artist_p_name_tf);


        }

    };*/
    private BroadcastReceiver SONG_STATUS_NEXT = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            next();

        }
    };
    private BroadcastReceiver SONG_STATUS_PREVIOUS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            previous();

        }

    };
    private BroadcastReceiver CONTENT_OUTPUT1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("pratikl","in cont op1 br");
            ArrayList<ContentDetailsOutput> filelist = (ArrayList<ContentDetailsOutput>) intent.getSerializableExtra("Content");
            ContentDetailsOutput contentDetailsOutput = new ContentDetailsOutput();
            contentDetailsOutput = filelist.get(0);
            String Song_name = contentDetailsOutput.getName();
            Log.v("Nihar555", "" + Song_name);

            adapter = new PlayerDataAdaptor(context, filelist, AudioPlayerActivity.this);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }

    };
    private BroadcastReceiver CONTENT_OUTPUT2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Util.multiholder.clear();

            Log.v("pratikl","in cont op2 br..");

            ArrayList<Episode_Details_output> filelist2 = (ArrayList<Episode_Details_output>) intent.getSerializableExtra("Content_multipart");
            adaptorPosition = intent.getIntExtra("position_item", 0);
            String Artist_multi = intent.getStringExtra("artist");
            Episode_Details_output episode_details_output = new Episode_Details_output();
            Song_name = episode_details_output.getName();

            player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
            multipartAdaptor = new PlayerDataMultipartAdaptor(context, filelist2, AudioPlayerActivity.this, adaptorPosition, Artist_multi);
            playerRecycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            playerRecycle.setAdapter(multipartAdaptor);
        }

    };

    private BroadcastReceiver SongStatusReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent

            Log.v("pratikl","in song status br");
            song_status = (intent.getStringExtra("songStatus")).trim();
            if (mediaPlayer.isPlaying()) {
//                albumArt_player.setImageResource(R.drawable.ic_pause_black_24dp);
                player_play_ic.setImageResource(R.drawable.player_player_pause_ic);
//                minicontroller.setVisibility(View.VISIBLE);
            } else {
//                albumArt_player.setImageResource(R.drawable.play_icon);
                player_play_ic.setImageResource(R.drawable.player_play_ic);
//                minicontroller.setVisibility(View.GONE);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (song_status.equals("close")) {
//                        minicontroller.setVisibility(View.GONE);
                    }
                    if (song_status.equals("next")) {
                        Log.v("SUBHA", "NEXT STATUS");
//                        next();
                    }
                    if (song_status.contains("@@@@@")) {
                        final String data[] = song_status.split("@@@@@");


                        total_duration.setText(timeC(Long.parseLong(data[1])));
                        curent_duration.setText(timeC(Long.parseLong(data[0])));
                        seekBar.setMax(Integer.parseInt(data[1]));
                        seekBar.setProgress(Integer.parseInt(data[0]));
                       /* seekbar_botomSht.setMax(Integer.parseInt(data[1]));
                        seekbar_botomSht.setProgress(Integer.parseInt(data[0]));*/
                        musicProgress.setMax(Integer.parseInt(data[1]));
                        musicProgress.setProgress(Integer.parseInt(data[0]));

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                duration_layout.setVisibility(View.VISIBLE);
                                StartTimer();
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                mediaPlayer.seekTo(seekBar.getProgress());
                            }
                        });
                    }
                }

            });
        }
    };

    @Override
    public void onGetValidateUserPreExecuteStarted() {

    }

    @Override
    public void onGetValidateUserPostExecuteCompleted(ValidateUserOutput validateUserOutput, int status, String message) {

        Log.v("pratikl","song status=="+status);
        pDialog = new ProgressBarHandler(this);
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
        if (status == 429) {

            Player_State(1);
        } else {
        }
    }


    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom();
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }

    public void StartTimer() {
        if (isRunning) {
            durationTimer.cancel();
            isRunning = false;
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                duration_layout.setVisibility(View.VISIBLE);

            }
        });

//        Toast.makeText(getApplicationContext(), "timer start", Toast.LENGTH_SHORT).show();
        durationTimer = new Timer();
        durationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                durationTimer.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        duration_layout.setVisibility(View.GONE);
                    }
                });
                isRunning = false;

            }
        }, 3000, 3000);
        isRunning = true;
    }




    public void PlaySongs(ContentDetailsOutput itemsList, boolean itemClicked) {
        if (itemClicked) {
            ValidateUserInput validateUserInput = new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userid);
            Log.v("niharId", "" + itemsList.getMuviUniqId());
            validateUserInput.setMuviUniqueId(itemsList.getMuviUniqId());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            if (Util.checkNetwork(AudioPlayerActivity.this) == true) {
              /*  GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, this);
                getValidateUserAsynTask.execute();*/
            } else {
                Util.showToast(AudioPlayerActivity.this, Util.getTextofLanguage(AudioPlayerActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }
            contentDetailsOutput = itemsList;
            album_name = itemsList.getName();
            song_url = itemsList.getMovieUrl();
            song_imageUrl = itemsList.getPoster();
            song_name = itemsList.getName();
            Player_State(1);
        } else {

        }
        Log.v("nihar_nihar", "" + itemsList.getName());

    }

    public void PlayMultiSongs(Episode_Details_output itemsList, boolean itemClicked,int currentAdapterPosition) {
        if (itemClicked) {
            ValidateUserInput validateUserInput = new ValidateUserInput();
            validateUserInput.setAuthToken(Util.authTokenStr);
            validateUserInput.setUserId(userid);
            validateUserInput.setMuviUniqueId(itemsList.getMuvi_uniq_id());
            validateUserInput.setSeasonId("0");
            validateUserInput.setEpisodeStreamUniqueId("0");
            if (Util.checkNetwork(AudioPlayerActivity.this) == true) {
                GetValidateUserAsynTask getValidateUserAsynTask = new GetValidateUserAsynTask(validateUserInput, this, this);
                getValidateUserAsynTask.execute();
            } else {
                Util.showToast(AudioPlayerActivity.this, Util.getTextofLanguage(AudioPlayerActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION));

            }
            this.currentAdapterPosition=currentAdapterPosition;
            Intent position = new Intent("SONG_POSITION");
            position.putExtra("songStatusNumber",currentAdapterPosition);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(position);

            album_name = itemsList.getName();
            song_url = itemsList.getVideo_url();
            song_imageUrl = itemsList.getPoster_url();
            song_name = itemsList.getEpisode_title();
            Player_State(1);
        } else {

        }


    }
    public static String timeC(long dur) {
        Log.v("Nihar", " duration=" + dur);
        String strDate = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(dur) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dur)),
                TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur)));
        Log.v("Nihar", "audio duration=" + strDate);
        return strDate;
    }
    public void Player_State(int funId) {
        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName", album_name);

        Log.v("pratikl","in player state  "+album_name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(playerData);

        Intent j = new Intent(AudioPlayerActivity.this, MusicService.class);
        j.putExtra("ALBUM", song_url);
//        j.putExtra("PERMALINK", desired_string);
        j.putExtra("ALBUM_ART", song_imageUrl);
        j.putExtra("ALBUM_NAME", name);
        j.putExtra("ALBUM_SONG_NAME", song_name);
        j.putExtra("STATE", funId);
        j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        Log.v("niharMulti", "" + "Called" + song_imageUrl);
        startService(j);


    }


    public  void next(){

        if (adaptorPosition < multipartAdaptor.getMultipartArray().size() ) {
            if (adaptorPosition == multipartAdaptor.getMultipartArray().size() - 1) {
                adaptorPosition = -1;
            }
            final Episode_Details_output multi_song_list_adaptor = multipartAdaptor.getMultipartArray().get(adaptorPosition + 1);
            PlayMultiSongs(multi_song_list_adaptor, true,currentAdapterPosition);
            adaptorPosition = adaptorPosition + 1;
            try {
                PlayerDataMultipartAdaptor.changePlayedSong(true,adaptorPosition,AudioPlayerActivity.this);
            }catch (Exception e){

            }

        }
        else {
            adaptorPosition = 0;
            final Episode_Details_output multi_song_list_adaptor = multipartAdaptor.getMultipartArray().get(0);
            PlayMultiSongs(multi_song_list_adaptor, true,currentAdapterPosition);
            try {
                PlayerDataMultipartAdaptor.changePlayedSong(true,adaptorPosition,AudioPlayerActivity.this);
            }catch (Exception e){

            }

        }
    }

    public  void previous(){
        if (adaptorPosition > 0) {
            final Episode_Details_output multi_song_list_adaptor = multipartAdaptor.getMultipartArray().get(adaptorPosition - 1);
            PlayMultiSongs(multi_song_list_adaptor, true,currentAdapterPosition);
            adaptorPosition = adaptorPosition - 1;
            try {
                PlayerDataMultipartAdaptor.changePlayedSong(true,adaptorPosition,AudioPlayerActivity.this);
            }catch (Exception e){

            }


        } else {
            final Episode_Details_output multi_song_list_adaptor = multipartAdaptor.getMultipartArray().get(adaptorPosition);
            PlayMultiSongs(multi_song_list_adaptor, true,currentAdapterPosition);
            try {
                PlayerDataMultipartAdaptor.changePlayedSong(true,adaptorPosition,AudioPlayerActivity.this);
            }catch (Exception e){

            }

        }
    }

}


