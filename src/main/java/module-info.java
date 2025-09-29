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

    opens com.simplerp.morpion to javafx.fxml;
    exports com.simplerp.morpion;
    exports com.simplerp.morpion.accueil;
    opens com.simplerp.morpion.accueil to javafx.fxml;
}