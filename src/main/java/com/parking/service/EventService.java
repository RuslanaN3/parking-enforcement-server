package com.parking.service;

import com.parking.dto.EventDto;
import com.parking.model.Event;
import java.util.List;

public interface EventService {
    List<Event> findAll();

    void handleEvent(EventDto eventDto);
}
