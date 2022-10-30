package com.parking.pes.service.impl;

import com.parking.pes.dto.Location;
import com.parking.pes.service.PermitService;
import org.springframework.stereotype.Service;

@Service
public class PermitServiceImpl implements PermitService {


    @Override
    public boolean checkPermission(String licensePlate, Location location) {
        return false;
    }
}
