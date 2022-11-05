package com.parking.pes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import com.parking.pes.dto.EventDto;
import com.parking.pes.dto.Location;
import com.parking.pes.dto.PermitResponse;
import com.parking.pes.dto.VehicleData;
import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.EventRepository;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.FineService;
import com.parking.pes.service.PermitService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private PermitService permitService;
    @Mock
    private FineService fineService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ParkedVehicleRepository parkedVehicleRepository;
    @Captor
    private ArgumentCaptor<ParkedVehicle> parkedVehicleArgumentCaptor;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, parkedVehicleRepository, permitService, fineService, 0.85);
    }

    @Test
    void testProcessEventWhenNotParkedCar() {
        EventDto eventDto = createEventDto("1636617600000", "ВС9096КЛ");
        String licensePlate = eventDto.getVehicleData().getLicensePlate();
        when(parkedVehicleRepository.findByLicensePlate(licensePlate)).thenReturn(null);

        eventService.processEvent(eventDto);

        verify(eventRepository).save(any(Event.class));
        verify(parkedVehicleRepository).findByLicensePlate(licensePlate);
        verify(parkedVehicleRepository).save(parkedVehicleArgumentCaptor.capture());
        verify(permitService, never()).checkPermit(anyString(), any());

        ParkedVehicle savedParkedVehicle = parkedVehicleArgumentCaptor.getValue();
        assertNotNull(savedParkedVehicle);
        assertEquals(licensePlate, savedParkedVehicle.getLicensePlate());
        assertEquals(eventDto.getTimestamp(), savedParkedVehicle.getFirstTimeSpotted());
        assertEquals(Status.STARTED, savedParkedVehicle.getStatus());
    }

    @Test
    void testProcessEventWhenParkedCar() {
        EventDto eventDto = createEventDto("1636619100000", "ВС9096КЛ");
        String licensePlate = eventDto.getVehicleData().getLicensePlate();
        ParkedVehicle parkedVehicle = createParkedVehicle(licensePlate, "1636619100000");
        when(parkedVehicleRepository.findByLicensePlate(eventDto.getVehicleData().getLicensePlate()))
            .thenReturn(parkedVehicle);
        when(permitService.checkPermit(eq(licensePlate), any())).thenReturn(new PermitResponse(true));

        eventService.processEvent(eventDto);

        verify(eventRepository).save(any(Event.class));
        verify(parkedVehicleRepository).findByLicensePlate(licensePlate);
        verify(fineService, never()).createFine(any(ParkedVehicle.class));
        verify(parkedVehicleRepository).save(parkedVehicleArgumentCaptor.capture());
        ParkedVehicle savedParkedVehicle = parkedVehicleArgumentCaptor.getValue();
        assertNotNull(savedParkedVehicle);
        assertEquals(licensePlate, savedParkedVehicle.getLicensePlate());
        assertEquals(eventDto.getTimestamp(), savedParkedVehicle.getLastTimeSpotted());
        assertEquals(Status.PAID, savedParkedVehicle.getStatus());
    }

    private EventDto createEventDto(String timestamp, String licensePlate) {
        EventDto eventDto = new EventDto();
        VehicleData vehicleData = new VehicleData();
        vehicleData.setConfidence(0.85);
        vehicleData.setLicensePlate(licensePlate);
        vehicleData.setImage("image");

        Location location = new Location();
        location.setLongitude("24.029716");
        location.setLatitude("49.839684");

        eventDto.setVehicleData(vehicleData);
        eventDto.setLocation(location);
        eventDto.setTimestamp(new Date(Long.parseLong(timestamp)));
        eventDto.setCameraId("cameraId");
        return eventDto;
    }

    private ParkedVehicle createParkedVehicle(String licensePlate, String timestamp) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setLicensePlate(licensePlate);
        parkedVehicle.setFirstTimeSpotted(new Date(Long.parseLong(timestamp)));
        return parkedVehicle;
    }
}