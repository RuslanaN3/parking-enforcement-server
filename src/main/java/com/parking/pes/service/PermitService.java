package com.parking.pes.service;

import com.parking.pes.dto.Location;

public interface PermitService {

    boolean checkPermission(String licensePlate, Location location);

}
