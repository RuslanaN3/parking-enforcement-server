package com.parking.pes.service.impl;

import com.parking.pes.dto.EventDto;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.EventService;
import com.parking.pes.service.PermitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private ParkedVehicleRepository parkedVehicleRepository;
    private PermitService permitService;

    private double minAnprConfidenceValue;

    public EventServiceImpl(EventRepository eventRepository, ParkedVehicleRepository parkedVehicleRepository,
                            PermitService permitService, @Value("${anpr.minConfidence:0.75}") double minAnprConfidenceValue) {
        this.eventRepository = eventRepository;
        this.parkedVehicleRepository = parkedVehicleRepository;
        this.minAnprConfidenceValue = minAnprConfidenceValue;
        this.permitService = permitService;
    }

    @Override
    public void processEvent(EventDto eventDto) {
        saveEvent(eventDto);

        String licensePlate = eventDto.getVehicleData().getLicensePlate();
        ParkedVehicle parkedVehicle = parkedVehicleRepository.findByLicensePlate(licensePlate);
        if (parkedVehicle == null) {
            ParkedVehicle newParkedVehicle = createParkedVehicle(eventDto);
            parkedVehicleRepository.save(newParkedVehicle);
            return;
        }
        //
        boolean hasPermission = permitService.checkPermission(licensePlate, eventDto.getLocation());

        if (!hasPermission) {
            // generate postanova
        }


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
            parkedVehicle.setStatus(Status.NOT_DETECTED);
        }
        return parkedVehicle;
    }

}
