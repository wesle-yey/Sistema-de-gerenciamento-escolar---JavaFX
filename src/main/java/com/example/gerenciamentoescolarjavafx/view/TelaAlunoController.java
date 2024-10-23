package com.example.gerenciamentoescolarjavafx.view;

import com.example.gerenciamentoescolarjavafx.dao.AlunoDAO;
import com.example.gerenciamentoescolarjavafx.model.Aluno;
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

import java.time.LocalDate;
import java.util.Objects;

import static javafx.application.Application.launch;

public class TelaAlunoController extends TelaGenerica<Aluno> {

    private final ObservableList<Aluno> listaAlunos = FXCollections.observableArrayList();
    AlunoDAO dao = new AlunoDAO();

    private final TableView<Aluno> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(construirTela(primaryStage));
        primaryStage.setTitle("Gerenciar Alunos");

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
        btnRemover.setId("btnRemover");qq
        Button btnEditar = new Button("Editar");

        btnAdicionar.setOnAction(_ -> adicionar());
        btnRemover.setOnAction(_ -> remover());
        btnEditar.setOnAction(_ -> editar());

        HBox hboxBotoes = new HBox(10, btnAdicionar, btnRemover, btnEditar);
        hboxBotoes.setPadding(new Insets(10, 0, 0, 0));
        hboxBotoes.setStyle("-fx-alignment: center");

        rootLayout.getChildren().addAll(tableView, hboxBotoes);

        return new Scene(rootLayout, 600, 400);
    }

    private void configurarColunas() {
        TableColumn<Aluno, Integer> matriculaColuna = new TableColumn<>("Matrícula");
        matriculaColuna.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        TableColumn<Aluno, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Aluno, LocalDate> dataNascimentoColuna = new TableColumn<>("Data de Nascimento");
        dataNascimentoColuna.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        TableColumn<Aluno, String> enderecoColuna = new TableColumn<>("Endereço");
        enderecoColuna.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tableView.getColumns().addAll(matriculaColuna, nomeColuna, dataNascimentoColuna, enderecoColuna);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarDados() {
        tableView.setItems(dao.buscarTodos());
    }

    @Override
    public void adicionar() {
        Stage janelaAdicionar = new Stage();
        janelaAdicionar.initModality(Modality.APPLICATION_MODAL);
        janelaAdicionar.setTitle("Adicionar Aluno");

        Label labelMatricula = new Label("Matrícula:");
        TextField campoMatricula = new TextField();

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();

        Label labelDataNascimento = new Label("Data de Nascimento:");
        DatePicker campoDataNascimento = new DatePicker();

        Label labelEndereco = new Label("Endereço:");
        TextField campoEndereco = new TextField();

        Button btnAdicionar = new Button("Adicionar");
        Button btnCancelar = new Button("Cancelar");

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(labelMatricula, 0, 0);
        layout.add(campoMatricula, 1, 0);
        layout.add(labelNome, 0, 1);
        layout.add(campoNome, 1, 1);
        layout.add(labelDataNascimento, 0, 2);
        layout.add(campoDataNascimento, 1, 2);
        layout.add(labelEndereco, 0, 3);
        layout.add(campoEndereco, 1, 3);
        layout.add(btnAdicionar, 0, 4);
        layout.add(btnCancelar, 1, 4);

        btnAdicionar.setOnAction(_ -> {
            int matricula = Integer.parseInt(campoMatricula.getText());
            String nome = campoNome.getText();
            LocalDate dataNascimento = campoDataNascimento.getValue();
            String endereco = campoEndereco.getText();

            Aluno novoAluno = new Aluno(matricula, nome, dataNascimento, endereco);

            if (dao.adicionar(novoAluno)) {
                listaAlunos.add(novoAluno);
                carregarDados();
            }

            janelaAdicionar.close();
        });

        btnCancelar.setOnAction(_ -> janelaAdicionar.close());

        Scene scene = new Scene(layout, 400, 300);
        janelaAdicionar.setScene(scene);
        janelaAdicionar.showAndWait();
    }

    @Override
    public void editar() {
        Aluno alunoSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (alunoSelecionado != null) {
            Stage janelaEditar = new Stage();
            janelaEditar.initModality(Modality.APPLICATION_MODAL);
            janelaEditar.setTitle("Editar Aluno");

            Label labelMatricula = new Label("Matrícula (não editável):");
            TextField campoMatricula = new TextField(String.valueOf(alunoSelecionado.getMatricula()));
            campoMatricula.setDisable(true); // Matrícula não pode ser editada

            Label labelNome = new Label("Nome:");
            TextField campoNome = new TextField(alunoSelecionado.getNome());

            Label labelDataNascimento = new Label("Data de Nascimento:");
            DatePicker campoDataNascimento = new DatePicker(alunoSelecionado.getDataNascimento());

            Label labelEndereco = new Label("Endereço:");
            TextField campoEndereco = new TextField(alunoSelecionado.getEndereco());

            Button btnSalvar = new Button("Salvar");
            Button btnCancelar = new Button("Cancelar");

            GridPane layout = new GridPane();
            layout.setPadding(new Insets(10));
            layout.setHgap(10);
            layout.setVgap(10);

            layout.add(labelMatricula, 0, 0);
            layout.add(campoMatricula, 1, 0);
            layout.add(labelNome, 0, 1);
            layout.add(campoNome, 1, 1);
            layout.add(labelDataNascimento, 0, 2);
            layout.add(campoDataNascimento, 1, 2);
            layout.add(labelEndereco, 0, 3);
            layout.add(campoEndereco, 1, 3);
            layout.add(btnSalvar, 0, 4);
            layout.add(btnCancelar, 1, 4);

            btnSalvar.setOnAction(_ -> {
                alunoSelecionado.setNome(campoNome.getText());
                alunoSelecionado.setDataNascimento(campoDataNascimento.getValue());
                alunoSelecionado.setEndereco(campoEndereco.getText());

                if (dao.atualizar(alunoSelecionado)) {
                    carregarDados(); // Atualiza a tabela após a edição
                }

                janelaEditar.close();
            });

            btnCancelar.setOnAction(_ -> janelaEditar.close());

            Scene scene = new Scene(layout, 400, 300);
            janelaEditar.setScene(scene);
            janelaEditar.showAndWait();
        }
    }

    @Override
    public void remover() {
        Aluno alunoSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (alunoSelecionado != null) {
            if (dao.remover(alunoSelecionado.getMatricula())) {
                listaAlunos.remove(alunoSelecionado);
                carregarDados(); // Atualiza a tabela após a remoção
            }
        }
    }
}
