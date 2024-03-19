module com.example.rub {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.example.rub.objects;
    exports com.example.rub.enums;


    opens com.example.rub to javafx.fxml;
    exports com.example.rub;
    exports com.example.rub.objects.filter;
}