package com.example.gerenciamentoescolarjavafx.view;

import com.example.gerenciamentoescolarjavafx.dao.DisciplinaDAO;
import com.example.gerenciamentoescolarjavafx.model.Disciplina;
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

public class TelaDisciplinaController extends TelaGenerica<Disciplina> {

    private final ObservableList<Disciplina> listaDisciplinas = FXCollections.observableArrayList();
    DisciplinaDAO dao = new DisciplinaDAO();

    private final TableView<Disciplina> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(construirTela(primaryStage));
        primaryStage.setTitle("Gerenciar Disciplinas");

        Scene scene = primaryStage.getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.show();

        configurarColunas();  // Configura as colunas da tabela
        carregarDados();  // Carrega os dados da tabela
    }

    @Override
    public Scene construirTela(Stage stage) {
        // Criando a VBox para organizar a TableView e botões
        // VBox para organizar a interface
        VBox rootLayout = new VBox();
        rootLayout.setPadding(new Insets(10));
        rootLayout.setSpacing(10); // Espaçamento entre elementos

        // Criando os botões "Adicionar", "Remover" e "Editar"
        Button btnAdicionar = new Button("Adicionar");
        Button btnRemover = new Button("Remover");
        btnRemover.setId("btnRemover");
        Button btnEditar = new Button("Editar");

        // Adicionando ações aos botões
        btnAdicionar.setOnAction(e -> adicionar());
        btnRemover.setOnAction(e -> remover());
        btnEditar.setOnAction(e -> editar());

        // Criando um HBox para organizar os botões
        HBox hboxBotoes = new HBox(10, btnAdicionar, btnRemover, btnEditar);
        hboxBotoes.setPadding(new Insets(10, 0, 0, 0));
        hboxBotoes.setStyle("-fx-alignment: center"); // Centraliza os botões

        // Adiciona a TableView e os botões na VBox
        rootLayout.getChildren().addAll(tableView, hboxBotoes);

        // Criação da cena com a VBox como raiz
        // Tamanho inicial da tela
        return new Scene(rootLayout, 600, 400);
    }

    private void configurarColunas() {
        // Coluna para o código da disciplina
        TableColumn<Disciplina, Integer> codigoColuna = new TableColumn<>("Código");
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        // Coluna para o nome da disciplina
        TableColumn<Disciplina, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nomeDisciplina"));

        // Coluna para a carga horária
        TableColumn<Disciplina, Integer> cargaHorariaColuna = new TableColumn<>("Carga Horária");
        cargaHorariaColuna.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));

        // Coluna para a ementa
        TableColumn<Disciplina, String> ementaColuna = new TableColumn<>("Ementa");
        ementaColuna.setCellValueFactory(new PropertyValueFactory<>("ementa"));

        // Adicionando as colunas à TableView
        tableView.getColumns().addAll(codigoColuna, nomeColuna, cargaHorariaColuna, ementaColuna);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void carregarDados() {
        // Carregar os dados na TableView
        tableView.setItems(dao.buscarTodas());
    }

    @Override
    public void adicionar() {
        System.out.println("Clicado no botão Adicionar");

        Stage janelaAdicionar = new Stage();
        janelaAdicionar.initModality(Modality.APPLICATION_MODAL);
        janelaAdicionar.setTitle("Adicionar Disciplina");

        // Criando Labels e TextFields
        Label labelTitulo = new Label("Adicionar Disciplina");
        Label labelCodigo = new Label("Código:");
        TextField campoCodigo = new TextField();

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();

        Label labelCargaHoraria = new Label("Carga Horária:");
        TextField campoCargaHoraria = new TextField();

        Label labelEmenta = new Label("Ementa:");
        TextField campoEmenta = new TextField();

        // Botões
        Button btnAdicionar = new Button("Adicionar");
        Button btnCancelar = new Button("Cancelar");

        /* Layout da tela */
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(labelTitulo, 0, 0, 2, 1);
        addLayout(labelCodigo, campoCodigo, labelNome, campoNome, labelCargaHoraria, campoCargaHoraria, labelEmenta, campoEmenta, layout);

        layout.add(btnAdicionar, 0, 5);
        layout.add(btnCancelar, 1, 5);

        // Ação do botão "Adicionar"
        btnAdicionar.setOnAction(_ -> {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();
            int cargaHoraria = Integer.parseInt(campoCargaHoraria.getText());
            String ementa = campoEmenta.getText();

            Disciplina novaDisciplina = new Disciplina(codigo, nome, cargaHoraria, ementa);

            if (dao.adicionar(novaDisciplina)) {
                listaDisciplinas.add(novaDisciplina);
                carregarDados();
            } else {
                System.out.println("Erro ao adicionar disciplina.");
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
        Disciplina disciplinaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (disciplinaSelecionada != null) {
            Stage janelaEditar = new Stage();
            janelaEditar.initModality(Modality.APPLICATION_MODAL);
            janelaEditar.setTitle("Editar Disciplina");

            Label labelTitulo = new Label("Editar Disciplina");
            Label labelCodigo = new Label("Código (não editável):");
            TextField campoCodigo = new TextField(disciplinaSelecionada.getCodigo());
            campoCodigo.setEditable(false);
            campoCodigo.getStyleClass().add("text-field-disabled");

            Label labelNome = new Label("Nome:");
            TextField campoNome = new TextField(disciplinaSelecionada.getNomeDisciplina());

            Label labelCargaHoraria = new Label("Carga Horária:");
            TextField campoCargaHoraria = new TextField(String.valueOf(disciplinaSelecionada.getCargaHoraria()));

            Label labelEmenta = new Label("Ementa:");
            TextField campoEmenta = new TextField(disciplinaSelecionada.getEmenta());

            Button btnSalvar = new Button("Salvar");
            Button btnCancelar = new Button("Cancelar");

            GridPane layout = new GridPane();
            layout.setPadding(new Insets(10));
            layout.setHgap(10);
            layout.setVgap(10);

            layout.add(labelTitulo, 0, 0, 2, 1);
            addLayout(labelCodigo, campoCodigo, labelNome, campoNome, labelCargaHoraria, campoCargaHoraria, labelEmenta, campoEmenta, layout);

            layout.add(btnSalvar, 0, 5);
            layout.add(btnCancelar, 1, 5);

            btnSalvar.setOnAction(_ -> {
                String nome = campoNome.getText();
                int cargaHoraria = Integer.parseInt(campoCargaHoraria.getText());
                String ementa = campoEmenta.getText();

                disciplinaSelecionada.setNomeDisciplina(nome);
                disciplinaSelecionada.setCargaHoraria(cargaHoraria);
                disciplinaSelecionada.setEmenta(ementa);

                if (dao.atualizar(disciplinaSelecionada)) {
                    carregarDados();
                    System.out.println("Disciplina editada com sucesso.");
                } else {
                    System.out.println("Erro ao editar a disciplina.");
                }

                janelaEditar.close();
            });

            btnCancelar.setOnAction(_ -> janelaEditar.close());

            Scene scene = new Scene(layout, 400, 250);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
            janelaEditar.setScene(scene);
            janelaEditar.showAndWait();
        } else {
            System.out.println("Nenhuma disciplina selecionada para edição.");
        }
    }

    @Override
    public void remover() {
        Disciplina disciplinaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (disciplinaSelecionada != null) {
            if (dao.remover(disciplinaSelecionada.getCodigo())) {
                listaDisciplinas.remove(disciplinaSelecionada);
                carregarDados();
                System.out.println("Disciplina removida com sucesso.");
            } else {
                System.out.println("Erro ao remover a disciplina.");
            }
        } else {
            System.out.println("Nenhuma disciplina selecionada para remoção.");
        }
    }

    private void addLayout(Label labelCodigo, TextField campoCodigo, Label labelNome, TextField campoNome, Label labelCargaHoraria, TextField campoCargaHoraria, Label labelEmenta, TextField campoEmenta, GridPane layout) {
        layout.add(labelCodigo, 0, 1);
        layout.add(campoCodigo, 1, 1);
        layout.add(labelNome, 0, 2);
        layout.add(campoNome, 1, 2);
        layout.add(labelCargaHoraria, 0, 3);
        layout.add(campoCargaHoraria, 1, 3);
        layout.add(labelEmenta, 0, 4);
        layout.add(campoEmenta, 1, 4);
    }
}
