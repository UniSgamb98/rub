module com.example.rub {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rub to javafx.fxml;
    exports com.example.rub;
}