package com.ucv.lab12.repository;

import com.ucv.lab12.model.Distribuidor;
import java.util.List;

public interface IDistribuidorRepository {
    List<Distribuidor> findAll();
    List<Distribuidor> findByFilters(String nombre); // Cambiado para recibir solo un parámetro
    void save(Distribuidor distribuidor);
    void update(Distribuidor distribuidor);
    void delete(int id);
    void deleteAll(List<Integer> ids);
}