package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/29/2016.
 */
public class Episode_Details_input {
    String permalink,authtoken,limit="10",offset="0";

   /* public Episode_Details_input(String permalink, String authtoken, String limit, String offset) {
        this.permalink = permalink;
        this.authtoken = authtoken;
        this.limit = limit;
        this.offset = offset;
    }*/

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
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
}
