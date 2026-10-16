package org.example.evaluacioncorte2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.evaluacioncorte2.DAO.ProductoDAO;
import org.example.evaluacioncorte2.model.Producto;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cmbFiltroCategoria;
    @FXML
    private CheckBox chkSoloStock;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;
    @FXML
    private Button btnRecargar;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, Integer> colId;
    @FXML
    private TableColumn<Producto, String> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;
    @FXML
    private Label lblContador;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private final List<String> categoriasValidas = List.of("Papelería", "Tecnología", "Accesorios");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        configurarCatalogos();
        configurarSeleccionTabla();
        consultarConFiltrosActuales();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tblProductos.setItems(listaProductos);
    }

    private void configurarCatalogos() {
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriasValidas));

        ObservableList<String> opcionesFiltro = FXCollections.observableArrayList("Todas");
        opcionesFiltro.addAll(categoriasValidas);
        cmbFiltroCategoria.setItems(opcionesFiltro);
        cmbFiltroCategoria.setValue("Todas");
    }

    private void configurarSeleccionTabla() {
        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                txtId.setText(String.valueOf(seleccionado.getIdProducto()));
                txtCodigo.setText(seleccionado.getCodigo());
                txtNombre.setText(seleccionado.getNombre());
                cmbCategoria.setValue(seleccionado.getCategoria());
                txtPrecio.setText(seleccionado.getPrecio().toPlainString());
                txtStock.setText(String.valueOf(seleccionado.getStock()));
            }
        });
    }

    @FXML
    public void accionBuscar() {
        consultarConFiltrosActuales();
    }

    @FXML
    public void accionLimpiarFiltros() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkSoloStock.setSelected(false);
        consultarConFiltrosActuales();
    }

    @FXML
    public void accionRecargar() {
        consultarConFiltrosActuales();
    }

    private void consultarConFiltrosActuales() {
        String texto = txtBuscar.getText();
        String categoria = cmbFiltroCategoria.getValue();
        boolean soloStock = chkSoloStock.isSelected();

        try {
            List<Producto> resultados = productoDAO.buscarConFiltros(texto, categoria, soloStock);
            listaProductos.setAll(resultados);
            lblContador.setText("Registros visibles: " + resultados.size());

            if (resultados.isEmpty() && texto != null && !texto.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Aviso", "Sin coincidencias.");
            }
        } catch (SQLException ex) {
            manejarExcepcionSQL(ex, "Consultar catálogo");
        }
    }

    @FXML
    public void accionLimpiarFormulario() {
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        tblProductos.getSelectionModel().clearSelection();
    }

    @FXML
    public void accionGuardar() {
        try {
            Producto p = validarFormulario();
            boolean guardado = productoDAO.insertar(p);
            if (guardado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto guardado correctamente.");
                accionLimpiarFormulario();
                consultarConFiltrosActuales();
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (SQLException e) {
            manejarExcepcionSQL(e, "Guardar producto");
        }
    }

    @FXML
    public void accionActualizar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción requerida", "Debe seleccionar un producto de la tabla para actualizar.");
            return;
        }

        try {
            Producto p = validarFormulario();
            p.setIdProducto(Integer.parseInt(txtId.getText().trim()));

            boolean actualizado = productoDAO.actualizar(p);
            if (actualizado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto actualizado correctamente.");
                accionLimpiarFormulario();
                consultarConFiltrosActuales();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Registro No Encontrado", "El producto ya no existe en la base de datos.");
                consultarConFiltrosActuales();
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (SQLException e) {
            manejarExcepcionSQL(e, "Actualizar producto");
        }
    }

    @FXML
    public void accionEliminar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción requerida", "Debe seleccionar un producto de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el producto seleccionado?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                boolean eliminado = productoDAO.eliminar(id);
                if (eliminado) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
                    accionLimpiarFormulario();
                    consultarConFiltrosActuales();
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Registro No Encontrado", "El producto seleccionado ya no existe en la base de datos.");
                    consultarConFiltrosActuales();
                }
            } catch (SQLException e) {
                manejarExcepcionSQL(e, "Eliminar producto");
            }
        }
    }

    private Producto validarFormulario() throws IllegalArgumentException {

        if (txtCodigo.getText() == null) throw new IllegalArgumentException("El código no puede estar vacío.");
        String cod = txtCodigo.getText().trim().toUpperCase();
        if (cod.isEmpty() || cod.length() > 10) {
            throw new IllegalArgumentException("El código debe tener entre 1 y 10 caracteres tras quitar espacios.");
        }

        if (txtNombre.getText() == null) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        String nom = txtNombre.getText().trim();
        if (nom.length() < 3 || nom.length() > 80) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 80 caracteres tras quitar espacios.");
        }

        String cat = cmbCategoria.getValue();
        if (cat == null || !categoriasValidas.contains(cat)) {
            throw new IllegalArgumentException("Seleccione una categoría válida: Papelería, Tecnología o Accesorios.");
        }

        String precioRaw = txtPrecio.getText();
        if (precioRaw == null || precioRaw.trim().isEmpty()) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }
        BigDecimal precio;
        try {
            precio = new BigDecimal(precioRaw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser numérico.");
        }
        if (precio.scale() > 2) {
            throw new IllegalArgumentException("El precio no puede tener más de 2 decimales.");
        }
        if (precio.compareTo(new BigDecimal("0.01")) < 0 || precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("El precio debe estar en el rango de 0.01 a 99999999.99.");
        }

        // V7, V8: Stock
        String stockRaw = txtStock.getText();
        if (stockRaw == null || stockRaw.trim().isEmpty()) {
            throw new IllegalArgumentException("El stock es obligatorio.");
        }
        long stockLong;
        try {
            stockLong = Long.parseLong(stockRaw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El stock debe ser un número entero.");
        }
        if (stockLong < 0 || stockLong > 2147483647L) {
            throw new IllegalArgumentException("El stock debe estar entre 0 y 2147483647.");
        }

        return new Producto(cod, nom, cat, precio, (int) stockLong);
    }

    private void manejarExcepcionSQL(SQLException ex, String operacion) {
        System.err.printf("[ERROR SQL] Operación: %s | SQLState: %s | Código: %d | Detalle: %s%n",
                operacion, ex.getSQLState(), ex.getErrorCode(), ex.getMessage());

        String mensajeUsuario = "Ocurrió un error inesperado al procesar la operación.";

        if (ex.getSQLState() != null && ex.getSQLState().startsWith("08")) {
            mensajeUsuario = "No fue posible conectar con PostgreSQL. Verifique si el servicio está activo.";
        }
        else if ("23505".equals(ex.getSQLState())) {
            mensajeUsuario = "El código ingresado ya existe en la base de datos.";
        }
        else if ("23514".equals(ex.getSQLState())) {
            mensajeUsuario = "Los datos no cumplen con las restricciones de integridad.";
        }

        mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", mensajeUsuario);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}