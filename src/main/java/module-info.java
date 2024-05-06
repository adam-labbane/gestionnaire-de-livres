module com.example.cdi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.controlsfx.controls;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires commons.email;
    requires okhttp3;
    requires org.json;


    opens com.example.cdi to javafx.fxml;
    opens com.example.cdi.Model to org.hibernate.orm.core;
    exports com.example.cdi;
    exports com.example.cdi.Repository;
    exports org.hibernate.mydialect to org.hibernate.orm.core;
    opens com.example.cdi.Repository to javafx.fxml;
}