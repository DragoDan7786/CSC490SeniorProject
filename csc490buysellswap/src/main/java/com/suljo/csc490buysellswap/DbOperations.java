package com.suljo.csc490buysellswap;

import java.sql.*;

public class DbOperations {
    /**
     * Helper method for other class methods that require a connection to the DB.
     * Modeled after the example here: <a href="https://learn.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver16">...</a>
     * @return the Connection.
     */
    private static Connection connectToDb() throws SQLException {
        /*
        String connectionURL = "jdbc:sqlserver://"
                + DbParameters.DB_ENDPOINT + ":"
                + DbParameters.DB_PORT + ";"
                + "database=" + DbParameters.DB_NAME + ";"
                + "user=" + DbParameters.USER_NAME + ";"
                + "password=" + DbParameters.PASSWORD + ";"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";
            //Establish the connection and return it if successful. If not, return null.
            return DriverManager.getConnection(connectionURL);
            */
         return DriverManager.getConnection(BuySellSwapApp.getDbProperties().getProperty("connectionStringPabloContained"));

    }

    /**
     * Searches the DB for a user with a given set of credentials. If found, creates and sets the
     * currentUser and returns true. If no match is found, returns false.
     * @param userName
     * @param pWord
     * @return
     */
    public static boolean login(String userName, String pWord){
        //Get connection to database.
        Connection conn = null;
        boolean userExists = false;
        try {
            //Connect to the DB.
            conn = connectToDb();
            //Query for a user with matching credentials.
            PreparedStatement prepStmt = conn.prepareStatement(DbQueries.loginQuery);
            prepStmt.setString(1, userName);
            prepStmt.setString(2, pWord);
            ResultSet result = prepStmt.executeQuery();
            //Iterate through the results.
            //If a matching user is found, create and set the currentUser accordingly.
            while (result.next()){
                String rowUserName = result.getString("userName");
                String rowPassWord = result.getString("pWord");
                if (userName.equals(rowUserName) && pWord.equals(rowPassWord)){
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
                    String hash = result.getString("hash");
                    boolean isAdmin = result.getBoolean("isAdmin");
                    BuySellSwapApp.setCurrentUser(new User(userID, userName, pWord, firstName, middleName, lastName,
                            dateOfBirth, street, city, state, zip, phoneNum, hash, isAdmin));
                    return true;
                }
                conn.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}
