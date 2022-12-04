package com.parking.service;

import com.parking.dto.ParkingAreaDto;
import com.parking.model.ParkingArea;
import com.parking.model.ParkingAreaType;
import java.util.List;
import org.locationtech.jts.geom.Point;

public interface ParkingAreaService {

    ParkingArea getParkingAreaByLocation(Point point);

    List<ParkingAreaDto> getParkingAreas();

    List<ParkingAreaDto> getParkingAreasByType(ParkingAreaType parkingAreaType);
}
