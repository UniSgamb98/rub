module com.example.rub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    exports com.example.rub.enums;
    exports com.example.rub.beans;


    opens com.example.rub to javafx.fxml;
    exports com.example.rub;
    exports com.example.rub.objects.filter.generic;
    exports com.example.rub.objects.filter.location;
    exports com.example.rub.objects.filter;
    exports com.example.rub.functionalities;
    exports com.example.rub.functionalities.locations;
    exports com.example.rub.objects.mail;
    exports com.example.rub.objects.note;
}