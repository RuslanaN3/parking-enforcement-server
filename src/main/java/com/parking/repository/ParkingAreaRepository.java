package com.parking.repository;

import com.parking.model.ParkingArea;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Integer> {

    @Query(value = "SELECT * FROM parking_area " +
                "WHERE ST_CONTAINS(polygon, Point(:lon, :lat)) = true || ST_TOUCHES(polygon, Point(:lon, :lat)) = true", nativeQuery = true)
    ParkingArea findParkingAreaByPoint(Double lat, Double lon);

}
