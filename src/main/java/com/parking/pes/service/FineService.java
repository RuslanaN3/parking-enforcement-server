package com.parking.pes.service;

import com.parking.pes.model.ParkedVehicle;

public interface FineService {
    void reportViolation(ParkedVehicle parkedVehicle);
}
