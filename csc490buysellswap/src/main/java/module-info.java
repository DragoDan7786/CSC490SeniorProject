module com.suljo.csc490buysellswap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.suljo.csc490buysellswap to javafx.fxml;
    exports com.suljo.csc490buysellswap;
}