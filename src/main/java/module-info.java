module com.mycompany.csc490sproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.csc490sproject to javafx.fxml;
    exports com.mycompany.csc490sproject;
}
