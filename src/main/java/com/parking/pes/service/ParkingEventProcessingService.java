package com.parking.pes.service;

import static com.parking.pes.model.Status.*;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.ParkingPolygonRepository;
import java.time.Duration;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventProcessingService {
    private static final Logger logger = LogManager.getLogger(ParkingEventProcessingService.class);

    private ParkedVehicleService parkedVehicleService;
    private ParkingVerificationService parkingVerificationService;
    private FineService fineService;
    private Double minLicensePlateConfidence;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService,
                                         ParkingVerificationService parkingVerificationService,
                                         FineService fineService,
                                         @Value("${anpr.minConfidence}") Double minLicensePlateConfidence) {
        this.parkedVehicleService = parkedVehicleService;
        this.parkingVerificationService = parkingVerificationService;
        this.fineService = fineService;
        this.minLicensePlateConfidence = minLicensePlateConfidence;
    }

    public void processEvent(Event event) {
        if (event.getLicencePlateConfidence() < minLicensePlateConfidence) {
            logger.info("Low confidence value {} for license plate: {}", event.getLicencePlateConfidence(), event.getLicensePlate());
            return;
        }

        ParkedVehicle parkedVehicle = parkedVehicleService.find(event.getLicensePlate());
        if (parkedVehicle == null) {
            logger.info("Parked vehicle not found, creating new for {}", event.getLicensePlate());
            parkedVehicleService.createFromEvent(event);
            return;
        }


        Status verificationResultStatus = parkingVerificationService.verify(parkedVehicle, event);
        if (verificationResultStatus == UNPAID &&
            (parkedVehicle.getStatus() == STARTED || isFined(parkedVehicle, event.getTimestamp()))) {
            fineService.reportViolation(parkedVehicle);
        }
        parkedVehicle.setStatus(verificationResultStatus);

        parkedVehicle.setLastTimeSpotted(event.getTimestamp());
        parkedVehicleService.save(parkedVehicle);
    }

    private boolean isFined(ParkedVehicle parkedVehicle, Instant timestamp) {
        return parkedVehicle.getStatus() == UNPAID &&
            is24HoursBetweenDates(parkedVehicle.getLastTimeSpotted(), timestamp);
    }

    private boolean is24HoursBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(24)));
    }
}
