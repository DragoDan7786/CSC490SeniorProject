package com.suljo.csc490buysellswap;

public class DbQueries {
    public static String loginQuery = """
        SELECT *
        FROM sprint03.[user]
        WHERE userName = ? AND pWord = ?;
        """;

    public static String insertNewListingQuery = """
        INSERT INTO sprint03.listing
            (title, description, priceInCents, isForRent, rentalPeriodHours, sellerUserID, listingImage)
        VALUES
            (?,?,?,?,?,?,?)
        ;
        """;


    public static String insertNewUserQuery = """
        INSERT INTO sprint03.[user]
            (userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, state, zip, phoneNum, isAdmin)
        VALUES
            (?,?,?,?,?,?,?,?,?,?,?,?)
        ;
        """;

    public static String selectMyListingsQuery = """
        SELECT *
        FROM sprint03.listing
        WHERE sellerUserID = ?
        ;
        """;
}
