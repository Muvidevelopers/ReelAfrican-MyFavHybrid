package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.release.reelAfrican.R;
import com.release.reelAfrican.actvity_audio.SubscriptionActivity;
import com.release.reelAfrican.model_audio.PlanModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/6/2016.
 */
public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {
    public   int layoutPosition = -1;
    boolean value=false;
    Context context;
    private ArrayList<PlanModel> moviesList;
    Boolean isClicked=false;
    SubscriptionActivity subscriptionActivity;

    public PlanAdapter(Context context, ArrayList<PlanModel> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
        this.subscriptionActivity= (SubscriptionActivity) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_row_audio, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  final int position) {
         final PlanModel movie = moviesList.get(position);

        if(isClicked && layoutPosition == position){
            holder.select_planMark_relative.setVisibility(View.VISIBLE);
            isClicked=false;
            layoutPosition = -1;
        }
        else{
            holder.select_planMark_relative.setVisibility(View.GONE);
        }

        holder.planName.setText(movie.getPlanNameStr());


        holder.relativeplannamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscriptionActivity.getPlanDetails(movie,position);
                isClicked=true;
                notifyItemRangeChanged(0,moviesList.size());
                layoutPosition=position;
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView planName, purchaseValue, freeTrial,subcriptionmonth,planPurchaseCurrenyTextView;
        RelativeLayout relativeplannamelayout,select_planMark_relative;



        public MyViewHolder(View view) {
            super(view);
            planName = (TextView) view.findViewById(R.id.planNameTextView);
            relativeplannamelayout = (RelativeLayout) view.findViewById(R.id.relativeplannamelayout);
            select_planMark_relative = (RelativeLayout) view.findViewById(R.id.select_planMark_relative);
        }
    }

}
