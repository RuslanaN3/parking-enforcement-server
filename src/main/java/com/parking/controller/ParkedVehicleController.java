package com.parking.controller;

import com.parking.dto.ParkedVehicleDto;
import com.parking.dto.ParkedVehicleFilter;
import com.parking.service.ParkedVehicleService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parked-vehicle")
public class ParkedVehicleController {

    private ParkedVehicleService parkedVehicleService;

    public ParkedVehicleController(ParkedVehicleService parkedVehicleService) {
        this.parkedVehicleService = parkedVehicleService;
    }

    @GetMapping
    public List<ParkedVehicleDto> getParkedVehicles() {
        return parkedVehicleService.getParkedVehicles();

    }

    @GetMapping("/{id}")
    public ParkedVehicleDto getParkedVehicle(@PathVariable Long id) {
        return parkedVehicleService.getParkedVehicle(id);
    }

    @PostMapping("/search")
    public List<ParkedVehicleDto> search(@RequestBody ParkedVehicleFilter parkedVehicleFilter){
        return parkedVehicleService.searchParkedVehicles(parkedVehicleFilter);
    }







}
