package com.suljo.csc490buysellswap;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

import static com.suljo.csc490buysellswap.DbOperations.hideMessage;
import static com.suljo.csc490buysellswap.DbOperations.selectAllActiveListings;
import static javafx.scene.layout.Priority.*;

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
    private TableView<Listing> myListingsTableView;
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
    @FXML
    private ListView<Listing> buyerBrowseListView;
    //***********My Listings Elements END**********//
    //***********Account Management Elements BEGIN**********//

    //***********Account Management Elements END**********//
    //***********Sell Tab Elements END**********//
    //***********Messages Tab Elements BEGIN**********//
    @FXML
    private TabPane messagesTabPane;
    @FXML
    private Tab messagesReceivedTab;
    @FXML
    private Tab messagesSentTab;
    @FXML
    private TableView<Message> messagesReceivedTable;
    @FXML
    private TableColumn<Message, String> messagesReceivedTableSubjectColumn;
    @FXML
    private TableColumn<Message, String> messagesReceivedTableFromColumn;
    @FXML
    private TableColumn<Message, String> messagesReceivedTableDatetimeColumn;
    @FXML
    private TextField messagesReceivedFromTextField;
    @FXML
    private TextField messagesReceivedDateTextField;
    @FXML
    private TextField messagesReceivedSubjectTextField;
    @FXML
    private TextArea messagesReceivedContentsTextArea;
    @FXML
    private TableView<Message> messagesSentTable;
    @FXML
    private TableColumn<Message, String> messagesSentTableSubjectColumn;
    @FXML
    private TableColumn<Message, String> messagesSentTableToColumn;
    @FXML
    private TableColumn<Message, String> messagesSentTableDatetimeColumn;
    @FXML
    private TextField messagesSentToTextField;
    @FXML
    private TextField messagesSentDateTextField;
    @FXML
    private TextField messagesSentSubjectTextField;
    @FXML
    private TextArea messagesSentContentsTextArea;
    //***********Messages Tab Elements END**********//
    @FXML
    private Tab buyTab;





    // Initialize price range to maximum and minimum possible values
    private int minPrice = 0;
    private int maxPrice = Integer.MAX_VALUE;
    private boolean priceFilterEdited = false;

    //***********General User View Methods BEGIN**********//
    public void initialize() {
        //If the currentUser isn't an admin, disable the admin tab.
        adminTab.setDisable(!BuySellSwapApp.getCurrentUser().isAdmin());
        initializeListAnItemTab();
        initializeMyListingsTab();
        browseListingInitialize();
        HBox.setHgrow(searchTxtField, ALWAYS);
        initializeMessagesTab();
        searchBarInitialize();
        //checks if db was edited upon hitting buy tab.
        buyTab.setOnSelectionChanged(event -> {
            if (buyTab.isSelected()) {
                // Call browseListingInitialize to update the ListView
                browseListingInitialize();
                searchBarInitialize();
            }
        });
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
                FileInputStream imageInputStream = new FileInputStream(listAnItemImage);
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
    private void listAnItemResetTab() {
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

    /**
     * Initializes the My Listings tab.
     */
    private void initializeMyListingsTab() {
        //Set up the TableView.
        myListingsTableColumnId.setCellValueFactory(new PropertyValueFactory<>("listingID"));
        myListingsTableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        myListingsTableColumnListedDatetime.setCellValueFactory(new PropertyValueFactory<>("datetimeAdded"));
        myListingsPopulateTableView();
        myListingsSetDetailedViewElementsNotEditable();
        myListingsDetailImageView.setVisible(false);
        myListingsTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> myListingsShowSelectionDetails(newValue));
    }

    /**
     * Enable the fields in the "detailed view".
     */
    private void myListingsSetDetailedViewElementsNotEditable() {
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
    private void myListingsPopulateTableView() {
        myListingsTableView.getItems().clear();
        try {
            for (Listing listing : DbOperations.selectMyListings()) {
                myListingsTableView.getItems().add(listing);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the detailed view fields to the current My Listings selection, or clears them if there is no selection.
     *
     * @param selection
     */
    private void myListingsShowSelectionDetails(Listing selection) {
        if (selection != null) {
            //Set textual parameters,
            myListingsDetailViewTitle.setText(selection.getTitle());
            myListingsDetailViewListingID.setText(Integer.toString(selection.getListingID()));
            myListingsDetailViewDescription.setText(selection.getDescription());
            myListingsDetailViewAdded.setText("Added: " +
                    DateTimeUtil.mssqlDatetime2ToHumanReadable(selection.getDatetimeAdded(), DateTimeUtil.yearMonthDay_12HoursMinutesSeconds));
            myListingsDetailViewModified.setText("Modified: " +
                    DateTimeUtil.mssqlDatetime2ToHumanReadable(selection.getDatetimeModified(), DateTimeUtil.yearMonthDay_12HoursMinutesSeconds));
            myListingsSetDetailViewPrice(selection);
            if (selection.isAvailable()) {
                myListingsDetailViewAvailable.setText("Available");
            } else {
                myListingsDetailViewAvailable.setText("Unavailable");
            }
            //Display the image, if any.
            Image image = selection.getImage();
            if (image != null) {
                myListingsDetailImageView.setImage(image);
                myListingsDetailImageView.setVisible(true);
            } else {
                myListingsDetailImageView.setVisible(false);
            }
        } else {
            //If selection is null, clear the detailed view.
            myListingsResetSelectionDetails();
        }
    }

    /**
     * Determines and sets the price string to display in the My Listings detailed view.
     * @param selection The selected listing whose details are to be displayed.
     */
    private void myListingsSetDetailViewPrice(Listing selection) {
        if (!selection.isForRent()) {
            myListingsDetailViewPrice.setText(String.format("$%.2f", selection.getPriceInCents() / 100.0));
        } else {
            int rentalPeriodHours = selection.getRentalPeriodHours();
            if (rentalPeriodHours == 1) {
                myListingsDetailViewPrice.setText(String.format("$%.2f per hour", selection.getPriceInCents() / 100.00));
            } else if (rentalPeriodHours < 24) {
                myListingsDetailViewPrice.setText(String.format("$%.2f per %d hours",
                        selection.getPriceInCents() / 100.00, rentalPeriodHours));
            } else if (rentalPeriodHours == 24) {
                myListingsDetailViewPrice.setText(String.format("$%.2f per day", selection.getPriceInCents() / 100.00));
            } else if (rentalPeriodHours % 24 == 0) {
                myListingsDetailViewPrice.setText(String.format("$%.2f per day", selection.getPriceInCents() / 100.00));
            } else {
                myListingsDetailViewPrice.setText(String.format("$%.2f for %d days and %d hours",
                        selection.getPriceInCents() / 100.00, rentalPeriodHours / 24, rentalPeriodHours % 24));
            }
        }
    }

    /**
     * Clears the detailed view of My Listings.
     */
    private void myListingsResetSelectionDetails() {
        myListingsDetailViewTitle.setText("Title");
        myListingsDetailViewListingID.setText("ID");
        myListingsDetailViewDescription.setText("Description.");
        myListingsDetailViewAdded.setText("Added");
        myListingsDetailViewModified.setText("Modified");
        myListingsDetailViewAvailable.setText("Available");
        myListingsDetailViewPrice.setText("Price");
        myListingsDetailImageView.setVisible(false);
    }

    /**
     * Generates an edit listing dialog for the currently selected listing.
     */
    @FXML
    private void myListingsHandleEditListing() {
        Listing selection = myListingsTableView.getSelectionModel().getSelectedItem();
        if (selection != null && BuySellSwapApp.editListingDialog(selection, myListingsTableView.getScene().getWindow())) {
            myListingsShowSelectionDetails(selection);
        }
    }

    /**
     * Makes the selected listing unavailable.
     */
    @FXML
    private void myListingsHandleMakeUnavailableButton(){
        try {
            Listing selectedListing = myListingsTableView.getSelectionModel().getSelectedItem();
            if (selectedListing != null && DbOperations.makeListingUnavailable(selectedListing.getListingID())){
                //If the listing was updated, refresh the tableview to keep it in sync with the database.
                myListingsPopulateTableView();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while connecting the database. The listing could not be made unavailable.");
            alert.setContentText(e.getMessage());
            e.printStackTrace();
        }
    }
    //***********My Listings Methods END**********//
    //***********Account Management Methods BEGIN**********//

    /**
     * Disables the user's account after getting appropriate confirmation.
     */
    @FXML
    private void acctMgmtDisableAccountButtonOnAction() {
        //Get user confirmation of their choice.
        Optional<ButtonType> result = acctMgmtDisableAccountConfirmationAlert();
        //If confirmation given, try to disable the account.
        if (result.get() == ButtonType.OK) {
            try {
                //Disable the account.
                DbOperations.disableUserAccount(BuySellSwapApp.getCurrentUser().getUserID());
                //If successful, inform the user and log them out.
                acctMgmtAccountDisabledAlert();
                menuItemLogoutOnAction();
            } catch (SQLException e) {
                //If unsuccessful, inform the user.
                acctMgmtAccountDisabledFailureAlert();
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates an alert which gets confirmation from the user that they wish to disable their account.
     * @return The user's choice.
     */
    private Optional<ButtonType> acctMgmtDisableAccountConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Disable Account Confirmation");
        alert.setHeaderText("Are you sure you want to disable your account?");
        alert.setContentText("""
                Disabling your account will disable and hide all your listings.
                You will no longer be able to interact with the application without registering a new account.
                                
                Are you sure you want to disable your account?
                """);
        return alert.showAndWait();
    }

    /**
     * Displays an alert informing the user that their account was successfully disabled.
     */
    private void acctMgmtAccountDisabledAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Disabled");
        alert.setHeaderText("Your account has been disabled.");
        alert.setContentText("""
                Your listings have been disabled and you will now be logged out.
                If you would like to return to the app, please register a new account.
                """);
        alert.showAndWait();
    }

    /**
     * Displays an alert to the user warning them that their account could not be disabled.
     */
    private void acctMgmtAccountDisabledFailureAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Not Disabled");
        alert.setHeaderText("Your account could not be disabled.");
        alert.setContentText("""
                It is possible that a database error occurred. Please try again later.
                """);
        alert.show();
    }
    //***********Account Management Methods END**********//

    //***********Browse Listings Methods BEGIN**********//
    private void browseListingInitialize() {
        try {
            buyerBrowseListView.setItems(selectAllActiveListings());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        buyerBrowseListView.setCellFactory(param -> new ListCell<Listing>() {
            @Override
            protected void updateItem(Listing listing, boolean empty) {
                super.updateItem(listing, empty);
                if (empty || listing == null || listing.getTitle() == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Convert price from cents to dollars
                    double priceInDollars = listing.getPriceInCents() / 100.0;

                    // Format date
                    DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS");
                    LocalDateTime dateTime = LocalDateTime.parse(listing.getDatetimeAdded(), oldFormat);
                    DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    String date = dateTime.format(newFormat);

                    // Determine type of listing
                    String typeOfListing = listing.isForRent() ? "Rent" : "Buy";

                    // Set the text of the ListCell
                    setText("Type: " + typeOfListing + "\nTitle: " + listing.getTitle() + "\nPrice: $" + priceInDollars + "\nDate Added: " + date);

                    ImageView imageView = new ImageView();
                    Image image = listing.getImage();
                    if (image != null) {
                        imageView.setImage(image);
                        imageView.setFitWidth(50);  // Adjust the width and height as needed
                        imageView.setFitHeight(50);
                        imageView.setPreserveRatio(true);
                    }

                    // Set the ImageView as the graphic of the ListCell
                    setGraphic(imageView);
                }
            }
        });


        buyerBrowseListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Listing selectedListing = buyerBrowseListView.getSelectionModel().getSelectedItem();

                // Create a new Stage for the detailed view
                Stage stage = new Stage();

                // Create a VBox for the detailed view
                VBox vbox = new VBox();

                // Add all fields of the listing to the VBox
                vbox.getChildren().add(new Label("Title: " + selectedListing.getTitle()));
                vbox.getChildren().add(new Label("Description: " + selectedListing.getDescription()));
                vbox.getChildren().add(new Label("Price: $" + selectedListing.getPriceInCents() / 100.0));
                if (selectedListing.isForRent())
                    vbox.getChildren().add(new Label("Rental Period Hours: " + selectedListing.getRentalPeriodHours()));
                vbox.getChildren().add(new Label("Seller User ID: " + selectedListing.getSellerUserID()));
                vbox.getChildren().add(new Label("Date Added: " + selectedListing.getDatetimeAdded()));

                // If the image is not null, add it to the VBox
                Image image = selectedListing.getImage();
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);  // Adjust the width and height as needed
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);
                    vbox.getChildren().add(imageView);
                }

                // Add buttons to the VBox
                Button msgBuyerButton = new Button("Contact Buyer");
                vbox.getChildren().add(msgBuyerButton);
                Button reportListingButton = new Button("Report Listing");
                vbox.getChildren().add(reportListingButton);
                // Add actions to the buttons as needed
                msgBuyerButton.setOnAction(e -> buyerBrowseMessageSeller(selectedListing));
                // Create a new Scene with the VBox and set it on the Stage
                Scene scene = new Scene(vbox);
                stage.setScene(scene);

                // Show the Stage
                stage.show();
            }
        });
    }

    /**
     * Generates a new message dialog populated with details relevant to the selected listing.
     *
     * @param listing The listing.
     */
    private void buyerBrowseMessageSeller(Listing listing) {
        try {
            String toUsername = DbOperations.userIDToUsername(listing.getSellerUserID());
            if (BuySellSwapApp.messageSendDialog(toUsername, listing.getTitle(), listing.getListingID(),
                    null, null, messagesTabPane.getScene().getWindow())) {
                messagesRefresh();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An database error occurred. Could not message seller.");
            alert.setContentText(e.getMessage());
            e.printStackTrace();
        }
    }
    //***********Browse Listings Methods END**********//
    //***********Messages Methods BEGIN**********//

    /**
     * Initiliazes the Messages tab elements. Sets table cell factories and text prompts.
     */
    private void initializeMessagesTab() {
        //Initialize the Received tab.
        messagesReceivedTableSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        messagesReceivedTableFromColumn.setCellValueFactory(cellData -> cellData.getValue().fromUsernameProperty());
        messagesReceivedTableDatetimeColumn.setCellValueFactory(cellData -> cellData.getValue().displayDatetimeSentProperty());
        messagesPopulateReceivedTable();
        messagesReceivedTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> messagesShowReceivedMessageDetails(newValue));
        messagesReceivedFromTextField.setEditable(false);
        messagesReceivedDateTextField.setEditable(false);
        messagesReceivedSubjectTextField.setEditable(false);
        messagesReceivedContentsTextArea.setEditable(false);
        //Initialize the Sent tab.
        messagesSentTableSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        messagesSentTableToColumn.setCellValueFactory(cellData -> cellData.getValue().toUsernameProperty());
        messagesSentTableDatetimeColumn.setCellValueFactory(cellData -> cellData.getValue().displayDatetimeSentProperty());
        messagesPopulateSentTable();
        messagesSentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> messagesShowSentMessageDetails(newValue));
        messagesSentToTextField.setEditable(false);
        messagesSentDateTextField.setEditable(false);
        messagesSentSubjectTextField.setEditable(false);
        messagesSentContentsTextArea.setEditable(false);
    }

    /**
     * Populates the Received tab master list with messages sent by the current user.
     * Messages that have been (soft) deleted == hidden are not added.
     */
    private void messagesPopulateReceivedTable() {
        try {
            messagesReceivedTable.getItems().clear();
            for (Message message : DbOperations.getMessagesToUser(BuySellSwapApp.getCurrentUser().getUserName())) {
                if (!message.isHidden()) {
                    messagesReceivedTable.getItems().add(message);
                }
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error retrieving current user's messages.");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }

    /**
     * Populates the Sent tab master list with messages sent by the current user.
     * Messages that have been (soft) deleted == hidden are not added.
     */
    private void messagesPopulateSentTable() {
        try {
            messagesSentTable.getItems().clear();
            for (Message message : DbOperations.getMessagesFromUser(BuySellSwapApp.getCurrentUser().getUserName())) {
                if (!message.isHidden()) {
                    messagesSentTable.getItems().add(message);
                }
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error retrieving current user's messages.");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }

    /**
     * Displays the selected message's details in the Received tab detail view.
     *
     * @param message The selected message.
     */
    private void messagesShowReceivedMessageDetails(Message message) {
        if (message != null) {
            messagesReceivedFromTextField.setText(message.getFromUsername());
            messagesReceivedDateTextField.setText(message.getDisplayDatetimeSent());
            if (message.getAboutListingID() != 0) {
                messagesReceivedSubjectTextField.setText("[Listing #" + message.getAboutListingID() + "] " + message.getSubject());
            } else {
                messagesReceivedSubjectTextField.setText(message.getSubject());
            }
            messagesReceivedContentsTextArea.setText(message.getContents());
        } else {
            messagesResetReceivedMessageDetails();
        }
    }

    /**
     * Displays the selected message's details in the Sent tab detail view.
     *
     * @param message The selected message.
     */
    private void messagesShowSentMessageDetails(Message message) {
        if (message != null) {
            messagesSentToTextField.setText(message.getToUsername());
            messagesSentDateTextField.setText(message.getDisplayDatetimeSent());
            if (message.getAboutListingID() != 0) {
                messagesReceivedSubjectTextField.setText("[Listing #" + message.getAboutListingID() + "] " + message.getSubject());
            } else {
                messagesReceivedSubjectTextField.setText(message.getSubject());
            }
            messagesSentContentsTextArea.setText(message.getContents());
        } else {
            messagesResetSentMessageDetails();
        }
    }

    /**
     * Resets the text prompts in the received messages detail view.
     */
    private void messagesResetReceivedMessageDetails() {
        messagesReceivedFromTextField.setText("From");
        messagesReceivedDateTextField.setText("Date");
        messagesReceivedSubjectTextField.setText("Subject");
        messagesReceivedContentsTextArea.setText("Contents");
    }

    /**
     * Resets the text prompts in the sent messages detail view.
     */
    private void messagesResetSentMessageDetails() {
        messagesSentToTextField.setText("To");
        messagesSentDateTextField.setText("Date");
        messagesSentSubjectTextField.setText("Subject");
        messagesSentContentsTextArea.setText("Contents");
    }

    /**
     * Updates both TableViews in the Messages tab, synchronizing them with the present state of the database.
     */
    @FXML
    private void messagesRefresh() {
        messagesPopulateReceivedTable();
        messagesPopulateSentTable();
    }

    /**
     * Generates a new message dialog with none of the fields pre-populated. The user must enter a correct username, etc.,
     * themselves.
     */
    @FXML
    private void messagesNewMessage() {
        if (BuySellSwapApp.messageSendDialog(null, null, null, null,
                null, messagesReceivedTable.getScene().getWindow())) {
            messagesRefresh();
        }
    }

    /**
     * Performs a soft delete on the selected message by setting the hidden flag and updating the database and GUI accordingly.
     */
    @FXML
    private void messagesHandleDeleteButton() {
        //Handle the case that the delete button is pressed without a message first being selected by checking for nulls.
        Message selectedMessage = getSelectedMessage();
        try {
            if (selectedMessage != null && confirmMessageDeletion()) {
                hideMessage(selectedMessage.getMessageID());
                messagesRefresh();
                //I was hoping to avoid database operations for the sake of efficiency, but the below only seems to
                //remove the message from a single TableView, not from both.
                //I need to move on to other things, so I'll just re-populate the tables, to ensure it's working.
                //But I'll leave the code below in case later we want to try and fix it.
//                //Messages can be sent to yourself, in which case they will appear in both TableViews.
//                //So just to be safe, remove the message from both TableViews, if present.
//                //Since each table is populated from separate queries, each query generating an independent set of
//                //Message instances, have to compare with an equals method - can't just remove the object itself, as the
//                //individual selected object is only in one of the two TableViews.
//                messagesSentTable.getItems().removeIf(message -> message.equals(selectedMessage));
//                messagesReceivedTable.getItems().removeIf(message -> message.equals(selectedMessage));
//                //No need to actually set the hidden property on the selected message, as this message will go out of
//                //scope and be garbage collected once it's removed from the TableView lists anyway.
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while deleting the message. Please try again.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Helper method. Gets the selected message from the TableView in the currently active tab (Received vs. Sent).
     *
     * @return The selected message in the currently active tab.
     */
    private Message getSelectedMessage() {
        Message selectedMessage = null;
        if (messagesTabPane.getSelectionModel().getSelectedItem().equals(messagesReceivedTab)) {
            selectedMessage = messagesReceivedTable.getSelectionModel().getSelectedItem();
        } else if (messagesTabPane.getSelectionModel().getSelectedItem().equals(messagesSentTab)) {
            selectedMessage = messagesSentTable.getSelectionModel().getSelectedItem();
        }
        return selectedMessage;
    }

    /**
     * Helper method for deleting messages. Generates a confirmation dialog and returns true if the user selects OK,
     * false if the user does not.
     *
     * @return True if the user selects OK to confirm, false otherwise.
     */
    private boolean confirmMessageDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this message?");
        Optional<ButtonType> choice = alert.showAndWait();
        return (choice.get() == ButtonType.OK);
    }

    /**
     * Generates a new message dialog with the text fields pre-populated according to the selected message.
     */
    @FXML
    private void messagesHandleReply() {
        Message selectedMessage = getSelectedMessage();
        if (selectedMessage != null) {
            String toUsername = getToUsername(selectedMessage);
            String replySubject = "Re: " + selectedMessage.getSubject(); //database constraint ensures not null
            String messageBody = selectedMessage.getContents();
            messageBody = "\n>>>" + messageBody.stripTrailing().replaceAll("\\n", "\n>>>");
            if (BuySellSwapApp.messageSendDialog(toUsername, replySubject, selectedMessage.getAboutListingID(), messageBody,
                    selectedMessage.getMessageID(), messagesTabPane.getScene().getWindow())) {
                messagesRefresh(); //Refresh message lists if the message is sent
            }
        }
    }

    /**
     * Helper method. Determins the toUsername string when generating a reply.
     * The toUsername is always be the other user involved in the exchange, regardless of whether the message was sent
     * by or received by the current user.
     *
     * @param selectedMessage The currently selected message to which a reply is being generated.
     * @return The username to which the reply should be sent.
     */
    private String getToUsername(Message selectedMessage) {
        String toUsername = null;
        if (messagesTabPane.getSelectionModel().getSelectedItem().equals(messagesReceivedTab)) {
            toUsername = selectedMessage.getFromUsername();
        } else if (messagesTabPane.getSelectionModel().getSelectedItem().equals(messagesSentTab)) {
            toUsername = selectedMessage.getToUsername();
        }
        return toUsername;
    }
    //***********Messages Methods END**********//


    //***********Search Filters and Search Bar BEGIN**********//


    //text field/search bar listener
    private void searchBarInitialize() {
        // Get the items from your ListView
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();

        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);

        searchTxtField.textProperty().

                addListener((observable, oldValue, newValue) ->

                {
                    filteredData.setPredicate(listing -> {
                        // If filter text is empty, display all listings.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare title and description of every listing with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (listing.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches title.
                        } else
                            return listing.getDescription().toLowerCase().contains(lowerCaseFilter); // Filter matches description.
// Does not match.
                    });
                });

        // Add filtered data to the ListView.
        buyerBrowseListView.setItems(filteredData);
    }

    //Price Filters here
    @FXML
    private void handleLowPriceMenuItemAction(ActionEvent event) {
        if (priceFilterEdited) {
            minPrice = 0;
            maxPrice = Integer.MAX_VALUE;
            priceFilterEdited = false;
        } else {
            minPrice = 0;
            maxPrice = 2499;
            priceFilterEdited = true;
        }
        updatePriceFilter();
    }

    @FXML
    private void handleMedPriceMenuItemAction(ActionEvent event) {ObservableList<Listing> observableListings = buyerBrowseListView.getItems();

        if (priceFilterEdited) {
            minPrice = 0;
            maxPrice = Integer.MAX_VALUE;
            priceFilterEdited = false;
        } else {
            minPrice = 2500;
            maxPrice = 9999;
            priceFilterEdited = true;
        }
        updatePriceFilter();
    }

    @FXML
    private void handleHighPriceMenuItemAction(ActionEvent event) {
        if (priceFilterEdited) {
            minPrice = 0;
            maxPrice = Integer.MAX_VALUE;
            priceFilterEdited = false;
        } else {
            minPrice = 10000;
            maxPrice = Integer.MAX_VALUE;
            priceFilterEdited = true;
        }
        updatePriceFilter();
    }

    private void updatePriceFilter() {
        browseListingInitialize();
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();

        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        filteredData.setPredicate(listing -> {
            int price = listing.getPriceInCents();
            return price >= minPrice && price <= maxPrice;
        });
        buyerBrowseListView.setItems(filteredData);
    }
    //sortings
    @FXML
    private void handleSortPriceHighToLowMenuItemAction(ActionEvent event) {
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();
        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        SortedList<Listing> sortedData = new SortedList<>(filteredData);
        sortedData.setComparator(Comparator.comparing(Listing::getPriceInCents).reversed());
        buyerBrowseListView.setItems(sortedData);
    }

    @FXML
    private void handleSortPriceLowToHighMenuItemAction(ActionEvent event) {
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();
        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        SortedList<Listing> sortedData = new SortedList<>(filteredData);
        sortedData.setComparator(Comparator.comparing(Listing::getPriceInCents));
        buyerBrowseListView.setItems(sortedData);
    }

    @FXML
    private void handleSortDateMenuItemAction(ActionEvent event) {
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();
        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        SortedList<Listing> sortedData = new SortedList<>(filteredData);
        sortedData.setComparator(Comparator.comparing(Listing::getDatetimeAdded));
        buyerBrowseListView.setItems(sortedData);
    }


    //buy rent filter

    @FXML
    private void handleForBuyMenuItemAction(ActionEvent event) {
        browseListingInitialize();
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();
        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        // Filter matches listings for rent
        filteredData.setPredicate(listing -> {
            return !listing.isForRent(); // Filter matches listings for buy
        });
        buyerBrowseListView.setItems(filteredData);
    }

    @FXML
    private void handleForRentMenuItemAction(ActionEvent event) {
        browseListingInitialize();
        ObservableList<Listing> observableListings = buyerBrowseListView.getItems();
        FilteredList<Listing> filteredData = new FilteredList<>(observableListings, p -> true);
        // Filter matches listings for rent
        filteredData.setPredicate(Listing::isForRent);
        buyerBrowseListView.setItems(filteredData);
    }


    //***********Search Filters and Search Bar END**********//
}
