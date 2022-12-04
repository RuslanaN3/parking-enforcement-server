package com.parking.controller;

import com.parking.dto.EventDto;
import com.parking.dto.EventFilter;
import com.parking.service.EventService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/events")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public void handleEvent(@Valid @RequestBody EventDto eventDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            eventService.handleEvent(eventDto);
        }
    }

    @GetMapping
    public List<EventDto> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    @GetMapping("/vehicle/{licensePlate}")
    public List<EventDto> getEventsByLicensePlate(@PathVariable String licensePlate) {
        return eventService.getEventsByLicensePlate(licensePlate);
    }

    @PostMapping("/search")
    public List<EventDto> search(@RequestBody EventFilter eventFilter) {
        return eventService.searchEvents(eventFilter);
    }

    @GetMapping("/timestamp/{from}/to/{to}")
    public List<EventDto> getEventsBetweenDates(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return eventService.getEventsBetweenDates(from, to);
    }

    @PostMapping("multiple")
    public EventDto handleEvent(@Valid @RequestBody List<EventDto> eventDtos, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            eventDtos.sort(Comparator.comparing(EventDto::getCycle));
            eventDtos.forEach(eventDto -> eventService.handleEvent(eventDto));
        }
        return eventDtos.get(0);
    }

    //@GetMapping("/{lat}/{lon}")
    //public ParkingArea getPointEntity(@PathVariable Double lat, @PathVariable Double lon) {
    //    //List<ParkingArea> pointEntities = pointEntityRepository.findAll();
    //    //Coordinate[] coordinates = new Coordinate[5];
    //    //coordinates[0] = new Coordinate(1, 2); // Starting point
    //    //coordinates[1] = new Coordinate(2, 20);
    //    //coordinates[2] = new Coordinate(20, 36);
    //    //coordinates[3] = new Coordinate(36, 100);
    //    //coordinates[4] = new Coordinate(1, 2); // Ending point
    //    //LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
    //    //Polygon poly = new Polygon(linear, null, new GeometryFactory());
    //    //ParkingArea parkingPolygon = new ParkingArea();
    //    //parkingPolygon.setId(5);
    //    //parkingPolygon.setMpolygon(poly);
    //    //pointEntityRepository.save(parkingPolygon);
    //
    //    //Point p = new GeometryFactory().createPoint(new Coordinate(lat, lon));
    //    return parkingAreaRepository.findParkingAreaByPoint(lat, lon);
    //}

}
