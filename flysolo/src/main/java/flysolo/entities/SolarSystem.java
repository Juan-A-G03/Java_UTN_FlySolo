package flysolo.entities;

import java.sql.Timestamp;

public class SolarSystem {
    
    private Integer id;
    private String name;
    private Integer systemXU12;  // Coordenada X en unidades de 10^12 km
    private Integer systemYU12;  // Coordenada Y en unidades de 10^12 km
    private Integer systemZU12;  // Coordenada Z en unidades de 10^12 km
    private Timestamp createdAt;

    // Constructors
    public SolarSystem() {
    }

    public SolarSystem(String name, Integer systemXU12, Integer systemYU12, Integer systemZU12) {
        this.name = name;
        this.systemXU12 = systemXU12;
        this.systemYU12 = systemYU12;
        this.systemZU12 = systemZU12;
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

    public Integer getSystemXU12() {
        return systemXU12;
    }

    public void setSystemXU12(Integer systemXU12) {
        this.systemXU12 = systemXU12;
    }

    public Integer getSystemYU12() {
        return systemYU12;
    }

    public void setSystemYU12(Integer systemYU12) {
        this.systemYU12 = systemYU12;
    }

    public Integer getSystemZU12() {
        return systemZU12;
    }

    public void setSystemZU12(Integer systemZU12) {
        this.systemZU12 = systemZU12;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SolarSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", systemXU12=" + systemXU12 +
                ", systemYU12=" + systemYU12 +
                ", systemZU12=" + systemZU12 +
                ", createdAt=" + createdAt +
                '}';
    }
}
