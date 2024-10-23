package com.example.gerenciamentoescolarjavafx.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaInicialLayout {

    private Button btnAluno;
    private Button btnProfessor;
    private Button btnTurma;
    private Button btnDisciplina;
    private Button btnSair;

    public Scene construirTela(Stage primaryStage) {
        btnAluno = new Button("Gerenciar Alunos");
        btnProfessor = new Button("Gerenciar Professores");
        btnTurma = new Button("Gerenciar Turmas");
        btnDisciplina = new Button("Gerenciar Disciplinas");
        btnSair = new Button("Sair");
        btnSair.setId("btnSair");

        // Configurando VBox
        VBox layout = new VBox(10, btnAluno, btnProfessor, btnDisciplina, btnTurma, btnSair);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        // Definindo a cena
        return new Scene(layout, 400, 300);
    }

    // Métodos getters para os botões
    public Button getBtnDisciplina() { return btnDisciplina; }
    public Button getBtnAluno() { return btnAluno; }
    public Button getBtnProfessor() { return btnProfessor; }
    public Button getBtnTurma() { return btnTurma; }
    public Button getBtnSair() { return btnSair; }
}

