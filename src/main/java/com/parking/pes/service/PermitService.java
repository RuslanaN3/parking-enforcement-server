package com.parking.pes.service;

import com.parking.pes.dto.Location;
import com.parking.pes.dto.PermitResponse;

public interface PermitService {

    PermitResponse checkPermit(String licensePlate, Location location);

}
