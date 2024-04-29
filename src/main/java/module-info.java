module com.example.myappmusicwithdatabase2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.swing;
    requires javafx.media;


    opens com.example.myappmusicwithdatabase2 to javafx.fxml;
    exports com.example.myappmusicwithdatabase2.main;
}