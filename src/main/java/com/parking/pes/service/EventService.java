package com.parking.pes.service;

import com.parking.pes.dto.EventDto;
import com.parking.pes.model.Event;
import java.util.List;

public interface EventService {
    List<Event> findAll();
    void processEvent(EventDto eventDto);
}
