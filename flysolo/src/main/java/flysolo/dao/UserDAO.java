package flysolo.dao;

import flysolo.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import flysolo.entities.User;

public class UserDAO {

    //list all users
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
    
    //find a user by ID
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
    
    //find a user by email
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
    
    //list users by type (PASSENGER, PILOT, ADMIN)
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
    
    //list pilots by status (PENDIENTE, APROBADO, RECHAZADO)
    public ArrayList<User> listPilotsByStatus(String pilotStatus) {
        ArrayList<User> pilots = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE user_type = 'PILOT' AND pilot_status = ?";

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
    
    //insert a user in the DB
    public boolean insert(User u) {
        String sql = "INSERT INTO users (name, email, faction, user_type, license_number, pilot_status, rating_avg) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean inserted = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getFaction());
            stmt.setString(4, u.getUserType());
            stmt.setString(5, u.getLicenseNumber());
            stmt.setString(6, u.getPilotStatus());
            stmt.setBigDecimal(7, u.getRatingAvg());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    u.setId(generatedKeys.getLong(1));
                }
                inserted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, generatedKeys);
        }

        return inserted;
    }
    
    //update a User by ID
    public boolean update(User u) {
        String sql = "UPDATE users SET name = ?, email = ?, faction = ?, user_type = ?, license_number = ?, pilot_status = ?, rating_avg = ? WHERE id = ?";
        boolean updated = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getFaction());
            stmt.setString(4, u.getUserType());
            stmt.setString(5, u.getLicenseNumber());
            stmt.setString(6, u.getPilotStatus());
            stmt.setBigDecimal(7, u.getRatingAvg());
            stmt.setLong(8, u.getId());

            updated = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return updated;
    }

    //delete a user by ID
    public boolean delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        boolean deleted = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);

            deleted = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return deleted;
    }

}
