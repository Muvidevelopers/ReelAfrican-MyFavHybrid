package com.release.reelAfrican.actvity_audio;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.home.apisdk.apiController.HomePageAsynTask;
import com.home.apisdk.apiModel.FeatureContentOutputModel;
import com.home.apisdk.apiModel.HomePageInputModel;
import com.home.apisdk.apiModel.HomePageOutputModel;
import com.home.apisdk.apiModel.HomePageSectionModel;
import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter_audio.RecycleAdaptor;
import com.release.reelAfrican.model_audio.SectionDataModel;
import com.release.reelAfrican.model_audio.SingleItemModel;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.release.reelAfrican.utils.Util.mediaPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomePageAsynTask.HomePage {
    private ProgressBarHandler progressBarHandler = null;
    ViewPager viewPager;
    ArrayList<SectionDataModel> alldata;
    String[] banner;
    Timer timer;
    String responseStr;
     Runnable Update;
    RecycleAdaptor adapter;
    private Handler handler;
    private int delay = 2000; //milliseconds
    private int page = 0;
    Context context;
    RecyclerView my_recycler_view;
    LinearLayoutManager LLM;
    ProgressBarHandler pDialog;
    String Section_id;
    ArrayList<HomePageSectionModel> homePageSectionModel = new ArrayList<>();
    ArrayList<FeatureContentOutputModel> featureContentOutputModelArrayList;
    private Handler mHandler = new Handler();

    View bottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;

    int ShowViewPagerImageIndex = 0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Util.viewPager!=null) {

                            if(ShowViewPagerImageIndex <=2) {
                                Log.v("BIBHU2", "viewPager ====== called");
                                Util.viewPager.setCurrentItem(ShowViewPagerImageIndex, true);
                                ShowViewPagerImageIndex = ShowViewPagerImageIndex+1;
                            }
                            else
                            {
                                ShowViewPagerImageIndex = 0;
                                Util.viewPager.setCurrentItem(ShowViewPagerImageIndex, true);
                                ShowViewPagerImageIndex = ShowViewPagerImageIndex+1;
                            }
                        }
                    }
                });
            }
        }, 1000, 3000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.logo);
        pDialog = new ProgressBarHandler(getActivity());
         my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        LLM =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        adapter = new RecycleAdaptor(getActivity(), );
       my_recycler_view.setLayoutManager(LLM);

        HomePageInputModel homePageInputModel=new HomePageInputModel();
        homePageInputModel.setAuthToken(Util.authTokenStr);
        if (Util.checkNetwork(getActivity()) == true) {
            HomePageAsynTask homePageAsynTask = new HomePageAsynTask(homePageInputModel,this,getActivity());
            homePageAsynTask.execute();
        }else{
            Util.showToast(getActivity(),Util.getTextofLanguage(getActivity(),Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION));

        }

        alldata = new ArrayList<SectionDataModel>();
        createDummyData();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
      /*  menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.nav_Search).setVisible(true);*/

    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            timer.cancel();
        }catch (Exception e){}

    }
    public void createDummyData() {
        for (int i = 1; i <= 5; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            dm.setAllItemsInSection(singleItem);
            alldata.add(dm);


            final String urlHeyma = "http://musicgoo.net/download-music.php?song_id=3478";
            String urlSuite = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Tubelight%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/01%20Radio%20-%20Tubelight%20%28Kamaal%20Khan%29%20320Kbps.mp3";
            String urlgetlow = "https://dl.pagal.link/upload_file/5570/Eng%20Pop/Collection%202015/Get%20Low%20%28Furious%207%29%20Dillon%20Francis%20n%20DJ%20Snake%20320Kbps.mp3";
            String urlPhirv = "http://sd.yoyodesi.com/128/482473/I%20m%20The%20One%20Ft%20Justin%20Bieber%20Quavo%20Chance%20The%20Rapper%20%20Lil%20Wayne%20-%20DJ%20Khaled%20(DJJOhAL.Com).mp3";
            String urlSou = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Behen%20Hogi%20Teri%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/01%20Tera%20Hoke%20Rahoon%20%28Arijit%20Singh%29%20320Kbps.mp3";
            String Raabta1 = "http://mp3dl.djmaza.click/download/eea7670c027bad4250c924b4df483530";
            String url_Raabta = "https://dl.pagal.link/upload_file/5570/6757/Latest%20Bollywood%20Hindi%20Mp3%20Songs%20-%202017/Raabta%20%282017%29%20Hindi%20Movie%20Mp3%20Songs/05%20Main%20Tera%20Boyfriend%20-%20Raabta%20%28Arijit%20Singh%29%20320Kbps.mp3";
            String url_purpose = "http://dl2.shirazsong.org/dl/music/94-10/best-of-november-2015/01%20-%20Justin%20Bieber%20-%20Purpose.mp3";
//        Intent intent = new Intent(getActivity(), MusicPlayer.class);
//        intent.putExtra(EXTRA_MESSAGE, Uri.parse(url));
//        startActivity(intent);
            final String ff = "http://thefader-res.cloudinary.com/images/w_760,c_limit,f_auto,q_auto:best/fast8_qpwkhu/fate-of-the-furious-soundtrack-migos-young-thug.jpg";
            String Tubelight = "https://www.djmaza.life/storage/images/400/5201.jpg";
            String ffgetlow = "https://cccmurphysboro.files.wordpress.com/2012/05/get-low.jpg";
            String Raabta_img = "https://www.djmaza.life/storage/images/400/5205.jpg";
            String shapeofu = "https://www.djmaza.life/storage/images/400/5191.jpg";
            final String half1 = "http://lq.yoyodesi.com/covers/57967.jpg";
            final String Raabta = "https://www.djmaza.life/storage/images/400/5213.jpg";
            final String purpose = "http://ecx.images-amazon.com/images/I/51epu%2B3amVL._AC_AA160_.jpg";
            final String baby = "https://dl.pagal.link/upload_file/5570/5773/IndiPop%20Mp3%20Songs%20-%202017/Baby%20Marvake%20Maanegi%20-%20Raftaar%20-%20Mp3%20Song/thumb-Baby%20Marvake%20Maanegi%20-%20Raftaar%20-%20320Kbps.jpg";


            singleItem.add(new SingleItemModel("Fast & Furious", "GO LOW", ffgetlow, urlgetlow));
            singleItem.add(new SingleItemModel(" I m The One Ft Justin Bieber ", "Dj Khaled ", half1, urlPhirv));
            singleItem.add(new SingleItemModel("Lambiyaan Si Judaiyaan ", "Raabta", Raabta_img, Raabta1));
            singleItem.add(new SingleItemModel("Tera Hoke Rahoon", "Behen Hogi Teri", shapeofu, urlSou));
            singleItem.add(new SingleItemModel("Tubelight", "Radio", Tubelight, urlSuite));
            singleItem.add(new SingleItemModel("Main Tera Boyfriend", " Raabta  ", Raabta, url_Raabta));
            singleItem.add(new SingleItemModel("Fate and furious", "Hey maa", ff, urlHeyma));
            singleItem.add(new SingleItemModel("Purpose", "Purpose", purpose, url_purpose));
        }
    }


    @Override
    public void onHomePagePreExecuteStarted() {

        pDialog.show();
    }

    @Override
    public void onHomePagePostExecuteCompleted(HomePageOutputModel homePageOutputModel, int status, String message) {
         homePageSectionModel =homePageOutputModel.getHomePageSectionModel();
        adapter = new RecycleAdaptor(getActivity(), homePageSectionModel);
        my_recycler_view.setAdapter(adapter);
        pDialog.hide();


    }


}


