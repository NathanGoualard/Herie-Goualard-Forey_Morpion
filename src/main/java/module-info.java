module com.simplerp.morpion {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.simplerp.morpion to javafx.fxml;
    opens com.simplerp.morpion.score to javafx.fxml;
    exports com.simplerp.morpion;
    exports com.simplerp.morpion.accueille;
    exports com.simplerp.morpion.score;
    opens com.simplerp.morpion.accueille to javafx.fxml;
}