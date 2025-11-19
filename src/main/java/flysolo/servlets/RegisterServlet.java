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
        // Mostrar formulario de registro
        request.getRequestDispatcher("/register.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String faction = request.getParameter("faction");
        String password = request.getParameter("password");

        // Validar entrada
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "El nombre es obligatorio");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "El email es obligatorio");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        if (password == null || email.trim().isEmpty()) {
            request.setAttribute("error", "La contraseña es obligatoria");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }
        
        if (faction == null || faction.trim().isEmpty()) {
            request.setAttribute("error", "La facción es obligatoria");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        // Comprobar si el email ya existe
        User existingUser = userController.getUserByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "El email ya está registrado");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("faction", faction);
            request.getRequestDispatcher("/register.html").forward(request, response);
            return;
        }

        // Registrar nuevo pasajero (tipo de usuario por defecto)
        // TODO: Añadir hashing de contraseña cuando se agregue la columna de contraseña
        boolean registered = userController.registerPassenger(name, email, password, faction);

        if (registered) {
            request.setAttribute("success", "Registro exitoso. Por favor, inicia sesión.");
            request.getRequestDispatcher("/login.html").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrarse. Intente nuevamente.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("faction", faction);
            request.getRequestDispatcher("/register.html").forward(request, response);
        }
    }
}