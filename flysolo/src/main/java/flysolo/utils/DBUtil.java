package flysolo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    
    private static final String URL = "jdbc:mysql://localhost:3306/flysolo";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    // Cargar driver una vez
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se encontró el driver MySQL", e);
        }
    }
    
    // Obtener conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Cerrar recursos
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { /* silencioso */ }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* silencioso */ }
        try { if (conn != null) conn.close(); } catch (SQLException e) { /* silencioso */ }
    }
    
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }
    
    public static void close(Connection conn) {
        close(conn, null, null);
    }
    
    // Método genérico para INSERT/UPDATE/DELETE
    public static boolean executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(conn, pstmt);
        }
    }
    
    // Método genérico para contar registros
    public static int count(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        } finally {
            close(conn, pstmt, rs);
        }
        return 0;
    }
}
