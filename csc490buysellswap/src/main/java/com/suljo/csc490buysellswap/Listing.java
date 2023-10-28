package com.suljo.csc490buysellswap;

import java.sql.Blob;

public class Listing {
    private int listingID;
    private String title;
    private String description;
    private int priceInCents;
    private boolean isAvailable;
    private boolean isForRent;
    private int rentalPeriodHours;
    private Blob image;
    private int sellerUserID;
    private String datetimeAdded;
    private String datetimeModified;

    /**
     * Constructor for a listing with an image.
     * @param listingID
     * @param title
     * @param description
     * @param priceInCents
     * @param isAvailable
     * @param isForRent
     * @param rentalPeriodHours
     * @param image
     * @param sellerUserID
     * @param datetimeAdded
     * @param datetimeModified
     */
    public Listing(int listingID, String title, String description, int priceInCents, boolean isAvailable, boolean isForRent, int rentalPeriodHours, Blob image, int sellerUserID, String datetimeAdded, String datetimeModified) {
        this.listingID = listingID;
        this.title = title;
        this.description = description;
        this.priceInCents = priceInCents;
        this.isAvailable = isAvailable;
        this.isForRent = isForRent;
        this.rentalPeriodHours = rentalPeriodHours;
        this.image = image;
        this.sellerUserID = sellerUserID;
        this.datetimeAdded = datetimeAdded;
        this.datetimeModified = datetimeModified;
    }

    public int getListingID() {
        return listingID;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetimeAdded() {
        return datetimeAdded;
    }

    public void setDatetimeAdded(String datetimeAdded) {
        this.datetimeAdded = datetimeAdded;
    }
}
