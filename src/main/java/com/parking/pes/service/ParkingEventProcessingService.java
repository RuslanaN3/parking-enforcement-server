package com.parking.pes.service;

import static com.parking.pes.model.Status.*;
import static com.parking.pes.model.Status.UNPAID;

import com.parking.pes.dto.Location;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventProcessingService {

    private ParkedVehicleService parkedVehicleService;
    private ParkingVerificationService parkingVerificationService;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService) {
        this.parkedVehicleService = parkedVehicleService;
    }

    public void processEvent(Event event) {
        if (!parkedVehicleService.existsParkedVehicle(event.getLicensePlate())) {
            parkedVehicleService.createFromEvent(event);
            return;
        }

        ParkedVehicle parkedVehicle = parkedVehicleService.find(event.getLicensePlate());
        Status resultStatus = parkingVerificationService.verify(parkedVehicle, event);
        parkedVehicle.setStatus(resultStatus);
        parkedVehicle.setLastTimeSpotted(event.getTimestamp());
        parkedVehicleService.save(parkedVehicle);


    }
}
