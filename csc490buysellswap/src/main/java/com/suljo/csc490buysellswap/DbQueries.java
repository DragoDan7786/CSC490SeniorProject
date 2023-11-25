package com.suljo.csc490buysellswap;

public class DbQueries {
    public static String loginQuery = """
        SELECT *
        FROM sprint05.[user]
        WHERE userName = ? AND pWord = ?;
        """;

    public static String insertNewListingQuery = """
        INSERT INTO sprint05.listing
            (title, description, priceInCents, isForRent, rentalPeriodHours, sellerUserID, listingImage)
        VALUES
            (?,?,?,?,?,?,?)
        ;
        """;


    public static String insertNewUserQuery = """
        INSERT INTO sprint05.[user]
            (userName, pWord, firstName, middleName, lastName, dateOfBirth, street, city, state, zip, phoneNum, isAdmin)
        VALUES
            (?,?,?,?,?,?,?,?,?,?,?,?)
        ;
        """;

    public static String selectMyListingsQuery = """
        SELECT *
        FROM sprint05.listing
        WHERE sellerUserID = ?
        ORDER BY datetimeAdded
        ;
        """;
          
    public static String selectAllActiveListingsQuery = """
            SELECT *
            FROM sprint05.listing
            WHERE isActive  = 1
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
            EXEC sprint05.spDisableAccount @userID = ?;
            """;

    public static String checkIfUsernameExistsQuery = """
            SELECT *
            FROM sprint05.[user]
            WHERE userName = ?
            ;
            """;

    public static String updateListingQuery = """
            UPDATE sprint05.listing
            SET title = ?, description = ?, priceInCents = ?, isAvailable = ?, isForRent = ?, rentalPeriodHours = ?,
                listingImage = ?, soldAtPriceInCents = ?, isActive = ?, isVisible = ?, datetimeSold = ?
            WHERE listingID = ?
            ;
            """;

    public static String selectMessagesToUserQuery = """
            SELECT *
            FROM sprint05.message
            WHERE toUsername = ?
            ORDER BY datetimeSent DESC
            ;
            """;

    public static String selectMessagesFromUserQuery = """
            SELECT *
            FROM sprint05.message
            WHERE fromUsername = ?
            ORDER BY datetimeSent DESC
            ;
            """;

    public static String listingIDExistsQuery = """
            SELECT *
            FROM sprint05.listing
            WHERE listingID = ?
            ;
            """;

    public static String insertNewMessageQuery = """
            INSERT INTO sprint05.message
                (fromUsername, toUsername, aboutListingID, subject, contents, replyToMessageID)
            VALUES
                (?,?,?,?,?,?)
            ;
            """;

    public static String hideMessageQuery = """
            UPDATE sprint05.message
            SET hidden = 1
            WHERE messageID = ?
            ;
            """;

    public static String userIdToUsernameQuery = """
            SELECT userName
            FROM sprint05.[user]
            WHERE userID = ?
            ;
            """;
}
