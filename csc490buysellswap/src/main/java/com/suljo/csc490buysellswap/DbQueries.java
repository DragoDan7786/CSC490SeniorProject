package com.suljo.csc490buysellswap;

public class DbQueries {
    public static String loginQuery = """
               SELECT *
               FROM sprint02.[user]
               WHERE userName = ? AND pWord = ?;
               """;

    public static String insertNewListingQuery = """
            INSERT INTO sprint02.saleable_item
                (name, description, priceInCents, isForRent, rentalPeriodHours, sellerUserID, itemImage)
            VALUES
                (?,?,?,?,?,?,?)
            ;
            """;

    public static String registrationQuery = """
            INSERT INTO sprint03.user
                (userName, pWord, firstName, lastName, dateOfBirth, street, city, state, zip, phoneNum)
            VALUES
                (?,?,?,?,?,?,?,?,?,?)
            ;
            """;
}
