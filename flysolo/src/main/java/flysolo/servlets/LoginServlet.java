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
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // TODO: Implementar password hashing para la promocion
        // Por ahora solo verificamos que el usuario exista con una password simple
        
        if (password == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
        User user = userController.getUserByEmail(email);

        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userType", user.getUserType());
            session.setAttribute("userFaction", user.getFaction());

            // Redirect based on user type
            if (user.isAdmin()) {
                response.sendRedirect("dashboard?view=admin");
            } else if (user.isPilot()) {
                response.sendRedirect("dashboard?view=pilot");
            } else {
                response.sendRedirect("views/home.jsp");
            }
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
