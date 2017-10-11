package com.release.reelAfrican.physical;

/**
 * Created by MUVI on 7/16/2017.
 */

public class OrderDetailsModel {

    private String Oderid;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    private int Qty;
    private String Price;
    private String poster;
    private String currencysymbol;

    public String getCurrencysymbol() {
        return currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        this.currencysymbol = currencysymbol;
    }

    public String getOderid() {
        return Oderid;
    }

    public void setOderid(String oderid) {
        Oderid = oderid;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


    private String PName;
}
