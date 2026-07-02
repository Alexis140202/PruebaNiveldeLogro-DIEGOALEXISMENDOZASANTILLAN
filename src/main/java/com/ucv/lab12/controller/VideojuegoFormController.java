package com.ucv.lab12.controller;

import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.service.IDistribuidorService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VideojuegoFormController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtConsola;
    @FXML private TextField txtGenero;
    @FXML private TextField txtClasificacion;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Distribuidor> cmbDistribuidor;
    @FXML private Button btnCancelar;

    private final IVideojuegoService service;
    private final IDistribuidorService distribuidorService;
    private Videojuego videojuego;
    private Runnable onGuardar;

    public VideojuegoFormController(IVideojuegoService service, IDistribuidorService distribuidorService) {
        this.service = service;
        this.distribuidorService = distribuidorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            cmbDistribuidor.getItems().setAll(distribuidorService.listar());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se cargaron los distribuidores.");
        }
    }

    public void setVideojuego(Videojuego v) {
        this.videojuego = v;
        if (v != null) {
            lblTitulo.setText("Editar Videojuego");
            txtNombre.setText(v.getNombre());
            txtConsola.setText(v.getConsola());
            txtGenero.setText(v.getGenero());
            txtClasificacion.setText(v.getClasificacion());
            txtDescripcion.setText(v.getDescripcion());

            for (Distribuidor d : cmbDistribuidor.getItems()) {
                if (d.getIdDistribuidor() == v.getIdDistribuidor()) {
                    cmbDistribuidor.setValue(d);
                    break;
                }
            }
        } else {
            lblTitulo.setText("Nuevo Videojuego");
        }
    }

    public void setOnGuardar(Runnable callback) { this.onGuardar = callback; }

    @FXML
    private void onGuardar() {
        if (txtNombre.getText().trim().isEmpty() || cmbDistribuidor.getValue() == null) {
            AlertUtil.advertencia("Validación", "Nombre y Distribuidor son obligatorios.");
            return;
        }

        Videojuego v = videojuego != null ? videojuego : new Videojuego();
        v.setNombre(txtNombre.getText().trim());
        v.setConsola(txtConsola.getText().trim());
        v.setGenero(txtGenero.getText().trim());
        v.setClasificacion(txtClasificacion.getText().trim());
        v.setDescripcion(txtDescripcion.getText().trim());
        v.setIdDistribuidor(cmbDistribuidor.getValue().getIdDistribuidor());
        v.setIdDesarrollador(1); // Default por la estructura del diagrama

        try {
            if (videojuego == null) service.crear(v);
            else service.actualizar(v);
            if (onGuardar != null) onGuardar.run();
            ((Stage) btnCancelar.getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.error("Error", "No se guardó: " + e.getMessage());
        }
    }

    @FXML private void onCancelar() { ((Stage) btnCancelar.getScene().getWindow()).close(); }
}