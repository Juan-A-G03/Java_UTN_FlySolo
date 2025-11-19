<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="flysolo.entities.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Panel - FlySolo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="<%= themeClass %>">
    <!-- Minimal dashboard landing: redirect users to the appropriate dashboard view -->
    <div class="container">
        <h1>Panel</h1>
        <p>Usa la navegación para acceder al panel apropiado.</p>
        <ul>
            <% if (currentUser != null && currentUser.isAdmin()) { %>
                <li><a href="dashboard?view=admin">Ir al Panel de Administración</a></li>
            <% } %>
            <% if (currentUser != null && currentUser.isPilot()) { %>
                <li><a href="dashboard?view=pilot">Ir al Panel de Piloto</a></li>
            <% } %>
            <li><a href="home">Volver al Inicio</a></li>
        </ul>
    </div>
</body>
</html>