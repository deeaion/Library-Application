module com.example.libraryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.libraryapp to javafx.fxml;
    exports com.example.libraryapp;
}