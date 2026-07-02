package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository implements IEmpleadoRepository {

    @Override
    public boolean registrar(Empleado emp) {
        String sql = "INSERT INTO EMPLEADO (COD_EMPLE, NOMBRES, APELLIDOS, DNI_EMPLE, SUELDO_BASICO, FECHA_INGRESO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getCodEmple());
            ps.setString(2, emp.getNombres());
            ps.setString(3, emp.getApellidos());
            ps.setString(4, emp.getDniEmple());
            ps.setDouble(5, emp.getSueldoBasico());
            ps.setDate(6, new java.sql.Date(emp.getFechaIngreso().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar docente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT COD_EMPLE, NOMBRES, APELLIDOS, DNI_EMPLE, SUELDO_BASICO, FECHA_INGRESO FROM EMPLEADO";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getString("COD_EMPLE"),
                        rs.getString("NOMBRES"),
                        rs.getString("APELLIDOS"),
                        rs.getString("DNI_EMPLE"),
                        rs.getDouble("SUELDO_BASICO"),
                        rs.getDate("FECHA_INGRESO")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar docentes: " + e.getMessage());
        }
        return lista;
    }
}