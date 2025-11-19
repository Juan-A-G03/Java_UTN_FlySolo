package flysolo.servlets;

import flysolo.controllers.TripController;
import flysolo.dao.PlanetDAO;
import flysolo.entities.Planet;
import flysolo.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private TripController tripController;
    private PlanetDAO planetDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.tripController = new TripController();
        this.planetDAO = new PlanetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        // Cargar todos los planetas para los dropdowns
        ArrayList<Planet> planets = planetDAO.list();
        request.setAttribute("planets", planets);

        // Forward a la página principal
        request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if ("calculatePrice".equals(action)) {
            calculatePrice(request, response);
        } else if ("requestTrip".equals(action)) {
            requestTrip(request, response);
        } else {
            response.sendRedirect("home");
        }
    }

    private void calculatePrice(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int originPlanetId = Integer.parseInt(request.getParameter("originPlanetId"));
            int destinationPlanetId = Integer.parseInt(request.getParameter("destinationPlanetId"));
            String tripMode = request.getParameter("tripMode");
            String tripType = request.getParameter("tripType");

            // Validar mismo planeta
            if (originPlanetId == destinationPlanetId) {
                request.setAttribute("error", "El origen y destino no pueden ser el mismo planeta");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                return;
            }

            // Calcular precio
            BigDecimal price = tripController.calculatePrice(originPlanetId, destinationPlanetId, tripMode, tripType);
            double distance = tripController.calculateDistance(originPlanetId, destinationPlanetId);

            // Obtener nombres de los planetas para mostrar
            Planet originPlanet = planetDAO.search(originPlanetId);
            Planet destPlanet = planetDAO.search(destinationPlanetId);

            // Establecer atributos para preservar datos del formulario
            request.setAttribute("originPlanetId", originPlanetId);
            request.setAttribute("destinationPlanetId", destinationPlanetId);
            request.setAttribute("tripMode", tripMode);
            request.setAttribute("tripType", tripType);
            request.setAttribute("calculatedPrice", price);
            request.setAttribute("distance", distance);
            request.setAttribute("originPlanet", originPlanet);
            request.setAttribute("destPlanet", destPlanet);

            // Recargar planetas
            ArrayList<Planet> planets = planetDAO.list();
            request.setAttribute("planets", planets);

            request.getRequestDispatcher("/views/home.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Selección de planeta inválida");
            ArrayList<Planet> planets = planetDAO.list();

             // DEBUG: imprimir planetas en consola
             System.out.println("=== PLANETS FROM DAO ===");
             if (planets == null) {
                 System.out.println("La lista de planetas es NULL");
             } else if (planets.isEmpty()) {
                 System.out.println("La lista de planetas está vacía");
             } else {
                 for (Planet p : planets) {
                     System.out.println("Planet: id=" + p.getId() + ", name=" + p.getName());
                 }
             }

            request.setAttribute("planets", planets);
            request.getRequestDispatcher("/views/home.jsp").forward(request, response);
        }
    }

    private void requestTrip(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isPassenger()) {
            request.setAttribute("error", "Solo los pasajeros pueden solicitar viajes");
            response.sendRedirect("home");
            return;
        }

        try {
            int originPlanetId = Integer.parseInt(request.getParameter("originPlanetId"));
            int destinationPlanetId = Integer.parseInt(request.getParameter("destinationPlanetId"));
            String tripMode = request.getParameter("tripMode");
            String tripType = request.getParameter("tripType");
            String notes = request.getParameter("notes");

            // Validar mismo planeta
            if (originPlanetId == destinationPlanetId) {
                request.setAttribute("error", "El origen y destino no pueden ser el mismo planeta");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                return;
            }

            // Crear solicitud de viaje
            boolean created = tripController.createTripRequest(
                user.getId(), 
                originPlanetId, 
                destinationPlanetId, 
                tripMode, 
                tripType, 
                notes
            );

            if (created) {
                request.setAttribute("success", "Solicitud de viaje creada correctamente. Esperando a que un piloto la acepte.");
                response.sendRedirect("myTrips");
            } else {
                request.setAttribute("error", "No se pudo crear la solicitud de viaje.");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Selección de planeta inválida");
            ArrayList<Planet> planets = planetDAO.list();
            request.setAttribute("planets", planets);
            request.getRequestDispatcher("/views/home.jsp").forward(request, response);
        }
    }
}