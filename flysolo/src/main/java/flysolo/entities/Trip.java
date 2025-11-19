package flysolo.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Trip {
    
    private Long id;
    private Long passengerUserId;
    private Long pilotUserId;
    private Integer originPlanetId;
    private Integer destinationPlanetId;
    private String tripMode;           // NORMAL | UNDERCOVER
    private String type;               // INMEDIATO | PROGRAMADO
    private String status;             // PENDIENTE | CONFIRMADO | EN_CURSO | COMPLETADO | CANCELADO
    private BigDecimal price;
    private Timestamp requestedAt;
    private Timestamp startedAt;
    private Timestamp completedAt;
    private Timestamp canceledAt;
    private String cancelReason;
    private String notes;
    private Timestamp updatedAt;
    
    // For display purposes (populated via JOIN)
    private String passengerName;
    private String pilotName;
    private String originPlanetName;
    private String destinationPlanetName;
    private String originSolarSystemName;
    private String destinationSolarSystemName;

    // Constructors
    public Trip() {
    }

    public Trip(Long passengerUserId, Integer originPlanetId, Integer destinationPlanetId, 
                String tripMode, String type, BigDecimal price) {
        this.passengerUserId = passengerUserId;
        this.originPlanetId = originPlanetId;
        this.destinationPlanetId = destinationPlanetId;
        this.tripMode = tripMode;
        this.type = type;
        this.price = price;
        this.status = "PENDIENTE";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassengerUserId() {
        return passengerUserId;
    }

    public void setPassengerUserId(Long passengerUserId) {
        this.passengerUserId = passengerUserId;
    }

    public Long getPilotUserId() {
        return pilotUserId;
    }

    public void setPilotUserId(Long pilotUserId) {
        this.pilotUserId = pilotUserId;
    }

    public Integer getOriginPlanetId() {
        return originPlanetId;
    }

    public void setOriginPlanetId(Integer originPlanetId) {
        this.originPlanetId = originPlanetId;
    }

    public Integer getDestinationPlanetId() {
        return destinationPlanetId;
    }

    public void setDestinationPlanetId(Integer destinationPlanetId) {
        this.destinationPlanetId = destinationPlanetId;
    }

    public String getTripMode() {
        return tripMode;
    }

    public void setTripMode(String tripMode) {
        this.tripMode = tripMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Timestamp requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public Timestamp getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Timestamp canceledAt) {
        this.canceledAt = canceledAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getOriginPlanetName() {
        return originPlanetName;
    }

    public void setOriginPlanetName(String originPlanetName) {
        this.originPlanetName = originPlanetName;
    }

    public String getDestinationPlanetName() {
        return destinationPlanetName;
    }

    public void setDestinationPlanetName(String destinationPlanetName) {
        this.destinationPlanetName = destinationPlanetName;
    }

    public String getOriginSolarSystemName() {
        return originSolarSystemName;
    }

    public void setOriginSolarSystemName(String originSolarSystemName) {
        this.originSolarSystemName = originSolarSystemName;
    }

    public String getDestinationSolarSystemName() {
        return destinationSolarSystemName;
    }

    public void setDestinationSolarSystemName(String destinationSolarSystemName) {
        this.destinationSolarSystemName = destinationSolarSystemName;
    }

    // Helper methods
    public boolean isPending() {
        return "PENDIENTE".equals(this.status);
    }

    public boolean isConfirmed() {
        return "CONFIRMADO".equals(this.status);
    }

    public boolean isInProgress() {
        return "EN_CURSO".equals(this.status);
    }

    public boolean isCompleted() {
        return "COMPLETADO".equals(this.status);
    }

    public boolean isCanceled() {
        return "CANCELADO".equals(this.status);
    }

    public boolean isUndercover() {
        return "UNDERCOVER".equals(this.tripMode);
    }

    public boolean isScheduled() {
        return "PROGRAMADO".equals(this.type);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", passengerUserId=" + passengerUserId +
                ", pilotUserId=" + pilotUserId +
                ", originPlanetId=" + originPlanetId +
                ", destinationPlanetId=" + destinationPlanetId +
                ", tripMode='" + tripMode + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", requestedAt=" + requestedAt +
                '}';
    }
}
