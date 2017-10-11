package com.release.reelAfrican.physical;

/**
 * Created by MUVI on 7/3/2017.
 */

public class PurchaseHmodel {
    public String getInvoice() {
        return Invoice;
    }

    public void setInvoice(String invoice) {
        Invoice = invoice;
    }

    public String getPurchasedate() {
        return Purchasedate;
    }

    public void setPurchasedate(String purchasedate) {
        Purchasedate = purchasedate;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getTransactionStatus() {
        return TransactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        TransactionStatus = transactionStatus;
    }

    public String getCurrencysymbol() {
        return Currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        Currencysymbol = currencysymbol;
    }

    public String getCurrencycode() {
        return Currencycode;
    }

    public void setCurrencycode(String currencycode) {
        Currencycode = currencycode;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getContenttype() {
        return Contenttype;
    }

    public void setContenttype(String contenttype) {
        Contenttype = contenttype;
    }

    String Invoice,Purchasedate,Amount,TransactionStatus,Currencysymbol,Currencycode,Id, OrderId,Contenttype;

    public PurchaseHmodel(String Invoice, String Purchasedate, String Amount, String TransactionStatus, String Currencysymbol, String Currencycode,String Id,String OrderId,String Contenttype) {
        this.Invoice = Invoice;
        this.Purchasedate = Purchasedate;
        this.Amount = Amount;
        this.TransactionStatus = TransactionStatus;
        this.Currencysymbol = Currencysymbol;
        this.Currencycode = Currencycode;
        this.Id = Id;
        this.OrderId = OrderId;
        this.Contenttype = Contenttype;
    }


}
