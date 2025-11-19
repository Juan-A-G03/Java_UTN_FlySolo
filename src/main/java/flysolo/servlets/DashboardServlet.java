package flysolo.servlets;

import flysolo.controllers.UserController;
import flysolo.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private UserController userController;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String view = request.getParameter("view");

        if (currentUser.isAdmin()) {
            if (view == null || "admin".equals(view)) {
                // Panel principal de administración
                request.getRequestDispatcher("/WEB-INF/views/admin/home.jsp").forward(request, response);
                return;
            } else if ("manageUsers".equals(view)) {
                ArrayList<User> users = userController.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("/WEB-INF/views/admin/manageUsers.jsp").forward(request, response);
                return;
            } else if ("pendingPilots".equals(view)) {
                // Mostrar todos los pilotos no aprobados (pendientes o rechazados o solicitantes)
                ArrayList<User> pending = userController.getPilotsNotApproved();
                request.setAttribute("pendingPilots", pending);
                request.getRequestDispatcher("/WEB-INF/views/admin/pendingPilots.jsp").forward(request, response);
                return;
            }
        }

        // Si no es admin o la vista es desconocida, redirigir al home
        if (currentUser.isPilot()) {
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } else {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Manejar acciones del administrador: aprobar/rechazar piloto, eliminar usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!currentUser.isAdmin()) {
            response.sendRedirect("home");
            return;
        }

        String action = request.getParameter("action");
        String idStr = request.getParameter("userId");
        String msg = null;
        String type = null;
        if (idStr != null) {
            try {
                long userId = Long.parseLong(idStr);
                boolean result = false;
                if ("approvePilot".equals(action)) {
                    result = userController.approvePilot(userId);
                    if (result) { msg = "Piloto aprobado correctamente."; type = "success"; }
                    else { msg = "No se pudo aprobar al piloto."; type = "error"; }
                } else if ("rejectPilot".equals(action)) {
                    result = userController.rejectPilot(userId);
                    if (result) { msg = "Piloto rechazado correctamente."; type = "success"; }
                    else { msg = "No se pudo rechazar al piloto."; type = "error"; }
                } else if ("deleteUser".equals(action)) {
                    result = userController.deleteUser(userId);
                    if (result) { msg = "Usuario eliminado correctamente."; type = "success"; }
                    else { msg = "No se pudo eliminar el usuario."; type = "error"; }
                }
            } catch (NumberFormatException e) {
                // ignorar
                msg = "ID de usuario inválido.";
                type = "error";
            }
        }

        // Después de la acción, redirigir de nuevo a la vista relevante y pasar un mensaje flash
        String view = request.getParameter("view");
        if (view == null) view = "admin";
        StringBuilder redirect = new StringBuilder("dashboard?view=").append(view);
        if (msg != null) {
            String enc = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
            redirect.append("&msg=").append(enc).append("&type=").append(type == null ? "info" : type);
        }
        response.sendRedirect(redirect.toString());
    }
}