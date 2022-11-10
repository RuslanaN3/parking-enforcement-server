package com.parking.dto;

import com.parking.model.Address;

public class PermitRequest {
    private String licensePlate;
    private Address parkingAreaAddress;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Address getParkingAreaAddress() {
        return parkingAreaAddress;
    }

    public void setParkingAreaAddress(Address parkingAreaAddress) {
        this.parkingAreaAddress = parkingAreaAddress;
    }
}
