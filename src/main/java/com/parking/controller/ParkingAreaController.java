package com.parking.controller;

import com.parking.dto.ParkingAreaDto;
import com.parking.model.ParkingAreaType;
import com.parking.service.ParkingAreaService;
import java.util.List;
import javax.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(params = "type")
    public List<ParkingAreaDto> getParkingAreasByType(@RequestParam ParkingAreaType type) {
        return parkingAreaService.getParkingAreasByType(type);
    }
}
