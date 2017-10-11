package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 08-May-17.
 */

public class SubscriptionPlanOutputModel {

    private String id;
    private String name;
    private String recurrence;
    private String frequency;
    private String studio_id;
    private String status;
    private String language_id;
    private String price;
    private String trial_period;
    private String trial_recurrence;

    public CurrencyModel getCurrencyDetails() {
        return currencyDetails;
    }

    public void setCurrencyDetails(CurrencyModel currencyDetails) {
        this.currencyDetails = currencyDetails;
    }

    CurrencyModel currencyDetails;

    public String getTrial_recurrence() {
        return trial_recurrence;
    }

    public void setTrial_recurrence(String trial_recurrence) {
        this.trial_recurrence = trial_recurrence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrial_period() {
        return trial_period;
    }

    public void setTrial_period(String trial_period) {
        this.trial_period = trial_period;
    }



}
