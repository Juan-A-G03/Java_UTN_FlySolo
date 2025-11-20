package flysolo.servlets;

import flysolo.controllers.TripController;
import flysolo.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/acceptTrip")
public class AcceptTripServlet extends HttpServlet {

    private TripController tripController;

    @Override
    public void init() throws ServletException {
        super.init();
        this.tripController = new TripController();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Solo pilotos aprobados pueden aceptar viajes
        if (!user.isPilot() || !user.isApprovedPilot()) {
            session.setAttribute("error", "No tienes permiso para aceptar viajes.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
            return;
        }

        String tripIdParam = request.getParameter("tripId");
        if (tripIdParam == null) {
            session.setAttribute("error", "ID de viaje inválido.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
            return;
        }

        try {
            long tripId = Long.parseLong(tripIdParam);
            boolean ok = tripController.acceptTrip(tripId, user.getId());
            if (ok) {
                session.setAttribute("success", "Has aceptado el viaje correctamente.");
                // Redirigir a la página de gestión del viaje para que el piloto pueda iniciarlo/finalizarlo/cancelarlo
                response.sendRedirect(request.getContextPath() + "/manageTrip?tripId=" + tripId);
                return;
            } else {
                session.setAttribute("error", "No se pudo aceptar el viaje. Asegúrate de que sigue en estado PENDIENTE.");
            }
        } catch (NumberFormatException ex) {
            session.setAttribute("error", "ID de viaje inválido.");
        }

        response.sendRedirect(request.getContextPath() + "/myTrips");
    }
}