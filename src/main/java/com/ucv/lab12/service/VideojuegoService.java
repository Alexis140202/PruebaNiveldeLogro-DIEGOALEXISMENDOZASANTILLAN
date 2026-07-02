package com.ucv.lab12.service;

import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.repository.IVideojuegoRepository;
import java.util.List;

public class VideojuegoService implements IVideojuegoService {

    private final IVideojuegoRepository repository;

    public VideojuegoService(IVideojuegoRepository repository) {
        this.repository = repository;
    }

    @Override public List<Videojuego> listar() { return repository.findAll(); }
    @Override public List<Videojuego> buscar(String n, String c) { return repository.findByFilters(n, c); }

    @Override
    public void crear(Videojuego v) {
        validar(v);
        repository.save(v);
    }

    @Override
    public void actualizar(Videojuego v) {
        validar(v);
        repository.update(v);
    }

    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    private void validar(Videojuego v) {
        if (v.getNombre() == null || v.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El Nombre del videojuego es obligatorio.");
        if (v.getConsola() == null || v.getConsola().trim().isEmpty())
            throw new IllegalArgumentException("La Consola es obligatoria.");
        if (v.getIdDistribuidor() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un Distribuidor válido.");
    }
}