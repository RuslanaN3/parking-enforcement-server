//package com.parking.pes.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.parking.pes.model.Event;
//import com.parking.pes.model.ParkedVehicle;
//import com.parking.pes.model.Status;
//import java.time.Instant;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class ParkingEventProcessingServiceTest {
//    private static final double TEST_MIN_LP_CONFIDENCE = 0.85;
//    private static final String TEST_LICENSE_PLATE = "ВС9096КЛ";
//
//    @Mock
//    private ParkedVehicleService parkedVehicleService;
//    @Mock
//    private ParkingVerificationService parkingVerificationService;
//    @Mock
//    private FineService fineService;
//    @Captor
//    private ArgumentCaptor<ParkedVehicle> parkedVehicleArgumentCaptor;
//    private ParkingEventProcessingService parkingEventProcessingService;
//
//    @BeforeEach
//    void setUp() {
//        parkingEventProcessingService =
//            new ParkingEventProcessingService(parkedVehicleService, parkingVerificationService, fineService,
//                TEST_MIN_LP_CONFIDENCE);
//    }
//
//    @Test
//    void testProcessEventWhenIsNotParkedCar() {
//        Event event = createEvent(TEST_LICENSE_PLATE, "2022-11-11T10:06:00.00Z", 0.95);
//        String licensePlate = event.getLicensePlate();
//        when(parkedVehicleService.find(licensePlate)).thenReturn(null);
//
//        parkingEventProcessingService.processEvent(event);
//
//        verify(parkedVehicleService).createFromEvent(event, TEST_MIN_LP_CONFIDENCE);
//    }
//
//    @Test
//    void testProcessEventWhenParkedCarWithPermit() {
//        Event event = createEvent(TEST_LICENSE_PLATE, "2022-11-11T10:06:00.00Z", 0.95);
//        String licensePlate = event.getLicensePlate();
//        ParkedVehicle parkedVehicle = createParkedVehicle(TEST_LICENSE_PLATE, "2022-11-11T09:50:00.00Z");
//        when(parkedVehicleService.find(licensePlate)).thenReturn(parkedVehicle);
//        when(parkingVerificationService.verify(parkedVehicle, event)).thenReturn(Status.PAID);
//
//        parkingEventProcessingService.processEvent(event);
//
//        verify(parkedVehicleService, never()).createFromEvent(event, TEST_MIN_LP_CONFIDENCE);
//        verify(parkingVerificationService).verify(parkedVehicle, event);
//        verify(fineService, never()).reportViolation(parkedVehicle);
//        verify(parkedVehicleService).save(parkedVehicleArgumentCaptor.capture());
//
//        ParkedVehicle savedParkedVehicle = parkedVehicleArgumentCaptor.getValue();
//        assertNotNull(savedParkedVehicle);
//        assertEquals(licensePlate, savedParkedVehicle.getLicensePlate());
//        assertEquals(event.getTimestamp(), savedParkedVehicle.getLastTimeSpotted());
//        assertEquals(Status.PAID, savedParkedVehicle.getStatus());
//    }
//
//    @Test
//    void testProcessEventWhenParkedCarWithoutPermit() {
//        Event event = createEvent("ВС9096КЛ", "2022-11-11T10:06:00.00Z", 0.95);
//        String licensePlate = event.getLicensePlate();
//        ParkedVehicle parkedVehicle = createParkedVehicle(TEST_LICENSE_PLATE, "2022-11-11T09:50:00.00Z");
//        when(parkedVehicleService.find(licensePlate)).thenReturn(parkedVehicle);
//        when(parkingVerificationService.verify(parkedVehicle, event)).thenReturn(Status.UNPAID);
//
//        parkingEventProcessingService.processEvent(event);
//
//        verify(parkedVehicleService, never()).createFromEvent(event, TEST_MIN_LP_CONFIDENCE);
//        verify(parkingVerificationService).verify(parkedVehicle, event);
//        verify(fineService).reportViolation(parkedVehicle);
//        verify(parkedVehicleService).save(parkedVehicleArgumentCaptor.capture());
//
//        ParkedVehicle savedParkedVehicle = parkedVehicleArgumentCaptor.getValue();
//        assertNotNull(savedParkedVehicle);
//        assertEquals(licensePlate, savedParkedVehicle.getLicensePlate());
//        assertEquals(event.getTimestamp(), savedParkedVehicle.getLastTimeSpotted());
//        assertEquals(Status.UNPAID, savedParkedVehicle.getStatus());
//    }
//
//    @Test
//    void testProcessEventWhenParkedCarLPNotDetected() {
//        Event event = createEvent(TEST_LICENSE_PLATE, "2022-11-11T10:06:00.00Z", 0.75);
//        String licensePlate = event.getLicensePlate();
//        ParkedVehicle parkedVehicle = createParkedVehicle(TEST_LICENSE_PLATE, "2022-11-11T09:50:00.00Z");
//        when(parkedVehicleService.find(licensePlate)).thenReturn(parkedVehicle);
//
//        parkingEventProcessingService.processEvent(event);
//
//        verify(parkedVehicleService, never()).createFromEvent(event, TEST_MIN_LP_CONFIDENCE);
//        verify(parkingVerificationService, never()).verify(parkedVehicle, event);
//        verify(parkedVehicleService).save(parkedVehicleArgumentCaptor.capture());
//
//        ParkedVehicle savedParkedVehicle = parkedVehicleArgumentCaptor.getValue();
//        assertNotNull(savedParkedVehicle);
//        assertEquals(licensePlate, savedParkedVehicle.getLicensePlate());
//        assertEquals(event.getTimestamp(), savedParkedVehicle.getLastTimeSpotted());
//        assertEquals(Status.LOW_CONFIDENCE, savedParkedVehicle.getStatus());
//    }
//
//    private Event createEvent(String licensePlate, String timestamp, Double licencePlateConfidence) {
//        Event event = new Event();
//        event.setLicensePlate(licensePlate);
//        event.setLicencePlateConfidence(licencePlateConfidence);
//        event.setTimestamp(Instant.parse(timestamp));
//        event.setLatitude("24.029716");
//        event.setLongitude("49.839684");
//        event.setCameraId("cameraId");
//        return event;
//    }
//
//    private ParkedVehicle createParkedVehicle(String licensePlate, String timestamp) {
//        ParkedVehicle parkedVehicle = new ParkedVehicle();
//        parkedVehicle.setLicensePlate(licensePlate);
//        parkedVehicle.setFirstTimeSpotted(Instant.parse(timestamp));
//        return parkedVehicle;
//    }
//}