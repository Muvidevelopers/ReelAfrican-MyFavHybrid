package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
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
import com.release.reelAfrican.actvity_audio.ListFragment;
import com.release.reelAfrican.actvity_audio.MultiPartFragment;
import com.release.reelAfrican.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Muvi on 6/23/2017.
 */

public class MultiDataAdaptor extends RecyclerView.Adapter<MultiDataAdaptor.ItemHolder> {

    private Episode_Details_output itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;
    ArrayList<Episode_Details_output> episode_details_output;
    int adaptorPosition = -1;
    int prevPosition =  -1;
    int position;
    ItemHolder myholder;
    String artist_multi_data;



    public MultiDataAdaptor(Context context, ArrayList<Episode_Details_output> episode_details_output, MultiPartFragment multiPartFragment, String artist_multi) {
        this.episode_details_output = episode_details_output;
        this.mContext = context;
        this.multiPartFragment = multiPartFragment;
        this.artist_multi_data=artist_multi;
    }




    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,   final int i) {
        final Episode_Details_output multi_song_list_adaptor =  episode_details_output.get(i);

       /* SQLiteDatabase DB = mContext.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);

        String DEL_QRY = "DELETE FROM " + Util.ADAPTOR_TABLE_NAME +"";
        DB.execSQL(DEL_QRY);

        String INS_QRY = "INSERT INTO " + Util.ADAPTOR_TABLE_NAME + "(ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME)" +
                " VALUES ( '" + multi_song_list_adaptor.getVideo_url() + "','" + multi_song_list_adaptor.getPermalink() + "','" + multi_song_list_adaptor.getPoster_url() + "'," +
                "'" + "" + "','" + multi_song_list_adaptor.getName() + "')";
        DB.execSQL(INS_QRY);*/
       // itemsList = episode_details_output.get(i);

        holder.list_songName.setText(multi_song_list_adaptor.getEpisode_title());
        holder.artist_multi.setText(artist_multi_data);
        Log.v("Nihar_artist","adaptor"+artist_multi_data);
        if(adaptorPosition!=-1 )
        {
            holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.white));
            adaptorPosition = -1;
        }
        else{
            if(prevPosition != -1 && i == prevPosition )
            {
                //change color like
                holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.artist_multi.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            else
            {
                //revert back to regular color
                holder.list_songName.setTextColor(Color.WHITE);
                holder.artist_multi.setTextColor(Color.WHITE);
            }
        }

        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.SongName = multi_song_list_adaptor.getEpisode_title();
                Util.ArtistName = artist_multi_data;

                multiPartFragment.PlaySongsmulti(multi_song_list_adaptor,itemClicked,holder.getAdapterPosition());



                prevPosition = i;
                notifyItemRangeChanged(0, episode_details_output.size());

            }
        });


//        if (currentWord.hasImage()) {
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


        if (i == episode_details_output.size()-1){
         holder.listDummy.setVisibility(View.VISIBLE);
           // holder.dividerView.setVisibility(View.VISIBLE);

        }else{
            holder.listDummy.setVisibility(View.GONE);
          //  holder.dividerView.setVisibility(View.VISIBLE);

        }

//        holder.itemImage.setImageResource();
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




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName,listDummy;
        private TextView artist_multi;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout listsong_layout;
        private View dividerView;


        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.listDummy = (TextView) view.findViewById(R.id.listDummy);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);
          this.artist_multi = (TextView) view.findViewById(R.id.list_artistName);
            Typeface artist_multi_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            artist_multi.setTypeface(artist_multi_tf);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            this.dividerView = (View) view.findViewById(R.id.divider);

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
}
