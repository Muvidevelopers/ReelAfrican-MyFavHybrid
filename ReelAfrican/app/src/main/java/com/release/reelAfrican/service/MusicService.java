package com.release.reelAfrican.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.Blank;
import com.release.reelAfrican.utils.Constants;
import com.release.reelAfrican.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static com.release.reelAfrican.utils.Util.mediaPlayer;
import static com.release.reelAfrican.utils.Util.pause_controller;
import static com.release.reelAfrican.utils.Util.updateProgressTimer;


public class MusicService extends Service {
    private boolean useFrontSpeaker;
    private AudioTrack audioTrackPlayer = null;
    private Timer progressTimer = null;
    String notify_image,desire_string;
    int adaptorPosition;
    String SongName, AlbumName, url,Artist;
    RemoteViews bigViews;
    Notification status;
    boolean mediaChanged = true;
    Bitmap Notification_albumArt;

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(SERVICE_ACTION, new IntentFilter("SERVICE_ACTION_NEXT"));

    }

    private BroadcastReceiver SERVICE_ACTION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Intent Sintent = new Intent("SONG_STATUS");
                Sintent.putExtra("songStatus", "play");
                showNotification(1);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Sintent);
            } else {
                mediaPlayer.pause();
                Intent Pintent = new Intent("SONG_STATUS");
                Pintent.putExtra("songStatus", "pause");
                showNotification(0);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Pintent);
            }

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(SERVICE_ACTION);
        mediaPlayer.pause();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.v("Nihar2", "enter");
            showNotification(1);
            if (updateProgressTimer != null) {
                try {
                    StopTimer();
//                    Toast.makeText(getApplicationContext(), "Timer Stop", Toast.LENGTH_SHORT).show();

                } catch (Exception g) {
//                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
                }
            }
            url = intent.getStringExtra("ALBUM");
            SongName = intent.getStringExtra("ALBUM_SONG_NAME");
            Artist = intent.getStringExtra("Artist");
            desire_string = intent.getStringExtra("PERMALINK");
            AlbumName = intent.getStringExtra("ALBUM_NAME");
            notify_image = intent.getStringExtra("ALBUM_ART");
            int func = intent.getIntExtra("STATE", 0);

            Intent playerData = new Intent("PLAYER_DETAILS");
            playerData.putExtra("SongName", SongName);
            playerData.putExtra("PERMALINK", desire_string);
            playerData.putExtra("songImageUrl", notify_image);
            playerData.putExtra("Artist", Artist);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(playerData);

            switch (func) {
                case 1:
                    try {

                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(useFrontSpeaker ? AudioManager.STREAM_VOICE_CALL : AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(url);
                            Log.v("Niharecer", "Exception at=" +url);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                            @Override
                            public void onPrepared(MediaPlayer player) {
                                mediaPlayer.start();
                                mediaChanged=true;
                                Log.v("Nihar2", "Song Played");
                                showNotification(1);
                            }

                        });

                        Util.Duration = mediaPlayer.getDuration();



                        StartTimer();

                    } catch (Exception e) {
                  }

                    Intent Str_intent = new Intent("SONG_STATUS");
                    Str_intent.putExtra("songStatus", "play");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Str_intent);

                    break;
            }
        }
        else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(getApplicationContext(), "next", Toast.LENGTH_SHORT).show();

            Intent previntent = new Intent("SONG_STATUS_PREVIOUS");
            previntent.putExtra("songstaus_previous", "previous");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(previntent);

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            if (!mediaPlayer.isPlaying()) {
                showNotification(1);
//            bigViews.setImageViewBitmap(R.id.status_bar_play, Constants.getDefaultAlbumArt(this, R.drawable.icon_pause_white));
                mediaPlayer.start();
                Intent Sintent = new Intent("SONG_STATUS");
                Sintent.putExtra("songStatus", "play");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Sintent);
                pause_controller="show";

            } else {
                showNotification(0);
//            bigViews.setImageViewBitmap(R.id.status_bar_play, Constants.getDefaultAlbumArt(this, R.drawable.icon_play_white));
                mediaPlayer.pause();
                Intent Pintent = new Intent("SONG_STATUS");
                Pintent.putExtra("songStatus", "pause");
                pause_controller="show";
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Pintent);


            }
        }else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Intent nextintent = new Intent("SONG_STATUS_NEXT");
            nextintent.putExtra("songstaus_next", "next");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(nextintent);

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            pause_controller="";

//            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            Intent Pintent = new Intent("CLOSE_NOTI");
            Pintent.putExtra("closeNotification", "close");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Pintent);
            stopForeground(true);
            stopSelf();
        }



    }
    catch (Exception e){
    }
        return START_STICKY;
    }

    public void StartTimer() {

//        Toast.makeText(getApplicationContext(), "timer start", Toast.LENGTH_SHORT).show();
        updateProgressTimer = new Timer();
        updateProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.getCurrentPosition() > 0 && mediaPlayer.getCurrentPosition()>=mediaPlayer.getDuration() &&  mediaChanged){

                        mediaChanged = false;
                        Log.v("Nihar_oncomplition","CALLED"+mediaPlayer.getDuration());
                        Intent intentnext = new Intent("SONG_STATUS");
                        intentnext.putExtra("songStatus", "next");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentnext);

                }
                Util.GetCurrentPosition = mediaPlayer.getCurrentPosition();
                Intent Str_intent = new Intent("SONG_STATUS");
                Str_intent.putExtra("songStatus", mediaPlayer.getCurrentPosition() + "@@@@@" + mediaPlayer.getDuration());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(Str_intent);

            }
        }, 1000, 1000);
    }

    public void StopTimer() {
        updateProgressTimer.cancel();
    }

    public void showNotification(int val)
    {
        bigViews = new RemoteViews(getPackageName(),R.layout.status_bar_expanded);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this,R.drawable.logo));
//        bigViews.setImageViewBitmap(R.id.status_bar_album_art,getBitmapFromURL(notify_image));
        Constants.getDefaultAlbumArt(this, R.drawable.logo);
        Intent notificationIntent;


            notificationIntent = new Intent(this, Blank.class);


        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
       bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
      bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        bigViews.setImageViewBitmap(R.id.status_bar_album_art,getBitmapFromURL(notify_image));


        if (val==1){
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                bm = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.player_player_pause_ic, options);
            } catch (Error ee) {
            } catch (Exception e) {
            }
            bigViews.setImageViewBitmap(R.id.status_bar_play,bm);
//            bigViews.setImageViewBitmap(R.id.status_bar_play,Constants.getDefaultAlbumArt(this,R.drawable.player_player_pause_ic));
        }if (val==0){
//        bigViews.setImageViewBitmap(R.id.status_bar_play,Constants.getDefaultAlbumArt(this,R.drawable.player_play_ic));
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.player_play_ic, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        bigViews.setImageViewBitmap(R.id.status_bar_play,bm);

    }
//         bigViews.setImageViewBitmap(R.id.status_bar_play,Constants.getDefaultAlbumArt(this,));

        bigViews.setTextViewText(R.id.song_name,SongName);
        bigViews.setTextViewText(R.id.album_name, AlbumName);

        bigViews.setTextColor(R.id.song_name, ContextCompat.getColor(this,android.R.color.white));
        bigViews.setTextColor(R.id.album_name,ContextCompat.getColor(this,android.R.color.white));
        bigViews.setInt(R.id.notificationbg,"setBackgroundResource", android.R.color.black);

        status = new Notification.Builder(this).build();

        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.logo;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }
    public Bitmap getBitmapFromURL(String strURL) {
       /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        try {
            if (strURL == null){
                strURL = "https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png";
            }
            URL url = new URL(strURL);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
          Bitmap myBitmap = null;
            try {
                connection.connect();
                InputStream input = connection.getInputStream();
                 myBitmap = BitmapFactory.decodeStream(input);
            }catch (Exception e){
//                Toast.makeText(this, "network not available", Toast.LENGTH_SHORT).show();
            }


            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public class bitmapAsync extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
