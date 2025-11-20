<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="flysolo.entities.Trip" %>
<%@ page import="flysolo.entities.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
    Boolean isPilotView = (Boolean) request.getAttribute("isPilotView");
    String infoMessage = (String) request.getAttribute("infoMessage");
    String pilotViewMode = (String) request.getAttribute("pilotViewMode");
    if (pilotViewMode == null) pilotViewMode = "pending"; // valor por defecto
%>
<!DOCTYPE html>
<html>
<head>
    <title>Mis viajes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/themes.css">
</head>
<body class="<%= themeClass %>">
<div class="container">
    <h1>Mis viajes</h1>

    <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("success") %></div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <div class="trips-list">
        <% if (isPilotView != null && isPilotView) { %>

            <!-- Menú para pilotos: permite alternar entre aceptar nuevos viajes (pendientes)
                 o ver el historial completo de viajes que hizo el piloto.
                 Comentarios en español según la solicitud del usuario. -->
            <div class="pilot-menu">
                <a href="<%= request.getContextPath() %>/myTrips?view=pending" class="menu-item <%= "pending".equals(pilotViewMode) ? "active" : "" %>">Aceptar nuevos viajes</a>
                <a href="<%= request.getContextPath() %>/myTrips?view=history" class="menu-item <%= "history".equals(pilotViewMode) ? "active" : "" %>">Ver todos mis viajes</a>
            </div>

            <%
                ArrayList<Trip> trips = (ArrayList<Trip>) request.getAttribute("trips");
                if (trips == null || trips.isEmpty()) {
            %>
                <p>No hay viajes para mostrar en esta vista.</p>
            <%
                } else {
                    for (Trip t : trips) {
            %>
                <div class="trip-card">
                    <h3>Trip #<%= t.getId() %> - <%= t.getOriginPlanetName() %> → <%= t.getDestinationPlanetName() %></h3>
                    <p>Pasajero: <%= t.getPassengerName() %> (ID: <%= t.getPassengerUserId() %>)</p>
                    <p>Precio: ₡ <%= t.getPrice() %></p>
                    <p>Solicitado el: <%= t.getRequestedAt() %></p>

                    <div class="trip-actions">
                        <% if ("pending".equals(pilotViewMode) && t.isPending()) { %>
                            <form method="post" action="<%= request.getContextPath() %>/acceptTrip">
                                <input type="hidden" name="tripId" value="<%= t.getId() %>">
                                <button type="submit" class="accept">Aceptar viaje</button>
                            </form>
                        <% } else { %>
                            <p>Estado: <strong><%= t.getStatus() %></strong></p>
                        <% } %>
                    </div>
                </div>
            <%
                    }
                }
            %>
        <% } else { %>
            <!-- Passenger view: only show info message -->
            <p><%= infoMessage != null ? infoMessage : "Se ha registrado tu viaje." %></p>
        <% } %>
    </div>

    <p><a href="home">Volver</a></p>
</div>
</body>
</html>