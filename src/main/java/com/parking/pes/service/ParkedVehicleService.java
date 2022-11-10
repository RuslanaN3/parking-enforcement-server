package com.parking.pes.service;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;

public interface ParkedVehicleService {
    boolean existsParkedVehicle(String licensePlate);

    ParkedVehicle createFromEvent(Event event);

    ParkedVehicle find(String licencePlate);

    ParkedVehicle find(String licensePlate, Status status);

    void save(ParkedVehicle parkedVehicle);
}
