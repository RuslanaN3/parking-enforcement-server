package com.parking.pes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PermitResponse {
    private String licensePlate;
    @JsonProperty("hasPermit")
    private Boolean hasPermit;

    public PermitResponse() {
    }

    public PermitResponse(Boolean hasPermit) {
        this.hasPermit = hasPermit;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Boolean hasPermit() {
        return hasPermit;
    }

    public void setHasPermit(Boolean hasPermit) {
        this.hasPermit = hasPermit;
    }
}
