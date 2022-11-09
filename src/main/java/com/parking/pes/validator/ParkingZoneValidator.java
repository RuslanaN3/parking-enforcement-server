package com.parking.pes.validator;

import com.parking.pes.dto.Location;
import com.parking.pes.repository.ParkingPolygonRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ParkingZoneValidator implements ConstraintValidator<ParkingZone, Location> {
    private ParkingPolygonRepository parkingPolygonRepository;

    public ParkingZoneValidator(ParkingPolygonRepository parkingPolygonRepository) {
        this.parkingPolygonRepository = parkingPolygonRepository;
    }

    @Override
    public void initialize(ParkingZone constraintAnnotation) {
    }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        return location != null &&
            parkingPolygonRepository.findParkingPolygonByPoint(location.getLatitude(), location.getLongitude()) != null;
    }
}
