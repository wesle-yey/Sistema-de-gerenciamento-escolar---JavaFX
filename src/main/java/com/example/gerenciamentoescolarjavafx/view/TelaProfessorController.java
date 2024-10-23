package com.example.gerenciamentoescolarjavafx.view;

import com.example.gerenciamentoescolarjavafx.dao.ProfessorDAO;
import com.example.gerenciamentoescolarjavafx.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

import static javafx.application.Application.launch;

public class TelaProfessorController extends TelaGenerica<Professor> {

    private final ObservableList<Professor> listaProfessores = FXCollections.observableArrayList();
    ProfessorDAO dao = new ProfessorDAO();

    private final TableView<Professor> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(construirTela(primaryStage));
        primaryStage.setTitle("Gerenciar Professores");

        Scene scene = primaryStage.getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.show();

        configurarColunas();  // Configura as colunas da tabela
        carregarDados();  // Carrega os dados da tabela
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
        TableColumn<Professor, Integer> idColuna = new TableColumn<>("ID");
        idColuna.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Professor, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Professor, String> especializacaoColuna = new TableColumn<>("Especialização");
        especializacaoColuna.setCellValueFactory(new PropertyValueFactory<>("especializacao"));

        TableColumn<Professor, String> departamentoColuna = new TableColumn<>("Departamento");
        departamentoColuna.setCellValueFactory(new PropertyValueFactory<>("departamento"));

        tableView.getColumns().addAll(idColuna, nomeColuna, especializacaoColuna, departamentoColuna);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarDados() {
        tableView.setItems(dao.buscarTodos());
    }

    @Override
    public void adicionar() {
        Stage janelaAdicionar = new Stage();
        janelaAdicionar.initModality(Modality.APPLICATION_MODAL);
        janelaAdicionar.setTitle("Adicionar Professor");

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();

        Label labelId = new Label("ID:");
        TextField campoId = new TextField();

        Label labelEspecializacao = new Label("Especialização:");
        TextField campoEspecializacao = new TextField();

        Label labelDepartamento = new Label("Departamento:");
        TextField campoDepartamento = new TextField();

        Button btnAdicionar = new Button("Adicionar");
        Button btnCancelar = new Button("Cancelar");

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(labelNome, 0, 0);
        layout.add(campoNome, 1, 0);
        layout.add(labelId, 0, 1);
        layout.add(campoId, 1, 1);
        layout.add(labelEspecializacao, 0, 2);
        layout.add(campoEspecializacao, 1, 2);
        layout.add(labelDepartamento, 0, 3);
        layout.add(campoDepartamento, 1, 3);

        layout.add(btnAdicionar, 0, 4);
        layout.add(btnCancelar, 1, 4);

        btnAdicionar.setOnAction(e -> {
            String nome = campoNome.getText();
            int id = Integer.parseInt(campoId.getText());
            String especializacao = campoEspecializacao.getText();
            String departamento = campoDepartamento.getText();

            Professor novoProfessor = new Professor(nome, id, especializacao, departamento);

            if (dao.adicionar(novoProfessor)) {
                listaProfessores.add(novoProfessor);
                carregarDados();
            } else {
                System.out.println("Erro ao adicionar professor.");
            }

            janelaAdicionar.close();
        });

        btnCancelar.setOnAction(e -> janelaAdicionar.close());

        Scene scene = new Scene(layout, 300, 250);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        janelaAdicionar.setScene(scene);
        janelaAdicionar.showAndWait();
    }

    @Override
    public void editar() {
        Professor professorSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (professorSelecionado != null) {
            Stage janelaEditar = new Stage();
            janelaEditar.initModality(Modality.APPLICATION_MODAL);
            janelaEditar.setTitle("Editar Professor");

            Label labelNome = new Label("Nome:");
            TextField campoNome = new TextField(professorSelecionado.getNome());

            Label labelId = new Label("ID (não editável):");
            TextField campoId = new TextField(String.valueOf(professorSelecionado.getId()));
            campoId.setDisable(true);  // ID não editável

            Label labelEspecializacao = new Label("Especialização:");
            TextField campoEspecializacao = new TextField(professorSelecionado.getEspecializacao());

            Label labelDepartamento = new Label("Departamento:");
            TextField campoDepartamento = new TextField(professorSelecionado.getDepartamento());

            Button btnAtualizar = new Button("Atualizar");
            Button btnCancelar = new Button("Cancelar");

            GridPane layout = new GridPane();
            layout.setPadding(new Insets(10));
            layout.setHgap(10);
            layout.setVgap(10);

            layout.add(labelNome, 0, 0);
            layout.add(campoNome, 1, 0);
            layout.add(labelId, 0, 1);
            layout.add(campoId, 1, 1);
            layout.add(labelEspecializacao, 0, 2);
            layout.add(campoEspecializacao, 1, 2);
            layout.add(labelDepartamento, 0, 3);
            layout.add(campoDepartamento, 1, 3);

            layout.add(btnAtualizar, 0, 4);
            layout.add(btnCancelar, 1, 4);

            btnAtualizar.setOnAction(e -> {
                professorSelecionado.setNome(campoNome.getText());
                professorSelecionado.setEspecializacao(campoEspecializacao.getText());
                professorSelecionado.setDepartamento(campoDepartamento.getText());

                if (dao.atualizar(professorSelecionado)) {
                    carregarDados();
                } else {
                    System.out.println("Erro ao atualizar professor.");
                }

                janelaEditar.close();
            });

            btnCancelar.setOnAction(e -> janelaEditar.close());

            Scene scene = new Scene(layout, 300, 250);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
            janelaEditar.setScene(scene);
            janelaEditar.showAndWait();
        }
    }

    @Override
    public void remover() {
        Professor professorSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (professorSelecionado != null && dao.remover(professorSelecionado.getId())) {
            listaProfessores.remove(professorSelecionado);
            carregarDados();
        } else {
            System.out.println("Erro ao remover professor.");
        }
    }
}
