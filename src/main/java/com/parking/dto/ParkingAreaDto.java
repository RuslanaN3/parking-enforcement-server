package com.parking.dto;

import com.parking.model.Address;
import com.parking.model.ParkingAreaType;
import com.parking.model.ParkingType;
import org.locationtech.jts.geom.Polygon;

public class ParkingAreaDto {
    private Polygon polygon;
    private Address address;
    private ParkingAreaType areaType;
    private String parkingSide;
    private ParkingType parkingType;
    private Integer parkingPlacesAmount;

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ParkingAreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(ParkingAreaType areaType) {
        this.areaType = areaType;
    }

    public String getParkingSide() {
        return parkingSide;
    }

    public void setParkingSide(String parkingSide) {
        this.parkingSide = parkingSide;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    public Integer getParkingPlacesAmount() {
        return parkingPlacesAmount;
    }

    public void setParkingPlacesAmount(Integer parkingPlacesAmount) {
        this.parkingPlacesAmount = parkingPlacesAmount;
    }
}