package com.parking.service;

import com.parking.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventProcessingService {
    private static final Logger logger = LogManager.getLogger(ParkingEventProcessingService.class);

    private ParkedVehicleService parkedVehicleService;
    private Double minLicensePlateConfidence;

    public ParkingEventProcessingService(ParkedVehicleService parkedVehicleService,
                                         @Value("${anpr.minConfidence}") Double minLicensePlateConfidence) {
        this.parkedVehicleService = parkedVehicleService;
        this.minLicensePlateConfidence = minLicensePlateConfidence;
    }

    public void processEvent(Event event) {
        if (event.getLicencePlateConfidence() < minLicensePlateConfidence) {
            logger.info("Low confidence value {} for license plate: {} at ", event.getLicencePlateConfidence(),
                event.getLicensePlate());
            return;
        }

        parkedVehicleService.processParkedVehicle(event);
    }
}
