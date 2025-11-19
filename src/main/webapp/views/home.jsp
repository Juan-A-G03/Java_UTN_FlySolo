<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="flysolo.entities.Planet" %>
<%@ page import="flysolo.entities.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.math.BigDecimal" %>
<%
    // Obtener el usuario de la sesión para determinar el tema
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
%>
<!DOCTYPE html>
<html>
<head>
    <title>FlySolo - Solicitar viaje</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/themes.css">
</head>
<body class="<%= themeClass %>">
    <!-- Barra superior con accesos rápidos -->
    <header class="top-nav">
        <div class="nav-left">
            <a href="home">Inicio</a>
        </div>
        <div class="nav-right">
            <%
                if (currentUser != null) {
                    if (currentUser.isAdmin()) {
            %>
                        <a href="dashboard?view=admin">Panel de Administración</a>
            <%
                    } else if (currentUser.isPilot()) {
            %>
                        <a href="dashboard?view=pilot">Panel de Piloto</a>
            <%
                    }
                }
            %>
            <a href="logout">Cerrar sesión</a>
        </div>
    </header>

    <div class="container">
        <%
            String factionIcon = "🚀";
            String factionTitle = "Solicitar un viaje";
            if ("REBEL".equals(faction)) {
                factionIcon = "✊";
                factionTitle = "Alianza Rebelde - Solicitar misión";
            } else if ("IMPERIAL".equals(faction)) {
                factionIcon = "⚔️";
                factionTitle = "Flota Imperial - Solicitar transporte";
            } else {
                factionIcon = "🌟";
                factionTitle = "Comerciante independiente - Solicitar viaje";
            }
        %>
        <div class="faction-badge"><%= faction %> - Facción</div>
        <h1><%= factionIcon %> <%= factionTitle %></h1>
        <p class="subtitle">Selecciona tu origen y destino para viajar por la galaxia</p>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">
                <%= request.getAttribute("success") %>
            </div>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form method="post" action="home" id="tripForm">
            <div class="form-group">
                <label for="originPlanetId">Planeta de origen</label>
                <select name="originPlanetId" id="originPlanetId" required>
                    <option value="">-- Selecciona planeta de origen --</option>
                    <% 
                        ArrayList<Planet> planets = (ArrayList<Planet>) request.getAttribute("planets");
                        Integer selectedOrigin = (Integer) request.getAttribute("originPlanetId");
                        if (planets != null) {
                            for (Planet planet : planets) {
                                String selected = (selectedOrigin != null && selectedOrigin.equals(planet.getId())) ? "selected" : "";
                    %>
                                <option value="<%= planet.getId() %>" <%= selected %>>
                                    <%= planet.getName() %> (<%= planet.getSolarSystemName() %>)
                                </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="destinationPlanetId">Planeta destino</label>
                <select name="destinationPlanetId" id="destinationPlanetId" required>
                    <option value="">-- Selecciona planeta destino --</option>
                    <% 
                        Integer selectedDest = (Integer) request.getAttribute("destinationPlanetId");
                        if (planets != null) {
                            for (Planet planet : planets) {
                                String selected = (selectedDest != null && selectedDest.equals(planet.getId())) ? "selected" : "";
                    %>
                                <option value="<%= planet.getId() %>" <%= selected %>>
                                    <%= planet.getName() %> (<%= planet.getSolarSystemName() %>)
                                </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <div class="switch-group">
                <div class="switch-container">
                    <span class="switch-label">Modo de viaje</span>
                    <label class="switch">
                        <input type="checkbox" name="tripMode" id="tripModeSwitch" value="UNDERCOVER">
                        <span class="slider">
                            <span>Normal</span>
                            <span>Encubierto</span>
                        </span>
                    </label>
                    <input type="hidden" name="tripMode" id="tripModeValue" value="NORMAL">
                </div>

                <div class="switch-container">
                    <span class="switch-label">Tipo de viaje</span>
                    <label class="switch">
                        <input type="checkbox" name="tripType" id="tripTypeSwitch" value="PROGRAMADO">
                        <span class="slider">
                            <span>Inmediato</span>
                            <span>Programado</span>
                        </span>
                    </label>
                    <input type="hidden" name="tripType" id="tripTypeValue" value="INMEDIATO">
                </div>
            </div>

            <div class="scheduled-date" id="scheduledDateGroup">
                <div class="form-group">
                    <label for="scheduledDate">Fecha y hora programada</label>
                    <input type="datetime-local" name="scheduledDate" id="scheduledDate">
                </div>
            </div>

            <div class="form-group">
                <label for="notes">Notas (opcional)</label>
                <textarea name="notes" id="notes" rows="3" placeholder="Alguna petición especial..."><%= request.getAttribute("notes") != null ? request.getAttribute("notes") : "" %></textarea>
            </div>

            <% if (request.getAttribute("calculatedPrice") != null) { %>
                <div class="price-display">
                    <h2>Coste estimado del viaje</h2>
                    <div class="price">₡ <%= request.getAttribute("calculatedPrice") %></div>
                    <div class="distance">
                        Distancia: <%= String.format("%.2f", request.getAttribute("distance")) %> km
                    </div>
                </div>
            <% } %>

            <div class="button-group">
                <button type="submit" name="action" value="calculatePrice" class="btn-calculate">
                    💰 Calcular precio
                </button>
                <button type="submit" name="action" value="requestTrip" class="btn-request" 
                        <%= request.getAttribute("calculatedPrice") == null ? "disabled" : "" %>>
                    🚀 Solicitar viaje
                </button>
            </div>
        </form>
    </div>

    <script>
        // Control del switch de modo de viaje
        const tripModeSwitch = document.getElementById('tripModeSwitch');
        const tripModeValue = document.getElementById('tripModeValue');
        
        tripModeSwitch.addEventListener('change', function() {
            tripModeValue.value = this.checked ? 'UNDERCOVER' : 'NORMAL';
        });

        // Switch de tipo de viaje con toggle de fecha programada
        const tripTypeSwitch = document.getElementById('tripTypeSwitch');
        const tripTypeValue = document.getElementById('tripTypeValue');
        const scheduledDateGroup = document.getElementById('scheduledDateGroup');
        const scheduledDateInput = document.getElementById('scheduledDate');
        
        tripTypeSwitch.addEventListener('change', function() {
            if (this.checked) {
                tripTypeValue.value = 'PROGRAMADO';
                scheduledDateGroup.style.display = 'block';
                scheduledDateInput.required = true;
            } else {
                tripTypeValue.value = 'INMEDIATO';
                scheduledDateGroup.style.display = 'none';
                scheduledDateInput.required = false;
            }
        });

        // Establecer fecha mínima para la programación
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        scheduledDateInput.min = now.toISOString().slice(0, 16);
    </script>
</body>
</html>