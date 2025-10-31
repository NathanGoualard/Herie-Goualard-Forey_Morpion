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
    requires annotations;
    requires java.sql;

    exports com.simplerp.morpion.accueil;
    opens com.simplerp.morpion.accueil to javafx.fxml;

    exports com.simplerp.morpion.score;
    opens com.simplerp.morpion.score to javafx.fxml;
}