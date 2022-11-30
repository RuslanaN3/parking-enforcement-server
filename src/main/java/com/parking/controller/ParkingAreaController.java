package com.parking.controller;

import com.parking.dto.ParkingAreaDto;
import com.parking.service.ParkingAreaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/parking-area")
public class ParkingAreaController {

    private ParkingAreaService parkingAreaService;

    public ParkingAreaController(ParkingAreaService parkingAreaService) {
        this.parkingAreaService = parkingAreaService;
    }

    @GetMapping
    public List<ParkingAreaDto> getParkingAreas() {
        return parkingAreaService.getParkingAreas();
    }

}
