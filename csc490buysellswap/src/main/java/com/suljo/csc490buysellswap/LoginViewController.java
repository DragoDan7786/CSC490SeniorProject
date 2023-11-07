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
        //Otherwise, search for a user with matching credentials.
        } else {
            try {
                int loginResult = DbOperations.login(username, password);
                switch (loginResult) {
                    case 0: //If the username/password combo found, currentUser is initialized and the view is switched.
                        try { BuySellSwapApp.setRoot("user-view"); } catch (IOException e) { throw new RuntimeException(e); }
                        break;
                    case -1:
                        generateInformationDialog("Could Not Login", "Username or password incorrect.",
                                "Please try again.").show();
                        break;
                    case -2:
                        generateInformationDialog("Could Not Login", "Account is disabled.",
                                "Please register for a new account or contact an administrator.").show();
                        break;
                }
            //If the database could not be connected to, a SQLException is thrown, which is caught here.
            //This may happen at initial app startup if the database is paused.
            } catch (SQLException e) {
                generateInformationDialog("Database Error", "Could not connect to the database.",
                        "The database may not be active. Please try again in a few minutes.").show();
                e.printStackTrace();
            }
        }
    }

    private Alert generateInformationDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
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
                    "State", "00000", "555-555-5555", false, true,
                    "2023-10-31"));
            BuySellSwapApp.setRoot("user-view");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}