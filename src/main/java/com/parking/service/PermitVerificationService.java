package com.parking.service;

import static com.parking.dto.Status.*;

import com.parking.dto.VerificationResult;
import com.parking.model.ParkingArea;
import com.parking.dto.PermitResponse;
import com.parking.model.ParkedVehicle;
import com.parking.model.ParkingAreaType;
import com.parking.dto.Status;
import org.springframework.stereotype.Service;

@Service
public class PermitVerificationService {
    private PermitService permitService;
    private FineService fineService;

    public PermitVerificationService(PermitService permitService, FineService fineService) {
        this.permitService = permitService;
        this.fineService = fineService;
    }

    public VerificationResult verify(ParkedVehicle parkedVehicle) {
        ParkingArea parkingArea = parkedVehicle.getParkingArea();
        if (parkingArea.getAreaType().equals(ParkingAreaType.PROHIBITED)) {
            fineService.reportViolation(parkedVehicle);
            return new VerificationResult(ILLEGAL_PARKING);
        }

        Status paymentStatus = verifyPermit(parkedVehicle);
        if (paymentStatus == UNPAID) {
            fineService.reportViolation(parkedVehicle);
            return new VerificationResult(UNPAID);
        }
        return new VerificationResult(PAID);
    }

    private Status verifyPermit(ParkedVehicle parkedVehicle) {
        PermitResponse permitResponse = permitService.checkPermit(parkedVehicle.getLicensePlate(), parkedVehicle.getParkingArea().getAddress());
        if (permitResponse.hasPermit()) {
            return PAID;
        } else return UNPAID;
    }

}
