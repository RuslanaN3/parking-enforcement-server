package com.parking.model;

import java.time.Instant;
import javax.persistence.*;

@Entity
public class ParkedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String licensePlate;

    private Instant firstTimeSpotted;

    private Instant lastTimeSpotted;

    @Enumerated(EnumType.STRING)
    private Status status = Status.STARTED;

    private Double longitude;

    private Double latitude;

    private Integer parkingAreaId;

    private Boolean resolved = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Instant getFirstTimeSpotted() {
        return firstTimeSpotted;
    }

    public void setFirstTimeSpotted(Instant firstTimeSpotted) {
        this.firstTimeSpotted = firstTimeSpotted;
    }

    public Instant getLastTimeSpotted() {
        return lastTimeSpotted;
    }

    public void setLastTimeSpotted(Instant lastTimeSpotted) {
        this.lastTimeSpotted = lastTimeSpotted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getParkingAreaId() {
        return parkingAreaId;
    }

    public void setParkingAreaId(Integer parkingAreaId) {
        this.parkingAreaId = parkingAreaId;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }
}
