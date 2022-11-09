package com.parking.pes.repository;

import com.parking.pes.model.ParkingPolygon;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingPolygonRepository extends JpaRepository<ParkingPolygon, Integer> {

    @Query(value = "SELECT * FROM parking_polygon " +
                "WHERE ST_CONTAINS(mpolygon, Point(:lat, :lon)) = true || ST_TOUCHES(mpolygon, Point(:lat, :lon)) = true", nativeQuery = true)
    ParkingPolygon findParkingPolygonByPoint(Double lat, Double lon);

}
