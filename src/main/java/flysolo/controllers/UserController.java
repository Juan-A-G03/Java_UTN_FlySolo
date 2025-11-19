package flysolo.controllers;

import flysolo.dao.UserDAO;
import flysolo.entities.User;
import java.util.ArrayList;

public class UserController {

    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    // Listar todos los usuarios
    public ArrayList<User> getAllUsers() {
        return userDAO.list();
    }

    // Obtener usuario por ID
    public User getUserById(long id) {
        return userDAO.search(id);
    }

    // Obtener usuario por email
    public User getUserByEmail(String email) {
        return userDAO.searchByEmail(email);
    }

    // Obtener usuarios por tipo
    public ArrayList<User> getUsersByType(String userType) {
        return userDAO.listByType(userType);
    }

    // Obtener todos los pasajeros
    public ArrayList<User> getAllPassengers() {
        return userDAO.listByType("PASSENGER");
    }

    // Obtener todos los pilotos
    public ArrayList<User> getAllPilots() {
        return userDAO.listByType("PILOT");
    }

    // Obtener todos los administradores
    public ArrayList<User> getAllAdmins() {
        return userDAO.listByType("ADMIN");
    }

    // Obtener pilotos por estado
    public ArrayList<User> getPilotsByStatus(String status) {
        return userDAO.listPilotsByStatus(status);
    }

    // Obtener pilotos pendientes (para aprobación por admin)
    public ArrayList<User> getPendingPilots() {
        return userDAO.listPilotsByStatus("PENDIENTE");
    }

    // Obtener pilotos no aprobados (incluye PENDIENTE, RECHAZADO o sin estado APROBADO)
    public ArrayList<User> getPilotsNotApproved() {
        return userDAO.listPilotsNotApproved();
    }

    // Obtener pilotos aprobados
    public ArrayList<User> getApprovedPilots() {
        return userDAO.listPilotsByStatus("APROBADO");
    }

    // Obtener pilotos rechazados
    public ArrayList<User> getRejectedPilots() {
        return userDAO.listPilotsByStatus("RECHAZADO");
    }

    // Crear nuevo usuario
    public boolean createUser(User user) {
        // Validar datos del usuario
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return false;
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Error: El email es obligatorio");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Error: La contraseña es obligatoria");
            return false;
        }              
        if (user.getFaction() == null || user.getFaction().trim().isEmpty()) {
            System.out.println("Error: La facción es obligatoria");
            return false;
        }
        if (user.getUserType() == null || user.getUserType().trim().isEmpty()) {
            System.out.println("Error: El tipo de usuario es obligatorio");
            return false;
        }

        // Validar facción
        if (!isValidFaction(user.getFaction())) {
            System.out.println("Error: Facción inválida. Debe ser IMPERIAL, REBEL o NEUTRAL");
            return false;
        }

        // Validar tipo de usuario
        if (!isValidUserType(user.getUserType())) {
            System.out.println("Error: Tipo de usuario inválido. Debe ser PASSENGER, PILOT o ADMIN");
            return false;
        }

        // Comprobar si el email ya existe
        User existingUser = userDAO.searchByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("Error: El email ya existe");
            return false;
        }

        return userDAO.insert(user);
    }

    // Registrar nuevo pasajero
    public boolean registerPassenger(String name, String email, String password, String faction) {
        User user = new User(name, email, password, faction, "PASSENGER");
        return createUser(user);
    }

    // Solicitar estado de piloto (el pasajero solicita ser piloto)
    public boolean requestPilotStatus(long userId, String licenseNumber) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }

        if (!"PASSENGER".equals(user.getUserType())) {
            System.out.println("Error: Solo los pasajeros pueden solicitar estado de piloto");
            return false;
        }

        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            System.out.println("Error: El número de licencia es obligatorio");
            return false;
        }

        user.setLicenseNumber(licenseNumber);
        user.setPilotStatus("PENDIENTE");

        return userDAO.update(user);
    }

    // El admin aprueba la solicitud de piloto
    public boolean approvePilot(long userId) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }

        // No aprobar si ya está aprobado
        if ("APROBADO".equals(user.getPilotStatus())) {
            System.out.println("Error: El usuario ya está aprobado como piloto");
            return false;
        }

        // Aprobar independientemente del estado actual (null, PENDIENTE o RECHAZADO)
        user.setUserType("PILOT");
        user.setPilotStatus("APROBADO");

        return userDAO.update(user);
    }

    // El admin rechaza la solicitud de piloto
    public boolean rejectPilot(long userId) {
        User user = userDAO.search(userId);
        if (user == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }

        // No rechazar si ya está aprobado
        if ("APROBADO".equals(user.getPilotStatus())) {
            System.out.println("Error: No se puede rechazar a un piloto ya aprobado");
            return false;
        }

        user.setPilotStatus("RECHAZADO");

        return userDAO.update(user);
    }

    // Actualizar usuario
    public boolean updateUser(User user) {
        if (user.getId() == null) {
            System.out.println("Error: Se requiere ID de usuario");
            return false;
        }

        User existingUser = userDAO.search(user.getId());
        if (existingUser == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }

        // Validar facción si se cambió
        if (user.getFaction() != null && !isValidFaction(user.getFaction())) {
            System.out.println("Error: Facción inválida");
            return false;
        }

        // Validar tipo de usuario si se cambió
        if (user.getUserType() != null && !isValidUserType(user.getUserType())) {
            System.out.println("Error: Tipo de usuario inválido");
            return false;
        }

        return userDAO.update(user);
    }

    // Eliminar usuario
    public boolean deleteUser(long id) {
        User user = userDAO.search(id);
        if (user == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }

        return userDAO.delete(id);
    }

    // Método helper para validar facción
    private boolean isValidFaction(String faction) {
        return "IMPERIAL".equals(faction) || "REBEL".equals(faction) || "NEUTRAL".equals(faction);
    }

    // Método helper para validar tipo de usuario
    private boolean isValidUserType(String userType) {
        return "PASSENGER".equals(userType) || "PILOT".equals(userType) || "ADMIN".equals(userType);
    }

    // Método helper para validar estado de piloto
    private boolean isValidPilotStatus(String pilotStatus) {
        return "PENDIENTE".equals(pilotStatus) || "APROBADO".equals(pilotStatus) || "RECHAZADO".equals(pilotStatus);
    }
}