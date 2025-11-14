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

        // Load all planets for the dropdowns
        ArrayList<Planet> planets = planetDAO.list();
        request.setAttribute("planets", planets);

        // Forward to home page
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

            // Validate same planet
            if (originPlanetId == destinationPlanetId) {
                request.setAttribute("error", "Origin and destination cannot be the same planet");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                return;
            }

            // Calculate price
            BigDecimal price = tripController.calculatePrice(originPlanetId, destinationPlanetId, tripMode, tripType);
            double distance = tripController.calculateDistance(originPlanetId, destinationPlanetId);

            // Get planet names for display
            Planet originPlanet = planetDAO.search(originPlanetId);
            Planet destPlanet = planetDAO.search(destinationPlanetId);

            // Set attributes to preserve form data
            request.setAttribute("originPlanetId", originPlanetId);
            request.setAttribute("destinationPlanetId", destinationPlanetId);
            request.setAttribute("tripMode", tripMode);
            request.setAttribute("tripType", tripType);
            request.setAttribute("calculatedPrice", price);
            request.setAttribute("distance", distance);
            request.setAttribute("originPlanet", originPlanet);
            request.setAttribute("destPlanet", destPlanet);

            // Reload planets
            ArrayList<Planet> planets = planetDAO.list();
            request.setAttribute("planets", planets);

            request.getRequestDispatcher("/views/home.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid planet selection");
            ArrayList<Planet> planets = planetDAO.list();
            request.setAttribute("planets", planets);
            request.getRequestDispatcher("/views/home.jsp").forward(request, response);
        }
    }

    private void requestTrip(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isPassenger()) {
            request.setAttribute("error", "Only passengers can request trips");
            response.sendRedirect("home");
            return;
        }

        try {
            int originPlanetId = Integer.parseInt(request.getParameter("originPlanetId"));
            int destinationPlanetId = Integer.parseInt(request.getParameter("destinationPlanetId"));
            String tripMode = request.getParameter("tripMode");
            String tripType = request.getParameter("tripType");
            String notes = request.getParameter("notes");

            // Validate same planet
            if (originPlanetId == destinationPlanetId) {
                request.setAttribute("error", "Origin and destination cannot be the same planet");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                return;
            }

            // Create trip request
            boolean created = tripController.createTripRequest(
                user.getId(), 
                originPlanetId, 
                destinationPlanetId, 
                tripMode, 
                tripType, 
                notes
            );

            if (created) {
                request.setAttribute("success", "Trip request created successfully! Waiting for a pilot to accept.");
                response.sendRedirect("myTrips");
            } else {
                request.setAttribute("error", "Failed to create trip request");
                ArrayList<Planet> planets = planetDAO.list();
                request.setAttribute("planets", planets);
                request.getRequestDispatcher("/views/home.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid planet selection");
            ArrayList<Planet> planets = planetDAO.list();
            request.setAttribute("planets", planets);
            request.getRequestDispatcher("/views/home.jsp").forward(request, response);
        }
    }
}
