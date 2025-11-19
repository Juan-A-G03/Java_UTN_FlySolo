package flysolo.controllers;

import flysolo.dao.TripDAO;
import flysolo.dao.PlanetDAO;
import flysolo.dao.SolarSystemDAO;
import flysolo.entities.Trip;
import flysolo.entities.Planet;
import flysolo.entities.SolarSystem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class TripController {

    private TripDAO tripDAO;
    private PlanetDAO planetDAO;
    private SolarSystemDAO solarSystemDAO;
    
    // Pricing constants
    private static final double BASE_PRICE_PER_KM = 0.00001;  // Precio base por km
    private static final double UNDERCOVER_MULTIPLIER = 1.5;  // +50% para viajes encubiertos
    private static final double IMMEDIATE_MULTIPLIER = 1.2;   // +20% para viajes inmediatos

    public TripController() {
        this.tripDAO = new TripDAO();
        this.planetDAO = new PlanetDAO();
        this.solarSystemDAO = new SolarSystemDAO();
    }

    // Get all trips
    public ArrayList<Trip> getAllTrips() {
        return tripDAO.list();
    }

    // Get trip by ID
    public Trip getTripById(long id) {
        return tripDAO.search(id);
    }

    // Get trips by passenger
    public ArrayList<Trip> getTripsByPassenger(long passengerUserId) {
        return tripDAO.listByPassenger(passengerUserId);
    }

    // Get trips by pilot
    public ArrayList<Trip> getTripsByPilot(long pilotUserId) {
        return tripDAO.listByPilot(pilotUserId);
    }

    // Get trips by status
    public ArrayList<Trip> getTripsByStatus(String status) {
        return tripDAO.listByStatus(status);
    }

    // Get pending trips (for pilots to accept)
    public ArrayList<Trip> getPendingTrips() {
        return tripDAO.listByStatus("PENDIENTE");
    }

    // Calculate distance between two planets in kilometers
    public double calculateDistance(int originPlanetId, int destinationPlanetId) {
        Planet originPlanet = planetDAO.search(originPlanetId);
        Planet destPlanet = planetDAO.search(destinationPlanetId);

        if (originPlanet == null || destPlanet == null) {
            System.out.println("Error: One or both planets not found");
            return 0;
        }

        SolarSystem originSystem = solarSystemDAO.search(originPlanet.getSolarSystemId());
        SolarSystem destSystem = solarSystemDAO.search(destPlanet.getSolarSystemId());

        if (originSystem == null || destSystem == null) {
            System.out.println("Error: One or both solar systems not found");
            return 0;
        }

        // Calculate absolute positions in km
        // Position = (system_coord * 10^12) + (planet_offset * 10^6)
        double originX = (originSystem.getSystemXU12() * 1e12) + (originPlanet.getPlanetXU6() * 1e6);
        double originY = (originSystem.getSystemYU12() * 1e12) + (originPlanet.getPlanetYU6() * 1e6);
        double originZ = (originSystem.getSystemZU12() * 1e12) + (originPlanet.getPlanetZU6() * 1e6);

        double destX = (destSystem.getSystemXU12() * 1e12) + (destPlanet.getPlanetXU6() * 1e6);
        double destY = (destSystem.getSystemYU12() * 1e12) + (destPlanet.getPlanetYU6() * 1e6);
        double destZ = (destSystem.getSystemZU12() * 1e12) + (destPlanet.getPlanetZU6() * 1e6);

        // Euclidean distance in 3D space
        double deltaX = destX - originX;
        double deltaY = destY - originY;
        double deltaZ = destZ - originZ;

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    // Calculate trip price based on distance, mode, and type
    public BigDecimal calculatePrice(int originPlanetId, int destinationPlanetId, 
            String tripMode, String tripType) {

		Planet originPlanet = planetDAO.search(originPlanetId);
		Planet destPlanet = planetDAO.search(destinationPlanetId);
		
		SolarSystem originSystem = solarSystemDAO.search(originPlanet.getSolarSystemId());
		SolarSystem destSystem = solarSystemDAO.search(destPlanet.getSolarSystemId());
		
		// Calculamos la distancia (aunque quizás no se use si es interestelar)
		double distance = calculateDistance(originPlanetId, destinationPlanetId);
		
		// Detectamos si están en distintos sistemas solares
		boolean isInterstellar = originSystem.getId() != destSystem.getId();
		
		double basePrice;
		
		if (isInterstellar) {
		// Precio fijo para salto interestelar
		basePrice = 5000;  
		} else {
		// Precio basado en distancia dentro del mismo sistema
		basePrice = distance * BASE_PRICE_PER_KM;
		}
		
		// Aplicamos multipliers existentes
		if ("UNDERCOVER".equals(tripMode)) {
		basePrice *= UNDERCOVER_MULTIPLIER;
		}
		
		if ("INMEDIATO".equals(tripType)) {
		basePrice *= IMMEDIATE_MULTIPLIER;
		}
		
		return new BigDecimal(basePrice).setScale(2, RoundingMode.HALF_UP);
		}


    // Create new trip request
    public boolean createTripRequest(long passengerUserId, int originPlanetId, 
                                     int destinationPlanetId, String tripMode, 
                                     String tripType, String notes) {
        // Validate inputs
        if (originPlanetId == destinationPlanetId) {
            System.out.println("Error: Origin and destination cannot be the same");
            return false;
        }

        // Validate trip mode
        if (!isValidTripMode(tripMode)) {
            System.out.println("Error: Invalid trip mode. Must be NORMAL or UNDERCOVER");
            return false;
        }

        // Validate trip type
        if (!isValidTripType(tripType)) {
            System.out.println("Error: Invalid trip type. Must be INMEDIATO or PROGRAMADO");
            return false;
        }

        // Calculate price
        BigDecimal price = calculatePrice(originPlanetId, destinationPlanetId, tripMode, tripType);

        if (price.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Error: Failed to calculate price");
            return false;
        }

        // Create trip
        Trip trip = new Trip(passengerUserId, originPlanetId, destinationPlanetId, 
                            tripMode, tripType, price);
        trip.setNotes(notes);

        return tripDAO.insert(trip);
    }

    // Pilot accepts a trip
    public boolean acceptTrip(long tripId, long pilotUserId) {
        Trip trip = tripDAO.search(tripId);

        if (trip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        if (!"PENDIENTE".equals(trip.getStatus())) {
            System.out.println("Error: Trip is not in PENDIENTE status");
            return false;
        }

        trip.setPilotUserId(pilotUserId);
        trip.setStatus("CONFIRMADO");

        return tripDAO.update(trip);
    }

    // Start a trip
    public boolean startTrip(long tripId) {
        Trip trip = tripDAO.search(tripId);

        if (trip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        if (!"CONFIRMADO".equals(trip.getStatus())) {
            System.out.println("Error: Trip must be CONFIRMADO to start");
            return false;
        }

        trip.setStatus("EN_CURSO");
        trip.setStartedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        return tripDAO.update(trip);
    }

    // Complete a trip
    public boolean completeTrip(long tripId) {
        Trip trip = tripDAO.search(tripId);

        if (trip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        if (!"EN_CURSO".equals(trip.getStatus())) {
            System.out.println("Error: Trip must be EN_CURSO to complete");
            return false;
        }

        trip.setStatus("COMPLETADO");
        trip.setCompletedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        return tripDAO.update(trip);
    }

    // Cancel a trip
    public boolean cancelTrip(long tripId, String cancelReason) {
        Trip trip = tripDAO.search(tripId);

        if (trip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        if ("COMPLETADO".equals(trip.getStatus()) || "CANCELADO".equals(trip.getStatus())) {
            System.out.println("Error: Cannot cancel a completed or already canceled trip");
            return false;
        }

        trip.setStatus("CANCELADO");
        trip.setCanceledAt(new java.sql.Timestamp(System.currentTimeMillis()));
        trip.setCancelReason(cancelReason);

        return tripDAO.update(trip);
    }

    // Update trip
    public boolean updateTrip(Trip trip) {
        if (trip.getId() == null) {
            System.out.println("Error: Trip ID is required");
            return false;
        }

        Trip existingTrip = tripDAO.search(trip.getId());
        if (existingTrip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        return tripDAO.update(trip);
    }

    // Delete trip
    public boolean deleteTrip(long id) {
        Trip trip = tripDAO.search(id);
        if (trip == null) {
            System.out.println("Error: Trip not found");
            return false;
        }

        return tripDAO.delete(id);
    }

    // Validation helpers
    private boolean isValidTripMode(String tripMode) {
        return "NORMAL".equals(tripMode) || "UNDERCOVER".equals(tripMode);
    }

    private boolean isValidTripType(String tripType) {
        return "INMEDIATO".equals(tripType) || "PROGRAMADO".equals(tripType);
    }
}
