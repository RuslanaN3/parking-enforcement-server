package com.parking.pes.service.impl;

import static com.parking.pes.model.Status.*;

import com.parking.pes.dto.EventDto;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.EventService;
import com.parking.pes.service.FineService;
import com.parking.pes.service.PermitService;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;
    private ParkedVehicleRepository parkedVehicleRepository;
    private PermitService permitService;
    private FineService fineService;

    private double minAnprConfidenceValue;

    public EventServiceImpl(EventRepository eventRepository, ParkedVehicleRepository parkedVehicleRepository,
                            PermitService permitService, FineService fineService,
                            @Value("${anpr.minConfidence}") double minAnprConfidenceValue) {
        this.eventRepository = eventRepository;
        this.parkedVehicleRepository = parkedVehicleRepository;
        this.minAnprConfidenceValue = minAnprConfidenceValue;
        this.permitService = permitService;
        this.fineService = fineService;
    }

    @Override
    @Transactional
    public void processEvent(EventDto eventDto) {
        logger.info("Start event processing, received event : {}, {}", eventDto.getVehicleData().getLicensePlate(), eventDto.getCameraId());
        saveEvent(eventDto);

        String licensePlate = eventDto.getVehicleData().getLicensePlate();
        ParkedVehicle parkedVehicle = parkedVehicleRepository.findByLicensePlate(licensePlate);

        if (parkedVehicle == null) {
            ParkedVehicle newParkedVehicle = createParkedVehicle(eventDto);
            parkedVehicleRepository.save(newParkedVehicle);
            return;
        }

        PermitResponse permitResponse = permitService.checkPermit(licensePlate, eventDto.getLocation());
        if (permitResponse.hasPermit()) {
            parkedVehicle.setStatus(PAID);
        } else if (parkedVehicle.getStatus().equals(STARTED)) {
            parkedVehicle.setStatus(UNPAID);
            fineService.createFine(parkedVehicle);
        } else if (parkedVehicle.getStatus().equals(UNPAID) && is24HoursBetweenDates(parkedVehicle.getLastTimeSpotted(), eventDto.getTimestamp())) {
            fineService.createFine(parkedVehicle);
        }
        parkedVehicle.setLastTimeSpotted(eventDto.getTimestamp());
        parkedVehicleRepository.save(parkedVehicle);
    }


    private boolean is24HoursBetweenDates(Date firstDate, Date secondDate) {
        return secondDate.toInstant().isAfter(firstDate.toInstant().plus(Duration.ofHours(24)));
    }

    private void saveEvent(EventDto eventDto) {
        Event event = new Event();
        event.setTimestamp(eventDto.getTimestamp());
        event.setLongitude(eventDto.getLocation().getLongitude());
        event.setLatitude(eventDto.getLocation().getLatitude());
        event.setLicensePlate(eventDto.getVehicleData().getLicensePlate());
        event.setCameraId(eventDto.getCameraId());

        eventRepository.save(event);
    }

    private ParkedVehicle createParkedVehicle(EventDto eventDto) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(eventDto.getTimestamp());
        parkedVehicle.setLicensePlate(eventDto.getVehicleData().getLicensePlate());
        if (eventDto.getVehicleData().getConfidence() < minAnprConfidenceValue) {
            parkedVehicle.setStatus(NOT_DETECTED);
        }
        return parkedVehicle;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
