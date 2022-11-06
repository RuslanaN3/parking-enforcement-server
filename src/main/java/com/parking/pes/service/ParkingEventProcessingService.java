package com.parking.pes.service;

import static com.parking.pes.model.Status.STARTED;
import static com.parking.pes.model.Status.UNPAID;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventProcessingService {

    private ParkedVehicleService parkedVehicleService;
    private ParkingVerificationService parkingVerificationService;
    private FineService fineService;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService,
                                         ParkingVerificationService parkingVerificationService,
                                         FineService fineService) {
        this.parkedVehicleService = parkedVehicleService;
        this.parkingVerificationService = parkingVerificationService;
        this.fineService = fineService;
    }

    public void processEvent(Event event) {
        ParkedVehicle parkedVehicle = parkedVehicleService.find(event.getLicensePlate());
        if (parkedVehicle == null) {
            parkedVehicleService.createFromEvent(event);
            return;
        }

        Status resultStatus = parkingVerificationService.verify(parkedVehicle, event);
        if (resultStatus == UNPAID && (parkedVehicle.getStatus().equals(STARTED) || isFined(parkedVehicle, event.getTimestamp()))) {
            fineService.createFine(parkedVehicle);
        }
        parkedVehicle.setStatus(resultStatus);
        parkedVehicle.setLastTimeSpotted(event.getTimestamp());
        parkedVehicleService.save(parkedVehicle);
    }

    private boolean isFined(ParkedVehicle parkedVehicle, Instant timestamp) {
        return parkedVehicle.getStatus().equals(UNPAID) && is24HoursBetweenDates(parkedVehicle.getLastTimeSpotted(), timestamp);
    }

    private boolean is24HoursBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(24)));
    }
}
