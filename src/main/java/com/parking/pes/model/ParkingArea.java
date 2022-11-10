package com.parking.pes.model;

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

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private ParkingAreaType type;

    private String parkingSide;

    private ParkingType parkingType;

    private Integer parkingSpacesNumber;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ParkingAreaType getType() {
        return type;
    }

    public void setType(ParkingAreaType type) {
        this.type = type;
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

    public Integer getParkingSpacesNumber() {
        return parkingSpacesNumber;
    }

    public void setParkingSpacesNumber(Integer parkingSpacesNumber) {
        this.parkingSpacesNumber = parkingSpacesNumber;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
