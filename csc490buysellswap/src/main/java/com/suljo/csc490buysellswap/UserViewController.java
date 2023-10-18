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
    //***********Sell Tab Elements BEGIN**********//
    @FXML
    private Tab adminTab;
    //***********List An Item Elements BEGIN**********//
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
    //***********List An Item Elements END**********//
    //***********Sell Tab Elements END**********//

    //***********General User View Methods BEGIN**********//
    public void initialize() {
        //TODO complete this stub (there will probably be more to do here as the app develops)
        //If the currentUser isn't an admin, disable the admin tab.
        adminTab.setDisable(!BuySellSwapApp.getCurrentUser().isAdmin());
        initializeListAnItemTab();
    }

    /**
     * Returns the user to the login page, effectively logging them out.
     * Also sets admin status to false.
     */
    @FXML
    private void menuItemLogoutOnAction() {
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
    //***********General User View Methods END**********//

    //***********List An Item Methods BEGIN**********//
    /**
     * Helper method which sets up the "List An Item" tab in the Sell tab.
     */
    private void initializeListAnItemTab() {
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
    private void setForRentButton() {
        saleOrRentToggleGroup.selectToggle(forRentButton);
        listAnItemRentalPeriodTextField.setDisable(false);
        hoursButton.setDisable(false);
        daysButton.setDisable(false);
    }

    /**
     * Adjusts UI elements when For Sale radio button is selected.
     */
    @FXML
    private void setForSaleButton() {
        saleOrRentToggleGroup.selectToggle(forSaleButton);
        listAnItemRentalPeriodTextField.setDisable(true);
        hoursButton.setDisable(true);
        daysButton.setDisable(true);
    }

    /**
     * Creates a file chooser, allowing the user to select an image which should be uploaded as part of their listing.
     */
    @FXML
    private void browseForListingPhoto() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter pngExtFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        fc.getExtensionFilters().add(pngExtFilter);
        listAnItemImage = fc.showOpenDialog(null);
        if (listAnItemImage != null) {
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

    /**
     * Clears the file selected by the user for upload with a listing (e.g., in case the user changes their mind about
     * adding an image).
     */
    @FXML
    private void listAnItemClearImageFile() {
        listAnItemImage = null;
        listAnItemFilePathTextField.clear();
        listAnItemFileOpenText.setVisible(false);
    }

    /**
     * Resets the tab, e.g., when the user decides not to add an item after all and wants to start over.
     */
    @FXML
    private void listAnItemResetTab(){
        listAnItemClearImageFile();
        listAnItemTitle.clear();
        listAnItemDescription.clear();
        listAnItemPriceTextField.setText("00.00");
        listAnItemRentalPeriodTextField.setText("00");
        listAnItemSuccess.setVisible(false);
        initializeListAnItemTab();
    }

    /**
     * Adds a listing to the database. Functionality is located in List An Item tab, nested within the Sell tab.
     */
    @FXML
    private void addListing() {
        //Get the values associated with the item.
        String itemName = listAnItemTitle.getText();
        String itemDesc = listAnItemDescription.getText();
        //The user may have entered something like 5.50, and it will come in as a string, so we need to convert it first
        //to a double, and then to an int representing cents.
        double price = Double.parseDouble(listAnItemPriceTextField.getText());
        int priceInCents = (int) (price * 100);
        boolean isForRent = forRentButton.isSelected();
        //-1 is a sentinel value / placeholder for the rental period when the item is not for rent.
        //This is used as the default value, as when it was placed as another conditional the compiler complained to me
        //that the variable may not have been initialized.
        int rentalPeriodHours = -1;
        if (isForRent && hoursButton.isSelected()) {
            rentalPeriodHours = Integer.parseInt(listAnItemRentalPeriodTextField.getText());
        } else if (isForRent && daysButton.isSelected()) {
            rentalPeriodHours = Integer.parseInt(listAnItemRentalPeriodTextField.getText()) * 24;
        }
        //Actually try to insert the item.
        try {
            //If the rows affected is >0, the item was successfully inserted.
            if (DbOperations.addNewListing(itemName, itemDesc, priceInCents, isForRent, rentalPeriodHours, listAnItemImage) > 0) {
                listAnItemResetTab();
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
    }
    //***********List An Item Methods END**********//
}
