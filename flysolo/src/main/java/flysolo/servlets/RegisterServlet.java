package flysolo.servlets;

import flysolo.controllers.UserController;
import flysolo.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserController userController;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Show register form
        request.getRequestDispatcher("/register.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String faction = request.getParameter("faction");
        String password = request.getParameter("password");

        // Validate input
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Name is required");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        if (password == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }
        
        if (faction == null || faction.trim().isEmpty()) {
            request.setAttribute("error", "Faction is required");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        // Check if email already exists
        User existingUser = userController.getUserByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "Email already registered");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("faction", faction);
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        // Register new passenger (default user type)
        // TODO: Add password hashing when you add password column
        boolean registered = userController.registerPassenger(name, email, password, faction);

        if (registered) {
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.html").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("faction", faction);
            request.getRequestDispatcher("/register.html").forward(request, response);
        }
    }
}
