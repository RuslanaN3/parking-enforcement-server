package com.parking.dto;


import java.time.Instant;

public class ParkedVehicleDto {
    private String licensePlate;
    private Instant firstTimeSpotted;
    private Instant lastTimeSpotted;
    private Status status;
    private Double longitude;
    private Double latitude;

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
}
