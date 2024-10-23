package com.example.gerenciamentoescolarjavafx.dao;

import com.example.gerenciamentoescolarjavafx.database.DatabaseConnection;
import com.example.gerenciamentoescolarjavafx.model.Aluno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AlunoDAO {

    private static final Logger log = LoggerFactory.getLogger(AlunoDAO.class);

    // Método para buscar todos os alunos
    public ObservableList<Aluno> buscarTodos() {
        ObservableList<Aluno> alunos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM aluno";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Aluno aluno = new Aluno(
                        resultSet.getInt("matricula"),
                        resultSet.getString("nome"),
                        resultSet.getDate("data_nascimento").toLocalDate(),
                        resultSet.getString("endereco")
                );
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar alunos: ", e);
        }

        return alunos;
    }

    // Método para adicionar um novo aluno
    public boolean adicionar(Aluno aluno) {
        String sql = "INSERT INTO aluno (matricula, nome, data_nascimento, endereco) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, aluno.getMatricula());
            preparedStatement.setString(2, aluno.getNome());
            preparedStatement.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            preparedStatement.setString(4, aluno.getEndereco());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao adicionar aluno: ", e);
        }

        return false;
    }

    // Método para remover um aluno pela matrícula
    public boolean remover(int matricula) {
        String sql = "DELETE FROM aluno WHERE matricula = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, matricula);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao remover aluno: ", e);
        }

        return false;
    }

    // Método para atualizar um aluno existente
    public boolean atualizar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, endereco = ? WHERE matricula = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, aluno.getNome());
            preparedStatement.setDate(2, Date.valueOf(aluno.getDataNascimento()));
            preparedStatement.setString(3, aluno.getEndereco());
            preparedStatement.setInt(4, aluno.getMatricula());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao atualizar aluno: ", e);
        }

        return false;
    }
}
