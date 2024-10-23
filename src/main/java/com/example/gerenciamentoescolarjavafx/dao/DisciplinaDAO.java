package com.example.gerenciamentoescolarjavafx.dao;

import com.example.gerenciamentoescolarjavafx.database.DatabaseConnection;
import com.example.gerenciamentoescolarjavafx.model.Disciplina;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DisciplinaDAO {

    private static final Logger log = LoggerFactory.getLogger(DisciplinaDAO.class);

    // Método para buscar todas as disciplinas
    public ObservableList<Disciplina> buscarTodas() {
        ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();
        String sql = "SELECT * FROM disciplina";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setCodigo(resultSet.getString("codigo_disciplina"));
                disciplina.setNomeDisciplina(resultSet.getString("nome"));
                disciplina.setCargaHoraria(resultSet.getInt("carga_horaria"));
                disciplina.setEmenta(resultSet.getString("ementa"));

                disciplinas.add(disciplina);
            }
        } catch (SQLException e) {
            log.error("e: ", e);
        }

        return disciplinas;
    }

    // Método para adicionar uma nova disciplina
    public boolean adicionar(Disciplina disciplina) {
        String sql = "INSERT INTO disciplina (codigo_disciplina, nome, carga_horaria, ementa) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, disciplina.getCodigo());
            preparedStatement.setString(2, disciplina.getNomeDisciplina());
            preparedStatement.setInt(3, disciplina.getCargaHoraria());
            preparedStatement.setString(4, disciplina.getEmenta());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("e: ", e);
        }

        return false;
    }

    // Método para remover uma disciplina pelo código
    public boolean remover(String codigo) {
        String sql = "DELETE FROM disciplina WHERE codigo_disciplina = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, codigo);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("e: ", e);
        }

        return false;
    }

    // Método para atualizar uma disciplina existente
    public boolean atualizar(Disciplina disciplina) {
        String sql = "UPDATE disciplina SET nome = ?, carga_horaria = ?, ementa = ? WHERE codigo_disciplina = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, disciplina.getNomeDisciplina());
            preparedStatement.setInt(2, disciplina.getCargaHoraria());
            preparedStatement.setString(3, disciplina.getEmenta());
            preparedStatement.setString(4, disciplina.getCodigo());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("e: ", e);
        }

        return false;
    }
}
