package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for login-view.fxml
 */
public class LoginViewController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField pwField;
    @FXML
    private Label loginErrorLabel;

    public void initialize() {
        loginErrorLabel.setVisible(false);
    }

    /**
     * Checks the credentials entered by the user and switches the view accordingly.
     */
    @FXML
    private void loginButtonOnAction() {
        //Get the user input.
        String username = usernameField.getText();
        String password = pwField.getText();
        pwField.clear();
        //If something not entered, warn the user.
        if (username.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setText("Username and password are required.");
            loginErrorLabel.setVisible(true);
        } else {
            //Run the login operation. Returns true if user was found and set, in which case, switch the view.
            try {
                //If the username/password combo found, currentUser is initialized and the view is switched.
                if (DbOperations.login(username, password)) {
                    try {
                        BuySellSwapApp.setRoot("user-view");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                //If the username/password combo is not found, an error message is displayed.
                else {
                    loginErrorLabel.setText("Username or password incorrect.");
                    loginErrorLabel.setVisible(true);
                }
                //If the database could not be connected to, a SQLException is thrown, which is caught here.
                //This may happen at initial app startup if the database is paused.
            } catch (SQLException e) {
                loginErrorLabel.setText("Could not connect to DB.");
                loginErrorLabel.setVisible(true);
                System.out.println(e);
            }
        }
    }

    /**
     * Switch to the registration view to create a new user.
     */
    @FXML
    private void buttonRegisterOnAction() {
        try {
            BuySellSwapApp.setRoot("registration-view");
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

    /**
     * Development tool - skip to user view without logging in, in order to avoid accessing the DB.
     */
    @FXML
    private void skipToUserView() {
        try {
            BuySellSwapApp.setCurrentUser(new User(1, "user", "pass", "Firstname",
                    "", "Lastname", "XX-XX-XXXX", "123 Street St", "City",
                    "State", "00000", "555-555-5555", "Hash1234!", false));
            BuySellSwapApp.setRoot("user-view");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}