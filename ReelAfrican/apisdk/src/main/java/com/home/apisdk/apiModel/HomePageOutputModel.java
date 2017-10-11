package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 08-May-17.
 */

public class HomePageOutputModel {

    ArrayList<HomePageBannerModel> homePageBannerModel;
    ArrayList<HomePageSectionModel> homePageSectionModel;

    HomePageTextModel homePageTextModel;

    public HomePageTextModel getHomePageTextModel() {
        return homePageTextModel;
    }

    public void setHomePageTextModel(HomePageTextModel homePageTextModel) {
        this.homePageTextModel = homePageTextModel;
    }

    public ArrayList<HomePageBannerModel> getHomePageBannerModel() {
        return homePageBannerModel;
    }

    public void setHomePageBannerModel( ArrayList<HomePageBannerModel> homePageBannerModel) {
        this.homePageBannerModel = homePageBannerModel;
    }

    public ArrayList<HomePageSectionModel> getHomePageSectionModel() {
        return homePageSectionModel;
    }

    public void setHomePageSectionModel(ArrayList<HomePageSectionModel> homePageSectionModel) {
        this.homePageSectionModel = homePageSectionModel;
    }


}
