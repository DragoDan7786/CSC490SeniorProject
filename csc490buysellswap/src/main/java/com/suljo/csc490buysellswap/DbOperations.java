package com.suljo.csc490buysellswap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * @param userName the userName
     * @param pWord the password
     * @return 0 if user found and active, -1 if username/password combination not matched, -2 if matched but user not active
     */
    public static int login(String userName, String pWord) throws SQLException {
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
                //If the username/password combo is found, check if the user is active.
                boolean isActive = result.getBoolean("isActive");
                //If user is inactive, return -2.
                if (!isActive){
                    return -2;
                }
                //If user is active, return 0.
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
                String registrationDatetime = result.getString("registrationDatetime");
                BuySellSwapApp.setCurrentUser(new User(userID, userName, pWord, firstName, middleName, lastName,
                        dateOfBirth, street, city, state, zip, phoneNum, isAdmin, isActive, registrationDatetime));
                return 0;
            }
            conn.close();
        }
        //If no user with this username/password combo is found, return -1.
        return -1;
    }


    //If converted to a stored procedure instead of a straight insert query, could recover new item ID and return it to the user.
    //Might not be strictly necessary if there is additional functionality to view all of your own items.
    /**
     * Inserts a new listing into the database.
     * @param itemName The itemName.
     * @param itemDesc The itemDesc.
     * @param priceInCents The price of the item, in cents.
     * @param isForRent If the item is for rent (vs. for sale).
     * @param rentalPeriodHours The rental period in hours, if the item is for rent.
     * @param image The image which accompanies the listing, if any, in PNG format.
     * @return The number of rows affected (should be 1 for a successful addition, 0 for a failure).
     * @throws SQLException Throws a SQLException if there is a database error.
     */
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
     * @param userName the userName
     * @param pWord the password
     * @return The number of rows affected, >=1 if new user creation was successful, otherwise 0.
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
     * @throws SQLException SQLException if there is a database error.
     */
    public static ArrayList<Listing> selectMyListings() throws SQLException {
        ArrayList<Listing> listings = new ArrayList<>();
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.selectMyListingsQuery);
        prepStmt.setInt(1, BuySellSwapApp.getCurrentUser().getUserID());
        ResultSet result = prepStmt.executeQuery();
        while (result.next()){
            listings.add(makeListingFromResult(result));
        }
        conn.close();
        return listings;
    }

    public static ObservableList<Listing> selectAllActiveListings() throws SQLException {
        ObservableList<Listing> activeListings = FXCollections.observableArrayList();
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.selectAllActiveListingsQuery);
        ResultSet result = prepStmt.executeQuery();
        while (result.next()) {
            activeListings.add(makeListingFromResult(result));
        }
        conn.close();
        return activeListings;
    }

    /**
     * Generates and returns a Listing from the current row in a ResultSet.
     * @param resultSet The ResultSet from a SQL query that returns rows from the Listing table.
     * @return A Listing object corresponding to the current row in the ResultSet.
     * @throws SQLException SQLException if there is a database error.
     */
    private static Listing makeListingFromResult(ResultSet resultSet) throws SQLException {
        int listingID = resultSet.getInt("listingID");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        int priceInCents = resultSet.getInt("priceInCents");
        boolean isAvailable = resultSet.getBoolean("isAvailable");
        boolean isForRent = resultSet.getBoolean("isForRent");
        int rentalPeriodHours = resultSet.getInt("rentalPeriodHours");
        Blob imageBlob = resultSet.getBlob("listingImage"); //might be null!
        int sellerUserID = resultSet.getInt("sellerUserID");
        String datetimeAdded = resultSet.getString("datetimeAdded");
        String datetimeModified = resultSet.getString("datetimeModified");
        int soldAtPriceInCents = resultSet.getInt("soldAtPriceInCents");
        boolean isActive = resultSet.getBoolean("isActive");
        boolean isVisible = resultSet.getBoolean("isVisible");
        LocalDate dateSold = DateTimeUtil.mssqlDatetime2StringToLocalDate(resultSet.getString("datetimeSold"));
        return new Listing(listingID, title, description, priceInCents, isAvailable, isForRent,
                rentalPeriodHours, imageBlob, sellerUserID, datetimeAdded, datetimeModified, soldAtPriceInCents,
                isActive, isVisible, dateSold);
    }

    /**
     * Disables the account of the given user and all their listings by setting flags appropriately.
     * @param userID the ID of the user whose account is being disabled.
     */
    public static void disableUserAccount(int userID) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.disableAccountQuery);
        prepStmt.setInt(1, userID);
        prepStmt.execute();
        conn.close();
    }

    /**
     * Determines if a username is already in use by querying the user table for users with that username.
     * @param username The username to be checked.
     * @return True if username exists, false otherwise.
     * @throws SQLException SQLException if there is a database error.
     */
    public static boolean usernameExists(String username) throws SQLException {
        boolean userExists;
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.checkIfUsernameExistsQuery);
        prepStmt.setString(1, username);
        ResultSet result = prepStmt.executeQuery();
        userExists = result.next();
        conn.close();
        return userExists;
    }

    /**
     * Determins if a listingID exists and is thus a valid listingID.
     * @param listingID The ID to be checked.
     * @return true if the listingID exists, false otherwise.
     * @throws SQLException SQLException if there is a database error.
     */
    public static boolean listingIDExists(int listingID) throws SQLException {
        boolean listingIDExists;
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.listingIDExistsQuery);
        prepStmt.setInt(1, listingID);
        ResultSet result = prepStmt.executeQuery();
        listingIDExists = result.next();
        conn.close();
        return listingIDExists;
    }

    /**
     * Updates the database table row which corresponds to a Listing object.
     * @param listing the updated listing
     * @param imageFile the File containing the new image, or null
     * @return true of database updated
     * @throws SQLException SQLException if there is a database error.
     * @throws FileNotFoundException FileNotFoundException if the image file cannot be opened.
     */
    public static boolean updateListing(Listing listing, File imageFile) throws SQLException, FileNotFoundException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.updateListingQuery);
        prepStmt.setString(1, listing.getTitle());
        prepStmt.setString(2, listing.getDescription());
        prepStmt.setInt(3, listing.getPriceInCents());
        prepStmt.setBoolean(4, listing.isAvailable());
        prepStmt.setBoolean(5, listing.isForRent());
        prepStmt.setInt(6, listing.getRentalPeriodHours());
        if (imageFile != null){
            prepStmt.setBinaryStream(7, new FileInputStream(imageFile));
        } else {
            prepStmt.setBinaryStream(7, null);
        }
        prepStmt.setInt(8, listing.getSoldAtPriceInCents());
        prepStmt.setBoolean(9, listing.isActive());
        prepStmt.setBoolean(10, listing.isVisible());
        if (listing.getDateSold() != null){
            prepStmt.setString(11, listing.getDateSold().toString());
        } else {
            prepStmt.setString(11, null);
        }
        prepStmt.setInt(12, listing.getListingID());
        int rowCount = prepStmt.executeUpdate();
        conn.close();
        if (rowCount == 1){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a list of messages sent *to* a given user.
     * @param username The username of the user.
     * @return A list of messages sent to the user.
     * @throws SQLException if there is a database error.
     */
    public static ArrayList<Message> getMessagesToUser(String username) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.selectMessagesToUserQuery);
        return getMessages(username, conn, prepStmt);
    }

    /**
     * Returns a list of messages sent *from* (sent by) a given user.
     * @param username The username of the user.
     * @return A list of messages sent by the user.
     * @throws SQLException if there is a database error.
     */
    public static ArrayList<Message> getMessagesFromUser(String username) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.selectMessagesFromUserQuery);
        return getMessages(username, conn, prepStmt);
    }

    /**
     * Helper method for getting messages to/from a given user. Actually gets the messages.
     * @param username The username of the user.
     * @param conn A connection to the database.
     * @param prepStmt The PreparedStatement with the query to find messages either sent by or to the user
     * @return A list of the messages.
     * @throws SQLException if there is a database error.
     */
    private static ArrayList<Message> getMessages(String username, Connection conn, PreparedStatement prepStmt) throws SQLException {
        prepStmt.setString(1, username);
        ResultSet result = prepStmt.executeQuery();
        ArrayList<Message> messages = new ArrayList<>();
        while (result.next()){
            messages.add(makeMessageFromResult(result));
        }
        conn.close();
        return messages;
    }

    /**
     * Generates a Message object from a row in a ResultSet of message table rows.
     * @param result The ResultSet
     * @return The Message object representation of the current ResultSet row.
     * @throws SQLException if there is a database error.
     */
    private static Message makeMessageFromResult(ResultSet result) throws SQLException {
        Integer messageID = result.getInt("messageID");
        String fromUsername = result.getString("fromUsername");
        String toUsername = result.getString("toUsername");
        LocalDateTime datetimeSent = DateTimeUtil.mssqlDatetime2StringToLocalDateTime(result.getString("datetimeSent"));
        Integer aboutListingID = result.getInt("aboutListingID");
        String subject = result.getString("subject");
        String contents = result.getString("contents");
        Integer replyToMessageID = result.getInt("replyToMessageID");
        boolean hidden = result.getBoolean("hidden");
        return new Message(messageID, fromUsername, toUsername, datetimeSent, aboutListingID, subject, contents, replyToMessageID, hidden);
    }

    /**
     * Inserts a new message into the message table.
     * @param fromUsername the username of the sending user.
     * @param toUsername the username of the receiving user.
     * @param aboutListingID the listing that this message concerns, may be null.
     * @param subject the subject line of the message.
     * @param contents the contents of the message.
     * @param replyToMessageID if this message is a reply to another message, the ID of that message
     * @return true if the message was successfully inserted into the database
     * @throws SQLException if there is a database error.
     */
    public static boolean insertNewMessage(String fromUsername, String toUsername, Integer aboutListingID,
                                           String subject, String contents, Integer replyToMessageID) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.insertNewMessageQuery);
        prepStmt.setString(1, fromUsername);
        prepStmt.setString(2, toUsername);
        if (aboutListingID != null){
            prepStmt.setInt(3, aboutListingID);
        } else {
            prepStmt.setNull(3, Types.INTEGER);
        }
        if (subject == null || subject.isEmpty()){
            prepStmt.setString(4, "[no subject]");
        } else {
            prepStmt.setString(4, subject);
        }
        prepStmt.setString(5, contents);
        if (replyToMessageID != null){
            prepStmt.setInt(6, replyToMessageID);
        } else {
            prepStmt.setNull(6, Types.INTEGER);
        }
        int rowCount = prepStmt.executeUpdate();
        conn.close();
        return rowCount != 0; //if 0 rows were affected, the message was not inserted
    }

    /**
     * Soft deletes a message by setting the hidden value.
     * @param messageID The ID of the message.
     * @return true if the update was successful.
     * @throws SQLException if there is a database error.
     */
    public static boolean hideMessage(int messageID) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.hideMessageQuery);
        prepStmt.setInt(1, messageID);
        int rowCount = prepStmt.executeUpdate();
        conn.close();
        return rowCount != 0; //if 0 rows were affected, nothing was deleted
    }

    /**
     * Gets the username corresponding to a userID.
     * @param userID The userID.
     * @return The username of the user.
     * @throws SQLException if there is a database error.
     */
    public static String userIDToUsername(int userID) throws SQLException {
        String username = null;
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.userIdToUsernameQuery);
        prepStmt.setInt(1, userID);
        ResultSet result = prepStmt.executeQuery();
        if (result.next()){
            username = result.getString("userName");
        }
        conn.close();
        return username;
    }

    /**
     * Sets a listing to not available.
     * @param listingID The listingID of the listing.
     * @return True if the listing was successfully updated.
     * @throws SQLException If there is a database error.
     */
    public static boolean makeListingUnavailable(int listingID) throws SQLException {
        Connection conn = connectToDb();
        PreparedStatement prepStmt = conn.prepareStatement(DbQueries.makeListingUnavailableQuery);
        prepStmt.setInt(1, listingID);
        int rowCount = prepStmt.executeUpdate();
        conn.close();
        return rowCount != 0;
    }
}
