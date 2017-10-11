package com.release.reelAfrican.physical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.release.reelAfrican.R;


/**
 * Created by Muvi on 10/12/2016.
 */
public class CardSpinnerAdapter extends BaseAdapter {
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private CardModel[] values;
    LayoutInflater inflter;
    public CardSpinnerAdapter(Context context,
                              CardModel[] values) {
        this.context = context;
        this.values = values;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount(){
        return values.length;
    }

    public CardModel getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_new, null);
        TextView names = (TextView) view.findViewById(R.id.cardTextView);
        names.setText(values[i].getCardNumber());
        return view;
    }


}

