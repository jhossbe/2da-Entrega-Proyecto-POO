module org.example.triviaucab1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.fasterxml.jackson.databind;

    opens org.example.triviaucab1 to javafx.fxml;
    exports org.example.triviaucab1;
    exports org.example.triviaucab1.controller;
    opens org.example.triviaucab1.controller to javafx.fxml;
    exports org.example.triviaucab1.module;
    opens org.example.triviaucab1.module to  javafx.fxml, com.fasterxml.jackson.databind;
}