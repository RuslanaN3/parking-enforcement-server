package com.parking.service.impl;

import static com.parking.dto.Status.*;
import static com.parking.util.TimeUtils.isBetweenDates;

import com.parking.dto.ParkedVehicleDto;
import com.parking.dto.ParkedVehicleFilter;
import com.parking.dto.Status;
import com.parking.dto.VerificationResult;
import com.parking.model.*;
import com.parking.repository.ParkedVehicleRepository;
import com.parking.service.ParkedVehicleService;
import com.parking.service.ParkingAreaService;
import com.parking.service.PermitVerificationService;
import com.parking.service.RouteCycleService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ParkedVehicleServiceImpl implements ParkedVehicleService {
    private static final Logger logger = LogManager.getLogger(ParkedVehicleServiceImpl.class);

    private ParkedVehicleRepository parkedVehicleRepository;
    private ParkingAreaService parkingAreaService;
    private PermitVerificationService permitVerificationService;
    private RouteCycleService routeCycleService;

    public ParkedVehicleServiceImpl(ParkedVehicleRepository parkedVehicleRepository,
                                    ParkingAreaService parkingAreaService,
                                    PermitVerificationService permitVerificationService,
                                    RouteCycleService routeCycleService) {
        this.parkedVehicleRepository = parkedVehicleRepository;
        this.parkingAreaService = parkingAreaService;
        this.permitVerificationService = permitVerificationService;
        this.routeCycleService = routeCycleService;
    }

    @Override
    public List<ParkedVehicleDto> getParkedVehicles() {
        return parkedVehicleRepository.findAll().stream()
            .map(this::convertToParkedVehicleDto)
            .collect(Collectors.toList());
    }

    @Override
    public ParkedVehicleDto getParkedVehicle(Long id) {
        Optional<ParkedVehicle> parkedVehicle = parkedVehicleRepository.findById(id);
        if (parkedVehicle.isEmpty()) {
            throw new NoSuchElementException();
        }
        return convertToParkedVehicleDto(parkedVehicle.get());
    }

    @Override
    public List<ParkedVehicleDto> searchParkedVehicles(ParkedVehicleFilter parkedVehicleFilter) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setLicensePlate(parkedVehicleFilter.getLicensePlate());
        parkedVehicle.setFirstTimeSpotted(parkedVehicleFilter.getFirstTimeSpotted());
        parkedVehicle.setLastTimeSpotted(parkedVehicleFilter.getLastTimeSpotted());
        parkedVehicle.setStatus(parkedVehicleFilter.getStatus());
        RouteCycle routeCycle = new RouteCycle();
        routeCycle.setCycleNumber(parkedVehicleFilter.getRouteCycle());
        parkedVehicle.setRouteCycle(routeCycle);

        ExampleMatcher parkedVehicleMatcher = ExampleMatcher.matchingAll()
            .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
            .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

        List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAll(Example.of(parkedVehicle, parkedVehicleMatcher));

        return parkedVehicles.stream().map(this::convertToParkedVehicleDto).collect(Collectors.toList());
    }

    public boolean existsParkedVehicle(String licensePlate) {
        return parkedVehicleRepository.existsParkedVehicleByLicensePlate(licensePlate);
    }

    public ParkedVehicle find(String licensePlate, Status status) {
        return parkedVehicleRepository.findByLicensePlateAndStatus(licensePlate, status);
    }

    public void save(ParkedVehicle parkedVehicle) {
        parkedVehicleRepository.save(parkedVehicle);
    }

    public void processParkedVehicle(Event event) {
        ParkedVehicle parkedVehicle = parkedVehicleRepository.findByLicensePlate(event.getLicensePlate());
        if (parkedVehicle == null) {
            logger.info("Parked vehicle not found, creating new for {}", event.getLicensePlate());
            createParkedVehicle(event);
            return;
        }

        if (isStatusStarted(parkedVehicle, event.getTimestamp()) || parkedVehicle.getStatus().equals(PAID)) {
            processExistingParkedVehicle(parkedVehicle, event);
        }
    }

    private void processExistingParkedVehicle(ParkedVehicle parkedVehicle, Event event) {
        ParkingArea parkingArea = parkingAreaService.getParkingAreaByLocation(event.getPoint());
        if (!parkedVehicle.getParkingArea().getId().equals(parkingArea.getId())) {
            parkedVehicleRepository.delete(parkedVehicle);
            createParkedVehicle(event);
            return;
        }
        VerificationResult verificationResult = permitVerificationService.verify(parkedVehicle);
        parkedVehicle.setStatus(verificationResult.getStatus());
        parkedVehicle.setLastTimeSpotted(event.getTimestamp());
        RouteCycle routeCycle = routeCycleService.findByCycleNumber(event.getCycle());
        parkedVehicle.setRouteCycle(routeCycle);
        parkedVehicleRepository.save(parkedVehicle);
    }

    private ParkedVehicle createParkedVehicle(Event event) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(event.getTimestamp());
        parkedVehicle.setLicensePlate(event.getLicensePlate());
        parkedVehicle.setLatitude(event.getPoint().getY());
        parkedVehicle.setLongitude(event.getPoint().getX());
        ParkingArea parkingArea = parkingAreaService.getParkingAreaByLocation(event.getPoint());
        parkedVehicle.setParkingArea(parkingArea);
        RouteCycle routeCycle = routeCycleService.findByCycleNumber(event.getCycle());
        parkedVehicle.setRouteCycle(routeCycle);
        return parkedVehicleRepository.save(parkedVehicle);
    }

    private boolean isStatusStarted(ParkedVehicle parkedVehicle, Instant timestamp) {
        return parkedVehicle.getStatus() == STARTED &&
            isBetweenDates(parkedVehicle.getFirstTimeSpotted(), timestamp, Duration.ofMinutes(15));
    }

    private ParkedVehicleDto convertToParkedVehicleDto(ParkedVehicle parkedVehicle) {
        ParkedVehicleDto parkedVehicleDto = new ParkedVehicleDto();
        parkedVehicleDto.setLicensePlate(parkedVehicle.getLicensePlate());
        parkedVehicleDto.setFirstTimeSpotted(parkedVehicle.getFirstTimeSpotted());
        parkedVehicleDto.setLastTimeSpotted(parkedVehicle.getLastTimeSpotted());
        parkedVehicleDto.setStatus(parkedVehicle.getStatus());
        parkedVehicleDto.setLongitude(parkedVehicle.getLongitude());
        parkedVehicleDto.setLatitude(parkedVehicle.getLatitude());
        return parkedVehicleDto;
    }
}
