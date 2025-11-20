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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserController userController;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Show login form
        request.getRequestDispatcher("login.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }

        // TODO: Implementar password hashing para la promocion
        // Por ahora solo verificamos que el usuario exista con una password simple
        
        if (password == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }
        
        User user = userController.getUserByEmail(email);

        if (user != null) {
            // Crear sesion y almacenar atributos del usuario
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userType", user.getUserType());
            session.setAttribute("userFaction", user.getFaction());

            // Dirigir al dashboard correspondiente segun el tipo de usuario
            if (user.isAdmin()) {
                response.sendRedirect("dashboard?view=admin");
            } else if (user.isPilot()) {
                // Dirigir al panel del piloto
                response.sendRedirect("myTrips");
            } else {
                response.sendRedirect("home");
            }
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.html").forward(request, response);
        }
    }
}