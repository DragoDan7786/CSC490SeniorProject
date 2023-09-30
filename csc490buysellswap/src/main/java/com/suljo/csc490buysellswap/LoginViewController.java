package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * Controller for login-view.fxml
 */
public class LoginViewController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField pwField;
    @FXML
    private Label badCredentialsLabel;


    public void initialize(){
        badCredentialsLabel.setVisible(false);
    }

    /**
     * Checks the credentials entered by the user and switches the view accordingly.
     */
    @FXML
    private void loginButtonOnAction(){
        String username = usernameField.getText();
        String password = pwField.getText();
        pwField.clear();
        DbOperations.login(username, password);
        if (username.isEmpty() || password.isEmpty()){
            badCredentialsLabel.setText("Username and password are required.");
            badCredentialsLabel.setVisible(true);
        }
        else if ((username.equals("user") || (username.equals("admin"))) && password.equals("pass")){
            //TODO update this to actually query the DB and check user credentials
            //If the user logging in is flagged as having admin status, set the flag accordingly.
            if (username.equals("admin")){
                BuySellSwapApp.setAdmin(true);
            }
            //Switch to the user view.
            try {
                BuySellSwapApp.setRoot("user-view");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            badCredentialsLabel.setText("Username or password incorrect.");
            badCredentialsLabel.setVisible(true);
        }
    }

    /**
     * Switch to the registration view to create a new user.
     */
    @FXML
    private void buttonRegisterOnAction(){
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
}