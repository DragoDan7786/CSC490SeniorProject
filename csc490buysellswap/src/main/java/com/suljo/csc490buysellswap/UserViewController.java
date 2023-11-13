package com.suljo.csc490buysellswap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

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
    @FXML
    private ImageView listAnItemImagePreview;
    @FXML
    private Label listAnItemNoImageLabel;
    @FXML
    private Rectangle listAnItemImagePreviewFrame;
    private File listAnItemImage;
    //***********List An Item Elements END**********//
    //***********My Listings Elements BEGIN**********//
    @FXML
    private TableView myListingsTableView;
    @FXML
    private TableColumn<Listing, Integer> myListingsTableColumnId;
    @FXML
    private TableColumn<Listing, String> myListingsTableColumnTitle;
    @FXML
    private TableColumn<Listing, String> myListingsTableColumnListedDatetime;
    @FXML
    private TextField myListingsDetailViewTitle;
    @FXML
    private TextField myListingsDetailViewAvailable;
    @FXML
    private TextField myListingsDetailViewListingID;
    @FXML
    private TextArea myListingsDetailViewDescription;
    @FXML
    private TextField myListingsDetailViewPrice;
    @FXML
    private TextField myListingsDetailViewAdded;
    @FXML
    private TextField myListingsDetailViewModified;
    @FXML
    private ImageView myListingsDetailImageView;
    @FXML
    private TextField searchTxtField;
    @FXML
    private HBox searchHbox;
    //***********My Listings Elements END**********//
    //***********Account Management Elements BEGIN**********//

    //***********Account Management Elements END**********//
    //***********Sell Tab Elements END**********//

    //***********General User View Methods BEGIN**********//
    public void initialize() {
        //If the currentUser isn't an admin, disable the admin tab.
        adminTab.setDisable(!BuySellSwapApp.getCurrentUser().isAdmin());
        initializeListAnItemTab();
        initializeMyListingsTab();
        HBox.setHgrow(searchTxtField, Priority.ALWAYS);
    }

    /**
     * Returns the user to the login page, effectively logging them out.
     * Also sets admin status to false.
     */
    @FXML
    private void menuItemLogoutOnAction() {
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
                //Display the image path.
                listAnItemFilePathTextField.setText(listAnItemImage.getCanonicalPath());
                //Display an image preview:
                //Get a file input stream.
                FileInputStream imageInputStream = new FileInputStream( listAnItemImage);
                //Create an image from the stream.
                Image inputImage = new Image((imageInputStream));
                //Set the ImageView.
                listAnItemImagePreview.setImage(inputImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listAnItemFileOpenText.setText("File opened.");
            listAnItemFileOpenText.setTextFill(Color.BLACK);
            listAnItemFileOpenText.setVisible(true);
            listAnItemImagePreview.setVisible(true);
            listAnItemNoImageLabel.setVisible(false);
            listAnItemImagePreviewFrame.setVisible(false);
        } else {
            listAnItemFileOpenText.setText("File could not be opened.");
            listAnItemFileOpenText.setTextFill(Color.RED);
            listAnItemFileOpenText.setVisible(true);
            listAnItemImagePreview.setVisible(false);
            listAnItemNoImageLabel.setVisible(true);
            listAnItemImagePreviewFrame.setVisible(true);
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
        listAnItemNoImageLabel.setVisible(true);
        listAnItemImagePreview.setVisible(false);
        listAnItemImagePreviewFrame.setVisible(true);
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
    //***********My Listings Methods BEGIN**********//
    private void initializeMyListingsTab(){
        //Set up the TableView.
        myListingsTableColumnId.setCellValueFactory(new PropertyValueFactory<Listing, Integer>("listingID"));
        myListingsTableColumnTitle.setCellValueFactory(new PropertyValueFactory<Listing, String>("title"));
        myListingsTableColumnListedDatetime.setCellValueFactory(new PropertyValueFactory<Listing, String>("datetimeAdded"));
        myListingsPopulateTableView();
        myListingsDisableDetailedView();
        myListingsDetailImageView.setVisible(false);
    }

    /**
     * Disable the fields in the "detailed view"
     */
    private void myListingsEnableDetailedView(){
        myListingsDetailViewTitle.setEditable(true);
        myListingsDetailViewAvailable.setEditable(true);
        myListingsDetailViewListingID.setEditable(true);
        myListingsDetailViewDescription.setEditable(true);
        myListingsDetailViewPrice.setEditable(true);
        myListingsDetailViewAdded.setEditable(true);
        myListingsDetailViewModified.setEditable(true);
    }

    /**
     * Enable the fields in the "detailed view".
     */
    private void myListingsDisableDetailedView(){
        myListingsDetailViewTitle.setEditable(false);
        myListingsDetailViewAvailable.setEditable(false);
        myListingsDetailViewListingID.setEditable(false);
        myListingsDetailViewDescription.setEditable(false);
        myListingsDetailViewPrice.setEditable(false);
        myListingsDetailViewAdded.setEditable(false);
        myListingsDetailViewModified.setEditable(false);
    }
    /**
     * Populates the TableView with the current user's listings.
     */
    @FXML
    private void myListingsPopulateTableView(){
        myListingsTableView.getItems().clear();
        try {
            for (Listing listing: DbOperations.selectMyListings()){
                myListingsTableView.getItems().add(listing);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the fields that constitute the "detailed view" with the values associated with the listing selected in
     * the TableView.
     */
    @FXML
    private void myListingsShowSelectedListing(){
        ObservableList<Listing> listings = (ObservableList<Listing>) myListingsTableView.getItems();
        int selectionIndex = myListingsTableView.getSelectionModel().getSelectedIndex();
        if (selectionIndex >= 0 && selectionIndex < listings.size()){
            Listing selection = listings.get(selectionIndex);
            myListingsDetailViewTitle.setText(selection.getTitle());
            if (selection.isAvailable()){
                myListingsDetailViewAvailable.setText("Available");
            } else {
                myListingsDetailViewAvailable.setText("Unavailable");
            }
            myListingsDetailViewListingID.setText(Integer.toString(selection.getListingID()));
            myListingsDetailViewDescription.setText(selection.getDescription());
            if (selection.isForRent()) {
                int rentalPeriodHours = selection.getRentalPeriodHours();
                if (rentalPeriodHours == 1) {
                    myListingsDetailViewPrice.setText(String.format("$%.2f per hour", selection.getPriceInCents() / 100.00));
                } else if (rentalPeriodHours < 24){
                    myListingsDetailViewPrice.setText(String.format("$%.2f per %d hours",
                            selection.getPriceInCents() / 100.00, rentalPeriodHours));
                }else if (rentalPeriodHours == 24){
                    myListingsDetailViewPrice.setText(String.format("$%.2f per day", selection.getPriceInCents()/100.00));
                } else if (rentalPeriodHours % 24 == 0){
                    myListingsDetailViewPrice.setText(String.format("$%.2f per day", selection.getPriceInCents()/100.00));
                } else {
                    myListingsDetailViewPrice.setText(String.format("$%.2f for %d days and %d hours",
                            selection.getPriceInCents()/100.00, rentalPeriodHours/24, rentalPeriodHours % 24));
                }
            } else {
                myListingsDetailViewPrice.setText(String.format("$%.2f", selection.getPriceInCents()/100.0));
            }
            myListingsDetailViewAdded.setText(selection.getDatetimeAdded());
            myListingsDetailViewModified.setText(selection.getDatetimeModified());
            try {
                Blob imageBlob = selection.getImage();
                if (imageBlob != null){
                    myListingsDetailImageView.setImage(new Image(selection.getImage().getBinaryStream()));
                    myListingsDetailImageView.setVisible(true);
                } else {
                    myListingsDetailImageView.setVisible(false);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //***********My Listings Methods END**********//
    //***********Account Management Methods BEGIN**********//
    @FXML
    private void acctMgmtDisableAccountButtonOnAction(){
        //Get user confirmation of their choice.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Disable Account Confirmation");
        alert.setHeaderText("Are you sure you want to disable your account?");
        alert.setContentText("""
                Disabling your account will disable and hide all your listings.
                You will no longer be able to interact with the application except to re-enable your account.
                
                Are you sure you want to disable your account?
                """);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
           //Disable the account.
           //Update database accordingly: set user isDisabled, user's listings isDisabled and isHidden.
        }
    }
    //***********Account Management Methods END**********//




}
