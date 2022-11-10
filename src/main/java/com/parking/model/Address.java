package com.parking.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String district;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
