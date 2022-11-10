package com.parking.service;

import com.parking.model.ParkedVehicle;

public interface FineService {
    void reportViolation(ParkedVehicle parkedVehicle);
}
