package com.parking.dto;

import java.time.Instant;

public class ParkedVehicleFilter {
    private String licensePlate;
    private Instant firstTimeSpotted;
    private Instant lastTimeSpotted;
    private Status status;
    private Integer routeCycle;

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

    public Integer getRouteCycle() {
        return routeCycle;
    }

    public void setRouteCycle(Integer routeCycle) {
        this.routeCycle = routeCycle;
    }
}
