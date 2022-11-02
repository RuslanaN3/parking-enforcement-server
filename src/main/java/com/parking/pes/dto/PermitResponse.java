package com.parking.pes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PermitResponse {
    private String licensePlate;
    private boolean hasPermit;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public boolean hasPermit() {
        return hasPermit;
    }

    public void setHasPermit(boolean hasPermit) {
        this.hasPermit = hasPermit;
    }
}
