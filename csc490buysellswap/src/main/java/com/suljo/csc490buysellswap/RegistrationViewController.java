package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;

import java.io.IOException;

public class RegistrationViewController {

    /**
     * Return to the login view from the registration view without submitting registration data.
     */
    @FXML
    private void cancelButtonOnAction(){
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
