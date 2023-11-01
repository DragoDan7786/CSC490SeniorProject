package com.suljo.csc490buysellswap;

public class User {
    private int userID;
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth; //Refactor this and other dates from String to Date or to LocalDate?
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNum;
    private boolean isAdmin;
    private boolean isActive;
    private String registrationDatetime;

    /**
     * Default constructor.
     */
    public User(){

    }

    public User(int userID, String userName, String password, String firstName, String middleName, String lastName,
                String dateOfBirth, String street, String city, String state, String zip, String phoneNum,
                boolean isAdmin, boolean isActive, String registrationDatetime) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNum = phoneNum;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
        this.registrationDatetime = registrationDatetime;
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

    public Boolean isAdmin() {
        return isAdmin;
    }

    public User setAdmin(Boolean admin) {
        isAdmin = admin;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRegistrationDatetime() {
        return registrationDatetime;
    }

    public void setRegistrationDatetime(String registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }
}
