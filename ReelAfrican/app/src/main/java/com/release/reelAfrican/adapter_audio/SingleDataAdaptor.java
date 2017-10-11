package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.home.apisdk.apiModel.ContentDetailsOutput;
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

public class SingleDataAdaptor extends RecyclerView.Adapter<SingleDataAdaptor.ItemHolder> {

    private ContentDetailsOutput itemsList;
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    private ListFragment listFragment;
    private MultiPartFragment multiPartFragment;
    boolean itemClicked = true;
    ArrayList<ContentDetailsOutput> list;




    public SingleDataAdaptor(Context context, ContentDetailsOutput itemsList, MultiPartFragment multiPartFragment) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.multiPartFragment = multiPartFragment;
        Log.v("pratikl","in constructor");
    }





    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);

        Log.v("pratikl","in constructor");
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder,  int i) {
        try {
           /* SQLiteDatabase DB = mContext.openOrCreateDatabase(Util.DATABASE_NAME, MODE_PRIVATE, null);

            String DEL_QRY = "DELETE FROM " + Util.ADAPTOR_TABLE_NAME +"";
            DB.execSQL(DEL_QRY);

            String INS_QRY = "INSERT INTO " + Util.ADAPTOR_TABLE_NAME + "(ALBUM_URL,PERMALINK,ALBUM_ART,ALBUM_NAME,ALBUM_SONG_NAME)" +
                    " VALUES ( '" + itemsList.getMovieUrl() + "','" + itemsList.getPermalink() + "','" + itemsList.getPoster() + "'," +
                    "'" + "" + "','" + itemsList.getName() + "')";
            DB.execSQL(INS_QRY);*/


            holder.list_songName.setText(itemsList.getName());
            holder.list_artistName.setText(itemsList.getArtist());
            holder.listsong_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Util.SongName = itemsList.getName();
                    Util.ArtistName = itemsList.getArtist();

                /*    holder.list_songName.setTextColor(mContext.getResources().getColor(R.color.button_background));
                    holder.list_artistName.setTextColor(mContext.getResources().getColor(R.color.button_background));*/
                    multiPartFragment.PlaySongs(itemsList,itemClicked);
                }
            });
            String link = itemsList.getPoster();
            Log.v("pratikl","link=="+link);

            Picasso.with(mContext)
                    .load(link)
                    .into(holder.list_albumart);
        }catch (Exception e ){
            Log.v("nihar_exception",""+e.toString());

        }

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
        private RelativeLayout listsong_layout;


        public ItemHolder(View view) {
            super(view);
            this.listsong_layout = (RelativeLayout) view.findViewById(R.id.listsong_layout);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            Typeface list_songName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.regular_fonts));
            list_songName.setTypeface(list_songName_tf);

            this.list_artistName = (TextView) view.findViewById(R.id.list_artistName);
            Typeface list_artistName_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.light_fonts));
            list_artistName.setTypeface(list_artistName_tf);

            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
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
