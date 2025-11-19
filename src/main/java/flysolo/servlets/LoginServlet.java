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
        // Mostrar formulario de login
        request.getRequestDispatcher("login.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "El email es obligatorio");
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }

        // Nota: implementar hashing de contraseñas en producción
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "La contraseña es obligatoria");
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }
        
        User user = userController.getUserByEmail(email);

        if (user != null) {
            // Crear sesión
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userType", user.getUserType());
            session.setAttribute("userFaction", user.getFaction());

            // Redirigir según tipo de usuario
            if (user.isAdmin()) {
                response.sendRedirect("dashboard?view=admin");
            } else if (user.isPilot()) {
                response.sendRedirect("dashboard?view=pilot");
            } else {
                response.sendRedirect("home");
            }
        } else {
            request.setAttribute("error", "Email o contraseña inválidos");
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.html").forward(request, response);
        }
    }
}