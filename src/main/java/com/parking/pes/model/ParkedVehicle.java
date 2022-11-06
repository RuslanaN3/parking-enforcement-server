package com.parking.pes.model;

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

    private String longitude;

    private String latitude;

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
