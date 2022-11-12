package com.parking.service;

import static com.parking.model.Status.*;

import com.parking.model.ParkingArea;
import com.parking.dto.PermitResponse;
import com.parking.model.ParkedVehicle;
import com.parking.model.Status;
import org.springframework.stereotype.Service;

@Service
public class PermitVerificationService {
    private PermitService permitService;

    public PermitVerificationService(PermitService permitService) {
        this.permitService = permitService;
    }

    public Status verify(ParkedVehicle parkedVehicle, ParkingArea parkingArea) {
        PermitResponse permitResponse = permitService.checkPermit(parkedVehicle.getLicensePlate(), parkingArea.getAddress());
        if (permitResponse.hasPermit()) {
            return PAID;
        } else
            return UNPAID;
    }

}
