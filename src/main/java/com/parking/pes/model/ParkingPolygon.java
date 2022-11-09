package com.parking.pes.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.locationtech.jts.geom.Polygon;


@Entity
public class ParkingPolygon {
    @Id
    private Integer id;
    private Polygon mpolygon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Polygon getMpolygon() {
        return mpolygon;
    }

    public void setMpolygon(Polygon mpolygon) {
        this.mpolygon = mpolygon;
    }
}
