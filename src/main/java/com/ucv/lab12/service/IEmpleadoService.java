package com.ucv.lab12.service;

import com.ucv.lab12.model.Empleado;
import java.util.List;

public interface IEmpleadoService {
    String registrarDocente(Empleado empleado);
    List<Empleado> obtenerTodos(); // Aseguramos que devuelva List<Empleado>
}