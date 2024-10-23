package com.example.gerenciamentoescolarjavafx.dao;

import com.example.gerenciamentoescolarjavafx.database.DatabaseConnection;
import com.example.gerenciamentoescolarjavafx.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class ProfessorDAO {

    private static final Logger log = LoggerFactory.getLogger(ProfessorDAO.class);

    // Método para buscar todos os professores
    public ObservableList<Professor> buscarTodos() {
        ObservableList<Professor> professores = FXCollections.observableArrayList();
        String sql = "SELECT * FROM professor";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Professor professor = new Professor(
                        resultSet.getString("nome"),
                        resultSet.getInt("id"),
                        resultSet.getString("especializacao"),
                        resultSet.getString("departamento")
                );
                professores.add(professor);
            }
        } catch (SQLException e) {
            log.error("Erro ao buscar professores: ", e);
        }

        return professores;
    }

    // Método para adicionar um novo professor
    public boolean adicionar(Professor professor) {
        String sql = "INSERT INTO professor (nome, id, especializacao, departamento) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, professor.getNome());
            preparedStatement.setInt(2, professor.getId());
            preparedStatement.setString(3, professor.getEspecializacao());
            preparedStatement.setString(4, professor.getDepartamento());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao adicionar professor: ", e);
        }

        return false;
    }

    // Método para remover um professor pelo id
    public boolean remover(int id) {
        String sql = "DELETE FROM professor WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao remover professor: ", e);
        }

        return false;
    }

    // Método para atualizar um professor existente
    public boolean atualizar(Professor professor) {
        String sql = "UPDATE professor SET nome = ?, especializacao = ?, departamento = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, professor.getNome());
            preparedStatement.setString(2, professor.getEspecializacao());
            preparedStatement.setString(3, professor.getDepartamento());
            preparedStatement.setInt(4, professor.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Erro ao atualizar professor: ", e);
        }

        return false;
    }
}
