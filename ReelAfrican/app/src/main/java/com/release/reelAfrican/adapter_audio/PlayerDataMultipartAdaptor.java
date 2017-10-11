package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.Episode_Details_output;
import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.AudioPlayerActivity;
import com.release.reelAfrican.actvity_audio.ListFragment;
import com.release.reelAfrican.actvity_audio.MultiPartFragment;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Muvi on 6/23/2017.
 */

public class PlayerDataMultipartAdaptor extends RecyclerView.Adapter<PlayerDataMultipartAdaptor.ItemHolder> {

    private Episode_Details_output itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;
    ArrayList<Episode_Details_output> episode_details_output;
    AudioPlayerActivity audioPlayerActivity;
    static int adaptorPosition = -1;
    static int prevPosition =  -1;
    int postn ;
    int position;
    static ItemHolder myholder;
    String Artist;



    public PlayerDataMultipartAdaptor(Context context, ArrayList<Episode_Details_output> episode_details_output, AudioPlayerActivity audioPlayerActivity, int adaptorPosition, String artist) {
        this.episode_details_output = episode_details_output;
        this.mContext = context;
        this.audioPlayerActivity = audioPlayerActivity;
        this.adaptorPosition= adaptorPosition;
        this.Artist=artist;
        Log.v("nihar_Adaptorp0",""+adaptorPosition);
        Log.v("pratil","multipart adapt cons");
    }

    public ArrayList<Episode_Details_output> getMultipartArray(){
        return this.episode_details_output;
    }



    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);

        Log.v("pratil","multipart adapt item hldr");
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,  final int i) {
        boolean addtomultiHolderArray = true;

        holder.list_songName.setTag(i);
        for(int j = 0; j<  Util.multiholder.size(); j++){
            if (Integer.parseInt( Util.multiholder.get(j).list_songName.getTag().toString())==i)
            {
                addtomultiHolderArray = false;
            }
        }

        if(addtomultiHolderArray){
            Util.multiholder.add(holder);
            addtomultiHolderArray=true;
        }

        Log.v("nihar_clicked","====***===="+ Util.multiholder.size());

        postn = i;
        final Episode_Details_output multi_song_list_adaptor =  episode_details_output.get(i);
        myholder=holder;
        itemsList=multi_song_list_adaptor ;




        if(adaptorPosition!=-1 && adaptorPosition==i)
        {
            Log.v("nihar_adaptorPosition","adaptorPosition  :"+adaptorPosition);
//            holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
  //          holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));
         /*   Drawable drawable = getDrawableByState(mContext.getApplicationContext());
            holder.mImageView.setImageDrawable(drawable);*/

            adaptorPosition = -1;
        }
        else{
            if(prevPosition != -1 && i == prevPosition )
            {
                //change color like
//                holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
   //             holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));
          /*      Drawable drawable = getDrawableByState(mContext.getApplicationContext());
                holder.mImageView.setImageDrawable(drawable);*/

            }

            else
            {
                //revert back to regular color
                holder.list_songName.setTextColor(Color.WHITE);
                holder.list_artistName.setTextColor(Color.WHITE);
//                holder.mImageView.setImageDrawable(null);


            }
        }



        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayerActivity.PlayMultiSongs(multi_song_list_adaptor,itemClicked,i);
                prevPosition = i;
                Util.multiholder.clear();
                notifyItemRangeChanged(0, episode_details_output.size());



            }
        });
        holder.list_songName.setText(multi_song_list_adaptor.getEpisode_title());
        holder.list_artistName.setText(Artist);



        String link = multi_song_list_adaptor.getPoster_url();
//        Glide.with(mContext).load(link).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.list_albumart) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                holder.list_albumart.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);


//        holder.itemImage.setImageResource();
//        notifyDataSetChanged();
//        notifyItemRangeChanged(0, episode_details_output.size());

        Log.v("pratil","multipart adapt bind viewhldr");
    }

    @Override
    public int getItemCount() {
        return episode_details_output.size();
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public static void changePlayedSong(boolean click,int AdaptorPosition,Context context) {
        Log.v("nihar_clicked",""+click+"==="+AdaptorPosition);
        Log.v("nihar_clicked","========"+ Util.multiholder.size());
        for (int i = 0 ;i<Util.multiholder.size();i++){
            if (i==AdaptorPosition){
                Util.multiholder.get(i).list_songName.setTextColor(context.getResources().getColor(R.color.button_background));
                Util.multiholder.get(i).list_artistName.setTextColor(context.getResources().getColor(R.color.button_background));

                prevPosition = AdaptorPosition;

            }
            else {
                Util.multiholder.get(i).list_songName.setTextColor(Color.WHITE);
                Util.multiholder.get(i).list_artistName.setTextColor(Color.WHITE);
            }

        }

    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName;
        private TextView list_artistName,list_albumName;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout listsong_layout;
        public ImageView mImageView;


        public ItemHolder(View view) {
            super(view);
            Log.v("pratil","multipart adapt itemholder ");
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
//            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
  //          list_artistName.setTypeface(list_songName_tf);


            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);

            this.mImageView = (ImageView) view.findViewById(R.id.equaliser);
//            this.list_option_menu = (ImageView) view.findViewById(R.id.list_option_menu);

//            mSlider = (RelativeLayout) view.findViewById(R.id.mSlider);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    fragment2 = new ListFragment();
//                    if (fragment == null){
//                        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.home_content,fragment2).addToBackStack("MuliFragment");
//                        fragmentTransaction.commit();
//
//                        // set the toolbar title
////                        getSupportActionBar().setTitle(title);
//                    }
//
//                }
//            });


        }
    }
    public Drawable getDrawableByState(Context context) {
        AnimationDrawable animation = (AnimationDrawable)
                ContextCompat.getDrawable(context, R.drawable.ic_equalizer_white_36dp);
        DrawableCompat.setTintList(animation, ColorStateList.valueOf(context.getResources().getColor(R.color.button_background)));
        animation.start();
        return animation;
    }
}