package com.suljo.csc490buysellswap;

public class DbQueries {
    public static String loginQuery = """
        SELECT *
        FROM sprint04.[user]
        WHERE userName = ? AND pWord = ?;
        """;

    public static String insertNewListingQuery = """
        INSERT INTO sprint04.listing
            (title, description, priceInCents, isForRent, rentalPeriodHours, sellerUserID, listingImage)
        VALUES
            (?,?,?,?,?,?,?)
        ;
        """;


    public static String insertNewUserQuery = """
        INSERT INTO sprint04.[user]
            (userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, state, zip, phoneNum, isAdmin)
        VALUES
            (?,?,?,?,?,?,?,?,?,?,?,?)
        ;
        """;

    public static String selectMyListingsQuery = """
        SELECT *
        FROM sprint04.listing
        WHERE sellerUserID = ?
        ORDER BY datetimeAdded
        ;
        """;

    public static String selectAllActiveListingsQuery = """
            SELECT *
            FROM sprint04.listing
            WHERE isActive  = 1
            ORDER BY datetimeAdded
            ;
            """;
}
