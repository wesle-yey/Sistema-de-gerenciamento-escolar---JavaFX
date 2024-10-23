package com.example.gerenciamentoescolarjavafx.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class TelaGenerica<T> {

    protected Button btnAdicionar;
    protected Button btnRemover;
    protected Button btnEditar;
    protected TableView<T> tableView;

    public Scene construirTela(Stage primaryStage) {
        // Inicializando os botões
        btnAdicionar = new Button("Adicionar");
        btnRemover = new Button("Remover");
        btnEditar = new Button("Editar");

        // Configurando o HBox para os botões
        HBox hboxBotoes = new HBox(10, btnAdicionar, btnRemover, btnEditar);
        hboxBotoes.setStyle("-fx-padding: 10; -fx-alignment: center");

        // Inicializando a TableView (ou ListView se preferir)
        tableView = new TableView<>();

        // Layout principal (BorderPane para organizar componentes)
        BorderPane layout = new BorderPane();
        layout.setTop(hboxBotoes);
        layout.setCenter(tableView);

        // Retornando a cena
        return new Scene(layout, 600, 400);
    }

    // Métodos abstratos para definir as ações dos botões
    protected abstract void adicionar();
    protected abstract void editar();
    protected abstract void remover();

    public abstract void start(Stage primaryStage);
}
