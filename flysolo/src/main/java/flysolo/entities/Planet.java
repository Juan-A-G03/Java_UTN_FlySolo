package flysolo.entities;

import java.sql.Timestamp;

public class Planet {
    
    private Integer id;
    private String name;
    private Integer solarSystemId;
    private Integer planetXU6;  // Coordenada X offset en unidades de 10^6 km
    private Integer planetYU6;  // Coordenada Y offset en unidades de 10^6 km
    private Integer planetZU6;  // Coordenada Z offset en unidades de 10^6 km
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // For display purposes (populated via JOIN)
    private String solarSystemName;

    // Constructors
    public Planet() {
    }

    public Planet(String name, Integer solarSystemId, Integer planetXU6, Integer planetYU6, Integer planetZU6) {
        this.name = name;
        this.solarSystemId = solarSystemId;
        this.planetXU6 = planetXU6;
        this.planetYU6 = planetYU6;
        this.planetZU6 = planetZU6;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSolarSystemId() {
        return solarSystemId;
    }

    public void setSolarSystemId(Integer solarSystemId) {
        this.solarSystemId = solarSystemId;
    }

    public Integer getPlanetXU6() {
        return planetXU6;
    }

    public void setPlanetXU6(Integer planetXU6) {
        this.planetXU6 = planetXU6;
    }

    public Integer getPlanetYU6() {
        return planetYU6;
    }

    public void setPlanetYU6(Integer planetYU6) {
        this.planetYU6 = planetYU6;
    }

    public Integer getPlanetZU6() {
        return planetZU6;
    }

    public void setPlanetZU6(Integer planetZU6) {
        this.planetZU6 = planetZU6;
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

    public String getSolarSystemName() {
        return solarSystemName;
    }

    public void setSolarSystemName(String solarSystemName) {
        this.solarSystemName = solarSystemName;
    }

    // Helper method to get full display name
    public String getFullName() {
        if (solarSystemName != null) {
            return name + " (" + solarSystemName + ")";
        }
        return name;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", solarSystemId=" + solarSystemId +
                ", solarSystemName='" + solarSystemName + '\'' +
                ", planetXU6=" + planetXU6 +
                ", planetYU6=" + planetYU6 +
                ", planetZU6=" + planetZU6 +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
