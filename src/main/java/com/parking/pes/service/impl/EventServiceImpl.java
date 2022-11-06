package com.parking.pes.service.impl;

import com.parking.pes.dto.EventDto;
import com.parking.pes.model.Event;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.service.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger logger = LogManager.getLogger(EventServiceImpl.class);

    private ParkingEventProcessingService parkingEventProcessingService;
    private EventRepository eventRepository;

    public EventServiceImpl(ParkingEventProcessingService parkingEventProcessingService,
                            EventRepository eventRepository) {
        this.parkingEventProcessingService = parkingEventProcessingService;
        this.eventRepository = eventRepository;
    }

    @Override
    public void handleEvent(EventDto eventDto) {
        logger.info("Start event processing, received event for license plate : {} at {}", eventDto.getVehicleData().getLicensePlate(),
            eventDto.getTimestamp());
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
        event.setLicencePlateConfidence(eventDto.getVehicleData().getConfidence());
        event.setCameraId(eventDto.getCameraId());
        return event;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
