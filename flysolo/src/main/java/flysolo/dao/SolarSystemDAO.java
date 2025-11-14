package flysolo.dao;

import flysolo.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import flysolo.entities.SolarSystem;

public class SolarSystemDAO {

    //list all solar systems
    public ArrayList<SolarSystem> list() {
        ArrayList<SolarSystem> systems = new ArrayList<>();

        String sql = "SELECT * FROM solar_systems ORDER BY name";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                SolarSystem s = new SolarSystem();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setSystemXU12(rs.getInt("system_x_u12"));
                s.setSystemYU12(rs.getInt("system_y_u12"));
                s.setSystemZU12(rs.getInt("system_z_u12"));
                s.setCreatedAt(rs.getTimestamp("created_at"));

                systems.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return systems;
    }
    
    //find a solar system by ID
    public SolarSystem search(int id) {
        SolarSystem system = null;
        String sql = "SELECT * FROM solar_systems WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                system = new SolarSystem();
                system.setId(rs.getInt("id"));
                system.setName(rs.getString("name"));
                system.setSystemXU12(rs.getInt("system_x_u12"));
                system.setSystemYU12(rs.getInt("system_y_u12"));
                system.setSystemZU12(rs.getInt("system_z_u12"));
                system.setCreatedAt(rs.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return system;
    }
    
    //find a solar system by name
    public SolarSystem searchByName(String name) {
        SolarSystem system = null;
        String sql = "SELECT * FROM solar_systems WHERE name = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            if (rs.next()) {
                system = new SolarSystem();
                system.setId(rs.getInt("id"));
                system.setName(rs.getString("name"));
                system.setSystemXU12(rs.getInt("system_x_u12"));
                system.setSystemYU12(rs.getInt("system_y_u12"));
                system.setSystemZU12(rs.getInt("system_z_u12"));
                system.setCreatedAt(rs.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return system;
    }
    
    //insert a solar system in the DB
    public boolean insert(SolarSystem s) {
        String sql = "INSERT INTO solar_systems (name, system_x_u12, system_y_u12, system_z_u12) VALUES (?, ?, ?, ?)";
        boolean inserted = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, s.getName());
            stmt.setInt(2, s.getSystemXU12());
            stmt.setInt(3, s.getSystemYU12());
            stmt.setInt(4, s.getSystemZU12());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    s.setId(generatedKeys.getInt(1));
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
    
    //update a SolarSystem by ID
    public boolean update(SolarSystem s) {
        String sql = "UPDATE solar_systems SET name = ?, system_x_u12 = ?, system_y_u12 = ?, system_z_u12 = ? WHERE id = ?";
        boolean updated = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, s.getName());
            stmt.setInt(2, s.getSystemXU12());
            stmt.setInt(3, s.getSystemYU12());
            stmt.setInt(4, s.getSystemZU12());
            stmt.setInt(5, s.getId());

            updated = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return updated;
    }

    //delete a solar system by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM solar_systems WHERE id = ?";
        boolean deleted = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            deleted = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return deleted;
    }

}
