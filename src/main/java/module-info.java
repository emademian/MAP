module com.example.labjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.labjavafx to javafx.fxml,javafx.base;
    opens com.example.labjavafx.model to javafx.fxml,javafx.base;
    exports com.example.labjavafx;
    exports com.example.labjavafx.service;
    //exports com.sun.javafx.event to org.controlsfx.controls;
}