package com.parking.service.impl;

import com.parking.model.RouteCycle;
import com.parking.model.Event;
import com.parking.dto.EventDto;
import com.parking.model.ParkedVehicle;
import com.parking.repository.*;
import com.parking.service.ParkingEventProcessingService;
import com.parking.service.RouteCycleService;
import com.parking.util.GeometryUtils;
import com.parking.service.EventService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class);

    private ParkingEventProcessingService parkingEventProcessingService;
    private EventRepository eventRepository;
    private ParkedVehicleRepository parkedVehicleRepository;

    public EventServiceImpl(ParkingEventProcessingService parkingEventProcessingService,
                            EventRepository eventRepository,
                            ParkedVehicleRepository parkedVehicleRepository) {
        this.parkingEventProcessingService = parkingEventProcessingService;
        this.eventRepository = eventRepository;
        this.parkedVehicleRepository = parkedVehicleRepository;
    }

    @Override
    @Transactional
    public void handleEvent(EventDto eventDto) {
        logger.info("Start event processing, received event with license plate : {} at {}",
            eventDto.getVehicleData().getLicensePlate(),
            eventDto.getTimestamp());
        Event event = createEvent(eventDto);
        saveEvent(event);
        parkingEventProcessingService.processEvent(event);
        logger.info("Finished event processing for license plate : {}",
            eventDto.getVehicleData().getLicensePlate());
    }

    @Override
    public List<Event> getEvents() {
        return eventRepository.findAll();
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

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
