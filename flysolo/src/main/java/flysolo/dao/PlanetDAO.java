package flysolo.dao;

import flysolo.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import flysolo.entities.Planet;

public class PlanetDAO {

    //list all planets with solar system name
    public ArrayList<Planet> list() {
        ArrayList<Planet> planets = new ArrayList<>();

        String sql = "SELECT p.*, s.name as solar_system_name " +
                     "FROM planets p " +
                     "INNER JOIN solar_systems s ON p.solar_system_id = s.id " +
                     "ORDER BY s.name, p.name";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Planet p = new Planet();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setSolarSystemId(rs.getInt("solar_system_id"));
                p.setPlanetXU6(rs.getInt("planet_x_u6"));
                p.setPlanetYU6(rs.getInt("planet_y_u6"));
                p.setPlanetZU6(rs.getInt("planet_z_u6"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUpdatedAt(rs.getTimestamp("updated_at"));
                p.setSolarSystemName(rs.getString("solar_system_name"));

                planets.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return planets;
    }
    
    //find a planet by ID
    public Planet search(int id) {
        Planet planet = null;
        String sql = "SELECT p.*, s.name as solar_system_name " +
                     "FROM planets p " +
                     "INNER JOIN solar_systems s ON p.solar_system_id = s.id " +
                     "WHERE p.id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                planet = new Planet();
                planet.setId(rs.getInt("id"));
                planet.setName(rs.getString("name"));
                planet.setSolarSystemId(rs.getInt("solar_system_id"));
                planet.setPlanetXU6(rs.getInt("planet_x_u6"));
                planet.setPlanetYU6(rs.getInt("planet_y_u6"));
                planet.setPlanetZU6(rs.getInt("planet_z_u6"));
                planet.setCreatedAt(rs.getTimestamp("created_at"));
                planet.setUpdatedAt(rs.getTimestamp("updated_at"));
                planet.setSolarSystemName(rs.getString("solar_system_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return planet;
    }
    
    //list planets by solar system
    public ArrayList<Planet> listBySolarSystem(int solarSystemId) {
        ArrayList<Planet> planets = new ArrayList<>();
        String sql = "SELECT p.*, s.name as solar_system_name " +
                     "FROM planets p " +
                     "INNER JOIN solar_systems s ON p.solar_system_id = s.id " +
                     "WHERE p.solar_system_id = ? " +
                     "ORDER BY p.name";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, solarSystemId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Planet p = new Planet();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setSolarSystemId(rs.getInt("solar_system_id"));
                p.setPlanetXU6(rs.getInt("planet_x_u6"));
                p.setPlanetYU6(rs.getInt("planet_y_u6"));
                p.setPlanetZU6(rs.getInt("planet_z_u6"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUpdatedAt(rs.getTimestamp("updated_at"));
                p.setSolarSystemName(rs.getString("solar_system_name"));

                planets.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return planets;
    }
    
    //insert a planet in the DB
    public boolean insert(Planet p) {
        String sql = "INSERT INTO planets (name, solar_system_id, planet_x_u6, planet_y_u6, planet_z_u6) VALUES (?, ?, ?, ?, ?)";
        boolean inserted = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getSolarSystemId());
            stmt.setInt(3, p.getPlanetXU6());
            stmt.setInt(4, p.getPlanetYU6());
            stmt.setInt(5, p.getPlanetZU6());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
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
    
    //update a Planet by ID
    public boolean update(Planet p) {
        String sql = "UPDATE planets SET name = ?, solar_system_id = ?, planet_x_u6 = ?, planet_y_u6 = ?, planet_z_u6 = ? WHERE id = ?";
        boolean updated = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getSolarSystemId());
            stmt.setInt(3, p.getPlanetXU6());
            stmt.setInt(4, p.getPlanetYU6());
            stmt.setInt(5, p.getPlanetZU6());
            stmt.setInt(6, p.getId());

            updated = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return updated;
    }

    //delete a planet by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM planets WHERE id = ?";
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
