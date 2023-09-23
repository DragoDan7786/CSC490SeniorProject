package com.suljo.csc490buysellswap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BuySellSwapApp extends Application {
    private static Scene scene;
    private static boolean isAdmin = false;

    @Override
    public void start(Stage stage) throws IOException {
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

    static void setAdmin(boolean value){
        isAdmin = value;
    }

    static boolean getAdminStatus(){
        return  isAdmin;
    }

    public static void main(String[] args) {
        launch();
    }
}