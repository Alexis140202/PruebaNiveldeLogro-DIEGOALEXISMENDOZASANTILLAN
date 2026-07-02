package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.service.IDistribuidorService;
import com.ucv.lab12.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DistribuidorController implements Initializable {

    // Filtro simplificado
    @FXML private TextField txtRazonSocial;
    @FXML private Label  lblTotal;

    // Tabla
    @FXML private TableView<Distribuidor>              tableView;
    @FXML private TableColumn<Distribuidor, Boolean>   colSeleccion;
    @FXML private TableColumn<Distribuidor, Integer>   colId;
    @FXML private TableColumn<Distribuidor, String>    colRazonSocial;
    @FXML private TableColumn<Distribuidor, Void>      colAcciones;

    private final IDistribuidorService service;
    private final ObservableList<Distribuidor> data = FXCollections.observableArrayList();

    public DistribuidorController(IDistribuidorService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos(""); // Corregido a un solo parámetro

        // Permitir búsqueda con Enter
        txtRazonSocial.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        tableView.setEditable(true);

        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);
        colSeleccion.setSortable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("idDistribuidor"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("nombre")); // Mapeado a nombre

        colAcciones.setCellFactory(crearCeldaAcciones());
        colAcciones.setSortable(false);

        tableView.setItems(data);
    }

    private Callback<TableColumn<Distribuidor, Void>, TableCell<Distribuidor, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox   hbox        = new HBox(5, btnEditar, btnEliminar);

            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#D32F2F;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");

                btnEditar.setOnAction(e -> {
                    Distribuidor d = getTableView().getItems().get(getIndex());
                    abrirFormulario(d);
                });
                btnEliminar.setOnAction(e -> {
                    Distribuidor d = getTableView().getItems().get(getIndex());
                    confirmarEliminar(d);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String nombre) {
        try {
            List<Distribuidor> lista = service.buscar(nombre);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error de conexión", "No se pudo cargar los datos:\n" + e.getMessage());
        }
    }

    @FXML
    private void onBuscar() {
        cargarDatos(txtRazonSocial.getText());
    }

    @FXML
    private void onCrear() {
        abrirFormulario(null);
    }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream()
                .filter(Distribuidor::isSeleccionado)
                .map(Distribuidor::getIdDistribuidor)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin selección", "Marque al menos un registro para eliminar.");
            return;
        }

        if (!AlertUtil.confirmar("Confirmar", "¿Eliminar seleccionados?")) return;

        try {
            service.eliminarSeleccionados(ids);
            cargarDatos(txtRazonSocial.getText());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void confirmarEliminar(Distribuidor d) {
        if (!AlertUtil.confirmar("Confirmar", "¿Eliminar a: " + d.getNombre() + "?")) return;

        try {
            service.eliminar(d.getIdDistribuidor());
            cargarDatos(txtRazonSocial.getText());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void abrirFormulario(Distribuidor d) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/distribuidor-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            DistribuidorFormController formCtrl = loader.getController();
            formCtrl.setDistribuidor(d);
            formCtrl.setOnGuardar(() -> cargarDatos(txtRazonSocial.getText()));

            Stage modal = new Stage();
            modal.setTitle(d == null ? "Nuevo Distribuidor" : "Editar Distribuidor");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}