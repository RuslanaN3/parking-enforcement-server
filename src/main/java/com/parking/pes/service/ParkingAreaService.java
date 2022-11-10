package com.parking.pes.service;

import com.parking.pes.model.ParkingArea;
import org.locationtech.jts.geom.Point;

public interface ParkingAreaService {

    ParkingArea findParkingAreaByLocation(Point point);
}
