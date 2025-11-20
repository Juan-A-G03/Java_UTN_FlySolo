<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="flysolo.entities.Trip" %>
<%@ page import="flysolo.entities.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    Trip trip = (Trip) request.getAttribute("trip");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Gestionar viaje</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
</head>
<body>
<div class="container">
    <h1>Gestionar viaje #<%= trip != null ? trip.getId() : "-" %></h1>

    <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("success") %></div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (trip == null) { %>
        <p>Viaje no encontrado.</p>
    <% } else { %>
        <div class="trip-card">
            <h3><%= trip.getOriginPlanetName() %> → <%= trip.getDestinationPlanetName() %></h3>
            <p>Pasajero: <%= trip.getPassengerName() %> (ID: <%= trip.getPassengerUserId() %>)</p>
            <p>Precio: ₡ <%= trip.getPrice() %></p>
            <p>Estado: <strong><%= trip.getStatus() %></strong></p>
            <p>Solicitado: <%= trip.getRequestedAt() %></p>
            <% if (trip.getStartedAt() != null) { %>
                <p>Iniciado: <%= trip.getStartedAt() %></p>
            <% } %>
            <% if (trip.getCompletedAt() != null) { %>
                <p>Finalizado: <%= trip.getCompletedAt() %></p>
            <% } %>
            <% if (trip.getCanceledAt() != null) { %>
                <p>Cancelado: <%= trip.getCanceledAt() %></p>
                <p>Motivo: <%= trip.getCancelReason() %></p>
            <% } %>
            <% if (trip.getNotes() != null) { %>
                <p>Notas: <%= trip.getNotes() %></p>
            <% } %>
        </div>

        <!-- Agrupamos las acciones en dos bloques: a la izquierda las acciones principales (Iniciar/Finalizar)
             y a la derecha la acción de Cancelar con su campo de motivo. Esto facilita el layout y evita
             que el botón de cancelar quede junto a las acciones principales accidentalmente. -->
        <div class="trip-actions">
            <div class="left-actions">
                <% if ("CONFIRMADO".equals(trip.getStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/manageTripAction">
                        <input type="hidden" name="tripId" value="<%= trip.getId() %>">
                        <button type="submit" name="action" value="start" class="btn-start">Iniciar viaje</button>
                    </form>
                <% } %>

                <% if ("EN_CURSO".equals(trip.getStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/manageTripAction">
                        <input type="hidden" name="tripId" value="<%= trip.getId() %>">
                        <button type="submit" name="action" value="complete" class="btn-complete">Finalizar viaje</button>
                    </form>
                <% } %>
            </div>

            <div class="right-actions">
                <% if (!"COMPLETADO".equals(trip.getStatus()) && !"CANCELADO".equals(trip.getStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/manageTripAction" class="cancel-form">
                        <input type="hidden" name="tripId" value="<%= trip.getId() %>">
                        <label for="cancelReason" class="visually-hidden">Motivo de cancelación</label>
                        <input type="text" id="cancelReason" name="cancelReason" required maxlength="255" class="cancel-input" placeholder="Motivo de cancelación">
                        <button type="submit" name="action" value="cancel" class="btn-cancel">Cancelar viaje</button>
                    </form>
                <% } %>
            </div>
        </div>
    <% } %>

    <p><a href="<%= request.getContextPath() %>/myTrips">Volver</a></p>
</div>
</body>
</html>