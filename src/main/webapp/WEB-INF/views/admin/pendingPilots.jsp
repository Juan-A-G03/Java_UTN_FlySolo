<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="flysolo.entities.User" %>
<%!
    // Helper para escapar texto y prevenir XSS
    private String escHtml(String s) {
        if (s == null) return null;
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
%>
<%
    ArrayList<User> pending = (ArrayList<User>) request.getAttribute("pendingPilots");
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
    String flashSuccess = (String) session.getAttribute("flashSuccess");
    String flashError = (String) session.getAttribute("flashError");
    if (flashSuccess != null) { session.removeAttribute("flashSuccess"); }
    if (flashError != null) { session.removeAttribute("flashError"); }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Solicitudes de Pilotos - Panel Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="<%= themeClass %> admin-page">
    <div class="container">
        <h1>Solicitudes de Pilotos</h1>
        <nav class="admin-nav">
            <a href="dashboard?view=admin">Resumen</a> |
            <a href="dashboard?view=manageUsers">Gestionar Usuarios</a> |
            <a href="dashboard?view=pendingPilots">Pilotos Pendientes</a>
        </nav>

        <% if (flashSuccess != null) { %>
            <div class="alert alert-success"><%= escHtml(flashSuccess) %></div>
        <% } %>
        <% if (flashError != null) { %>
            <div class="alert alert-error"><%= escHtml(flashError) %></div>
        <% } %>

        <table class="user-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Licencia</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (pending != null && !pending.isEmpty()) {
                       for (User u : pending) {
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getName() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getLicenseNumber() != null ? u.getLicenseNumber() : "-" %></td>
                    <td><%= u.getPilotStatus() != null ? u.getPilotStatus() : "SIN SOLICITUD" %></td>
                    <td>
                        <form method="post" action="dashboard" style="display:inline" onsubmit="return confirmApprove();">
                            <input type="hidden" name="userId" value="<%= u.getId() %>">
                            <input type="hidden" name="view" value="pendingPilots">
                            <button type="submit" name="action" value="approvePilot">Aprobar</button>
                        </form>
                        <form method="post" action="dashboard" style="display:inline" onsubmit="return confirmReject();">
                            <input type="hidden" name="userId" value="<%= u.getId() %>">
                            <input type="hidden" name="view" value="pendingPilots">
                            <button type="submit" name="action" value="rejectPilot">Rechazar</button>
                        </form>
                    </td>
                </tr>
                <%    }
                   } else { %>
                <tr><td colspan="6">No hay pilotos pendientes</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <script>
        function confirmApprove() {
            return confirm('¿Confirmar aprobación del piloto?');
        }
        function confirmReject() {
            return confirm('¿Confirmar rechazo del piloto?');
        }
    </script>
</body>
</html>