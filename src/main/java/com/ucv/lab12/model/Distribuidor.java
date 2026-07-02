package com.ucv.lab12.model;

import javafx.beans.property.*;

public class Distribuidor {
    private final IntegerProperty idDistribuidor = new SimpleIntegerProperty();
    private final StringProperty  nombre         = new SimpleStringProperty();
    private final BooleanProperty seleccionado   = new SimpleBooleanProperty(false);

    public Distribuidor() {}

    public Distribuidor(int idDistribuidor, String nombre) {
        setIdDistribuidor(idDistribuidor);
        setNombre(nombre);
    }

    public IntegerProperty idDistribuidorProperty() { return idDistribuidor; }
    public StringProperty  nombreProperty()         { return nombre; }
    public BooleanProperty seleccionadoProperty()   { return seleccionado; }

    public int    getIdDistribuidor() { return idDistribuidor.get(); }
    public String getNombre()         { return nombre.get(); }
    public boolean isSeleccionado()    { return seleccionado.get(); }

    public void setIdDistribuidor(int v)   { idDistribuidor.set(v); }
    public void setNombre(String v)        { nombre.set(v); }
    public void setSeleccionado(boolean v) { seleccionado.set(v); }

    @Override
    public String toString() {
        return getNombre();
    }
}