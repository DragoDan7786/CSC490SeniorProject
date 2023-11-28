package com.suljo.csc490buysellswap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class BuySellSwapApp extends Application {
    private static Scene scene;
    private static User currentUser;
    private static Properties dbProperties;
    private static final String dbPropertiesFilepathJC = "src/main/java/com/suljo/csc490buysellswap/db.properties";
    private static final String dbPropertiesFilepathSA = "C:\\Users\\sulem\\IdeaProjects\\CSC490SeniorProject\\csc490buysellswap\\src\\main\\java\\com\\suljo\\csc490buysellswap\\db.properties";

    @Override
    public void start(Stage stage) throws IOException {
        dbProperties = new Properties();
        dbProperties.load(new FileInputStream(dbPropertiesFilepathJC));
        scene = new Scene(loadFXML("login-view"), 960, 645);
        stage.setTitle("CSC 490 Buy-Sell-Swap");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BuySellSwapApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Generates a dialog from which the user can edit a listing.
     * @param listing The listing being edited.
     * @param parentWindow The window spawning the dialog.
     * @return True if changes to the listing were saved, otherwise false.
     */
    public static boolean editListingDialog(Listing listing, Window parentWindow){
        try {
            //Create and set up the stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Listing ID " + listing.getListingID());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentWindow);
            //Load the FXML.
            FXMLLoader loader = new FXMLLoader(BuySellSwapApp.class.getResource("edit-listing-dialog.fxml"));
            dialogStage.setScene(new Scene(loader.load()));
            //Set up the controller.
            EditListingDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setListing(listing);
            //Show the stage.
            dialogStage.showAndWait();
            //If the listing was updated, return true. Otherwise, return false.
            return controller.isListingUpdated();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a send message dialog.
     * @param toUsername The username which the "to" field will be initialized with, or null.
     * @param subjectString The value to which the subject field should be initialized, or null.
     * @param listingID The ID of the listing which this message concerns and to which the listing ID field will be initialized, or null.
     * @param messageBody The value to which the message body field will be initialized, or null.
     * @param replyToMessageID If this message is a reply, the messageID of the message being replied to, otherwise null.
     * @param parentWindow The Window which is spawning this dialog.
     * @return True if the message was sent.
     */
    public static boolean messageSendDialog(String toUsername, String subjectString, Integer listingID, String messageBody,
                                            Integer replyToMessageID, Window parentWindow){
        try {
            //Create and set up the stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("BuySellSwap Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentWindow);
            //Load the FXML.
            FXMLLoader loader = new FXMLLoader(BuySellSwapApp.class.getResource("message-send-dialog.fxml"));
            dialogStage.setScene(new Scene(loader.load()));
            //Set up the controller.
            MessageSendDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setReplyToMessageID(replyToMessageID);
            if (toUsername != null){
                controller.setToFieldText(toUsername);
            }
            if (subjectString != null){
                controller.setSubjectFieldText(subjectString);
            }
            if (listingID != null){
                controller.setListingIdField(listingID.toString());
            }
            if (messageBody != null){
                controller.setMessageBodyFieldText(messageBody);
            }
            //Show the stage.
            dialogStage.showAndWait();
            //Signal if the message was sent (or not).
            return controller.isMessageSent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays an alert to the user when an exception is thrown while interacting with the database.
     * The alert will display the exception message.
     * @param exception The exception that was generated.
     */
    public static Optional<ButtonType> sqlExceptionAlert(SQLException exception){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("There was a problem interacting with the database. Please try again.");
        alert.setContentText(exception.getMessage());
        return  alert.showAndWait();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        BuySellSwapApp.currentUser = currentUser;
    }

    public static Properties getDbProperties() {
        return dbProperties;
    }

    public static void main(String[] args) {
        launch();
    }
}