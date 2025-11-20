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

@WebServlet(urlPatterns = {"/manageTrip", "/manageTripAction"})
public class ManageTripServlet extends HttpServlet {

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
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Move flash messages from session to request
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

        String tripIdParam = request.getParameter("tripId");
        if (tripIdParam == null) {
            session.setAttribute("error", "ID de viaje inválido.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
            return;
        }

        try {
            long tripId = Long.parseLong(tripIdParam);
            Trip trip = tripController.getTripById(tripId);
            if (trip == null) {
                session.setAttribute("error", "Viaje no encontrado.");
                response.sendRedirect(request.getContextPath() + "/myTrips");
                return;
            }

            request.setAttribute("trip", trip);
            request.getRequestDispatcher("/views/manageTrip.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            session.setAttribute("error", "ID de viaje inválido.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Only pilots can manage trips
        if (!user.isPilot() || !user.isApprovedPilot()) {
            session.setAttribute("error", "No tienes permiso para gestionar viajes.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
            return;
        }

        String tripIdParam = request.getParameter("tripId");
        String action = request.getParameter("action");

        if (tripIdParam == null || action == null) {
            session.setAttribute("error", "Parámetros inválidos.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
            return;
        }

        try {
            long tripId = Long.parseLong(tripIdParam);
            boolean ok = false;

            switch (action) {
                case "start":
                    ok = tripController.startTrip(tripId);
                    if (ok) session.setAttribute("success", "Viaje iniciado correctamente.");
                    else session.setAttribute("error", "No se pudo iniciar el viaje.");
                    break;
                case "complete":
                    ok = tripController.completeTrip(tripId);
                    if (ok) session.setAttribute("success", "Viaje finalizado correctamente.");
                    else session.setAttribute("error", "No se pudo finalizar el viaje.");
                    break;
                case "cancel":
                    String cancelReason = request.getParameter("cancelReason");
                    if (cancelReason == null || cancelReason.trim().isEmpty()) {
                        session.setAttribute("error", "Debe indicar un motivo para la cancelación.");
                        response.sendRedirect(request.getContextPath() + "/manageTrip?tripId=" + tripId);
                        return;
                    }
                    ok = tripController.cancelTrip(tripId, cancelReason);
                    if (ok) session.setAttribute("success", "Viaje cancelado correctamente.");
                    else session.setAttribute("error", "No se pudo cancelar el viaje.");
                    break;
                default:
                    session.setAttribute("error", "Acción desconocida.");
            }

            response.sendRedirect(request.getContextPath() + "/manageTrip?tripId=" + tripId);

        } catch (NumberFormatException ex) {
            session.setAttribute("error", "ID de viaje inválido.");
            response.sendRedirect(request.getContextPath() + "/myTrips");
        }
    }
}
