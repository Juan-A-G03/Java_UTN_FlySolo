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
    <title>Admin Home - FlySolo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="<%= themeClass %> admin-page">
    <div class="container">
        <!-- Hero / header -->
        <div class="admin-hero">
            <h1>Panel de Administración</h1>
            <div class="subtitle">Bienvenido, <strong><%= currentUser != null ? currentUser.getName() : "Admin" %></strong></div>
        </div>

        <!-- Navigation -->
        <nav class="admin-nav">
            <a href="dashboard?view=admin">Resumen</a> |
            <a href="dashboard?view=manageUsers">Gestionar Usuarios</a> |
            <a href="dashboard?view=pendingPilots">Solicitudes de Pilotos</a> |
            <a href="home">Inicio</a>
        </nav>

        <!-- Action cards -->
        <section class="admin-cards">
            <div class="card">
                <h3>Visualiza y gestiona usuarios</h3>
                <p class="subtitle">Accede a la lista de usuarios para editar o eliminar cuentas.</p>
                <a class="btn" href="dashboard?view=manageUsers">Gestionar Usuarios</a>
            </div>

            <div class="card">
                <h3>Revisar y aprobar solicitudes de pilotos</h3>
                <p class="subtitle">Revisa las solicitudes pendientes para convertir pasajeros en pilotos.</p>
                <a class="btn" href="dashboard?view=pendingPilots">Revisar Solicitudes</a>
            </div>
        </section>
    </div>
</body>
</html>