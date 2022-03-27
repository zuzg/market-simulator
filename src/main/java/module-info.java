module com.jarmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.jarmark to javafx.fxml;
    exports com.jarmark;
    exports com.jarmark.gui;
    opens com.jarmark.gui to javafx.fxml;
}