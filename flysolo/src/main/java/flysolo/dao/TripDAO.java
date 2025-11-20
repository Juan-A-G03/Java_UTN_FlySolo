package flysolo.dao;

import flysolo.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import flysolo.entities.Trip;

public class TripDAO {

    //list all trips with full details
    public ArrayList<Trip> list() {
        ArrayList<Trip> trips = new ArrayList<>();

        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "ORDER BY t.requested_at DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Trip t = mapResultSetToTrip(rs);
                trips.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trips;
    }
    
    //find a trip by ID
    public Trip search(long id) {
        Trip trip = null;
        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "WHERE t.id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                trip = mapResultSetToTrip(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trip;
    }
    
    //list trips by passenger
    public ArrayList<Trip> listByPassenger(long passengerUserId) {
        ArrayList<Trip> trips = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "WHERE t.passenger_user_id = ? " +
                     "ORDER BY t.requested_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, passengerUserId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Trip t = mapResultSetToTrip(rs);
                trips.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trips;
    }
    
    //list trips by pilot
    public ArrayList<Trip> listByPilot(long pilotUserId) {
        ArrayList<Trip> trips = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "WHERE t.pilot_user_id = ? " +
                     "ORDER BY t.requested_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, pilotUserId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Trip t = mapResultSetToTrip(rs);
                trips.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trips;
    }
    
    //list trips by status
    public ArrayList<Trip> listByStatus(String status) {
        ArrayList<Trip> trips = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "WHERE t.status = ? " +
                     "ORDER BY t.requested_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Trip t = mapResultSetToTrip(rs);
                trips.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trips;
    }
    
    // list only PENDIENTE trips that have no pilot assigned (defensive)
    public ArrayList<Trip> listPendingUnassigned() {
        ArrayList<Trip> trips = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "passenger.name as passenger_name, " +
                     "pilot.name as pilot_name, " +
                     "op.name as origin_planet_name, " +
                     "dp.name as destination_planet_name, " +
                     "os.name as origin_solar_system_name, " +
                     "ds.name as destination_solar_system_name " +
                     "FROM trips t " +
                     "INNER JOIN users passenger ON t.passenger_user_id = passenger.id " +
                     "LEFT JOIN users pilot ON t.pilot_user_id = pilot.id " +
                     "INNER JOIN planets op ON t.origin_planet_id = op.id " +
                     "INNER JOIN planets dp ON t.destination_planet_id = dp.id " +
                     "INNER JOIN solar_systems os ON op.solar_system_id = os.id " +
                     "INNER JOIN solar_systems ds ON dp.solar_system_id = ds.id " +
                     "WHERE t.status = 'PENDIENTE' AND t.pilot_user_id IS NULL " +
                     "ORDER BY t.requested_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Trip t = mapResultSetToTrip(rs);
                trips.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return trips;
    }
    
    //insert a trip in the DB
    public boolean insert(Trip t) {
        String sql = "INSERT INTO trips (passenger_user_id, pilot_user_id, origin_planet_id, destination_planet_id, trip_mode, type, status, price, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean inserted = false;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, t.getPassengerUserId());
            
            if (t.getPilotUserId() != null) {
                stmt.setLong(2, t.getPilotUserId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            
            stmt.setInt(3, t.getOriginPlanetId());
            stmt.setInt(4, t.getDestinationPlanetId());
            stmt.setString(5, t.getTripMode());
            stmt.setString(6, t.getType());
            stmt.setString(7, t.getStatus());
            stmt.setBigDecimal(8, t.getPrice());
            stmt.setString(9, t.getNotes());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    t.setId(generatedKeys.getLong(1));
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
    
    //update a Trip by ID
    public boolean update(Trip t) {
        String sql = "UPDATE trips SET passenger_user_id = ?, pilot_user_id = ?, origin_planet_id = ?, destination_planet_id = ?, trip_mode = ?, type = ?, status = ?, price = ?, started_at = ?, completed_at = ?, canceled_at = ?, cancel_reason = ?, notes = ? WHERE id = ?";
        boolean updated = false;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, t.getPassengerUserId());
            
            if (t.getPilotUserId() != null) {
                stmt.setLong(2, t.getPilotUserId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            
            stmt.setInt(3, t.getOriginPlanetId());
            stmt.setInt(4, t.getDestinationPlanetId());
            stmt.setString(5, t.getTripMode());
            stmt.setString(6, t.getType());
            stmt.setString(7, t.getStatus());
            stmt.setBigDecimal(8, t.getPrice());
            stmt.setTimestamp(9, t.getStartedAt());
            stmt.setTimestamp(10, t.getCompletedAt());
            stmt.setTimestamp(11, t.getCanceledAt());
            stmt.setString(12, t.getCancelReason());
            stmt.setString(13, t.getNotes());
            stmt.setLong(14, t.getId());

            updated = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, null);
        }

        return updated;
    }

    //delete a trip by ID
    public boolean delete(long id) {
        String sql = "DELETE FROM trips WHERE id = ?";
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
    
    // Helper method to map ResultSet to Trip
    private Trip mapResultSetToTrip(ResultSet rs) throws SQLException {
        Trip t = new Trip();
        t.setId(rs.getLong("id"));
        t.setPassengerUserId(rs.getLong("passenger_user_id"));
        
        long pilotId = rs.getLong("pilot_user_id");
        if (!rs.wasNull()) {
            t.setPilotUserId(pilotId);
        }
        
        t.setOriginPlanetId(rs.getInt("origin_planet_id"));
        t.setDestinationPlanetId(rs.getInt("destination_planet_id"));
        t.setTripMode(rs.getString("trip_mode"));
        t.setType(rs.getString("type"));
        t.setStatus(rs.getString("status"));
        t.setPrice(rs.getBigDecimal("price"));
        t.setRequestedAt(rs.getTimestamp("requested_at"));
        t.setStartedAt(rs.getTimestamp("started_at"));
        t.setCompletedAt(rs.getTimestamp("completed_at"));
        t.setCanceledAt(rs.getTimestamp("canceled_at"));
        t.setCancelReason(rs.getString("cancel_reason"));
        t.setNotes(rs.getString("notes"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Display fields
        t.setPassengerName(rs.getString("passenger_name"));
        t.setPilotName(rs.getString("pilot_name"));
        t.setOriginPlanetName(rs.getString("origin_planet_name"));
        t.setDestinationPlanetName(rs.getString("destination_planet_name"));
        t.setOriginSolarSystemName(rs.getString("origin_solar_system_name"));
        t.setDestinationSolarSystemName(rs.getString("destination_solar_system_name"));
        
        return t;
    }

}