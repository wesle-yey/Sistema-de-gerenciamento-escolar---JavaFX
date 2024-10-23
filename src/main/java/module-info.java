module com.example.gerenciamentoescolarjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;

    // Exportando o pacote viewControllers para JavaFX
    opens com.example.gerenciamentoescolarjavafx.view to javafx.graphics;

    exports com.example.gerenciamentoescolarjavafx.view;
    exports com.example.gerenciamentoescolarjavafx.model;
}
