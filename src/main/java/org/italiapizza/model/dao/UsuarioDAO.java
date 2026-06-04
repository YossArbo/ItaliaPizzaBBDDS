package org.italiapizza.model.dao;

import java.sql.Connection;
import org.italiapizza.controller.exception.usuario.EliminacionUsuarioException;
import org.italiapizza.controller.exception.usuario.LoginException;
import org.italiapizza.controller.exception.usuario.RegistroUsuarioException;
import org.italiapizza.model.connection.MySQLConnectionManager;
import org.italiapizza.model.dto.Cliente;
import org.italiapizza.model.dto.Empleado;
import org.italiapizza.model.dto.Usuario;
import org.italiapizza.utils.password.SeguridadUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.italiapizza.controller.exception.usuario.EdicionUsuarioException;

public class UsuarioDAO {
    
    public UsuarioDAO() {
    }
    
    /**
     * Registra un nuevo usuario (Cliente o Empleado) en la BD.
     * @param usuario
     * @return
     * @throws RegistroUsuarioException 
     */
    public int registrarUsuario(Usuario usuario) throws RegistroUsuarioException {
        if (usuario == null) {
            throw new RegistroUsuarioException("El usuario no puede ser nulo");
        }
        
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sqlUsuario = "INSERT INTO usuario (nombres, apellidos, tipo_usuario, estatus) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement psUsuario = conn.getConnection().prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, usuario.getNombres());
                psUsuario.setString(2, usuario.getApellidos());
                psUsuario.setString(3, usuario.getTipoUsuario());
                psUsuario.setInt(4, usuario.getEstatus());
                psUsuario.executeUpdate();
                
                int idUsuario = obtenerLlaveGenerada(psUsuario);
                
                if (usuario instanceof Empleado) {
                    registrarEmpleado(conn, idUsuario, (Empleado) usuario);
                } else if (usuario instanceof Cliente) {
                    registrarCliente(conn, idUsuario, (Cliente) usuario);
                }
                
                if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                    insertarEmail(conn, idUsuario, usuario.getEmail());
                }
                if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
                    insertarTelefono(conn, idUsuario, usuario.getTelefono());
                }
                return idUsuario;
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al registrar usuario: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * Autenticación segura usando SHA-256 
     * @param nombreUsuario
     * @param contrasenaIngresada
     * @return
     * @throws LoginException 
     */
    public Empleado login(String nombreUsuario, String contrasenaIngresada) throws LoginException {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) throw new LoginException("No puede estar el usuario vacio");
        if (contrasenaIngresada == null || contrasenaIngresada.isEmpty()) throw new LoginException("Contraseña vacía");
        
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.estatus, " +
                         "e.nombre_usuario, e.contrasena, e.rol " +
                         "FROM usuario u JOIN empleado e ON u.id_usuario = e.id_usuario " +
                         "WHERE e.nombre_usuario = ? AND u.estatus = 1";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombreUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new LoginException("Credenciales inválidas.");
                    
                    String contrasenaBD = rs.getString("contrasena");
                    if (!SeguridadUtil.validarContrasena(contrasenaIngresada, contrasenaBD)) {
                        throw new LoginException("Credenciales inválidas.");
                    }
                    
                    return new Empleado(
                        rs.getInt("id_usuario"), rs.getString("nombres"), rs.getString("apellidos"), 
                        null, null, 1, nombreUsuario, rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            throw new LoginException("Error en BD al hacer login: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * Eliminación lógica del usuario validando sesión activa o pedidos.
     * @param idUsuarioAEliminar
     * @param idUsuarioActual
     * @throws EliminacionUsuarioException 
     */
    public void eliminarUsuario(int idUsuarioAEliminar, int idUsuarioActual) throws EliminacionUsuarioException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "CALL sp_eliminar_usuario(?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idUsuarioAEliminar);
                ps.setInt(2, idUsuarioActual);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            String msg = e.getMessage();
            if (msg.contains("No se puede eliminar la cuenta")) {
                throw new EliminacionUsuarioException("No puede eliminar su propia cuenta activa", e);
            } else if (msg.contains("No se puede eliminar un cliente")) {
                throw new EliminacionUsuarioException("No puede eliminar cliente con pedidos", e);
            } else {
                throw new EliminacionUsuarioException("Error al eliminar: " + msg, e);
            }
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * Edita un usuario 
     * @param usuario
     * @throws EdicionUsuarioException 
     */
    public void editarUsuario(Usuario usuario) throws EdicionUsuarioException {
            if (usuario == null || usuario.getIdUsuario() <= 0) {
            throw new EdicionUsuarioException("El usuario no se puede validar");
        }
        
        Connection conn = null;
        try {
            MySQLConnectionManager.getInstance().connectionAdmin();
            conn = MySQLConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlUsuario = "UPDATE usuario SET nombres = ?, apellidos = ? WHERE id_usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                ps.setString(1, usuario.getNombres());
                ps.setString(2, usuario.getApellidos());
                ps.setInt(3, usuario.getIdUsuario());
                ps.executeUpdate();
            }

            if ("Empleado".equals(usuario.getTipoUsuario())) {
                actualizarEmpleado(conn, (Empleado) usuario);
            } else if ("Cliente".equals(usuario.getTipoUsuario())) {
                actualizarCliente(conn, (Cliente) usuario);
            }

            reemplazarEmail(conn, usuario.getIdUsuario(), usuario.getEmail());
            reemplazarTelefono(conn, usuario.getIdUsuario(), usuario.getTelefono());
        
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new EdicionUsuarioException("Error al editar usuario: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Obtiene los datos completos de un usuario específico usando su ID.
     * @param idUsuario
     * @return
     * @throws RegistroUsuarioException 
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) throws RegistroUsuarioException {
        if (idUsuario <= 0) throw new RegistroUsuarioException("ID inválido");
        
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT id_usuario, nombres, apellidos, estatus, tipo_usuario FROM usuario WHERE id_usuario = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String tipoUsuario = rs.getString("tipo_usuario");
                        if ("Empleado".equals(tipoUsuario)) {
                            return obtenerDetalleEmpleado(conn, rs);
                        } else if ("Cliente".equals(tipoUsuario)) {
                            return obtenerDetalleCliente(conn, rs);
                        }
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al obtener usuario: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Búsqueda por nombre case-insensitive.
     * @param nombreBusqueda
     * @return
     * @throws RegistroUsuarioException 
     */
    public List<Usuario> buscarPorNombre(String nombreBusqueda) throws RegistroUsuarioException {
        if (nombreBusqueda == null || nombreBusqueda.isEmpty()) {
            throw new RegistroUsuarioException("El nombre de búsqueda no puede estar vacío");
        }
        
        List<Usuario> lista = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT id_usuario, nombres, apellidos, estatus, tipo_usuario FROM usuario " +
                         "WHERE LOWER(CONCAT(nombres, ' ', apellidos)) LIKE LOWER(?) AND estatus = 1";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + nombreBusqueda + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String tipoUsuario = rs.getString("tipo_usuario");
                        if ("Empleado".equals(tipoUsuario)) {
                            lista.add(obtenerDetalleEmpleado(conn, rs));
                        } else if ("Cliente".equals(tipoUsuario)) {
                            lista.add(obtenerDetalleCliente(conn, rs));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al buscar usuarios: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }
    
    
    /**
     * Busqueda por direccion case insensitive
     * @param direccionBusqueda
     * @return
     * @throws RegistroUsuarioException 
     */
    public List<Usuario> buscarPorDireccion(String direccionBusqueda) throws RegistroUsuarioException {
        if (direccionBusqueda == null || direccionBusqueda.isEmpty()) {
            throw new RegistroUsuarioException("La dirección de búsqueda no puede estar vacía");
        }

        List<Usuario> lista = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.estatus, u.tipo_usuario " +
                         "FROM usuario u " +
                         "JOIN cliente c ON u.id_usuario = c.id_usuario " +
                         "WHERE LOWER(CONCAT_WS(' ', c.calle, c.numero, c.ciudad, c.codigo_postal)) LIKE LOWER(?) " +
                         "AND u.estatus = 1";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + direccionBusqueda + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(obtenerDetalleCliente(conn, rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al buscar usuarios por dirección: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }
    
    public List<Usuario> buscarPorTelefono(String telefonoBusqueda) throws RegistroUsuarioException {
        if (telefonoBusqueda == null || telefonoBusqueda.isEmpty()) {
            throw new RegistroUsuarioException("El teléfono de búsqueda no puede estar vacío");
        }

        List<Usuario> lista = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT DISTINCT u.id_usuario, u.nombres, u.apellidos, u.estatus, u.tipo_usuario " +
                         "FROM usuario u " +
                         "JOIN telefono_usuario t ON u.id_usuario = t.id_usuario " +
                         "WHERE t.telefono LIKE ? AND u.estatus = 1";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + telefonoBusqueda + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String tipoUsuario = rs.getString("tipo_usuario");
                        if ("Empleado".equals(tipoUsuario)) {
                            lista.add(obtenerDetalleEmpleado(conn, rs));
                        } else if ("Cliente".equals(tipoUsuario)) {
                            lista.add(obtenerDetalleCliente(conn, rs));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al buscar usuarios por teléfono: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    /**
     * Verifica que el nombre de usuario sea unico al momento de registrar.
     * @param nombreUsuario
     * @return
     * @throws RegistroUsuarioException 
     */
    public boolean existeNombreUsuario(String nombreUsuario) throws RegistroUsuarioException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT COUNT(*) FROM empleado WHERE nombre_usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombreUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al validar nombre de usuario: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    
    private int obtenerLlaveGenerada(PreparedStatement ps) throws RegistroUsuarioException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) throw new RegistroUsuarioException("No se generó ID");
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RegistroUsuarioException("Error al obtener ID", e);
        }
    }
    
    private void registrarEmpleado(MySQLConnectionManager conn, int id, Empleado e) throws SQLException {
        String sql = "INSERT INTO empleado (id_usuario, nombre_usuario, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, e.getNombreUsuario());
            ps.setString(3, SeguridadUtil.hashearContrasena(e.getContrasena()));
            ps.setString(4, e.getRol());
            ps.executeUpdate();
        }
    }
    
    private void registrarCliente(MySQLConnectionManager conn, int id, Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (id_usuario, calle, numero, ciudad, codigo_postal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, c.getCalle());
            ps.setString(3, c.getNumero());
            ps.setString(4, c.getCiudad());
            ps.setString(5, c.getCodigoPostal());
            ps.executeUpdate();
        }
    }
    
    private void insertarEmail(MySQLConnectionManager conn, int id, String email) throws SQLException {
        String sql = "INSERT INTO email_usuario (id_usuario, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }
    
    private void insertarTelefono(MySQLConnectionManager conn, int id, String tel) throws SQLException {
        String sql = "INSERT INTO telefono_usuario (id_usuario, telefono) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, tel);
            ps.executeUpdate();
        }
    }

    private Empleado obtenerDetalleEmpleado(MySQLConnectionManager conn, ResultSet rs) throws SQLException {
        int idUsuario = rs.getInt("id_usuario");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        int estatus = rs.getInt("estatus");
        
        String sqlEmpleado = "SELECT nombre_usuario, rol FROM empleado WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlEmpleado)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rsEmpleado = ps.executeQuery()) {
                if (rsEmpleado.next()) {
                    String nombreUsuario = rsEmpleado.getString("nombre_usuario");
                    String rol = rsEmpleado.getString("rol");
                    return new Empleado(idUsuario, nombres, apellidos, null, null, estatus, nombreUsuario, rol);
                }
            }
        }
        return null;
    }
    
    private Cliente obtenerDetalleCliente(MySQLConnectionManager conn, ResultSet rs) throws SQLException {
        int idUsuario = rs.getInt("id_usuario");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        int estatus = rs.getInt("estatus");
        
        String sqlCliente = "SELECT calle, numero, ciudad, codigo_postal FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rsCliente = ps.executeQuery()) {
                if (rsCliente.next()) {
                    String calle = rsCliente.getString("calle");
                    String numero = rsCliente.getString("numero");
                    String ciudad = rsCliente.getString("ciudad");
                    String codigoPostal = rsCliente.getString("codigo_postal");
                    return new Cliente(idUsuario, nombres, apellidos, null, null, estatus, calle, numero, ciudad, codigoPostal);
                }
            }
        }
        return null;
    }

    private void actualizarEmpleado(Connection conn, Empleado empleado) throws SQLException {
        boolean cambiarPassword = empleado.getContrasena() != null && !empleado.getContrasena().isEmpty();
    
        String sql = cambiarPassword 
            ? "UPDATE empleado SET nombre_usuario = ?, rol = ?, contrasena = ? WHERE id_usuario = ?" 
            : "UPDATE empleado SET nombre_usuario = ?, rol = ? WHERE id_usuario = ?";
    
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombreUsuario());
            ps.setString(2, empleado.getRol());
        
            if (cambiarPassword) {
                ps.setString(3, SeguridadUtil.hashearContrasena(empleado.getContrasena()));
                ps.setInt(4, empleado.getIdUsuario());
            } else {
                ps.setInt(3, empleado.getIdUsuario());
            }
        
            ps.executeUpdate();
        }
    }

    private void actualizarCliente(Connection conn, Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET calle = ?, numero = ?, ciudad = ?, codigo_postal = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getCalle());
            ps.setString(2, cliente.getNumero());
            ps.setString(3, cliente.getCiudad());
            ps.setString(4, cliente.getCodigoPostal());
            ps.setInt(5, cliente.getIdUsuario());
            ps.executeUpdate();
        }
    }

    private void reemplazarEmail(Connection conn, int idUsuario, String email) throws SQLException {
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM email_usuario WHERE id_usuario = ?")) {
            del.setInt(1, idUsuario);
            del.executeUpdate();
        }
        if (email != null && !email.isEmpty()) {
            String sql = "INSERT INTO email_usuario (id_usuario, email) VALUES (?, ?)";
            try (PreparedStatement ins = conn.prepareStatement(sql)) {
                ins.setInt(1, idUsuario);
                ins.setString(2, email);
                ins.executeUpdate();
            }
        }
    }

    private void reemplazarTelefono(Connection conn, int idUsuario, String telefono) throws SQLException {
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM telefono_usuario WHERE id_usuario = ?")) {
            del.setInt(1, idUsuario);
            del.executeUpdate();
        }
        
        if (telefono != null && !telefono.isEmpty()) {
            String sql = "INSERT INTO telefono_usuario (id_usuario, telefono) VALUES (?, ?)";
            try (PreparedStatement ins = conn.prepareStatement(sql)) {
                ins.setInt(1, idUsuario);
                ins.setString(2, telefono);
                ins.executeUpdate();
            }
        }
    }
}