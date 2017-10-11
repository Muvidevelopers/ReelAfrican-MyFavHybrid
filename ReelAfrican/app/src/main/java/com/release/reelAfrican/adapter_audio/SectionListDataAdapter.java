package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.MultiPartFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Muvi on 6/5/2017.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.ItemHolder> {

    private ArrayList<FeatureContentOutputModel> itemsList;
    private Context mContext;
    Fragment fragment = null;
    FeatureContentOutputModel singleItem;



    public SectionListDataAdapter(Context context, ArrayList<FeatureContentOutputModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int i) {
         singleItem = itemsList.get(i);

        holder.position = i;

        Log.v("Nihar",singleItem.getPermalink());

        holder.title_tv.setText(singleItem.getName());


//        if (currentWord.hasImage()) {
        String link=singleItem.getPoster_url();
        Picasso.with(mContext)
                .load(link)
                .into(holder.itemImage);


//        holder.itemImage.setImageResource();
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView title_tv;
        protected  int position;

        private ImageView itemImage;
//        private RelativeLayout mSlider;

        public ItemHolder(final View view) {
            super(view);
            this.title_tv = (TextView) view.findViewById(R.id.title_tv);
            Typeface title_tv_tf = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.light_fonts));
            title_tv.setTypeface(title_tv_tf);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
//            mSlider = (RelativeLayout) view.findViewById(R.id.mSlider);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent =  new Intent(mContext,MultiPartActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    mContext.startActivity(intent);

                    String moviePermalink = itemsList.get(position).getPermalink();
                    String content_types_id = itemsList.get(position).getContent_types_id();

                    Log.v("Nihar","hhhf"+moviePermalink);
                    fragment = new MultiPartFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString( "PERMALINK" ,moviePermalink);
                    arguments.putString( "CONTENT_TYPE" ,content_types_id);
                    fragment.setArguments(arguments);
                    if (fragment != null){
                        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.home_content, fragment,"MultiPartFragment");
                        fragmentTransaction.addToBackStack("MultiPartFragment");
                        fragmentTransaction.commit();

                        // set the toolbar title
//                        getSupportActionBar().setTitle(title);
                    }
                }
            });
        }
    }
}
