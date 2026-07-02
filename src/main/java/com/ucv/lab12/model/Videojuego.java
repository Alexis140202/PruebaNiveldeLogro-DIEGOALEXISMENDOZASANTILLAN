package com.ucv.lab12.model;

import javafx.beans.property.*;

public class Videojuego {
    private final IntegerProperty idVideojuego    = new SimpleIntegerProperty();
    private final StringProperty  consola         = new SimpleStringProperty();
    private final StringProperty  nombre          = new SimpleStringProperty();
    private final StringProperty  genero          = new SimpleStringProperty();
    private final StringProperty  clasificacion   = new SimpleStringProperty();
    private final StringProperty  descripcion     = new SimpleStringProperty();
    private final IntegerProperty idDesarrollador = new SimpleIntegerProperty();
    private final IntegerProperty idDistribuidor  = new SimpleIntegerProperty();
    private final BooleanProperty seleccionado   = new SimpleBooleanProperty(false);

    // Propiedad auxiliar para mostrar el nombre del distribuidor en la tabla
    private final StringProperty  nombreDistribuidor = new SimpleStringProperty();

    public Videojuego() {}

    public Videojuego(int idVideojuego, String consola, String nombre, String genero,
                      String clasificacion, String descripcion, int idDesarrollador, int idDistribuidor) {
        setIdVideojuego(idVideojuego);
        setConsola(consola);
        setNombre(nombre);
        setGenero(genero);
        setClasificacion(clasificacion);
        setDescripcion(descripcion);
        setIdDesarrollador(idDesarrollador);
        setIdDistribuidor(idDistribuidor);
    }

    public IntegerProperty idVideojuegoProperty()    { return idVideojuego; }
    public StringProperty  consolaProperty()         { return consola; }
    public StringProperty  nombreProperty()          { return nombre; }
    public StringProperty  generoProperty()          { return genero; }
    public StringProperty  clasificacionProperty()   { return clasificacion; }
    public StringProperty  descripcionProperty()     { return descripcion; }
    public IntegerProperty idDesarrolladorProperty() { return idDesarrollador; }
    public IntegerProperty idDistribuidorProperty()  { return idDistribuidor; }
    public BooleanProperty seleccionadoProperty()   { return seleccionado; }
    public StringProperty  nombreDistribuidorProperty() { return nombreDistribuidor; }

    public int    getIdVideojuego()    { return idVideojuego.get(); }
    public String getConsola()         { return consola.get(); }
    public String getNombre()          { return nombre.get(); }
    public String getGenero()          { return genero.get(); }
    public String getClasificacion()   { return clasificacion.get(); }
    public String getDescripcion()     { return descripcion.get(); }
    public int    getIdDesarrollador() { return idDesarrollador.get(); }
    public int    getIdDistribuidor()  { return idDistribuidor.get(); }
    public boolean isSeleccionado()    { return seleccionado.get(); }
    public String getNombreDistribuidor() { return nombreDistribuidor.get(); }

    public void setIdVideojuego(int v)    { idVideojuego.set(v); }
    public void setConsola(String v)         { consola.set(v); }
    public void setNombre(String v)          { nombre.set(v); }
    public void setGenero(String v)          { genero.set(v); }
    public void setClasificacion(String v)   { clasificacion.set(v); }
    public void setDescripcion(String v)     { descripcion.set(v); }
    public void setIdDesarrollador(int v) { idDesarrollador.set(v); }
    public void setIdDistribuidor(int v)  { idDistribuidor.set(v); }
    public void setSeleccionado(boolean v)  { seleccionado.set(v); }
    public void setNombreDistribuidor(String v) { nombreDistribuidor.set(v); }
}