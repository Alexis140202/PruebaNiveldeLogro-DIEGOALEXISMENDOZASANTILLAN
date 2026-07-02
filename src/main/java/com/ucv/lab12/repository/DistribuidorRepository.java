package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Distribuidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribuidorRepository implements IDistribuidorRepository, AutoCloseable {

    private final DatabaseConfig dbConfig;

    public DistribuidorRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Distribuidor> findAll() {
        return findByFilters("");
    }

    // Filtramos únicamente por Nombre, tal como indica el diagrama de la guía
    public List<Distribuidor> findByFilters(String nombre) {
        String sql = """
                SELECT idDistribuidor, Nombre
                FROM Distribuidor
                WHERE Nombre LIKE ?
                ORDER BY Nombre
                """;
        List<Distribuidor> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + (nombre == null ? "" : nombre.trim()) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Distribuidor(
                            rs.getInt("idDistribuidor"),
                            rs.getString("Nombre")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar distribuidores", e);
        }
        return list;
    }

    @Override
    public void save(Distribuidor d) {
        String sql = "INSERT INTO Distribuidor (Nombre) VALUES (?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar distribuidor", e);
        }
    }

    @Override
    public void update(Distribuidor d) {
        String sql = "UPDATE Distribuidor SET Nombre = ? WHERE idDistribuidor = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.setInt(2, d.getIdDistribuidor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar distribuidor", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Distribuidor WHERE idDistribuidor = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar distribuidor", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sb = new StringBuilder("DELETE FROM Distribuidor WHERE idDistribuidor IN (");
        for (int i = 0; i < ids.size(); i++) {
            sb.append(i == 0 ? "?" : ",?");
        }
        sb.append(")");

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar distribuidores en bloque", e);
        }
    }

    @Override
    public void close() {
        dbConfig.close();
    }
}