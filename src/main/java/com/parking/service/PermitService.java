package com.parking.service;

import com.parking.dto.PermitResponse;
import com.parking.model.Address;

public interface PermitService {

    PermitResponse checkPermit(String licensePlate, Address parkingAreaAddress);

}
