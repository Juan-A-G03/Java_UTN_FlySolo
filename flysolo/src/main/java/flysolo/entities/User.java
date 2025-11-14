package flysolo.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class User {
    
    private Long id;
    private String name;
    private String password;
    private String email;
    private String faction;           // IMPERIAL | REBEL | NEUTRAL
    private String userType;          // PASSENGER | PILOT | ADMIN
    private String licenseNumber;
    private String pilotStatus;       // NULL | PENDIENTE | APROBADO | RECHAZADO
    private BigDecimal ratingAvg;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public User() {
    }

    public User(String name, String email, String faction, String userType) {
        this.name = name;
        this.email = email;
        this.faction = faction;
        this.userType = userType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPilotStatus() {
        return pilotStatus;
    }

    public void setPilotStatus(String pilotStatus) {
        this.pilotStatus = pilotStatus;
    }

    public BigDecimal getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(BigDecimal ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isPilot() {
        return "PILOT".equals(this.userType);
    }

    public boolean isPassenger() {
        return "PASSENGER".equals(this.userType);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.userType);
    }

    public boolean isApprovedPilot() {
        return isPilot() && "APROBADO".equals(this.pilotStatus);
    }

    public boolean isPendingPilot() {
        return "PENDIENTE".equals(this.pilotStatus);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", faction='" + faction + '\'' +
                ", userType='" + userType + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", pilotStatus='" + pilotStatus + '\'' +
                ", ratingAvg=" + ratingAvg +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
