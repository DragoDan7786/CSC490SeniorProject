package com.suljo.csc490buysellswap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}