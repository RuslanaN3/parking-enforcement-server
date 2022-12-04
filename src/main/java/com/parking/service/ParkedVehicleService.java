package com.parking.service;

import com.parking.dto.ParkedVehicleDto;
import com.parking.dto.ParkedVehicleFilter;
import com.parking.model.Event;
import com.parking.model.ParkedVehicle;
import com.parking.dto.Status;
import java.util.List;

public interface ParkedVehicleService {
    List<ParkedVehicleDto> getParkedVehicles();

    ParkedVehicleDto getParkedVehicle(Long id);

    List<ParkedVehicleDto> searchParkedVehicles(ParkedVehicleFilter parkedVehicleFilter);

    boolean existsParkedVehicle(String licensePlate);


    ParkedVehicle find(String licensePlate, Status status);

    void save(ParkedVehicle parkedVehicle);

    void processParkedVehicle(Event event);
}
