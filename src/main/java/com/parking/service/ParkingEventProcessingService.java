package com.parking.service;

import static com.parking.model.Status.*;

import com.parking.model.Event;
import com.parking.model.ParkingArea;
import com.parking.model.ParkedVehicle;
import com.parking.model.Status;
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
    private ParkingAreaService parkingAreaService;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService,
                                         ParkingVerificationService parkingVerificationService,
                                         FineService fineService,
                                         @Value("${anpr.minConfidence}") Double minLicensePlateConfidence,
                                         ParkingAreaService parkingAreaService) {
        this.parkedVehicleService = parkedVehicleService;
        this.parkingVerificationService = parkingVerificationService;
        this.fineService = fineService;
        this.minLicensePlateConfidence = minLicensePlateConfidence;
        this.parkingAreaService = parkingAreaService;
    }

    public void processEvent(Event event) {
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
        // for STARTED we know it is parked in this area
        ParkingArea parkingArea = parkingAreaService.findParkingAreaByLocation(event.getPoint());

        if (parkingArea != null && parkingArea.getId().equals(parkedVehicle.getParkingAreaId())) {
            Status verificationResultStatus = parkingVerificationService.verify(parkedVehicle, parkingArea);
            if (verificationResultStatus == UNPAID && (parkedVehicle.getStatus() == STARTED || isFined(parkedVehicle, event.getTimestamp()))) {
                fineService.reportViolation(parkedVehicle);
            }
            parkedVehicle.setStatus(verificationResultStatus);
            parkedVehicle.setLastTimeSpotted(event.getTimestamp());
            parkedVehicleService.save(parkedVehicle);
        }
        else {
            ParkedVehicle parkedVehicle1 = createParkedVehicle(event);
            parkedVehicleService.save(parkedVehicle1);
            parkedVehicleService.delete(parkedVehicle.getId());
        }
    }

    private boolean isFined(ParkedVehicle parkedVehicle, Instant timestamp) {
        return parkedVehicle.getStatus() == UNPAID &&
            is24HoursBetweenDates(parkedVehicle.getLastTimeSpotted(), timestamp);
    }

    private boolean is24HoursBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(24)));
    }

    private ParkedVehicle createParkedVehicle(Event event) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(event.getTimestamp());
        parkedVehicle.setLicensePlate(event.getLicensePlate());
        parkedVehicle.setLatitude(event.getPoint().getY());
        parkedVehicle.setLongitude(event.getPoint().getX());
        ParkingArea parkingArea = parkingAreaService.findParkingAreaByLocation(event.getPoint());
        parkedVehicle.setParkingAreaId(parkingArea.getId());
        return parkedVehicle;
    }
}
