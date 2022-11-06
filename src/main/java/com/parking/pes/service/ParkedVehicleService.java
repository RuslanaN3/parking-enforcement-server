package com.parking.pes.service;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;

public interface ParkedVehicleService {
    boolean existsParkedVehicle(String licensePlate);

    ParkedVehicle createFromEvent(Event event, Double minLicensePlateConfidence);

    ParkedVehicle find(String licencePlate);

    void save(ParkedVehicle parkedVehicle);
}
