package flysolo.servlets;

import flysolo.controllers.TripController;
import flysolo.entities.Trip;
import flysolo.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/myTrips")
public class MyTripsServlet extends HttpServlet {

    private TripController tripController;

    @Override
    public void init() throws ServletException {
        super.init();
        this.tripController = new TripController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Mover mensajes flash de sesión a request 
        Object successFlash = session.getAttribute("success");
        if (successFlash != null) {
            request.setAttribute("success", successFlash);
            session.removeAttribute("success");
        }
        Object errorFlash = session.getAttribute("error");
        if (errorFlash != null) {
            request.setAttribute("error", errorFlash);
            session.removeAttribute("error");
        }

        // Leer parámetro view para que el piloto pueda elegir la vista
        // Valores posibles: "pending" (por defecto) o "history"
        String view = request.getParameter("view");
        if (view == null) {
            view = "pending";
        }

        // Si el usuario es piloto, mostrar la lista correspondiente
        if (user.isPilot()) {
            ArrayList<Trip> trips;
            if ("history".equals(view)) {
                // Mostrar todos los viajes que hizo el piloto a lo largo del tiempo
                trips = tripController.getTripsByPilot(user.getId());
                request.setAttribute("pilotViewMode", "history");
            } else {
                // Por defecto, mostrar viajes pendientes para aceptar
                trips = tripController.getPendingTrips();
                request.setAttribute("pilotViewMode", "pending");
            }

            request.setAttribute("trips", trips);
            request.setAttribute("isPilotView", true);
        } else {
            // Para pasajeros, mostrar mensaje de confirmación si viene de registrar un viaje
            request.setAttribute("isPilotView", false);
            request.setAttribute("infoMessage", "Se ha registrado tu viaje, te avisaremos cuando haya sido aceptado");
        }

        request.getRequestDispatcher("/views/myTrips.jsp").forward(request, response);
    }

}