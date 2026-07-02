package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IVideojuegoService;
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

public class VideojuegoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtConsola;
    @FXML private Label  lblTotal;

    @FXML private TableView<Videojuego> tableView;
    @FXML private TableColumn<Videojuego, Boolean> colSeleccion;
    @FXML private TableColumn<Videojuego, Integer> colId;
    @FXML private TableColumn<Videojuego, String>  colNombre;
    @FXML private TableColumn<Videojuego, String>  colConsola;
    @FXML private TableColumn<Videojuego, String>  colGenero;
    @FXML private TableColumn<Videojuego, String>  colClasificacion;
    @FXML private TableColumn<Videojuego, String>  colDescripcion;
    @FXML private TableColumn<Videojuego, String>  colDistribuidor;
    @FXML private TableColumn<Videojuego, Void>    colAcciones;

    private final IVideojuegoService service;
    private final IDistribuidorService distribuidorService;
    private final ObservableList<Videojuego> data = FXCollections.observableArrayList();

    public VideojuegoController(IVideojuegoService service, IDistribuidorService distribuidorService) {
        this.service = service;
        this.distribuidorService = distribuidorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos("", "");
    }

    private void configurarColumnas() {
        tableView.setEditable(true);
        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);

        colId.setCellValueFactory(new PropertyValueFactory<>("idVideojuego"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colConsola.setCellValueFactory(new PropertyValueFactory<>("consola"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<>("nombreDistribuidor"));

        colAcciones.setCellFactory(crearCeldaAcciones());
        tableView.setItems(data);
    }

    private Callback<TableColumn<Videojuego, Void>, TableCell<Videojuego, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox        = new HBox(5, btnEditar, btnEliminar);
            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;");
                btnEliminar.setStyle("-fx-background-color:#D32F2F;-fx-text-fill:white;-fx-cursor:hand;");
                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> confirmarEliminar(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String nombre, String consola) {
        try {
            List<Videojuego> lista = service.buscar(nombre, consola);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo cargar los datos: " + e.getMessage());
        }
    }

    @FXML private void onBuscar() { cargarDatos(txtNombre.getText(), txtConsola.getText()); }
    @FXML private void onCrear() { abrirFormulario(null); }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream().filter(Videojuego::isSeleccionado).map(Videojuego::getIdVideojuego).collect(Collectors.toList());
        if (ids.isEmpty()) { AlertUtil.advertencia("Sin selección", "Marque un registro."); return; }
        if (!AlertUtil.confirmar("Confirmar", "¿Eliminar seleccionados?")) return;
        try {
            service.eliminarSeleccionados(ids);
            onBuscar();
        } catch (Exception e) { AlertUtil.error("Error", e.getMessage()); }
    }

    private void confirmarEliminar(Videojuego v) {
        if (AlertUtil.confirmar("Confirmar", "¿Eliminar a: " + v.getNombre() + "?")) {
            try { service.eliminar(v.getIdVideojuego()); onBuscar(); } catch (Exception e) { AlertUtil.error("Error", e.getMessage()); }
        }
    }

    private void abrirFormulario(Videojuego v) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/videojuego-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();
            VideojuegoFormController formCtrl = loader.getController();
            formCtrl.setVideojuego(v);
            formCtrl.setOnGuardar(this::onBuscar);
            Stage modal = new Stage();
            modal.setTitle(v == null ? "Nuevo Videojuego" : "Editar Videojuego");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
        } catch (IOException e) { AlertUtil.error("Error", "No se abrió el formulario: " + e.getMessage()); }
    }
}