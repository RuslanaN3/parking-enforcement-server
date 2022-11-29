package com.parking.service.impl;

import com.parking.model.ParkingArea;
import com.parking.repository.RouteCycleRepository;
import com.parking.repository.ParkedVehicleRepository;
import com.parking.repository.ParkingAreaRepository;
import com.parking.service.ParkingAreaService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class ParkingAreaServiceImpl implements ParkingAreaService {

    private ParkingAreaRepository parkingAreaRepository;
    private ParkedVehicleRepository parkedVehicleRepository;
    private RouteCycleRepository routeCycleRepository;

    public ParkingAreaServiceImpl(ParkingAreaRepository parkingAreaRepository) {
        this.parkingAreaRepository = parkingAreaRepository;
    }

    @Override
    public ParkingArea findParkingAreaByLocation(Point point) {
        return parkingAreaRepository.findParkingAreaByPoint(point.getY(), point.getX()).orElseThrow();
    }
}
