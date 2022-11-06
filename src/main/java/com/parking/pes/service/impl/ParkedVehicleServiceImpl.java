package com.parking.pes.service.impl;

import com.parking.pes.model.Event;
import com.parking.pes.model.ParkedVehicle;
import com.parking.pes.model.Status;
import com.parking.pes.repository.ParkedVehicleRepository;
import com.parking.pes.service.ParkedVehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkedVehicleServiceImpl implements ParkedVehicleService {
    private ParkedVehicleRepository parkedVehicleRepository;

    public ParkedVehicleServiceImpl(ParkedVehicleRepository parkedVehicleRepository) {
        this.parkedVehicleRepository = parkedVehicleRepository;
    }

    public boolean existsParkedVehicle(String licensePlate) {
        return parkedVehicleRepository.existsParkedVehicleByLicensePlate(licensePlate);
    }

    public ParkedVehicle find(String licensePlate) {
        return parkedVehicleRepository.findByLicensePlate(licensePlate);
    }

    public void save(ParkedVehicle parkedVehicle) {
        parkedVehicleRepository.save(parkedVehicle);
    }

    public ParkedVehicle createFromEvent(Event event, Double minLicensePlateConfidence) {
        ParkedVehicle parkedVehicle = new ParkedVehicle();
        parkedVehicle.setFirstTimeSpotted(event.getTimestamp());
        parkedVehicle.setLicensePlate(event.getLicensePlate());
        if (event.getLicencePlateConfidence() < minLicensePlateConfidence) {
            parkedVehicle.setStatus(Status.LOW_CONFIDENCE);
        }
        return parkedVehicleRepository.save(parkedVehicle);
    }
}
