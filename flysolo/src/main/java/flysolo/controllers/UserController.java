package flysolo.controllers;

import flysolo.dao.UserDAO;
import flysolo.entities.User;
import java.util.ArrayList;

public class UserController {

    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    // List all users
    public ArrayList<User> getAllUsers() {
        return userDAO.list();
    }

    // Get user by ID
    public User getUserById(long id) {
        return userDAO.search(id);
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userDAO.searchByEmail(email);
    }

    // Get users by type
    public ArrayList<User> getUsersByType(String userType) {
        return userDAO.listByType(userType);
    }

    // Get all passengers
    public ArrayList<User> getAllPassengers() {
        return userDAO.listByType("PASSENGER");
    }

    // Get all pilots
    public ArrayList<User> getAllPilots() {
        return userDAO.listByType("PILOT");
    }

    // Get all admins
    public ArrayList<User> getAllAdmins() {
        return userDAO.listByType("ADMIN");
    }

    // Get pilots by status
    public ArrayList<User> getPilotsByStatus(String status) {
        return userDAO.listPilotsByStatus(status);
    }

    // Get pending pilots (for admin approval)
    public ArrayList<User> getPendingPilots() {
        return userDAO.listPilotsByStatus("PENDIENTE");
    }

    // Get approved pilots
    public ArrayList<User> getApprovedPilots() {
        return userDAO.listPilotsByStatus("APROBADO");
    }

    // Get rejected pilots
    public ArrayList<User> getRejectedPilots() {
        return userDAO.listPilotsByStatus("RECHAZADO");
    }

    // Create new user
    public boolean createUser(User user) {
        // Validate user data
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Error: Name is required");
            return false;
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Error: Email is required");
            return false;
        }
        if (user.getFaction() == null || user.getFaction().trim().isEmpty()) {
            System.out.println("Error: Faction is required");
            return false;
        }
        if (user.getUserType() == null || user.getUserType().trim().isEmpty()) {
            System.out.println("Error: User type is required");
            return false;
        }

        // Validate faction
        if (!isValidFaction(user.getFaction())) {
            System.out.println("Error: Invalid faction. Must be IMPERIAL, REBEL, or NEUTRAL");
            return false;
        }

        // Validate user type
        if (!isValidUserType(user.getUserType())) {
            System.out.println("Error: Invalid user type. Must be PASSENGER, PILOT, or ADMIN");
            return false;
        }

        // Check if email already exists
        User existingUser = userDAO.searchByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("Error: Email already exists");
            return false;
        }

        return userDAO.insert(user);
    }

    // Register new passenger
    public boolean registerPassenger(String name, String email, String faction) {
        User user = new User(name, email, faction, "PASSENGER");
        return createUser(user);
    }

    // Request to become a pilot (passenger applies to be pilot)
    public boolean requestPilotStatus(long userId, String licenseNumber) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        if (!"PASSENGER".equals(user.getUserType())) {
            System.out.println("Error: Only passengers can request pilot status");
            return false;
        }

        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            System.out.println("Error: License number is required");
            return false;
        }

        user.setLicenseNumber(licenseNumber);
        user.setPilotStatus("PENDIENTE");

        return userDAO.update(user);
    }

    // Admin approves pilot request
    public boolean approvePilot(long userId) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        if (!"PENDIENTE".equals(user.getPilotStatus())) {
            System.out.println("Error: User does not have a pending pilot request");
            return false;
        }

        user.setUserType("PILOT");
        user.setPilotStatus("APROBADO");

        return userDAO.update(user);
    }

    // Admin rejects pilot request
    public boolean rejectPilot(long userId) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        if (!"PENDIENTE".equals(user.getPilotStatus())) {
            System.out.println("Error: User does not have a pending pilot request");
            return false;
        }

        user.setPilotStatus("RECHAZADO");

        return userDAO.update(user);
    }

    // Update user
    public boolean updateUser(User user) {
        if (user.getId() == null) {
            System.out.println("Error: User ID is required");
            return false;
        }

        User existingUser = userDAO.search(user.getId());
        if (existingUser == null) {
            System.out.println("Error: User not found");
            return false;
        }

        // Validate faction if changed
        if (user.getFaction() != null && !isValidFaction(user.getFaction())) {
            System.out.println("Error: Invalid faction");
            return false;
        }

        // Validate user type if changed
        if (user.getUserType() != null && !isValidUserType(user.getUserType())) {
            System.out.println("Error: Invalid user type");
            return false;
        }

        return userDAO.update(user);
    }

    // Delete user
    public boolean deleteUser(long id) {
        User user = userDAO.search(id);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        return userDAO.delete(id);
    }

    // Helper method to validate faction
    private boolean isValidFaction(String faction) {
        return "IMPERIAL".equals(faction) || "REBEL".equals(faction) || "NEUTRAL".equals(faction);
    }

    // Helper method to validate user type
    private boolean isValidUserType(String userType) {
        return "PASSENGER".equals(userType) || "PILOT".equals(userType) || "ADMIN".equals(userType);
    }

    // Helper method to validate pilot status
    private boolean isValidPilotStatus(String pilotStatus) {
        return "PENDIENTE".equals(pilotStatus) || "APROBADO".equals(pilotStatus) || "RECHAZADO".equals(pilotStatus);
    }
}
