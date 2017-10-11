package com.release.reelAfrican.model;

import java.io.Serializable;

/**
 * Created by Muvi_BBSR on 6/15/2017.
 */

public class MainMenuChildModel implements Serializable {

    String title="";
    String permalink="";
    String id="";
    String parent_id="";
    String Class="";
    String link_type="";
    String value = "";
    String id_seq = "";
    String language_id = "";
    String language_parent_id = "";


    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setPermalink(String permalink){
        this.permalink = permalink;
    }
    public String getPermalink(){
        return permalink;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setParent_id(String parent_id){
        this.parent_id = parent_id;
    }
    public String getParent_id(){
        return parent_id;
    }

    public void setClas(String Class){
        this.Class = Class;
    }
    public String getClas(){
        return Class;
    }

    public void setLink_type(String link_type){
        this.link_type = link_type;
    }
    public String getLink_type(){
        return link_type;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setId_seq(String id_seq) {
        this.id_seq = id_seq;
    }

    public String getId_seq() {
        return id_seq;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_parent_id(String language_parent_id) {
        this.language_parent_id = language_parent_id;
    }

    public String getLanguage_parent_id() {
        return language_parent_id;
    }


}
