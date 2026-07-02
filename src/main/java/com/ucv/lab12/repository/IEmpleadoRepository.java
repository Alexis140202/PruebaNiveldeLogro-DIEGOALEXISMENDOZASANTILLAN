package com.ucv.lab12.repository;

import com.ucv.lab12.model.Empleado;
import java.util.List;

public interface IEmpleadoRepository {
    boolean registrar(Empleado empleado);
    List<Empleado> listarTodos();
}