package com.home.apisdk.apiModel;

/**
 * Created by User on 13-12-2016.
 */
public class HomePageSectionModel {

    private String studio_id;
    private String section_id;
    private String title;
    private String language_id;


    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
