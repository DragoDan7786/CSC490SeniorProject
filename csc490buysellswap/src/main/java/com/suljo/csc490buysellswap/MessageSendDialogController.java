package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

public class MessageSendDialogController {
    private boolean messageSent = false;
    private Integer replyToMessageID;

    private Stage stage;
    @FXML
    private TextField toField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField listingIdField;
    @FXML
    private TextArea messageBodyField;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void handleCancel(){
        stage.close();
    }

    public boolean isMessageSent() {
        return messageSent;
    }

    public void setReplyToMessageID(Integer replyToMessageID) {
        this.replyToMessageID = replyToMessageID;
    }

    public void setToFieldText(String text){
        toField.setText(text);
    }

    public void setSubjectFieldText(String text){
        subjectField.setText(text);
    }

    public void setListingIdField(String text){
        listingIdField.setText(text);
    }

    public void setMessageBodyFieldText(String text){
        messageBodyField.setText(text);
    }

    @FXML
    private void handleSend(){
        try {
            String fromUsername = BuySellSwapApp.getCurrentUser().getUserName();
            String toUsername = toField.getText();
            String subject = subjectField.getText();
            Integer listingID;
            if (listingIdField.getText().isEmpty()){
                listingID = null;
            } else {
                listingID = Integer.parseInt(listingIdField.getText());
            }
            String contents = messageBodyField.getText();
            //Check that "To" field contains a valid username. If not, warn user and return.
            if (toUsername.isEmpty()){
                invalidInputAlert("Invalid Username", "'To' field cannot be empty.",
                        "Please enter a valid username.");
                return;
            } else if (!DbOperations.usernameExists(toUsername)){
                invalidInputAlert("Invalid Username", "Username does not exist.",
                        "Please check the 'To' field for typos and try again.");
                return;
            //Check that Listing ID field, if not empty, is valid. If not, warn user and return.
            } else if (listingID != null && !DbOperations.listingIDExists(listingID)) {
                invalidInputAlert("Invalid Listing ID", "A listing with this ID does not exist.",
                        "Please confirm the listing ID and check for typos.");
                return;
            //If checks are passed, send the message by adding it to the database.
            } else if (DbOperations.insertNewMessage(fromUsername, toUsername, listingID, subject, contents, replyToMessageID)){
                //If successful, set boolean signal, inform the user, and then close the dialog.
                messageSent = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message Sent");
                alert.setHeaderText("Message successfully sent.");
                alert.setContentText(null);
                alert.showAndWait();
                stage.close();
            } else {
                //Catch all for if the message could not be sent for some other reason.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error sending message.");
                alert.setHeaderText("Message could not be successfully sent.");
                alert.setContentText("The reason for the message send failure is unknown. Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            BuySellSwapApp.sqlExceptionAlert(e);
            e.printStackTrace();
        }
    }

    private void invalidInputAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
