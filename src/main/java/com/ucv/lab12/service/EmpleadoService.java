package com.ucv.lab12.service;

import com.ucv.lab12.model.Empleado;
import com.ucv.lab12.repository.EmpleadoRepository;
import com.ucv.lab12.repository.IEmpleadoRepository;
import java.util.List;

public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository repository = new EmpleadoRepository();

    @Override
    public String registrarDocente(Empleado emp) {
        if (emp.getCodEmple() == null || emp.getCodEmple().trim().isEmpty()) {
            return "El código de empleado es obligatorio.";
        }
        if (emp.getDniEmple() == null || emp.getDniEmple().length() != 8) {
            return "El DNI debe tener exactamente 8 dígitos.";
        }
        if (emp.getSueldoBasico() <= 0) {
            return "El sueldo básico debe ser mayor a cero.";
        }

        boolean exito = repository.registrar(emp);
        return exito ? "EXITO" : "Error al registrar en la base de datos.";
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return repository.listarTodos();
    }
}