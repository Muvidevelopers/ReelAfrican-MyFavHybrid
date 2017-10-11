package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 9/29/2016.
 */
public class Search_Data_input {
    String limit="10",authToken,offset="0",q;

   /* public Search_Data_input(String limit, String authToken, String offset, String q) {
        this.limit = limit;
        this.authToken = authToken;
        this.offset = offset;
        this.q = q;
    }*/

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
