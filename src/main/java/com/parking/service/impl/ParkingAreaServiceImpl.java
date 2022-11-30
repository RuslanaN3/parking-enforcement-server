package com.parking.service.impl;

import com.parking.dto.ParkingAreaDto;
import com.parking.model.ParkingArea;
import com.parking.repository.ParkingAreaRepository;
import com.parking.service.ParkingAreaService;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class ParkingAreaServiceImpl implements ParkingAreaService {

    private ParkingAreaRepository parkingAreaRepository;

    public ParkingAreaServiceImpl(ParkingAreaRepository parkingAreaRepository) {
        this.parkingAreaRepository = parkingAreaRepository;
    }

    @Override
    public ParkingArea getParkingAreaByLocation(Point point) {
        return parkingAreaRepository.findParkingAreaByPoint(point.getY(), point.getX());
    }

    @Override
    public List<ParkingAreaDto> getParkingAreas() {
        return parkingAreaRepository.findAll()
            .stream().map(this::convertToParkingAreaDto).collect(Collectors.toList());
    }

    private ParkingAreaDto convertToParkingAreaDto(ParkingArea parkingArea) {
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPolygon(parkingArea.getPolygon());
        parkingAreaDto.setAddress(parkingArea.getAddress());
        parkingAreaDto.setAreaType(parkingArea.getAreaType());
        parkingAreaDto.setParkingSide(parkingArea.getParkingSide());
        parkingAreaDto.setParkingType(parkingArea.getParkingType());
        parkingAreaDto.setParkingPlacesAmount(parkingArea.getParkingPlacesAmount());
        return parkingAreaDto;
    }
}
