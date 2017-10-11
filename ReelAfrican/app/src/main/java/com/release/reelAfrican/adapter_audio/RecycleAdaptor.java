package com.release.reelAfrican.adapter_audio;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.GetFeatureContentAsynTask;
import com.home.apisdk.apiModel.FeatureContentInputModel;
import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.home.apisdk.apiModel.HomePageSectionModel;
import com.release.reelAfrican.R;
import com.release.reelAfrican.slider.MyCustomPagerAdapter;
import com.release.reelAfrican.slider.ZoomOutPageTransformer;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import java.util.ArrayList;

/**
 * Created by Muvi on 6/5/2017.
 */

public class RecycleAdaptor extends RecyclerView.Adapter<RecycleAdaptor.MyHolder> implements GetFeatureContentAsynTask.GetFeatureContent {
    private ArrayList<HomePageSectionModel> HomePageSectionModel;
    private Context mContext;
    Fragment fragment = null;
    ProgressBarHandler progressHandler;
    int myrequestCode;
    private ArrayList<MyHolder> myHolderArrayList = new ArrayList<MyHolder>();

    private ArrayList<FeatureContentOutputModel> featureContentOutputModelArrayList;

    public RecycleAdaptor(Context context, ArrayList<HomePageSectionModel> HomePageSectionModel) {
        this.HomePageSectionModel = HomePageSectionModel;
        this.mContext = context;
        myHolderArrayList.clear();

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        MyHolder mh = new MyHolder(v);
         progressHandler = new ProgressBarHandler(mContext);
        return mh;
    }

    @Override
    public void onBindViewHolder( final MyHolder itemRowHolder, final int  i) {
        if (i == 0){
            itemRowHolder.view_pager_vg.setVisibility(View.VISIBLE);
            Util.viewPager = itemRowHolder.viewPager;

        }     else{
            itemRowHolder.view_pager_vg.setVisibility(View.GONE);
        }
        Log.v("nihar_space",""+HomePageSectionModel.size());
        if (i==HomePageSectionModel.size()-1){

            itemRowHolder.space_view.setVisibility(View.VISIBLE);
        }


        final String sectionName = HomePageSectionModel.get(i).getTitle();
        HomePageSectionModel FeatureContentOutputModel = HomePageSectionModel.get(i);
        final String section_data_id = HomePageSectionModel.get(i).getSection_id();
        itemRowHolder.itemTitle.setText(sectionName);
        Log.v("pratikid","section id=="+section_data_id);
        myHolderArrayList.add(itemRowHolder);

        FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
        featureContentInputModel.setAuthToken(Util.authTokenStr);
//        featureContentInputModel.setAuthToken("00d7ae780e9aff77f85378dd470e9317");
        featureContentInputModel.setSection_id(section_data_id);
        featureContentInputModel.setRequest_code(i);
        if (Util.checkNetwork(mContext) == true) {
            GetFeatureContentAsynTask getFeatureContentAsynTask = new GetFeatureContentAsynTask(featureContentInputModel,this,mContext);
            getFeatureContentAsynTask.execute();
        }else{
            Util.showToast(mContext,Util.getTextofLanguage(mContext,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }




      /*  SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, featureContentOutputModelArrayList);
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);*/


        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                fragment = new com.release.reelAfrican.actvity_audio.ViewAllFragment();
                Bundle arguments = new Bundle();


                arguments.putString("sectionid",section_data_id);
                fragment.setArguments(arguments);
                if (fragment != null) {

                    FragmentManager fragmentManager =((AppCompatActivity)mContext).getSupportFragmentManager();;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment,"ViewAll");
                    fragmentTransaction.addToBackStack("ViewAll");
                    fragmentTransaction.commit();

                }

            }
        });


        itemRowHolder.iimg_recycle_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    myHolderArrayList.get(myrequestCode).recycler_view_list.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                itemRowHolder.linearLayoutManager.smoothScrollToPosition(itemRowHolder.recycler_view_list,null,itemRowHolder.linearLayoutManager.findLastVisibleItemPosition()+3);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != HomePageSectionModel ? HomePageSectionModel.size() : 0);
    }

    @Override
   public void onGetFeatureContentPreExecuteStarted() {
      /*  if (!progressHandler.isShowing()) {
            progressHandler.show();
        }*/

    }

    @Override
    public void onGetFeatureContentPostExecuteCompleted(ArrayList<FeatureContentOutputModel> featureContentOutputModelArray, int status, String message, int requestCode) {
        this.featureContentOutputModelArrayList=featureContentOutputModelArray;
        this.myrequestCode=requestCode;
        if (progressHandler.isShowing()) {
            progressHandler.hide();
        }
        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, featureContentOutputModelArrayList);
        myHolderArrayList.get(requestCode).recycler_view_list.setHasFixedSize(true);
        myHolderArrayList.get(requestCode).linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        myHolderArrayList.get(requestCode).recycler_view_list.setLayoutManager(myHolderArrayList.get(requestCode).linearLayoutManager);
        myHolderArrayList.get(requestCode).recycler_view_list.setAdapter(itemListDataAdapter);


    }


    public class MyHolder extends RecyclerView.ViewHolder{
        protected TextView itemTitle;
        public RecyclerView recycler_view_list;
        LinearLayoutManager linearLayoutManager;
        protected TextView btnMore,space_view;
        protected RelativeLayout view_pager_vg;
        protected ImageView iimg_recycle_img;
        MyCustomPagerAdapter myCustomPagerAdapter;
        ViewPager viewPager;

        int page;


        public MyHolder(View view) {
            super(view);
            this.view_pager_vg = (RelativeLayout) view.findViewById(R.id.view_pager_vg);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.space_view = (TextView) view.findViewById(R.id.space_view);
            this.iimg_recycle_img = (ImageView) view.findViewById(R.id.iimg_recycle_img);
            Typeface itemTitle_tf = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.regular_fonts));
            itemTitle.setTypeface(itemTitle_tf);

            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (TextView) view.findViewById(R.id.btnMore);
            Typeface btnMore_tf = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.regular_fonts));
            btnMore.setTypeface( btnMore_tf);

            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

//        viewPager.setPageTransformer(true, new DepthPageTransformer());
           String[] banner= new String[]{"https://sampledesign.muvi.com/mobileaudio/newimage.png"};
            myCustomPagerAdapter = new MyCustomPagerAdapter(mContext, banner);
            viewPager.setAdapter(myCustomPagerAdapter);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            //tabLayout.setupWithViewPager(viewPager, true);

            Log.v("BIBHU2","viewPager ====== called");

//            "https://t3.ftcdn.net/jpg/01/24/05/60/240_F_124056004_pbfPOjDj8UfU3nV4YofiWxiXPpvnLCM0.jpg","https://s-media-cache-ak0.pinimg.com/originals/99/d6/04/99d604c95210d831da61e049a1f7fc79.jpg"
         /*
            final Handler handler = new Handler();
          final  Runnable Update = new Runnable() {
                public void run() {
                    if (page == myCustomPagerAdapter.getCount()-1) {
                        page = 0;
                    }
                    viewPager.setCurrentItem(page++, true);
                }
            };

           Timer  timer = new Timer(); // This will create a new Thread
            timer .schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 1000, 6000);*/




        }
    }
}
