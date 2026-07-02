package com.ucv.lab12.controller;

import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.service.IDistribuidorService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DistribuidorFormController implements Initializable {

    @FXML private Label    lblTitulo;
    @FXML private TextField txtRazonSocial; // Usamos el ID existente del FXML para el Nombre
    @FXML private Label     lblRazonSocialError;
    @FXML private Button    btnCancelar;

    private final IDistribuidorService service;
    private Distribuidor distribuidor;
    private Runnable onGuardar;

    public DistribuidorFormController(IDistribuidorService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblRazonSocialError.setVisible(false);
        limitarLongitud(txtRazonSocial, 45);

        txtRazonSocial.textProperty().addListener((o, v1, v2) ->
                lblRazonSocialError.setVisible(false));
    }

    public void setDistribuidor(Distribuidor d) {
        this.distribuidor = d;
        if (d != null) {
            lblTitulo.setText("Editar Distribuidor");
            txtRazonSocial.setText(d.getNombre());
        } else {
            lblTitulo.setText("Nuevo Distribuidor");
        }
    }

    public void setOnGuardar(Runnable callback) {
        this.onGuardar = callback;
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) return;

        Distribuidor d = distribuidor != null ? distribuidor : new Distribuidor();
        d.setNombre(txtRazonSocial.getText().trim());

        try {
            if (distribuidor == null) {
                service.crear(d);
                AlertUtil.info("Éxito", "Distribuidor creado exitosamente.");
            } else {
                service.actualizar(d);
                AlertUtil.info("Éxito", "Distribuidor actualizado exitosamente.");
            }
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (IllegalArgumentException ex) {
            AlertUtil.advertencia("Validación", ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:\n" + ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private boolean validarFormulario() {
        if (txtRazonSocial.getText() == null || txtRazonSocial.getText().trim().isEmpty()) {
            lblRazonSocialError.setText("El Nombre es obligatorio.");
            lblRazonSocialError.setVisible(true);
            txtRazonSocial.requestFocus();
            return false;
        }
        return true;
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void limitarLongitud(TextInputControl control, int max) {
        control.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > max) {
                control.setText(oldVal);
            }
        });
    }
}