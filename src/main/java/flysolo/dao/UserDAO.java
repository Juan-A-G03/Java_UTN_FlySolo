package flysolo.dao;

import flysolo.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import flysolo.entities.User;

public class UserDAO {

    // listar todos los usuarios
    public ArrayList<User> list() {
        ArrayList<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setFaction(rs.getString("faction"));
                u.setUserType(rs.getString("user_type"));
                u.setLicenseNumber(rs.getString("license_number"));
                u.setPilotStatus(rs.getString("pilot_status"));
                u.setRatingAvg(rs.getBigDecimal("rating_avg"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return users;
    }
    
    // buscar un usuario por ID
    public User search(long id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFaction(rs.getString("faction"));
                user.setUserType(rs.getString("user_type"));
                user.setLicenseNumber(rs.getString("license_number"));
                user.setPilotStatus(rs.getString("pilot_status"));
                user.setRatingAvg(rs.getBigDecimal("rating_avg"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return user;
    }
    
    // buscar un usuario por email
    public User searchByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFaction(rs.getString("faction"));
                user.setUserType(rs.getString("user_type"));
                user.setLicenseNumber(rs.getString("license_number"));
                user.setPilotStatus(rs.getString("pilot_status"));
                user.setRatingAvg(rs.getBigDecimal("rating_avg"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return user;
    }
    
    // listar usuarios por tipo (PASSENGER, PILOT, ADMIN)
    public ArrayList<User> listByType(String userType) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_type = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userType);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setFaction(rs.getString("faction"));
                u.setUserType(rs.getString("user_type"));
                u.setLicenseNumber(rs.getString("license_number"));
                u.setPilotStatus(rs.getString("pilot_status"));
                u.setRatingAvg(rs.getBigDecimal("rating_avg"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));

                users.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return users;
    }
    
    // listar pilotos por estado (PENDIENTE, APROBADO, RECHAZADO)
    public ArrayList<User> listPilotsByStatus(String pilotStatus) {
        ArrayList<User> pilots = new ArrayList<>();
        // permitir listar CUALQUIER usuario con un pilot_status dado (incluye pasajeros que solicitaron convertirse en pilotos)
        String sql = "SELECT * FROM users WHERE pilot_status = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pilotStatus);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setFaction(rs.getString("faction"));
                u.setUserType(rs.getString("user_type"));
                u.setLicenseNumber(rs.getString("license_number"));
                u.setPilotStatus(rs.getString("pilot_status"));
                u.setRatingAvg(rs.getBigDecimal("rating_avg"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));

                pilots.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return pilots;
    }
    
    // listar pilotos no aprobados (incluye PENDIENTE, RECHAZADO u otros sin APROBADO)
    public ArrayList<User> listPilotsNotApproved() {
        ArrayList<User> pilots = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE (pilot_status IS NULL OR pilot_status != 'APROBADO') AND (license_number IS NOT NULL OR user_type = 'PILOT' OR pilot_status = 'PENDIENTE')";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setFaction(rs.getString("faction"));
                u.setUserType(rs.getString("user_type"));
                u.setLicenseNumber(rs.getString("license_number"));
                u.setPilotStatus(rs.getString("pilot_status"));
                u.setRatingAvg(rs.getBigDecimal("rating_avg"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                u.setUpdatedAt(rs.getTimestamp("updated_at"));

                pilots.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return pilots;
    }
    
    // insertar un usuario en la BD
    public boolean insert(User u) {
        String sql = "INSERT INTO users (name, email, password, faction, user_type, license_number, pilot_status, rating_avg) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean inserted = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getFaction());
            stmt.setString(5, u.getUserType());
            stmt.setString(6, u.getLicenseNumber());
            stmt.setString(7, u.getPilotStatus());
            stmt.setBigDecimal(8, u.getRatingAvg());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                inserted = true;
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    u.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, generatedKeys);
        }

        return inserted;
    }
    
    // actualizar un usuario en la BD
    public boolean update(User u) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, faction = ?, user_type = ?, license_number = ?, pilot_status = ?, rating_avg = ? WHERE id = ?";
        boolean updated = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getFaction());
            stmt.setString(5, u.getUserType());
            stmt.setString(6, u.getLicenseNumber());
            stmt.setString(7, u.getPilotStatus());
            stmt.setBigDecimal(8, u.getRatingAvg());
            stmt.setLong(9, u.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                updated = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }

        return updated;
    }
    
    // eliminar un usuario de la BD
    public boolean delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        boolean deleted = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                deleted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }

        return deleted;
    }
}