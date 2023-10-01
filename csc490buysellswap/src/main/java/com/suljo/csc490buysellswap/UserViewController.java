package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import java.io.IOException;

/**
 * Controller for user-view.fxml
 */
public class UserViewController {
    @FXML
    private Tab adminTab;
    public void initialize(){
        //TODO complete this stub (there will probably be more to do here as the app develops)
        //If the currentUser isn't an admin, disable the admin tab.
        adminTab.setDisable(!BuySellSwapApp.getCurrentUser().isAdmin());
    }

    /**
     * Returns the user to the login page, effectively logging them out.
     * Also sets admin status to false.
     */
    @FXML
    private void menuItemLogoutOnAction(){
        //TODO this may need to be updated to do more, such as disconnect from the database
        BuySellSwapApp.setCurrentUser(null);
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
