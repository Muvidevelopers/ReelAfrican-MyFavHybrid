package com.release.reelAfrican.model;

import java.io.Serializable;

/**
 * Created by Muvi_BBSR on 6/15/2017.
 */

public class UserMenuChildModel implements Serializable {

    String title = "";
    String permalink = "";
    String id = "";
    String parent_id = "";
    String Class = "";



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setClas(String Class) {
        this.Class = Class;
    }

    public String getClas() {
        return Class;
    }




}
