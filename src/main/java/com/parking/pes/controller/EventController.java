package com.parking.pes.controller;

import com.parking.pes.dto.EventDto;
import com.parking.pes.service.EventService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/events")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventDto handleEvent(@Valid @RequestBody EventDto eventDto) {
        eventService.handleEvent(eventDto);
        return eventDto;
    }

}
