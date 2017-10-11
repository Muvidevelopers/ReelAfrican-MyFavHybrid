package com.release.reelAfrican.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter.Pager;
import com.release.reelAfrican.adapter.VideoFilterAdapter;
import com.release.reelAfrican.model.GridItem;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

/*
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;
*/

/**
 * Created by user on 28-06-2015.
 */
public class SearchActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    ProgressBarHandler videoPDialog;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    int previousTotal = 0;
    ProgressBarHandler gDialog;

    SharedPreferences pref;
    private RelativeLayout noInternetConnectionLayout;
    //firsttime load
    boolean firstTime = false;

    //search
    String searchTextStr;
    boolean isSearched = false;
    private SearchView.SearchAutoComplete theTextArea;
    RelativeLayout noDataLayout;

    /*The Data to be posted*/
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;

    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    //Adapter for GridView
    private VideoFilterAdapter customGridAdapter;
    Toolbar mActionBarToolbar;

    //Model for GridView
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    GridLayoutManager mLayoutManager;
    String posterUrl;

    // UI
    private GridView gridView;
    RelativeLayout footerView;
    private String movieVideoUrlStr = "";
    //private String movieThirdPartyUrl = "";
    TextView noDataTextView;
    TextView noInternetTextView;

    public SearchActivity() {
        // Required empty public constructor

    }

    View header;
    private boolean isLoading = false;
    private int lastVisibleItem, totalItemCount;
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_search);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pref = getSharedPreferences(Util.LOGIN_PREF, 0); // 0 - for private mode

        posterUrl = Util.getTextofLanguage(SearchActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Vod"));
        tabLayout.addTab(tabLayout.newTab().setText("Audio"));
        tabLayout.addTab(tabLayout.newTab().setText("Physical"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setOnTabSelectedListener(this);

        footerView = (RelativeLayout) findViewById(R.id.loadingPanel);

        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(Util.getTextofLanguage(SearchActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(Util.getTextofLanguage(SearchActivity.this, Util.NO_CONTENT, Util.DEFAULT_NO_CONTENT));

        noInternetConnectionLayout.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);

        //subhalaxmi

        //Detect Network Connection

        boolean isNetwork = Util.checkNetwork(SearchActivity.this);
        if (isNetwork == false) {
            noInternetConnectionLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
           // gridView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        }



    }

    @Override
    public void onBackPressed() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_search, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(Util.getTextofLanguage(SearchActivity.this, Util.SEARCH_PLACEHOLDER, Util.DEFAULT_SEARCH_PLACEHOLDER));
        searchView.requestFocus();
        int searchImgId = getResources().getIdentifier(String.valueOf(R.id.search_button), null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);

        searchView.setMaxWidth(10000);

        final SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setBackgroundResource(R.drawable.edit);

        theTextArea.setHint(Util.getTextofLanguage(SearchActivity.this, Util.TEXT_SEARCH_PLACEHOLDER, Util.DEFAULT_TEXT_SEARCH_PLACEHOLDER));

        theTextArea.setHintTextColor(Color.parseColor("#dadada"));//or any color that you want
        theTextArea.setTextColor(Color.WHITE);

        theTextArea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    String query = theTextArea.getText().toString().trim();
                    if (query.equalsIgnoreCase("") || query == null) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this, R.style.MyAlertDialogStyle);
                        dlgAlert.setMessage(Util.getTextofLanguage(SearchActivity.this, Util.SEARCH_ALERT, Util.DEFAULT_SEARCH_ALERT));
                        dlgAlert.setTitle(Util.getTextofLanguage(SearchActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(SearchActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(Util.getTextofLanguage(SearchActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();
                    } else {
                        resetData();
                        firstTime = true;

                        offset = 1;
                        listSize = 0;
                        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                            limit = 20;
                        } else {
                            limit = 15;
                        }
                        itemsInServer = 0;
                        isLoading = false;
                        //searchTextStr = query;
                        Util.searchstring=query;
                        if (itemData != null && itemData.size() > 0) {
                            itemData.clear();
                        }
                        boolean isNetwork = Util.checkNetwork(SearchActivity.this);
                        isSearched = true;
                        if (isNetwork == false) {
                            noInternetConnectionLayout.setVisibility(View.VISIBLE);
                          //  gridView.setVisibility(View.GONE);

                        } else {



                                tabLayout.setVisibility(View.VISIBLE);
                                Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
                                viewPager.setAdapter(adapter);
                                tabLayout.setupWithViewPager(viewPager);


                        }
                    }
                    return true;
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return true;
    }




        public void onResume() {
            super.onResume();
            //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);


        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

        }

        //load video urls as per resolution

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }

        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
                this.clickListener = clickListener;
                gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        }
                    }
                });
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildPosition(child));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        }


        public void resetData() {
            if (itemData != null && itemData.size() > 0) {
                itemData.clear();
            }
            firstTime = true;

            offset = 1;
            isLoading = false;
            listSize = 0;
            if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
                limit = 20;
            } else {
                limit = 15;
            }
            itemsInServer = 0;
            isSearched = false;
        }

    }

