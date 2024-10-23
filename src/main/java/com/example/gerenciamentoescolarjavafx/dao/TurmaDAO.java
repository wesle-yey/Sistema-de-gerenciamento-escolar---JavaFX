package com.example.gerenciamentoescolarjavafx.dao;

import com.example.gerenciamentoescolarjavafx.database.DatabaseConnection;
import com.example.gerenciamentoescolarjavafx.model.Turma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class TurmaDAO {

    private static final Logger log = LoggerFactory.getLogger(TurmaDAO.class);

    public ObservableList<Turma> buscarTodas() {
        ObservableList<Turma> turmas = FXCollections.observableArrayList();

        String sql = "SELECT * FROM turma";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Turma turma = new Turma(
                        rs.getString("codigo_turma"),
                        rs.getString("horario"),
                        rs.getString("sala"),
                        rs.getInt("capacidade")
                );
                turmas.add(turma);
            }

        } catch (SQLException e) {
            log.error("Erro ao buscar turmas do banco de dados", e);
        }

        return turmas;
    }

    public boolean adicionar(Turma turma) {
        String sql = "INSERT INTO turma (codigo_turma, horario, sala, capacidade) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, turma.getCodigoTurma());
            stmt.setString(2, turma.getHorario());
            stmt.setString(3, turma.getSala());
            stmt.setInt(4, turma.getCapacidade());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            log.error("Erro ao adicionar turma ao banco de dados", e);
            return false;
        }
    }

    public boolean atualizar(Turma turma) {
        String sql = "UPDATE turma SET horario = ?, sala = ?, capacidade = ? WHERE codigo_turma = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, turma.getHorario());
            stmt.setString(2, turma.getSala());
            stmt.setInt(3, turma.getCapacidade());
            stmt.setString(4, turma.getCodigoTurma());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            log.error("Erro ao atualizar turma no banco de dados", e);
            return false;
        }
    }

    public boolean remover(String codigoTurma) {
        String sql = "DELETE FROM turma WHERE codigo_turma = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoTurma);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            log.error("Erro ao remover turma do banco de dados", e);
            return false;
        }
    }
}
