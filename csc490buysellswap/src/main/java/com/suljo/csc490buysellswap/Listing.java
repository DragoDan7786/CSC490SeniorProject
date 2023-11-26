package com.suljo.csc490buysellswap;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;

public class Listing {
    private int listingID;
    private String title;
    private String description;
    private int priceInCents;
    private boolean isAvailable;
    private boolean isForRent;
    private int rentalPeriodHours;
    private Blob imageBlob;
    private Image image;
    private int sellerUserID;
    private String datetimeAdded;
    private String datetimeModified;
    private int soldAtPriceInCents;
    private boolean isActive;
    private boolean isVisible;
    private LocalDate dateSold;

    public Listing(int listingID, String title, String description, int priceInCents, boolean isAvailable,
                   boolean isForRent, int rentalPeriodHours, Blob imageBlob, int sellerUserID, String datetimeAdded,
                   String datetimeModified, int soldAtPriceInCents, boolean isActive, boolean isVisible,
                   LocalDate dateSold) {
        this.listingID = listingID;
        this.title = title;
        this.description = description;
        this.priceInCents = priceInCents;
        this.isAvailable = isAvailable;
        this.isForRent = isForRent;
        this.rentalPeriodHours = rentalPeriodHours;
        this.imageBlob = imageBlob;
        if (imageBlob != null){
            try {
                this.image = new Image(imageBlob.getBinaryStream());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.image = null;
        }
        this.sellerUserID = sellerUserID;
        this.datetimeAdded = datetimeAdded;
        this.datetimeModified = datetimeModified;
        this.soldAtPriceInCents = soldAtPriceInCents;
        this.isActive = isActive;
        this.isVisible = isVisible;
        this.dateSold = dateSold;
    }

    /**
     * Returns a copy of this Listing.
     * @return A shallow copy of this listing.
     */
    public Listing copy(){
        return new Listing(listingID, title, description, priceInCents, isAvailable, isForRent, rentalPeriodHours,
                imageBlob, sellerUserID, datetimeAdded, datetimeModified, soldAtPriceInCents, isActive, isVisible,
                dateSold);
    }

    /**
     * Sets the member variables of this listing to equal those of another, turning this listing into a shallow copy.
     * @param other The listing whose values wll be copied.
     */
    public void copyValues(Listing other){
        this.listingID = other.listingID;
        this.title = other.title;
        this.description = other.description;
        this.priceInCents = other.priceInCents;
        this.isAvailable = other.isAvailable;
        this.isForRent = other.isForRent;
        this.rentalPeriodHours = other.rentalPeriodHours;
        this.imageBlob = other.imageBlob;
        this.image = other.image;
        this.sellerUserID = other.sellerUserID;
        this.datetimeAdded = other.datetimeAdded;
        this.datetimeModified = other.datetimeModified;
        this.soldAtPriceInCents = other.soldAtPriceInCents;
        this.isActive = other.isActive;
        this.isVisible = other.isVisible;
        this.dateSold = other.dateSold;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isForRent() {
        return isForRent;
    }

    public void setForRent(boolean forRent) {
        isForRent = forRent;
    }

    public int getRentalPeriodHours() {
        return rentalPeriodHours;
    }

    public void setRentalPeriodHours(int rentalPeriodHours) {
        this.rentalPeriodHours = rentalPeriodHours;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }

    public int getSellerUserID() {
        return sellerUserID;
    }

    public void setSellerUserID(int sellerUserID) {
        this.sellerUserID = sellerUserID;
    }

    public String getDatetimeModified() {
        return datetimeModified;
    }

    public void setDatetimeModified(String datetimeModified) {
        this.datetimeModified = datetimeModified;
    }

    public int getSoldAtPriceInCents() {
        return soldAtPriceInCents;
    }

    public void setSoldAtPriceInCents(int soldAtPriceInCents) {
        this.soldAtPriceInCents = soldAtPriceInCents;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public LocalDate getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDate dateSold) {
        this.dateSold = dateSold;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
