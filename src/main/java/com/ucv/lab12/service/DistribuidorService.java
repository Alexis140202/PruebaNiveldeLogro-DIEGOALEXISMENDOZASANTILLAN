package com.ucv.lab12.service;

import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.repository.IDistribuidorRepository;

import java.util.List;

public class DistribuidorService implements IDistribuidorService {

    private final IDistribuidorRepository repository;

    public DistribuidorService(IDistribuidorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Distribuidor> listar() {
        return repository.findAll();
    }

    @Override
    public List<Distribuidor> buscar(String nombre) {
        return repository.findByFilters(nombre);
    }

    @Override
    public void crear(Distribuidor distribuidor) {
        validar(distribuidor);
        repository.save(distribuidor);
    }

    @Override
    public void actualizar(Distribuidor distribuidor) {
        validar(distribuidor);
        repository.update(distribuidor);
    }

    @Override
    public void eliminar(int id) {
        repository.delete(id);
    }

    @Override
    public void eliminarSeleccionados(List<Integer> ids) {
        repository.deleteAll(ids);
    }

    @Override
    public void validar(Distribuidor d) {
        if (d.getNombre() == null || d.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El Nombre del distribuidor es obligatorio.");
        }
        if (d.getNombre().trim().length() > 45) {
            throw new IllegalArgumentException("El Nombre no puede superar los 45 caracteres.");
        }
    }
}