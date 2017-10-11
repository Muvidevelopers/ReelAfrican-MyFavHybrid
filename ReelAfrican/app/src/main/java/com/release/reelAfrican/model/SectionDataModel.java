package com.release.reelAfrican.model;

import java.util.ArrayList;

/**
 * Created by Muvi on 12/15/2016.
 */

public class SectionDataModel {
    private String headerTitle;
    private String sectiontype;

    public String getSectiontype() {
        return sectiontype;
    }

    public void setSectiontype(String sectiontype) {
        this.sectiontype = sectiontype;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    private String permalink;
    private ArrayList<SingleItemModel> allItemsInSection;








    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle,String sectiontype, String permalink, ArrayList<SingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.sectiontype = sectiontype;
        this.allItemsInSection = allItemsInSection;
        this.permalink = permalink;

    }



    public String getHeaderTitle() {
        return headerTitle;
    }
    public String getHeaderPermalink() {
        return permalink;
    }
    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
    public void setHHeaderPermalink(String permalink) {
        this.permalink = permalink;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
