package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.apisdk.apiModel.ContentListOutput;
import com.release.reelAfrican.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by muvi on 27/6/17.
 */

public class ViewMoreAdapter extends RecyclerView.Adapter<ViewMoreAdapter.MyHolder> {
    ArrayList<ContentListOutput> contentListOutputArray;
    ContentListOutput contentListOutput;
    Context mContext;


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView title_tv;

        public MyHolder(View itemView) {

            super(itemView);
            this.title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            Typeface title_tv_tf = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.light_fonts));
            title_tv.setTypeface(title_tv_tf);
            this.itemImage=(ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public ViewMoreAdapter(Context context, ArrayList<ContentListOutput> contentListOutputArray){
        this.contentListOutputArray=contentListOutputArray;
        this.mContext=context;

        Log.v("pratik","adapter callewd1");
        Log.v("pratik","adapter callewd2=="+contentListOutputArray.size());
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("pratik","adapter callewd3");
        View v= LayoutInflater.from(mContext).inflate(R.layout.list_single_card, null);
        MyHolder myHolder=new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int i) {
        contentListOutput = contentListOutputArray.get(i);
        String posterUrl =  contentListOutput.getPosterUrl();
        String videoUrl=contentListOutput.getPermalink();
        String name=contentListOutput.getName();



        //Toast.makeText(mContext, "No content found", Toast.LENGTH_SHORT).show();

        holder.title_tv.setText(name);

        Log.v("pratik","vurl==="+videoUrl +"poster url=="+posterUrl);
        Picasso.with(mContext)
                .load(posterUrl)
                .into(holder.itemImage);
    }



    @Override
    public int getItemCount() {
        Log.v("pratik","called 4==="+(null != contentListOutputArray ? contentListOutputArray.size() : 0));
        return (null != contentListOutputArray ? contentListOutputArray.size() : 0);
    }
}
