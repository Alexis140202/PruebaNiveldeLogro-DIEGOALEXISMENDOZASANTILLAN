package com.ucv.lab12.controller;

import com.ucv.lab12.model.Empleado;
import com.ucv.lab12.service.EmpleadoService;
import com.ucv.lab12.service.IEmpleadoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Date;

public class EmpleadoController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private TextField txtSueldo;

    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, String> colCodigo;
    @FXML private TableColumn<Empleado, String> colNombres;
    @FXML private TableColumn<Empleado, String> colApellidos;
    @FXML private TableColumn<Empleado, String> colDni;
    @FXML private TableColumn<Empleado, Double> colSueldo;

    private final IEmpleadoService service = new EmpleadoService();

    @FXML
    public void initialize() {
        // Mapeo exacto con los nombres de los atributos del Modelo
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codEmple"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dniEmple"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldoBasico"));

        cargarTabla();
    }

    @FXML
    private void handleGuardar() {
        try {
            Empleado emp = new Empleado(
                    txtCodigo.getText(),
                    txtNombres.getText(),
                    txtApellidos.getText(),
                    txtDni.getText(),
                    Double.parseDouble(txtSueldo.getText()),
                    new Date()
            );

            String resultado = service.registrarDocente(emp);

            if (resultado.equals("EXITO")) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Docente registrado correctamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Validación", resultado);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El sueldo debe ser un número válido.");
        }
    }

    private void cargarTabla() {
        tblEmpleados.setItems(FXCollections.observableArrayList(service.obtenerTodos()));
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtDni.clear();
        txtSueldo.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}