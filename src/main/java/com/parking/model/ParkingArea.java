package com.parking.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.Map;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Polygon;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class ParkingArea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Polygon polygon;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private ParkingAreaType areaType;

    private String parkingSide;

    @Enumerated(EnumType.STRING)
    private ParkingType parkingType;

    private Integer parkingPlacesAmount;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> parameters;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
