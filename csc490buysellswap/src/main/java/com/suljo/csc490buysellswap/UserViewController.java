package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for user-view.fxml
 */
public class UserViewController {
    @FXML
    private Tab adminTab;
    @FXML
    private ToggleGroup saleOrRentToggleGroup;
    @FXML
    private RadioButton forSaleButton;
    @FXML
    private RadioButton forRentButton;
    @FXML
    private ToggleGroup rentalPeriodToggleGroup;
    @FXML
    private RadioButton hoursButton;
    @FXML
    private RadioButton daysButton;
    @FXML
    private TextField listAnItemTitle;
    @FXML
    private TextArea listAnItemDescription;
    @FXML
    private TextField listAnItemFilePathTextField;
    @FXML
    private Label listAnItemFileOpenText;
    @FXML
    private TextField listAnItemPriceTextField;
    @FXML
    private TextField listAnItemRentalPeriodTextField;
    @FXML
    private Label listAnItemSuccess;

    private File listAnItemImage;

    public void initialize(){
        //TODO complete this stub (there will probably be more to do here as the app develops)
        //If the currentUser isn't an admin, disable the admin tab.
        adminTab.setDisable(!BuySellSwapApp.getCurrentUser().isAdmin());
        initializeListAnItemTab();
    }

    /**
     * Helper method which sets up the "List An Item" tab in the Sell tab.
     */
    private void initializeListAnItemTab(){
        setForSaleButton();
        rentalPeriodToggleGroup.selectToggle(daysButton);
        listAnItemFileOpenText.setVisible(false);
        listAnItemFilePathTextField.setDisable(true);
        listAnItemSuccess.setVisible(false);
    }

    /**
     * Adjusts UI elements when For Rent radio button is selected.
     */
    @FXML
    private void setForRentButton(){
        saleOrRentToggleGroup.selectToggle(forRentButton);
        listAnItemRentalPeriodTextField.setDisable(false);
        hoursButton.setDisable(false);
        daysButton.setDisable(false);
    }

    /**
     * Adjusts UI elements when For Sale radio button is selected.
     */
    @FXML
    private void setForSaleButton(){
        saleOrRentToggleGroup.selectToggle(forSaleButton);
        listAnItemRentalPeriodTextField.setDisable(true);
        hoursButton.setDisable(true);
        daysButton.setDisable(true);
    }

    /**
     * Creates a file chooser, allowing the user to select an image which should be uploaded as part of their listing.
     */
    @FXML
    private void browseForListingPhoto(){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter pngExtFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        fc.getExtensionFilters().add(pngExtFilter);
        listAnItemImage = fc.showOpenDialog(null);
        if (listAnItemImage != null){
            try {
                listAnItemFilePathTextField.setText(listAnItemImage.getCanonicalPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listAnItemFileOpenText.setText("File opened.");
            listAnItemFileOpenText.setTextFill(Color.BLACK);
            listAnItemFileOpenText.setVisible(true);
        } else {
            listAnItemFileOpenText.setText("File could not be opened.");
            listAnItemFileOpenText.setTextFill(Color.RED);
            listAnItemFileOpenText.setVisible(true);
        }
    }

    @FXML
    private void addListing(){
        String itemName = listAnItemTitle.getText();
        String itemDesc = listAnItemDescription.getText();
        double price = Double.parseDouble(listAnItemPriceTextField.getText());
        int priceInCents = (int)price*100;
        boolean isForRent = forRentButton.isSelected();
        int rentalPeriodHours = -1;
        if (isForRent && hoursButton.isSelected()) {
            rentalPeriodHours = Integer.parseInt(listAnItemRentalPeriodTextField.getText());
        } else if (isForRent && daysButton.isSelected()){
            rentalPeriodHours = Integer.parseInt(listAnItemRentalPeriodTextField.getText()) * 24;
        }

        try {
            if (DbOperations.addNewListing(itemName, itemDesc, priceInCents, isForRent, rentalPeriodHours, listAnItemImage) > 0){
                listAnItemSuccess.setText("Item successfully listed.");
                listAnItemSuccess.setTextFill(Color.BLACK);
                listAnItemSuccess.setVisible(true);
            } else {
                listAnItemSuccess.setText("Item could not be listed.");
                listAnItemSuccess.setTextFill(Color.RED);
                listAnItemSuccess.setVisible(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("addListing activated");
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
