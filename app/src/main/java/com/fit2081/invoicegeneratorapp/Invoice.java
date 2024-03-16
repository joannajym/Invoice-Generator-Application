package com.fit2081.invoicegeneratorapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoices")
public class Invoice {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "invoiceID")
    private String invoiceID;
    @ColumnInfo(name = "issuerName")
    private String issuerName;
    @ColumnInfo(name = "buyerName")
    private String buyerName;
    @ColumnInfo(name = "invoiceTotal")
    private double invoiceTotal;

    // Constructor
    public Invoice(String invoiceID, String issuerName, String buyerName, double invoiceTotal) {
        this.invoiceID = invoiceID;
        this.issuerName = issuerName;
        this.buyerName = buyerName;
        this.invoiceTotal = invoiceTotal;
    }

    // Getters and Setters
    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceID='" + invoiceID + '\'' +
                ", issuerName='" + issuerName + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", invoiceTotal=" + invoiceTotal +
                '}';
    }
}
