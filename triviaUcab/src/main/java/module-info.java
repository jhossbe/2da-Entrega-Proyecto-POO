module org.example.triviaucab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.example.triviaucab to javafx.fxml;
    exports org.example.triviaucab.controller;
    opens org.example.triviaucab.controller to javafx.fxml;
    opens com.example.triviaucab to javafx.fxml;
}