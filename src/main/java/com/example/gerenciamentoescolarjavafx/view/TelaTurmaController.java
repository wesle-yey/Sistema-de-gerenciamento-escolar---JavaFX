package com.example.gerenciamentoescolarjavafx.view;

import com.example.gerenciamentoescolarjavafx.dao.TurmaDAO;
import com.example.gerenciamentoescolarjavafx.model.Turma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

import static javafx.application.Application.launch;

public class TelaTurmaController extends TelaGenerica<Turma> {

    private final ObservableList<Turma> listaTurmas = FXCollections.observableArrayList();
    TurmaDAO dao = new TurmaDAO();

    private final TableView<Turma> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(construirTela(primaryStage));
        primaryStage.setTitle("Gerenciar Turmas");

        Scene scene = primaryStage.getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.show();

        configurarColunas();
        carregarDados();
    }

    @Override
    public Scene construirTela(Stage stage) {
        VBox rootLayout = new VBox();
        rootLayout.setPadding(new Insets(10));
        rootLayout.setSpacing(10);

        Button btnAdicionar = new Button("Adicionar");
        Button btnRemover = new Button("Remover");
        btnRemover.setId("btnRemover");
        Button btnEditar = new Button("Editar");

        btnAdicionar.setOnAction(e -> adicionar());
        btnRemover.setOnAction(e -> remover());
        btnEditar.setOnAction(e -> editar());

        HBox hboxBotoes = new HBox(10, btnAdicionar, btnRemover, btnEditar);
        hboxBotoes.setPadding(new Insets(10, 0, 0, 0));
        hboxBotoes.setStyle("-fx-alignment: center");

        rootLayout.getChildren().addAll(tableView, hboxBotoes);

        return new Scene(rootLayout, 600, 400);
    }

    private void configurarColunas() {
        TableColumn<Turma, String> codigoColuna = new TableColumn<>("Código Turma");
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigoTurma"));

        TableColumn<Turma, String> horarioColuna = new TableColumn<>("Horário");
        horarioColuna.setCellValueFactory(new PropertyValueFactory<>("horario"));

        TableColumn<Turma, String> salaColuna = new TableColumn<>("Sala");
        salaColuna.setCellValueFactory(new PropertyValueFactory<>("sala"));

        TableColumn<Turma, Integer> capacidadeColuna = new TableColumn<>("Capacidade");
        capacidadeColuna.setCellValueFactory(new PropertyValueFactory<>("capacidade"));

        tableView.getColumns().addAll(codigoColuna, horarioColuna, salaColuna, capacidadeColuna);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarDados() {
        tableView.setItems(dao.buscarTodas());
    }

    @Override
    public void adicionar() {
        Stage janelaAdicionar = new Stage();
        janelaAdicionar.initModality(Modality.APPLICATION_MODAL);
        janelaAdicionar.setTitle("Adicionar Turma");

        Label labelCodigo = new Label("Código Turma:");
        TextField campoCodigo = new TextField();

        Label labelHorario = new Label("Horário:");
        TextField campoHorario = new TextField();

        Label labelSala = new Label("Sala:");
        TextField campoSala = new TextField();

        Label labelCapacidade = new Label("Capacidade:");
        TextField campoCapacidade = new TextField();

        Button btnAdicionar = new Button("Adicionar");
        Button btnCancelar = new Button("Cancelar");

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(labelCodigo, 0, 1);
        layout.add(campoCodigo, 1, 1);
        layout.add(labelHorario, 0, 2);
        layout.add(campoHorario, 1, 2);
        layout.add(labelSala, 0, 3);
        layout.add(campoSala, 1, 3);
        layout.add(labelCapacidade, 0, 4);
        layout.add(campoCapacidade, 1, 4);

        layout.add(btnAdicionar, 0, 5);
        layout.add(btnCancelar, 1, 5);

        btnAdicionar.setOnAction(_ -> {
            String codigo = campoCodigo.getText();
            String horario = campoHorario.getText();
            String sala = campoSala.getText();
            int capacidade = Integer.parseInt(campoCapacidade.getText());

            Turma novaTurma = new Turma(codigo, horario, sala, capacidade);

            if (dao.adicionar(novaTurma)) {
                listaTurmas.add(novaTurma);
                carregarDados();
            } else {
                System.out.println("Erro ao adicionar turma.");
            }

            janelaAdicionar.close();
        });

        btnCancelar.setOnAction(_ -> janelaAdicionar.close());

        Scene scene = new Scene(layout, 300, 250);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        janelaAdicionar.setScene(scene);
        janelaAdicionar.showAndWait();
    }

    @Override
    public void editar() {
        Turma turmaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (turmaSelecionada != null) {
            Stage janelaEditar = new Stage();
            janelaEditar.initModality(Modality.APPLICATION_MODAL);
            janelaEditar.setTitle("Editar Turma");

            Label labelCodigo = new Label("Código (não editável):");
            TextField campoCodigo = new TextField(turmaSelecionada.getCodigoTurma());
            campoCodigo.setEditable(false);

            Label labelHorario = new Label("Horário:");
            TextField campoHorario = new TextField(turmaSelecionada.getHorario());

            Label labelSala = new Label("Sala:");
            TextField campoSala = new TextField(turmaSelecionada.getSala());

            Label labelCapacidade = new Label("Capacidade:");
            TextField campoCapacidade = new TextField(String.valueOf(turmaSelecionada.getCapacidade()));

            Button btnSalvar = new Button("Salvar");
            Button btnCancelar = new Button("Cancelar");

            GridPane layout = new GridPane();
            layout.setPadding(new Insets(10));
            layout.setHgap(10);
            layout.setVgap(10);

            layout.add(labelCodigo, 0, 1);
            layout.add(campoCodigo, 1, 1);
            layout.add(labelHorario, 0, 2);
            layout.add(campoHorario, 1, 2);
            layout.add(labelSala, 0, 3);
            layout.add(campoSala, 1, 3);
            layout.add(labelCapacidade, 0, 4);
            layout.add(campoCapacidade, 1, 4);

            layout.add(btnSalvar, 0, 5);
            layout.add(btnCancelar, 1, 5);

            btnSalvar.setOnAction(_ -> {
                turmaSelecionada.setHorario(campoHorario.getText());
                turmaSelecionada.setSala(campoSala.getText());
                turmaSelecionada.setCapacidade(Integer.parseInt(campoCapacidade.getText()));

                if (dao.atualizar(turmaSelecionada)) {
                    carregarDados();
                    System.out.println("Turma editada com sucesso.");
                } else {
                    System.out.println("Erro ao editar a turma.");
                }

                janelaEditar.close();
            });

            btnCancelar.setOnAction(_ -> janelaEditar.close());

            Scene scene = new Scene(layout, 300, 250);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
            janelaEditar.setScene(scene);
            janelaEditar.showAndWait();
        } else {
            System.out.println("Nenhuma turma selecionada para edição.");
        }
    }

    @Override
    public void remover() {
        Turma turmaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (turmaSelecionada != null) {
            if (dao.remover(turmaSelecionada.getCodigoTurma())) {
                listaTurmas.remove(turmaSelecionada);
                carregarDados();
                System.out.println("Turma removida com sucesso.");
            } else {
                System.out.println("Erro ao remover a turma.");
            }
        } else {
            System.out.println("Nenhuma turma selecionada para remoção.");
        }
    }
}
