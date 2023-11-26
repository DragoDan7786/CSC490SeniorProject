package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.sql.SQLException;

public class EditListingDialogController {
    private Stage stage;
    private Listing listing;
    private File imageFile;
    private boolean listingUpdated = false;
    @FXML
    private Image image;
    @FXML
    private TextField listingTitleTextField;
    @FXML
    private TextArea listingDescriptionTextArea;
    @FXML
    private ToggleGroup saleRentToggleGroup;
    @FXML
    private RadioButton forSaleRadio;
    @FXML
    private RadioButton forRentRadio;
    @FXML
    private TextField rentalPeriodTextField;
    @FXML
    private ToggleGroup rentalPeriodToggleGroup;
    @FXML
    private RadioButton daysRadio;
    @FXML
    private RadioButton hoursRadio;
    @FXML
    private TextField listedPriceTextField;
    @FXML
    private TextField finalSalePriceTextField;
    @FXML
    private DatePicker dateSoldDatePicker;
    @FXML
    private ImageView listingImageView;
    @FXML
    private TextField filePathTextField;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void handleCancel(){
        stage.close();
    }

    public boolean isListingUpdated() {
        return listingUpdated;
    }

    /**
     * Sets the Listing to be edited and initializes fields to the Listing's starting values.
     * @param listing
     */
    public void setListing(Listing listing){
        this.listing = listing;
        listingTitleTextField.setText(listing.getTitle());
        listingDescriptionTextArea.setText(listing.getDescription());
        if (!listing.isForRent()){
            saleRentToggleGroup.selectToggle(forSaleRadio);
            handleSaleToggle();
        } else {
            saleRentToggleGroup.selectToggle(forRentRadio);
            handleRentToggle();
            rentalPeriodToggleGroup.selectToggle(hoursRadio);
            rentalPeriodTextField.setText(String.valueOf(listing.getRentalPeriodHours()));
        }
        listedPriceTextField.setText(String.format("%.2f", (double)listing.getPriceInCents()/100));
        //A sale price of -1 flags a listing as having not yet been sold, so display the final price only the value is positive.
        if (listing.getSoldAtPriceInCents() >= 0){
            finalSalePriceTextField.setText(String.format("%.2f", (double)listing.getSoldAtPriceInCents()/100));
            dateSoldDatePicker.setValue(listing.getDateSold());
        } else {
            finalSalePriceTextField.setText("");
        }
        image = listing.getImage();
        if (image != null){
            listingImageView.setImage(image);
            listingImageView.setVisible(true);
        } else {
            listingImageView.setVisible(false);
        }
    }

    /**
     * Disables the fields associated with renting an item.
     */
    @FXML
    private void handleSaleToggle(){
        rentalPeriodTextField.clear();
        rentalPeriodTextField.setDisable(true);
        daysRadio.setDisable(true);
        hoursRadio.setDisable(true);
    }

    /**
     * Enables the fields associated with renting an item.
     */
    @FXML
    private void handleRentToggle(){
        rentalPeriodTextField.setDisable(false);
        daysRadio.setDisable(false);
        hoursRadio.setDisable(false);
    }

    /**
     * Allows the user to select a PNG-formmated image.
     */
    @FXML
    private void handleSelectImage(){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter pngExtFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        fc.getExtensionFilters().add(pngExtFilter);
        imageFile = fc.showOpenDialog(null);
        if (imageFile != null){
            try {
                //Show the path of the opened image.
                filePathTextField.setText(imageFile.getCanonicalPath());
                //Show the image itself.
                image = new Image(new FileInputStream(imageFile));
                listingImageView.setImage(image);
                listingImageView.setVisible(true);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Displaying Image");
                alert.setHeaderText("The file was opened but could not be read.");
                alert.setContentText(e.toString());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File Error");
            alert.setHeaderText(null);
            alert.setContentText("The selected file could not be opened.");
            listingImageView.setVisible(false);
            filePathTextField.clear();
        }
    }

    /**
     * Removes the image, if any.
     */
    @FXML
    private void handleRemoveImage(){
        imageFile = null;
        image = null;
        filePathTextField.clear();
        listingImageView.setVisible(false);
    }

    /**
     * Updates the selecting Listing object and the corresponding entry in the database according to the values
     * entered by the user.
     */
    @FXML
    private void handleSaveChanges(){
        //Make a backup Listing. If the database update fails, we can restore the values of the underlying Listing.
        Listing backup = listing.copy();
        //Update the object in memory first.
        updateListingObject();
        //Then update the database to match.
        try {
            if (DbOperations.updateListing(listing, imageFile)){
                //If successful, set the signal and tell the user.
                listingUpdated = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Item successfully updated.");
                alert.showAndWait();
                stage.close();
            } else {
                //If unsuccessful for a reason other than a database error, warn the user.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Failed");
                alert.setHeaderText("Could note update listing.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
                listing.copyValues(backup);
            }
        } catch (SQLException e) {
            //If unsuccessful due to a database error, display the error information to the user.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error accessing database.\nCould not update listing.");
            alert.setContentText(e.toString());
            alert.showAndWait();
            listing.copyValues(backup);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File Error");
            alert.setHeaderText("Could not open image file.");
            alert.setContentText(e.toString());
            alert.showAndWait();
            listing.copyValues(backup);
        }
    }

    /**
     * Updates the selected Listing object to match the values entered by the user.
     */
    private void updateListingObject(){
        listing.setTitle(listingTitleTextField.getText());
        listing.setDescription(listingDescriptionTextArea.getText());
        listing.setForRent(saleRentToggleGroup.getSelectedToggle().equals(forRentRadio));
        if (listing.isForRent() && rentalPeriodToggleGroup.getSelectedToggle().equals(daysRadio)){
            listing.setRentalPeriodHours(Integer.parseInt(rentalPeriodTextField.getText())*24);
        } else if (listing.isForRent() && rentalPeriodToggleGroup.getSelectedToggle().equals(hoursRadio)) {
            listing.setRentalPeriodHours(Integer.parseInt(rentalPeriodTextField.getText()));
        } else {
            listing.setRentalPeriodHours(-1);
        }
        if (!listedPriceTextField.getText().isEmpty()){
            double listedPrice = Double.parseDouble(listedPriceTextField.getText());
            listing.setPriceInCents((int) (listedPrice * 100));
        }
        if (!finalSalePriceTextField.getText().isEmpty()){
            double finalSalePrice = Double.parseDouble(finalSalePriceTextField.getText());
            listing.setSoldAtPriceInCents((int) finalSalePrice * 100);
            //If there is a final sale price, by implication the item is not available.
            listing.setAvailable(false);
            listing.setActive(false);
            listing.setVisible(false);
        }
        listing.setDateSold(dateSoldDatePicker.getValue());
        listing.setImage(image);
    }
}
