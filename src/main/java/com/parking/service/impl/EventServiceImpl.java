package com.parking.service.impl;

import com.parking.dto.EventFilter;
import com.parking.dto.Location;
import com.parking.dto.VehicleData;
import com.parking.model.Event;
import com.parking.dto.EventDto;
import com.parking.repository.*;
import com.parking.service.ParkingEventProcessingService;
import com.parking.service.RouteCycleService;
import com.parking.util.GeometryUtils;
import com.parking.service.EventService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class);

    private ParkingEventProcessingService parkingEventProcessingService;
    private RouteCycleService routeCycleService;
    private EventRepository eventRepository;

    public EventServiceImpl(ParkingEventProcessingService parkingEventProcessingService,
                            RouteCycleService routeCycleService,
                            EventRepository eventRepository) {
        this.parkingEventProcessingService = parkingEventProcessingService;
        this.routeCycleService = routeCycleService;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void handleEvent(EventDto eventDto) {
        logger.info("Start event processing, received event with license plate : {} at {}",
            eventDto.getVehicleData().getLicensePlate(),
            eventDto.getTimestamp());
        Event event = createEvent(eventDto);
        saveEvent(event);
        routeCycleService.createCycleIfNotPresent(event.getCycle());
        parkingEventProcessingService.processEvent(event);
        logger.info("Finished event processing for license plate : {}",
            eventDto.getVehicleData().getLicensePlate());
    }

    @Override
    public EventDto getEvent(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new NoSuchElementException();
        }
        return convertToEventDto(event.get());
    }

    @Override
    public List<EventDto> getEvents() {
        return eventRepository.findAll().stream()
            .sorted(Comparator.comparing(Event::getTimestamp).reversed())
            .map(this::convertToEventDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsBetweenDates(LocalDateTime from, LocalDateTime to) {
        return eventRepository.findAllByTimestampBetween(from.toInstant(ZoneOffset.UTC), to.toInstant(ZoneOffset.UTC))
            .stream()
            .sorted(Comparator.comparing(Event::getTimestamp).reversed())
            .map(this::convertToEventDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsByLicensePlate(String licensePlate) {
        return eventRepository.findAllByLicensePlate(licensePlate).stream()
            .map(this::convertToEventDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsFiltered(EventFilter eventFilter) {
        Event event = new Event();
        event.setCameraId(eventFilter.getCameraId());
        event.setCycle(eventFilter.getCycle());

        ExampleMatcher eventMatcher = ExampleMatcher.matchingAll()
            .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
            .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

        List<Event> events = eventRepository.findAll(Example.of(event, eventMatcher));
        return events.stream().map(this::convertToEventDto).collect(Collectors.toList());
    }

    private void saveEvent(Event event) {
        eventRepository.save(event);
    }

    private Event createEvent(EventDto eventDto) {
        Event event = new Event();
        event.setTimestamp(eventDto.getTimestamp());
        Point point =
            GeometryUtils.getPoint(eventDto.getLocation().getLongitude(), eventDto.getLocation().getLatitude());
        event.setPoint(point);
        event.setLicensePlate(eventDto.getVehicleData().getLicensePlate());
        event.setLicencePlateConfidence(eventDto.getVehicleData().getConfidence());
        event.setCameraId(eventDto.getCameraId());
        event.setCameraPosition(eventDto.getCameraPosition());
        event.setCycle(eventDto.getCycle());
        return event;
    }

    private EventDto convertToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setCameraId(event.getCameraId());
        eventDto.setCameraPosition(event.getCameraPosition());
        eventDto.setCycle(event.getCycle());

        Location location = new Location(event.getPoint().getX(), event.getPoint().getY());
        eventDto.setLocation(location);
        eventDto.setTimestamp(event.getTimestamp());

        VehicleData vehicleData = new VehicleData();
        vehicleData.setConfidence(event.getLicencePlateConfidence());
        vehicleData.setLicensePlate(event.getLicensePlate());
        eventDto.setVehicleData(vehicleData);

        return eventDto;
    }
}