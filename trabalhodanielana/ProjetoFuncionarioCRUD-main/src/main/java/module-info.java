module elieldm.funcionarios {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens anacarolina.funcionarios to javafx.fxml;
    opens anacarolina.funcionarios.Util to javafx.fxml;
    opens anacarolina.funcionarios.Controller to javafx.fxml;

    exports anacarolina.funcionarios;
    exports anacarolina.funcionarios.Util;
    exports anacarolina.funcionarios.Controller to javafx.fxml;
}
