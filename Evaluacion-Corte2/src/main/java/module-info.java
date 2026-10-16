module org.example.evaluacioncorte2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.example.evaluacioncorte2 to javafx.fxml;
    opens org.example.evaluacioncorte2.controller to javafx.fxml;
    opens org.example.evaluacioncorte2.model to javafx.base;

    exports org.example.evaluacioncorte2;
    exports org.example.evaluacioncorte2.controller;
    exports org.example.evaluacioncorte2.model;
}