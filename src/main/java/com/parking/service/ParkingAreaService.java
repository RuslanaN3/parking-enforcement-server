package com.parking.service;

import com.parking.model.ParkingArea;
import org.locationtech.jts.geom.Point;

public interface ParkingAreaService {

    ParkingArea findParkingAreaByLocation(Point point);
}
