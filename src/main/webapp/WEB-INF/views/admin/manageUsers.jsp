<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="flysolo.entities.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestionar Usuarios - Panel Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="<%= themeClass %> admin-page">
    <div class="container">
        <h1>Gestionar Usuarios</h1>
        <nav class="admin-nav">
            <a href="dashboard?view=admin">Resumen</a> |
            <a href="dashboard?view=manageUsers">Gestionar Usuarios</a> |
            <a href="dashboard?view=pendingPilots">Solicitudes de Pilotos</a>
        </nav>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert alert-success"><c:out value="${sessionScope.flashSuccess}"/></div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
            <div class="alert alert-error"><c:out value="${sessionScope.flashError}"/></div>
            <c:remove var="flashError" scope="session" />
        </c:if>

        <table class="user-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Tipo</th>
                    <th>Facción</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% if (users != null) {
                       for (User u : users) {
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getName() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getUserType() %></td>
                    <td><%= u.getFaction() %></td>
                    <td><%= u.getPilotStatus() != null ? u.getPilotStatus() : "-" %></td>
                    <td>
                        <form method="post" action="dashboard" style="display:inline" onsubmit="return confirmDelete();">
                            <input type="hidden" name="userId" value="<%= u.getId() %>">
                            <input type="hidden" name="view" value="manageUsers">
                            <button type="submit" name="action" value="deleteUser">Eliminar</button>
                        </form>
                        <!-- Futuro: editar usuario -->
                    </td>
                </tr>
                <%    }
                   } else { %>
                <tr><td colspan="7">No se encontraron usuarios</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <script>
        function confirmDelete() {
            return confirm('¿Eliminar usuario? Esta acción no se puede deshacer.');
        }
    </script>
</body>
</html>