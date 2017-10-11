package com.release.reelAfrican.adapter_audio;

import android.content.Context;
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

import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.MultiPartFragment;
import  com.release.reelAfrican.model_audio.GridItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Muvi on 6/8/2017.
 */

public class ListDataAdaptor extends RecyclerView.Adapter<ListDataAdaptor.ItemHolder> {
    private Context mContext;
    Fragment fragment = null;
    Fragment fragment2 = null;
    ArrayList<GridItem> itemData = new ArrayList<>();
    int position;

    public ListDataAdaptor(Context context, ArrayList<GridItem> itemData) {
        this.itemData = itemData;
        this.mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listdata_item, null);
        ItemHolder mh = new ItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int i) {
        position=i;
        final GridItem item = itemData.get(i);
        holder.list_songName.setText(item.getTitle());
        String link = item.getImage();
        Picasso.with(mContext)
                .load(link)
                .into(holder.list_albumart);
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemData.size());
        Log.v("pratikf","Deleted..........");
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView list_songName;
        private ImageView list_albumart;

        public ItemHolder(View view) {
            super(view);
            this.list_songName = (TextView) view.findViewById(R.id.list_songName);
            this.list_albumart = (ImageView) view.findViewById(R.id.list_albumart);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment2 = new MultiPartFragment();
                    if (fragment == null) {

                        Bundle arguments = new Bundle();
                        arguments.putString( "PERMALINK" ,itemData.get(position).getPermalink());
                        Log.v("pratikf","getPermalink()==="+itemData.get(position).getPermalink());
                        Log.v("pratikf","position()==="+position);
                        arguments.putString( "CONTENT_TYPE" ,itemData.get(position).getVideoTypeId());
                        Log.v("pratikf","CONTENT_TYPE.........."+itemData.get(position).getVideoTypeId());
                        fragment2.setArguments(arguments);
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, fragment2);
                        fragmentTransaction.commit();

                    }

                }
            });
        }
    }
}
