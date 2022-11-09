package com.parking.pes.controller;

import com.parking.pes.dto.EventDto;
import com.parking.pes.model.ParkingPolygon;
import com.parking.pes.repository.ParkingPolygonRepository;
import com.parking.pes.service.EventService;
import javax.validation.Valid;
import org.locationtech.jts.geom.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/events")
public class EventController {

    private EventService eventService;
    private ParkingPolygonRepository parkingPolygonRepository;

    public EventController(EventService eventService,
                           ParkingPolygonRepository parkingPolygonRepository) {
        this.eventService = eventService;
        this.parkingPolygonRepository = parkingPolygonRepository;
    }

    @PostMapping
    public EventDto handleEvent(@Valid @RequestBody EventDto eventDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            eventService.handleEvent(eventDto);
        }
        return eventDto;
    }

    @GetMapping("/{lat}/{lon}")
    public ParkingPolygon getPointEntity(@PathVariable Double lat, @PathVariable Double lon) {
        //List<ParkingPolygon> pointEntities = pointEntityRepository.findAll();
        //Coordinate[] coordinates = new Coordinate[5];
        //coordinates[0] = new Coordinate(1, 2); // Starting point
        //coordinates[1] = new Coordinate(2, 20);
        //coordinates[2] = new Coordinate(20, 36);
        //coordinates[3] = new Coordinate(36, 100);
        //coordinates[4] = new Coordinate(1, 2); // Ending point
        //LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
        //Polygon poly = new Polygon(linear, null, new GeometryFactory());
        //ParkingPolygon parkingPolygon = new ParkingPolygon();
        //parkingPolygon.setId(5);
        //parkingPolygon.setMpolygon(poly);
        //pointEntityRepository.save(parkingPolygon);

        Point p = new GeometryFactory().createPoint(new Coordinate(lat, lon));
        return parkingPolygonRepository.findParkingPolygonByPoint(lat, lon);
    }

}
