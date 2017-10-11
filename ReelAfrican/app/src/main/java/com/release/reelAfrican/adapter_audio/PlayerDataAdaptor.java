package com.release.reelAfrican.adapter_audio;

import android.content.Context;
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
import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.AudioPlayerActivity;
import com.release.reelAfrican.actvity_audio.ListFragment;

import com.release.reelAfrican.actvity_audio.MultiPartFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Muvi on 6/23/2017.
 */

public class PlayerDataAdaptor extends RecyclerView.Adapter<PlayerDataAdaptor.ItemHolder> {

    private ContentDetailsOutput itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;
    AudioPlayerActivity audioPlayerActivity ;



    public PlayerDataAdaptor(Context context, ArrayList<ContentDetailsOutput> list, AudioPlayerActivity audioPlayerActivity) {
        this.list = list;
        this.mContext = context;
        this.audioPlayerActivity= audioPlayerActivity;

        Log.v("pratik","in player data adapter contructor");
    }



    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);

        Log.v("pratik","in player data adapter item hldr");

        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,  int i) {
        itemsList =  list.get(i);
//        holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
  //      holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));

        holder.list_songName.setText(itemsList.getName());
        holder.list_artistName.setText(itemsList.getArtist());

        holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "click"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
               Log.v("nihar_nihar",""+itemsList.getName());
//                    mainActivity.details(singleItem,itemClicked);
                audioPlayerActivity.PlaySongs(itemsList,itemClicked);




                Log.v("pratik","in player data adapter onbindview holder");
            }
        });


//        if (currentWord.hasImage()) {
        String link = itemsList.getPoster();
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
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    ///swipe left and right


//    public void removeItem(int position) {
//        itemsList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, itemsList.size());
//    }




    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName;
        private TextView list_artistName;
        private ImageView list_albumart,list_option_menu;
        private RelativeLayout list_view;
        private RelativeLayout listsong_layout;

        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.list_view);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
//            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
   //         list_songName.setTypeface(list_songName_tf);


            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
//            Typeface list_artistName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
  //          list_artistName.setTypeface(list_artistName_tf);

            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);

            Log.v("pratik","in player data adapter item holder");
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
