package com.suljo.csc490buysellswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class DbOperations {
    /**
     * Helper method for other class methods that require a connection to the DB.
     * Modeled after the example here: <a href="https://learn.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver16">...</a>
     *
     * @return the Connection.
     */
    private static Connection connectToDb() throws SQLException {
        return DriverManager.getConnection(BuySellSwapApp.getDbProperties().getProperty("connectionStringPablo"));
    }

    /**
     * Searches the DB for a user with a given set of credentials. If found, creates and sets the
     * currentUser and returns true. If no match is found, returns false. Throws SQLException if
     * unable to connect to the database.
     *
     * @param userName
     * @param pWord
     * @return
     */
    public static boolean login(String userName, String pWord) throws SQLException {
        //Get connection to database.
        Connection conn = connectToDb();
        //Query for a user with matching credentials.
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.loginQuery);
        prepStmt.setString(1, userName);
        prepStmt.setString(2, pWord);
        ResultSet result = prepStmt.executeQuery();
        //Iterate through the results.
        //If a matching user is found, create and set the currentUser accordingly.
        while (result.next()) {
            String rowUserName = result.getString("userName");
            String rowPassWord = result.getString("pWord");
            if (userName.equals(rowUserName) && pWord.equals(rowPassWord)) {
                int userID = result.getInt("userID");
                String firstName = result.getString("firstName");
                String middleName = result.getString("middleName");
                String lastName = result.getString("lastName");
                String dateOfBirth = result.getString("dateOfBirth");
                String street = result.getString("street");
                String city = result.getString("city");
                String state = result.getString("state");
                String zip = result.getString("zip");
                String phoneNum = result.getString("phoneNum");
                boolean isAdmin = result.getBoolean("isAdmin");
                BuySellSwapApp.setCurrentUser(new User(userID, userName, pWord, firstName, middleName, lastName,
                        dateOfBirth, street, city, state, zip, phoneNum, isAdmin));
                return true;
            }
            conn.close();
        }
        return false;
    }

    //If converted to a stored procedure instead of a straight insert query, could recover new item ID and return it to the user.
    //Might not be strictly necessary if there is additional functionality to view all of your own items.
    public static int addNewListing(String itemName, String itemDesc, int priceInCents, boolean isForRent, int rentalPeriodHours, File image) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.insertNewListingQuery);
        prepStmt.setString(1, itemName);
        prepStmt.setString(2, itemDesc);
        prepStmt.setInt(3, priceInCents);
        prepStmt.setBoolean(4, isForRent);
        prepStmt.setInt(5, rentalPeriodHours);
        prepStmt.setInt(6, BuySellSwapApp.getCurrentUser().getUserID());
        if (image != null) {
            try {
                prepStmt.setBinaryStream(7, new FileInputStream(image));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            prepStmt.setBinaryStream(7, null);
        }
        int rowsAffected = prepStmt.executeUpdate();
        conn.close();
        return rowsAffected;
    }

    /**
     * Attempts to create new user in database. Takes parameters from Registration form and adds new user to database.
     * Returns number of rows affected. Successful if ros affected >=1.
     * Does not allow for creation of admin through GUI.
     * @param userName
     * @param pWord
     * @return
     */
    public static int addNewUser(String userName,String  pWord,String  firstName,String  middleName,String  lastName,Date  dateOfBirth,String  street,String  city,String  state,String  zip,String  phoneNum, Boolean isAdmin) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.insertNewUserQuery);
        prepStmt.setString(1, userName);
        prepStmt.setString(2, pWord);
        prepStmt.setString(3, firstName);
        prepStmt.setString(4, middleName);
        prepStmt.setString(5, lastName);
        prepStmt.setDate(6, dateOfBirth);
        prepStmt.setString(7, street);
        prepStmt.setString(8, city);
        prepStmt.setString(9, state);
        prepStmt.setString(10, zip);
        prepStmt.setString(11, phoneNum);
        prepStmt.setBoolean(12, false);
        int rowsAffected = prepStmt.executeUpdate();
        conn.close();
        return rowsAffected;
    }

    /**
     * Queries the database for all listings belonging to the current user.
     * @return An ArrayList of the Listings.
     * @throws SQLException
     */
    public static ArrayList<Listing> selectMyListings() throws SQLException {
        ArrayList<Listing> listings = new ArrayList<>();
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.selectMyListingsQuery);
        prepStmt.setInt(1, BuySellSwapApp.getCurrentUser().getUserID());
        ResultSet result = prepStmt.executeQuery();
        while (result.next()){
            //Get the values for the current item.
            int listingID = result.getInt("listingID");
            String title = result.getString("title");
            String description = result.getString("description");
            int priceInCents = result.getInt("priceInCents");
            boolean isAvailable = result.getBoolean("isAvailable");
            boolean isForRent = result.getBoolean("isForRent");
            int rentalPeriodHours = result.getInt("rentalPeriodHours");
            Blob image = result.getBlob("listingImage"); //might be null!
            int sellerUserID = result.getInt("sellerUserID");
            String datetimeAdded = result.getString("datetimeAdded");
            String datetimeModified = result.getString("datetimeModified");
            //Add the listing to the ArrayList.
            listings.add(new Listing(listingID, title, description, priceInCents, isAvailable, isForRent,
                    rentalPeriodHours, image, sellerUserID, datetimeAdded, datetimeModified));
        }
        conn.close();
        return listings;
    }
}
