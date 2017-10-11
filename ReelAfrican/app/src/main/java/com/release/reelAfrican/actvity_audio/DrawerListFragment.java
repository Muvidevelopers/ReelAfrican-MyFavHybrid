package com.release.reelAfrican.actvity_audio;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.apisdk.apiController.GetContentListAsynTask;
import com.home.apisdk.apiModel.ContentListInput;
import com.home.apisdk.apiModel.ContentListOutput;
import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter_audio.ViewMoreAdapter;
import com.release.reelAfrican.utils.ProgressBarHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerListFragment extends Fragment implements GetContentListAsynTask.GetContentList{

    TextView textView;
    RecyclerView recyclerView;
    ArrayList<ContentListInput> contentListInputs=new ArrayList<>();
    ViewMoreAdapter viewMoreAdapter;
    RelativeLayout relativelayout;
    Context context;
    ProgressBarHandler pDialog;

    //  private ArrayList<FeatureContentOutputModel> featureContentOutputModelArrayList;



    public DrawerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  =  inflater.inflate(R.layout.fragment_drawer_list, container, false);

        //   textView=(TextView) v.findViewById(R.id.textView1);
        relativelayout=(RelativeLayout)v.findViewById(R.id.relativelayout);
        recyclerView=(RecyclerView)v.findViewById(R.id.recycler_view_list1);
        pDialog = new ProgressBarHandler(getActivity());

        Bundle argument=getArguments();
        String perma=argument.getString("PERMALINK");
        String displayname=argument.getString("displayname");

      /*  String sectionid=argument.getString("sectionid");
        Log.v("pratik","section id=="+sectionid);*/

        ContentListInput contentListInput=new ContentListInput();
        contentListInput.setAuthToken("d95a752be6de25c4949be14258656097");
        contentListInput.setPermalink(perma);
        GetContentListAsynTask getContentListAsynTask=new GetContentListAsynTask(contentListInput,this,getActivity());
        getContentListAsynTask.execute();

        //getFeatureContent
        /*FeatureContentInputModel featureContentInputModel = new FeatureContentInputModel();
        featureContentInputModel.setAuthToken(Util.authTokenStr);
        featureContentInputModel.setSection_id(sectionid);
        GetFeatureContentAsynTask getFeatureContentAsynTask = new GetFeatureContentAsynTask(featureContentInputModel,this,getActivity());
        getFeatureContentAsynTask.execute();*/

        //  textView.setText(displayname);
        Log.v("pratik","show name=="+displayname);
        return v  ;
    }

    @Override
    public void onGetContentListPreExecuteStarted() {

        if (!pDialog.isShowing()){
            pDialog.show();
        }

    }

    @Override
    public void onGetContentListPostExecuteCompleted(ArrayList<ContentListOutput> contentListOutputArray, int status, int totalItems, String message) {


        if (pDialog.isShowing()){
            pDialog.hide();
        }
        Log.v("pratik","content op=="+contentListOutputArray);
        Log.v("pratik","status op=="+status);
        Log.v("pratik","message op=="+message);

        try{
            viewMoreAdapter=new ViewMoreAdapter(getActivity().getApplicationContext(), contentListOutputArray) ;
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(viewMoreAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));

        }catch (Exception e){
            Log.v("pratik11","Exception==="+e.toString());
        }


        Log.v("pratik","size =="+contentListOutputArray.size());
        if(contentListOutputArray.size()==0){
            relativelayout.setVisibility(View.VISIBLE);
            // Toast.makeText(context, "data not found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        menu.getItem(2).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }
}
