package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/21/2016.
 */
public class ContentListInput {
    String limit = "10",offset = "0";
    String orderby,authToken,permalink,country;

    /*public ContentListInput(String authToken,String permalink,String limit, String offset, String orderby , String country) {
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.authToken = authToken;
        this.permalink = permalink;
        this.country = country;
    }*/

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



}
