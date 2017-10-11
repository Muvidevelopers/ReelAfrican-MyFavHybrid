package com.release.reelAfrican.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.release.reelAfrican.activity.Aod;
import com.release.reelAfrican.activity.Physy;
import com.release.reelAfrican.activity.Vod;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Vod tab1 = new Vod();
                return tab1;
            case 1:
                Aod tab2 = new Aod();
                return tab2;
            case 2:
                Physy tab3 = new Physy();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        //this is where you set the titles
        switch(position) {
            case 0:
                return "Vod";
            case 1:
                return "Audio";
            case 2:
                return "Physical";
        }
        return null;
    }
}