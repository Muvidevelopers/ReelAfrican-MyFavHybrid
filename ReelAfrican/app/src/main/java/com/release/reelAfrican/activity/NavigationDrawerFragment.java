package com.release.reelAfrican.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;


import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter.ExpandableListAdapter;
import com.release.reelAfrican.model.FooterMenuModel;
import com.release.reelAfrican.model.MainMenuChildModel;
import com.release.reelAfrican.model.MainMenuModel;
import com.release.reelAfrican.model.NavDrawerItem;
import com.release.reelAfrican.model.UserMenuChildModel;
import com.release.reelAfrican.model.UserMenuModel;
import com.release.reelAfrican.physical.ProductListingActivity;
import com.release.reelAfrican.utils.ProgressBarHandler;
import com.release.reelAfrican.utils.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private NavigationDrawerCallbacks mCallbacks;
    public static ArrayList<NavDrawerItem> menuList;
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;

    public HashMap<String, ArrayList<String>> expandableListDetail;
    ArrayList<String> titleArray = new ArrayList<>();
    ExpandableListAdapter adapter;
    SharedPreferences pref;


    String loggedInStr=null;
    Fragment fragment = null;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10, statusCode;
    TextView text;
    String str = "#", abs;
    String Title, Permalink, ID, TitleChild, PermalinkChild, IDChild, ClasChild, UserTitleChild,
            UserIDChild, UserParentIdChild, UserPermalinkChild, UserClasChild, fdomain, flink_type, fid, fdisplay_name,
            fpermalink, furl, ParentIdChild, LinkTypeChild, ParentId, Clas, LinkType, UserTitle, UserPermalink, UserID,
            UserParentId, UserClas,Value,Id_seq,Language_id,Language_parent_id,ValueChild,Id_seq_Child,Language_id_Child,Language_parent_id_Child;


    ArrayList<MainMenuModel> mainMenuModelArrayList = new ArrayList<MainMenuModel>();
    ArrayList<MainMenuChildModel> mainMenuChildModelArrayList = new ArrayList<MainMenuChildModel>();
    ArrayList<UserMenuModel> userMenuModelArrayList = new ArrayList<UserMenuModel>();
    ArrayList<UserMenuChildModel> userMenuChildModelArrayList = new ArrayList<UserMenuChildModel>();
    ArrayList<FooterMenuModel> footerMenuModelArrayList = new ArrayList<FooterMenuModel>();


    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    Button Send;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        pref = getActivity().getSharedPreferences(Util.LOGIN_PREF, 0);
        loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);


        AsynLoadMenuDetails asynLoadMenuDetails = new AsynLoadMenuDetails ();
        asynLoadMenuDetails.executeOnExecutor (threadPoolExecutor);



        /*HomeWatcher mHomeWatcher = new HomeWatcher(getContext());

        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...

                Log.v("SUBHA","Home Button pressed fragment");

                Fragment fragment = new HomeFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();

            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();*/
    }



//    private class AsynLoadMenuDetails extends AsyncTask<Void, Void, Void> {
//        String responseStr;
//        ProgressBarHandler progressDialog;
//
//        @Override
//
//        protected Void doInBackground (Void... params) {
//            String urlRouteList =Util.rootUrl().trim()+Util.GetMenusUrl.trim();
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpGet httppost = new HttpGet(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//
////                httppost.addHeader ("authToken", "15aa254ee854890b338ee76f0365b135");
//                httppost.addHeader("authToken",Util.authTokenStr.trim());
//                HttpResponse response = httpclient.execute (httppost);
//                responseStr = EntityUtils.toString (response.getEntity ());
//
//
//                JSONObject myJson = null;
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    statusCode = myJson.getInt ("code");
//                }
//
//
//                if (statusCode == 200) {
//
//                    mainMenuModelArrayList.clear ();
//                    mainMenuChildModelArrayList.clear ();
//                    userMenuModelArrayList.clear ();
//                    userMenuChildModelArrayList.clear ();
//                    footerMenuModelArrayList.clear ();
//
//
//                    try {
//                        menuList = new ArrayList<NavDrawerItem>();
////                        JSONObject jsonMain = myJson.optJSONObject ("menus");
//                        JSONArray jsonMainMenu = myJson.optJSONArray ("menu_items");
//
//                        MainMenuModel mainMenuModel1 = new MainMenuModel ();
//                        mainMenuModel1.setTitle (Util.getTextofLanguage(getActivity(), Util.HOME, Util.DEFAULT_HOME));
//                        mainMenuModelArrayList.add(mainMenuModel1);
//
//                        for (int i = 0; i < jsonMainMenu.length (); i++) {
//                            MainMenuModel mainMenuModel = new MainMenuModel ();
//                            Title = jsonMainMenu.getJSONObject (i).optString ("title").toString ().trim ();
//                            mainMenuModel.setTitle (Title);
//                            Permalink = jsonMainMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
//                            mainMenuModel.setPermalink (Permalink);
//                            ID = jsonMainMenu.getJSONObject (i).optString ("id").toString ().trim ();
//                            mainMenuModel.setId (ID);
//                            ParentId = jsonMainMenu.getJSONObject (i).optString ("parent_id").toString ().trim ();
//                            mainMenuModel.setParent_id (ParentId);
//                            Clas = jsonMainMenu.getJSONObject (i).optString ("class").toString ().trim ();
//                            mainMenuModel.setClas (Clas);
//                            LinkType = jsonMainMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
//                            mainMenuModel.setLink_type (LinkType);
//                            Value = jsonMainMenu.getJSONObject (i).optString ("value").toString ().trim ();
//                            mainMenuModel.setValue (Value);
//                            Language_id = jsonMainMenu.getJSONObject (i).optString ("language_id").toString ().trim ();
//                            mainMenuModel.setLanguage_id (Language_id);
//                            Language_parent_id = jsonMainMenu.getJSONObject (i).optString ("language_parent_id").toString ().trim ();
//                            mainMenuModel.setLanguage_parent_id (Language_parent_id);
//                            Id_seq = jsonMainMenu.getJSONObject (i).optString ("id_seq").toString ().trim ();
//                            mainMenuModel.setId_seq (Id_seq);
//
//                            mainMenuModelArrayList.add (mainMenuModel);
//
//                            try {
//
//
//                                JSONArray jsonChildNode = jsonMainMenu.getJSONObject (i).getJSONArray ("child");
//                                for (int j = 0; j < jsonChildNode.length (); j++) {
//
//                                    MainMenuChildModel mainMenuChildModel = new MainMenuChildModel ();
//                                    TitleChild = jsonChildNode.getJSONObject (j).optString ("title").toString ().trim ();
//                                    mainMenuChildModel.setTitle (TitleChild);
//                                    PermalinkChild = jsonChildNode.getJSONObject (j).optString ("permalink").toString ().trim ();
//                                    mainMenuChildModel.setPermalink (PermalinkChild);
//                                    IDChild = jsonChildNode.getJSONObject (j).optString ("id").toString ().trim ();
//                                    mainMenuChildModel.setId (IDChild);
//                                    ParentIdChild = jsonChildNode.getJSONObject (j).optString ("parent_id").toString ().trim ();
//                                    mainMenuChildModel.setParent_id (ParentIdChild);
//                                    ClasChild = jsonChildNode.getJSONObject (j).optString ("class").toString ().trim ();
//                                    mainMenuChildModel.setClas (ClasChild);
//                                    LinkTypeChild = jsonChildNode.getJSONObject (j).optString ("link_type").toString ().trim ();
//                                    mainMenuChildModel.setLink_type (LinkTypeChild);
//                                    ValueChild = jsonChildNode.getJSONObject (i).optString ("value").toString ().trim ();
//                                    mainMenuChildModel.setValue (ValueChild);
//                                    Language_id_Child = jsonChildNode.getJSONObject (i).optString ("language_id").toString ().trim ();
//                                    mainMenuChildModel.setLanguage_id (Language_id_Child);
//                                    Language_parent_id_Child = jsonChildNode.getJSONObject (i).optString ("language_parent_id").toString ().trim ();
//                                    mainMenuChildModel.setLanguage_parent_id (Language_parent_id_Child);
//                                    Id_seq_Child = jsonChildNode.getJSONObject (i).optString ("id_seq").toString ().trim ();
//                                    mainMenuChildModel.setId_seq (Id_seq_Child);
//
//
//                                    mainMenuChildModelArrayList.add (mainMenuChildModel);
//
//
//                                }
//                            } catch (Exception e) {
//                            }
//                        }
//
//
//
//                        JSONArray jsonFooterMenu = myJson.optJSONArray ("footer_menu");
//
//                        try {
//                            for (int i = 0; i < jsonFooterMenu.length (); i++) {
//                                FooterMenuModel footerMenuModel = new FooterMenuModel ();
//                                fdomain = jsonFooterMenu.getJSONObject (i).optString ("domain").toString ().trim ();
//                                footerMenuModel.setDomain (fdomain);
//                                flink_type = jsonFooterMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
//                                footerMenuModel.setLink_type (Permalink);
//                                fid = jsonFooterMenu.getJSONObject (i).optString ("id").toString ().trim ();
//                                footerMenuModel.setId (fid);
//                                fdisplay_name = jsonFooterMenu.getJSONObject (i).optString ("display_name").toString ().trim ();
//                                footerMenuModel.setDisplay_name (fdisplay_name);
//                                fpermalink = jsonFooterMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
//                                footerMenuModel.setPermalink (fpermalink);
//                                furl = jsonFooterMenu.getJSONObject (i).optString ("url").toString ().trim ();
//                                footerMenuModel.setUrl (furl);
//                                footerMenuModelArrayList.add (footerMenuModel);
//
//                            }
//                        } catch (Exception e) {
//                        }
//
//                    } catch (Exception e) {
//                    }
//
//
//                }
//            } catch (Exception e) {
//            }
//
//
//            return null;
//
//        }
//
//
//        protected void onPostExecute (Void result) {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.hide();
//                progressDialog = null;
//            }
//            setMenuItemsInDrawer();
//
//        }
//
//
//        @Override
//        protected void onPreExecute () {
////            progressDialog = ProgressDialog.show(this, "", "Please Wait.....");
//
//
//            progressDialog = new ProgressBarHandler(getActivity ());
//            progressDialog.show();
//
//
//            /*progressDialog = new ProgressDialog(getActivity (), R.style.CustomDialogTheme);
//            progressDialog.setCancelable (false);
//            progressDialog.setProgressStyle (android.R.style.Widget_ProgressBar_Large_Inverse);
//            progressDialog.setIndeterminate (false);
//            progressDialog.setIndeterminateDrawable (getResources ().getDrawable (R.drawable.progress_rawable));
//            progressDialog.show ();*/
//        }
//
//    }



    private class AsynLoadMenuDetails extends AsyncTask<Void, Void, Void> {
        String responseStr;
        ProgressBarHandler progressDialog;

        @Override

        protected Void doInBackground (Void... params) {
            String urlRouteList =Util.rootUrl().trim()+Util.GetMenusUrl.trim();
            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

//                httppost.addHeader ("authToken", "15aa254ee854890b338ee76f0365b135");
                httppost.addHeader("authToken",Util.authTokenStr.trim());
                HttpResponse response = httpclient.execute (httppost);
                responseStr = EntityUtils.toString (response.getEntity ());


                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    statusCode = myJson.getInt ("code");
                }


                if (statusCode == 200) {

                    mainMenuModelArrayList.clear ();
                    mainMenuChildModelArrayList.clear ();
                    userMenuModelArrayList.clear ();
                    userMenuChildModelArrayList.clear ();
                    footerMenuModelArrayList.clear ();


                    try {
                        menuList = new ArrayList<NavDrawerItem>();
                        JSONObject jsonMain = myJson.optJSONObject ("menus");
                        JSONArray jsonMainMenu = jsonMain.optJSONArray ("mainmenu");

                        MainMenuModel mainMenuModel1 = new MainMenuModel ();
                        mainMenuModel1.setTitle (Util.getTextofLanguage(getActivity(), Util.HOME, Util.DEFAULT_HOME));
                        mainMenuModelArrayList.add(mainMenuModel1);

                        for (int i = 0; i < jsonMainMenu.length (); i++) {
                            MainMenuModel mainMenuModel = new MainMenuModel ();
                            Title = jsonMainMenu.getJSONObject (i).optString ("title").toString ().trim ();
                            mainMenuModel.setTitle (Title);
                            Permalink = jsonMainMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
                            mainMenuModel.setPermalink (Permalink);
                            ID = jsonMainMenu.getJSONObject (i).optString ("id").toString ().trim ();
                            mainMenuModel.setId (ID);
                            ParentId = jsonMainMenu.getJSONObject (i).optString ("parent_id").toString ().trim ();
                            mainMenuModel.setParent_id (ParentId);
                            Clas = jsonMainMenu.getJSONObject (i).optString ("class").toString ().trim ();
                            mainMenuModel.setClas (Clas);
                            LinkType = jsonMainMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
                            mainMenuModel.setLink_type (LinkType);
                            mainMenuModelArrayList.add (mainMenuModel);

                            try {


                                JSONArray jsonChildNode = jsonMainMenu.getJSONObject (i).getJSONArray ("children");
                                for (int j = 0; j < jsonChildNode.length (); j++) {

                                    MainMenuChildModel mainMenuChildModel = new MainMenuChildModel ();
                                    TitleChild = jsonChildNode.getJSONObject (j).optString ("title").toString ().trim ();
                                    mainMenuChildModel.setTitle (TitleChild);
                                    PermalinkChild = jsonChildNode.getJSONObject (j).optString ("permalink").toString ().trim ();
                                    mainMenuChildModel.setPermalink (PermalinkChild);
                                    IDChild = jsonChildNode.getJSONObject (j).optString ("id").toString ().trim ();
                                    mainMenuChildModel.setId (IDChild);
                                    ParentIdChild = jsonChildNode.getJSONObject (j).optString ("parent_id").toString ().trim ();
                                    mainMenuChildModel.setParent_id (ParentIdChild);
                                    ClasChild = jsonChildNode.getJSONObject (j).optString ("class").toString ().trim ();
                                    mainMenuChildModel.setClas (ClasChild);
                                    LinkTypeChild = jsonChildNode.getJSONObject (j).optString ("link_type").toString ().trim ();
                                    mainMenuChildModel.setLink_type (LinkTypeChild);
                                    mainMenuChildModelArrayList.add (mainMenuChildModel);


                                }
                            } catch (Exception e) {
                            }
                        }

                        //}
                        /*JSONArray jsonUserMenu = jsonMain.optJSONArray ("usermenu");
                        try {
                            for (int i = 0; i < jsonUserMenu.length (); i++) {
                                UserMenuModel userMenuModel = new UserMenuModel ();
                                UserTitle = jsonUserMenu.optJSONObject (i).optString ("title").toString ().trim ();
                                userMenuModel.setTitle (UserTitle);
                                UserPermalink = jsonUserMenu.optJSONObject (i).optString ("permalink").toString ().trim ();
                                userMenuModel.setPermalink (UserPermalink);
                                UserID = jsonUserMenu.optJSONObject (i).optString ("id").toString ().trim ();
                                userMenuModel.setId (UserID);
                                UserParentId = jsonUserMenu.optJSONObject (i).optString ("parent_id").toString ().trim ();
                                userMenuModel.setParent_id (UserParentId);
                                UserClas = jsonUserMenu.optJSONObject (i).optString ("class").toString ().trim ();
                                userMenuModel.setClas (UserClas);
                                userMenuModelArrayList.add (userMenuModel);


                                if (UserPermalink.equals (str)) {
                                    try {

                                        JSONArray jsonUserChildNode = jsonUserMenu.getJSONObject (i).getJSONArray ("children");
                                        int lengthJsonUserChildArr = jsonUserChildNode.length ();
                                        for (int j = 0; j < lengthJsonUserChildArr; j++) {

                                            UserMenuChildModel userMenuChildModel = new UserMenuChildModel ();
                                            UserTitleChild = jsonUserChildNode.optJSONObject (j).optString ("title").toString ().trim ();
                                            userMenuChildModel.setTitle (UserTitleChild);

                                            UserPermalinkChild = jsonUserChildNode.optJSONObject (j).optString ("permalink").toString ().trim ();
                                            userMenuChildModel.setPermalink (UserPermalinkChild);

                                            UserIDChild = jsonUserChildNode.optJSONObject (j).optString ("id").toString ().trim ();
                                            userMenuChildModel.setId (UserIDChild);
                                            Log.d ("ANU", "Response===" + UserIDChild);
                                            UserParentIdChild = jsonUserChildNode.optJSONObject (j).optString ("parent_id").toString ().trim ();
                                            userMenuChildModel.setParent_id (UserParentIdChild);
                                            Log.d ("ANU", "Response===" + UserParentIdChild);
                                            UserClasChild = jsonUserChildNode.optJSONObject (j).optString ("class").toString ().trim ();
                                            userMenuChildModel.setClas (UserClasChild);
                                            Log.d ("ANU", "Response===" + UserClasChild);
                                            userMenuChildModelArrayList.add (userMenuChildModel);


                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }*/


                        JSONArray jsonFooterMenu = jsonMain.optJSONArray ("footer_menu");

                        try {
                            for (int i = 0; i < jsonFooterMenu.length (); i++) {
                                FooterMenuModel footerMenuModel = new FooterMenuModel ();
                                fdomain = jsonFooterMenu.getJSONObject (i).optString ("domain").toString ().trim ();
                                footerMenuModel.setDomain (fdomain);
                                flink_type = jsonFooterMenu.getJSONObject (i).optString ("link_type").toString ().trim ();
                                footerMenuModel.setLink_type (Permalink);
                                fid = jsonFooterMenu.getJSONObject (i).optString ("id").toString ().trim ();
                                footerMenuModel.setId (fid);
                                fdisplay_name = jsonFooterMenu.getJSONObject (i).optString ("display_name").toString ().trim ();
                                footerMenuModel.setDisplay_name (fdisplay_name);
                                fpermalink = jsonFooterMenu.getJSONObject (i).optString ("permalink").toString ().trim ();
                                footerMenuModel.setPermalink (fpermalink);
                                furl = jsonFooterMenu.getJSONObject (i).optString ("url").toString ().trim ();
                                footerMenuModel.setUrl (furl);
                                footerMenuModelArrayList.add (footerMenuModel);

                            }
                        } catch (Exception e) {
                        }

                    } catch (Exception e) {
                    }


                }
            } catch (Exception e) {
            }


            return null;

        }


        protected void onPostExecute (Void result) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
                progressDialog = null;
            }
            setMenuItemsInDrawer();

        }


        @Override
        protected void onPreExecute () {
//            progressDialog = ProgressDialog.show(this, "", "Please Wait.....");


            progressDialog = new ProgressBarHandler(getActivity ());
            progressDialog.show();


            /*progressDialog = new ProgressDialog(getActivity (), R.style.CustomDialogTheme);
            progressDialog.setCancelable (false);
            progressDialog.setProgressStyle (android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog.setIndeterminate (false);
            progressDialog.setIndeterminateDrawable (getResources ().getDrawable (R.drawable.progress_rawable));
            progressDialog.show ();*/
        }

    }







    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu (true);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mDrawerListView = (ExpandableListView) inflater.inflate (R.layout.drawer_drawer, container, false);
        mDrawerListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

               selectItem (position);


            }
        });

      /*  expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
        mDrawerListView.setAdapter(new ExpandableListAdapter(getActivity(),mainMenuModelArrayList, mainMenuChildModelArrayList));
     */
        //for expand the child content in navigation
        mDrawerListView.setOnGroupExpandListener (new OnGroupExpandListener() {

            @Override
            public void onGroupExpand (int groupPosition) {

                Util.drawer_collapse_expand_imageview.remove(groupPosition);
                Util.drawer_collapse_expand_imageview.add(groupPosition,groupPosition+","+1);
                Log.v("SUBHA1","setOnGroupExpandListener==="+groupPosition);


                for(int i=0;i<Util.drawer_collapse_expand_imageview.size();i++)
                {
                    String expand_collapse_image_info[] = Util.drawer_collapse_expand_imageview.get(i).split(",");
                    Log.v("SUBHA1","setOnGroupExpandListener===Data=========="+expand_collapse_image_info[0]+","+expand_collapse_image_info[1]);
                }


            }
        });
        //for expan less the child content in navigation
        mDrawerListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Util.drawer_collapse_expand_imageview.remove(groupPosition);
                Util.drawer_collapse_expand_imageview.add(groupPosition,groupPosition+","+0);
                Log.v("SUBHA1","setOnGroupCollapseListener===="+groupPosition);

                for(int i=0;i<Util.drawer_collapse_expand_imageview.size();i++)
                {
                    String expand_collapse_image_info[] = Util.drawer_collapse_expand_imageview.get(i).split(",");
                    Log.v("SUBHA1","setOnGroupCollapseListener===Data=========="+expand_collapse_image_info[0]+","+expand_collapse_image_info[1]);
                }


            }
        });


        mDrawerListView.setOnGroupClickListener (new ExpandableListView.OnGroupClickListener () {
            public boolean onGroupClick (ExpandableListView parent, View v, int listPosition, long id) {
                boolean retVal = true;
                boolean mylibrary_title_added = false;


                if(mainMenuModelArrayList.size()>listPosition){


                    for (int l = 0; l < mainMenuChildModelArrayList.size (); l++) {

                        if (mainMenuModelArrayList.get(listPosition).getId().equals (mainMenuChildModelArrayList.get (l).getParent_id ())) {
                            retVal = false;
                        }
                    }
                }

                for (int i = 0; i < footerMenuModelArrayList.size (); i++) {
                    Log.v("SUBHA","title"+footerMenuModelArrayList.get(i).getPermalink());
                    Log.v("SUBHA","titleArray.get(listPosition)"+titleArray.get(listPosition));

                    if(footerMenuModelArrayList.get(i).getDisplay_name().trim().equals(titleArray.get(listPosition)))
                    {
                        if(footerMenuModelArrayList.get(i).getPermalink().equals("contactus"))
                        {                         //   isNavigated = 1;

                            fragment = new ContactUs();
                            Bundle bundle = new Bundle();
                            bundle.putString("title", footerMenuModelArrayList.get(i).getDisplay_name());
                            Log.v("SUBHA","CONTACT USfooterMenuModelArrayList.get(i).getPermalink()"+footerMenuModelArrayList.get(i).getDisplay_name());

                            fragment.setArguments(bundle);
                            getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();
                            mDrawerLayout.closeDrawers();
                        }
                        else {


                            if (footerMenuModelArrayList.get(i).getLink_type().trim().equalsIgnoreCase("external")){
                              /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(footerMenuModelArrayList.get(listPosition).getUrl().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);
*/
                                //isNavigated = 1;
                                Log.v("SUBHA","hello"+footerMenuModelArrayList.get(i).getDisplay_name());

                                mDrawerLayout.openDrawer(Gravity.LEFT);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(footerMenuModelArrayList.get(listPosition).getUrl().trim()));
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(browserIntent);

                            }else {
                               // isNavigated = 1;

                                fragment = new AboutUs();
                                Bundle bundle = new Bundle();
                                Log.v("SUBHA","footerMenuModelArrayList.get(i).getPermalink()"+footerMenuModelArrayList.get(i).getPermalink());
                                bundle.putString("item", footerMenuModelArrayList.get(i).getPermalink());
                                bundle.putString("title", footerMenuModelArrayList.get(i).getDisplay_name());
                                fragment.setArguments(bundle);
                                getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();
                                mDrawerLayout.closeDrawers();
                            }



                           /* fragment = new AboutUs();
                            Bundle bundle = new Bundle();
                            bundle.putString("item", footerMenuModelArrayList.get(i).getPermalink());
                            bundle.putString("title", footerMenuModelArrayList.get(i).getDisplay_name());
                            fragment.setArguments(bundle);*/
                        }

                    }


                }



                if(listPosition==0)
                {
                   // isNavigated = 1;

                    Fragment fragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    fragment.setArguments(bundle);
                    getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();
                    mDrawerLayout.closeDrawers();
                }
               // this is for if child is not there then another fragment open
                else {
                    if (retVal){

                        if(mainMenuModelArrayList.size()>listPosition)
                        {
                            if(mainMenuModelArrayList.get(listPosition).getTitle().equals(Util.getTextofLanguage(getActivity(), Util.MY_LIBRARY, Util.DEFAULT_MY_LIBRARY))){
                               // isNavigated = 1;

                                fragment = new MyLibraryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", mainMenuModelArrayList.get(listPosition).getTitle());
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                mDrawerLayout.closeDrawers();
                            }



                            else if(mainMenuModelArrayList.get(listPosition).getPermalink().equals("muvikart")){


                                mDrawerLayout.closeDrawers();
                                // isNavigated = 1;

                                Intent productlist = new Intent(getActivity(), ProductListingActivity.class);
                                productlist.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(productlist);


                            }

                            else
                            {

                                if (mainMenuModelArrayList.get(listPosition).getLink_type().equalsIgnoreCase("2")){
                                  //  isNavigated = 1;

                                   // getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.openDrawer(Gravity.LEFT);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mainMenuModelArrayList.get(listPosition).getPermalink().trim()));
                                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(browserIntent);
                                    return retVal;


                                }else {
                                   // isNavigated = 1;

                                    fragment = new VideosListFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title", mainMenuModelArrayList.get(listPosition).getTitle());
                                    bundle.putString("item", mainMenuModelArrayList.get(listPosition).getPermalink());
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                    mDrawerLayout.closeDrawers();
                                }

                            }
                        }
                    }
                }

                return retVal;
            }
        });



        mDrawerListView.setOnChildClickListener (new ExpandableListView.OnChildClickListener () {

            @Override
            public boolean onChildClick (ExpandableListView parent, View v,
                                         int listPosition, int childPosition, long id) {

//=================================================================================================//

             /*   String ParentId = mainMenuModelArrayList.get(listPosition).getId ();
                ArrayList<String> arrayList = new ArrayList<String>();

               *//* for(int i=0 ;i<mainMenuChildModelArrayList.size ();i++)
                {
                    if(mainMenuChildModelArrayList.get(i).getParent_id ().equals (ParentId))
                    {
                        arrayList.add (mainMenuChildModelArrayList.get(i).getTitle ());
                        arrayList.add (mainMenuChildModelArrayList.get(i).getPermalink ());
                    }
                }*//*

                //Toast.makeText(getActivity(), "clicked the child content", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new VideosListFragment();
                        Bundle args = new Bundle();
                        //args.putString ("data", arrayList.get(childPosition));
                         args.putString ("title", mainMenuChildModelArrayList.get(childPosition).getTitle ());
                         args.putString ("item", mainMenuChildModelArrayList.get(childPosition).getPermalink ());
               *//* args.putString ("title", mainMenuChildModelArrayList.get(i).getTitle ());
                args.putString ("item", mainMenuChildModelArrayList.get(i).getPermalink ());*//*
                        fragment.setArguments (args);

                        //Inflate the fragment*/


                //===========================================================================//

                String ParentId = mainMenuModelArrayList.get(listPosition).getId ();
                ArrayList<Integer> arrayList = new ArrayList<Integer>();

                for(int i=0 ;i<mainMenuChildModelArrayList.size ();i++)
                {
                    if(mainMenuChildModelArrayList.get(i).getParent_id ().equals (ParentId))
                    {
                        arrayList.add(i);
                    }
                }
               // isNavigated = 1;

                Fragment fragment = new VideosListFragment();
                Bundle args = new Bundle();
                args.putString ("title", mainMenuChildModelArrayList.get(arrayList.get(childPosition)).getTitle());
                args.putString ("item", mainMenuChildModelArrayList.get(arrayList.get(childPosition)).getPermalink());
                fragment.setArguments (args);

                //Inflate the fragment

                //=========================================================//

                        getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();
                mDrawerLayout.closeDrawers ();

                return true;
            }
        });

        mDrawerListView.setItemChecked (mCurrentSelectedPosition, true);

        View header = inflater.inflate (R.layout.drawer_header, null);
        mDrawerListView.addHeaderView (header);



        return mDrawerListView;
    }


    public boolean isDrawerOpen () {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen (mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp (int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity ().findViewById (fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow (R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        try {
            ActionBar actionBar = getActionBar ();

            actionBar.setDisplayHomeAsUpEnabled (true);
            actionBar.setHomeButtonEnabled (true);
        } catch (Exception e) {
        }
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity (),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed (View drawerView) {
                super.onDrawerClosed (drawerView);
                //getActionBar ().setIcon (R.drawable.ic_drawer);

                if (!isAdded ()) {
                    return;
                }

                getActivity ().supportInvalidateOptionsMenu (); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened (View drawerView) {
                super.onDrawerOpened (drawerView);
                loggedInStr = pref.getString("PREFS_LOGGEDIN_KEY", null);

                boolean my_libary_added = false;
//add library to navdrawer if it login and enable from cms/**********/
                for(int i=0;i<mainMenuModelArrayList.size();i++) {

                    if (mainMenuModelArrayList.get(i).getTitle().trim().equals(Util.getTextofLanguage(getActivity(), Util.MY_LIBRARY, Util.DEFAULT_MY_LIBRARY))) {
                        my_libary_added = true;
                    }
                }

                    if (Util.getTextofLanguage(getActivity(), Util.IS_MYLIBRARY, Util.DEFAULT_IS_MYLIBRARY).equals("1") && loggedInStr != null) {
                        if(!my_libary_added)
                        {
                            MainMenuModel mainMenuModel1 = new MainMenuModel ();
                            mainMenuModel1.setTitle (Util.getTextofLanguage(getActivity(), Util.MY_LIBRARY, Util.DEFAULT_MY_LIBRARY));
                            mainMenuModelArrayList.add(mainMenuModel1);
                            setMenuItemsInDrawer();
                        }
                    }
                    else{
                        if(my_libary_added)
                        {
                            mainMenuModelArrayList.remove(mainMenuModelArrayList.size()-1);
                            setMenuItemsInDrawer();
                        }
                    }


                if (!isAdded ()) {
                    return;
                }


                //getActivity ().supportInvalidateOptionsMenu (); // calls onPrepareOptionsMenu()
            }
        };


        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post (new Runnable() {
            @Override
            public void run () {
                mDrawerToggle.syncState ();
            }
        });

        mDrawerLayout.setDrawerListener (mDrawerToggle);
    }

    private void selectItem (int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked (position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer (mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected (position);
        }
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach (activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach () {
        super.onDetach ();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putInt (STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged (newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged (newConfig);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen ()) {
            inflater.inflate (R.menu.menu_main, menu);
            showGlobalContextActionBar ();
        }
        super.onCreateOptionsMenu (menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected (item)) {
            return true;
        }


        return super.onOptionsItemSelected (item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar () {
        ActionBar actionBar = getActionBar ();
        actionBar.setDisplayShowTitleEnabled (true);
        actionBar.setNavigationMode (ActionBar.NAVIGATION_MODE_STANDARD);

    }

    private ActionBar getActionBar () {
        return ((ActionBarActivity) getActivity ()).getSupportActionBar ();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public void setMenuItemsInDrawer(){
        expandableListDetail = new LinkedHashMap<String, ArrayList<String>>();
        titleArray.clear();

        for (int i = 0; i < mainMenuModelArrayList.size (); i++) {
            titleArray.add (mainMenuModelArrayList.get (i).getTitle ());
            ArrayList<String> childArray = new ArrayList<>();

            for (int j = 0; j < mainMenuChildModelArrayList.size (); j++) {


                if (mainMenuModelArrayList.get (i).getId ().equals (mainMenuChildModelArrayList.get (j).getParent_id ())) {
                    childArray.add (mainMenuChildModelArrayList.get (j).getTitle ());


                }
            }

            expandableListDetail.put(mainMenuModelArrayList.get(i).getTitle(), childArray);

        }

        for (int k = 0; k < footerMenuModelArrayList.size (); k++) {
            titleArray.add(footerMenuModelArrayList.get(k).getDisplay_name());
            ArrayList<String> childArray = new ArrayList<>();
            expandableListDetail.put(footerMenuModelArrayList.get(k).getDisplay_name(), childArray);
        }

        adapter = new ExpandableListAdapter(getActivity (),titleArray, expandableListDetail,mainMenuModelArrayList,footerMenuModelArrayList);
        mDrawerListView.setAdapter (adapter);
        adapter.notifyDataSetChanged();




        Fragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        getFragmentManager ().beginTransaction ().replace (R.id.container, fragment).commit ();
    }
}
