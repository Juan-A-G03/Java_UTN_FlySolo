<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="flysolo.entities.Planet" %>
<%@ page import="flysolo.entities.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.math.BigDecimal" %>
<%
    // Get user from session to determine theme
    User currentUser = (User) session.getAttribute("user");
    String faction = currentUser != null ? currentUser.getFaction() : "NEUTRAL";
    String themeClass = "theme-" + faction.toLowerCase();
%>
<!DOCTYPE html>
<html>
<head>
    <title>FlySolo - Request Trip</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homeUser.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/themes.css">
</head>
<body class="<%= themeClass %>">
    <div class="container">
        <%
            String factionIcon = "🚀";
            String factionTitle = "Request a Trip";
            if ("REBEL".equals(faction)) {
                factionIcon = "✊";
                factionTitle = "Rebel Alliance - Request Mission";
            } else if ("IMPERIAL".equals(faction)) {
                factionIcon = "⚔️";
                factionTitle = "Imperial Fleet - Request Transport";
            } else {
                factionIcon = "🌟";
                factionTitle = "Independent Trader - Request Trip";
            }
        %>
        <div class="faction-badge"><%= faction %> FACTION</div>
        <h1><%= factionIcon %> <%= factionTitle %></h1>
        <p class="subtitle">Select your origin and destination to travel across the galaxy</p>

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
                <label for="originPlanetId">Origin Planet</label>
                <select name="originPlanetId" id="originPlanetId" required>
                    <option value="">-- Select Origin Planet --</option>
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
                <label for="destinationPlanetId">Destination Planet</label>
                <select name="destinationPlanetId" id="destinationPlanetId" required>
                    <option value="">-- Select Destination Planet --</option>
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
                    <span class="switch-label">Trip Mode</span>
                    <label class="switch">
                        <input type="checkbox" name="tripMode" id="tripModeSwitch" value="UNDERCOVER">
                        <span class="slider">
                            <span>Normal</span>
                            <span>Undercover</span>
                        </span>
                    </label>
                    <input type="hidden" name="tripMode" id="tripModeValue" value="NORMAL">
                </div>

                <div class="switch-container">
                    <span class="switch-label">Trip Type</span>
                    <label class="switch">
                        <input type="checkbox" name="tripType" id="tripTypeSwitch" value="PROGRAMADO">
                        <span class="slider">
                            <span>Immediate</span>
                            <span>Scheduled</span>
                        </span>
                    </label>
                    <input type="hidden" name="tripType" id="tripTypeValue" value="INMEDIATO">
                </div>
            </div>

            <div class="scheduled-date" id="scheduledDateGroup">
                <div class="form-group">
                    <label for="scheduledDate">Scheduled Date and Time</label>
                    <input type="datetime-local" name="scheduledDate" id="scheduledDate">
                </div>
            </div>

            <div class="form-group">
                <label for="notes">Notes (Optional)</label>
                <textarea name="notes" id="notes" rows="3" placeholder="Any special requests or instructions..."><%= request.getAttribute("notes") != null ? request.getAttribute("notes") : "" %></textarea>
            </div>

            <% if (request.getAttribute("calculatedPrice") != null) { %>
                <div class="price-display">
                    <h2>Estimated Trip Cost</h2>
                    <div class="price">₡ <%= request.getAttribute("calculatedPrice") %></div>
                    <div class="distance">
                        Distance: <%= String.format("%.2f", request.getAttribute("distance")) %> km
                    </div>
                </div>
            <% } %>

            <div class="button-group">
                <button type="submit" name="action" value="calculatePrice" class="btn-calculate">
                    💰 Calculate Price
                </button>
                <button type="submit" name="action" value="requestTrip" class="btn-request" 
                        <%= request.getAttribute("calculatedPrice") == null ? "disabled" : "" %>>
                    🚀 Request Trip
                </button>
            </div>
        </form>
    </div>

    <script>
        // Trip Mode Switch
        const tripModeSwitch = document.getElementById('tripModeSwitch');
        const tripModeValue = document.getElementById('tripModeValue');
        
        tripModeSwitch.addEventListener('change', function() {
            tripModeValue.value = this.checked ? 'UNDERCOVER' : 'NORMAL';
        });

        // Trip Type Switch with scheduled date toggle
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

        // Set current datetime as minimum for scheduled trips
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        scheduledDateInput.min = now.toISOString().slice(0, 16);
    </script>
</body>
</html>
