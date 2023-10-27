package com.suljo.csc490buysellswap;

import java.sql.Blob;

public class Listing {
    private int listingID;
    private String title; //TODO: Should change DDL name from
    private String description;
    private int priceInCents;
    private boolean isAvailable;
    private boolean isForRent;
    private int rentalPeriodHours;
    private Blob image;
    private int sellerUserID;
    private String datetimeAdded;
    private String datetimeModified;

}
