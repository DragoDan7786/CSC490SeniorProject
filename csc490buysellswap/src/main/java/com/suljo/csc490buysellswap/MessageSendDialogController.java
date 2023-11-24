package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MessageSendDialogController {
    private boolean messageSent = false;
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

    public void setFieldInitialValues(String toUsername, String subjectString, Integer listingID){
        if (toUsername != null){
            toField.setText(toUsername);
        }
        if (subjectString != null){
            subjectField.setText(subjectString);
        }
        if (listingID != null){
            listingIdField.setText(listingID.toString());
        }
    }
}
