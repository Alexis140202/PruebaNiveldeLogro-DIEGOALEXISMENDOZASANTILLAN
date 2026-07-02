package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Videojuego;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoRepository implements IVideojuegoRepository {

    private final DatabaseConfig dbConfig;

    public VideojuegoRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Videojuego> findAll() {
        return findByFilters("", "");
    }

    @Override
    public List<Videojuego> findByFilters(String nombre, String consola) {
        String sql = """
                SELECT v.idVideojuego, v.Consola, v.Nombre, v.Genero, v.Clasificacion, 
                       v.Descripcion, v.IDdesarrollador, v.IDdistribuidor, d.Nombre AS NombreDistribuidor
                FROM Videojuego v
                INNER JOIN Distribuidor d ON v.IDdistribuidor = d.idDistribuidor
                WHERE v.Nombre LIKE ? AND v.Consola LIKE ?
                ORDER BY v.Nombre
                """;
        List<Videojuego> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + (nombre == null ? "" : nombre.trim()) + "%");
            ps.setString(2, "%" + (consola == null ? "" : consola.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Videojuego v = new Videojuego(
                            rs.getInt("idVideojuego"),
                            rs.getString("Consola"),
                            rs.getString("Nombre"),
                            rs.getString("Genero"),
                            rs.getString("Clasificacion"),
                            rs.getString("Descripcion"),
                            rs.getInt("IDdesarrollador"),
                            rs.getInt("IDdistribuidor")
                    );
                    v.setNombreDistribuidor(rs.getString("NombreDistribuidor"));
                    list.add(v);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar videojuegos", e);
        }
        return list;
    }

    @Override
    public void save(Videojuego v) {
        String sql = """
                INSERT INTO Videojuego (Consola, Nombre, Genero, Clasificacion, Descripcion, IDdesarrollador, IDdistribuidor)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getConsola());
            ps.setString(2, v.getNombre());
            ps.setString(3, v.getGenero());
            ps.setString(4, v.getClasificacion());
            ps.setString(5, v.getDescripcion());
            ps.setInt(6, v.getIdDesarrollador());
            ps.setInt(7, v.getIdDistribuidor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar videojuego", e);
        }
    }

    @Override
    public void update(Videojuego v) {
        String sql = """
                UPDATE Videojuego
                SET Consola = ?, Nombre = ?, Genero = ?, Clasificacion = ?, Descripcion = ?, IDdesarrollador = ?, IDdistribuidor = ?
                WHERE idVideojuego = ?
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getConsola());
            ps.setString(2, v.getNombre());
            ps.setString(3, v.getGenero());
            ps.setString(4, v.getClasificacion());
            ps.setString(5, v.getDescripcion());
            ps.setInt(6, v.getIdDesarrollador());
            ps.setInt(7, v.getIdDistribuidor());
            ps.setInt(8, v.getIdVideojuego());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar videojuego", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Videojuego WHERE idVideojuego = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar videojuego", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sb = new StringBuilder("DELETE FROM Videojuego WHERE idVideojuego IN (");
        for (int i = 0; i < ids.size(); i++) sb.append(i == 0 ? "?" : ",?");
        sb.append(")");
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar bloque de videojuegos", e);
        }
    }
}