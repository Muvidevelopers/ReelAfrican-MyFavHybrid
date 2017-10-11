package com.release.reelAfrican.physical;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProductlistingModel {

    public String getProdidd() {
        return prodidd;
    }

    public void setProdidd(String prodidd) {
        this.prodidd = prodidd;
    }

    private String ID;
    private String poster;

    public String getCurrencysymbol() {
        return currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        this.currencysymbol = currencysymbol;
    }

    private String prodidd;
    private String currencysymbol;


    public String getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(String currencyid) {
        this.currencyid = currencyid;
    }

    private String title;
    private String genere;
    private String currencyid;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUniqid() {
        return uniqid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(String custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getIs_free_offer() {
        return is_free_offer;
    }

    public void setIs_free_offer(String is_free_offer) {
        this.is_free_offer = is_free_offer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPersonalization_id() {
        return personalization_id;
    }

    public void setPersonalization_id(String personalization_id) {
        this.personalization_id = personalization_id;
    }

    public String getPersonalization_image() {
        return personalization_image;
    }

    public void setPersonalization_image(String personalization_image) {
        this.personalization_image = personalization_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String quantity;
    private String sku;
    private String uniqid;
    private String permalink;
    private String custom_fields;
    private String is_free_offer;
    private String size;

    private String personalization_id;
    private String personalization_image;
    private String status;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private int item_Count;

    public int getItem_Count() {
        return item_Count;
    }

    public void setItem_Count(int item_Count) {
        this.item_Count = item_Count;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }



}
