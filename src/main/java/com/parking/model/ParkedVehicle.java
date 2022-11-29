package com.parking.model;

import com.parking.dto.Status;
import java.time.Instant;
import javax.persistence.*;

@Entity
public class ParkedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;

    private Instant firstTimeSpotted;

    private Instant lastTimeSpotted;

    @Enumerated(EnumType.STRING)
    private Status status = Status.STARTED;

    private Double longitude;

    private Double latitude;

    @ManyToOne
    private ParkingArea parkingArea;

    private Boolean resolved = false;

    @ManyToOne
    private RouteCycle routeCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ParkingArea getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(ParkingArea parkingArea) {
        this.parkingArea = parkingArea;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public RouteCycle getRouteCycle() {
        return routeCycle;
    }

    public void setRouteCycle(RouteCycle routeCycle) {
        this.routeCycle = routeCycle;
    }
}
