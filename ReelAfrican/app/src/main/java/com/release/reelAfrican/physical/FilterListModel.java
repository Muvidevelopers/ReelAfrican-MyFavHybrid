package com.release.reelAfrican.physical;

/**
 * Created by Muvi on 10/5/2016.
 */
public class FilterListModel {

    private String title;

    public boolean isGenre() {
        return isGenre;
    }

    public void setIsGenre(boolean isGenre) {
        this.isGenre = isGenre;
    }

    private boolean isGenre;

    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getSectionType() {
        return value;
    }

    public void setSectionType(String sectionType) {
        this.value = sectionType;
    }

    private String value;


    public FilterListModel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public FilterListModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
