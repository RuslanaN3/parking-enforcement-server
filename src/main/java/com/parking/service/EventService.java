package com.parking.service;

import com.parking.dto.EventDto;
import com.parking.dto.EventFilter;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto getEvent(Long id);

    List<EventDto> getEvents();

    List<EventDto> getEventsByLicensePlate(String licenseNumber);

    List<EventDto> getEventsFiltered(EventFilter eventFilter);

    List<EventDto> getEventsBetweenDates(LocalDateTime from, LocalDateTime to);

    void handleEvent(EventDto eventDto);

}
