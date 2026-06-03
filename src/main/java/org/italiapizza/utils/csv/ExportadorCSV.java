/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.utils.csv;

import org.italiapizza.controller.exception.csv.ExportacionException;
import org.italiapizza.model.dto.DetallePedido;
import org.italiapizza.model.dto.Inventario;
import org.italiapizza.model.dto.Pedido;
import org.italiapizza.model.dto.Producto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.italiapizza.controller.exception.csv.ExportacionException;

/**
 * 
 * @author Yos
 */
public final class ExportadorCSV {

    private static final char SEPARADOR = ',';
    private static final String FIN_LINEA = "\r\n";
    private static final char BOM = '\uFEFF';
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ExportadorCSV() {}

    public static String exportarInventario(List<Inventario> inventario, String rutaArchivo)
            throws ExportacionException {

        if (inventario == null) {
            throw new ExportacionException("La lista de inventario no puede ser nula");
        }

        String[] encabezados = {
                "ID Producto", "Codigo", "Nombre", "Cantidad Sistema",
                "Existencia Almacen", "Diferencia", "Unidad", "Estado"
        };

        List<String[]> filas = new ArrayList<>();
        for (Inventario inv : inventario) {
            filas.add(new String[]{
                    String.valueOf(inv.getIdProducto()),
                    inv.getCodigo(),
                    inv.getNombre(),
                    formatearNumero(inv.getCantidadSistema()),
                    formatearNumero(inv.getExistenciaAlmacen()),
                    formatearNumero(inv.getDiferencia()),
                    inv.getUnidadMedida(),
                    inv.getEstadoTexto()
            });
        }

        return exportar(encabezados, filas, rutaArchivo);
    }

    /**
     * Exporta una lista de pedidos a CSV (resultado de búsquedas de pedidos).
     * @param pedidos
     * @param rutaArchivo
     * @return
     * @throws ExportacionException 
     */
    public static String exportarPedidos(List<Pedido> pedidos, String rutaArchivo)
            throws ExportacionException {

        if (pedidos == null) {
            throw new ExportacionException("La lista de pedidos no puede ser nula");
        }

        String[] encabezados = {
                "ID Pedido", "Fecha", "ID Cliente", "ID Empleado", "Estatus", "Monto Total"
        };

        List<String[]> filas = new ArrayList<>();
        for (Pedido p : pedidos) {
            filas.add(new String[]{
                    String.valueOf(p.getIdPedido()),
                    formatearFecha(p.getFechaPedido()),
                    p.getIdCliente() != null ? String.valueOf(p.getIdCliente()) : "",
                    String.valueOf(p.getIdEmpleado()),
                    p.getEstatus(),
                    formatearNumero(p.getMontoTotal())
            });
        }

        return exportar(encabezados, filas, rutaArchivo);
    }

    
    public static String exportarProductos(List<Producto> productos, String rutaArchivo)
            throws ExportacionException {

        if (productos == null) {
            throw new ExportacionException("La lista de productos no puede ser nula");
        }

        String[] encabezados = {
                "ID", "Codigo", "Nombre", "Descripcion", "Precio", "Restriccion",
                "Tipo", "Unidad", "Contenido", "Estatus"
        };

        List<String[]> filas = new ArrayList<>();
        for (Producto p : productos) {
            filas.add(new String[]{
                    String.valueOf(p.getIdProducto()),
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    formatearNumero(p.getPrecio()),
                    p.getRestriccion(),
                    p.getTipoProducto(),
                    p.getUnidadMedida(),
                    formatearNumero(p.getContenido()),
                    p.estaActivo() ? "Activo" : "Inactivo"
            });
        }

        return exportar(encabezados, filas, rutaArchivo);
    }

    public static String exportarDetallePedido(List<DetallePedido> detalles, String rutaArchivo)
            throws ExportacionException {

        if (detalles == null) {
            throw new ExportacionException("La lista de detalles no puede ser nula");
        }

        String[] encabezados = {
                "ID Pedido", "ID Producto", "Producto", "Unidades", "Precio Unitario", "Subtotal"
        };

        List<String[]> filas = new ArrayList<>();
        for (DetallePedido d : detalles) {
            filas.add(new String[]{
                    String.valueOf(d.getIdPedido()),
                    String.valueOf(d.getIdProducto()),
                    d.getNombreProducto(),
                    String.valueOf(d.getUnidades()),
                    formatearNumero(d.getPrecioUnitario()),
                    formatearNumero(d.getSubtotal())
            });
        }

        return exportar(encabezados, filas, rutaArchivo);
    }

    /**
     * Escribe un CSV genérico a partir de encabezados y filas ya armadas.
     * metodo GENÉRICO (reutilizable para cualquier tabla)
     * @param encabezados
     * @param filas
     * @param rutaArchivo
     * @return
     * @throws ExportacionException 
     */
    public static String exportar(String[] encabezados, List<String[]> filas, String rutaArchivo)
            throws ExportacionException {

        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new ExportacionException("La ruta del archivo no puede estar vacía");
        }

        File archivo = asegurarExtensionCsv(rutaArchivo);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

            // BOM para que Excel reconozca el UTF-8 y muestre bien los acentos
            writer.write(BOM);

            if (encabezados != null) {
                writer.write(unirFila(encabezados));
                writer.write(FIN_LINEA);
            }

            if (filas != null) {
                for (String[] fila : filas) {
                    writer.write(unirFila(fila));
                    writer.write(FIN_LINEA);
                }
            }

            writer.flush();
            return archivo.getAbsolutePath();

        } catch (IOException e) {
            throw new ExportacionException("Error al escribir el archivo CSV: " + e.getMessage(), e);
        }
    }

    /**
     * Une los campos de una fila aplicando el escapado (formato especial) CSV a cada uno.
     */
    private static String unirFila(String[] campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                sb.append(SEPARADOR);
            }
            sb.append(escapar(campos[i]));
        }
        return sb.toString();
    }

    /**
     * Escapa un valor
     * Encierra en comillas si contiene la coma separadora, comillas o saltos
     * de línea, y duplica las comillas internas
     * @param valor
     * @return 
     */
    private static String escapar(String valor) {
        if (valor == null) {
            return "";
        }

        boolean necesitaComillas = valor.indexOf(SEPARADOR) >= 0
                || valor.indexOf('"') >= 0
                || valor.indexOf('\n') >= 0
                || valor.indexOf('\r') >= 0;

        String resultado = valor.replace("\"", "\"\"");

        if (necesitaComillas) {
            resultado = "\"" + resultado + "\"";
        }

        return resultado;
    }

    private static String formatearNumero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static String formatearFecha(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATO_FECHA) : "";
    }

    
    private static File asegurarExtensionCsv(String rutaArchivo) {
        String ruta = rutaArchivo.trim();
        if (!ruta.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            ruta = ruta + ".csv";
        }
        return new File(ruta);
    }
}