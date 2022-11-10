package com.parking.pes.service.impl;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.ParkedVehicleService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ParkedVehicleServiceImpl implements ParkedVehicleService {
    private static final Logger logger = LogManager.getLogger(ParkedVehicleServiceImpl.class);

    private ParkedVehicleRepository parkedVehicleRepository;

    public ParkedVehicleServiceImpl(ParkedVehicleRepository parkedVehicleRepository) {
        this.parkedVehicleRepository = parkedVehicleRepository;
    }

    public boolean existsParkedVehicle(String licensePlate) {
        return parkedVehicleRepository.existsParkedVehicleByLicensePlate(licensePlate);
    }

    public ParkedVehicle find(String licensePlate) {
        return parkedVehicleRepository.findByLicensePlate(licensePlate);
    }

    public ParkedVehicle find(String licensePlate, Status status) {
        return parkedVehicleRepository.findByLicensePlateAndStatus(licensePlate, status);
    }

    public void save(ParkedVehicle parkedVehicle) {
        parkedVehicleRepository.save(parkedVehicle);
    }

    public ParkedVehicle createFromEvent(Event event) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(event.getTimestamp());
        parkedVehicle.setLicensePlate(event.getLicensePlate());
        return parkedVehicleRepository.save(parkedVehicle);
    }

    //@Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    //public void deactivatePaidParkedVehicles() {
    //    logger.info("Paid parked vehicle deactivation started");
    //    List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAllByStatus(Status.PAID);
    //    parkedVehicles.stream()
    //        .filter(parkedVehicle -> isHourBetweenDates(parkedVehicle.getLastTimeSpotted(), Instant.now()))
    //        .forEach(parkedVehicle -> parkedVehicle.setDeactivated(true));
    //    parkedVehicleRepository.saveAll(parkedVehicles);
    //}
    //
    //@Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    //public void deactivateUnpaidParkedVehicles() {
    //    logger.info("Unpaid parked vehicle deactivation started");
    //    List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAllByStatus(Status.UNPAID);
    //    parkedVehicles.stream()
    //        .filter(parkedVehicle -> isHourBetweenDates(parkedVehicle.getLastTimeSpotted(), Instant.now()))
    //        .forEach(parkedVehicle -> parkedVehicle.setDeactivated(true));
    //    parkedVehicleRepository.saveAll(parkedVehicles);
    //}

    private boolean isHourBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(1)));
    }
}
