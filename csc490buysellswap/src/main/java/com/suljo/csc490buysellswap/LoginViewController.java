package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

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
        if (username.isEmpty() || password.isEmpty()){
            badCredentialsLabel.setText("Username and password are required.");
            badCredentialsLabel.setVisible(true);
        }
        else if (username.equals("user") && password.equals("pass")){
            //TODO update this to actually query the DB and check user credentials
            try {
                BuySellSwapApp.setRoot("user-view");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (username.equals("admin") && password.equals("pass")){
            //TODO add admin view and handle admin login
            System.out.println("Show admin view if credentials match those of an admin.");
        }
        else {
            badCredentialsLabel.setText("Username or password incorrect.");
            badCredentialsLabel.setVisible(true);
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