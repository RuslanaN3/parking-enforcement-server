package com.parking.service;

import static com.parking.model.Status.*;

import com.parking.model.*;
import com.parking.repository.RouteCycleRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventProcessingService {
    private static final Logger logger = LogManager.getLogger(ParkingEventProcessingService.class);

    private ParkedVehicleService parkedVehicleService;
    private PermitVerificationService permitVerificationService;
    private FineService fineService;
    private Double minLicensePlateConfidence;
    private ParkingAreaService parkingAreaService;
    private RouteCycleService routeCycleService;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService,
                                         PermitVerificationService permitVerificationService,
                                         FineService fineService,
                                         @Value("${anpr.minConfidence}") Double minLicensePlateConfidence,
                                         ParkingAreaService parkingAreaService,
                                         RouteCycleService routeCycleService) {
        this.parkedVehicleService = parkedVehicleService;
        this.permitVerificationService = permitVerificationService;
        this.fineService = fineService;
        this.minLicensePlateConfidence = minLicensePlateConfidence;
        this.parkingAreaService = parkingAreaService;
        this.routeCycleService = routeCycleService;
    }

    public void processEvent(Event event) {
        routeCycleService.createCycleIfNotPresent(event.getCycle());
        if (event.getLicencePlateConfidence() < minLicensePlateConfidence) {
            logger.info("Low confidence value {} for license plate: {} at ", event.getLicencePlateConfidence(),
                event.getLicensePlate());
            return;
        }

        ParkedVehicle parkedVehicle = parkedVehicleService.find(event.getLicensePlate());
        if (parkedVehicle == null) {
            logger.info("Parked vehicle not found, creating new for {}", event.getLicensePlate());
            parkedVehicleService.save(createParkedVehicle(event));
            return;
        }
        processParkedVehicleEvent(parkedVehicle, event);
    }

    private void processParkedVehicleEvent(ParkedVehicle parkedVehicle, Event event) {
        if ((parkedVehicle.getStatus() == STARTED &&
            isBetweenDates(parkedVehicle.getFirstTimeSpotted(), event.getTimestamp(), Duration.ofMinutes(15)))
            || parkedVehicle.getStatus() == PAID) {
            ParkingArea parkingArea = parkingAreaService.findParkingAreaByLocation(event.getPoint());
            if (parkingArea != null && parkingArea.getId()
                .equals(parkedVehicle.getParkingArea().getId())) { // handle parking area not found by different way
                Status verificationResultStatus = permitVerificationService.verify(parkedVehicle, parkingArea);
                if (verificationResultStatus == UNPAID) {
                    fineService.reportViolation(parkedVehicle);
                }
                parkedVehicle.setStatus(verificationResultStatus);
                parkedVehicle.setLastTimeSpotted(event.getTimestamp());
                RouteCycle routeCycle = routeCycleService.findByCycleNumber(event.getCycle());
                parkedVehicle.setRouteCycle(routeCycle);
                parkedVehicleService.save(parkedVehicle);
            } else {
                ParkedVehicle parkedVehicleOnOtherLocation = createParkedVehicle(event);
                parkedVehicleService.save(parkedVehicleOnOtherLocation);
                parkedVehicleService.delete(parkedVehicle.getId());
            }
        }
    }

    private boolean isFined(ParkedVehicle parkedVehicle, Instant timestamp) {
        return parkedVehicle.getStatus() == UNPAID &&
            is24HoursBetweenDates(parkedVehicle.getLastTimeSpotted(), timestamp);
    }

    private boolean is24HoursBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(24)));
    }

    private boolean isBetweenDates(Instant firstDate, Instant secondDate, Duration duration) {
        return secondDate.isAfter(firstDate.plus(duration));
    }

    private ParkedVehicle createParkedVehicle(Event event) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(event.getTimestamp());
        parkedVehicle.setLicensePlate(event.getLicensePlate());
        parkedVehicle.setLatitude(event.getPoint().getY());
        parkedVehicle.setLongitude(event.getPoint().getX());
        ParkingArea parkingArea = parkingAreaService.findParkingAreaByLocation(event.getPoint());
        parkedVehicle.setParkingArea(parkingArea);
        RouteCycle routeCycle = routeCycleService.findByCycleNumber(event.getCycle());
        parkedVehicle.setRouteCycle(routeCycle);
        return parkedVehicle;
    }
}