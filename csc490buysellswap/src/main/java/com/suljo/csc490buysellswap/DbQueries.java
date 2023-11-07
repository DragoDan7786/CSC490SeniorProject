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

    /**
     * See DDL for definition of the stored procedure.
     * Briefly, sets isActive, isAvailable, and isVisible flags to false for all listings where
     * the sellerUserID == @userID, and sets the isActive flag = false in the [user] table where
     * userID == @userID.
     */
    public static String disableAccountQuery = """
            EXEC sprint04.spDisableAccount @userID = ?;
            """;

    public static String checkIfUsernameExistsQuery = """
            SELECT *
            FROM sprint04.[user]
            WHERE userID = ?
            """;
}
