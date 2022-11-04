package com.parking.pes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.pes.dto.EventDto;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.EventService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventServiceIntegrationTest {
    private static final String PERMIT_SERVICE_URL = "https://permissionservice.com";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ParkedVehicleRepository parkedVehicleRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @AfterEach
    void clear() {
        eventRepository.deleteAll();
        parkedVehicleRepository.deleteAll();
    }

    @Test
    void testProcessEventsWhenMultipleParkedCars(
        @Value("classpath:tests-event-multiple-cars.json") Resource eventsResource) throws Exception {
        List<EventDto> eventDtos = objectMapper.readValue(eventsResource.getInputStream(), new TypeReference<>() {
        });

        PermitResponse permitResponse = new PermitResponse();
        permitResponse.setHasPermit(true);
        mockServer.expect(ExpectedCount.times(2),
            requestTo(PERMIT_SERVICE_URL))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(objectMapper.writeValueAsString(permitResponse), MediaType.APPLICATION_JSON));

        eventDtos.forEach(eventDto -> eventService.processEvent(eventDto));

        List<Event> events = eventService.findAll();
        assertNotNull(events);
        assertEquals(5, events.size());

        List<ParkedVehicle> parkedVehicles = parkedVehicleRepository.findAll();
        assertNotNull(parkedVehicles);
        assertEquals(3, parkedVehicles.size());

        ParkedVehicle firstParkedCar = parkedVehicleRepository.findByLicensePlate("ВС9096КЛ");
        assertEquals(eventDtos.get(0).getTimestamp(), firstParkedCar.getFirstTimeSpotted());
        assertEquals(Status.PAID, firstParkedCar.getStatus());

        ParkedVehicle secondParkedCar = parkedVehicleRepository.findByLicensePlate("АО5005КЛ");
        assertEquals(eventDtos.get(1).getTimestamp(), secondParkedCar.getFirstTimeSpotted());
        assertEquals(Status.PAID, secondParkedCar.getStatus());

        ParkedVehicle thirdParkedCar = parkedVehicleRepository.findByLicensePlate("АА3456НВ");
        assertEquals(eventDtos.get(2).getTimestamp(), thirdParkedCar.getFirstTimeSpotted());
        assertEquals(Status.STARTED, thirdParkedCar.getStatus());

    }
}