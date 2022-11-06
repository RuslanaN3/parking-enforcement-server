package com.parking.pes.service;

import static com.parking.pes.model.Status.*;

import com.parking.pes.dto.Location;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import java.time.Duration;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class ParkingVerificationService {
    private PermitService permitService;

    public ParkingVerificationService(PermitService permitService) {
        this.permitService = permitService;
    }

    // check for related location logic
    public Status verify(ParkedVehicle parkedVehicle, Event newEvent) {
        PermitResponse permitResponse = permitService.checkPermit(parkedVehicle.getLicensePlate(), new Location());
        if (permitResponse.hasPermit()) {
            return PAID;
        } else return UNPAID;
    }

}
