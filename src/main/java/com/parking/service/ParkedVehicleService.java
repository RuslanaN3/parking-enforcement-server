package com.parking.service;

import com.parking.model.ParkedVehicle;
import com.parking.model.Status;

public interface ParkedVehicleService {
    boolean existsParkedVehicle(String licensePlate);

    ParkedVehicle find(String licencePlate);

    ParkedVehicle find(String licensePlate, Status status);

    void save(ParkedVehicle parkedVehicle);

    void delete(Integer parkedVehicleId);
}
