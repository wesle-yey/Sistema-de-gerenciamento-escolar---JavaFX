package com.example.gerenciamentoescolarjavafx.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class TelaInicialController extends Application {

    private Stage stageAluno;
    private Stage stageDisciplina;
    private Stage stageProfessor;
    private Stage stageTurma;

    @Override
    public void start(Stage primaryStage) {
        TelaInicialLayout layout = new TelaInicialLayout();

        // Setando a tela inicial
        Scene scene = layout.construirTela(primaryStage);

        // Adicionando o arquivo CSS
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Gerenciamento escolar");
        primaryStage.show();

    // Ações para os botões
        layout.getBtnDisciplina().setOnAction(e -> abrirTelaDisciplina());
        layout.getBtnAluno().setOnAction(e -> abrirTelaAluno());
        layout.getBtnProfessor().setOnAction(e -> abrirTelaProfessor());
        layout.getBtnTurma().setOnAction(e -> abrirTelaMatricula());
        layout.getBtnSair().setOnAction(e -> fecharAplicacao(primaryStage));

        // Fechando todas as janelas ao fechar a janela principal
        primaryStage.setOnCloseRequest(e -> fecharAplicacao(primaryStage));
    }

    private void abrirTelaDisciplina() {
        if (stageDisciplina == null) {
            stageDisciplina = new Stage();
            TelaDisciplinaController telaDisciplina = new TelaDisciplinaController();
            telaDisciplina.start(stageDisciplina);
            stageDisciplina.setOnCloseRequest(e -> stageDisciplina = null); // Limpar a referência ao fechar
        } else {
            stageDisciplina.toFront(); // Trazer a tela para frente
        }
    }

    private void abrirTelaAluno() {
        if (stageAluno == null) {
            stageAluno = new Stage();
            TelaAlunoController telaAluno = new TelaAlunoController();
            telaAluno.start(stageAluno);
            stageAluno.setOnCloseRequest(e -> stageAluno = null); // Limpar a referência ao fechar
        } else {
            stageAluno.toFront(); // Trazer a tela para frente
        }
    }

    private void abrirTelaProfessor() {
        if (stageProfessor == null) {
            stageProfessor = new Stage();
            TelaProfessorController telaProfessor = new TelaProfessorController();
            telaProfessor.start(stageProfessor);
            stageProfessor.setOnCloseRequest(e -> stageProfessor = null); // Limpar a referência ao fechar
        } else {
            stageProfessor.toFront(); // Trazer a tela para frente
        }
    }

    private void abrirTelaMatricula() {
        if (stageTurma == null) {
            stageTurma = new Stage();
            TelaTurmaController telaTurma = new TelaTurmaController();
            telaTurma.start(stageTurma);
            stageTurma.setOnCloseRequest(e -> stageTurma = null); // Limpar a referência ao fechar
        } else {
            stageTurma.toFront(); // Trazer a tela para frente
        }
    }

    private void fecharAplicacao(Stage primaryStage) {
        // Fechar todas as janelas abertas
        if (stageDisciplina != null) {
            stageDisciplina.close();
        }
        if (stageAluno != null) {
            stageAluno.close();
        }
        if (stageProfessor != null) {
            stageProfessor.close();
        }
        if (stageTurma != null) {
            stageTurma.close();
        }
        primaryStage.close(); // Fechar a janela principal
    }

    public static void main(String[] args) {
        launch(args);
    }
}
