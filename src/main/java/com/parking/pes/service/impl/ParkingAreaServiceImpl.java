package com.parking.pes.service.impl;

import com.parking.pes.model.ParkingArea;
import com.parking.pes.repository.ParkingAreaRepository;
import com.parking.pes.service.ParkingAreaService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class ParkingAreaServiceImpl implements ParkingAreaService {

    private ParkingAreaRepository parkingAreaRepository;

    public ParkingAreaServiceImpl(ParkingAreaRepository parkingAreaRepository) {
        this.parkingAreaRepository = parkingAreaRepository;
    }

    @Override
    public ParkingArea findParkingAreaByLocation(Point point) {
        return parkingAreaRepository.findParkingAreaByPoint(point.getY(), point.getX());
    }
}
