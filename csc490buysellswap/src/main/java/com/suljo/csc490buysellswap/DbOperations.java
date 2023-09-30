package com.suljo.csc490buysellswap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbOperations {
    /**
     * Helper method for other class methods that require a connection to the DB.
     * Modeled after the example here: <a href="https://learn.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver16">...</a>
     * @return the Connection.
     */
    private static Connection connectToDb(){
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
        try {
            return DriverManager.getConnection(connectionURL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void login(String userName, String pWord){
        //Get connection to database.
        Connection conn = connectToDb();
        if (conn != null){
            System.out.println("Connection acquired.");
        }


        //Query for user with matching credentials.

        //If a user is found, create User and set currentUser. Return true to caller to indicate success.

        //If nothing found, return false to caller to indicate failure.
    }


}
