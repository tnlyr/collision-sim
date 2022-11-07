module edu.vanier.collisions {
    requires javafx.controls;
    requires javafx.fxml;

    exports edu.vanier.collisions.views;
    opens edu.vanier.collisions.controllers to javafx.fxml;
}