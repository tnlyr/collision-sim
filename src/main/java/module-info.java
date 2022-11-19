module edu.vanier.collisions {
    requires javafx.controls;
    requires javafx.fxml;

    exports edu.vanier.collisions.views;
    exports edu.vanier.collisions.models to javafx.fxml;
    opens edu.vanier.collisions.controllers to javafx.fxml;
}