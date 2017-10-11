package com.release.reelAfrican.model;

import java.io.Serializable;

/**
 * Created by Muvi_BBSR on 6/15/2017.
 */

public class FooterMenuModel implements Serializable {

    String domain="";
    String link_type = "";
    String id = "";
    String display_name = "";
    String permalink = "";
    String url = "";





    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getLink_type()
    {
        return link_type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDisplay_name()
    {
        return display_name;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalink()
    {
        return permalink;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }


}
