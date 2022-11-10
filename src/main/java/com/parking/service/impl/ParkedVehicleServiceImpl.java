package com.parking.service.impl;

import com.parking.model.ParkedVehicle;
import com.parking.model.Status;
import com.parking.repository.ParkedVehicleRepository;
import com.parking.repository.ParkingAreaRepository;
import com.parking.service.ParkedVehicleService;
import java.time.Duration;
import java.time.Instant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ParkedVehicleServiceImpl implements ParkedVehicleService {
    private static final Logger logger = LogManager.getLogger(ParkedVehicleServiceImpl.class);

    private ParkedVehicleRepository parkedVehicleRepository;
    private ParkingAreaRepository parkingAreaRepository;

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

    //@Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    //public void deactivatePaidParkedVehicles() {
    //    logger.info("Paid parked vehicle deactivation started");
    //    List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAllByStatus(Status.PAID);
    //    parkedVehicles.stream()
    //        .filter(parkedVehicle -> isHourBetweenDates(parkedVehicle.getLastTimeSpotted(), Instant.now()))
    //        .forEach(parkedVehicle -> parkedVehicle.setResolved(true));
    //    parkedVehicleRepository.saveAll(parkedVehicles);
    //}
    //
    //@Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    //public void deactivateUnpaidParkedVehicles() {
    //    logger.info("Unpaid parked vehicle deactivation started");
    //    List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAllByStatus(Status.UNPAID);
    //    parkedVehicles.stream()
    //        .filter(parkedVehicle -> isHourBetweenDates(parkedVehicle.getLastTimeSpotted(), Instant.now()))
    //        .forEach(parkedVehicle -> parkedVehicle.setResolved(true));
    //    parkedVehicleRepository.saveAll(parkedVehicles);
    //}

    private boolean isHourBetweenDates(Instant firstDate, Instant secondDate) {
        return secondDate.isAfter(firstDate.plus(Duration.ofHours(1)));
    }

    public void delete(Integer parkedVehicleId) {
        parkedVehicleRepository.deleteById(parkedVehicleId);
    }
}
