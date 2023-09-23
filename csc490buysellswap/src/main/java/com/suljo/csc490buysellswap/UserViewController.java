package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller for user-view.fxml
 */
public class UserViewController {
    /**
     * Returns the user to the login page, effectively logging them out.
     */
    @FXML
    private void menuItemLogoutOnAction(){
        //TODO this may need to be updated to do more, such as disconnect from the database
        try {
            BuySellSwapApp.setRoot("login-view");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    private void menuItemExitOnAction() {
        System.exit(0);
    }
}
