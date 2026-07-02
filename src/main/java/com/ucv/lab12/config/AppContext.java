package com.ucv.lab12.config;

import com.ucv.lab12.controller.*;
import com.ucv.lab12.repository.*;
import com.ucv.lab12.service.*;

public class AppContext {

    private static AppContext instance;

    private final DatabaseConfig dbConfig;
    private final IDistribuidorRepository  distribuidorRepository;
    private final IDistribuidorService     distribuidorService;
    private final IVideojuegoRepository    videojuegoRepository;
    private final IVideojuegoService       videojuegoService;

    private AppContext() {
        this.dbConfig               = new DatabaseConfig();
        this.distribuidorRepository = new DistribuidorRepository(dbConfig);
        this.distribuidorService    = new DistribuidorService(distribuidorRepository);
        this.videojuegoRepository   = new VideojuegoRepository(dbConfig);
        this.videojuegoService      = new VideojuegoService(videojuegoRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    public Object getController(Class<?> type) {
        if (type == DistribuidorController.class)     return new DistribuidorController(distribuidorService);
        if (type == DistribuidorFormController.class) return new DistribuidorFormController(distribuidorService);
        if (type == VideojuegoController.class)       return new VideojuegoController(videojuegoService, distribuidorService);
        if (type == VideojuegoFormController.class)   return new VideojuegoFormController(videojuegoService, distribuidorService);

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public IDistribuidorService getDistribuidorService() { return distribuidorService; }
    public IVideojuegoService getVideojuegoService()     { return videojuegoService; }

    public void destroy() { dbConfig.close(); }
}