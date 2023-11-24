package com.suljo.csc490buysellswap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BuySellSwapApp extends Application {
    private static Scene scene;
    private static User currentUser;
    private static Properties dbProperties;

    @Override
    public void start(Stage stage) throws IOException {
        dbProperties = new Properties();
        dbProperties.load(new FileInputStream("src/main/java/com/suljo/csc490buysellswap/db.properties"));
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

    public static boolean showEditListingDialog(Listing listing, Window parentWindow){
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

    public static boolean showMessageSendDialog(String toUsername, String subjectString, Integer listingID, Window parentWindow){
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
            //Show the stage.
            dialogStage.showAndWait();
            //Signal if the message was sent (or not).
            return controller.isMessageSent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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