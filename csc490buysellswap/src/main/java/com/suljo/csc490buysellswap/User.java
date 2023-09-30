package com.suljo.csc490buysellswap;

public class User {
    private int userID;
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    //Should this really be a string?
    //Or should it be an int, which can be translated to a date of birth - e.g., number of days since January 1st, 1900?
    private String dateOfBirth;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNum;
    private String hash;
    private Boolean isAdmin;

    /**
     * Default constructor.
     */
    public User(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String password() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String firstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String middleName() {
        return middleName;
    }

    public User setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String lastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String dateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String street() {
        return street;
    }

    public User setStreet(String street) {
        this.street = street;
        return this;
    }

    public String city() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }

    public String state() {
        return state;
    }

    public User setState(String state) {
        this.state = state;
        return this;
    }

    public String zip() {
        return zip;
    }

    public User setZip(String zip) {
        this.zip = zip;
        return this;
    }

    public String phoneNum() {
        return phoneNum;
    }

    public User setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public String hash() {
        return hash;
    }

    public User setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public User setAdmin(Boolean admin) {
        isAdmin = admin;
        return this;
    }

}
