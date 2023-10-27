package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class RegistrationViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField pwField;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField mNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField townField;
    @FXML
    private TextField zipField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField stateField;
    @FXML
    private DatePicker birthDatePicker;

    /*
    * Creates and registers a user
     */
    @FXML
    private void registerOnAction() {
        //Get the user input.
        String username = usernameField.getText();
        String password = pwField.getText();
        String fName = fNameField.getText();
        String mName = mNameField.getText();
        String lName = lNameField.getText();
        Date bDate = java.sql.Date.valueOf(birthDatePicker.getValue());
        String streetName = streetField.getText();
        String townName = townField.getText();
        String stateName = stateField.getText();
        String zipCode = zipField.getText();
        String phoneNumber = phoneField.getText();
        Boolean isAdmin = false;
        pwField.clear();
        //If something is missing, warn the user. Also warn user if password in "insecure"
        if (username.isEmpty()) {
            errorLabel.setText("Username is missing");
            errorLabel.setVisible(true);
        } else if (password.isEmpty()) {
            errorLabel.setText("Please enter a password");
            errorLabel.setVisible(true);
        } else if (!isValid(password)) {
            errorLabel.setText("Password needs to contain at 1 upper or lowercase letter, at least one number, and at least one special character.");
            errorLabel.setVisible(true);
        } else if (fName.isEmpty()) {
            errorLabel.setText("Please enter your first name");
            errorLabel.setVisible(true);
        } else if (mName.isEmpty()) {
            errorLabel.setText("Please enter your middle name");
            errorLabel.setVisible(true);
        } else if (lName.isEmpty()) {
            errorLabel.setText("Please enter your last name");
            errorLabel.setVisible(true);
        }
        //checks if date field is null
        else if (birthDatePicker.getValue() == null){
            errorLabel.setText("Please enter your date of birth");
            errorLabel.setVisible(true);
        }else if (streetName.isEmpty()) {
            errorLabel.setText("Street name must be present");
            errorLabel.setVisible(true);
        }else if (stateName.isEmpty()) {
            errorLabel.setText("State name must be present");
            errorLabel.setVisible(true);
        } else if (zipCode.isEmpty()) {
            errorLabel.setText("Zip code must be present");
            errorLabel.setVisible(true);
        } else if (phoneNumber.isEmpty()) {
            errorLabel.setText("Please enter your phone number");
            errorLabel.setVisible(true);
        } else {
            //Run the login operation. Returns true if user was found and set, in which case, switch the view.
            try {
                //If the username/password combo found, throw user found error.
                if (DbOperations.login(username, password)) {
                        errorLabel.setText("Can not create user, user already exists");
                        errorLabel.setVisible(true);
                }
                //If the database could not be connected to, a SQLException is thrown, which is caught here.
                //This may happen at initial app startup if the database is paused.
            } catch (SQLException e) {
                errorLabel.setText("Could not connect to DB.");
                errorLabel.setVisible(true);
                System.out.println(e);
            }
            //User is not duplicate so insert user into table.
            try {

                if (DbOperations.addNewUser(username,password,fName,mName,lName,bDate,streetName,townName,stateName,zipCode,phoneNumber,isAdmin) > 0){
                    errorLabel.setText("User Created");
                    errorLabel.setVisible(true);
                    //clears fields
                    clearFields();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



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

    //password verification method
    private boolean isValid(String password) {

        // for checking if password length
        // is above 8 and below 80
        if (!((password.length() >= 8)
                && (password.length() <= 80))) {
            return false;
        }

        // to check space
        if (password.contains(" ")) {
            return false;
        }

        int count = 0;

        // check digits from 0 to 9
        for (int i = 0; i <= 9; i++) {
            // to convert int to string
            String str1 = Integer.toString(i);

            if (password.contains(str1)) {
                count = 1;
                break;
            }
            }
            if (count == 0) {
                return false;
            }


        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }

        count = 0;

        // checking capital letters
        for (int i = 65; i <= 90; i++) {

            // type casting
            char c = (char) i;

            String str1 = Character.toString(c);
            if (password.contains(str1)) {
                count = 1;
                break;
            }
            }
            if (count == 0) {
                return false;
            }

            count = 0;

            // checking small letters
            for (int i = 97; i <= 122; i++) {

                // type casting
                char c = (char) i;
                String str1 = Character.toString(c);

                if (password.contains(str1)) {
                    count = 1;
                    break;
                }
            }
        return count != 0;
    }

    private void clearFields(){
        usernameField.clear();
        pwField.clear();
        fNameField.clear();
        mNameField.clear();
        lNameField.clear();
        birthDatePicker.cancelEdit();
        streetField.clear();
        townField.clear();
        stateField.clear();
        zipField.clear();
        phoneField.clear();
    }
}
