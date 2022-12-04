module edu.vanier.collisions {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    exports edu.vanier.collisions.views;
    exports edu.vanier.collisions.models to javafx.fxml;
    opens edu.vanier.collisions.controllers to javafx.fxml;
}