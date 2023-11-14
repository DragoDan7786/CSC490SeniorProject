package com.suljo.csc490buysellswap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        dbProperties.load(new FileInputStream("C:\\Users\\sulem\\IdeaProjects\\CSC490SeniorProject\\csc490buysellswap\\src\\main\\java\\com\\suljo\\csc490buysellswap\\db.properties"));
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