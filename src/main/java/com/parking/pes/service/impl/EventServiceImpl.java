package com.parking.pes.service.impl;

import com.parking.pes.dto.EventDto;
import com.parking.pes.model.Event;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.service.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class);

    private ParkingEventProcessingService parkingEventProcessingService;
    private EventRepository eventRepository;
    private Double minAnprConfidenceValue;

    public EventServiceImpl(EventRepository eventRepository,
                            ParkingEventProcessingService parkingEventProcessingService,
                            @Value("${anpr.minConfidence}") double minAnprConfidenceValue) {
        this.eventRepository = eventRepository;
        this.minAnprConfidenceValue = minAnprConfidenceValue;
        this.parkingEventProcessingService = parkingEventProcessingService;
    }

    @Override
    public void handleEvent(EventDto eventDto) {
        logger.info("Start event processing, received event : {}, {}", eventDto.getVehicleData().getLicensePlate(),
            eventDto.getCameraId());
        Event event = createEvent(eventDto);
        saveEvent(event);
        parkingEventProcessingService.processEvent(event);
    }

    private void saveEvent(Event event) {
        eventRepository.save(event);
    }

    private Event createEvent(EventDto eventDto) {
        Event event = new Event();
        event.setTimestamp(eventDto.getTimestamp());
        event.setLongitude(eventDto.getLocation().getLongitude());
        event.setLatitude(eventDto.getLocation().getLatitude());
        event.setLicensePlate(eventDto.getVehicleData().getLicensePlate());
        event.setCameraId(eventDto.getCameraId());
        return event;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
